package sample.Start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/sample.fxml"));
        primaryStage.setTitle("Admin");
        primaryStage.setScene(new Scene(root, 293, 192));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}