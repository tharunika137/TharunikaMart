package com.tharunika.tharunikamart.model;
import java.math.BigDecimal;
public record Product(long id,long sellerId,String sellerName,String name,String description,BigDecimal price,int stockQty,String category,String imageUrl) {}
