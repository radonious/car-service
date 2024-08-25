package edu.carservice.repository;

import edu.carservice.model.Order;
import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository implements CrudRepository<Order> {

    private final DataSource dataSource;

    @Autowired
    public OrderRepository(DataSource ds) {
        dataSource = ds;
    }

    @Override
    public long count() {
        long result = 0;
        String SQL_QUERY = "select COUNT(*) as result from car_service.order;";
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
    public Order findById(long id) {
        Order result = null;
        String SQL_QUERY = "select * from car_service.order where id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                Long carId = rs.getLong("car_id");
                OrderState state = OrderState.valueOf(rs.getString("state"));
                OrderCategory category = OrderCategory.valueOf(rs.getString("category"));
                result = new Order(id, userId, carId, state, category);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    public Order findByCarId(long carId) {
        Order result = null;
        String SQL_QUERY = "select * from car_service.order where car_id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setLong(1, carId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long userId = rs.getLong("user_id");
                OrderState state = OrderState.valueOf(rs.getString("state"));
                OrderCategory category = OrderCategory.valueOf(rs.getString("category"));
                result = new Order(id, userId, carId, state, category);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<Order> findAll() {
        List<Order> result = new ArrayList<>();
        String SQL_QUERY = "select * from car_service.order;";
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                ResultSet rs = pst.executeQuery()
        ) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long userId = rs.getLong("user_id");
                Long carId = rs.getLong("car_id");
                OrderState state = OrderState.valueOf(rs.getString("state"));
                OrderCategory category = OrderCategory.valueOf(rs.getString("category"));
                result.add(new Order(id, userId, carId, state, category));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void save(Order obj) {
        String SQL_QUERY = "insert into car_service.order values (nextval('car_service.order_id_seq'), ?, ?, ?, ?);";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setLong(1, obj.getUserId());
            pst.setLong(2, obj.getCarId());
            pst.setString(3, obj.getState().toString());
            pst.setString(4, obj.getCategory().toString());

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
    public void delete(Order obj) {
        deleteById(obj.getId());
    }

    @Override
    public void deleteById(long id) {
        String SQL_QUERY = "delete from car_service.order where id = ?;";
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


    public boolean existsByCar(long carId) {
        return findByCarId(carId) != null;
    }

    @Override
    public void update(Order obj) {
        String SQL_QUERY = "update car_service.order set user_id = ?, car_id = ?, state = ?, category = ? where id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setLong(1, obj.getUserId());
            pst.setLong(2, obj.getCarId());
            pst.setString(3, obj.getState().toString());
            pst.setString(4, obj.getCategory().toString());
            pst.setLong(5, obj.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
