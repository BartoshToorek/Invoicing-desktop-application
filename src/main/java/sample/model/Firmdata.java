package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Firmdata {
    private final StringProperty firmdata_nip = new SimpleStringProperty();
    private final StringProperty firmdata_name = new SimpleStringProperty();
    private final StringProperty firmdata_phonenr = new SimpleStringProperty();
    private final StringProperty firmdata_email = new SimpleStringProperty();
    private final IntegerProperty firmdata_ID = new SimpleIntegerProperty();

    public String getFirmdata_nip() {
        return firmdata_nip.get();
    }

    public StringProperty firmdata_nipProperty() {
        return firmdata_nip;
    }

    public void setFirmdata_nip(String firmdata_nip) {
        this.firmdata_nip.set(firmdata_nip);
    }

    public String getFirmdata_name() {
        return firmdata_name.get();
    }

    public StringProperty firmdata_nameProperty() {
        return firmdata_name;
    }

    public void setFirmdata_name(String firmdata_name) {
        this.firmdata_name.set(firmdata_name);
    }

    public String getFirmdata_phonenr() {
        return firmdata_phonenr.get();
    }

    public StringProperty firmdata_phonenrProperty() {
        return firmdata_phonenr;
    }

    public void setFirmdata_phonenr(String firmdata_phonenr) {
        this.firmdata_phonenr.set(firmdata_phonenr);
    }

    public String getFirmdata_email() {
        return firmdata_email.get();
    }

    public StringProperty firmdata_emailProperty() {
        return firmdata_email;
    }

    public void setFirmdata_email(String firmdata_email) {
        this.firmdata_email.set(firmdata_email);
    }

    public Integer getFirmdata_ID() {return firmdata_ID.get();}

    public IntegerProperty firmdata_IDProperty() {return firmdata_ID;}

    public void setFirmdata_ID(Integer firmdata_ID) {this.firmdata_ID.set(firmdata_ID);}
}
