package org.munkuk.database.dao;

import org.munkuk.database.dao.interfaces.ReizigerDAO;
import org.munkuk.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigersDAOPsql implements ReizigerDAO {

    Connection connection;
    public ReizigersDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String sqlStatement = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) values(?, ?, ?, ? ,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, reiziger.getId());
        preparedStatement.setString(2, reiziger.getVoorletters());
        preparedStatement.setString(3, reiziger.getTussenvoegsel());
        preparedStatement.setString(4, reiziger.getAchternaam());
        preparedStatement.setDate(5, reiziger.getGeboortedatum());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String sqlStatement = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setString(1, reiziger.getVoorletters());
        preparedStatement.setString(2, reiziger.getTussenvoegsel());
        preparedStatement.setString(3, reiziger.getAchternaam());
        preparedStatement.setDate(4, reiziger.getGeboortedatum());
        preparedStatement.setInt(5, reiziger.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        String sqlStatement = "DELETE FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, reiziger.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

        return false;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String sqlStatement = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) return resultSetToReiziger(resultSet);
        else return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws SQLException {
        String sqlStatement = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        List<Reiziger> reizigers = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            reizigers.add(resultSetToReiziger(resultSet));
        }

        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String sqlStatement = "SELECT * FROM reiziger";
        List<Reiziger> reizigers = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            reizigers.add(resultSetToReiziger(resultSet));
        }
        return reizigers;
    }

    public Reiziger resultSetToReiziger(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("reiziger_id");
        String voorletters = resultSet.getString("voorletters");
        String tussenvoegsel = resultSet.getString("tussenvoegsel");
        String achternaam = resultSet.getString("achternaam");
        Date geboortedatum = resultSet.getDate("geboortedatum");

        return new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
    }
}
