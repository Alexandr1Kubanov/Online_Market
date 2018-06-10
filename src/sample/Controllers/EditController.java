package sample.Controllers;

import javafx.event.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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


    public void idset(int a) {
        System.out.println(a);
    }

    public void getItemButton(String str) {
        SQLiteAdapter sql = new SQLiteAdapter();
        ArrayList<String> list = new ArrayList<>();
        ObservableList<Universal> observableList = sql.AddTableView(str, list);
        columnsAdd(tableView, list);
        tableView.setItems(observableList);
    }

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
        }
    }


    public void select(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (!(source instanceof Button)) {
            return;
        }
        Button clickedButton = (Button) source;
        switch (clickedButton.getId()) {
            case "ProductButton":
                getItemButton("Select * From Product");
                break;
            case "UserButton":
                getItemButton("Select * From User");
                break;
            case "OrderButton":
                getItemButton("Select * From AllOrder");
                break;
        }
    }
}



