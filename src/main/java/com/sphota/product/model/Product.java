package com.sphota.product.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal basePrice;

    /**
     * GST rate as a percentage (e.g., 18.0 for 18%).
     */
    @Column(nullable = false)
    private BigDecimal gstRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Returns the GST amount for this product.
     */
    public BigDecimal getGstAmount() {
        return basePrice.multiply(gstRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the price inclusive of GST.
     */
    public BigDecimal getPriceWithGst() {
        return basePrice.add(getGstAmount()).setScale(2, RoundingMode.HALF_UP);
    }
}
