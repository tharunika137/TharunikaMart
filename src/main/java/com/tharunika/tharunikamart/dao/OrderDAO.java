package com.tharunika.tharunikamart.dao;
import com.tharunika.tharunikamart.model.*; import java.sql.*; import java.util.*;
public class OrderDAO {
 private final javax.sql.DataSource ds; public OrderDAO(javax.sql.DataSource ds){this.ds=ds;}
 public long place(long buyer,List<CartItem> items)throws SQLException{
  try(Connection c=ds.getConnection()){c.setAutoCommit(false);try{
   java.math.BigDecimal total=items.stream().map(CartItem::total).reduce(java.math.BigDecimal.ZERO,java.math.BigDecimal::add);
   long id; try(PreparedStatement p=c.prepareStatement("INSERT INTO orders(buyer_id,status,total_amount) VALUES(?,'PENDING',?)",Statement.RETURN_GENERATED_KEYS)){p.setLong(1,buyer);p.setBigDecimal(2,total);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();id=r.getLong(1);}}
   for(CartItem i:items){try(PreparedStatement p=c.prepareStatement("INSERT INTO order_items(order_id,product_id,quantity,unit_price) VALUES(?,?,?,?)")){p.setLong(1,id);p.setLong(2,i.productId());p.setInt(3,i.quantity());p.setBigDecimal(4,i.unitPrice());p.executeUpdate();}try(PreparedStatement p=c.prepareStatement("UPDATE products SET stock_qty=stock_qty-? WHERE id=? AND stock_qty>=?")){p.setInt(1,i.quantity());p.setLong(2,i.productId());p.setInt(3,i.quantity());if(p.executeUpdate()!=1)throw new SQLException("Insufficient stock");}}
   c.commit();return id;
  }catch(Exception e){c.rollback();throw e;}}
 }
 public List<OrderSummary> buyerOrders(long buyer)throws SQLException{return list("SELECT o.*,u.name buyer_name,u.email buyer_email FROM orders o JOIN users u ON u.id=o.buyer_id WHERE o.buyer_id=? ORDER BY o.created_at DESC",buyer);}
 public List<OrderSummary> allOrders()throws SQLException{return list("SELECT o.*,u.name buyer_name,u.email buyer_email FROM orders o JOIN users u ON u.id=o.buyer_id ORDER BY o.created_at DESC",null);}
 private List<OrderSummary> list(String q,Long id)throws SQLException{List<OrderSummary> o=new ArrayList<>();try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q)){if(id!=null)p.setLong(1,id);try(ResultSet r=p.executeQuery()){while(r.next())o.add(new OrderSummary(r.getLong("id"),r.getString("buyer_name"),r.getString("buyer_email"),r.getString("status"),r.getBigDecimal("total_amount"),r.getTimestamp("created_at").toLocalDateTime()));}}return o;}
 public void updateStatus(long id,String status)throws SQLException{try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement("UPDATE orders SET status=? WHERE id=?")){p.setString(1,status);p.setLong(2,id);p.executeUpdate();}}
}
