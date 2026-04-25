package com.jmoreno_dev.productmanager.config;

import com.jmoreno_dev.productmanager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderScheduledTasks {

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        orderService.cancelExpiredOrders();
    }
}
