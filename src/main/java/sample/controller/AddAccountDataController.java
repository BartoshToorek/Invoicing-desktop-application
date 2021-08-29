package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.AlertBox;
import sample.Database.DatabaseHandler;
import sample.Shaker;
import sample.model.Firmdata;
import sample.model.Location;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAccountDataController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField addAccountNip;

    @FXML
    private TextField addAccountFirmName;

    @FXML
    private TextField addAccountPhoneNumber;

    @FXML
    private TextField addAccountFirmEmail;

    @FXML
    private TextField addAccountCity;

    @FXML
    private TextField addAccountStreet;

    @FXML
    private TextField addAccountPostalCode;

    @FXML
    private Button addAccountDataSaveButton;

    @FXML
    private Button addAccountDataBackButton;

    @FXML
    private Button deleteAccountDataButton;

    @FXML
    private Button editAccountDataButton;

    @FXML
    private Button newAccountDataButton;

    @FXML
    private TableView<Firmdata> accountFirmDataTableView;

    @FXML
    private TableColumn<Firmdata, String> accountFirmNameColumn;

    @FXML
    private TableColumn<Firmdata, String> accountNipColumn;

    @FXML
    private TableColumn<Firmdata, String> accountFirmEmailColumn;

    @FXML
    private TableColumn<Firmdata, String> accountFirmPhoneNumberColumn;

    @FXML
    private TableView<Location> accountLocationTableView;

    @FXML
    private TableColumn<Location, String> accountCityColumn;

    @FXML
    private TableColumn<Location, String> accountStreetColumn;

    @FXML
    private TableColumn<Location, String> accountPostalCodeColumn;

    private ObservableList<Firmdata> firmDataObservableList;
    private ObservableList<Location> locationObservableList;
    private boolean EDIT = false , ADD = false;

    @FXML
    void initialize() {

    populateAccountFirmDataTableView();
    populateAccountLocationTableView();

    addAccountDataBackButton.setOnAction(event -> {
        addAccountDataBackButton.getScene().getWindow().hide();
        try {
            Parent tableview = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            Scene tableviewScene = new Scene(tableview);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableviewScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    addAccountDataSaveButton.setOnAction(event -> {
        try {
            if (addAccountNip.getText().trim().isEmpty() || addAccountFirmName.getText().trim().isEmpty() || addAccountPhoneNumber.getText().trim().isEmpty()
                    || addAccountFirmEmail.getText().trim().isEmpty() || addAccountCity.getText().trim().isEmpty() || addAccountStreet.getText().trim().isEmpty()
                    || addAccountPostalCode.getText().trim().isEmpty()) {
                AlertBox.display("Nieudana próba", "Wymagane pola nie mogą być puste");
            } else {
                createFirmData();
            }
        }catch(NullPointerException e){
            AlertBox.display("Nieudana próba", "Wymagane pola nie mogą być puste");
        }
    });

    deleteAccountDataButton.setOnAction(event -> {
        deleteFirmData();
    });

    editAccountDataButton.setOnAction(event -> {
        ADD = false;
        EDIT = true;
        editFirmData();
    });

    newAccountDataButton.setOnAction(event -> {
        EDIT = false;
        ADD = true;
        clearFields();
    });

    }

    private void editFirmData(){
        try {
            Firmdata firmdata = accountFirmDataTableView.getSelectionModel().getSelectedItem();
            Location location = accountLocationTableView.getSelectionModel().getSelectedItem();
            addAccountNip.setText(firmdata.getFirmdata_nip());
            addAccountFirmName.setText(firmdata.getFirmdata_name());
            addAccountPhoneNumber.setText(firmdata.getFirmdata_phonenr());
            addAccountFirmEmail.setText(firmdata.getFirmdata_email());
            addAccountCity.setText(location.getLocation_city());
            addAccountStreet.setText(location.getLocation_street());
            addAccountPostalCode.setText(location.getLocation_postalcode());
        }catch(NullPointerException e){
            AlertBox.display("Nie wybrano wierszy","Naciśnij na informacje w tabli aby zaznaczyc wiersz");
        }
    }

    private void createFirmData(){
        DatabaseHandler databaseHandler = new DatabaseHandler();

        String accountNip = addAccountNip.getText().trim();
        String accountFirmName = addAccountFirmName.getText().trim();
        String accountPhoneNumber = addAccountPhoneNumber.getText().trim();
        String accountEmail = addAccountFirmEmail.getText().trim();
        String accountCity = addAccountCity.getText().trim();
        String accountStreet =  addAccountStreet.getText().trim();
        String accountPostalCode = addAccountPostalCode.getText().trim();
        if(accountFirmNameCheck(accountFirmName)  && accountPhoneNumberCheck(accountPhoneNumber)&& accountNipCheck(accountNip) && accountEmailCheck(accountEmail) &&
                accountCityCheck(accountCity) && accountStreetCheck(accountStreet) && accountPostalCodeCheck(accountPostalCode)) {
            Firmdata firmdata = new Firmdata();
            firmdata.setFirmdata_nip(accountNip);
            firmdata.setFirmdata_name(accountFirmName);
            firmdata.setFirmdata_phonenr(accountPhoneNumber);
            firmdata.setFirmdata_email(accountEmail);

            Location location = new Location();
            location.setLocation_city(accountCity);
            location.setLocation_street(accountStreet);
            location.setLocation_postalcode(accountPostalCode);
            if (EDIT) {
                databaseHandler.updateAccountHasFirmdata(firmdata);
                if(!databaseHandler.DUPLICATE){
                    databaseHandler.updateAccountHasLocation(location);
                }else{
                    deleteFirmData();
                }
            } else if (ADD) {
                databaseHandler.addFirmData(firmdata);
                databaseHandler.addAccountHasFirmdata(firmdata);
                if(!databaseHandler.DUPLICATE){
                    databaseHandler.addLocation(location);
                    databaseHandler.addAccountHasLocation(location);
                }else{
                    deleteFirmData();
                }
            }
        }else{
            shakeAllFields();
        }
        clearFields();
        populateAccountFirmDataTableView();
        populateAccountLocationTableView();
    }

    private void deleteFirmData(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.deleteAccountHasFirmdata();
        databaseHandler.deleteAccountHasLocation();
        populateAccountFirmDataTableView();
        populateAccountLocationTableView();
    }

    private void clearFields(){
        addAccountNip.setText(null);
        addAccountFirmName.setText(null);
        addAccountPhoneNumber.setText(null);
        addAccountFirmEmail.setText(null);
        addAccountCity.setText(null);
        addAccountStreet.setText(null);
        addAccountPostalCode.setText(null);
    }

    private void shakeAllFields(){
        Shaker shakeAccountPhoneNumber = new Shaker(addAccountPhoneNumber);
        Shaker shakeAccountNip = new Shaker(addAccountNip);
        Shaker shakeAccountEmail = new Shaker(addAccountFirmEmail);
        Shaker shakeAccountFirmName = new Shaker(addAccountFirmName);
        Shaker shakeAccountCity = new Shaker(addAccountCity);
        Shaker shakeAccountStreet = new Shaker(addAccountStreet);
        Shaker shakeAccountPostalCode = new Shaker(addAccountPostalCode);

        shakeAccountPhoneNumber.movement();
        shakeAccountNip.movement();
        shakeAccountEmail.movement();
        shakeAccountFirmName.movement();
        shakeAccountCity.movement();
        shakeAccountStreet.movement();
        shakeAccountPostalCode.movement();
    }

    private void populateAccountFirmDataTableView() {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        firmDataObservableList = FXCollections.observableArrayList();
        ResultSet accountFirmDataRows = databaseHandler.getFirmData();
        try{
            while(accountFirmDataRows.next()){
                Firmdata firmdata = new Firmdata();
                firmdata.setFirmdata_nip(accountFirmDataRows.getString("firmdata_nip"));
                firmdata.setFirmdata_name(accountFirmDataRows.getString("firmdata_name"));
                firmdata.setFirmdata_phonenr(accountFirmDataRows.getString("firmdata_phonenr"));
                firmdata.setFirmdata_email(accountFirmDataRows.getString("firmdata_email"));
                firmDataObservableList.add(firmdata);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        accountNipColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_nip"));
        accountFirmNameColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_name"));
        accountFirmPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_phonenr"));
        accountFirmEmailColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_email"));
        accountFirmDataTableView.setItems(firmDataObservableList);
    }

    private void populateAccountLocationTableView(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        locationObservableList = FXCollections.observableArrayList();
        ResultSet accountLocationRows = databaseHandler.getLocation();
        try{
            while(accountLocationRows.next()){
                Location location = new Location();
                location.setLocation_city(accountLocationRows.getString("location_city"));
                location.setLocation_street(accountLocationRows.getString("location_street"));
                location.setLocation_postalcode(accountLocationRows.getString("location_postalcode"));
                locationObservableList.add(location);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        accountCityColumn.setCellValueFactory(new PropertyValueFactory<>("location_city"));
        accountStreetColumn.setCellValueFactory(new PropertyValueFactory<>("location_street"));
        accountPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("location_postalcode"));
        accountLocationTableView.setItems(locationObservableList);
    }

    private boolean accountEmailCheck(String accountEmail){
        if(accountEmail.trim().length() > 45 || !accountEmail.trim().matches("^[A-Za-z0-9+_-]+(?:\\.[A-Za-z0-9+_-]+)*@(?:[A-Za-z0-9.-]+\\.)+[a-zA-Z]{2,7}$")){
            AlertBox.display("Nieprawidłowy format email", "Nieprawidłowy format email");
            addAccountFirmEmail.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountPhoneNumberCheck(String accountPhoneNumber){
        if(accountPhoneNumber.trim().length() != 11 || !accountPhoneNumber.trim().matches("^\\d{3}[\\p{javaSpaceChar}]\\d{3}[\\p{javaSpaceChar}]\\d{3}$")){
            AlertBox.display("Nieprawidłowy format numeru telefonu", "Wymagana długość to 9 cyfr");
            addAccountPhoneNumber.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountNipCheck(String accountNip){
        if(accountNip.trim().length() != 10 || !accountNip.trim().matches("^[0-9]+$")){
            AlertBox.display("Nieprawidłowy format NIP", "Wymagana długość to 10 cyfr");
            addAccountNip.setText(null);
        }else{
            return true;
        }
        return false;
    }

     private boolean accountFirmNameCheck(String accountFirmName){
        if(accountFirmName.trim().length() > 45){
            AlertBox.display("Za długa nazwa","Maksymalna ilośc znaków to 45");
            addAccountFirmName.setText(null);
        }else{
            return true;
        }
        return false;
     }

    private boolean accountCityCheck(String accountCity){
        if(accountCity.trim().length() > 45 || !accountCity.trim().matches("^[a-zA-Z\\p{IsAlphabetic}\\d]+(?:[\\s-][a-zA-Z\\p{IsAlphabetic}\\d]+)*$")){
            AlertBox.display("Niepoprawna nazwa","Maksymalna ilośc znaków to 45 liter");
            addAccountCity.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountStreetCheck(String accountStreet){
        if(accountStreet.trim().length() > 45 || !accountStreet.trim().matches("^[a-zA-Z\\p{IsAlphabetic}\\d]+(?:[\\s-][a-zA-Z\\p{IsAlphabetic}\\d]+)[\\p{Blank}]*[0-9]*[a-zA-Z]$")){
            AlertBox.display("Za długa nazwa","Maksymalna ilośc znakoów to 45 na koncu podajemy numer domu/mieszkania");
            addAccountStreet.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean accountPostalCodeCheck(String accountPostalCode){
        if(accountPostalCode.trim().length() != 6 || !accountPostalCode.trim().matches("^[0-9]{2}[-][0-9]{3}$")){
            AlertBox.display("Niepoprawny format kodu pocztowego","przyjmowany format to xx-xxx");
            addAccountPostalCode.setText(null);
        }else{
            return true;
        }
        return false;
    }
}
