package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction {
    private final IntegerProperty transaction_quantity = new SimpleIntegerProperty();
    private final StringProperty transaction_unit = new SimpleStringProperty();
    private final IntegerProperty transaction_ID = new SimpleIntegerProperty();

    public Integer getTransaction_quantity() {return transaction_quantity.get();}

    public IntegerProperty transaction_quantityType() {return transaction_quantity;}

    public void setTransaction_quantity(Integer transactionQuantity) {this.transaction_quantity.set(transactionQuantity);}

    public String getTransaction_unit() {return transaction_unit.get();}

    public StringProperty transaction_unitType() {return transaction_unit;}

    public void setTransaction_unit(String transaction_unit) {this.transaction_unit.set(transaction_unit);}

    public Integer getTransaction_ID() {return transaction_ID.get();}

    public IntegerProperty transaction_IDType() {return transaction_ID;}

    public void setTransactionID(Integer transactionID) {this.transaction_ID.set(transactionID);}
}
