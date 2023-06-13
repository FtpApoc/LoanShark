package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Main;
import javafx.fxml.FXML;

import java.io.IOException;

public class ControllerParent {

    //This is used on initialization of each page to set the previous page
    protected static String returnPage;

    @FXML // return to previous page
    protected void GoBack() throws IOException {
        callChangeScene(returnPage);
    }

    //Replace current scene with new given scene
    protected void callChangeScene(String scene) throws IOException {
        Main m = new Main();
        m.changeScene(scene);
    }
}
