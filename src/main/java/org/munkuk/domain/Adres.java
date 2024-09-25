package org.munkuk.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity @Table(name = "adres")
@Getter @AllArgsConstructor @NoArgsConstructor
public class Adres {

//    @Id @Column(name="adres_id")
    private int id;
    @Setter private String postcode;
    @Setter private String huisnummer;
    @Setter private String straat;
    @Setter private String woonplaats;

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "reiziger_id")
    @Setter private Reiziger reiziger;

    public String toString() {
        return "Adres [id=" + id
                + ", postcode=" + postcode
                + ", huisnummer=" + huisnummer
                + ", straat=" + straat
                + ", woonplaats=" + woonplaats
                + ", reiziger_id=" + reiziger.getId()
                + "]";
    }

}
