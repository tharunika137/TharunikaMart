package com.tharunika.tharunikamart.dao;
import com.tharunika.tharunikamart.model.Product; import java.sql.*; import java.math.BigDecimal; import java.util.*;
public class ProductDAO {
 private final javax.sql.DataSource ds; public ProductDAO(javax.sql.DataSource ds){this.ds=ds;}
 public List<Product> search(String q,String category)throws SQLException{
  List<Product> out=new ArrayList<>(); String sql="SELECT p.*,u.name seller_name FROM products p JOIN users u ON u.id=p.seller_id WHERE (?='' OR LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) AND (?='' OR p.category=?) ORDER BY p.created_at DESC";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(sql)){String x="%"+q.toLowerCase()+"%";p.setString(1,q);p.setString(2,x);p.setString(3,x);p.setString(4,category);p.setString(5,category);try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}} return out;
 }
 public Optional<Product> find(long id)throws SQLException{
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement("SELECT p.*,u.name seller_name FROM products p JOIN users u ON u.id=p.seller_id WHERE p.id=?")){p.setLong(1,id);try(ResultSet r=p.executeQuery()){if(r.next())return Optional.of(map(r));}}return Optional.empty();
 }
 public long create(long seller,String name,String desc,BigDecimal price,int stock,String category,String image)throws SQLException{
  String q="INSERT INTO products(seller_id,name,description,price,stock_qty,category,image_url) VALUES(?,?,?,?,?,?,?)";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){p.setLong(1,seller);p.setString(2,name);p.setString(3,desc);p.setBigDecimal(4,price);p.setInt(5,stock);p.setString(6,category);p.setString(7,image);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();return r.getLong(1);}}
 }
 public void update(long id,long seller,String name,String desc,BigDecimal price,int stock,String category,String image)throws SQLException{
  String q="UPDATE products SET name=?,description=?,price=?,stock_qty=?,category=?,image_url=? WHERE id=? AND seller_id=?";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,name);p.setString(2,desc);p.setBigDecimal(3,price);p.setInt(4,stock);p.setString(5,category);p.setString(6,image);p.setLong(7,id);p.setLong(8,seller);p.executeUpdate();}
 }
 public void delete(long id,long seller)throws SQLException{try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM products WHERE id=? AND seller_id=?")){p.setLong(1,id);p.setLong(2,seller);p.executeUpdate();}}
 public List<Product> sellerProducts(long seller)throws SQLException{List<Product> o=new ArrayList<>();try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement("SELECT p.*,u.name seller_name FROM products p JOIN users u ON u.id=p.seller_id WHERE p.seller_id=? ORDER BY p.created_at DESC")){p.setLong(1,seller);try(ResultSet r=p.executeQuery()){while(r.next())o.add(map(r));}}return o;}
 public void moderateDelete(long id)throws SQLException{try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM products WHERE id=?")){p.setLong(1,id);p.executeUpdate();}}
 private Product map(ResultSet r)throws SQLException{return new Product(r.getLong("id"),r.getLong("seller_id"),r.getString("seller_name"),r.getString("name"),r.getString("description"),r.getBigDecimal("price"),r.getInt("stock_qty"),r.getString("category"),r.getString("image_url"));}
}
