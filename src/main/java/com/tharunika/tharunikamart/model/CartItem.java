package com.tharunika.tharunikamart.model;
import java.math.BigDecimal;
public record CartItem(long id,long productId,String productName,BigDecimal unitPrice,int quantity,int stockQty,String imageUrl) {
 public BigDecimal total(){ return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
