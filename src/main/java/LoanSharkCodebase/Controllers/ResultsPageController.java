package LoanSharkCodebase.Controllers;

import javafx.fxml.FXML;

public class ResultsPageController extends ControllerParent{

    @FXML
    private void initialize(){
        ControllerParent.returnPage= "itemLibrary.fxml";
    }
}
