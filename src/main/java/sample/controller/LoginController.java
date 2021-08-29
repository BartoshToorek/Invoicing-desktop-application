package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Database.DatabaseHandler;
import sample.Shaker;
import sample.model.Account;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button loginSignupButton;

    public static String userColumn;

    @FXML
    void initialize() {
        loginButton.setOnAction(event ->{
            DatabaseHandler databaseHandler = new DatabaseHandler();
            String loginText = loginUsernameField.getText().trim();
            String loginPassword = loginPasswordField.getText().trim();

            Account account = new Account();
            account.setLogin(loginText);
            account.setPassword(loginPassword);

            ResultSet accountRows = databaseHandler.getAccount(account);
            int counter = 0;
            try{
                while(accountRows.next()){
                    counter++;
                }
                if(counter==1){
                    getCurrentUser();
                    showMenuScreen();
                }else{
                    Shaker shakeUser = new Shaker(loginUsernameField);
                    Shaker shakePassword = new Shaker(loginPasswordField);
                    shakeUser.movement();
                    shakePassword.movement();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }catch (NullPointerException e){
                Shaker shakeUser = new Shaker(loginUsernameField);
                Shaker shakePassword = new Shaker(loginPasswordField);
                shakeUser.movement();
                shakePassword.movement();
            }
        });

        loginSignupButton.setOnAction(event -> {
            loginSignupButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/signup.fxml"));
            try{
                loader.load();
            }catch(IOException e){
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });

    }

    private void showMenuScreen() {
        loginSignupButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/menu.fxml"));

        try{
            loader.load();
        }catch(IOException e){
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private String getCurrentUser(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        String loginText = loginUsernameField.getText().trim();

        Account account = new Account();
        account.setLogin(loginText);
        ResultSet current_user = databaseHandler.currentUser(account);

        try{
            while(current_user.next()){
                userColumn = current_user.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userColumn;
    }
}
