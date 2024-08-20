package edu.carservice.model;

import edu.carservice.util.UserCategory;

public class User {
    private Long id;
    private String name;
    private String password;
    private UserCategory category;

    public User() {}

    public User(Long id, String name, String password, UserCategory category) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }
}
