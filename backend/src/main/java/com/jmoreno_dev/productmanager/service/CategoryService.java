package com.jmoreno_dev.productmanager.service;

import com.jmoreno_dev.productmanager.dto.CategoryDTO;
import com.jmoreno_dev.productmanager.dto.CategoryStatisticsDTO;
import com.jmoreno_dev.productmanager.entity.Category;
import com.jmoreno_dev.productmanager.repository.CategoryRepository;
import com.jmoreno_dev.productmanager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CategoryDTO> getAllCategories() {
        List<Category> rootCategories = categoryRepository.findAllRootCategoriesWithChildren();
        return rootCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getAllCategoriesFlat() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTOFlat)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findByIdWithChildren(id);
        if (category == null) {
            return null;
        }
        return convertToDTO(category);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        if (categoryDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryDTO.getParentId()).orElse(null);
            if (parent != null) {
                if (parent.getParent() != null) {
                    throw new IllegalArgumentException("Cannot create category: maximum 2 levels allowed");
                }
                category.setParent(parent);
            }
        }

        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }

        category.setName(categoryDTO.getName());

        if (categoryDTO.getParentId() != null && !categoryDTO.getParentId().equals(id)) {
            Category parent = categoryRepository.findById(categoryDTO.getParentId()).orElse(null);
            if (parent != null) {
                if (parent.getParent() != null) {
                    throw new IllegalArgumentException("Cannot update category: maximum 2 levels allowed");
                }
                if (isDescendant(category, parent)) {
                    throw new IllegalArgumentException("Cannot set parent to a descendant category");
                }
                category.setParent(parent);
            }
        } else if (categoryDTO.getParentId() == null) {
            category.setParent(null);
        }

        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    private boolean isDescendant(Category category, Category potentialDescendant) {
        if (category.getChildren() == null || category.getChildren().isEmpty()) {
            return false;
        }
        for (Category child : category.getChildren()) {
            if (child.getId().equals(potentialDescendant.getId())) {
                return true;
            }
            if (isDescendant(child, potentialDescendant)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }

        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with children");
        }

        long productCount = productRepository.countByCategoryId(id);
        if (productCount > 0) {
            throw new IllegalStateException("Cannot delete category with products");
        }

        categoryRepository.delete(category);
        return true;
    }

    public List<CategoryStatisticsDTO> getCategoryStatistics() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryStatisticsDTO> statistics = new ArrayList<>();

        for (Category category : categories) {
            long count = productRepository.countByCategoryId(category.getId());
            CategoryStatisticsDTO dto = new CategoryStatisticsDTO();
            dto.setCategoryId(category.getId());
            dto.setCategoryName(category.getName());
            dto.setProductCount(count);
            statistics.add(dto);
        }

        return statistics;
    }

    public List<Long> getAllCategoryIdsIncludingChildren(Long categoryId) {
        List<Long> ids = new ArrayList<>();
        ids.add(categoryId);
        
        Category category = categoryRepository.findByIdWithChildren(categoryId);
        if (category != null && category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                ids.add(child.getId());
            }
        }
        
        return ids;
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }
        if (category.getChildren() != null) {
            dto.setChildren(category.getChildren().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
        }
        dto.setProductCount((long) (category.getProducts() != null ? category.getProducts().size() : 0));
        return dto;
    }

    private CategoryDTO convertToDTOFlat(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }
        return dto;
    }
}
