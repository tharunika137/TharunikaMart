package com.tharunika.tharunikamart.dao;
import com.tharunika.tharunikamart.model.User; import java.sql.*; import java.util.*;
public class UserDAO {
 private final javax.sql.DataSource ds; public UserDAO(javax.sql.DataSource ds){this.ds=ds;}
 public Optional<User> findByEmail(String email)throws SQLException{
  String q="SELECT * FROM users WHERE email=?";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,email);try(ResultSet r=p.executeQuery()){if(r.next())return Optional.of(map(r));}} return Optional.empty();
 }
 public long create(String name,String email,String hash,String role)throws SQLException{
  String q="INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){p.setString(1,name);p.setString(2,email);p.setString(3,hash);p.setString(4,role);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();return r.getLong(1);}}
 }
 public List<User> findAll()throws SQLException{
  List<User> out=new ArrayList<>(); try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM users ORDER BY created_at DESC");ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}return out;
 }
 private User map(ResultSet r)throws SQLException{return new User(r.getLong("id"),r.getString("name"),r.getString("email"),r.getString("password_hash"),r.getString("role"),r.getTimestamp("created_at").toLocalDateTime());}
}
