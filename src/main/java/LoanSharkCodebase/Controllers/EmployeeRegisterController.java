package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Singletons.DatabaseSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

import java.sql.ResultSet;
import java.sql.SQLException;


public class EmployeeRegisterController extends ControllerParent {

    //load from the FXML into the live handler
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField conformationField;
    @FXML private Label confirmationLabel;
    @FXML private CheckBox adminCheckbox;

    @FXML
    private void initialize() {ControllerParent.returnPage = "homePage.fxml";
    }

    DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();

    //used in separate functions
    Integer employeeCount = 0;

    //check inputs are entered
    private boolean inputCheck(String[] userInputs){
    for(String input : userInputs){
        if(input == ""){
            return false;
            }
        } return (userInputs[1].equals(userInputs[2])); // will only return true if equal, otherwise will return false
    }

    private boolean uniqueEmployeeName(String name) throws SQLException {
        //call to DB to receive existing employees
        String EmployeeQuery = "select * from loanshark.employee";
        ResultSet queryOutput = databaseInstance.databaseQuery(EmployeeQuery);

        //only returns true if no records match user entries
        while (queryOutput.next()) {
            employeeCount += 1;
            String employeeUsername = queryOutput.getString("employeeUsername");
            if(employeeUsername.equals(name)){
                confirmationLabel.setText("Duplicate Employee Name");
                return false;
            }
        }  return true;
    }

    @FXML //OnAction for SearchButton in FXML
    protected void authenticateInputs() throws SQLException {
        String name = usernameField.getText();
        String password = passwordField.getText();
        String confPass = conformationField.getText();
        //generate an array of strings of user Inputs
        String[] userInputs = {name,password,confPass};

        //Default as false
        boolean authComplete = false;

        //return call to check inputs as entered
        boolean inputCheckBool = inputCheck(userInputs);

        if (inputCheckBool){
            confirmationLabel.setText(""); //done for secondary attempts with already shown labels

            //return call to authenticate if users registration is unique
            authComplete = uniqueEmployeeName(name);
            if(authComplete){
                addEmployee(userInputs); //call to add employee to DB
            }
        } else{
            confirmationLabel.setText("Missing/Incorrect Details");
        }

        }

    //Once Authenticated, Add employee to database
    private void addEmployee(String[] userInputs) throws SQLException {
        Integer employeeID = employeeCount +1;
        String employeeUsername =("\""+userInputs[0]+"\""); // must be surrounded in quotes for SQL detection as Strings
        String employeePassword = ("\""+userInputs[1]+"\"");
        Boolean employeeAdminBool = adminCheckbox.isSelected();

        //generating SQL request to send to database singleton using user inputs
        String query = String.format("""
        insert into loanshark.employee (employeeID,employeeUsername,EmployeePassword,EmployeeAdminBool)
        VALUES (%s,%s,%s,%s);""",employeeID,employeeUsername,employeePassword,employeeAdminBool);


        databaseInstance.databaseInsert(query);
        employeeCount = 0; //reset for next EmployeeNumber check with new employee
        confirmationLabel.setTextFill(Paint.valueOf("white")); // not red
        confirmationLabel.setText("employee added"); //confirm to user success


    }
}


