package edu.carservice.dto;

import edu.carservice.util.CarCondition;


public record CarDTO(
        Long id,
        String brand,
        String model,
        Integer year,
        Integer price,
        CarCondition condition
) {
}
