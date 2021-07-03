package ru.vinotekavf.vinotekaapp.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private String vendorCode;//артикул
    private String productName;
    private String volume;//объем
    private String releaseYear;
    private String price;
    private String promotionalPrice;//цена по скидке/акции
    private String remainder;//остаток
    private String maker;

    //артикул в Festival Vin
    private String fvVendorCode;
    private String fvProductName;

    public Position() {
    }

    public Position(String vendorCode, String productName, String volume, String releaseYear, String price, String promotionalPrice, String remainder) {
        this.vendorCode = vendorCode;
        this.productName = productName;
        this.volume = volume;
        this.releaseYear = releaseYear;
        this.price = price;
        this.promotionalPrice = promotionalPrice;
        this.remainder = remainder;
    }

    public Position(Provider provider, String vendorCode, String productName, String volume, String releaseYear, String price, String promotionalPrice, String remainder) {
        this.provider = provider;
        this.vendorCode = vendorCode;
        this.productName = productName;
        this.volume = volume;
        this.releaseYear = releaseYear;
        this.price = price;
        this.promotionalPrice = promotionalPrice;
        this.remainder = remainder;
    }
}
