package edu.carservice.model;

import edu.carservice.util.OrderCategory;
import edu.carservice.util.OrderState;

public class Order {
    private Long id;
    private Long userId;
    private Long carId;
    private OrderState state;
    private OrderCategory category;

    public Order() {}

    public Order(Long id, Long userId, Long carId, OrderState state, OrderCategory category) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.state = state;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCarId() {
        return carId;
    }

    public OrderState getState() {
        return state;
    }

    public OrderCategory getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void setCategory(OrderCategory category) {
        this.category = category;
    }
}
