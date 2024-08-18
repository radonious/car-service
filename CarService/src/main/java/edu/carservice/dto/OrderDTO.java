package edu.carservice.dto;

import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;

public record OrderDTO(
        Long id,
        Long userId,
        Long carId,
        OrderState state,
        OrderCategory category
) {
}
