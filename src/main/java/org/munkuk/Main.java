package org.munkuk;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.munkuk.database.DatabaseConnection;
import org.munkuk.database.HibernateUtil;
import org.munkuk.database.dao.OVChipkaartDAOHibernate;
import org.munkuk.database.dao.ReizigerDAOHibernate;
import org.munkuk.database.dao.interfaces.OVChipkaartDAO;
import org.munkuk.database.dao.interfaces.ReizigerDAO;
import org.munkuk.database.dao.ReizigersDAOPsql;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Reiziger;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ReizigersDAOPsql reizigerDAOPsql = new ReizigersDAOPsql(DatabaseConnection.getConnection());

        try (session) {
            ReizigerDAOHibernate reizigerDAOHibernate = new ReizigerDAOHibernate(session);
            OVChipkaartDAOHibernate ovChipkaartDAOHibernate = new OVChipkaartDAOHibernate(session);
            testReizigerDAO(reizigerDAOPsql);
            testReizigerDAO(reizigerDAOHibernate);

            testOVChipkaartDAO(reizigerDAOHibernate, ovChipkaartDAOHibernate);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            HibernateUtil.shutdown();
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
        System.out.println(rdao.getClass() + "\n");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        rdao.delete(sietske);
        System.out.print("[Test] ReizigerDAO.save() eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
        System.out.println("[Test] ReizigerDAO.update() eerst: " + rdao.findById(77));
        sietske.setTussenvoegsel("temp");
        rdao.update(sietske);
        System.out.println("[Test] ReizigerDAO.update() vervolgens: " + rdao.findById(77) + "\n");

        reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.delete() eerst: " + reizigers.size());
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.delete() vervolgens: " + reizigers.size());
    }

    private static void testOVChipkaartDAO(ReizigerDAO rdao, OVChipkaartDAO ovcDAO) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        System.out.println(ovcDAO.getClass() + "\n");

        List<OVChipkaart> ovchipkaarts = ovcDAO.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende ovchipkaarts:");
        for (OVChipkaart ov : ovchipkaarts) {
            System.out.println(ov);
        }
        System.out.println();

        Reiziger daan = new Reiziger(88, "D", "de", "Jong", Date.valueOf("2005-01-01"));

        System.out.println("[Test] OVChipkaartDAO.save() eerst " + ovchipkaarts.size() + " ovchipkaarten.");

        OVChipkaart ovChipkaart = new OVChipkaart(12345, daan, Date.valueOf("2024-12-20"), 1, 150);
        rdao.save(daan);
        ovcDAO.save(ovChipkaart);

        ovchipkaarts = ovcDAO.findAll();
        System.out.println("[Test] OVChipkaartDAO.save() vervolgens: " + ovchipkaarts.size() + " ovchipkaarten\n");


        System.out.println("[Test] OVChipkaartDAO.update() eerst: " + ovcDAO.findById(12345));
        ovChipkaart.setGeldig_tot(Date.valueOf("2025-12-20"));
        ovcDAO.update(ovChipkaart);
        System.out.println("[Test] OVChipkaartDAO.update() vervolgens: " + ovcDAO.findById(12345) + "\n");

        ovchipkaarts = ovcDAO.findAll();
        System.out.println("[Test] OVChipkaartDAO.delete() eerst: " + ovchipkaarts.size());
        ovcDAO.delete(ovChipkaart);
        ovchipkaarts = ovcDAO.findAll();
        System.out.println("[Test] OVChipkaartDAO.delete() vervolgens: " + ovchipkaarts.size());

        rdao.delete(daan);
    }
}