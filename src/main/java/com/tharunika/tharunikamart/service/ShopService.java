package com.tharunika.tharunikamart.service;
import com.tharunika.tharunikamart.dao.*; import com.tharunika.tharunikamart.model.*; import java.math.BigDecimal; import java.util.*;
public class ShopService {
 public final ProductDAO products; public final CartDAO cart; public final OrderDAO orders;
 public ShopService(ProductDAO p,CartDAO c,OrderDAO o){products=p;cart=c;orders=o;}
 public List<Product> browse(String q,String cat)throws Exception{return products.search(q,cat);}
 public List<CartItem> cart(long user)throws Exception{return cart.find(user);}
 public BigDecimal total(List<CartItem> items){return items.stream().map(CartItem::total).reduce(BigDecimal.ZERO,BigDecimal::add);}
 public long checkout(long user)throws Exception{List<CartItem> items=cart.find(user);if(items.isEmpty())throw new IllegalArgumentException("Your cart is empty.");long id=orders.place(user,items);cart.clear(user);return id;}
}
