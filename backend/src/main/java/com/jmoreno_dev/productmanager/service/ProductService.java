package com.jmoreno_dev.productmanager.service;

import com.jmoreno_dev.productmanager.dto.ProductDTO;
import com.jmoreno_dev.productmanager.entity.Category;
import com.jmoreno_dev.productmanager.entity.Product;
import com.jmoreno_dev.productmanager.exceptions.InvalidProductException;
import com.jmoreno_dev.productmanager.repository.CategoryRepository;
import com.jmoreno_dev.productmanager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Long> categoryIds = categoryService.getAllCategoryIdsIncludingChildren(categoryId);
        return productRepository.findByCategoryIds(categoryIds).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return product != null ? convertToDTO(product) : null;
    }

    @Transactional
    public ProductDTO saveOrUpdateProduct(ProductDTO productDTO) throws InvalidProductException {
        Product product = convertToEntity(productDTO);
        ProductValidator.validateProduct(product);
        Product saved = productRepository.save(product);
        return convertToDTO(saved);
    }

    @Transactional
    public Product saveOrUpdateProduct(Product product) throws InvalidProductException {
        ProductValidator.validateProduct(product);
        return productRepository.save(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws InvalidProductException {
        Product existing = productRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        existing.setName(productDTO.getName());
        existing.setDescription(productDTO.getDescription());
        existing.setPrice(productDTO.getPrice());
        existing.setQuantity(productDTO.getQuantity());

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
            existing.setCategory(category);
        } else {
            existing.setCategory(null);
        }

        ProductValidator.validateProduct(existing);
        Product saved = productRepository.save(existing);
        return convertToDTO(saved);
    }

    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) return false;
        productRepository.deleteById(id);
        return true;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        return product;
    }
}
