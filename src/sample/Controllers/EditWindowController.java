package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.SQLiteAdapter.SQLiteAdapter;
import sample.Universal;

import javax.swing.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

public class EditWindowController {
    private ArrayList<String> lableList = new ArrayList();
    private ComboBox comboBoxExistence=new ComboBox();
    private ComboBox comboboxCategory = new ComboBox();
    private ArrayList<TextField> textFielfWL = new ArrayList<>();
    //private ComboBox comboBox2 = new ComboBox();
    private ComboBox comboboxCountry = new ComboBox();
    private ComboBox comboBoxSale = new ComboBox();
    private ObservableList<Universal> ComboBoxList;
    private ArrayList<String> textFieldList = new ArrayList<>();
    private ArrayList<String> quareSqlEditWindow = new ArrayList<>();
    private ArrayList<TextField> text = new ArrayList<>();
    private ArrayList<String> allInfoFromWindow = new ArrayList<>();
    private ObservableList <String> collectorlist = FXCollections.observableArrayList();
    private ArrayList<String> countrylist = new ArrayList<>();
    private String buttonid;
    private int id;
    private int[]lastIdinDB = new int[1];



    private void comandSql()
    {
        quareSqlEditWindow.add("Select Product.Name_Product, Product.Producing_country, Product.Count, Product.Unit," +
                " Product_Price.price, Product.Sale , Product.Existence ,Product.ImageLink From Product," +
                " Product_Price Where Product_Price.id_product = Product.ID_Product AND Product.ID_Product =");

        quareSqlEditWindow.add("Select User.Name as 'Имя/Логин', User.Number_Phone as 'Номер Тел.', User.Password as 'Пароль', " +
                        " Addresses.City as 'Город' , Addresses.Street as 'Улица' , Addresses.House as 'Номер Дома' ," +
                        " Addresses.Apartment as 'Номер Квартиры' " +
                        " From User Join Addresses " +
                        " Where User.ID_User = Addresses.id_user AND Addresses.id_user =");

        quareSqlEditWindow.add("Select User.Name,Product.Name_Product,AllOrder.Count,AllOrder.date_order ,AllOrder.Total_Price, " +
                "AllOrder.Total_Unit,Addresses.City,Addresses.Street,Addresses.House ,User.Number_Phone " +
                "From AllOrder Join User Join Addresses Join Product " +
                "Where AllOrder.id_user = User.ID_User " +
                "AND AllOrder.id_user = Addresses.id_user " +
                "AND AllOrder.id_product = Product.ID_Product " +
                "AND User.ID_User =");
    }

    @FXML
    AnchorPane AddWindow;
    @FXML
    VBox vbox1;
    @FXML
    Button buttonOK;

    @FXML
    private void initialize() {
        countrylist.add("Россия");
        countrylist.add("Азербайджан");
        countrylist.add("Бразилия");
        countrylist.add("Турция");
        countrylist.add("Евросоюз");
        countrylist.add("Япония");
        countrylist.add("Китай");
        countrylist.add("Австралия");
        countrylist.add("Индия");
        countrylist.add("США");
        countrylist.add("Африка");

        for (int i = 0; i <= countrylist.size()-1 ; i++) {comboboxCountry.getItems().add(countrylist.get(i));}
        comboboxCountry.setPromptText("Выберете страну производителя");

        comandSql();
    }

    void getId(int id, int SqlCommand,String idButton) {
        this.buttonid = idButton;
        this.id = id;
        if (id != 0)
        {
            //присваиваем переменной quare запрос из листа запросов контроллера с ID выбранного поля
            String quare = quareSqlEditWindow.get(SqlCommand) + this.id;
            //Подключаемся к БД
            SQLiteAdapter sql = new SQLiteAdapter();
            //заполняет лист полученными данными по ID выбранного поля
            textFieldList = sql.AddTextField(quare);
        }

        //System.out.println(id);
    }

    //заполняем полученными данными переменные контроллера
    void startEdit(ArrayList lableList, ObservableList combolist) {
        ComboBoxList = combolist;
        this.lableList = lableList;
        setAddWindow();//заполняем модальное окно полученными данными
    }

