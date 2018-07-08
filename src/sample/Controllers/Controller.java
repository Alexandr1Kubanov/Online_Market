package sample.Controllers;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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

    private int userId;
    private int lableId;


    @FXML
    TextField login;
    @FXML
    PasswordField password;
    @FXML
    Button enter,escape;

    public void Click()  {

        String fromPwd = password.getText();
        String fromLogin = login.getText();

        if(!fromLogin.isEmpty()&&!fromPwd.isEmpty()) {
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter();

            userId = sqLiteAdapter.checkUser(fromLogin,fromPwd);
            sqLiteAdapter = new SQLiteAdapter();
            lableId= sqLiteAdapter.checkUser(userId);
            if(userId == -1)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"",ButtonType.OK);
                alert.setTitle("Ошибка ввода данных");
                alert.setHeaderText("Вы ввели не правельные логин и пароль");
                alert.setContentText("Попробуйте ещё раз");
                password.setText("");
                login.setText(fromLogin);
                alert.show();
            }
            if(userId != 0 && lableId == 1)
            {
                Stage stage = (Stage) enter.getScene().getWindow();
                stage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/AdminProdDB2.fxml"));
                Parent root1 = null;
                try {
                    root1 = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage = new Stage();
                stage.initModality(Modality.NONE);
                stage.setTitle("Таблица данных");
                assert root1 != null;
                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.show();

                EditController edit = fxmlLoader.getController();
                edit.idset(userId);
            }
            if(userId !=0 && lableId == 0){
                Stage stage = (Stage) enter.getScene().getWindow();
                stage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/UserWindowMarket2.fxml"));
                Parent root1 = null;
                try {
                    root1 = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage = new Stage();
                stage.initModality(Modality.NONE);
                stage.setTitle("Online Market");
                assert root1 != null;
                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.show();
            }
        }
    }
    public  void ClickEnterLogin(){
        password.requestFocus();
    }

    public void ClickEnter() {
       Click();
    }
    public  void escapeButton(){
        Stage stage = (Stage) login.getScene().getWindow();
        stage.close();
    }


}



