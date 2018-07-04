package sample.Controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.SQLiteAdapter.SQLiteAdapter;
import sample.Universal;
import javafx.util.Callback;

import java.io.IOException;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;


public class EditController {

    @FXML
    Button addButton,deleteButton,editButton,searchButton;
    @FXML
    Button productButton,orderButton,userButton;
    @FXML
    TextField searchField;
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
    private ArrayList<String> quareSqlSelect = new ArrayList<>();
    private ArrayList<String> quareSqlDelete = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private ObservableList<Universal> observableListComboBox;
    private ObservableList<Universal> observableList  = FXCollections.observableArrayList();
    private FilteredList<Universal> filteredData;
    private String buttonID = "";

    @FXML
    private void initialize() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        });

        commandSqllist();
        combobox.setVisible(true);
        comboboxadd("Select * From Categories");
        productButton.fire();
        //filteredData = new FilteredList<>(observableList, e -> true);

        searchField.setOnKeyReleased(e ->{
            searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(universal->{
                    if( newValue==null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if(universal.property(0).getValue().toLowerCase().contains(lowerCaseFilter))
                    {
                        return true;
                    }
                    else if(universal.property(1).getValue().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                    return false;
                });
            }));
            tableView.setItems(filteredData);
        });

    }

    private void commandSqllist() {
        //лист запросов на удаление из БД
        quareSqlDelete.add("Delete From Product Where ID_Product =");
        quareSqlDelete.add("Delete From User Where ID_User =");
        quareSqlDelete.add("Delete From AllOrder Where ID_Order =");

        //лист Select запросов на выборку из таблиц БД
        quareSqlSelect.add("Select Product.ID_Product,Product.Name_Product as 'Наименование Продукта', " +
                "Product.Producing_country as 'Страна производитель',Product.Count as 'Количество',Product_Price.Price as 'Цена', Product.Sale as 'Распродажа', Product.Existence as 'Наличие на складе' " +
                " From Product INNER Join Product_Price ON Product_Price.id_product = Product.ID_Product ");

        quareSqlSelect.add("Select User.ID_User, User.Name as 'Имя/Логин', User.Number_Phone as 'Номер Телефона', User.Password as 'Пароль', " +
                " Addresses.City as 'Город' , Addresses.Street as 'Улица' , Addresses.House as 'Номер Дома' , Addresses.Apartment as 'Номер Квартиры' " +
                " From User Join Addresses " +
                " Where User.ID_User = Addresses.id_user");

        quareSqlSelect.add("Select AllOrder.id_user,User.Name as 'ФИО',Product.Name_Product as 'Наименование Продукта', " +
                "AllOrder.Count  as 'Количество',AllOrder.date_order as 'Дата заказа',AllOrder.Total_Price as 'Цена', " +
                "AllOrder.Total_Unit,Addresses.City as 'Город',Addresses.Street as 'Улица', " +
                "Addresses.House as 'Номер дома',User.Number_Phone as 'Номер телефона' " +
                "From AllOrder INNER Join User ON AllOrder.id_user =User.ID_User " +
                "Inner Join Addresses ON AllOrder.id_user = Addresses.id_user  " +
                "Inner Join Product ON AllOrder.id_product = Product.ID_Product");

        quareSqlSelect.add("Select Product.ID_Product, Product.Name_Product as 'Наименование Продукта', Product.Producing_country as 'Страна производитель', " +
                "Product.Count as 'Количество', Product_Price.Price as 'Цена', " +
                "(CASE WHEN Product.Sale = 0 THEN 'нет' ELSE 'да' END) as 'Распродажа', (CASE WHEN Product.Existence = 0 THEN 'нет' ELSE 'да' END) as 'Наличие на складе' " +
                "From Product Join Product_Price Join categories_product " +
                "Where Product.ID_Product=Product_Price.id_product " +
                "AND Product.ID_Product=categories_product.ID_Product " +
                "AND categories_product.ID_Categories = ");

        //лист Insert запросов в БД
        //quareSqlSelect.add("Insert Into Product(ID_Product,Name_Product,Old_Price,Rating,Unit,Presence,Sale)Values(");
    }

    void idset(int a) {

    }

    //метод получает строку запроса и передает ее в метод заполнения листа данными из БД и вывода в tableviwe
    @FXML
    private void getItemButton(String str) {
        SQLiteAdapter sql = new SQLiteAdapter();
        list.clear();
        //System.out.println(str);
        observableList = sql.AddTableView(str, list);
        columnsAdd(tableView, list);
        tableView.setItems(observableList);
        filteredData = new FilteredList<>(observableList, e -> true);

    }



    //метод получает ссылку на TableView для заполнения Имена колон данными из полученного листа
    @FXML
    private void columnsAdd(TableView tv, ArrayList list) {
        tv.getColumns().clear();

        for (int i = 0; i < list.size(); i++) {
            TableColumn<Universal, String> col = new TableColumn<>();
            int fin = i;
            col.setCellValueFactory((TableColumn.CellDataFeatures<Universal, String> column) -> column.getValue().property(fin));
            col.setText((String) list.get(i));
            tv.getColumns().add(col);
            col.prefWidthProperty().bind(tv.widthProperty().divide(list.size()));
        }
    }

    //вывод в TableView данных из выбранной категории продуктов в combobox
    @FXML
    private void comboBoxSelected(ActionEvent actionEvent) {
        if(((Universal) combobox.getValue()).getId() != 16 && ((Universal) combobox.getValue()).getId() != 0 )
        {
            String str = quareSqlSelect.get(3) + ((Universal) combobox.getValue()).getId();
            getItemButton(str);
        }
        if(((Universal) combobox.getValue()).getId() == 16 ){
            String str = quareSqlSelect.get(0);
            getItemButton(str);
        }
    }

    //Заполнение ComboBox именами категорий продуктов из БД
    @FXML
    private void comboboxadd(String str) {
        SQLiteAdapter sql = new SQLiteAdapter();
        ArrayList<String> list = new ArrayList<>();
        observableListComboBox = sql.AddTableView(str, list);
        if(!observableListComboBox.isEmpty())
        {
        combobox.setItems(observableListComboBox);
        convertInComboBox(combobox);
        }
        else {
            System.out.println("Нет данных");
        }
    }

    //конвертер для правильного отображения строк данных observableListComboBox в combobox
    private String convertInComboBox(ComboBox cb) {
        cb.setCellFactory((comboBox) -> new ListCell<Universal>() {
            @Override
            protected void updateItem(Universal item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.property(0).get());
                }
            }
        });
        cb.setConverter(new StringConverter<Universal>() {
            @Override
            public String toString(Universal currentBox) {
                if (currentBox == null) {
                    return null;
                } else {

                    return currentBox.property(0).get();
                }
            }

            @Override
            public Universal fromString(String string) {
                return null;
            }
        });
        return null;
    }

    //реализация кнопки Удалить
    @FXML
    private void DelButton() {
        //Проверка на соответсвие имени первых столбцов tableview и удаление записей
        if (((TableColumn) tableView.getColumns().get(0)).getText().equals("Наименование Продукта") &&
                ((TableColumn) tableView.getColumns().get(1)).getText().equals("Количество")) {
            AlertAndDelete(0);//метод вывода сообщения на удаления записи,
            productButton.fire();
        }
        if (((TableColumn) tableView.getColumns().get(0)).getText().equals("Имя") &&
                ((TableColumn) tableView.getColumns().get(1)).getText().equals("Номер Телефона")) {
            AlertAndDelete(1);//метод вывода сообщения на удаления записи
            userButton.fire();
        }
        if (((TableColumn) tableView.getColumns().get(0)).getText().equals("ФИО") &&
                ((TableColumn) tableView.getColumns().get(1)).getText().equals("Наименование Продукта")) {
            AlertAndDelete(2);//метод вывода сообщения на удаления записи
            orderButton.fire();
        }
    }

    //реализация кнопки Добавления и Редактирование
    @FXML
    private void ClickedButtonAddorEdit() {
        try {
            ArrayList<String> testList = list;
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/EditWindow.fxml"));
            Parent root = fxmlLoader.load();
            EditWindowController editcontroller = fxmlLoader.getController();

            //переменной buttonID присваевается значение при нажатии кнопок таблиц с данными
            if (buttonID.equals("1"))
            {// buttonID "1" -id кнопки ProductButton
                if (editButton.isArmed())
                { //если была нажата кнопка editButton
                    //получаем ID выбранной в TableView ячейки
                    getID = ((Universal) tableView.getSelectionModel().getSelectedItem()).getId();
                    //передаем в метод контроллера ID_product,целое число для команды sql,и id нажатой кнопки
                    editcontroller.getId(getID, 0,buttonID);

                }
                if(!editButton.isArmed()){editcontroller.getId(0, 0,buttonID);}

                //срабатывает при нажатии на кнопку addButton
                //передаем ссылки на лист с именами столбцов таблицы и лист имен категорий продуктов из combobox
                editcontroller.startEdit(list, observableListComboBox);
            } else {
                //buttonID "2" -id кнопки UserButton
                if (editButton.isArmed() && buttonID.equals("2"))
                {
                    getID = ((Universal) tableView.getSelectionModel().getSelectedItem()).getId();
                    editcontroller.getId(getID, 1,buttonID);
                }
                //buttonID "3" -id кнопки OrderButton
                if (editButton.isArmed() && buttonID.equals("3"))
                {
                    getID = ((Universal) tableView.getSelectionModel().getSelectedItem()).getId();
                    editcontroller.getId(getID, 2,buttonID);
                }
                if(!editButton.isArmed()) {editcontroller.getId(0, 0,buttonID);}
                //ObservableList<Universal> obs = null;
                //
                editcontroller.startEdit(list, null);
            }
            stage.setScene(new Scene(root));
            stage.setTitle("");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

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
        if (!(source instanceof Button))
        {
            return;
        }

        Button clickedButton = (Button) source;
        switch (clickedButton.getId()) {
            case "productButton":
                combobox.setVisible(true);
                combobox.setPromptText("Выберете категорию");
                getItemButton(quareSqlSelect.get(0));
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                combobox.setVisibleRowCount(8);
                buttonID = "1";
                break;
            case "userButton":
                getItemButton(quareSqlSelect.get(1));
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                combobox.setVisible(false);
                categories.setVisible(false);
                buttonID = "2";
                break;
            case "orderButton":
                getItemButton(quareSqlSelect.get(2));
                combobox.setVisible(false);
                categories.setVisible(false);
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                buttonID = "3";
                break;
            case "addButton":
                categories.setVisible(false);
                editButton.setVisible(false);
                deleteButton.setVisible(false);
                ClickedButtonAddorEdit();//метод обрабатывает нажатие кнопок таблиц данных
                break;
        }
    }

    @FXML
    //метод вывода сообщения на удаления записи из tableview и БД(передается номер команды quaresqlDelete)
    private void AlertAndDelete(int num) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Внимание");
        alert.setContentText("Вы уверены,что хотите удалить запись?");
        alert.setTitle("Удаление");
        ButtonType Yes = new ButtonType("ДА");
        ButtonType No = new ButtonType("Нет");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(Yes, No);
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null)
        {
            Label label = new Label("Не выбрано!");
        } else if (option.get() == Yes) {
            //получаем ID выбранного поля
            getID = ((Universal) tableView.getSelectionModel().getSelectedItem()).getId();
            if (getID != 0)
            {
                //присваиваем переменной quareSqldel запрос на удаление и ID выбранного поля
                String quareSqldel = quareSqlDelete.get(num) + getID;
                //Подключаемся к БД
                SQLiteAdapter sql = new SQLiteAdapter();
                //Передаем собранный запрос в БД
                sql.FromBaseById(quareSqldel);
                //Отрисовываем таблицу с новыми данными
            } else if (option.get() == No) {
                Label label = new Label("Cancelled!");
            }
        }

    }




}



