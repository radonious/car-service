package edu.carservice.service;

import edu.carservice.annotations.Loggable;
import edu.carservice.model.Car;
import edu.carservice.repository.CarRepository;
import edu.carservice.util.CarCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Loggable
@Service
public class CarService {
    CarRepository carRepository;

    @Autowired
    public CarService(CarRepository repository) {
        carRepository = repository;
    }

    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public void addCar(String brand, String model, int year, int price, CarCondition condition) throws IOException {
        if (year < 0 || price < 0) throw new InvalidPropertiesFormatException("Invalid car data.");
        Car car = new Car(null, brand, model, year, price, condition);
        carRepository.save(car);
    }

    public Car getCar(long id) throws IOException {
        if (!carRepository.existsById(id)) throw new IOException("Invalid car index.");
        return carRepository.findById(id);
    }

    public void updateCar(Car newCar) throws IOException {
        if (!carRepository.existsById(newCar.getId())) throw new IOException("Invalid car index.");
        carRepository.update(newCar);
    }

    public void removeCar(long id) throws IOException {
        if (!carRepository.existsById(id)) throw new IOException("Invalid car index.");
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
