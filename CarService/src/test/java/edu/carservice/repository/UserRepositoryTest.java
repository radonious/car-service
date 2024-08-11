package edu.carservice.repository;

import edu.carservice.model.User;
import edu.carservice.util.UserCategory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testng.annotations.Test;

import java.util.List;

public class UserRepositoryTest {

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    public UserRepository userRepository;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    public void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        userRepository = new UserRepository(connectionProvider.getDataSource());
    }

    @Test
    void shouldGetAllUser() {
        userRepository.save(new User(null, "some_user", "some_pass", UserCategory.CLIENT));
        userRepository.save(new User(null, "another_user", "another_pass", UserCategory.ADMIN));

        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void shouldGetSecondUser() {
        userRepository.save(new User(null, "some_user", "some_pass", UserCategory.CLIENT));
        userRepository.save(new User(null, "another_user", "another_pass", UserCategory.ADMIN));

        User user = userRepository.findById(2);
        Assertions.assertEquals(user.getName(), "another_user");
        Assertions.assertEquals(user.getPassword(), "another_pass");
    }

    @Test
    void shouldFindUserByName() {
        userRepository.save(new User(null, "some_user", "some_pass", UserCategory.CLIENT));
        userRepository.save(new User(null, "another_user", "another_pass", UserCategory.ADMIN));

        boolean res = userRepository.existsByName("another_user");
        Assertions.assertTrue(res);
    }

    @Test
    void shouldUpdatePassword() {
        User user = new User(null, "another_user", "another_pass", UserCategory.ADMIN);
        userRepository.save(new User(null, "some_user", "some_pass", UserCategory.CLIENT));
        userRepository.save(user);

        user.setPassword("12345");

        userRepository.update(user);
        userRepository.findByName("some_user");
        Assertions.assertEquals(user.getPassword(), "12345");
    }
}
