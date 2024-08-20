package edu.carservice.service;

import edu.carservice.annotations.Loggable;
import edu.carservice.model.User;
import edu.carservice.repository.UserRepository;
import edu.carservice.util.ConnectionPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Loggable
public class UserService {

    UserRepository userRepository = new UserRepository(ConnectionPool.getDataSource());

    public void addUser(User user) throws IOException {
        if (isUser(user.getName())) throw new IOException("User with this name already exist.");
        userRepository.save(user);
    }

    public void updateUser(User user) throws IOException {
        if (!userRepository.existsById(user.getId())) throw new IOException("No such user id.");
        userRepository.update(user);
    }

    public boolean checkPassword(String name, String password) throws IOException {
        if (!isUser(name)) throw new IOException("No such user.");
        return userRepository.findByName(name).getPassword().equals(password);
    }

    public boolean isUser(String name) {
        return userRepository.existsByName(name);
    }

    public User getUser(String name) throws IOException {
        if (!isUser(name)) throw new IOException("No such user.");
        return userRepository.findByName(name);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void removeUser(long id) throws IOException {
        if (!userRepository.existsById(id)) throw new IOException("No such user.");
        userRepository.deleteById(id);
    }

    public void displayUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    public void displayOrderedByName() {
        List<User> array = new ArrayList<>(userRepository.findAll());
        array.sort(Comparator.comparing(User::getName));
        array.forEach(System.out::println);
    }

    public void displayOrderedByCategory() {
        List<User> array = new ArrayList<>(userRepository.findAll());
        array.sort(Comparator.comparing(User::getCategory));
        array.forEach(System.out::println);
    }
}
