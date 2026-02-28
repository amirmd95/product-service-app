package com.sphota.product.service;

import com.sphota.product.dto.ProductRequest;
import com.sphota.product.dto.ProductResponse;
import com.sphota.product.model.Category;
import com.sphota.product.model.Product;
import com.sphota.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_withoutCategory_setsNullCategory() {
        ProductRequest request = new ProductRequest();
        request.setName("Widget");
        request.setDescription("A widget");
        request.setBasePrice(new BigDecimal("100.00"));
        request.setGstRate(new BigDecimal("18"));
        request.setCategoryId(null);

        Product saved = new Product();
        saved.setId(1L);
        saved.setName("Widget");
        saved.setDescription("A widget");
        saved.setBasePrice(new BigDecimal("100.00"));
        saved.setGstRate(new BigDecimal("18"));
        saved.setCategory(null);

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Widget", response.getName());
        assertNull(response.getCategoryId());
        verify(categoryService, never()).getCategoryById(any());
    }

    @Test
    void createProduct_withCategory_setsCategory() {
        ProductRequest request = new ProductRequest();
        request.setName("Phone");
        request.setBasePrice(new BigDecimal("500.00"));
        request.setGstRate(new BigDecimal("18"));
        request.setCategoryId(1L);

        Category category = new Category(1L, "Electronics", "Electronic goods");
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        Product saved = new Product();
        saved.setId(2L);
        saved.setName("Phone");
        saved.setBasePrice(new BigDecimal("500.00"));
        saved.setGstRate(new BigDecimal("18"));
        saved.setCategory(category);

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.createProduct(request);

        assertEquals("Electronics", response.getCategoryName());
        assertEquals(1L, response.getCategoryId());
    }

    @Test
    void getProductById_notFound_throws() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(99L));
    }

    @Test
    void getProductById_returnsGstCalculation() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Gadget");
        product.setBasePrice(new BigDecimal("200.00"));
        product.setGstRate(new BigDecimal("18"));
        product.setCategory(null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertEquals(new BigDecimal("36.00"), response.getGstAmount());
        assertEquals(new BigDecimal("236.00"), response.getPriceWithGst());
    }
}
