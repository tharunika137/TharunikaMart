package com.tharunika.tharunikamart.service;
import com.tharunika.tharunikamart.dao.UserDAO; import com.tharunika.tharunikamart.model.User; import com.tharunika.tharunikamart.util.PasswordUtil; import java.sql.SQLException;
public class AuthService {
 private final UserDAO dao; public AuthService(UserDAO dao){this.dao=dao;}
 public User login(String email,String password,String role)throws Exception{
  User u=dao.findByEmail(email).orElse(null); if(u==null||!u.role().equals(role)||!PasswordUtil.matches(password,u.passwordHash()))return null; return u;
 }
 public long register(String name,String email,String password,String role)throws Exception{
  if(name.length()<2||!com.tharunika.tharunikamart.util.ValidationUtil.validEmail(email)||password.length()<6)throw new IllegalArgumentException("Please enter valid details. Password must be at least 6 characters.");
  if(!role.equals("BUYER")&&!role.equals("SELLER"))throw new IllegalArgumentException("Invalid registration role.");
  if(dao.findByEmail(email).isPresent())throw new IllegalArgumentException("Email is already registered.");
  return dao.create(name,email,PasswordUtil.hash(password),role);
 }
}
