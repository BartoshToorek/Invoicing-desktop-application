package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceFile {
    StringProperty fileName = new SimpleStringProperty();

    public String getFileName() {return fileName.get();}

    public StringProperty fileNameProperty() {return fileName;}

    public void setFileName(String fileName) {this.fileName.set(fileName);}

}
