package org.example.domain;

import lombok.*;

import java.sql.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reiziger {

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
