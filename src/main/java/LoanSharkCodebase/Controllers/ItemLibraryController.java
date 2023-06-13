package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Objects.ItemObject;
import LoanSharkCodebase.Singletons.DatabaseSingleton;
import LoanSharkCodebase.Singletons.LoanSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemLibraryController extends ControllerParent {

    @FXML TableView<ItemObject> tableView;
    @FXML TableColumn<ItemObject, String> itemNameCol;
    @FXML TableColumn<ItemObject, Integer> copiesAvCol;
    @FXML TableColumn<ItemObject, String[]> genreCol;
    @FXML TableColumn<ItemObject, Integer> readLevCol;
    @FXML TableColumn viewItemPage;

    @FXML TextField searchBar;

    @FXML
    private void initialize() throws SQLException {
        ControllerParent.returnPage = "homePage.fxml";
        //Populate Table
        String itemQuery = "Select * from loanshark.item";
        populateTable(itemQuery);
    }

    private void populateTable(String itemQuery) throws SQLException {
        //Column 1, Name
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));

        //Column 2, Copies Available
        copiesAvCol.setCellValueFactory(new PropertyValueFactory<>("Copies"));

        //Column 3, Genre of item
        genreCol.setCellValueFactory(new PropertyValueFactory<>("Genre"));

        //Column 4, reading level
        readLevCol.setCellValueFactory(new PropertyValueFactory<>("ReadLevel"));

        //Column 5, Item Page
        viewItemPage.setCellValueFactory(new PropertyValueFactory<>("BtnViewItemPage"));

        //setting table contents to getItemObjects

        tableView.getItems().setAll(getItemObjects(itemQuery));

    }

    public ObservableList<ItemObject> getItemObjects(String itemQuery) throws SQLException {
        ObservableList<ItemObject> itemEntries = FXCollections.observableArrayList();

        DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();
        LoanSingleton loanInstance = LoanSingleton.getInstance();


        ResultSet queryOutput = databaseInstance.databaseQuery(itemQuery);

        while(queryOutput.next()){
            String ItemID = queryOutput.getString("ItemID");
            String ItemName = queryOutput.getString("ItemName");
            Integer CopiesAvailable = queryOutput.getInt("CopiesAvailable");
            Integer ReadingLevel = queryOutput.getInt("ReadingLevel");
            Boolean IsFiction = queryOutput.getBoolean("IsFiction");
            String IsFictStr = "";
            if(IsFiction == true){
                IsFictStr = "Fiction";
            }else {
               IsFictStr = "Non-Fiction";
            }

            Button btnViewItemPage = new Button();
            btnViewItemPage.getStyleClass().add("TableButton");
            btnViewItemPage.setText("View Page");

            btnViewItemPage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        loanInstance.setLoanID(ItemID);
                        callChangeScene("itemPage.fxml");
                    } catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }
            });


            itemEntries.add(new ItemObject(ItemName,CopiesAvailable,IsFictStr,ReadingLevel,btnViewItemPage));
        }

        return itemEntries;
    }

    @FXML
    private void submitButton() throws SQLException {
        if (searchBar.getText().equals("")) {
            initialize();
        } else {
            String itemQuery = "Select * from loanshark.item where ItemName =" + "\"" + searchBar.getText() + "\"";
            try {
                populateTable(itemQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
