package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Invoice {
    private final StringProperty invoice_serial = new SimpleStringProperty();
    private final StringProperty invoice_type = new SimpleStringProperty();
    private final StringProperty invoice_date = new SimpleStringProperty();
    private final StringProperty invoice_transaction_date = new SimpleStringProperty();
    private final StringProperty invoice_payment_date = new SimpleStringProperty();
    private final StringProperty invoice_payment_type = new SimpleStringProperty();
    private final IntegerProperty invoice_ID = new SimpleIntegerProperty();

    public String getInvoiceSerial() {return invoice_serial.get();}

    public StringProperty invoice_serialType() {return invoice_serial;}

    public void setInvoiceSerial(String invoice_serial) {this.invoice_serial.set(invoice_serial);}

    public String getInvoiceType() {return invoice_type.get();}

    public StringProperty invoice_typeType() {return invoice_type;}

    public void setInvoiceType(String invoice_type) {this.invoice_type.set(invoice_type);}

    public String getInvoiceDate() {return invoice_date.get();}

    public StringProperty invoice_dateType() {return invoice_date;}

    public void setInvoiceDate(String invoice_date) {this.invoice_date.set(invoice_date);}

    public String getInvoiceTransactionDate() {return invoice_transaction_date.get();}

    public StringProperty invoice_transaction_dateType() {return invoice_transaction_date;}

    public void setInvoiceTransactionDate(String invoice_transaction_date) {this.invoice_transaction_date.set(invoice_transaction_date);}

    public String getInvoicePaymentDate() {return invoice_payment_date.get();}

    public StringProperty getInvoice_payment_dateType() {return invoice_payment_date;}

    public void setInvoicePaymentDate(String invoice_payment_date) {this.invoice_payment_date.set(invoice_payment_date);}

    public String getInvoicePaymentType() {return invoice_payment_type.get();}

    public StringProperty invoice_payment_typeType() {return invoice_payment_type;}

    public void setInvoicePaymentType(String invoice_payment_type) {this.invoice_payment_type.set(invoice_payment_type);}

    public Integer getInvoice_ID() { return invoice_ID.get(); }

    public IntegerProperty invoice_IDProperty() {return invoice_ID;}

    public void setInvoice_ID(Integer invoice_ID){ this.invoice_ID.set(invoice_ID);}



}
