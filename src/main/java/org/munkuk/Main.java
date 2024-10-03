package org.munkuk;

import org.hibernate.Session;
import org.munkuk.database.DatabaseConnection;
import org.munkuk.database.HibernateUtil;
import org.munkuk.database.dao.*;
import org.munkuk.database.dao.interfaces.AdresDAO;
import org.munkuk.database.dao.interfaces.OVChipkaartDAO;
import org.munkuk.database.dao.interfaces.ProductDAO;
import org.munkuk.database.dao.interfaces.ReizigerDAO;
import org.munkuk.domain.Adres;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Product;
import org.munkuk.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(DatabaseConnection.getConnection());
        AdresDAOPsql adresDAOPsql = new AdresDAOPsql(DatabaseConnection.getConnection());
        Reiziger daan = new Reiziger(88, "D", "de", "Jong", Date.valueOf("2005-01-01"));

        try (session) {
            ReizigerDAOHibernate reizigerDAOHibernate = new ReizigerDAOHibernate(session);
            OVChipkaartDAOHibernate ovChipkaartDAOHibernate = new OVChipkaartDAOHibernate(session);
            ProductDAOHibernate productDAOHibernate = new ProductDAOHibernate(session);

            if (reizigerDAOPsql.findById(daan.getId()) == null) {
                reizigerDAOPsql.save(daan);
            }

            testReizigerDAO(reizigerDAOPsql);

            testAddressDAO(daan, adresDAOPsql);
            testProductDAO(daan, ovChipkaartDAOHibernate, productDAOHibernate);
            reizigerDAOPsql.delete(daan);

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

    private static void testAddressDAO(Reiziger reiziger, AdresDAO adresDAO) throws SQLException {
        System.out.println("\n---------- Test AddressDAO -------------");
        System.out.println(adresDAO.getClass() + "\n");

        List<Adres> addresses = adresDAO.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende addresses:");
        for (Adres ad : addresses) {
            System.out.println(ad);
        }
        System.out.println();

//        Reiziger daan = new Reiziger(88, "D", "de", "Jong", Date.valueOf("2005-01-01"));
//        reizigerDAO.save(daan);

        System.out.println("[Test] AdresDAO.save() eerst " + addresses.size() + " adresen.");
        Adres address = new Adres(7, "1234AB", "15", "Heidelberglaan", "Utrecht", reiziger);
        adresDAO.save(address);

        addresses = adresDAO.findAll();
        System.out.println("[Test] AdresDAO.save() vervolgens: " + addresses.size() + " adresen.\n");

        System.out.println("[Test] AdresDAO.update() eerst: " + adresDAO.findByReiziger(reiziger));
        address.setPostcode("1337AB");
        adresDAO.update(address);
        System.out.println("[Test] AdresDAO.update() vervolgens: " + adresDAO.findByReiziger(reiziger) + "\n");

        addresses = adresDAO.findAll();
        System.out.println("[Test] AdresDAO.delete() eerst: " + addresses.size());
        adresDAO.delete(address);
        addresses = adresDAO.findAll();
        System.out.println("[Test] AdresDAO.delete() vervolgens: " + addresses.size());
    }

    private static void testProductDAO(Reiziger reiziger, OVChipkaartDAO ovChipkaartDAO, ProductDAO productDAO) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");
        System.out.println(productDAO.getClass() + "\n");

        List<Product> products = productDAO.findAll();
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende products:");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println();

//        productDAO.delete(productDAO.findByOVChipkaart())

        System.out.println("[Test] ProductDAO.save() eerst " + products.size() + " product.");
        OVChipkaart ovChipkaart = new OVChipkaart(100, reiziger, Date.valueOf("2024-12-20"), 2, 100);
        ovChipkaartDAO.save(ovChipkaart);
        List<OVChipkaart> ovChipkaarts = new ArrayList<>();
        ovChipkaarts.add(ovChipkaart);
        Product product = new Product(120, "Studentenkorting", "Gratis rijden tijdens de week", 20.0, ovChipkaarts);
        ovChipkaart.addProduct(product);
        productDAO.save(product);
        products = productDAO.findAll();
        System.out.println("[Test] ProductDAO.save() vervolgens: " + products.size() + " product.\n");

        System.out.println("[Test] ProductDAO.update() eerst " + productDAO.findByOVChipkaart(ovChipkaart) + " product.");
        product.setNaam("Studentkorting");
        productDAO.update(product);
        System.out.println("[Test] ProductDAO.update() vervolgens: " + productDAO.findByOVChipkaart(ovChipkaart) + " product.\n");

        products = productDAO.findAll();
        System.out.println("[Test] ProductDAO.delete() eerst: " + products.size() + " product.");
        productDAO.delete(product);
        products = productDAO.findAll();
        System.out.println("[Test] ProductDAO.delete() vervolgens: " + products.size() + " product.");

        ovChipkaartDAO.delete(ovChipkaart);
    }
}