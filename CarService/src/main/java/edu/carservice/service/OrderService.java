package edu.carservice.service;

import edu.carservice.annotations.Loggable;
import edu.carservice.model.Car;
import edu.carservice.model.Order;
import edu.carservice.model.User;
import edu.carservice.repository.OrderRepository;
import edu.carservice.util.ConnectionPool;
import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;

import java.io.IOException;
import java.util.List;

@Loggable
public class OrderService {

    OrderRepository orderRepository = new OrderRepository(ConnectionPool.getDataSource());

    public void addOrder(long userId, long carId, OrderCategory category) throws IOException {
        if (orderRepository.existsByCar(carId)) throw new IOException("Car already ordered.");
        orderRepository.save(new Order(null, userId, carId, OrderState.CREATED, category));
    }

    public Order getOrder(long id) throws IOException {
        if (!orderRepository.existsById(id)) throw new IOException("No such order id");
        return orderRepository.findById(id);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public void removeOrder(long id) throws IOException {
        if (!orderRepository.existsById(id)) throw new IOException("No such order id");
        orderRepository.deleteById(id);
    }

    public void updateOrder(Order order) throws IOException {
        if (!orderRepository.existsById(order.getId())) throw new IOException("No such order id");
        orderRepository.update(order);
    }

    public void setOrderState(long id, OrderState state) {
        Order order = orderRepository.findById(id);
        order.setState(state);
        orderRepository.update(order);
    }

    public void displayOrders() {
        orderRepository.findAll().forEach(System.out::println);
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
