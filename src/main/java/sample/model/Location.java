package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Location {
    private final StringProperty location_city = new SimpleStringProperty();
    private final StringProperty location_street = new SimpleStringProperty();
    private final StringProperty location_postalcode = new SimpleStringProperty();
    private final IntegerProperty location_ID = new SimpleIntegerProperty();

    public String getLocation_city() { return location_city.get(); }

    public StringProperty location_cityProperty() { return location_city; }

    public void setLocation_city(String location_city) { this.location_city.set(location_city); }

    public String getLocation_street() { return location_street.get(); }

    public StringProperty location_streetProperty() { return location_street; }

    public void setLocation_street(String location_street) { this.location_street.set(location_street); }

    public String getLocation_postalcode() { return location_postalcode.get(); }

    public StringProperty location_postalcodeProperty() { return location_postalcode; }

    public void setLocation_postalcode(String location_postalcode) { this.location_postalcode.set(location_postalcode); }

    public Integer getLocation_ID() { return location_ID.get(); }

    public IntegerProperty location_IDProperty() { return location_ID; }

    public void setLocation_ID(Integer location_ID) { this.location_ID.set(location_ID); }

}
