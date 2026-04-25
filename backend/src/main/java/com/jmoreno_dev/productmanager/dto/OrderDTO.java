package com.jmoreno_dev.productmanager.dto;

import com.jmoreno_dev.productmanager.entity.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private String username;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
