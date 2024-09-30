package org.munkuk.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity @Table(name = "ov_chipkaart")
@Getter @AllArgsConstructor @NoArgsConstructor
public class OVChipkaart {

    @Id @Column(name = "kaart_nummer")
    private int id;
    @Setter private Date geldig_tot;
    @Setter private int klasse;
    @Setter private double saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reiziger_id")
    @Setter private Reiziger reiziger;

    public OVChipkaart(int id, Reiziger reiziger, Date geldig_tot, int klasse, double saldo) {
        this.id = id;
        this.reiziger = reiziger;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
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
