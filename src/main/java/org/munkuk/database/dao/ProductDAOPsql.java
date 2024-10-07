package org.munkuk.database.dao;

import org.munkuk.database.dao.interfaces.ProductDAO;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {

    Connection connection;
    public ProductDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String sqlStatement = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, product.getProduct_nummer());
        preparedStatement.setString(2, product.getNaam());
        preparedStatement.setString(3, product.getBeschrijving());
        preparedStatement.setDouble(4, product.getPrijs());

        // Adds the connection between OVChipkaart -> Product.
        // product.getOvChipkaarts().forEach(ovChipkaart -> ovChipkaart.addProduct(product));

        preparedStatement.executeUpdate();
        preparedStatement.close();

        saveOVChipkaartProduct(product);

        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String sqlStatement = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setString(1, product.getNaam());
        preparedStatement.setString(2, product.getBeschrijving());
        preparedStatement.setDouble(3, product.getPrijs());
        preparedStatement.setInt(4, product.getProduct_nummer());


        preparedStatement.executeUpdate();
        preparedStatement.close();

        deleteOVChipkaartProduct(product);
        saveOVChipkaartProduct(product);

        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        deleteOVChipkaartProduct(product);

        String sqlStatement = "DELETE FROM product WHERE product_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, product.getProduct_nummer());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sqlStatement = """
                SELECT p.* FROM product p
                	JOIN ov_chipkaart_product ocp ON ocp.product_nummer = p.product_nummer
                	WHERE ocp.kaart_nummer = ?;
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, ovChipkaart.getId());

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            products.add(resultSetToProduct(resultSet));
        }

        return products;
    }

    public Product findById(int id) throws SQLException {
        String sqlStatement = "SELECT * FROM product WHERE product_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) return resultSetToProduct(resultSet);
        else return null;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sqlStatement = "SELECT * FROM product";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            products.add(resultSetToProduct(resultSet));
        }

        return products;
    }

    private Product resultSetToProduct(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("product_nummer");
        String naam = resultSet.getString("naam");
        String beschrijving = resultSet.getString("beschrijving");
        double prijs = resultSet.getDouble("prijs");

        return new Product(id, naam, beschrijving, prijs);
    }

    private void saveOVChipkaartProduct(Product product) throws SQLException {
        String sqlStatement = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);


        for (OVChipkaart ovChipkaart : product.getOvChipkaarts()) {
            preparedStatement.setInt(1, ovChipkaart.getId());
            preparedStatement.setInt(2, product.getProduct_nummer());
            preparedStatement.executeUpdate();
        }

        preparedStatement.close();
    }

    private void deleteOVChipkaartProduct(Product product) throws SQLException {
        String sqlStatement = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, product.getProduct_nummer());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

}
