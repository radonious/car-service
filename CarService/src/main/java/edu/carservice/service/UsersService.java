package edu.carservice.service;

import edu.carservice.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class UsersService {
    static private final HashMap<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getName(), user);
    }

    public void updateUser(User user) {
        users.put(user.getName(), user);
    }

    public boolean checkPassword(String name, String password) throws IOException {
        if (!users.containsKey(name)) throw new IOException("No such user.");
        return  users.get(name).getPassword().equals(password);
    }

    public boolean isUser(String name) {
        return users.containsKey(name);
    }

    public User getUser(String name) throws IOException {
        if (!users.containsKey(name)) throw new IOException("No such user.");
        return users.get(name);
    }

    public void displayUsers() {
        for (User user : users.values()) {
            System.out.println(user);
        }
    }

    public void displayOrderedByName() {
        ArrayList<User> array = new ArrayList<>(users.values());
        array.sort(Comparator.comparing(User::getName));
        for (User user : array) {
            System.out.println(user);
        }
    }

    public void displayOrderedByCategory() {
        ArrayList<User> array = new ArrayList<>(users.values());
        array.sort(Comparator.comparing(User::getCategory));
        for (User user : array) {
            System.out.println(user);
        }
    }
}
