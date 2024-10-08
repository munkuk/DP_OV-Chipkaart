package org.munkuk.database.dao;

import org.munkuk.database.dao.interfaces.OVChipkaartDAO;
import org.munkuk.database.dao.interfaces.ProductDAO;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Product;
import org.munkuk.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    Connection connection;
    public OVChipkaartDAOPsql(Connection connection) { this.connection = connection; }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        String sqlStatement = "insert into ov_chipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) values (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, ovChipkaart.getId());
        preparedStatement.setDate(2, ovChipkaart.getGeldig_tot());
        preparedStatement.setInt(3, ovChipkaart.getKlasse());
        preparedStatement.setDouble(4, ovChipkaart.getSaldo());
        preparedStatement.setInt(5, ovChipkaart.getReiziger().getId());

//        ProductDAOPsql productDAOPsql = new ProductDAOPsql(connection);
//        List<Product> products = productDAOPsql.findByOVChipkaart(ovChipkaart);
//        products.forEach(ovChipkaart::addProduct);

        if (ovChipkaart.getReiziger() != null) {
            ovChipkaart.getReiziger().getOvChipkaarten().add(ovChipkaart);
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(connection);
            if (reizigerDAOPsql.findById(ovChipkaart.getReiziger().getId()) == null) {
                reizigerDAOPsql.save(ovChipkaart.getReiziger());
            }
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();

        saveOVChipkaartProduct(ovChipkaart);
        return true;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        String sqlStatement = """
                update ov_chipkaart
                set geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ?
                where kaart_nummer = ?
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setDate(1, ovChipkaart.getGeldig_tot());
        preparedStatement.setInt(2, ovChipkaart.getKlasse());
        preparedStatement.setDouble(3, ovChipkaart.getSaldo());
        preparedStatement.setInt(4, ovChipkaart.getReiziger().getId());
        preparedStatement.setInt(5, ovChipkaart.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        deleteOVChipkaartProduct(ovChipkaart);
        saveOVChipkaartProduct(ovChipkaart);
        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        deleteOVChipkaartProduct(ovChipkaart);

        String sqlStatement = "delete from ov_chipkaart where kaart_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, ovChipkaart.getId());

        if (ovChipkaart.getReiziger() != null) {
            ovChipkaart.getReiziger().getOvChipkaarten().remove(ovChipkaart);

            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(connection);
            reizigerDAOPsql.update(ovChipkaart.getReiziger());
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        String sqlStatement = "select * from ov_chipkaart where kaart_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) return resultSetToOVChipkaart(resultSet);
        else return null;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        List<OVChipkaart> ovChipkaarts = new ArrayList<>();

        String sqlStatement = "select * from ov_chipkaart where reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, reiziger.getId());

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ovChipkaarts.add(resultSetToOVChipkaart(resultSet));
        }
        return ovChipkaarts;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> ovChipkaarts = new ArrayList<>();

        String sqlStatement = "select * from ov_chipkaart";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ovChipkaarts.add(resultSetToOVChipkaart(resultSet));
        }
        return ovChipkaarts;
    }

    public OVChipkaart resultSetToOVChipkaart(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("kaart_nummer");
        Date geldig_tot = resultSet.getDate("geldig_tot");
        int klasse = resultSet.getInt("klasse");
        int saldo = resultSet.getInt("saldo");
        int reiziger_id = resultSet.getInt("reiziger_id");

        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(connection);
        Reiziger reiziger = reizigerDAOPsql.findById(reiziger_id);

        return new OVChipkaart(id, reiziger, geldig_tot, klasse, saldo);
    }

    private void saveOVChipkaartProduct(OVChipkaart ovChipkaart) throws SQLException {
        String sqlStatement = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        for (Product product : ovChipkaart.getProducts()) {
            preparedStatement.setInt(1, ovChipkaart.getId());
            preparedStatement.setInt(2, product.getProduct_nummer());
            preparedStatement.executeUpdate();
        }

//        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    private void deleteOVChipkaartProduct(OVChipkaart ovChipkaart) throws SQLException {
        String sqlStatement = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, ovChipkaart.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
