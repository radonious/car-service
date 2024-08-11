package edu.carservice.service;

import edu.carservice.model.Car;
import edu.carservice.model.Order;
import edu.carservice.model.User;
import edu.carservice.util.OrderState;

import java.util.ArrayList;

public class FilterService {

    private ArrayList<Car> getCars() {
        CarManageService carManageService = new CarManageService();
        return carManageService.getCars();
    };

    private ArrayList<Order> getOrders() {
        OrderService orderService = new OrderService();
        return orderService.getOrders();
    }

    public void carsByBrand(String brand) {
        ArrayList<Car> cars = getCars();
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getBrand().equalsIgnoreCase(brand)) {
                System.out.println(i + " - " + cars.get(i));
            }
        }
    }

    public void carsByModel(String brand) {
        ArrayList<Car> cars = getCars();
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getBrand().equalsIgnoreCase(brand)) {
                System.out.println(i + " - " + cars.get(i));
            }
        }
    }

    public void carsByYear(int year) {
        ArrayList<Car> cars = getCars();
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getYear() == year) {
                System.out.println(i + " - " + cars.get(i));
            }
        }
    }

    public void carsByPrice(int price) {
        ArrayList<Car> cars = getCars();
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getPrice() == price) {
                System.out.println(i + " - " + cars.get(i));
            }
        }
    }

    public void ordersByUser(User user) {
        ArrayList<Order> orders = getOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getUser().equals(user)) {
                System.out.println(i + " - " + orders.get(i));
            }
        }
    }

    public void ordersByCar(Car car) {
        ArrayList<Order> orders = getOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getCar().equals(car)) {
                System.out.println(i + " - " + orders.get(i));
            }
        }
    }

    public void orderByState(OrderState state) {
        ArrayList<Order> orders = getOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getState().equals(state)) {
                System.out.println(i + " - " + orders.get(i));
            }
        }
    }
}
