package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.SQLiteAdapter.SQLiteAdapter;

import java.io.IOException;


public class Controller {

    int userId;

    @FXML
    TextField Login;
    @FXML
    PasswordField pwd;
    @FXML
    Button Enter;
    @FXML
    Button remember;


    public void Click()  {

        String fromPwd = pwd.getText().toString();
        String fromLogin = Login.getText().toString();

        if(!fromLogin.isEmpty()&&!fromPwd.isEmpty()) {
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter();

            userId = sqLiteAdapter.checkUser(fromLogin,fromPwd);

            if(userId == -1)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"",ButtonType.OK);
                alert.setTitle("Ошибка ввода данных");
                alert.setHeaderText("Вы ввели не правельные логин и пароль");
                alert.setContentText("Попробуйте ещё раз");
                pwd.setText("");
                Login.setText(fromLogin);
                alert.show();
            }
            else
            {
                Stage stage = (Stage) Enter.getScene().getWindow();
                stage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/AdminProdDB.fxml"));
                Parent root1 = null;
                try {
                    root1 = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Таблица данных");
                stage.setScene(new Scene(root1));
                stage.show();

                EditController edit = fxmlLoader.getController();
                edit.idset(userId);
            }
        }
    }
    public  void ClickEnterLogin(){
        pwd.requestFocus();
    }

    public void ClickEnter() {
       Click();
    }


}



