package com.sphota.product.service;

import com.sphota.product.dto.CategoryRequest;
import com.sphota.product.model.Category;
import com.sphota.product.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategories_returnsList() {
        Category cat = new Category(1L, "Electronics", "Electronic goods");
        when(categoryRepository.findAll()).thenReturn(List.of(cat));

        List<Category> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    void getCategoryById_found() {
        Category cat = new Category(1L, "Electronics", "Electronic goods");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));

        Category result = categoryService.getCategoryById(1L);

        assertEquals("Electronics", result.getName());
    }

    @Test
    void getCategoryById_notFound_throws() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void createCategory_savesAndReturns() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Clothing");
        request.setDescription("Clothing items");

        when(categoryRepository.existsByName("Clothing")).thenReturn(false);
        Category saved = new Category(1L, "Clothing", "Clothing items");
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        Category result = categoryService.createCategory(request);

        assertEquals("Clothing", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_duplicateName_throws() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");
        request.setDescription("Electronic goods");

        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(request));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_notFound_throws() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(99L));
        verify(categoryRepository, never()).deleteById(any());
    }
}
