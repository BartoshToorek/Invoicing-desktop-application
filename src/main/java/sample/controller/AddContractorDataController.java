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
import sample.Database.Const;
import sample.Database.DatabaseHandler;
import sample.Shaker;
import sample.model.Contractor;
import sample.model.Firmdata;
import sample.model.Location;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class AddContractorDataController {

    @FXML
    private TextField addContractorNip;

    @FXML
    private TextField addContractorFirmName;

    @FXML
    private TextField addContractorLastname;

    @FXML
    private TextField addContractorFirstname;

    @FXML
    private TextField addContractorPhoneNumber;

    @FXML
    private TextField addContractorFirmEmail;

    @FXML
    private TextField addContractorCity;

    @FXML
    private TextField addContractorStreet;

    @FXML
    private TextField addContractorPostalCode;

    @FXML
    private Button addContractorSaveButton;

    @FXML
    private Button addContractorBackButton;

    @FXML
    private Button deleteContractorDataButton;

    @FXML
    private Button editContractorDataButton;

    @FXML
    private Button newContractorDataButton;

    @FXML
    private TableView<Firmdata> firmDataTableView;

    @FXML
    private TableColumn<Firmdata, String> contractorNipColumn;

    @FXML
    private TableColumn<Firmdata, String> contractorFirmNameColumn;

    @FXML
    private TableColumn<Firmdata, String> contractorPhoneNumberColumn;

    @FXML
    private TableColumn<Firmdata, String> contractorEmailColumn;

    @FXML
    private TableColumn<Firmdata , Integer> firmdataIDcolumn;

    @FXML
    private TableView<Location> locationTableView;

    @FXML
    private TableColumn<Location, String> contractorCityColumn;

    @FXML
    private TableColumn<Location, String> contractorStreetColumn;

    @FXML
    private TableColumn<Location, String> contractorPostalCodeColumn;

    @FXML
    private TableColumn<Location , Integer> locationIDcolumn;

    @FXML
    private TableView<Contractor> contractorTableView;

    @FXML
    private TableColumn<Contractor, String> contractorFirstnameColumn;

    @FXML
    private TableColumn<Contractor, String> contractorLastnameColumn;

    @FXML
    private TableColumn<Contractor, Integer> contractorIDcolumn;


    private ObservableList<Contractor> contractorObservableList;
    private ObservableList<Firmdata> firmDataObservableList;
    private ObservableList<Location> locationObservableList;
    ObservableList<Contractor> contractorfordeletion = FXCollections.observableArrayList();
    private boolean EDIT = false, ADD = false;

    @FXML
    void initialize() {

        populateContractorTableView();
        populateFirmDataTableView();
        populateLocationTableView();

        addContractorBackButton.setOnAction(event -> {
            addContractorBackButton.getScene().getWindow().hide();
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

        addContractorSaveButton.setOnAction(event -> {
            if(addContractorNip.getText().trim().isEmpty() || addContractorFirmName.getText().trim().isEmpty() || addContractorLastname.getText().trim().isEmpty()
            || addContractorFirstname.getText().trim().isEmpty() || addContractorPhoneNumber.getText().trim().isEmpty() || addContractorFirmEmail.getText().trim().isEmpty()
            || addContractorCity.getText().trim().isEmpty() || addContractorStreet.getText().trim().isEmpty() || addContractorPostalCode.getText().trim().isEmpty()){
                AlertBox.display("Nieudana próba","Wymagane pola nie mogą być puste");
            }else{
                createContractorData();
            }
        });

        deleteContractorDataButton.setOnAction(event -> {
            deleteContractorData();
        });

        editContractorDataButton.setOnAction(event -> {
            ADD = false;
            EDIT = true;
            editContractorFirmData();
        });

        newContractorDataButton.setOnAction(event -> {
            EDIT = false;
            ADD = true;
            clearFields();
        });


    }

    private void editContractorFirmData(){
        try {
            Contractor contractor = contractorTableView.getSelectionModel().getSelectedItem();
            Firmdata firmdata = firmDataTableView.getSelectionModel().getSelectedItem();
            Location location = locationTableView.getSelectionModel().getSelectedItem();
            addContractorNip.setText(firmdata.getFirmdata_nip());
            addContractorFirmName.setText(firmdata.getFirmdata_name());
            addContractorLastname.setText(contractor.getContractor_lastname());
            addContractorFirstname.setText(contractor.getContractor_firstname());
            addContractorPhoneNumber.setText(firmdata.getFirmdata_phonenr());
            addContractorFirmEmail.setText(firmdata.getFirmdata_email());
            addContractorCity.setText(location.getLocation_city());
            addContractorStreet.setText(location.getLocation_street());
            addContractorPostalCode.setText(location.getLocation_postalcode());
        }catch(NullPointerException e){
            AlertBox.display("Nie wybrano wierszy","Naciśnij na informacje w tabli aby zaznaczyc wiersz");
        }
    }

    private void createContractorData(){
        DatabaseHandler databaseHandler = new DatabaseHandler();

        String contractorNip = addContractorNip.getText().trim();
        String contractorFirmName = addContractorFirmName.getText().trim();
        String contractorLastname = addContractorLastname.getText().trim();
        String contractorFirstname = addContractorFirstname.getText().trim();
        String contractorPhoneNumber = addContractorPhoneNumber.getText().trim();
        String contractorEmail = addContractorFirmEmail.getText().trim();
        String contractorCity = addContractorCity.getText().trim();
        String contractorStreet = addContractorStreet.getText().trim();
        String contractorPostalCode = addContractorPostalCode.getText().trim();

        if(contractorNipCheck(contractorNip) && contractorFirmNameCheck(contractorFirmName) && contractorLastnameCheck(contractorLastname) &&
           contractorFirstnameCheck(contractorFirstname) && contractorPhoneNumberCheck(contractorPhoneNumber) && contractorEmailCheck(contractorEmail) &&
           contractorCityCheck(contractorCity) && contractorStreetCheck(contractorStreet) && contractorPostalCodeCheck(contractorPostalCode)) {
            Contractor contractor = new Contractor();
            contractor.setContractor_firstname(contractorFirstname);
            contractor.setContractor_lastname(contractorLastname);
            Firmdata firmdata = new Firmdata();
            firmdata.setFirmdata_nip(contractorNip);
            firmdata.setFirmdata_name(contractorFirmName);
            firmdata.setFirmdata_email(contractorEmail);
            firmdata.setFirmdata_phonenr(contractorPhoneNumber);
            Location location = new Location();
            location.setLocation_city(contractorCity);
            location.setLocation_street(contractorStreet);
            location.setLocation_postalcode(contractorPostalCode);
            if(EDIT){
                try {
                    Contractor contractorEdit = contractorTableView.getSelectionModel().getSelectedItem();
                    Firmdata firmDataEdit = firmDataTableView.getSelectionModel().getSelectedItem();
                    Location locationEdit = locationTableView.getSelectionModel().getSelectedItem();
                    Integer contractorID = contractorEdit.getContractor_ID();
                    Integer firmDataID = firmDataEdit.getFirmdata_ID();
                    Integer locationID = locationEdit.getLocation_ID();
                    contractor.setContractor_ID(contractorID);
                    firmdata.setFirmdata_ID(firmDataID);
                    location.setLocation_ID(locationID);
                    databaseHandler.updateContractor(contractor);
                    databaseHandler.updateContractorHasFirmdata(firmdata);
                }catch(NullPointerException e){
                    AlertBox.display("Niepoprawna operacja","Uzyj przycisku Nowe dane");
                }
                if(!databaseHandler.DUPLICATE) {
                    databaseHandler.updateContractorHasLocation(location);
                }else{
                    deleteContractorData();
                }
                clearFields();
            }else if(ADD){
                databaseHandler.addContractor(contractor);
                databaseHandler.addAccountHasContractor(contractor);
                databaseHandler.addFirmData(firmdata);
                if(!databaseHandler.DUPLICATE){
                    databaseHandler.addContractorHasFirmdata(firmdata, contractor);
                    databaseHandler.addLocation(location);
                    databaseHandler.addContractorHasLocation(location, contractor);
                }else{
                    contractorfordeletion = contractorTableView.getItems();
                    int temp = contractorfordeletion.lastIndexOf(contractor);
                    Contractor contractorTemp = contractorfordeletion.get(temp);
                    int contractorID = contractorTemp.getContractor_ID();
                    databaseHandler.deleteContractor(contractorID);
                }
                clearFields();
            }
        }else{
            shakeAllFields();
        }
        clearFields();
        populateContractorTableView();
        populateFirmDataTableView();
        populateLocationTableView();
    }

    private void clearFields(){
        addContractorNip.setText(null);
        addContractorFirmName.setText(null);
        addContractorLastname.setText(null);
        addContractorFirstname.setText(null);
        addContractorPhoneNumber.setText(null);
        addContractorFirmEmail.setText(null);
        addContractorCity.setText(null);
        addContractorStreet.setText(null);
        addContractorPostalCode.setText(null);
    }

    private void shakeAllFields(){
        Shaker shakeNip = new Shaker(addContractorNip);
        Shaker shakeFirmName = new Shaker(addContractorFirmName);
        Shaker shakeLastname = new Shaker(addContractorLastname);
        Shaker shakeFirstname = new Shaker(addContractorFirstname);
        Shaker shakerPhoneNumber =  new Shaker(addContractorPhoneNumber);
        Shaker shakeEmail = new Shaker(addContractorFirmEmail);
        Shaker shakeCity = new Shaker(addContractorCity);
        Shaker shakeStreet =  new Shaker(addContractorStreet);
        Shaker shakePostalCode = new Shaker(addContractorPostalCode);

        shakeNip.movement();
        shakeFirmName.movement();
        shakeLastname.movement();
        shakeFirstname.movement();
        shakerPhoneNumber.movement();
        shakeEmail.movement();
        shakeCity.movement();
        shakeStreet.movement();
        shakePostalCode.movement();
    }

    private void deleteContractorData(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        try {
            Contractor contractor = contractorTableView.getSelectionModel().getSelectedItem();
            Firmdata firmdata = firmDataTableView.getSelectionModel().getSelectedItem();
            Location location = locationTableView.getSelectionModel().getSelectedItem();
            Integer contractorID = contractor.getContractor_ID();
            Integer firmDataID = firmdata.getFirmdata_ID();
            Integer locationID = location.getLocation_ID();
            databaseHandler.deleteContractor(contractorID);
            databaseHandler.deleteContractorHasFirmData(firmDataID);
            databaseHandler.deleteContractorHasLocation(locationID);
        }catch(NullPointerException e){
            AlertBox.display("Nie wybrano wierszy", "Kliknij informacje w tabeli aby zaznaczyc wiesz");
        }
        populateLocationTableView();
        populateFirmDataTableView();
        populateContractorTableView();
    }

    private void populateContractorTableView(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        contractorObservableList = FXCollections.observableArrayList();
        ResultSet contractorRows = databaseHandler.getContractor();
        try{
            while(contractorRows.next()){
                Contractor contractor = new Contractor();
                contractor.setContractor_firstname(contractorRows.getString("contractor_firstname"));
                contractor.setContractor_lastname(contractorRows.getString("contractor_lastname"));
                contractor.setContractor_ID(contractorRows.getInt("contractor_id"));
                contractorObservableList.add(contractor);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        contractorFirstnameColumn.setCellValueFactory(new PropertyValueFactory<>("contractor_firstname"));
        contractorLastnameColumn.setCellValueFactory(new PropertyValueFactory<>("contractor_lastname"));
        contractorIDcolumn.setCellValueFactory(new PropertyValueFactory<>("contractor_id"));
        contractorTableView.setItems(contractorObservableList);
    }

    private void populateFirmDataTableView(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        firmDataObservableList = FXCollections.observableArrayList();
        ResultSet contractorFirmDataRows = databaseHandler.getContractorHasFirmdata();
        try{
            while(contractorFirmDataRows.next()){
                Firmdata firmdata = new Firmdata();
                firmdata.setFirmdata_nip(contractorFirmDataRows.getString("firmdata_nip"));
                firmdata.setFirmdata_name(contractorFirmDataRows.getString("firmdata_name"));
                firmdata.setFirmdata_phonenr(contractorFirmDataRows.getString("firmdata_phonenr"));
                firmdata.setFirmdata_email(contractorFirmDataRows.getString("firmdata_email"));
                firmdata.setFirmdata_ID(contractorFirmDataRows.getInt("firmdata_id"));
                firmDataObservableList.add(firmdata);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        contractorNipColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_nip"));
        contractorFirmNameColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_name"));
        contractorPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_phonenr"));
        contractorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_email"));
        firmdataIDcolumn.setCellValueFactory(new PropertyValueFactory<>("firmdata_id"));
        firmDataTableView.setItems(firmDataObservableList);
    }

    private void populateLocationTableView(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        locationObservableList = FXCollections.observableArrayList();
        ResultSet contractorLocationRows = databaseHandler.getContractorHasLocation();
        try{
            while(contractorLocationRows.next()){
                Location location = new Location();
                location.setLocation_city(contractorLocationRows.getString("location_city"));
                location.setLocation_street(contractorLocationRows.getString("location_street"));
                location.setLocation_postalcode(contractorLocationRows.getString("location_postalcode"));
                location.setLocation_ID(contractorLocationRows.getInt("location_id"));
                locationObservableList.add(location);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        contractorCityColumn.setCellValueFactory(new PropertyValueFactory<>("location_city"));
        contractorStreetColumn.setCellValueFactory(new PropertyValueFactory<>("location_street"));
        contractorPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("location_postalcode"));
        locationIDcolumn.setCellValueFactory(new PropertyValueFactory<>("location_id"));
        locationTableView.setItems(locationObservableList);
    }

    private boolean contractorFirstnameCheck(String contractorFirstname){
        if(contractorFirstname.trim().length() > 45 || !contractorFirstname.trim().matches("^[a-zA-Z\\p{IsAlphabetic}\\d\\-\\.\\']*$")){
            AlertBox.display("Nieprawidłowy format imienia","Maksymalna długość to 45 liter");
            addContractorFirstname.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorLastnameCheck(String contractorLastname){
        if(contractorLastname.trim().length() > 45 || !contractorLastname.trim().matches("^[a-zA-Z\\p{IsAlphabetic}\\d\\-\\.\\']*$")){
            AlertBox.display("Nieprawidłowy format imienia","Maksymalna długośc to 45 liter");
            addContractorLastname.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorEmailCheck(String contractorEmail){
        if(contractorEmail.trim().length() > 45 || !contractorEmail.trim().matches("^[A-Za-z0-9+_-]+(?:\\.[A-Za-z0-9+_-]+)*@(?:[A-Za-z0-9.-]+\\.)+[a-zA-Z]{2,7}$")){
            AlertBox.display("Nieprawidłowy format email", "Nieprawidłowy format email");
            addContractorFirmEmail.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorPhoneNumberCheck(String contractorPhoneNumber){
        if(contractorPhoneNumber.trim().length() != 11 || !contractorPhoneNumber.trim().matches("^\\d{3}[\\p{javaSpaceChar}]\\d{3}[\\p{javaSpaceChar}]\\d{3}$")){
            AlertBox.display("Nieprawidłowy format numeru telefonu", "Wymagana długość to 9 cyfr");
            addContractorPhoneNumber.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorNipCheck(String contractorNip){
        if(contractorNip.trim().length() != 10 || !contractorNip.trim().matches("^[0-9]+$")){
            AlertBox.display("Nieprawidłowy format NIP", "Wymagana długość to 10 cyfr");
            addContractorNip.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorFirmNameCheck(String contractorFirmName){
        if(contractorFirmName.trim().length() > 45){
            AlertBox.display("Za długa nazwa","Maksymalna ilośc znaków to 45");
            addContractorFirmName.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorCityCheck(String contractorCity){
        if(contractorCity.trim().length() > 45 || !contractorCity.trim().matches("^[a-zA-Z\\p{IsAlphabetic}\\d]+(?:[\\s-][a-zA-Z\\p{IsAlphabetic}\\d]+)*$")){
            AlertBox.display("Niepoprawna nazwa","Maksymalna ilośc znaków to 45 liter");
            addContractorCity.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorStreetCheck(String contractorStreet){
        if(contractorStreet.trim().length() > 45 || !contractorStreet.matches("^[a-zA-Z\\p{IsAlphabetic}\\d]+(?:[\\s-][a-zA-Z\\p{IsAlphabetic}\\d]+)*[\\p{Blank}]*[0-9][a-zA-Z]$")){
            AlertBox.display("Za długa nazwa","Maksymalna ilośc znakoów to 45");
            addContractorStreet.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean contractorPostalCodeCheck(String contractorPostalCode){
        if(contractorPostalCode.trim().length() != 6 || !contractorPostalCode.trim().matches("^[0-9]{2}[-][0-9]{3}$")){
            AlertBox.display("Niepoprawny format kodu pocztowego","przyjmowany format to xx-xxx");
            addContractorPostalCode.setText(null);
        }else{
            return true;
        }
        return false;
    }



}
