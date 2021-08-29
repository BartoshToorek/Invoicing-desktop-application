package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contractor {
    private final StringProperty contractor_firstName = new SimpleStringProperty();
    private final StringProperty contractor_lastName = new SimpleStringProperty();
    private final IntegerProperty contractor_ID = new SimpleIntegerProperty();

    public String getContractor_firstname() {
        return contractor_firstName.get();
    }

    public StringProperty contractor_firstNameProperty() {
        return contractor_firstName;
    }

    public void setContractor_firstname(String contractor_firstName){ this.contractor_firstName.set(contractor_firstName); }

    public String getContractor_lastname() {
        return contractor_lastName.get();
    }

    public StringProperty contractor_lastNameProperty() {
        return contractor_lastName;
    }

    public void setContractor_lastname(String contractor_lastName){ this.contractor_lastName.set(contractor_lastName); }

    public Integer getContractor_ID() { return contractor_ID.get(); }

    public IntegerProperty contractor_IDProperty(){return contractor_ID;}

    public void setContractor_ID(Integer contractor_ID){this.contractor_ID.set(contractor_ID);}
}
