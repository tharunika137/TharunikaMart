
package com.tharunika.tharunikamart.filter;
import javax.servlet.*;import javax.servlet.http.*;import java.io.IOException;
/** Adds baseline hardening headers to every response: no MIME-sniffing, no framing (clickjacking), a same-origin CSP, and a conservative referrer policy. */
public class SecurityHeadersFilter implements Filter {
 public void init(FilterConfig config) throws ServletException {}
 public void destroy() {}
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
  HttpServletResponse s=(HttpServletResponse)res;
  s.setHeader("X-Content-Type-Options","nosniff");
  s.setHeader("X-Frame-Options","DENY");
  s.setHeader("Content-Security-Policy","default-src 'self'; img-src 'self' data:; style-src 'self' https://fonts.googleapis.com; font-src https://fonts.gstatic.com; script-src 'self'; frame-ancestors 'none'");
  s.setHeader("Referrer-Policy","same-origin");
  chain.doFilter(req,res);
 }
}