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

    private int getID;

    //лист команд для запросов
    private ArrayList<String> quareSql=new ArrayList<>();
    private void commandSqllist(){
        quareSql.add("Select ID_Product,Name_Product as 'Наименование Продукта' ," +
                "Old_Price as 'Старая Цена',Unit as 'Количество'," +
                "Presence as 'Наличие',Sale as 'Акция' From Product");

        quareSql.add("Select User.ID_User,Name as 'Имя',Number_Phone as 'Номер Телефона'," +
                "City as 'Город',Street as 'Улица',House as 'Номер Дома',Apartment as 'Номер Квартиры' " +
                "From User,Addresses Where User.ID_User=Addresses.id_user");

        quareSql.add("Select ID_Order, User.Name as 'Имя',Product.Name_Product as 'Наименование Продукта'," +
                "Count as 'Количество',date_order as 'Дата Заказа',AllOrder.phone_user as 'Номер Телефона'" +
                ",AllOrder.Total_Price as 'Общая цена',AllOrder.Total_Unit as 'Общее количество' " +
                "From AllOrder,User,Addresses,Product " +
                "where AllOrder.id_user=User.ID_User and Addresses.id_user = User.ID_User " +
                "and AllOrder.id_product = Product.ID_Product");
        quareSql.add("Delete From Product Where ID_Product =");

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

    //реализация кнопки Удалить
    @FXML
    private void del(){
        //получаем ID выбранного поля
        getID=((Universal)tableView.getSelectionModel().getSelectedItem()).getId();
        //присваиваем переменной quareSqldel запрос на удаление и ID выбранного поля
        String quareSqldel = quareSql.get(3)+getID;
        //Подключаемся к БД
        SQLiteAdapter sql = new SQLiteAdapter();
        //Передаем собранный запрос в БД
        sql.DeleteFromBaseById(quareSqldel);
        //Отрисовываем таблицу с новыми данными
        ProductButton.fire();
    }
    //реализация кнопки Редактировать
    @FXML
    private void edit(){
        getID=((Universal)tableView.getSelectionModel().getSelectedItem()).getId();

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
                getItemButton(quareSql.get(2));
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



