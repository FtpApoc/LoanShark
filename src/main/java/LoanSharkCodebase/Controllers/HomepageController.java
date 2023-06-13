package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Singletons.AdminSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.HashMap;

public class HomepageController extends ControllerParent{


    @FXML private Button btnEmployeeReg;


    //used to both check and deactivate the Admin boolean when currently logged in
    AdminSingleton admin = AdminSingleton.getInstance();

    @FXML //This method will run on the loading of the page, and check whether the user is an admin
    public void initialize(){
        Boolean hasAdmin = admin.getAdminBool();

        if(hasAdmin){ // if so, the user is able to see the Register Employees Button
            btnEmployeeReg.setVisible(true);
        }
    }


    @FXML
    protected void LogOut() throws IOException {
        admin.disableAdminBool();
        callChangeScene("loginView.fxml");
    }


    @FXML
    protected void ButtonPressed(ActionEvent event) throws IOException {
        final Node btnNode = (Node) event.getSource();
        String btnName = btnNode.getId();
        sendToScene(btnName);
    }

    private void sendToScene(Object btnName) throws IOException {
        HashMap<String,String> SceneDestination = new HashMap<>();

                            //Which button , FXML file for that page
        SceneDestination.put("btnCustomers","customerSearch.fxml");
        SceneDestination.put("btnItemSearch","itemLibrary.fxml");
        SceneDestination.put("btnEmployeeReg","employeeRegister.fxml");
        SceneDestination.put("btnReturns","returnSearch.fxml");

        callChangeScene(SceneDestination.get(btnName));
    }
}
