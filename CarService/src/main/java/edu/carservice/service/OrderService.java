package edu.carservice.service;

import edu.carservice.model.Car;
import edu.carservice.model.Order;
import edu.carservice.model.User;
import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class OrderService {
    private static final ArrayList<Order> orders = new ArrayList<Order>();
    private static final HashSet<Car> ordered = new HashSet<>();

    public void addBuyOrder(User user, Car car) throws IOException {
        if (ordered.contains(car)) throw new IOException("Car already ordered.");
        orders.add(new Order(user, car, OrderState.CREATED, OrderCategory.BUY));
    }

    public void addServiceOrder(User user, Car car) throws IOException {
        if (ordered.contains(car)) throw new IOException("Car already ordered.");
        orders.add(new Order(user, car, OrderState.CREATED, OrderCategory.SERVICE));
    }

    public void displayOrders() {
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(i + " - " + orders.get(i));
        }
    }

    public Order getOrder(int index) {
        return orders.get(index);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrderState(int index, OrderState state) {
        orders.get(index).setState(state);
    }
}