    // заполнение окна контроллера
    private void setAddWindow() {

        //если comboboxList не пустой заполняем combobox контроллера и добавляем его в модальное окно
        if (ComboBoxList != null) {
            comboboxCategory.setPromptText("Выберете категорию");
            for (int i = 0; i <= ComboBoxList.size() - 1; i++) {
                comboboxCategory.getItems().add(ComboBoxList.get(i).property(0).getValue());
            }
            vbox1.getChildren().addAll(comboboxCategory);
            collectorlist.add(comboboxCategory.getPromptText());
        }

        //заполняем модальное окно именами столбцов текущего TableView и дополныем к имени поле TextField
        for (int i = 0; i < lableList.size() ; i++)
        {
            String l = lableList.get(i);

            if (l.equals("Наличие"))
            {
                comboBoxExistence.getItems().add("В наличие");
                comboBoxExistence.getItems().add("Нет на складе");
                Label label = new Label();
                label.setText("Наличие на складе");
                vbox1.getChildren().add(label);
                vbox1.getChildren().add(comboBoxExistence);
                collectorlist.add(comboBoxExistence.getItems().get(0).toString());
                continue;
            }
            if (l.equals("Страна Произв."))
            {
                Label label = new Label();
                label.setText("Страна Производитель");
                vbox1.getChildren().add(label);
                vbox1.getChildren().add(comboboxCountry);
                collectorlist.add("");
                continue;
            }
            if (l.equals("Распродажа"))
            {
                comboBoxSale.getItems().add("Да");
                comboBoxSale.getItems().add("Нет");
                Label label = new Label();
                label.setText(l);
                vbox1.getChildren().add(label);
                vbox1.getChildren().add(comboBoxSale);
                collectorlist.add("");
                continue;
            } else {
                Label label = new Label();
                label.setText(lableList.get(i));

                if (!textFieldList.isEmpty())
                {
                    TextField tf = new TextField();
                    tf.setText(textFieldList.get(i));
                    tf.setId("tf-"+lableList.get(i));
                    textFielfWL.add(tf);
                   tf.textProperty().addListener((observable, oldValue, newValue) -> tf.setText(newValue));
                }
                else{
                    TextField tf = new TextField();
                    tf.setId("tf-"+lableList.get(i));
                    textFielfWL.add(tf);
                    tf.textProperty().addListener((observable, oldValue, newValue) -> tf.setText(newValue));
                }
                collectorlist.add(textFielfWL.get(textFielfWL.size()-1).getText());
                vbox1.getChildren().addAll(label,textFielfWL.get(textFielfWL.size()-1));

            }
        }
    }

    //Action кнопки подтвердить
    public void getResultFromWindow(ActionEvent actionEvent) {
        int countemptyrow = 0;
        for(int i = 0; i<textFielfWL.size();i++){
            if(textFielfWL.get(i).getText().equals("")|| textFielfWL.get(i).getText().equals("Введите данные"))
            {
                countemptyrow++;
                textFielfWL.get(i).setText("Введите данные");
                textFielfWL.get(i).setStyle("-fx-text-inner-color: red;");
                continue;
            }
        }
        if(countemptyrow > 0) {
            Alert();
        }
        else if(countemptyrow == 0){
            getInfoEdit(allInfoFromWindow);
        }

    }

    //метод обработки введенных данных при нажатии кнопки подтвердить
    private void getInfoEdit(ArrayList allInfoFromWindow) {
        int count = 0;
        //обработка окна User
        if (ComboBoxList == null && allInfoFromWindow.isEmpty())
        {
            for (int i = 0; i < collectorlist.size(); i++) {
                if (!collectorlist.isEmpty())
                {
                    allInfoFromWindow.add(textFielfWL.get(i).getText());
                }
            }
            UserAddEdit(allInfoFromWindow);
        }
        //обработка окна Product
        if(ComboBoxList != null && allInfoFromWindow.isEmpty()) {
            allInfoFromWindow.add(comboboxCategory.getValue().toString());
            for (int i = 1; i < collectorlist.size(); i++) {
                if (!collectorlist.isEmpty())
                {
                    if (i == 2)
                    {
                        allInfoFromWindow.add(comboboxCountry.getValue().toString());
                        continue;
                    }
                    if (i == 6)
                    {
                        allInfoFromWindow.add(comboBoxSale.getValue().toString());
                        continue;
                    }
                    if (i == 7)
                    {
                        allInfoFromWindow.add(comboBoxExistence.getValue().toString());
                        continue;
                    } else {
                        allInfoFromWindow.add(textFielfWL.get(count).getText());
                        count++;
                    }
                }
            }
            ProductAddEdit(allInfoFromWindow);
        }
    }


