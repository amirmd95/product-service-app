package com.sphota.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal gstRate;
    private BigDecimal gstAmount;
    private BigDecimal priceWithGst;
    private Long categoryId;
    private String categoryName;
}
