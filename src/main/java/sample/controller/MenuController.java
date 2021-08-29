package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button menuAddAccountData;

    @FXML
    private Button menuAddProduct;

    @FXML
    private Button menuAddContractor;

    @FXML
    private Button menuMakeInvoice;

    @FXML
    private Button menuLogout;

    @FXML
    private Button menuQuitButton;

    @FXML
    private Button menuInvoiceListButton;

    @FXML
    void initialize() {


        menuAddAccountData.setOnAction(event -> {
            menuAddAccountData.getScene().getWindow().hide();
            try {
                Parent tableview = FXMLLoader.load(getClass().getResource("/view/accountdata.fxml"));
                Scene tableviewScene = new Scene(tableview);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(tableviewScene);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        menuAddContractor.setOnAction(event -> {
            menuAddContractor.getScene().getWindow().hide();
            try {
                Parent tableview = FXMLLoader.load(getClass().getResource("/view/contractordata.fxml"));
                Scene tableviewScene = new Scene(tableview);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(tableviewScene);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        menuAddProduct.setOnAction(event -> {
            menuAddProduct.getScene().getWindow().hide();
            try {
                Parent tableview = FXMLLoader.load(getClass().getResource("/view/addproduct.fxml"));
                Scene tableviewScene = new Scene(tableview);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(tableviewScene);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuLogout.setOnAction(event -> {
            menuLogout.getScene().getWindow().hide();
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

        menuMakeInvoice.setOnAction(event -> {
            menuMakeInvoice.getScene().getWindow().hide();
            try {
                Parent tableview = FXMLLoader.load(getClass().getResource("/view/addinvoice.fxml"));
                Scene tableviewScene = new Scene(tableview);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(tableviewScene);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuInvoiceListButton.setOnAction(event -> {
            menuMakeInvoice.getScene().getWindow().hide();
            try {
                Parent tableview = FXMLLoader.load(getClass().getResource("/view/invoicelist.fxml"));
                Scene tableviewScene = new Scene(tableview);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(tableviewScene);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
