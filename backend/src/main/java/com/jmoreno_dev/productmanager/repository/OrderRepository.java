package com.jmoreno_dev.productmanager.repository;

import com.jmoreno_dev.productmanager.entity.Order;
import com.jmoreno_dev.productmanager.entity.Order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :threshold")
    List<Order> findPendingOrdersOlderThan(@Param("status") OrderStatus status, @Param("threshold") LocalDateTime threshold);

    boolean existsByOrderNumber(String orderNumber);
}
