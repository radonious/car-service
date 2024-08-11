package edu.carservice.service;

import edu.carservice.model.Car;
import edu.carservice.model.Order;
import edu.carservice.model.User;
import edu.carservice.repository.OrderRepository;
import edu.carservice.util.ConnectionPool;
import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;

import java.io.IOException;
import java.util.List;

public class OrderService {

    OrderRepository orderRepository = new OrderRepository(ConnectionPool.getDataSource());

    public void addBuyOrder(long userId, long carId) throws IOException {
        if (orderRepository.existsByCar(carId)) throw new IOException("Car already ordered.");
        orderRepository.save(new Order(null, userId, carId, OrderState.CREATED, OrderCategory.BUY));
    }

    public void addServiceOrder(long userId, long carId) throws IOException {
        if (orderRepository.existsByCar(carId)) throw new IOException("Car already ordered.");
        orderRepository.save(new Order(null, userId, carId, OrderState.CREATED, OrderCategory.SERVICE));
    }

    public void displayOrders() {
        orderRepository.findAll().forEach(System.out::println);
    }

    public Order getOrder(long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public void setOrderState(long id, OrderState state) {
        Order order = orderRepository.findById(id);
        order.setState(state);
        orderRepository.update(order);
    }

    public void filterByUser(User user) {
        List<Order> orders = getOrders();
        orders.stream().filter(e -> e.getUserId().equals(user.getId())).forEach(System.out::println);
    }

    public void filterByCar(Car car) {
        List<Order> orders = getOrders();
        orders.stream().filter(e -> e.getCarId().equals(car.getId())).forEach(System.out::println);
    }

    public void filterByState(OrderState state) {
        List<Order> orders = getOrders();
        orders.stream().filter(e -> e.getState().equals(state)).forEach(System.out::println);
    }
}
