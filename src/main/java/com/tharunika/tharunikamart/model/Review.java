package com.tharunika.tharunikamart.model;
import java.time.LocalDateTime;
public record Review(long id,long productId,long userId,String userName,int rating,String comment,LocalDateTime createdAt) {}
