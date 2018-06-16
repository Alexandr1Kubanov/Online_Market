package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UserController {
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;
    @FXML
    Button editButton;
    @FXML
    Button searchButton;
    @FXML
    TextField searchField;
    @FXML
    Button ProductButton;
    @FXML
    Button UserButton;
    @FXML
    Button OrderButton;
    @FXML
    TableView tableView;
    @FXML
    ComboBox combobox;
    @FXML
    Label categories;
    @FXML
    ImageView imageView;

    public void select() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("C:/Users/User/IdeaProjects/Online_Market/555.png"));
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setImage(image);
    }


    public void buttonAction() throws FileNotFoundException {


    }
}
