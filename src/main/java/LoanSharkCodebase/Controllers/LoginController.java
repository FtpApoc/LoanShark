package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Singletons.AdminSingleton;
import LoanSharkCodebase.Singletons.DatabaseSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.ResultSet;

import java.io.IOException;

public class LoginController extends ControllerParent {

    //load from the FXML into the live handler
    @FXML private Label confirmationLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    protected void userLogin() throws IOException {
        confirmationLabel.setText("Unsuccessful Log In");
        checkLogin();
    }

    private void checkLogin() {
        DatabaseSingleton loginConnect = DatabaseSingleton.getInstance();
        AdminSingleton admin = AdminSingleton.getInstance();
        String connectQuery = "select * from loanshark.employee";

        try{
            ResultSet queryOutput = loginConnect.databaseQuery(connectQuery);

            while (queryOutput.next()){
                String QueryUsername = queryOutput.getString("employeeUsername");
                String QueryPassword = queryOutput.getString("employeePassword");
                Boolean QueryAdminBool = queryOutput.getBoolean("employeeAdminBool");

                if (usernameField.getText().equals(QueryUsername) && passwordField.getText().equals(QueryPassword)) {
                    // If database marks employee as admin, set admin as true in system
                    if (QueryAdminBool.equals(true)){
                        admin.enableAdminBool();
                    }
                    SendToHomepage();
                }
            }

        } catch (Exception e){
            throw new RuntimeException("unhandled", e);
        }
    }

    private void SendToHomepage() throws IOException {
        confirmationLabel.setText("Loading HomePage");

        callChangeScene("homePage.fxml");
    }
}

