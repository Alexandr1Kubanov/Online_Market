package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import sample.SQLiteAdapter.SQLiteAdapter;
import sample.Universal;

import java.io.InputStream;
import java.util.ArrayList;

public class UserController {

    @FXML
    private ImageView ImajeLeft;

    @FXML
    private AnchorPane AnchorTopLeft, AnchorTop, AnchorInfo, AnchorBasket, anchorPaneBasket, AnchorButton;

    @FXML
    private TabPane PanePreviewProduct;

    @FXML
    private Tab TabProduct;

    @FXML
    private AnchorPane AnchorTabProduct;

    @FXML
    private GridPane GridProduct;

    @FXML
    private TabPane TabPanInfo;

    @FXML
    private Tab TabInfo;

    @FXML
    private Tab TabBasket;

    @FXML
    private ScrollPane scrollPaneBasket;

    @FXML
    private Button okB;

    @FXML
    private Button cancelB;

    @FXML
    private Button exitB;

    @FXML
    private ComboBox Categories;

    @FXML
    Button countButtonlable;

    private ArrayList<String> quareSqlSelect = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private ObservableList<Universal> observableListComboBox;
    private ObservableList<Universal> observableList = FXCollections.observableArrayList();


    //команды sql запросов
    @FXML
    private void setQuareSqlSelect() {

        quareSqlSelect.add("Select Product.ID_Product,Product.Name_Product as 'Наименование', " +
                " Product.Producing_country as 'Страна Произв.',Product.Count as 'Коли-во', Product.Unit as 'Ед.Измерения' ," +
                " Product_Price.Price as 'Цена', Product.Sale as 'Распродажа', Product.Existence as 'Наличие' ," +
                " Product.ImageLink as 'Ссылка' , Product.Description as 'Описание'"  +
                " From Product INNER Join Product_Price ON Product_Price.id_product = Product.ID_Product ");

        quareSqlSelect.add("Select Product.ID_Product, Product.Name_Product as 'Наименование Продукта', Product.Producing_country as 'Страна производитель', " +
                "Product.Count as 'Количество', Product.Unit as 'Единица измерения' , Product_Price.Price as 'Цена', " +
                " Product.Sale  as 'Распродажа', Product.Existence as 'Наличие на складе' " +
                " From Product Join Product_Price Join categories_product " +
                " Where Product.ID_Product=Product_Price.id_product " +
                " AND Product.ID_Product=categories_product.ID_Product " +
                " AND categories_product.ID_Categories = ");
    }

    @FXML
    private void initialize() {
        comboboxadd("Select * From Categories");
        setQuareSqlSelect();
        getItem(quareSqlSelect.get(0));
    }

    //метода собирает данные о категориях продуктов из БД
    @FXML
    private void getItem(String str) {
        SQLiteAdapter sql = new SQLiteAdapter();
        list.clear();
        observableList.clear();
        observableList = sql.AddTableView(str, list);
        addtoGridPane(observableList);
    }

    //заполнение Combobox Категориями
    @FXML
    private void comboboxadd(String str) {
        SQLiteAdapter sql = new SQLiteAdapter();
        ArrayList<String> list = new ArrayList<>();
        observableListComboBox = sql.AddTableView(str, list);
        if (!observableListComboBox.isEmpty()) {
            Categories.setItems(observableListComboBox);
            convertInComboBox(Categories);
        } else {
            System.out.println("Нет данных");
        }
    }

    //конвертер для правильного отображения строк данных observableListComboBox в combobox
    private String convertInComboBox(ComboBox cb) {cb.setCellFactory((comboBox) -> new ListCell<Universal>() {
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


    //выбранная категория продуктов
    @FXML
    public void SelectedItems(ActionEvent actionEvent) {
        String str = quareSqlSelect.get(1) + ((Universal) Categories.getValue()).getId();
        getItem(str);
    }

    @FXML
    private void addtoGridPane(ObservableList<Universal> list) {
        ArrayList<GridPane> gridPaneArrayList = new ArrayList<>();
        Class<?> clazz = this.getClass();
        int sitenumber=0;
        int rowindex = 0;
        int columIndex = 0;
        int count = 0;

        //todo если продуктов меньше 8 не выводит,исправить
        for (count=0; count < list.size(); count--) {
            GridPane GridProduct2 = new GridPane();
            GridProduct2.setVgap(3);
            GridProduct2.setHgap(3);

            for (int a = 0; a <= 8; a++) {

                if (rowindex > 2) {
                    rowindex = 0;
                }
                if (a == 3 || a == 6) {
                    columIndex++;
                }
                if (rowindex <= 2) {
                    TextArea text = new TextArea(list.get(count).property(8).getValue());
                    text.setWrapText(true);
                    text.setEditable(false);
                    text.setMaxWidth(177);
                    text.setMaxHeight(70);
                    text.setPadding(new Insets(2, 2, 2, 2));
                    Tooltip tooltrip = new Tooltip(text.getText());
                    text.setTooltip(tooltrip);

                    //кнопка Добавить в корзину
                    Button button = new Button("В корзину");
                    button.setMaxWidth(120);
                    button.setAlignment(Pos.CENTER);

                    ImageView imageView = new ImageView();
                    InputStream input = clazz.getResourceAsStream(list.get(count).property(7).getValue());

                    if(input != null){
                        Image image = new Image(input);
                        imageView.setImage(image);
                    }
                    if(input == null){
                        InputStream input2 = clazz.getResourceAsStream("../image/icons8-продажа-48.png");
                        Image image = new Image(input2);
                        imageView.setImage(image);
                    }

                    //присваиваем из листа наименование продукта
                    Label lab = new Label(list.get(count).property(0).getValue());


                    Label price = new Label(list.get(count).property(4).getValue() + " / " +list.get(count).property(3).getValue());
                    //добавляем TextField количество
                    TextField tf = new TextField();
                    tf.setMaxWidth(40);
                    tf.setText("1");

                    //вешаем на кнопку событие и реализуем в нем счетчик покупок
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //System.out.println("Продукт добавлен в количестве " + tf.getText());
                            int count = Integer.parseInt(countButtonlable.getText());
                            count++;
                            countButtonlable.setStyle("-fx-background-color : #FFFFFF;" + "-fx-background-radius: 10;");
                            countButtonlable.setText(String.valueOf(count));
                            tf.setText("1");
                        }
                    });

                    //отрисовываем данные
                    VBox vBox1 = new VBox(new HBox(imageView, new VBox(lab, price)), text, new HBox(button, tf));
                    vBox1.setPadding(new Insets(5, 5, 5, 5));
                    GridProduct2.add(vBox1, columIndex, rowindex, 1, 1);

                    count++;
                    rowindex++;
                }
            }
            count++;
            AnchorPane anchorProduct = new AnchorPane();
            anchorProduct.getChildren().addAll(GridProduct2);
            Tab tab = new Tab();
            tab.setText("Стр."+ ++sitenumber);
            tab.setContent(anchorProduct);
            PanePreviewProduct.getTabs().add(tab);
        }
    }
}
