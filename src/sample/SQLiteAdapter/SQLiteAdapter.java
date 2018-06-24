package sample.SQLiteAdapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Universal;


import java.sql.*;
import java.util.ArrayList;

public class SQLiteAdapter {

    private Connection connection;
    private boolean baseOk = false;

    public SQLiteAdapter() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Online_Market.iml(DB).db");
            Statement statement = connection.createStatement();
            statement.execute("Select ID_User from User LIMIT 1");
            baseOk = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnetion() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int checkUser(String l, String p) {
        if (baseOk) {
            String query1 = "select ID_User from User where Name='" + l + "' and Password ='" + p + "' and lable = 1";

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

    public ObservableList<Universal> AddTableView(String str, ArrayList nameColumns) {
        ObservableList<Universal> PL = FXCollections.observableArrayList();
        if (baseOk) {
            String query = str;
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                int count = resultSet.getMetaData().getColumnCount();
                for (int i = 2; i <= count; i++) {
                    nameColumns.add(resultSet.getMetaData().getColumnName(i));
                }
                while (resultSet.next()) {
                    PL.add(new Universal(resultSet, count));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnetion();
            }
        }
        return PL;
    }

    public void FromBaseById(String str) {
        if (baseOk) {
            String query = str;
            try {
                Statement statement = connection.createStatement();
                statement.execute(query);
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
                ResultSet resultSet = statement.executeQuery(query);
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


}
