package sample.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InvoiceModel {
    String invoiceSerial;
    String invoiceType;
    String invoiceDate;
    String invoiceTransactionDate;
    String invoicePaymentDate;
    String invoicePaymentType;
    String invoiceCity;
    String sellerCity;
    String sellerStreet;
    String sellerPostalCode;
    String sellerBankNumber;
    String sellerNip;
    String sellerFirmName;
    String sellerPhoneNumber;
    String sellerEmail;
    String buyerCity;
    String buyerStreet;
    String buyerPostalCode;
    String buyerNip;
    String buyerFirmName;
    String buyerPhoneNumber;
    String buyerEmail;
    List<ProductModel> productModelList;
    List<VatModel> vatModelList;
    String invoiceBruttoPrice;
}
