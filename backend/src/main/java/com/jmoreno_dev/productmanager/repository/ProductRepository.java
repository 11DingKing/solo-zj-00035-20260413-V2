package com.jmoreno_dev.productmanager.repository;

import com.jmoreno_dev.productmanager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    long countByCategoryId(Long categoryId);

    @Query("SELECT p.category.id, COUNT(p) FROM Product p GROUP BY p.category.id")
    List<Object[]> countProductsByCategory();
}
