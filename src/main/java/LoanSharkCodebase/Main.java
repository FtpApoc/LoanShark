package LoanSharkCodebase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

    //Main is an application and therefore is an extension of the application class
public class Main extends Application {

    private static Stage stg;
    @Override // need further clarification
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        primaryStage.setResizable(false);
//        String css = this.getClass().getResource("LoanShark.css").toExternalForm();

        Parent root = FXMLLoader.load(getClass().getResource("loginView.fxml")); //getting the fxml file to pass to primaryScene line 21
        Scene primaryScene = new Scene(root, 1440, 810); // create a new scene of contents root, of stated size.
//        primaryScene.getStylesheets().add(css);
        primaryStage.setTitle("LoanShark Library System");
        primaryStage.setScene(primaryScene); // scene from line 15 FXML loaded into line 13 stage
        primaryStage.show();

    }

    public void changeScene(String fxmlPage) throws IOException {
        Parent page = FXMLLoader.load(getClass().getResource(fxmlPage));
        stg.getScene().setRoot(page);
    }


    public static void main(String[] args) {
        launch();
    } //launch will load the start method
}