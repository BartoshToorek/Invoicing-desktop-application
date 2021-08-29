package sample.controller;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.AddPDF;
import sample.AlertBox;
import sample.Database.DatabaseHandler;
import sample.Shaker;
import sample.model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddInvoiceController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Product> productListTableView;

    @FXML
    private TableColumn<Product, String> productListNameColumn;

    @FXML
    private TableColumn<Product, String> productListNettoColumn;

    @FXML
    private TableColumn<Product, String> productListVatColumn;

    @FXML
    private TableColumn<Product, String> productIDListColumn;

    @FXML
    private TableView<Transaction> transactionTableView;

    @FXML
    private TableColumn<Transaction, String> transactionQuantityColumn;

    @FXML
    private TableColumn<Transaction, String> transactionUnitColumn;

    @FXML
    private TextField addTransactionQuantityTextField;

    @FXML
    private TextField addTransactionUnitTextField;

    @FXML
    private Button addProductsButton;

    @FXML
    private Button saveTransactionButton;

    @FXML
    private TextField searchContractorTextField;

    @FXML
    private DatePicker addInvoiceTransactionDatePicker;

    @FXML
    private DatePicker addInvoicePaymentDatePicker;

    @FXML
    private TextField addInvoicePaymentType;

    @FXML
    private ComboBox<String> choseInvoiceType;

    @FXML
    private Button backProductsButton;

    @FXML
    private Button createInvoiceButton;

    @FXML
    private Button backInvoiceButton;

    private ObservableList<Product> productObservableList;
    private ObservableList<Transaction> transactionObservableList = FXCollections.observableArrayList();
    private ObservableList<Product> addedProductObservableList = FXCollections.observableArrayList();
    private ObservableList<Integer> transactionIDObservableList = FXCollections.observableArrayList();
    private ArrayList<String> nettoValueList =  new ArrayList<>();
    private ArrayList<String>  vatValueList = new ArrayList<>();
    private ArrayList<String> bruttoValueList = new ArrayList<>();
    InvoiceModel invoiceModel = new InvoiceModel();
    ProductModel productModel = new ProductModel();
    VatModel vatModel = new VatModel();
    private boolean ADDPRODUCT = true;
    private final String pattern = "dd/MM/yyyy";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    @FXML
    void initialize() {

        populateProductListTableView();
        productListTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        addInvoiceTransactionDatePicker.setConverter(new StringConverter<LocalDate>() {

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        addInvoicePaymentDatePicker.setConverter(new StringConverter<LocalDate>() {

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });


        backInvoiceButton.setOnAction(event -> {
            backInvoiceButton.getScene().getWindow().hide();
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

        choseInvoiceType.getItems().addAll("Faktura VAT","Faktura poprawkowa");

        addProductsButton.setOnAction(event -> {
            populateChosenProductListTableView();
        });

        saveTransactionButton.setOnAction(event -> {
            if(addTransactionQuantityTextField.getText().trim().isEmpty() || addTransactionUnitTextField.getText().trim().isEmpty()){
                AlertBox.display("Nieudana próba","Wymagane pola ilości i jednostki nie moga być puste");
            }else{
                createTransaction();
                addToTransactionObservableList();
            }
        });

        createInvoiceButton.setOnAction(event -> {
            if(searchContractorTextField.getText().trim().isEmpty() || addInvoicePaymentType.getText().trim().isEmpty() || choseInvoiceType.getSelectionModel().getSelectedItem().isEmpty()
                    || addedProductObservableList.isEmpty() || transactionObservableList.isEmpty() || addInvoiceTransactionDatePicker.getValue() == null ||
                    addInvoicePaymentDatePicker == null){
                AlertBox.display("Nieprawidłowe żądanie","Wymagane pola nie mogą byc puste");
            }else{
                addInvoiceToDataBase();
                InvoiceModel invoiceModelToPDF = populateInvoiceModel();
                try {
                    new AddPDF().createPdf(invoiceModelToPDF);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                AlertBox.display("Powodzenie","Utworzono fakture");
            }
        });

        backProductsButton.setOnAction(event -> {
            populateProductListTableView();
            addedProductObservableList.clear();
            ADDPRODUCT = true;
        });
    }

    private void populateProductListTableView() {
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
        productListNameColumn.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        productListNettoColumn.setCellValueFactory(new PropertyValueFactory<>("product_netto"));
        productListVatColumn.setCellValueFactory(new PropertyValueFactory<>("product_vat"));
        productIDListColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productListTableView.setItems(productObservableList);
    }

    private void populateChosenProductListTableView(){
        if(ADDPRODUCT){
            if(productListTableView.getSelectionModel().getSelectedIndices().isEmpty()){
                AlertBox.display("Brak produktów/usług","Aby dodać, wybierz produkty z listy");
            }else{
                for(Integer i : productListTableView.getSelectionModel().getSelectedIndices()){
                    addedProductObservableList.add(productObservableList.get(i));
                }
                productListNameColumn.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                productListNettoColumn.setCellValueFactory(new PropertyValueFactory<>("product_netto"));
                productListVatColumn.setCellValueFactory(new PropertyValueFactory<>("product_vat"));
                productIDListColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
                productListTableView.setItems(addedProductObservableList);
                ADDPRODUCT = false;
            }
        }else{
            Shaker shaker = new Shaker(addProductsButton);
            shaker.movement();
        }
    }


    private void addToTransactionObservableList(){
        String transactionQuantityForCheck = addTransactionQuantityTextField.getText();
        String transactionUnit = addTransactionUnitTextField.getText();
        Transaction transaction = new Transaction();
        try{
            Integer transactionQuantity = Integer.parseInt(transactionQuantityForCheck);
            transaction.setTransaction_quantity(transactionQuantity);
            transaction.setTransaction_unit(transactionUnit);
            transactionObservableList.add(transaction);
        }catch(NullPointerException e){
            AlertBox.display("Nie wybrano wierszy", "Naciśnij na informacje w tabli aby zaznaczyc wiersz");
        }
        transactionQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_quantity"));
        transactionUnitColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_unit"));
        transactionTableView.setItems(transactionObservableList);
        addTransactionQuantityTextField.setText(null);
        addTransactionUnitTextField.setText(null);
        productListTableView.getSelectionModel().clearSelection();
    }

    private void createTransaction(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        String transactionQuantityForCheck = addTransactionQuantityTextField.getText();
        String transactionUnit = addTransactionUnitTextField.getText();
        try{
            Product product = productListTableView.getSelectionModel().getSelectedItem();
            Integer productID = product.getProduct_ID();
            Transaction transaction = new Transaction();
            Integer transactionQuantity = Integer.parseInt(transactionQuantityForCheck);
            transaction.setTransaction_quantity(transactionQuantity);
            transaction.setTransaction_unit(transactionUnit);
            ResultSet transactionIDRows = databaseHandler.addTransaction(transaction);
            try{
                while(transactionIDRows.next()){
                    Integer transactionID = transactionIDRows.getInt(1);
                    databaseHandler.addTransactionHasProduct(transactionID,productID);
                    transactionIDObservableList.add(transactionID);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }catch(NullPointerException e) {
            AlertBox.display("Nie wybrano wierszy", "Naciśnij na informacje w tabli aby zaznaczyc wiersz");
        }
    }

    public static String round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }


    private String calculateInvoiceData(){
        float nettoValue = 0;
        float vatValue = 0;
        float bruttoValue = 0;
        float bruttoPrice = 0;
        for(int i =0 ; i < addedProductObservableList.size() ; i++ ){
            Float productNetto = addedProductObservableList.get(i).getProduct_netto();
            Float productVat = addedProductObservableList.get(i).getProduct_vat();
            Integer transactionQuantity = transactionObservableList.get(i).getTransaction_quantity();
            System.out.println("Przypisane wartosci: "+ productNetto +" | "+ productVat +" | "+ transactionQuantity);
            nettoValue = productNetto * transactionQuantity;
            vatValue = nettoValue * productVat;
            bruttoValue = nettoValue + vatValue;
            System.out.println("Wartosc netto: "+ nettoValue +" Wartośc VAT: "+ vatValue +" Kwota brutto "+bruttoValue);
            nettoValueList.add(round(nettoValue, 2));
            vatValueList.add(round(vatValue,2));
            bruttoValueList.add(round(bruttoValue, 2));
            bruttoPrice += bruttoValue;
        }
        System.out.println("Suma brutto: "+ bruttoPrice);
        return round(bruttoPrice, 2);
    }

    public String addZero(int month) {
        if (month < 10) {
            return "0" + month;
         } else {
           return String.valueOf(month);
        }
    }

    private String createInvoiceSerial(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ResultSet invoiceDateRows = databaseHandler.getInvoiceDatesForSerial();
        List<LocalDate> invoiceDates = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        String invoiceTodayDate = "/" + addZero(todayDate.getMonthValue()) + "/" + todayDate.getYear();
        try{
            while(invoiceDateRows.next()){
                String invoiceDate = invoiceDateRows.getString("invoice_date");
                String[] getDate = invoiceDate.split("/");
                LocalDate invoiceLocalDate = LocalDate.of(Integer.parseInt(getDate[2]), Integer.parseInt(getDate[1]), Integer.parseInt(getDate[0]));
                invoiceDates.add(invoiceLocalDate);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        long invoiceCountPerMonth = invoiceDates.stream().filter(localDate -> localDate.getMonthValue() == todayDate.getMonthValue()).count();
        return ++invoiceCountPerMonth +"-"+ invoiceTodayDate;
    }

    private String getInvoiceSerial(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ResultSet invoiceDateRows = databaseHandler.getInvoiceDatesForSerial();
        List<LocalDate> invoiceDates = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        String invoiceTodayDate = "/" + addZero(todayDate.getMonthValue()) + "/" + todayDate.getYear();
        try{
            while(invoiceDateRows.next()){
                String invoiceDate = invoiceDateRows.getString("invoice_date");
                String[] getDate = invoiceDate.split("/");
                LocalDate invoiceLocalDate = LocalDate.of(Integer.parseInt(getDate[2]), Integer.parseInt(getDate[1]), Integer.parseInt(getDate[0]));
                invoiceDates.add(invoiceLocalDate);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        long invoiceCountPerMonth = invoiceDates.stream().filter(localDate -> localDate.getMonthValue() == todayDate.getMonthValue()).count();
        return invoiceCountPerMonth +"-"+ invoiceTodayDate;
    }

    private void addInvoiceToDataBase(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        LocalDate todayDate = LocalDate.now();
        String invoiceTodayDate = addZero(todayDate.getDayOfMonth())+"/"+addZero(todayDate.getMonthValue())+"/"+todayDate.getYear();
        LocalDate transactionDateValue = addInvoiceTransactionDatePicker.getValue();
        LocalDate paymentDateValue = addInvoicePaymentDatePicker.getValue();
        String invoiceTransactionDate = addZero(transactionDateValue.getDayOfMonth())+"/"+addZero(transactionDateValue.getMonthValue())+"/"+transactionDateValue.getYear();
        String invoicePaymentDate = addZero(paymentDateValue.getDayOfMonth())+"/"+addZero(paymentDateValue.getMonthValue())+"/"+paymentDateValue.getYear();
        String invoicePaymentType = addInvoicePaymentType.getText().trim();
        String invoiceType = choseInvoiceType.getSelectionModel().getSelectedItem();
        if(!addedProductObservableList.isEmpty() && !transactionObservableList.isEmpty()) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceSerial(createInvoiceSerial());
            invoice.setInvoiceType(invoiceType);
            invoice.setInvoiceDate(invoiceTodayDate);
            invoice.setInvoiceTransactionDate(invoiceTransactionDate);
            invoice.setInvoicePaymentDate(invoicePaymentDate);
            invoice.setInvoicePaymentType(invoicePaymentType);
            ResultSet invoiceIDRows = databaseHandler.addInvoice(invoice);
            try {
                while (invoiceIDRows.next()) {
                    Integer invoiceID = invoiceIDRows.getInt(1);
                    databaseHandler.addAccountHasInvoice(invoiceID);
                    addLocationForInvoice(invoiceID);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                addTransactionsForInvoice(invoice);
                addTransactionsForContractor();
            }
        }else{
            shakeAllFields();
        }

    }

    private InvoiceModel populateInvoiceModel(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        String contractorNip = searchContractorTextField.getText().trim();
        ResultSet sellerBankNumber = databaseHandler.getAccountBankNumber();
        ResultSet sellerFirmData = databaseHandler.getFirmData();
        ResultSet sellerLocation = databaseHandler.getLocation();
        ResultSet invoiceData = databaseHandler.getInvoiceData(getInvoiceSerial());
        ResultSet buyerFirmData = databaseHandler.getContractorFirmData(contractorNip);
        ResultSet buyerLocation = databaseHandler.getContractorLocation(contractorNip);
        try{
            while(invoiceData.next()){
                invoiceModel.setInvoiceSerial(invoiceData.getString("invoice_serial"));
                invoiceModel.setInvoiceType(invoiceData.getString("invoice_type"));
                invoiceModel.setInvoiceDate(invoiceData.getString("invoice_date"));
                invoiceModel.setInvoiceTransactionDate(invoiceData.getString("invoice_transaction_date"));
                invoiceModel.setInvoicePaymentDate(invoiceData.getString("invoice_payment_date"));
                invoiceModel.setInvoicePaymentType(invoiceData.getString("invoice_payment_type"));
            }
            while(sellerLocation.next()){
                invoiceModel.setSellerCity(sellerLocation.getString("location_city"));
                invoiceModel.setSellerStreet(sellerLocation.getString("location_street"));
                invoiceModel.setSellerPostalCode(sellerLocation.getString("location_postalcode"));
                invoiceModel.setInvoiceCity(sellerLocation.getString("location_city"));
            }
            while(sellerBankNumber.next()){
                invoiceModel.setSellerBankNumber(sellerBankNumber.getString("account_banknumber"));
            }
            while(sellerFirmData.next()){
                invoiceModel.setSellerNip(sellerFirmData.getString("firmdata_nip"));
                invoiceModel.setSellerFirmName(sellerFirmData.getString("firmdata_name"));
                invoiceModel.setSellerPhoneNumber(sellerFirmData.getString("firmdata_phonenr"));
                invoiceModel.setSellerEmail(sellerFirmData.getString("firmdata_email"));
            }
            while(buyerLocation.next()){
                invoiceModel.setBuyerCity(buyerLocation.getString("location_city"));
                invoiceModel.setBuyerStreet(buyerLocation.getString("location_street"));
                invoiceModel.setBuyerPostalCode(buyerLocation.getString("location_postalcode"));
            }
            while(buyerFirmData.next()){
                invoiceModel.setBuyerNip(buyerFirmData.getString("firmdata_nip"));
                invoiceModel.setBuyerFirmName(buyerFirmData.getString("firmdata_name"));
                invoiceModel.setBuyerPhoneNumber(buyerFirmData.getString("firmdata_phonenr"));
                invoiceModel.setBuyerEmail(buyerFirmData.getString("firmdata_email"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        invoiceModel.setInvoiceBruttoPrice(calculateInvoiceData());
        List<ProductModel> listaProductModel = new ArrayList<>();
        List<VatModel> listaVatModel = new ArrayList<>();
        for(int i = 0 ; i < addedProductObservableList.size() ; i++){
            productModel = new ProductModel();
            vatModel = new VatModel();
            productModel.setProductName(addedProductObservableList.get(i).getProduct_name());
            productModel.setProductNetto(Float.toString(addedProductObservableList.get(i).getProduct_netto()));
            productModel.setProductVat(Float.toString(addedProductObservableList.get(i).getProduct_vat()));
            productModel.setProductNettoValue(nettoValueList.get(i));
            productModel.setProductVatValue(vatValueList.get(i));
            productModel.setProductQuantity(Integer.toString(transactionObservableList.get(i).getTransaction_quantity()));
            productModel.setProductUnit(transactionObservableList.get(i).getTransaction_unit());
            productModel.setProductBruttoValue(bruttoValueList.get(i));
            vatModel.setStawkaVat(Float.toString(addedProductObservableList.get(i).getProduct_vat()));
            vatModel.setKwotaVat(vatValueList.get(i));
            vatModel.setKwotaNetto(nettoValueList.get(i));
            vatModel.setKwotaBrutto(bruttoValueList.get(i));
            listaProductModel.add(productModel);
            listaVatModel.add(vatModel);
        }
        invoiceModel.setProductModelList(listaProductModel);
        invoiceModel.setVatModelList(listaVatModel);
        System.out.println(invoiceModel.toString());
        return invoiceModel;
    }

    private void addLocationForInvoice(Integer invoiceID){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ResultSet locationIDResultSet = databaseHandler.getAccountLocationID();
        try{
            while(locationIDResultSet.next()){
                Integer locationID = locationIDResultSet.getInt("location_id");
                databaseHandler.addInvoiceHasLocation(invoiceID, locationID);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void addTransactionsForInvoice(Invoice invoice){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        for(Integer transactionID : transactionIDObservableList){
                databaseHandler.addInvoiceHasTransaction(invoice, transactionID);
            }
    }

    private void addTransactionsForContractor(){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        String contractorNip = searchContractorTextField.getText().trim();
        ResultSet contractorIDResultSet = databaseHandler.getContractorID(contractorNip);
        if(contractorNipCheck(contractorNip)){
            try{
                while(contractorIDResultSet.next()){
                    Integer contractorID = contractorIDResultSet.getInt("contractor_id");
                    for(Integer transactionID : transactionIDObservableList){
                        databaseHandler.addContractorHasTransaction(contractorID, transactionID);
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else{
            Shaker shakeNip = new Shaker(searchContractorTextField);
            shakeNip.movement();
        }
    }


    private boolean contractorNipCheck(String contractorNip){
        if(contractorNip.trim().length() != 10 || !contractorNip.trim().matches("^[0-9]+$")){
            AlertBox.display("Nieprawidłowy format NIP", "Wymagana długość to 10 cyfr");
            searchContractorTextField.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean transactionQuantityCheck(String transactionQuantity){
        if(transactionQuantity.trim().length() != 3 || !transactionQuantity.trim().matches("^[0-9]+$")){
            AlertBox.display("Nieprawidłowy format ilości", "Wymagana długość to maskymalnie 3 cyfry");
            addTransactionQuantityTextField.setText(null);
        }else{
            return true;
        }
        return false;
    }

    private boolean transactionUnitCheck(String transactionQuantity){
        if(transactionQuantity.trim().length() != 45){
            AlertBox.display("Nieprawidłowy format jednostki", "");
            addTransactionQuantityTextField.setText(null);
        }else{
            return true;
        }
        return false;
    }


    private void shakeAllFields(){
        Shaker shakeNip = new Shaker(searchContractorTextField);
        Shaker shakeInvoiceTransactionDate = new Shaker(addInvoiceTransactionDatePicker);
        Shaker shakeInvoicePaymentDate = new Shaker(addInvoicePaymentDatePicker);
        Shaker shakeInvoicePaymentType = new Shaker(addInvoicePaymentType);

        shakeNip.movement();
        shakeInvoiceTransactionDate.movement();
        shakeInvoicePaymentDate.movement();
        shakeInvoicePaymentType.movement();
    }







}



