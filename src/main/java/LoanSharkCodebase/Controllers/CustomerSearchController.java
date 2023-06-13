package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Singletons.CustomerSingleton;
import LoanSharkCodebase.Singletons.DatabaseSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class CustomerSearchController extends ControllerParent{

    //FXML elements to be edited as program runs
    @FXML private TextField searchField;
    @FXML private Label lblOperationFailed;
    @FXML private Label lblNewCustNum;
    @FXML private Label lblAddedToDB;


    @FXML
    private void initialize(){
        ControllerParent.returnPage= "homePage.fxml";
    }

    //activation of 2 singletons to get access to the database, and deposit information about the customer.
    DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();
    CustomerSingleton customerInstance = CustomerSingleton.getInstance();

    @FXML //OnAction for Search Button for FXML
    protected void customerSearch() throws SQLException, IOException {

        //Entry from User
        String custIDSearchText = searchField.getText();
        String customerQuery = "select * from loanshark.customer";

        //Ensuring TextField is populated for query
        if(custIDSearchText != ""){

            //call to the database singleton for customer information
          ResultSet queryOutput = databaseInstance.databaseQuery(customerQuery);

          while (queryOutput.next()){
            String compareID = queryOutput.getString("customerID");

            //if user text equals Database Entry, go to ID page
            if(custIDSearchText.equals(compareID)){
                customerInstance.setCustomerID(custIDSearchText);
                callChangeScene("customerPage.fxml");

            } else { // if user text does not match DB Entry
                lblOperationFailed.setText("Customer ID not found");
            }
          }
        }
    }

    @FXML //Generate a new Customer record in the database
    protected void addToDB(){
        Random rand = new Random();
        int custNumber = rand.nextInt(1000000);
        String custID = String.format("CU"+"%06d",custNumber);
        String custIDformatted = String.format("\""+custID+"\"");

        String custQuery = String.format("""
                Insert into loanshark.customer(CustomerID,CurrentLoans)
                Values(%s,0);
                """,custIDformatted);

        //add to database, display new ID and tell user the ID has been added
        databaseInstance.databaseInsert(custQuery);
        lblNewCustNum.setText(custID);
        lblAddedToDB.setText("Customer ID added to Database");
    }
}
