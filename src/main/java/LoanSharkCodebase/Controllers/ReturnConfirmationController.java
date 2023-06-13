package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Singletons.DatabaseSingleton;
import LoanSharkCodebase.Singletons.ReturnSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ReturnConfirmationController extends ControllerParent {

    //used to set visibility after Loan Returned
    @FXML private Rectangle rctSaveInFile;
    @FXML private Button btnSaveInFile;
    @FXML private Label lblSaveInFile;

    //Top Section
    @FXML private Label EvR; //Event Reference
    @FXML private Label CuR; //Customer Reference
    @FXML private Label ItR; //Item Reference
    @FXML private Label ItN; //Item Name

    //Middle Section
    @FXML private Label DtL; //Date Loaned
    @FXML private Label ToL; //Time Of Loan

    //Bottom Section
    @FXML private Label DtR; //Date of Latest Return
    @FXML private Label ItV; //Item Version
    @FXML private Label ItL; //Item Late
    @FXML private Label LtF; //Late Fee

    //Right Side
    @FXML private Label CtB; // Cost Breakdown
    @FXML private Label TlC; // Total Cost

    @FXML
    private void initialize() throws SQLException {
        ControllerParent.returnPage= "returnSearch.fxml";
        eventDetails();
    }

    //On load of event, get information on LoanEvent
    private void eventDetails() throws SQLException {
        String eventQuery = ("""
        Select loanevent.*, item.ItemName 
        from loanevent Inner join loanshark.item on loanevent.ItemID = item.ItemID
        where EventID =""" + "\"" + eventID + "\"" );
        ResultSet queryOutput = databaseInstance.databaseQuery(eventQuery);

        //used in wider scope than the query
        Date dateNow = Date.valueOf((LocalDate.now()));
        Date storeDtR = null;
        String storeLtF = null;

        while (queryOutput.next()) { //setting query results to page results or variables
            storeDtR = Date.valueOf(queryOutput.getString("ReturnDate"));
            storeLtF = queryOutput.getString("VersionFee");

            EvR.setText(eventID);
            CuR.setText(queryOutput.getString("CustomerID"));
            ItR.setText(queryOutput.getString("ItemID"));
            ItN.setText(queryOutput.getString("ItemName"));
            DtL.setText(queryOutput.getString("DateLoaned"));
            ToL.setText(queryOutput.getString("LoanLength") + " Months");
            DtR.setText(String.valueOf(storeDtR));
            ItV.setText(queryOutput.getString("ItemVersion"));
            LtF.setText(storeLtF + " Per Day");
        }

        if(dateNow.compareTo(storeDtR) > 0 ){ //if date now is greater than stored date, item is late
            ItL.setText("Yes");

            //difference is calculated in milliseconds using getTime and converted into days
            long diffInDate = Math.abs(dateNow.getTime() - storeDtR.getTime());
            String diffDays = String.valueOf(TimeUnit.DAYS.convert(diffInDate, TimeUnit.MILLISECONDS));

            //Final labels are calculated and set using this information
            CtB.setText(diffDays+" Days x £"+ storeLtF);
            Double doubleLtF = Double.parseDouble(storeLtF);
            Double doubleDiffDays = Double.parseDouble(diffDays);
            TlC.setText("£"+(doubleDiffDays * doubleLtF));
        } else {
            ItL.setText("No"); //if not overdue, CtB and TlC do not need to be changed
        };



    }
    //get instances of database interaction and able to getReturnID
    DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();
    ReturnSingleton returnInstance = ReturnSingleton.getInstance();
    String eventID = returnInstance.getReturnID();

    @FXML
    private void returnItem() throws SQLException {
        //used to get the full customer record for each loan event
        String shuffleQuery = """
                SELECT customer.*, loanevent.*
                from loanevent inner join customer on loanevent.CustomerID = customer.CustomerID
                where EventID = """+"\"" + eventID + "\"";

        ArrayList<String> events = new ArrayList<>();
        Integer eventCount = null;
        String customerID = null;
        String itemID = null;
        String itemVersion = null;

        ResultSet shuffleOutput = databaseInstance.databaseQuery(shuffleQuery);

        while(shuffleOutput.next()){ //set up to create a new set of active LoanEvents
            customerID = shuffleOutput.getString("CustomerID");
            eventCount = shuffleOutput.getInt("CurrentLoans");
            itemVersion = shuffleOutput.getString("ItemVersion");
            itemID = shuffleOutput.getString("ItemID");
            for(int i = 0; i < eventCount; i++){

                //search database for loan events belonging to the customer
               String eventNumberIncrement = "EventNumber"+(i+1);
               String eventIncOutput = shuffleOutput.getString(eventNumberIncrement);

               //if not the removed event, add to the array
               if(!eventIncOutput.equals(eventID) ){events.add(eventIncOutput);}
            }
        }

        //add a change to remove the spot which will not be needed with 1 less entry, and lower active loan count
        String removeExtraEvent =  "EventNumber"+eventCount +"= null, CurrentLoans = "+(eventCount-1);



        String customerLoanEvents = "";
        for(int i = 0; i < events.size(); i++){
            if(!events.isEmpty()){ //for each event in a customer record, add to an array
                customerLoanEvents = "SET "+customerLoanEvents +"EventNumber"+(i+1)+"= "+ "\""+events.get(i)+"\""+",";
            }

        }

        //incrementing the correct version
        HashMap<String,String> itemVersionMap = new HashMap<>();
        itemVersionMap.put("Standard","movie");
        itemVersionMap.put("DirCut","movie");
        itemVersionMap.put("Paperback","book");
        itemVersionMap.put("Hardback","book");
        itemVersionMap.put("Audiobook","book");

        String itemType = itemVersionMap.get(itemVersion);
        String versionAvailable = itemVersion+"Available";
        String versionAvailabilityQuery = "Update loanshark."+itemType+" SET "+versionAvailable+" = "+versionAvailable+" + 1 where ItemID = "+"\""+itemID+"\"" ;


        //combine all the elements of the query to form a remove, reshuffle and adjustment
        customerLoanEvents = customerLoanEvents + removeExtraEvent;

        //send query to re-order the customer table to remove the record, and sort remaining records into highest slots
        String customerRequest = "UPDATE loanshark.customer "+customerLoanEvents+" where CustomerID = "+"\"" + customerID + "\""+";";

        databaseInstance.databaseInsert(customerRequest);

        //increment the stock of the newly returned item in the item table
        String availabilityQuery = "UPDATE loanshark.item SET CopiesAvailable = CopiesAvailable + 1 Where ItemID = "+"\"" + itemID + "\""+";";
        databaseInstance.databaseInsert(availabilityQuery);

        databaseInstance.databaseInsert(versionAvailabilityQuery);

        //Delete the record from the loan event table
        String removeQuery = "DELETE FROM loanshark.loanevent WHERE EventID = "+"\"" + eventID + "\"";
        databaseInstance.databaseInsert(removeQuery);

        //Show the success of returning, and allow a receipt file to be made
        rctSaveInFile.setVisible(true);
        btnSaveInFile.setVisible(true);
        lblSaveInFile.setVisible(true);
    }

    @FXML
    private void saveInFile(){

        //the receipt text using Event properties shown in the results screen
        String receiptText = String.format("""
        Event ID: %s
        Customer Reference: %s
        Item Reference: %s
        Item Name: %s
        
        Breakdown Of Cost: %s
        Total Cost: %s
        """,EvR.getText(), CuR.getText(),ItR.getText(),ItN.getText(),CtB.getText(),TlC.getText());

        try{ //if unique, create a text file to put the receipt into
            File returnReceipt = new File("ReturnReceipt.txt");
            if(returnReceipt.createNewFile()){
                lblSaveInFile.setText("Receipt Created");
            }else{
                lblSaveInFile.setText("Existing Receipt");
            }

            //write the receiptText into the newly created file
            FileWriter receiptWriter = new FileWriter("ReturnReceipt.txt");
            receiptWriter.write(receiptText);
            receiptWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }


}