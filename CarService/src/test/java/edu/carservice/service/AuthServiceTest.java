package edu.carservice.service;

import edu.carservice.util.UserCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AuthServiceTest {

    AuthService authService;

    @BeforeEach
    public void setUp() {
        authService = new AuthService();
    }

    @Test
    public void test1() throws IOException {
        authService.signUp("name", "pass", UserCategory.ADMIN);
        Assertions.assertNotNull( authService.signIn("name", "pass"));
    }
}
