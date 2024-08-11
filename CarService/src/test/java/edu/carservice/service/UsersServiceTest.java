package edu.carservice.service;

import edu.carservice.model.User;
import edu.carservice.util.UserCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class UsersServiceTest {
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        usersService = new UsersService();
    }

    @Test
    void test1() throws IOException {
        User user = new User("name", "pass", UserCategory.ADMIN);
        usersService.addUser(user);
        Assertions.assertEquals(user, usersService.getUser("name"));
        Assertions.assertTrue(usersService.checkPassword("name", "pass"));
    }

    @Test
    void test2() throws IOException {
        User user = new User("name", "oldPass", UserCategory.ADMIN);
        usersService.addUser(user);
        user.setPassword("newPass");
        usersService.updateUser(usersService.getUser("name"));
        Assertions.assertEquals(user, usersService.getUser("name"));
        Assertions.assertTrue(usersService.checkPassword("name", "newPass"));
    }

    @Test
    void test3() throws IOException {
        User user = new User("name", "oldPass", UserCategory.ADMIN);
        usersService.addUser(user);
        Assertions.assertTrue(usersService.isUser("name"));
    }
}
