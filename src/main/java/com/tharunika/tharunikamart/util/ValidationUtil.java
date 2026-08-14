package com.tharunika.tharunikamart.util;
import java.math.BigDecimal;
public final class ValidationUtil {
 private ValidationUtil(){}
 public static String clean(String s){ return s == null ? "" : s.trim(); }
 public static boolean validEmail(String s){ return s != null && s.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"); }
 public static BigDecimal money(String s){ try { BigDecimal v=new BigDecimal(s); return v.scale()>2?v.setScale(2,java.math.RoundingMode.HALF_UP):v; } catch(Exception e){return null;} }
}
