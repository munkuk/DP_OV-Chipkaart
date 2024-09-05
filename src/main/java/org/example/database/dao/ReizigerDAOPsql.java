package org.example.database.dao;

import org.example.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    Connection connection;
    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }


    @Override
    public boolean save(Reiziger reiziger) {
        boolean result = false;

        String sql = "insert into reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) values(?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, reiziger.getId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, reiziger.getGeboortedatum());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            result = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        boolean result = false;

        String sql = "update reiziger set voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? where reiziger_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reiziger.getVoorletters());
            preparedStatement.setString(2, reiziger.getTussenvoegsel());
            preparedStatement.setString(3, reiziger.getAchternaam());
            preparedStatement.setDate(4, reiziger.getGeboortedatum());
            preparedStatement.setInt(5, reiziger.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            result = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        boolean result = false;
        String sql = "delete from reiziger where reiziger_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, reiziger.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            result = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Reiziger findById(int id) {
        Reiziger reiziger = null;

        String sql = "select * from reiziger where reiziger_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                reiziger = resultSetToReiziger(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) {
        String sql = "select * from reiziger where geboortedatum = ?";
        List<Reiziger> reizigers = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reizigers.add(resultSetToReiziger(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {
        String sql = "select * from reiziger";
        List<Reiziger> reizigers = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reizigers.add(resultSetToReiziger(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reizigers;
    }

    public Reiziger resultSetToReiziger(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("reiziger_id");
        String voorletters = resultSet.getString("voorletters");
        String tussenvoegsel = resultSet.getString("tussenvoegsel");
        String achternaam = resultSet.getString("achternaam");
        java.sql.Date geboortedatum = resultSet.getDate("geboortedatum");

        return new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
    }

}
