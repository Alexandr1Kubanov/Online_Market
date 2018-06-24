package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Universal {

    private int id;
    private ObservableList<StringProperty> ItemList = FXCollections.observableArrayList();

    public Universal(ResultSet resultSet, int count) throws SQLException {   //конструктор по умолчанию заполняет лист данными из БД
        id = resultSet.getInt(1);
        for (int i = 2; i <= count; i++) {
            StringProperty sp = new SimpleStringProperty(resultSet.getString(i));
            this.ItemList.add(sp);
        }
    }

    public int getId() {
        return id;
    }

    public StringProperty property(int i) {
        return ItemList.get(i);
    }


}

