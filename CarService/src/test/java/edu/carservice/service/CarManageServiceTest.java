package edu.carservice.service;

import edu.carservice.model.Car;
import edu.carservice.util.CarCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CarManageServiceTest {

    CarManageService carManageService;

    @BeforeEach
    public void setUp() {
        carManageService = new CarManageService();
    }

    @Test
    public void test1() throws IOException {
        carManageService.addCar("brand", "model", 2024, 3000000, CarCondition.NEW);
        Assertions.assertEquals(carManageService.getCar(0).getBrand(), "brand");
        Assertions.assertEquals(carManageService.getCar(0).getModel(), "model");
        Assertions.assertEquals(carManageService.getCar(0).getYear(), 2024);
        Assertions.assertEquals(carManageService.getCar(0).getPrice(), 3000000);
        Assertions.assertEquals(carManageService.getCar(0).getCondition(), CarCondition.NEW);
        Assertions.assertEquals(carManageService.getCars().size(), 1);
    }
}
