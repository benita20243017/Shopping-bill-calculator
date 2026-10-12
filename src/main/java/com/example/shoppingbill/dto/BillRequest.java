package com.example.shoppingbill.dto;

import java.math.BigDecimal;

public class BillRequest {
    private BigDecimal taxRate = new BigDecimal("0.10");
    private BigDecimal discountRate = BigDecimal.ZERO;

    public BillRequest() {
    }

    public BillRequest(BigDecimal taxRate, BigDecimal discountRate) {
        this.taxRate = taxRate;
        this.discountRate = discountRate;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
}
