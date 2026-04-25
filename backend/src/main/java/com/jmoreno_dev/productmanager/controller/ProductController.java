package com.jmoreno_dev.productmanager.controller;

import com.jmoreno_dev.productmanager.dto.ProductDTO;
import com.jmoreno_dev.productmanager.exceptions.InvalidProductException;
import com.jmoreno_dev.productmanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping (path = "/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(required = false) Long categoryId) {
        List<ProductDTO> products;
        if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        } else {
            products = productService.getAllProducts();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO p = productService.getProductById(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) throws InvalidProductException {
        ProductDTO created = productService.saveOrUpdateProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) throws InvalidProductException {
        ProductDTO existing = productService.getProductById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        ProductDTO updated = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
