package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Objects.Recommendations;
import LoanSharkCodebase.Singletons.CustomerSingleton;
import LoanSharkCodebase.Objects.CustomerLoans;
import LoanSharkCodebase.Singletons.DatabaseSingleton;
import LoanSharkCodebase.Singletons.LoanSingleton;
import LoanSharkCodebase.Singletons.ReturnSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerPageController extends ControllerParent {

    //Table and Columns set up in FXML
    @FXML TableView<CustomerLoans> tableView;
    @FXML TableColumn<CustomerLoans, String> itemNameCol;
    @FXML TableColumn<CustomerLoans, Date> dateDueCol;
    @FXML TableColumn<CustomerLoans, String> overDueCol;
    @FXML TableColumn returnItemCol;

    //Recommendatiion elemeents Set up for live handling
    @FXML private Label custIDtext;
    @FXML private Label custLoanNumber;
    @FXML private Label recItemName;

    //used by multiple methods
    CustomerSingleton customerInstance = CustomerSingleton.getInstance();
    DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();
    ArrayList<String> itemNames = new ArrayList<>();

    @FXML
    private void initialize() throws SQLException {
        ControllerParent.returnPage= "customerSearch.fxml";


        //populate table
        startTableview();
        recommendation();
    }

    private void startTableview() throws SQLException {

        //column 1, the item name column
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));

        //column 2, the Return Date column
        dateDueCol.setCellValueFactory(new PropertyValueFactory<>("DateDue"));

        //column 3, the overdue item column
        overDueCol.setCellValueFactory(new PropertyValueFactory<>("Overdue"));

        //column 4, the item return button column
        returnItemCol.setCellValueFactory(new PropertyValueFactory<>("returnButton"));

        //set table contents to getLoans
        tableView.getItems().setAll(getLoans());
    }

    //get all specific customer loans
    public ObservableList<CustomerLoans> getLoans() throws SQLException{


        //list used to store customer event objects
        ObservableList<CustomerLoans> tableEntries = FXCollections.observableArrayList();

        String customerID = customerInstance.getCustomerID();
        String customerQuery ="""
        Select loanevent.*, item.ItemName 
        from loanevent Inner join loanshark.item on loanevent.ItemID = item.ItemID
        where CustomerID =""" + "\"" + customerID + "\"";

        ResultSet customerOutput = databaseInstance.databaseQuery(customerQuery);

        //aquiring event information
        while(customerOutput.next()){
            String eventID = customerOutput.getString("EventID");
            String itemName = customerOutput.getString("ItemName");
            Date dateDue = customerOutput.getDate("ReturnDate");
            Date dateNow = Date.valueOf((LocalDate.now()));
            String overDue = null;
            if(dateNow.compareTo(dateDue) > 0 ) { //if date now is greater than stored date, item is late
                overDue = "Yes";
            } else {overDue = "No";} // Item is not overdue

            //for use in recommendations
            itemNames.add(itemName);

            //Used for the Button to send to results page
            ReturnSingleton returnInstance = ReturnSingleton.getInstance();

            //Create Return Item Button
            Button returnButton = new Button();
            returnButton.getStyleClass().add("ConfirmButton");
            returnButton.setText("Return Item");

            //give Action property to the button
            returnButton.setOnAction(new EventHandler(){
                @Override
                public void handle(Event event) {
                    try {
                        returnInstance.setReturnID(eventID);
                        callChangeScene("ReturnConformation.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            //add each matching event record to the tableEntries List
            tableEntries.add(new CustomerLoans(itemName,dateDue,overDue,eventID,returnButton));
        }
        //Return list to table view for display
        return tableEntries;
    }

    String matchItem = "";
    private void recommendation() throws SQLException {
        String customerID = customerInstance.getCustomerID();
        //writing information about the customer
        String custQuery = ("select * from loanshark.customer where CustomerID ="+"\"" + customerID + "\""+";");
        ResultSet queryOutput = databaseInstance.databaseQuery(custQuery);

        while(queryOutput.next()){
            custIDtext.setText(queryOutput.getString("CustomerID"));
            custLoanNumber.setText(queryOutput.getString("CurrentLoans"));
        }
        Recommendations recObject = new Recommendations(itemNames);
        matchItem = recObject.recommend();
        recItemName.setText(matchItem);

    }

    @FXML
    private void GoToRecPage() throws SQLException, IOException {
        //check if recommendation matchItem exists
        if(matchItem != ""){

            //get the ItemID of the recommended item from the String name matchItem above at 139
            String itemQuery = "Select ItemID from item where ItemName = "+"\"" + matchItem + "\""+";";
            ResultSet itemResult = databaseInstance.databaseQuery(itemQuery);


            while(itemResult.next()){
                String matchItemID = itemResult.getString("ItemID");

                //set up
                LoanSingleton loanInstance = LoanSingleton.getInstance();
                loanInstance.setLoanID(matchItemID);
                callChangeScene("itemPage.fxml");
            }
        }
        

    }
}
