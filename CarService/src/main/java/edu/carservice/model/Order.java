package edu.carservice.model;

import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private Long carId;
    private OrderState state;
    private OrderCategory category;
}
