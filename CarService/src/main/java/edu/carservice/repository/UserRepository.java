package edu.carservice.repository;

import edu.carservice.model.User;
import edu.carservice.util.UserCategory;
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
public class UserRepository implements CrudRepository<User> {

    private final DataSource dataSource;

    @Autowired
    public UserRepository(DataSource ds) {
        dataSource = ds;
    }

    @Override
    public long count() {
        long result = 0;
        String SQL_QUERY = "select COUNT(*) as result from car_service.user;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getLong("result");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public User findById(long id) {
        User result = null;
        String SQL_QUERY = "select * from car_service.user where id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                UserCategory category = UserCategory.valueOf(rs.getString("category").toUpperCase());
                result = new User(id, username, password, category);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    public User findByName(String username) {
        User result = null;
        String SQL_QUERY = "select * from car_service.user where username = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String password = rs.getString("password");
                UserCategory category = UserCategory.valueOf(rs.getString("category").toUpperCase());
                result = new User(id, username, password, category);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        String SQL_QUERY = "select * from car_service.user;";
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                ResultSet rs = pst.executeQuery()
        ) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                UserCategory category = UserCategory.valueOf(rs.getString("category").toUpperCase());
                result.add(new User(id, username, password, category));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void save(User obj) {
        String SQL_QUERY = "insert into car_service.user values (nextval('car_service.user_id_seq'), ?, ?, ?);";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getPassword());
            pst.setString(3, obj.getCategory().toString());

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
    public void delete(User obj) {
        deleteById(obj.getId());
    }

    @Override
    public void deleteById(long id) {
        String SQL_QUERY = "delete from car_service.user where id = ?;";
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

    public boolean existsByName(String username) {
        return findByName(username) != null;
    }

    @Override
    public void update(User obj) {
        String SQL_QUERY = "update car_service.user set username = ?, password = ?, category = ? where id = ?;";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getPassword());
            pst.setString(3, obj.getCategory().toString());
            pst.setLong(4, obj.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
