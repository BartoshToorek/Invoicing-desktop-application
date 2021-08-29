package sample.model;

import javafx.beans.property.*;

public class Product {
    private final StringProperty product_name = new SimpleStringProperty();
    private final FloatProperty product_netto = new SimpleFloatProperty();
    private final FloatProperty product_vat = new SimpleFloatProperty();
    private final IntegerProperty product_ID = new SimpleIntegerProperty();

    public String getProduct_name() {
        return product_name.get();
    }

    public StringProperty product_nameProperty() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name.set(product_name);
    }

    public float getProduct_netto() {
        return product_netto.get();
    }

    public FloatProperty product_nettoProperty() {
        return product_netto;
    }

    public void setProduct_netto(float product_netto) {
        this.product_netto.set(product_netto);
    }

    public float getProduct_vat() {
        return product_vat.get();
    }

    public FloatProperty product_vatProperty() {
        return product_vat;
    }

    public void setProduct_vat(float product_vat) {
        this.product_vat.set(product_vat);
    }

    public Integer getProduct_ID() { return product_ID.get(); }

    public IntegerProperty product_IDProperty() {return product_ID;}

    public void setProduct_ID(Integer product_ID){ this.product_ID.set(product_ID);}



}
