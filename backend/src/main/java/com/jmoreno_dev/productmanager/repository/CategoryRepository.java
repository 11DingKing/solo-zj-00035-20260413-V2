package com.jmoreno_dev.productmanager.repository;

import com.jmoreno_dev.productmanager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNull();

    List<Category> findByParentId(Long parentId);

    boolean existsByParentId(Long parentId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL")
    List<Category> findAllRootCategoriesWithChildren();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.id = :id")
    Category findByIdWithChildren(@Param("id") Long id);

    @Query("SELECT c.id FROM Category c WHERE c.parent.id = :parentId OR c.id = :parentId")
    List<Long> findAllCategoryIdsIncludingChildren(@Param("parentId") Long parentId);
}
