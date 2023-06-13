module com.example.loanshark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
                            
    opens LoanSharkCodebase to javafx.fxml;
    opens LoanSharkCodebase.Controllers to javafx.fxml;

    exports LoanSharkCodebase.Controllers;
    exports LoanSharkCodebase;
    exports LoanSharkCodebase.Singletons;
    opens LoanSharkCodebase.Singletons to javafx.fxml;
    exports LoanSharkCodebase.Objects;
    opens LoanSharkCodebase.Objects to javafx.fxml;
}