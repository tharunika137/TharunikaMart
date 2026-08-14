package com.tharunika.tharunikamart.model;
import java.math.BigDecimal; import java.time.LocalDateTime;
public record OrderSummary(long id,String buyerName,String buyerEmail,String status,BigDecimal totalAmount,LocalDateTime createdAt) {}
