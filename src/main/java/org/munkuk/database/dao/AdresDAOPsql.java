package org.munkuk.database.dao;

import org.munkuk.database.dao.interfaces.AdresDAO;
import org.munkuk.domain.Adres;
import org.munkuk.domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    Connection connection;
    public AdresDAOPsql(Connection connection) { this.connection = connection; }


    @Override
    public boolean save(Adres adres) throws SQLException {
        String sqlStatement = "INSERT INTO adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, adres.getId());
        preparedStatement.setString(2, adres.getPostcode());
        preparedStatement.setString(3, adres.getHuisnummer());
        preparedStatement.setString(4, adres.getStraat());
        preparedStatement.setString(5, adres.getWoonplaats());
        preparedStatement.setInt(6, adres.getReiziger().getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        return true;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        String sqlStatement = """
                UPDATE adres
                SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ?
                WHERE adres_id = ?
               """;

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, adres.getPostcode());
        preparedStatement.setString(2, adres.getHuisnummer());
        preparedStatement.setString(3, adres.getStraat());
        preparedStatement.setString(4, adres.getWoonplaats());
        preparedStatement.setInt(5, adres.getReiziger().getId());
        preparedStatement.setInt(6, adres.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        return true;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        String sqlStatement = "DELETE FROM adres WHERE adres_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, adres.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

        return true;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String sqlStatement = "SELECT * FROM adres WHERE reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, reiziger.getId());

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) return resultSetToAdres(resultSet);
        else return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adresList = new ArrayList<>();
        String sqlStatement = "SELECT * FROM adres";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            adresList.add(resultSetToAdres(resultSet));
        }

        return adresList;
    }

    public Adres resultSetToAdres(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("adres_id");
        String postcode = resultSet.getString("postcode");
        String huisnummer = resultSet.getString("huisnummer");
        String straat = resultSet.getString("straat");
        String woonplaats = resultSet.getString("woonplaats");
        int reizigerId = resultSet.getInt("reiziger_id");

        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(connection);
        Reiziger reiziger = reizigerDAOPsql.findById(reizigerId);
        if (reiziger == null)
            throw new SQLException("Address has no user linked!");

        return new Adres(id, postcode, huisnummer, straat, woonplaats, reiziger);
    }
}
