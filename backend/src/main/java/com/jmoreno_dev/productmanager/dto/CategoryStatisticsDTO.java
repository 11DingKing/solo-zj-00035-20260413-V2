package com.jmoreno_dev.productmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStatisticsDTO {
    private Long categoryId;
    private String categoryName;
    private Long productCount;
}
