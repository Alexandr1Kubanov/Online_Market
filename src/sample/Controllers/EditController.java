package sample.Controllers;

import javafx.event.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import sample.SQLiteAdapter.SQLiteAdapter;
import sample.Universal;

import java.util.ArrayList;


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


    private ArrayList<String> quareSql=new ArrayList<>();
    private void commandSqllist(){
        quareSql.add("Select ID_Product,Name_Product as 'Наименование Продукта' ," +
                "Old_Price as 'Старая Цена',Unit as 'Количество'," +
                "Presence as 'Наличие',Sale as 'Акция' From Product");

        quareSql.add("Select User.ID_User,Name as 'Имя',Number_Phone as 'Номер Телефона'," +
                "City as 'Город',Street as 'Улица',House as 'Номер Дома',Apartment as 'Номер Квартиры' " +
                "From User,Addresses Where User.ID_User=Addresses.id_user");

    }
    public void idset(int a) {

    }

    //метод получает строку запроса и передает ее в метод заполнения листа данными из БД и метод вывода в tableviwe
    @FXML
    private void getItemButton(String str) {
        SQLiteAdapter sql = new SQLiteAdapter();
        ArrayList<String> list = new ArrayList<>();
        ObservableList<Universal> observableList = sql.AddTableView(str, list);
        columnsAdd(tableView, list);
        tableView.setItems(observableList);

    }


    public void initialize() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) ->{
            editButton.setVisible(true);
            deleteButton.setVisible(true);
            });
        commandSqllist();
        combobox.setVisible(false);


    }

    //метод получает ссылку на TableView для заполнения его данными из полученного листа
    @FXML
    private void columnsAdd(TableView tv, ArrayList list) {
        tv.getColumns().clear();

        for (int i = 0; i < list.size(); i++) {
            TableColumn<Universal, String> col = new TableColumn<>();
            int fin = i;
            col.setCellValueFactory((TableColumn.CellDataFeatures<Universal, String> column) -> {
                return column.getValue().property(fin);
            });
            col.setText((String) list.get(i));
            tv.getColumns().add(col);
            col.prefWidthProperty().bind(tv.widthProperty().divide(list.size()));

        }

    }

    //Заполнение ComboBox именами категорий продуктов из БД
    @FXML
    private void comboboxadd(String str) {
        combobox.getItems().add("AllCategories");
        SQLiteAdapter sql = new SQLiteAdapter();
        ArrayList<String> list = new ArrayList<>();
        ObservableList<Universal> observableList = sql.AddTableView(str, list);
        for (int i = 0; i <= observableList.size()-1; i++) {
            combobox.getItems().add(observableList.get(i).property(0).getValue());
        }

    }

    //пока что заглушка
    public void buttonAction() {
    }


    //метод обработки нажатых пользователем Button
    @FXML
    private void select(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        combobox.setVisible(true);
        categories.setVisible(true);

        if (!(source instanceof Button)) {
            return;
        }

        Button clickedButton = (Button) source;
        switch (clickedButton.getId()) {
            case "ProductButton":
                getItemButton(quareSql.get(0));
                comboboxadd("Select * From Categories");

                editButton.setVisible(false);
                deleteButton.setVisible(false);
                break;
            case "UserButton":
                getItemButton(quareSql.get(1));
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                combobox.setVisible(false);
                categories.setVisible(false);
                break;
            case "OrderButton":
                getItemButton("Select * From AllOrder");

                combobox.setVisible(false);
                categories.setVisible(false);
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                break;
            case "addButton":
                combobox.setVisible(false);
                categories.setVisible(false);
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                break;
        }
    }
}



