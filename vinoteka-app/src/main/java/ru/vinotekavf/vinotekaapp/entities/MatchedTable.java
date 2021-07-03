package ru.vinotekavf.vinotekaapp.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class MatchedTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Данные о поставщике
    private String provider;
    private String phone;
    private String managerName;

    //Данные о позиции
    private String vendorCode;//артикул
    private String productName;
    private String volume;//объем
    private String releaseYear;
    private String price;
    private String promotionalPrice;//цена по скидке/акции
    private String remainder;//остаток

    //артикул в Festival Vin
    private String fvVendorCode;
    private String fvProductName;
}
