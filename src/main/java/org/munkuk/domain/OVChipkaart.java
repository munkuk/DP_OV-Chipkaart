package org.munkuk.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "ov_chipkaart")
@Getter @AllArgsConstructor @NoArgsConstructor
public class OVChipkaart {

    @Id @Column(name = "kaart_nummer")
    private int id;
    @Setter private Date geldig_tot;
    @Setter private int klasse;
    @Setter private double saldo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reiziger_id")
    @Setter private Reiziger reiziger;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "OV_Chipkaart_Product",
            joinColumns = @JoinColumn(name = "kaart_nummer"),
            inverseJoinColumns = @JoinColumn(name = "product_nummer"))
    @Setter private List<Product> products = new ArrayList<>();

    public OVChipkaart(int id, Reiziger reiziger, Date geldig_tot, int klasse, int saldo) {
        this.id = id;
        this.reiziger = reiziger;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            product.getOvChipkaarts().add(this);
        }
    }

    public void removeProduct(Product product) {
        if (products.contains(product)) {
            products.remove(product);
//            product.getOvChipkaarts().remove(this); // Causes ConcurrentModificationError :-)
        }
    }

    public String toString() {
        return "OVChipkaart [id=" + id
                + ", reiziger_id=" + reiziger.getId()
                + ", geldig_tot=" + geldig_tot
                + ", klasse=" + klasse
                + ", saldo=" + saldo
                + "]";
    }
}
