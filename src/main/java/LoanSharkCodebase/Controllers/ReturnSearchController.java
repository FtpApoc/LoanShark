package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Singletons.DatabaseSingleton;
import LoanSharkCodebase.Singletons.ReturnSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnSearchController extends ControllerParent{

    @FXML private Label lblOperationFailed;
    @FXML private TextField eventTextField;

    @FXML
    private void initialize(){
        ControllerParent.returnPage= "homePage.fxml";
    }

    //get instances of database interaction and able to setReturnID
    DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();
    ReturnSingleton returnInstance = ReturnSingleton.getInstance();


    @FXML
    private void eventIDSearch() throws SQLException, IOException {
         String eventText = eventTextField.getText();
         String eventQuery = "select * from loanshark.loanevent";

        if(eventText != ""){
            ResultSet queryOutput = databaseInstance.databaseQuery(eventQuery);

            while(queryOutput.next()){ // if entered EventID matches one in record, go to that page
                String compareID = queryOutput.getString( "EventID");
                if(compareID.equals(eventText)){
                    returnInstance.setReturnID(eventText);
                    callChangeScene("returnConformation.fxml");
                } else {
                    lblOperationFailed.setText("Incorrect Event ID");
                }
            }

        } else{ //if text != "" is not satisfied
            lblOperationFailed.setText("Event ID not entered");
        }
    }
}
