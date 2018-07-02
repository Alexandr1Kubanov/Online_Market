package sample.SQLiteAdapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Universal;


import java.sql.*;
import java.util.ArrayList;

public class SQLiteAdapter {

    private Connection connection;
    private boolean baseOk = false;

    private ResultSet resultSet;

    public SQLiteAdapter() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Online_Market.iml(DB).db");
            Statement statement = connection.createStatement();
            statement.execute("Select ID_User from User LIMIT 1");
            baseOk = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnetion() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int checkUser(String login, String password) {
        if (baseOk) {
            String query1 = "select ID_User from User where Name='" + login + "' and Password ='" + password+"'";

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query1);

                while (rs.next()) {
                    return rs.getInt("ID_User");
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                closeConnetion();
            }

        }
        return -1;
    }
    public int checkUser(int userId) {
        if (baseOk) {
            String query1 = "select lable from User where ID_User = " + userId;

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query1);

                while (rs.next()) {
                    return rs.getInt("lable");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnetion();
            }
        }
        return -1;
    }

    //метод принимает строку запроса команды SQL и ссылку на ArrayList(заполняет его Именами колон таблиц из БД)
    public ObservableList<Universal> AddTableView(String str, ArrayList nameColumns) {
        ObservableList<Universal> PL = FXCollections.observableArrayList();
        if (baseOk) {
            String query = str;
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                int count = resultSet.getMetaData().getColumnCount();
                for (int i = 2; i <= count; i++) {
                    nameColumns.add(resultSet.getMetaData().getColumnName(i));//заполняет его Именами колон таблиц из БД
                }
                while (resultSet.next()) {
                    PL.add(new Universal(resultSet, count));//заполняет его данными колон таблиц из БД
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnetion();
            }
        }
        return PL;
    }

    //
    public void FromBaseById(String str) {
        if (baseOk) {
            try {
                Statement statement = connection.createStatement();
                statement.execute(str);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnetion();
            }
        }

    }

    public ArrayList<String> AddTextField(String str) {
        ArrayList<String> list = new ArrayList<>();
        if (baseOk) {
            String query = str;
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                int count = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= count; i++) {
                    list.add(resultSet.getString(i));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnetion();
            }
        }
        return list;
    }
    public void updateDataBase(String command,int [] lastid){
        if (baseOk) {
            try {

                Statement statement = connection.createStatement();
                statement.execute(command);
                resultSet = statement.getGeneratedKeys();

                while (resultSet.next()){
                    lastid[0]=resultSet.getInt(1);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnetion();
            }
        }

    }


}
