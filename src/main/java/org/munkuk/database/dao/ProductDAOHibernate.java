package org.munkuk.database.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.munkuk.database.dao.interfaces.ProductDAO;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductDAOHibernate implements ProductDAO {

    Session session;
    public ProductDAOHibernate(Session session) { this.session = session; }

    @Override
    public boolean save(Product product) throws SQLException {
        Transaction transaction = session.beginTransaction();
        product.getOvChipkaarts().forEach(ovChipkaart -> ovChipkaart.addProduct(product));
        session.persist(product);

        transaction.commit();
        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        Transaction transaction = session.beginTransaction();
        session.merge(product);

        transaction.commit();
        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        Transaction transaction = session.beginTransaction();
//        product.getOvChipkaarts().forEach(ovChipkaart -> {
//            System.out.println(ovChipkaart);
//            ovChipkaart.removeProduct(product);
//        });

        List<OVChipkaart> ovChipkaarts = product.getOvChipkaarts();
        for (OVChipkaart ovChipkaart : ovChipkaarts) {
            ovChipkaart.removeProduct(product);
        }

        session.remove(product);
        transaction.commit();
        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        Transaction transaction = session.beginTransaction();
        String hql = "SELECT p FROM Product p JOIN p.ovChipkaarts o WHERE o.id = :ovChipkaartId";
        List<Product> products = session.createQuery(hql, Product.class).setParameter("ovChipkaartId", ovChipkaart.getId()).list();

        transaction.commit();
        return products;
    }

    public Product findById(int id) throws SQLException {
        Transaction transaction = session.beginTransaction();
        Product product = session.get(Product.class, id);
        transaction.commit();
        return product;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        Transaction transaction = session.beginTransaction();
        String hql = "SELECT p FROM Product p";
        List<Product> products = session.createQuery(hql, Product.class).list();
        transaction.commit();
        return products;
    }
}
