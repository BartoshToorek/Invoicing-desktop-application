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
import sample.model.Contractor;
import sample.model.Firmdata;
import sample.model.Location;
import sample.model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddProductController {

    @FXML
    private TextField addProductName;

    @FXML
    private TextField addProdcutNetto;

    @FXML
    private TextField addProductVat;

    @FXML
    private Button addProductSaveButton;

    @FXML
    private Button addProductBackButton;

    @FXML
    private Button newProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button editProductButton;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> productNettoColumn;

    @FXML
    private TableColumn<Product, String> productVatColumn;

    @FXML
    private TableColumn<Product, String> productIDColumn;


    private ObservableList<Product> productObservableList;
    private boolean ADD = false, EDIT = false;

    @FXML
    void initialize() {

     populateProductTableView();

    addProductBackButton.setOnAction(event -> {
        addProductBackButton.getScene().getWindow().hide();
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

    addProductSaveButton.setOnAction(event -> {
        if(addProductName.getText().trim().isEmpty() || addProdcutNetto.getText().trim().isEmpty() || addProductVat.getText().trim().isEmpty()){
            AlertBox.display("Nieudana próba","Wymagane pola nie mogą być puste");
        }else{
            try{
                createProduct();
                populateProductTableView();
            }catch(NumberFormatException e){
                AlertBox.display("Nieprawdłowy format danych"," Wartości netto i vat musza byc numeryczne, a częsci dziesiętne oddzielone znakiem . ");
                Shaker shakeProductNetto = new Shaker(addProdcutNetto);
                Shaker shakeProductVat = new Shaker(addProductVat);
                shakeProductVat.movement();
                shakeProductNetto.movement();
            }
        }
    });

    deleteProductButton.setOnAction(event -> {
        deleteProduct();
    });

    editProductButton.setOnAction(event -> {
        ADD = false;
        EDIT = true;
        editProduct();
    });

    newProductButton.setOnAction(event -> {
        EDIT = false;
        ADD = true;
        clearFields();
    });

    }

    private void editProduct(){
        try {
            Product product = productTableView.getSelectionModel().getSelectedItem();
            addProductName.setText(product.getProduct_name());
            addProdcutNetto.setText(Float.toString(product.getProduct_netto()));
            addProductVat.setText(Float.toString(product.getProduct_vat()));
        }catch(NullPointerException e){
            AlertBox.display("Nie wybrano wierszy","Naciśnij na informacje w tabli aby zaznaczyc wiersz");
        }
    }

    private void deleteProduct(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        try {
            Product product = productTableView.getSelectionModel().getSelectedItem();
            Integer productID = product.getProduct_ID();
            databaseHandler.deleteProduct(productID);
        }catch(NullPointerException e){
            AlertBox.display("Nie wybrano wierszy", "Kliknij informacje w tabeli aby zaznaczyc wiesz");
        }
        populateProductTableView();
    }

    private void createProduct(){
        DatabaseHandler databaseHandler = new DatabaseHandler();

        String productName = addProductName.getText().trim();
        String productNettoForCheck = addProdcutNetto.getText().trim();
        String productVatForCheck = addProductVat.getText().trim();
        if(productNameCheck(productName) && productNettoCheck(productNettoForCheck) && productVatCheck(productVatForCheck)) {
            float productNetto = Float.parseFloat(productNettoForCheck);
            float productVat = Float.parseFloat(productVatForCheck);
            //System.out.println(productVat);
            Product product = new Product();
            product.setProduct_name(productName);
            product.setProduct_netto(productNetto);
            product.setProduct_vat(productVat);
            if(EDIT){
                try{
                    Product productforID = productTableView.getSelectionModel().getSelectedItem();
                    Integer productID = productforID.getProduct_ID();
                    databaseHandler.updateProduct(product, productID);
                }catch(NullPointerException e){
                    e.printStackTrace();
                }

            }else if(ADD){
                databaseHandler.AddProduct(product);
                databaseHandler.addAccountHasProduct(product);
            }
            clearFields();
        }else{
            Shaker shakeProductName = new Shaker(addProductName);
            shakeProductName.movement();
        }
    }

    private void clearFields(){
        addProductName.setText(null);
        addProdcutNetto.setText(null);
        addProductVat.setText(null);
    }

    private void populateProductTableView() {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        productObservableList = FXCollections.observableArrayList();
        ResultSet productRows = databaseHandler.getProduct();
        try{
            while(productRows.next()){
                Product product = new Product();
                product.setProduct_name(productRows.getString("product_name"));
                product.setProduct_netto(productRows.getFloat("product_netto"));
                product.setProduct_vat(productRows.getFloat("product_vat"));
                product.setProduct_ID(productRows.getInt("product_id"));
                productObservableList.add(product);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        productNettoColumn.setCellValueFactory(new PropertyValueFactory<>("product_netto"));
        productVatColumn.setCellValueFactory(new PropertyValueFactory<>("product_vat"));
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productTableView.setItems(productObservableList);
    }

    private boolean productNameCheck(String productName){
        if(productName.trim().length() > 45){
            AlertBox.display(" Za długa nazwa","Maksymalna ilośc znakoów to 45 ");
            addProductName.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean productNettoCheck(String productNetto){
        if(productNetto.trim().length() > 19 || !productNetto.trim().matches("^\\d{1,6}(\\.\\d{1,2})?$")){
            AlertBox.display("Nieprawidłowy format","Maksymalna długośc to 19 cyfr w tym czesci dziesietne");
            addProductName.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean productVatCheck(String productVat){
        if(productVat.trim().length() > 5 || !productVat.trim().matches("^[0](\\.\\d{1,2})?$")){
            AlertBox.display("Nieprawidłowy format","Maksymalna dlugość to 5 przyjmowany format to 0.xx");
            addProductName.setText(null);
        }else{
            return true;
        }
        return false;
    }


}





