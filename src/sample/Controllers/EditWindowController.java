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
import sample.SQLiteAdapter.SQLiteAdapter;
import sample.Universal;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
        quareSqlEditWindow.add("Select Product.Name_Product,Product.Producing_country,Product.Count,Product_Price.price,Product.Sale,Product.Existence From Product, Product_Price Where Product_Price.id_product = Product.ID_Product AND Product.ID_Product =");

        quareSqlEditWindow.add("Select Name,Number_Phone,City,Street ,House ,Apartment From User,Addresses Where User.ID_User=Addresses.id_user AND Addresses.id_user =");

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
        if (id != 0) {
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

            if (l.equals("Наличие на складе"))
            {
                comboBoxExistence.getItems().add("В наличие");
                comboBoxExistence.getItems().add("Нет на складе");
                Label label = new Label();
                label.setText(lableList.get(i));
                vbox1.getChildren().add(label);
                vbox1.getChildren().add(comboBoxExistence);
                collectorlist.add(comboBoxExistence.getItems().get(0).toString());
                continue;
            }
            if (l.equals("Страна производитель"))
            {
                Label label = new Label();
                label.setText(l);
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
                collectorlist.add(textFielfWL.get(textFielfWL.size()-1).getText());
                vbox1.getChildren().addAll(label,textFielfWL.get(textFielfWL.size()-1));

            }
        }
    }


    //метод обработки введенных данных при нажатии кнопки подтвердить
    private void getInfoEdit(ArrayList allInfoFromWindow){
        int count=0;
            if (!comboboxCategory.getValue().toString().equals("Выберете категорию") && comboboxCategory.getValue() != null)
            {
                allInfoFromWindow.add(comboboxCategory.getValue().toString());
                for (int i = 1; i < collectorlist.size(); i++) {
                    if (!collectorlist.isEmpty())
                    {
                        if (i == 2)
                        {
                            allInfoFromWindow.add(comboboxCountry.getValue().toString());
                            continue;
                        }
                        if (i == 5)
                        {
                            allInfoFromWindow.add(comboBoxSale.getValue().toString());
                            continue;
                        }
                        if (i == 6)
                        {
                            allInfoFromWindow.add(comboBoxExistence.getValue().toString());
                            continue;
                        } else {
                            allInfoFromWindow.add(textFielfWL.get(count).getText());
                            count++;
                        }
                    }
                }
            }
            else{
            for (int i = 0; i < collectorlist.size(); i++) {
                if (!collectorlist.isEmpty())
                {
                    allInfoFromWindow.add(collectorlist.get(i));
                }
            }
        }

       if(!allInfoFromWindow.containsAll(collectorlist))
        {
            InsertAndUpdateDB(allInfoFromWindow);
        }else{
            System.out.println("Они одинаковы");
        }
    }

    //метод для реализации добавления
    public void getinfofromwindowadd(){}

    //метод передает sql запрос с полученными из контроллера данными в БД
    private void InsertAndUpdateDB(ArrayList infolist){
        if(!infolist.isEmpty())
        {
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter();
            if (buttonid.equals("1"))
            {
                //заносит данные в таблицу Product
                sqLiteAdapter.updateDataBase("Insert Into Product(Name_Product,Producing_country,Count,Sale,Existence)Values('" + infolist.get(1) +
                        "','" + infolist.get(2) + "','" + infolist.get(3) + "','" + infolist.get(5) + "','" + infolist.get(6) + "')",lastIdinDB);
                int idproduct = lastIdinDB[0];

                long curTime = System.currentTimeMillis();
                String curStringDate = new SimpleDateFormat("dd.MM.yyyy").format(curTime);

                sqLiteAdapter=new SQLiteAdapter();
                //заносит данные в таблицу Product_Price
                sqLiteAdapter.updateDataBase("Insert Into Product_Price(id_product,date_start,price)Values('"
                        +lastIdinDB[0]+"','"+ curStringDate+"','"+infolist.get(4)+"')",lastIdinDB);

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
                sqLiteAdapter.updateDataBase("Insert Into categories_product(ID_Product,ID_Categories)Values('"
                        +idproduct+"','"+idcategories+"')",lastIdinDB);
            }
        }
    }

    public void getResultFromWindow(ActionEvent actionEvent) {
        getInfoEdit(allInfoFromWindow);
    }
}
