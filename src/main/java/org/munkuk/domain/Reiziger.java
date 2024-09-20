package org.munkuk.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Date;

@Entity @Table(name = "reiziger")
@Getter @AllArgsConstructor @NoArgsConstructor
public class Reiziger {

    @Id @Column(name = "reiziger_id")
    private int id;
    @Setter
    private String voorletters;
    @Setter
    private String tussenvoegsel;
    @Setter
    private String achternaam;
    @Setter
    private Date geboortedatum;

    public String toString() {
        return "Reiziger [id=" + id
                + ", voorletters=" + voorletters
                + ", tussenvoegsel=" + tussenvoegsel
                + ", achternaam=" + achternaam
                + ", geboortedatum=" + geboortedatum
                + "]";
    }
}
