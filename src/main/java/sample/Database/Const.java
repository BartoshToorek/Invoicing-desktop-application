package sample.Database;

public class Const {
    // tables
    public static final String ACCOUNT_TABLE = "Account";
    public static final String ACCOUNT_HAS_FIRMDATA_TABLE = "account_has_firmdata";
    public static final String ACCOUNT_HAS_INVOICE_TABLE = "account_has_invoice";
    public static final String ACCOUNT_HAS_LOCATION_TABLE = "account_has_location";
    public static final String ACCOUNT_HAS_PRODUCT_TABLE = "account_has_product";
    public static final String ACCOUNT_HAS_CONTRACTOR_TABLE = "account_has_contractor";
    public static final String CONTRACTOR_TABLE = "contractor";
    public static final String CONTRACTOR_HAS_FIRMDATA_TABLE = "contractor_has_firmdata";
    public static final String CONTRACTOR_HAS_LOCATION_TABLE = "contractor_has_location";
    public static final String CONTRACTOR_HAS_TRANSACTION_TABLE = "contractor_has_transaction";
    public static final String FIRMDATA_TABLE = "firmdata";
    public static final String INVOICE_TABLE = "invoice";
    public static final String INVOICE_HAS_LOCATION_TABLE = "invoice_has_location";
    public static final String INVOICE_HAS_TRANSACTION_TABLE = "invoice_has_transaction";
    public static final String LOCATION_TABLE = "location";
    public static final String PRODUCT_TABLE  = "product";
    public static final String TRANSACTION_TABLE = "transaction";
    public static final String TRANSACTION_HAS_PRODUCT_TABLE = "transaction_has_product";

    // ACCOUNT column names
    public static final String ACCOUNT_ID = "account_id";
    public static final String ACCOUNT_LOGIN = "account_login";
    public static final String ACCOUNT_PASSWORD = "account_password";
    public static final String ACCOUNT_FIRSTNAME = "account_firstname";
    public static final String ACCOUNT_LASTNAME = "account_lastname";
    public static final String ACCOUNT_BANKNUMBER = "account_banknumber";

    // ACCOUNT_HAS_FIRMDATA column names
    public static final String ACCOUNT_HAS_FIRMDATA_ID = "account_has_firmdata_id";
    public static final String FK_FIRMDATA_ID = "firmdata_firmdata_id";
    public static final String FK_ACCOUNT_ID = "account_account_id";

    // ACCOUNT_HAS_INVOICE column names
    public static final String ACCOUNT_HAS_INVOICE_ID = "account_has_invoice_id";
    //public static final String FK_ACCOUNT_ID = "account_account_id";
    public static final String FK_INVOICE_ID = "invoice_invoice_id";

    // ACCOUNT_HAS_LOCATION column names
    public static final String ACCOUNT_HAS_LOCATION_ID = "account_has_location_id";
    //public static final String FK_ACCOUNT_ID = "account_account_id";
    public static final String FK_LOCATION_ID = "location_location_id";

    //ACCOUNT_HAS_PRODUCT column names
    public static final String ACCOUNT_HAS_PRODUCT_ID = "account_has_product_id";
    public static final String FK_PRODUCT_ID = "product_product_id";
    //public static final String FK_ACCOUNT_ID = "account_account_id"

    //ACCOUNT_HAS_CONTRACTOR column names
    public static final String ACCOUNT_HAS_CONTRACTOR_ID = "account_has_contractor_id";
    public static final String FK_CONTRACTOR_ID = "contractor_contractor_id";
    //public static final String FK_ACCOUNT_ID = "account_account_id"

    // CONTRACTOR column names
    public static final String CONTRACTOR_ID = "contractor_id";
    public static final String CONTRACTOR_FIRSTNAME = "contractor_firstname";
    public static final String CONTRACTOR_LASTNAME = "contractor_lastname";

    // CONTRACTOR_HAS_FIRMDATA column names
    public static final String CONTRACTOR_HAS_FIRMDATA_ID = "contractor_has_firmdata_id";
    //public static final String FK_CONTRACTOR_ID = "contractor_contractor_id";
    //public static final String FK_FIRMDATA_ID = "firmdata_firmdata_id";

    // CONTRACTOR_HAS_LOCATION column names
    public static final String CONTRACTOR_HAS_LOCATION_ID = "contractor_has_location_id";
    //public static final String FK_CONTRACTOR_ID = "contractor_contractor_id";
    //public static final String FK_LOCATION_ID = "location_location_id";

    // CONTRACTOR_HAS_TRANSACTION column names
    public static final String CONTRACTOR_HAS_TRANSACTION_ID = "contractor_has_transaction_id";
    //public static final String FK_CONTRACTOR_ID = "contractor_contractor_id";
    //public static final String FK_LOCATION_ID = "location_location_id";

    // FIRMDATA column names
    public static final String FIRMDATA_ID = "firmdata_id";
    public static final String FIRMDATA_NIP = "firmdata_nip";
    public static final String FIRMDATA_NAME = "firmdata_name";
    public static final String FIRMDATA_PHONE_NUMBER = "firmdata_phonenr";
    public static final String FIRMDATA_EMAIL = "firmdata_email";

    // INVOICE column names
    public static final String INVOICE_ID = "invoice_id";
    public static final String INVOICE_SERIAL = "invoice_serial";
    public static final String INVOICE_TYPE = "invoice_type";
    public static final String INVOICE_DATE = "invoice_date";
    public static final String INVOICE_TRANSACTION_DATE = "invoice_transaction_date";
    public static final String INVOICE_PAYMENT_DATE = "invoice_payment_date";
    public static final String INVOICE_PAYMENT_TYPE = "invoice_payment_type";

    // INVOICE_HAS_LOCATION column names
    public static final String INVOICE_HAS_LOCATION_ID = "invoice_has_location_id";
    //public static final String FK_INVOICE_ID = "invoice_invoice_id";
    //public static final String FK_LOCATION_ID = "location_location_id";

    //INVOICE_HAS_TRANSACTION column names
    public static final String INVOICE_HAS_TRANSACTION_ID = "invoice_has_transaction_id";
    public static final String FK_TRANSACTION_ID = "transaction_transaction_id";
    //public static final String FK_INVOICE_ID = "invoice_invoice_id";

    //LOCATION column names
    public static final String LOCATION_ID = "location_id";
    public static final String LOCATION_CITY = "location_city";
    public static final String LOCATION_STREET = "location_street";
    public static final String LOCATION_POSTALCODE = "location_postalcode";

    //PRODUCT column names
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_NETTO = "product_netto";
    public static final String PRODUCT_VAT = "product_vat";

    //TRANSACTION column name
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_QUANTITY = "transaction_quantity";
    public static final String TRANSACTION_UNIT = "transaction_unit";


    //TRANSACTION_HAS_PRODUCT column name
    public static final String TRANSACTION_HAS_PRODUCT_ID = "transaction_has_product_id";
    //public static final String FK_TRANSACTION_ID = "transaction_transaction_id";
    //public static final String FK_PRODUCT_ID = "product_product_id";













}
