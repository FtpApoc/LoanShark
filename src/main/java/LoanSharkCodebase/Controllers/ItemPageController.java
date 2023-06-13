package LoanSharkCodebase.Controllers;

import LoanSharkCodebase.Singletons.DatabaseSingleton;
import LoanSharkCodebase.Singletons.LoanSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class ItemPageController extends ControllerParent{

    @FXML private ChoiceBox<String> cboxBookVersion;
    @FXML private ChoiceBox<String> cboxLoanLength;
    @FXML private TextField textFieldCustomerID;
    @FXML private Label anyVerAvail;
    @FXML private Label lblItemType;
    @FXML private Label lblBookName;
    @FXML private Label lblAuthor;
    @FXML private Label lblItemID;
    @FXML private Label lblVer1;
    @FXML private Label lblVer2;
    @FXML private Label lblVer3;
    @FXML private Label lblGen1;
    @FXML private Label lblGen2;
    @FXML private Label lblGen3;
    @FXML private Rectangle rctReadingLevel;
    @FXML private Label lblReadingLevel;
    @FXML private ImageView imageView;
    @FXML private Label lblOperationFailed;


    LoanSingleton loanInstance = LoanSingleton.getInstance();
    DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();
    String loanID = loanInstance.getLoanID();


    @FXML
    private void initialize() throws SQLException {

        ControllerParent.returnPage= "itemLibrary.fxml";
        ImageLoad(loanID);
        itemDetails();

    }

    //Unexpected, unxplained issue, not able to get a relative path. have to use absolute path and a hash table rather than integrating into DB.
    private void ImageLoad(String loanID){
        HashMap<String,String> ImageSelector = new HashMap<>();
        ImageSelector.put("IT410421","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\blackfish.jpg");
        ImageSelector.put("IT000193","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\theMartian.jpg");
        ImageSelector.put("IT040207","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\dune.jpg");
        ImageSelector.put("IT120326","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\memento.jpg");
        ImageSelector.put("IT203213","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\theTimeMachine.jpg");
        ImageSelector.put("IT203858","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\inception.jpg");
        ImageSelector.put("IT204973","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\mythos.jpg");
        ImageSelector.put("IT295238","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\freeSolo.jpg");
        ImageSelector.put("IT404294","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\wolfOfWallStreet.jpg");
        ImageSelector.put("IT903263","C:\\Users\\Andre\\IdeaProjects\\LoanShark\\src\\main\\resources\\assets\\steveJobs.jpg");

        String selectedImage = ImageSelector.get(loanID);


        Image loanImage = new Image(selectedImage);
        imageView.setImage(loanImage);
    }

    private void itemDetails() throws SQLException {


        String ItemQuery = "select item.* from loanshark.item where ItemID = "+"\"" + loanID + "\"";
        ResultSet loanResult = databaseInstance.databaseQuery(ItemQuery);

        while(loanResult.next()){
            String ItemName = loanResult.getString("ItemName");
            String CopiesAvailable = loanResult.getString("CopiesAvailable");
            Boolean IsBook = loanResult.getBoolean("IsBook");
            Integer ReadingLevel = loanResult.getInt("ReadingLevel");
            String GenreTag1 = loanResult.getString("GenreTag1");
            String GenreTag2 = loanResult.getString("GenreTag2");
            String GenreTag3 = loanResult.getString("GenreTag3");
            String Picture = loanResult.getString("ItemPicture");
            String[] Lengths = {"1 Month","2 Months","3 Months"};

            lblItemID.setText(loanID);
            lblBookName.setText(ItemName);
            anyVerAvail.setText(CopiesAvailable);
            lblReadingLevel.setText(String.valueOf(ReadingLevel));
            lblGen1.setText("- "+GenreTag1);
            lblGen2.setText("- "+GenreTag2);
            lblGen3.setText("- "+GenreTag3);
            cboxLoanLength.getItems().addAll(Lengths);


            if(IsBook){
                String bookQuery = "select * from loanshark.book where ItemID = "+"\"" + loanID + "\"";
                ResultSet bookResponse = databaseInstance.databaseQuery(bookQuery);
                while(bookResponse.next()){
                    String Author = bookResponse.getString("Author");
                    Integer Paperback = bookResponse.getInt("PaperbackAvailable");
                    Integer Hardback = bookResponse.getInt("HardbackAvailable");
                    Integer Audiobook = bookResponse.getInt("AudiobookAvailable");
                    String[] Versions = {"Paperback","Hardback","Audiobook"};


                    lblAuthor.setText(Author);
                    cboxBookVersion.getItems().addAll(Versions);
                    lblItemType.setText("Book");


                }
            } else{
                String movieQuery = "select * from loanshark.movie where ItemID = "+"\"" + loanID + "\"";
                ResultSet movieResponse = databaseInstance.databaseQuery(movieQuery);
                while(movieResponse.next()){
                    String Director = movieResponse.getString("Director");
                    Integer Standard = movieResponse.getInt("StandardAvailable");
                    Integer DirCut = movieResponse.getInt("DirCutAvailable");
                    String[] Versions = {"Standard","Director's Cut"};

                    lblVer1.setText("- Standard");
                    lblVer2.setText("- Director's Cut");
                    lblVer3.setVisible(false);
                    lblAuthor.setText(Director);
                    cboxBookVersion.getItems().addAll(Versions);
                    lblItemType.setText("Movie");
                }
            }
         }


    }
    public void ConfirmLoan() throws SQLException {
        String LoanLength = cboxLoanLength.getValue();
        String VersionType = cboxBookVersion.getValue();
        String CustomerID = textFieldCustomerID.getText();
        String[] userInputs = {LoanLength,VersionType,CustomerID};
        Boolean CorrectInputs = verifyinputs(userInputs);
        if(CorrectInputs){
            CheckCustomer(CustomerID);
        }


    }

    private Boolean verifyinputs(String[] userInputs) {
        for(String input : userInputs) {
            if (input == "") {
                return false;
            } else {
                lblOperationFailed.setText("Empty Input Found");
            }
        } return true;
    }

    private void CheckCustomer(String CustomerID) throws SQLException {
        String customerQuery = "Select customer.CurrentLoans from customer where CustomerID ="+"\"" + CustomerID + "\"";
        ResultSet queryResponse = databaseInstance.databaseQuery(customerQuery);

        while(queryResponse.next()){
            Integer CurrentLoans = queryResponse.getInt("CurrentLoans");
            if (CurrentLoans < 5){
                if(Integer.valueOf(anyVerAvail.getText()) > 0){
                    AddToDB();
                } else {
                    lblOperationFailed.setText("No Versions Available");
                }
            } else {
                lblOperationFailed.setText("Customer cannot loan this item");
            }
        }
    }

    private void AddToDB() throws SQLException {
        //date Format needed to parse into SQL
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        //Event Number
        Random rand = new Random();
        int EventNumber = rand.nextInt(1000000);
        String EventID = String.format("LE" + "%06d", EventNumber);
        String EventIDformatted = String.format("\"" + EventID + "\"");

        //CustomerID
        String CustomerID = "\"" + textFieldCustomerID.getText() + "\"";

        //ItemID is loanID
        String ItemID = ("\"" + loanID + "\"");

        //Date Loaned
        Date dateNow = Date.valueOf((LocalDate.now()));
        String dateNowFormat = dateFormat.format(dateNow);

        //Length Of loan
        char LengthOfLoanChar = (cboxLoanLength.getValue()).charAt(0);
        String LengthofLoanString = String.valueOf(LengthOfLoanChar);
        Integer LengthOfLoanInteger = Integer.valueOf(LengthofLoanString);

        //ReturnDate
        Date EndDate = Date.valueOf(LocalDate.now().plusMonths(LengthOfLoanInteger));
        String endDateFormat = dateFormat.format(EndDate);


        //ItemVersion
        String ItemVersion = "\"" + cboxBookVersion.getValue() + "\"";


        //Compile and Send Query
        String InsertQuery = String.format(
                """
                        INSERT INTO loanshark.loanevent(EventID,CustomerID,ItemID,DateLoaned,LoanLength,ReturnDate,ItemVersion,VersionFee)
                        VALUES(%s,%s,%s,%s,%s,%s,%s,%s)
                        """, EventIDformatted, CustomerID, ItemID, dateNowFormat, LengthOfLoanInteger, endDateFormat, ItemVersion, 1.0);
        databaseInstance.databaseInsert(InsertQuery);

        //Successful output response
        lblOperationFailed.setText("Operation Success, EventID is " + EventID);
        lblOperationFailed.setTextFill(Paint.valueOf("white"));

        //Decrease Stock of item
        String availabilityQuery = "UPDATE loanshark.item SET CopiesAvailable = CopiesAvailable - 1 Where ItemID =" + ItemID;
        databaseInstance.databaseInsert(availabilityQuery);

        //Add to customers db
        String customerQuery = "Select customer.CurrentLoans from customer where CustomerID =" + CustomerID;
        ResultSet queryResponse = databaseInstance.databaseQuery(customerQuery);

        while (queryResponse.next()) {
            String currentLoans = queryResponse.getString("CurrentLoans");
            Integer newCurrentLoans = Integer.valueOf(currentLoans) + 1;
            String eventNumberFormatted = "EventNumber" + newCurrentLoans;

            String custQuery = String.format("""
                    Update loanshark.customer
                    SET %s = %s,
                    CurrentLoans = %s
                    where CustomerID = %s;
                    """, eventNumberFormatted, EventIDformatted,newCurrentLoans,CustomerID);

            databaseInstance.databaseInsert(custQuery);
        }
    }
}

