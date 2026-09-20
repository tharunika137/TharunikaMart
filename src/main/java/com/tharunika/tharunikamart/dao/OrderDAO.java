package com.tharunika.tharunikamart.dao;
import com.tharunika.tharunikamart.model.*; import java.sql.*; import java.util.*;
public class OrderDAO {
 private final javax.sql.DataSource ds; public OrderDAO(javax.sql.DataSource ds){this.ds=ds;}
 public long place(long buyer,List<CartItem> items,String shippingName,String shippingAddress,String shippingPhone,String paymentMethod)throws SQLException{
  try(Connection c=ds.getConnection()){c.setAutoCommit(false);try{
   java.math.BigDecimal total=items.stream().map(CartItem::total).reduce(java.math.BigDecimal.ZERO,java.math.BigDecimal::add);
   long id; try(PreparedStatement p=c.prepareStatement("INSERT INTO orders(buyer_id,status,total_amount,shipping_name,shipping_address,shipping_phone,payment_method) VALUES(?,'PENDING',?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){p.setLong(1,buyer);p.setBigDecimal(2,total);p.setString(3,shippingName);p.setString(4,shippingAddress);p.setString(5,shippingPhone);p.setString(6,paymentMethod);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();id=r.getLong(1);}}
   for(CartItem i:items){try(PreparedStatement p=c.prepareStatement("INSERT INTO order_items(order_id,product_id,quantity,unit_price) VALUES(?,?,?,?)")){p.setLong(1,id);p.setLong(2,i.productId());p.setInt(3,i.quantity());p.setBigDecimal(4,i.unitPrice());p.executeUpdate();}try(PreparedStatement p=c.prepareStatement("UPDATE products SET stock_qty=stock_qty-? WHERE id=? AND stock_qty>=?")){p.setInt(1,i.quantity());p.setLong(2,i.productId());p.setInt(3,i.quantity());if(p.executeUpdate()!=1)throw new SQLException("Insufficient stock");}}
   c.commit();return id;
  }catch(Exception e){c.rollback();throw e;}}
 }
 public List<OrderSummary> buyerOrders(long buyer)throws SQLException{return list("SELECT o.*,u.name buyer_name,u.email buyer_email FROM orders o JOIN users u ON u.id=o.buyer_id WHERE o.buyer_id=? ORDER BY o.created_at DESC",buyer);}
 public List<OrderSummary> allOrders()throws SQLException{return list("SELECT o.*,u.name buyer_name,u.email buyer_email FROM orders o JOIN users u ON u.id=o.buyer_id ORDER BY o.created_at DESC",null);}
 private List<OrderSummary> list(String q,Long id)throws SQLException{List<OrderSummary> o=new ArrayList<>();try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q)){if(id!=null)p.setLong(1,id);try(ResultSet r=p.executeQuery()){while(r.next())o.add(new OrderSummary(r.getLong("id"),r.getString("buyer_name"),r.getString("buyer_email"),r.getString("status"),r.getBigDecimal("total_amount"),r.getTimestamp("created_at").toLocalDateTime(),r.getString("shipping_name"),r.getString("shipping_address"),r.getString("shipping_phone"),r.getString("payment_method")));}}return o;}
 public void updateStatus(long id,String status)throws SQLException{try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement("UPDATE orders SET status=? WHERE id=?")){p.setString(1,status);p.setLong(2,id);p.executeUpdate();}}
 /** Buyer-initiated cancellation: only the owning buyer can cancel, and only while the order is still PENDING. Restocks the items on success. */
 public boolean cancel(long orderId,long buyerId)throws SQLException{
  try(Connection c=ds.getConnection()){c.setAutoCommit(false);try{
   int updated; try(PreparedStatement p=c.prepareStatement("UPDATE orders SET status='CANCELLED' WHERE id=? AND buyer_id=? AND status='PENDING'")){p.setLong(1,orderId);p.setLong(2,buyerId);updated=p.executeUpdate();}
   if(updated==1){try(PreparedStatement p=c.prepareStatement("UPDATE products p SET stock_qty=stock_qty+(SELECT quantity FROM order_items oi WHERE oi.order_id=? AND oi.product_id=p.id) WHERE p.id IN (SELECT product_id FROM order_items WHERE order_id=?)")){p.setLong(1,orderId);p.setLong(2,orderId);p.executeUpdate();}}
   c.commit();return updated==1;
  }catch(Exception e){c.rollback();throw e;}}
 }
}
