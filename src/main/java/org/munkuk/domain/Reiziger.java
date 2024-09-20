package org.munkuk.domain;

import jakarta.persistence.*;
import lombok.*;
import org.munkuk.database.dao.interfaces.ReizigerDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "reiziger")
@Getter @AllArgsConstructor @NoArgsConstructor
public class Reiziger {

    @Id @Column(name = "reiziger_id")
    private int id;
    @Setter private String voorletters;
    @Setter private String tussenvoegsel;
    @Setter private String achternaam;
    @Setter private Date geboortedatum;

    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public String toString() {
        return "Reiziger [id=" + id
                + ", voorletters=" + voorletters
                + ", tussenvoegsel=" + tussenvoegsel
                + ", achternaam=" + achternaam
                + ", geboortedatum=" + geboortedatum
                + "]";
    }
}
