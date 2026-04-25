package com.jmoreno_dev.productmanager.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequestDTO {
    @NotNull(message = "Username is required")
    private String username;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequestDTO> items = new ArrayList<>();
}