    //метод передает sql запрос с полученными из контроллера данными в БД(реализации добавления в ProductTable)
    private void ProductAddEdit(ArrayList infolist){
        if(!infolist.isEmpty())
        {
            //реализация Add(ProductTable)
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter();
            if (buttonid.equals("1")&& id == 0)
            {
                //заносит данные в таблицу Product
                sqLiteAdapter.updateDataBase("Insert Into Product(Name_Product ,Producing_country " +
                        ",Count ,Unit ,Sale ,Existence ,ImageLink )" +
                        "Values('" + infolist.get(1) +
                        "','" + infolist.get(2) + "','" + infolist.get(3) + "','" + infolist.get(4) + "'" +
                        ",'" + infolist.get(6) + "','"+ infolist.get(7) + "','" + infolist.get(8) + "')",lastIdinDB);
                int idproduct = lastIdinDB[0];

                long curTime = System.currentTimeMillis();
                String curStringDate = new SimpleDateFormat("dd.MM.yyyy").format(curTime);

                sqLiteAdapter=new SQLiteAdapter();
                //заносит данные в таблицу Product_Price
                sqLiteAdapter.updateDataBase("Insert Into Product_Price(id_product,date_start,price)Values('"
                        +lastIdinDB[0]+"','"+ curStringDate+"','"+infolist.get(5)+"')",lastIdinDB);

                //присваиваем переменной ID выбранной категории
                int idcategories=0;
                for (Universal aComboBoxList : ComboBoxList) {
                    if ((aComboBoxList.property(0).getValue()).equals(comboboxCategory.getValue().toString()))
                    {
                        idcategories = aComboBoxList.getId();
                        break;
                    }
                }
                sqLiteAdapter=new SQLiteAdapter();
                //заносит данные в таблицу categories_product
                sqLiteAdapter.updateDataBase("Insert Into categories_product(ID_Product,ID_Categories)Values('"
                        +idproduct+"','"+idcategories+"')",lastIdinDB);
            }

            //реализация Update(ProductTable)
            if(buttonid.equals("1")&& id != 0)
            {
                sqLiteAdapter.updateDataBase("UPDATE Product SET Name_Product= '"+infolist.get(1)+"', " +
                        " Producing_country= '"+infolist.get(2)+"', Count= '"+infolist.get(3)+"' ," +
                        " Unit='"+infolist.get(4)+"', Sale='"+infolist.get(6)+"', Existence='"+infolist.get(7)+"', " +
                        " ImageLink='"+infolist.get(8)+"' WHERE ID_Product ='"+id+"'",lastIdinDB);


                long curTime = System.currentTimeMillis();
                String curStringDate = new SimpleDateFormat("dd.MM.yyyy").format(curTime);

                sqLiteAdapter=new SQLiteAdapter();
                //заносит данные в таблицу Product_Price
                sqLiteAdapter.updateDataBase("Update Product_Price SET id_product='"+id+"', " +
                        " date_start='"+curStringDate+"', price = '"+infolist.get(5)+"'" +
                        " WHERE ID_Product ='"+id+"'",lastIdinDB);

                //присваиваем переменной ID выбранной категории
                int idcategories=0;
                for (Universal aComboBoxList : ComboBoxList) {
                    if ((aComboBoxList.property(0).getValue()).equals(comboboxCategory.getValue().toString())) {
                        idcategories = aComboBoxList.getId();
                        break;
                    }
                }
                sqLiteAdapter=new SQLiteAdapter();
                //заносит данные в таблицу categories_product
                sqLiteAdapter.updateDataBase("Update categories_product SET ID_Product= '"+ id +"',ID_Categories ='"+ idcategories +"'",lastIdinDB);
                Stage stage = (Stage) buttonOK.getScene().getWindow();
                stage.close();
            }
        }

        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();

        //Пока не работает обновление TableView
        //EditController ec  = new EditController();
        //ec.start();

    }

    //метод передает sql запрос с полученными из контроллера данными в БД(реализации добавления в UserTable)
    private void UserAddEdit(ArrayList infolist){
        if(!infolist.isEmpty())
        {
            //реализация Add(ProductTable)
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter();

            if (buttonid.equals("2")&& id == 0)
            {
                //заносит данные в таблицу UserTable
                sqLiteAdapter.updateDataBase("Insert Into User(Name,Nubmer_Phone,count_of_buy,sum_of_buy,Card_Code,Password,lable)" +
                        " Values('" +infolist.get(0)+"','" + infolist.get(1) + "',' ',' ',' ', '"+infolist.get(2)+"', '0')",lastIdinDB);
                int iduser = lastIdinDB[0];

            }

            //реализация Update(UserTable)
            if(buttonid.equals("2")&& id != 0)
            {
                sqLiteAdapter.updateDataBase("UPDATE User SET Name = '"+infolist.get(0)+"', Number_Phone = '"+infolist.get(1)+"', Password = '"+infolist.get(2)+"' ,lable ='0'  WHERE ID_User = "+id,lastIdinDB);

                sqLiteAdapter=new SQLiteAdapter();
                sqLiteAdapter.updateDataBase("UPDATE Addresses SET City= '"+infolist.get(3)+"', " +
                        " Street = '"+infolist.get(4)+"', house= '"+infolist.get(5)+"', " +
                        " Apartment='"+infolist.get(6)+"' WHERE id_user ='"+id+"'",lastIdinDB);

                Stage stage = (Stage) buttonOK.getScene().getWindow();
                stage.close();
            }
        }

        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();

        //Пока не работает обновление TableView
        //EditController ec  = new EditController();
        //ec.start();

    }


    private void Alert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Внимание");
        alert.setContentText("Вы забыли ввести все данные");
        alert.setTitle("");
        ButtonType Yes = new ButtonType("ок");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(Yes);
        Optional<ButtonType> option = alert.showAndWait();
    }
}
