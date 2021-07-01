package ru.vinotekavf.vinotekaapp.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class VendorCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //поставщик
    private String provider;

    private String phone;

    private String managerName;

    //артикул
    private String vendorCode;

    private String productName;

    //объем
    private String volume;

    private String releaseYear;

    private String price;

    //цена по скидке/акции
    private String promotionalPrice;

    //остаток
    private String remainder;
}
