package com.tharunika.tharunikamart.dao;
import com.tharunika.tharunikamart.model.Review; import java.sql.*; import java.util.*;
public class ReviewDAO {
 private final javax.sql.DataSource ds; public ReviewDAO(javax.sql.DataSource ds){this.ds=ds;}
 /** One review per buyer per product: a re-submission updates their existing rating/comment instead of duplicating. */
 public void upsert(long productId,long userId,int rating,String comment)throws SQLException{
  String q="MERGE INTO reviews(product_id,user_id,rating,comment) KEY(product_id,user_id) VALUES(?,?,?,?)";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setLong(1,productId);p.setLong(2,userId);p.setInt(3,rating);p.setString(4,comment);p.executeUpdate();}
 }
 public List<Review> forProduct(long productId)throws SQLException{
  List<Review> out=new ArrayList<>(); String q="SELECT r.*,u.name user_name FROM reviews r JOIN users u ON u.id=r.user_id WHERE r.product_id=? ORDER BY r.created_at DESC";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setLong(1,productId);try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}} return out;
 }
 /** Average rating and review count for every product, keyed by product id. Used to show star ratings on the browse grid without one query per card. */
 public Map<Long,double[]> summaryByProduct()throws SQLException{
  Map<Long,double[]> out=new HashMap<>(); String q="SELECT product_id,AVG(rating) avg_rating,COUNT(*) cnt FROM reviews GROUP BY product_id";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())out.put(r.getLong("product_id"),new double[]{r.getDouble("avg_rating"),r.getDouble("cnt")});}
  return out;
 }
 private Review map(ResultSet r)throws SQLException{return new Review(r.getLong("id"),r.getLong("product_id"),r.getLong("user_id"),r.getString("user_name"),r.getInt("rating"),r.getString("comment"),r.getTimestamp("created_at").toLocalDateTime());}
}
