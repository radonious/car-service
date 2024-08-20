package edu.carservice.repository;

import edu.carservice.model.Car;
import edu.carservice.util.CarCondition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarRepository implements CrudRepository<Car> {

    private DataSource dataSource;

    public CarRepository(DataSource ds) {
        dataSource = ds;
    }

    @Override
    public long count() {
        long result = 0;
        String SQL_QUERY = "select COUNT(*) as result from car_service.car;";
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                ResultSet rs = pst.executeQuery();
        ) {
            if (rs.next()) {
                result = rs.getLong("result");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Car findById(long id) {
        Car result = null;
        String SQL_QUERY = "select * from car_service.car where id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                Integer year = rs.getInt("year");
                Integer price = rs.getInt("price");
                CarCondition condition = CarCondition.valueOf(rs.getString("condition").toUpperCase());
                result = new Car(id, brand, model, year, price, condition);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<Car> findAll() {
        List<Car> result = new ArrayList<>();
        String SQL_QUERY = "select * from car_service.car;";
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                ResultSet rs = pst.executeQuery()
        ) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                Integer year = rs.getInt("year");
                Integer price = rs.getInt("price");
                CarCondition condition = CarCondition.valueOf(rs.getString("condition").toUpperCase());
                result.add(new Car(id, brand, model, year, price, condition));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void save(Car obj) {
        String SQL_QUERY = "insert into car_service.car values (nextval('car_service.car_id_seq'), ?, ?, ?, ?, ?);";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, obj.getBrand());
            pst.setString(2, obj.getModel());
            pst.setInt(3, obj.getYear());
            pst.setInt(4, obj.getPrice());
            pst.setString(5, obj.getCondition().toString());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating failed, no ID obtained.");
                }
            } else {
                throw new SQLException("Creating failed, no records inserted.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    @Override
    public void delete(Car obj) {
        deleteById(obj.getId());
    }

    @Override
    public void deleteById(long id) {
        String SQL_QUERY = "delete  from car_service.car where id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setLong(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(long id) {
        return findById(id) != null;
    }

    @Override
    public void update(Car obj) {
        String SQL_QUERY = "update car_service.car set brand = ?, model = ?, year = ?, price = ?, condition = ? where id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, obj.getBrand());
            pst.setString(2, obj.getModel());
            pst.setInt(3, obj.getYear());
            pst.setInt(4, obj.getPrice());
            pst.setString(5, obj.getCondition().toString());
            pst.setLong(6, obj.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
