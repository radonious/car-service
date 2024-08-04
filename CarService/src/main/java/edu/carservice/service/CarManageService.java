package edu.carservice.service;

import edu.carservice.model.Car;
import edu.carservice.util.CarCondition;

import java.io.IOException;
import java.util.ArrayList;

public class CarManageService {
    static ArrayList<Car> cars = new ArrayList<>();

    public ArrayList<Car> getCars() {
        return new ArrayList<>(cars);
    }

    public void displayCars() {
        for (int i = 0; i < cars.size(); i++) {
            System.out.println(i + " - " + cars.get(i));
        }
    }

    public void addCar(String brand, String model, int year, int price, CarCondition condition) throws IOException {
        if (year < 0 || price < 0) throw new IOException("Invalid car data.");
        Car car = new Car(brand, model, year, price, condition);
        cars.add(car);
    }

    public Car getCar(int index) {
        if (index < 0 || index >= cars.size()) throw new IndexOutOfBoundsException("Invalid car index.");
        return cars.get(index);
    }

    public void updateCar(int index, Car newCar) {
        if (index < 0 || index >= cars.size()) throw new IndexOutOfBoundsException("Invalid car index.");
        cars.set(index, newCar);
    }

    public void removeCar(int index) {
        if (index < 0 || index >= cars.size()) throw new IndexOutOfBoundsException("Invalid car index.");
        cars.remove(index);
    }
}
