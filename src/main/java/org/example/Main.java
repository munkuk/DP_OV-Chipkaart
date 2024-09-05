package org.example;

import org.example.database.DatabaseConnection;
import org.example.database.dao.ReizigerDAO;
import org.example.database.dao.ReizigerDAOPsql;
import org.example.domain.Reiziger;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(DatabaseConnection.getConnection());
        try {
            testReizigerDAO(reizigerDAOPsql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.delete(sietske);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
        System.out.println("Testing updates: ");
        System.out.println("[Test] Eerst: " + rdao.findById(77));
        sietske.setTussenvoegsel("temp");
        rdao.update(sietske);
        System.out.println("[Test] Vervolgens: " + rdao.findById(77) + "\n");

        System.out.println("Testing delete: ");
        reizigers = rdao.findAll();
        System.out.println("[Test] Eerst: " + reizigers.size());
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println("[Test] Vervolgens: " + reizigers.size());
    }

}