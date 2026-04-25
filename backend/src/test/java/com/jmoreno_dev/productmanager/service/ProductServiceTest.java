package com.jmoreno_dev.productmanager.service;

import com.jmoreno_dev.productmanager.entity.Product;
import com.jmoreno_dev.productmanager.exceptions.InvalidProductException;
import com.jmoreno_dev.productmanager.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() throws InvalidProductException {
        Product validProduct = new Product(null, "Laptop", "High-end gaming laptop", 1500.00, 9, null);
        Product savedProduct = productService.saveOrUpdateProduct(validProduct);
        assertNotNull (savedProduct.getId(), "Saved product should have an ID");

        assertNotNull(productRepository
                .findById(savedProduct.getId()).orElse(null), "Product should be found in the repository");
    }

    @Test
    void testSaveInvalidProduct() {
        Product invalidProduct = new Product(null, null, "No name product", 100.00, 5, null);
        assertThrows(Exception.class, () ->
                productService.saveOrUpdateProduct(invalidProduct), "Saving a product without a name should throw an exception");
    }
}

