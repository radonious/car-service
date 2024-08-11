package edu.carservice.service;

import edu.carservice.model.Car;
import edu.carservice.repository.CarRepository;
import edu.carservice.util.CarCondition;
import edu.carservice.util.ConnectionPool;

import java.io.IOException;
import java.util.List;

public class CarManageService {
    CarRepository carRepository = new CarRepository(ConnectionPool.getDataSource());

    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public void addCar(String brand, String model, int year, int price, CarCondition condition) throws IOException {
        if (year < 0 || price < 0) throw new IOException("Invalid car data.");
        Car car = new Car(null, brand, model, year, price, condition);
        carRepository.save(car);
    }

    public Car getCar(long id) throws IOException {
        if (id < 0 || id >= carRepository.count()) throw new IOException("Invalid car index.");
        return carRepository.findById(id);
    }

    public void updateCar(Car newCar) throws IOException {
        carRepository.update(newCar);
    }

    public void removeCar(long id) throws IOException {
        if (id < 0 || id >= carRepository.count()) throw new IOException("Invalid car index.");
        carRepository.deleteById(id);
    }

    public void displayCars() {
        List<Car> cars = getCars();
        cars.forEach(System.out::println);
    }

    public void displayCarsByBrand(String brand) {
        List<Car> cars = getCars();
        cars.stream().filter(e -> e.getBrand().equals(brand)).forEach(System.out::println);
    }

    public void displayCarsByModel(String model) {
        List<Car> cars = getCars();
        cars.stream().filter(e -> e.getModel().equals(model)).forEach(System.out::println);
    }

    public void displayCarsByYear(int year) {
        List<Car> cars = getCars();
        cars.stream().filter(e -> e.getYear() == year).forEach(System.out::println);
    }

    public void displayCarsByPrice(int price) {
        List<Car> cars = getCars();
        cars.stream().filter(e -> e.getPrice() == price).forEach(System.out::println);
    }
}
