package sample.SQLiteAdapter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

public class SQLiteAdapter {

    Connection connection;
    public boolean baseOk=false;

    public SQLiteAdapter(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Online_Market.iml(DB).db");
            Statement statement = connection.createStatement();
            statement.execute("Select ID_User from User LIMIT 1");
            baseOk=true;

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void closeConnetion()
    {
        try {
            if(connection!=null)
                connection.close();
        }catch (SQLException e){
                e.printStackTrace();
        }
    }


    public int checkUser(String l, String p){
        if( baseOk)
        {
            String query1= "select ID_User from User where Name='"+l+"' and Password ='"+p+"' and lable = 1";

            try {
                Statement statement = connection.createStatement();
                ResultSet rs =statement.executeQuery(query1);

                while (rs.next()){
                    return rs.getInt("ID_User");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally {
                closeConnetion();
            }
        }
        return -1;
    }


}
