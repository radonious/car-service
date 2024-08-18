package edu.carservice.model;

import edu.carservice.util.CarCondition;

public class Car {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private Integer price;
    private CarCondition condition;

    public Car() {}

    public Car(Long id, String brand, String model, Integer year, Integer price, CarCondition condition) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getYear() {
        return year;
    }

    public CarCondition getCondition() {
        return condition;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setCondition(CarCondition condition) {
        this.condition = condition;
    }
}
