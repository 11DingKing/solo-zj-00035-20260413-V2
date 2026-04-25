package com.jmoreno_dev.productmanager.service;

import com.jmoreno_dev.productmanager.entity.Product;
import com.jmoreno_dev.productmanager.exceptions.InvalidProductException;

public class ProductValidator {

    public static void validateProduct(Product product) throws InvalidProductException {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new InvalidProductException ("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new InvalidProductException("Product price must be non-negative");
        }
        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new InvalidProductException("Product quantity must be non-negative");
        }
    }
}
