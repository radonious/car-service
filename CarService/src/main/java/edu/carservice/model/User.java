package edu.carservice.model;

import edu.carservice.util.UserCategory;

public class User {
    private String name;
    private String password;
    private UserCategory category;

    public User(String name, String password, UserCategory category) {
        this.name = name;
        this.password = password;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", category=" + category +
                '}';
    }
}
