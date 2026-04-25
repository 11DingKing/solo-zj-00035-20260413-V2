package com.jmoreno_dev.productmanager.service;

import com.jmoreno_dev.productmanager.dto.CreateOrderRequestDTO;
import com.jmoreno_dev.productmanager.dto.OrderDTO;
import com.jmoreno_dev.productmanager.dto.OrderItemDTO;
import com.jmoreno_dev.productmanager.dto.OrderItemRequestDTO;
import com.jmoreno_dev.productmanager.entity.Order;
import com.jmoreno_dev.productmanager.entity.Order.OrderStatus;
import com.jmoreno_dev.productmanager.entity.OrderItem;
import com.jmoreno_dev.productmanager.entity.Product;
import com.jmoreno_dev.productmanager.repository.OrderRepository;
import com.jmoreno_dev.productmanager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private final Random random = new Random();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Transactional
    public OrderDTO createOrder(CreateOrderRequestDTO request) {
        String orderNumber = generateUniqueOrderNumber();
        
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUsername(request.getUsername());
        order.setStatus(OrderStatus.PENDING);
        
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;
        
        for (OrderItemRequestDTO itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.getProductId()));
            
            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() + 
                        ". Available: " + product.getQuantity() + ", Requested: " + itemRequest.getQuantity());
            }
            
            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            productRepository.save(product);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            
            orderItems.add(orderItem);
            totalAmount += product.getPrice() * itemRequest.getQuantity();
        }
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        
        return convertToDTO(savedOrder);
    }

    public Page<OrderDTO> getOrders(OrderStatus status, Pageable pageable) {
        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findByStatus(status, pageable);
        } else {
            orders = orderRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        return orders.map(this::convertToDTO);
    }

    public OrderDTO getOrderById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        return orderOpt.map(this::convertToDTO).orElse(null);
    }

    public OrderDTO getOrderByNumber(String orderNumber) {
        Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
        return orderOpt.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be cancelled");
        }
        
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProductId()).orElse(null);
            if (product != null) {
                product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                productRepository.save(product);
            }
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        
        return convertToDTO(savedOrder);
    }

    @Transactional
    public void cancelExpiredOrders() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);
        List<Order> expiredOrders = orderRepository.findPendingOrdersOlderThan(OrderStatus.PENDING, threshold);
        
        for (Order order : expiredOrders) {
            try {
                for (OrderItem orderItem : order.getOrderItems()) {
                    Product product = productRepository.findById(orderItem.getProductId()).orElse(null);
                    if (product != null) {
                        product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                        productRepository.save(product);
                    }
                }
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String generateUniqueOrderNumber() {
        String datePart = LocalDate.now().format(dateFormatter);
        String orderNumber;
        int attempts = 0;
        
        do {
            int randomPart = 100000 + random.nextInt(900000);
            orderNumber = datePart + randomPart;
            attempts++;
        } while (orderRepository.existsByOrderNumber(orderNumber) && attempts < 100);
        
        if (attempts >= 100) {
            throw new RuntimeException("Failed to generate unique order number after 100 attempts");
        }
        
        return orderNumber;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUsername(order.getUsername());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        
        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProductId());
            itemDTO.setProductName(item.getProductName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setUnitPrice(item.getUnitPrice());
            itemDTO.setSubtotal(item.getUnitPrice() * item.getQuantity());
            itemDTOs.add(itemDTO);
        }
        dto.setOrderItems(itemDTOs);
        
        return dto;
    }
}
