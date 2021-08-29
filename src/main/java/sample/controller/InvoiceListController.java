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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.AlertBox;
import sample.model.Firmdata;
import sample.model.InvoiceFile;


import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InvoiceListController {

    @FXML
    private TableView<InvoiceFile> invoiceListTableView;

    @FXML
    private TableColumn<InvoiceFile, String> invoiceColumn;

    @FXML
    private Button openButton;

    @FXML
    private Button backButton;

    private ObservableList<InvoiceFile> invoiceList = FXCollections.observableArrayList();

    @FXML
    void initialize() {

    populateInvoiceListTableView();

        backButton.setOnAction(event -> {
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

        openButton.setOnAction(event -> {
            openInvoice();
        });

    }

    private void populateInvoiceListTableView() {
        File folder = new File("C:/Users/Bartek/Desktop/inz/FakturowanieApp/faktury");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            InvoiceFile invoiceFile = new InvoiceFile();
            invoiceFile.setFileName(listOfFiles[i].getName());
            invoiceList.add(invoiceFile);
        }
        invoiceColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        invoiceListTableView.setItems(invoiceList);
    }

    private  void openInvoice() {
        InvoiceFile invoiceFile = invoiceListTableView.getSelectionModel().getSelectedItem();
        String target = invoiceFile.getFileName();
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("C:/Users/Bartek/Desktop/inz/FakturowanieApp/faktury/"+target);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                AlertBox.display("Nieprawidłowe żądanie","brak programu do otwarcia pdf");
            }
        }
    }


}