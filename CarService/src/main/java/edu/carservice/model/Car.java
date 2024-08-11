package edu.carservice.model;

import edu.carservice.util.CarCondition;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Car {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private Integer price;
    private CarCondition condition;
}
