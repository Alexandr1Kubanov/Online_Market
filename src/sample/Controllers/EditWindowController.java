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
import java.util.ArrayList;

public class EditWindowController {


    @FXML
    AnchorPane AddWindow;

    public void initialize(){

    }

    public void setAddWindow(ArrayList list){
        System.out.println(list);
    }
}
