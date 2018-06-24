package sample.Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.SQLiteAdapter.SQLiteAdapter;
import sample.Universal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EditWindowController {
    ArrayList<String> lableList = new ArrayList();
    ComboBox comboBox1 = new ComboBox();
    ObservableList<Universal> ComboBoxList;
    ArrayList<String> textFieldList = new ArrayList<>();
    private ArrayList<String> quareSqlEditWindow = new ArrayList<>();

    private void comandSql() {
        quareSqlEditWindow.add("Select Name_Product,Count,Existence,Sale,Producing_country From Product where ID_Product =");

        quareSqlEditWindow.add("Select Name,Number_Phone,City,Street ,House ,Apartment From User,Addresses Where User.ID_User=Addresses.id_user AND Addresses.id_user=");

        quareSqlEditWindow.add("Select User.Name,Product.Name_Product,AllOrder.Count,AllOrder.date_order ,AllOrder.Total_Price," +
                "AllOrder.Total_Unit,Addresses.City,Addresses.Street,Addresses.House ,User.Number_Phone From AllOrder INNER Join User ON AllOrder.id_user =User.ID_User " +
                "Inner Join Addresses ON AllOrder.id_user = Addresses.id_user  " +
                "Inner Join Product ON AllOrder.id_product = Product.ID_Product AND User.ID_User =");

    }

    @FXML
    AnchorPane AddWindow;

    @FXML
    VBox vbox1;

    @FXML
    Button buttonOK;


    public void getId(int id, int i) {
        if (id != 0) {
            //присваиваем переменной quareSqldel запрос на удаление и ID выбранного поля
            String quare = quareSqlEditWindow.get(i) + id;
            //Подключаемся к БД
            SQLiteAdapter sql = new SQLiteAdapter();
            textFieldList = sql.AddTextField(quare);
        }
        System.out.println(id);
    }

    public void startEdit(ArrayList list1, ObservableList list) {
        ComboBoxList = list;
        lableList = list1;
        setAddWindow();
    }

    @FXML
    private void initialize() {
        comandSql();
    }

    public void setAddWindow() {
        if (ComboBoxList != null) {
            comboBox1.setValue("Выберете категорию");
            for (int i = 0; i <= ComboBoxList.size() - 1; i++) {
                comboBox1.getItems().add(ComboBoxList.get(i).property(0).getValue());
            }
            vbox1.getChildren().addAll(comboBox1);
        }
        for (int i = 0; i < lableList.size(); i++) {
            Label label = new Label();
            label.setText(lableList.get(i));
            TextField tf = new TextField();

            if (!textFieldList.isEmpty()) {
                tf.setText(textFieldList.get(i));
            }
            vbox1.getChildren().addAll(label, tf);
            System.out.println(label);
        }
    }

    public void getResultFromWindow(ActionEvent actionEvent) {

    }
}
