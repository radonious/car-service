package edu.carservice.service;

import edu.carservice.model.Car;
import edu.carservice.model.User;
import edu.carservice.util.CarCondition;
import edu.carservice.util.UserCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class OrderServiceTest {
    OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    void test() throws IOException {
        User user = new User("name", "pass", UserCategory.ADMIN);
        Car car = new Car("car", "model", 2000, 3000000, CarCondition.NEW);
        orderService.addBuyOrder(user, car);
        Assertions.assertEquals(1, orderService.getOrders().size());
        orderService.addServiceOrder(user, car);
        Assertions.assertEquals(2, orderService.getOrders().size());
    }
}
