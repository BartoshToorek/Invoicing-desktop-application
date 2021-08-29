package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.AlertBox;
import sample.Database.DatabaseHandler;
import sample.Shaker;
import sample.model.Account;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpBackButton;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpBankNumber;

    @FXML
    private TextField signUpLogin;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private PasswordField signUpPasswordCheck;

    @FXML
    private Button signUpSignupButton;
    private Object Node;

    @FXML
    void initialize() {


    signUpBackButton.setOnAction(event -> {

        signUpBackButton.getScene().getWindow().hide();
        try {
            Parent tableview = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene tableviewScene = new Scene(tableview);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableviewScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    signUpSignupButton.setOnAction(event -> {
        if (signUpLogin.getText().trim().isEmpty() || signUpPassword.getText().trim().isEmpty()
                || signUpFirstName.getText().trim().isEmpty() || signUpLastName.getText().trim().isEmpty()
                || signUpBankNumber.getText().trim().isEmpty() || signUpPasswordCheck.getText().trim().isEmpty()){
            AlertBox.display("Nieudana rejestracja","Wymagane pola nie mogą być puste");
        }else {
            createAccount();
        }
    });
    }

    private void createAccount(){
        DatabaseHandler databaseHandler = new DatabaseHandler();

        String accountLogin = signUpLogin.getText().trim();
        String accountPassword = signUpPassword.getText().trim();
        String accountFirstName = signUpFirstName.getText().trim();
        String accountLastName = signUpLastName.getText().trim();
        String accountPasswordCheck = signUpPasswordCheck.getText().trim();
        String accountBankNumber = signUpBankNumber.getText().trim();

        if(accountPassword.equals(accountPasswordCheck) && accountPasswordLengthCheck(accountPassword) && accountBankNumberCheck(accountBankNumber) &&
           accountFirstnameCheck(accountFirstName) && accountLastnameCheck(accountLastName) && accountLoginCheck(accountLogin)){
            Account account = new Account(accountLogin, accountPassword, accountFirstName, accountLastName, accountBankNumber);
            databaseHandler.signUpUser(account);
            clearFields();
        }else{
            Shaker shakePassword = new Shaker(signUpPassword);
            Shaker shakePasswordCheck = new Shaker(signUpPasswordCheck);
            shakePassword.movement();
            shakePasswordCheck.movement();
        }
    }

    private boolean accountBankNumberCheck(String accountBankNumber){
        if(accountBankNumber.trim().length() != 32
                || !accountBankNumber.trim().matches("^\\d{2}[\\p{javaSpaceChar}]\\d{4}[\\p{javaSpaceChar}]\\d{4}[\\p{javaSpaceChar}]\\d{4}[\\p{javaSpaceChar}]\\d{4}[\\p{javaSpaceChar}]\\d{4}[\\p{javaSpaceChar}]\\d{4}$")){
            AlertBox.display("Nieprawidłowy format numeru konta", "Maksymalna dlugosc to 26 cyf");
            signUpBankNumber.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountFirstnameCheck(String accountFirstname){
        if(accountFirstname.trim().length() > 45 || !accountFirstname.trim().matches("^[a-zA-Z\\p{IsAlphabetic}\\d\\-\\.\\']*$")){
            AlertBox.display("Nieprawidłowy format imienia","Maksymalna długość to 45 liter");
            signUpFirstName.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountLastnameCheck(String accountLastname){
        if(accountLastname.trim().length() > 45 || !accountLastname.trim().matches("^[a-zA-Z\\p{IsAlphabetic}\\d\\-\\.\\']*$")){
            AlertBox.display("Nieprawidłowy format nazwiska","Maksymalna długość to 45 liter");
            signUpLastName.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountLoginCheck(String accountLogin){
        if(accountLogin.trim().length() > 45 || !accountLogin.trim().matches("^[a-zA-Z0-9._-]{3,}$")){
            AlertBox.display("Nieprawidłowy format loginu","Maksymalna/minimalna długośc to 45/3 liter lub/i cyfr");
            signUpLogin.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountPasswordLengthCheck(String accountPassword){
        if(accountPassword.trim().length() > 45 || accountPassword.trim().length() < 3){
            AlertBox.display("Nieprawidłowy format hasła","Maksymalna/minimalna długość to 45/3 znaków");
            signUpPassword.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private void clearFields(){
        signUpLogin.setText(null);
        signUpPassword.setText(null);
        signUpFirstName.setText(null);
        signUpLastName.setText(null);
        signUpPasswordCheck.setText(null);
        signUpBankNumber.setText(null);
    }
}
