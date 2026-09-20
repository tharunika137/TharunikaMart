package com.tharunika.tharunikamart.service;
import com.tharunika.tharunikamart.dao.UserDAO; import com.tharunika.tharunikamart.model.User; import com.tharunika.tharunikamart.util.PasswordUtil;
public class AuthService {
 private final UserDAO dao; public AuthService(UserDAO dao){this.dao=dao;}
 /** Emails are matched and stored case-insensitively, so "User@X.com" and "user@x.com" are treated as the same account. */
 private String normalize(String email){ return email==null?"":email.trim().toLowerCase(); }
 public User login(String email,String password,String role)throws Exception{
  if(password==null||password.isEmpty())return null;
  User u=dao.findByEmail(normalize(email)).orElse(null);
  if(u==null||!u.role().equals(role)||!PasswordUtil.matches(password,u.passwordHash()))return null;
  return u;
 }
 public long register(String name,String email,String password,String role)throws Exception{
  String normEmail=normalize(email);
  if(name==null||name.length()<2||!com.tharunika.tharunikamart.util.ValidationUtil.validEmail(normEmail)||password==null||password.length()<6)throw new IllegalArgumentException("Please enter valid details. Password must be at least 6 characters.");
  if(!role.equals("BUYER")&&!role.equals("SELLER"))throw new IllegalArgumentException("Invalid registration role.");
  if(dao.findByEmail(normEmail).isPresent())throw new IllegalArgumentException("Email is already registered.");
  return dao.create(name,normEmail,PasswordUtil.hash(password),role);
 }
}
