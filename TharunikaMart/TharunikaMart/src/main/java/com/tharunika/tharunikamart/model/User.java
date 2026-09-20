package com.tharunika.tharunikamart.model;
import java.time.LocalDateTime;
public record User(long id, String name, String email, String passwordHash, String role, LocalDateTime createdAt) {}
