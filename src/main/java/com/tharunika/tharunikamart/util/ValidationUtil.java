package com.tharunika.tharunikamart.util;
import java.math.BigDecimal;
public final class ValidationUtil {
 private ValidationUtil(){}
 public static String clean(String s){ return s == null ? "" : s.trim(); }
 public static boolean validEmail(String s){ return s != null && s.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"); }
 public static BigDecimal money(String s){ try { BigDecimal v=new BigDecimal(s); return v.scale()>2?v.setScale(2,java.math.RoundingMode.HALF_UP):v; } catch(Exception e){return null;} }
 /** Parses an int, returning def on any invalid input instead of throwing. */
 public static int intOr(String s,int def){ try { return Integer.parseInt(clean(s)); } catch(Exception e){ return def; } }
 /** Clamps a requested quantity to a safe, positive range no larger than the available stock. */
 public static int clampQty(int requested,int stock){ if(stock<1) return 0; if(requested<1) return 1; return Math.min(requested,stock); }
 /** Clamps a star rating to the 1-5 range accepted by the reviews table. */
 public static int clampRating(int rating){ return Math.max(1,Math.min(5,rating)); }
 /** HTML-escapes user-supplied text before it is written into a JSP page, to prevent stored XSS. */
 public static String esc(String s){
  if(s==null) return "";
  StringBuilder b=new StringBuilder(s.length());
  for(int i=0;i<s.length();i++){ char c=s.charAt(i);
   switch(c){
    case '&': b.append("&amp;"); break;
    case '<': b.append("&lt;"); break;
    case '>': b.append("&gt;"); break;
    case '"': b.append("&quot;"); break;
    case '\'': b.append("&#39;"); break;
    default: b.append(c);
   }
  }
  return b.toString();
 }
 /** Resolves a stored image_url into a src the browser can actually load: an external http(s) URL is used as-is,
  *  a blank one falls back to the shared placeholder, and anything else (a path into our own /assets/img) is
  *  made relative to this deployment's context path so it still works whether the app is deployed at "/" or "/TharunikaMart". */
 public static String imageSrc(String url,String ctx){
  if(url==null||url.isBlank()) return ctx+"/assets/img/placeholder.svg";
  if(url.startsWith("http://")||url.startsWith("https://")) return url;
  return ctx+(url.startsWith("/")?url:"/"+url);
 }
}