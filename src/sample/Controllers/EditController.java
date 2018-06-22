package sample.Controllers;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.SQLiteAdapter.SQLiteAdapter;
import sample.Universal;

import java.io.IOException;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Optional;


public class EditController {
    int countColumn;
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
    AnchorPane AnchornAdmin;

    private int getID;

    //лист команд для запросов
    private ArrayList<String> quareSql=new ArrayList<>();
    private ArrayList<String> quareSqlDelete=new ArrayList<>();
    private ArrayList<String> quareSqlSelect=new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    private CollationElementIterator label;

    private void commandSqllist(){
        //лист запросов на удаление из БД
        quareSqlDelete.add("Delete From Product Where ID_Product =");
        quareSqlDelete.add("Delete From User Where ID_User =");
        quareSqlDelete.add("Delete From AllOrder Where ID_Order =");

        quareSqlSelect.add("Select ");

        //лист Select запросов на выборку из таблиц БД
        quareSql.add("Select ID_Product,Name_Product as 'Наименование Продукта' ," +
                "Count as 'Количество',Existence as 'Налицие',Sale as 'Распродажа'," +
                "Producing_country as 'Страна производитель' From Product");

        quareSql.add("Select User.ID_User,Name as 'Имя',Number_Phone as 'Номер Телефона'," +
                "City as 'Город',Street as 'Улица',House as 'Номер Дома',Apartment as 'Номер Квартиры' " +
                "From User,Addresses Where User.ID_User=Addresses.id_user");

        quareSql.add("Select AllOrder.ID_Order,User.Name as 'ФИО',Product.Name_Product as 'Наименование Продукта'," +
                "AllOrder.Count  as 'Количество',AllOrder.date_order as 'Дата заказа',AllOrder.Total_Price as 'Цена'," +
                "AllOrder.Total_Unit,Addresses.City as 'Город',Addresses.Street as 'Улица'," +
                "Addresses.House as 'Номер дома',User.Number_Phone as 'Номер телефона'" +
                "From AllOrder INNER Join User ON AllOrder.id_user =User.ID_User " +
                "Inner Join Addresses ON AllOrder.id_user = Addresses.id_user  " +
                "Inner Join Product ON AllOrder.id_product = Product.ID_Product");

        //лист Insert запросов в БД
        quareSql.add("Insert Into Product(ID_Product,Name_Product,Old_Price,Rating,Unit,Presence,Sale)Values(");
    }

    public void idset(int a) {

    }

    //метод получает строку запроса и передает ее в метод заполнения листа данными из БД и метод вывода в tableviwe
    @FXML
    private void getItemButton(String str) {
        SQLiteAdapter sql = new SQLiteAdapter();

        ObservableList<Universal> observableList = sql.AddTableView(str, list);
        columnsAdd(tableView, list);
        tableView.setItems(observableList);

        countColumn = tableView.getColumns().size();
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

        //Проверка на соответсвие имени первых столбцов tableview и удаление записей
        if (((TableColumn)tableView.getColumns().get(0)).getText().equals(new String("Наименование Продукта"))&&
                ((TableColumn)tableView.getColumns().get(1)).getText().equals(new String("Количество"))){
            AlertAndDelete(0);//метод вывода сообщения на удаления записи
            ProductButton.fire();

        }
        if (((TableColumn)tableView.getColumns().get(0)).getText().equals(new String("Имя"))&&
                ((TableColumn)tableView.getColumns().get(1)).getText().equals(new String("Номер Телефона"))){
            AlertAndDelete(1);//метод вывода сообщения на удаления записи
            UserButton.fire();
        }
        if (((TableColumn)tableView.getColumns().get(0)).getText().equals(new String("ФИО"))&&
                ((TableColumn)tableView.getColumns().get(1)).getText().equals(new String("Наименование Продукта"))){
            AlertAndDelete(2);//метод вывода сообщения на удаления записи
            OrderButton.fire();
        }

        //Всплывающее окно перед удалением
        /*Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Внимание");
        alert.setContentText("Вы уверены,что хотите удалить запись?");
        alert.setTitle("Удаление");
        ButtonType Yes = new ButtonType("ДА");
        ButtonType No= new ButtonType("Нет");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(Yes,No);
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            this.label.setText("Не выбрано!");
        } else if (option.get() == Yes) {
            //получаем ID выбранного поля
            getID = ((Universal) tableView.getSelectionModel().getSelectedItem()).getId();
            if (getID != 0) {
                //присваиваем переменной quareSqldel запрос на удаление и ID выбранного поля
                String quareSqldel = quareSql.get(3) + getID;
                //Подключаемся к БД
                SQLiteAdapter sql = new SQLiteAdapter();
                //Передаем собранный запрос в БД
                sql.FromBaseById(quareSqldel);
                //Отрисовываем таблицу с новыми данными
                ProductButton.fire();


            } else if (option.get() == No) {
                this.label.setText("Cancelled!");
            }
        }*/
    }

    @FXML
    private void AddButton(){
        combobox.setVisible(false);
        categories.setVisible(false);
        editButton.setVisible(false);
        deleteButton.setVisible(false);

        //getID=((Universal)tableView.getSelectionModel().getSelectedItem()).getId();

        try {
            newScene("../fxml/EditWindow.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //реализация кнопки Редактировать
    @FXML
    private void edit(){
        //getID=((Universal)tableView.getSelectionModel().getSelectedItem()).getId();

           try {
               newScene("../fxml/EditWindow.fxml");
           } catch (IOException e) {
               e.printStackTrace();
           }
    }

    //метод обработки нажатых пользователем Button
    @FXML
    private void select(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        combobox.setVisible(true);
        categories.setVisible(true);

        if (!(source instanceof Button)) {return; }

        Button clickedButton = (Button) source;
        switch (clickedButton.getId()) {
            case "ProductButton":
                getItemButton(quareSql.get(0));
                comboboxadd("Select * From Categories");
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                combobox.setValue("AllProduct");
                combobox.setVisibleRowCount(5);
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
                AddButton();
                break;
        }
    }

    //метод вывода сообщения на удаления записи из tableview и БД(передается номер команды quaresqlDelete)
    public void AlertAndDelete(int num){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Внимание");
        alert.setContentText("Вы уверены,что хотите удалить запись?");
        alert.setTitle("Удаление");
        ButtonType Yes = new ButtonType("ДА");
        ButtonType No= new ButtonType("Нет");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(Yes,No);
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            this.label.setText("Не выбрано!");
        } else if (option.get() == Yes) {
            //получаем ID выбранного поля
            getID = ((Universal) tableView.getSelectionModel().getSelectedItem()).getId();
            if (getID != 0) {
                //присваиваем переменной quareSqldel запрос на удаление и ID выбранного поля
                String quareSqldel = quareSqlDelete.get(num) + getID;
                //Подключаемся к БД
                SQLiteAdapter sql = new SQLiteAdapter();
                //Передаем собранный запрос в БД
                sql.FromBaseById(quareSqldel);
                //Отрисовываем таблицу с новыми данными
            } else if (option.get() == No) {
                this.label.setText("Cancelled!");
            }
        }

    }

    //модальное окно для Редактирования и Добавления записей
    public void newScene(String str) throws IOException {

        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.isFocused();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(str));
        Parent root1 =null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Редактирование");
        Scene scene = new Scene(root1);


        EditWindowController editWindowControllere = fxmlLoader.getController();
        editWindowControllere.setAddWindow(list);
        stage.setScene(scene);
        stage.show();
    }


    public void comboBoxSelected(ActionEvent actionEvent) {
        combobox.getValue();
    }

}



