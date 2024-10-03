package org.munkuk.database.dao.interfaces;

import org.munkuk.domain.Adres;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Product;
import org.munkuk.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;
    boolean update(Product product) throws SQLException;
    boolean delete(Product product) throws SQLException;
    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException;
    List<Product> findAll() throws SQLException;
}
