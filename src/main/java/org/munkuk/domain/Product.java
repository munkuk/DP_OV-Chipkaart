package org.munkuk.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity @Table(name = "product")
@Getter @AllArgsConstructor @NoArgsConstructor
public class Product {

    @Id
    private int product_nummer;
    @Setter private String naam;
    @Setter private String beschrijving;
    @Setter private double prijs;

    @ManyToMany(mappedBy = "products")
    private List<OVChipkaart> ovChipkaarts = new ArrayList<>();

    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public String toString() {
        return "Product [product_nummer=" + product_nummer + ", naam=" + naam + ", beschrijving=" + beschrijving + ", prijs=" + prijs + "]";
    }
}
