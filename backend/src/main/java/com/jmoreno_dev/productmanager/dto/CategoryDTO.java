package com.jmoreno_dev.productmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String parentName;
    private List<CategoryDTO> children = new ArrayList<>();
    private Long productCount = 0L;
}
