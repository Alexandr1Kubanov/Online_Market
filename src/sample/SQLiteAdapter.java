package sample;
import java.sql.*;

public class SQLiteAdapter {

    Connection connection;
    public boolean baseOk=false;

    public SQLiteAdapter(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Online_Market.iml(DB).db");
            Statement statement = connection.createStatement();
            statement.execute("select id from User LIMIT 1");
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
}
