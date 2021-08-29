package sample.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VatModel {

    String stawkaVat;
    String kwotaNetto;
    String kwotaVat;
    String kwotaBrutto;

}