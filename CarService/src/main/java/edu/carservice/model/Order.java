package edu.carservice.model;

import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;

public class Order {
    private User user;
    private Car car;
    private OrderState state;
    private OrderCategory category;

    public Order(User user, Car car, OrderState state, OrderCategory category) {
        this.user = user;
        this.car = car;
        this.state = state;
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public OrderCategory getCategory() {
        return category;
    }

    public void setCategory(OrderCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "{" +
                "user=" + user +
                ", car=" + car +
                ", state=" + state +
                ", category=" + category +
                '}';
    }
}
