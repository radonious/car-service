package edu.carservice.service;

import edu.carservice.model.User;
import edu.carservice.util.UserCategory;

import java.io.IOException;

public class AuthService {

    UsersService usersService = new UsersService();

    public void signUp(String name, String password, UserCategory category) throws IOException {
        if (usersService.isUser(name))
            throw new IOException("User with this name already exists.");;

        usersService.addUser(new User(name, password, category));
    }

    public User signIn(String name, String password) throws IOException {
        if (!usersService.isUser(name) && !usersService.checkPassword(name, password))
            throw new IOException("Invalid username or password.");

        return usersService.getUser(name);
    }
}
