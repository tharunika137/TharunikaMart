package com.tharunika.tharunikamart.filter;
import javax.servlet.*;import javax.servlet.http.*;import java.io.IOException;
public class AuthFilter implements Filter {
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
  HttpServletRequest r=(HttpServletRequest)req;HttpServletResponse s=(HttpServletResponse)res;String path=r.getRequestURI().substring(r.getContextPath().length());
  if(path.startsWith("/app/")&&!path.equals("/app/home")&&!path.equals("/app/login")&&!path.equals("/app/register")&&!path.startsWith("/app/product")){
   if(r.getSession(false)==null||r.getSession(false).getAttribute("user")==null){s.sendRedirect(r.getContextPath()+"/app/login");return;}
  } chain.doFilter(req,res);
 }
}
