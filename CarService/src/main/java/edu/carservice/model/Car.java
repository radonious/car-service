package edu.carservice.model;

import edu.carservice.util.CarCondition;

public class Car {
    private String brand;
    private String model;
    private int year;
    private int price;
    private CarCondition condition;

    public Car(String brand, String model, int year, int price, CarCondition condition) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public CarCondition getCondition() {
        return condition;
    }

    public void setCondition(CarCondition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", condition=" + condition +
                '}';
    }
}
