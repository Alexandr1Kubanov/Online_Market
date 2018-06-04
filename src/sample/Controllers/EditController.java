package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.Product;

public class EditController {
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
    TableView <Product> tableProduct;

    private ObservableList<Product> AddData= FXCollections.observableArrayList();

    public void idset(int a){
        System.out.println(a);
    }



}
