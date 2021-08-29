package sample.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductModel {
    String productName;
    String productQuantity;
    String productUnit;
    String productNetto;
    String productVat;
    String productNettoValue;
    String productVatValue;
    String productBruttoValue;
}
