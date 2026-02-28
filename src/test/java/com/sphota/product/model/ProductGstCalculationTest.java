package com.sphota.product.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductGstCalculationTest {

    private Product buildProduct(String basePrice, String gstRate) {
        Product product = new Product();
        product.setName("Test Product");
        product.setBasePrice(new BigDecimal(basePrice));
        product.setGstRate(new BigDecimal(gstRate));
        return product;
    }

    @Test
    void testGstAmountAt18Percent() {
        Product product = buildProduct("100.00", "18");
        assertEquals(new BigDecimal("18.00"), product.getGstAmount());
    }

    @Test
    void testPriceWithGstAt18Percent() {
        Product product = buildProduct("100.00", "18");
        assertEquals(new BigDecimal("118.00"), product.getPriceWithGst());
    }

    @Test
    void testGstAmountAt5Percent() {
        Product product = buildProduct("200.00", "5");
        assertEquals(new BigDecimal("10.00"), product.getGstAmount());
    }

    @Test
    void testPriceWithGstAt5Percent() {
        Product product = buildProduct("200.00", "5");
        assertEquals(new BigDecimal("210.00"), product.getPriceWithGst());
    }

    @Test
    void testGstAmountRounding() {
        Product product = buildProduct("99.99", "18");
        assertEquals(new BigDecimal("18.00"), product.getGstAmount());
    }

    @Test
    void testPriceWithGstRounding() {
        Product product = buildProduct("99.99", "18");
        assertEquals(new BigDecimal("117.99"), product.getPriceWithGst());
    }
}
