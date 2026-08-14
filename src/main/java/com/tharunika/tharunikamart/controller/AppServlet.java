package com.tharunika.tharunikamart.controller;
import com.tharunika.tharunikamart.dao.*;import com.tharunika.tharunikamart.model.*;import com.tharunika.tharunikamart.service.*;import com.tharunika.tharunikamart.util.ValidationUtil;
import javax.servlet.*;import javax.servlet.http.*;import java.io.*;import java.math.*;import java.util.*;
public class AppServlet extends HttpServlet {
 private AuthService auth; private ShopService shop; private ProductDAO products; private OrderDAO orders; private UserDAO users;
 public void init(){javax.sql.DataSource ds=(javax.sql.DataSource)getServletContext().getAttribute("ds");users=new UserDAO(ds);products=new ProductDAO(ds);orders=new OrderDAO(ds);auth=new AuthService(users);shop=new ShopService(products,new CartDAO(ds),orders);}
 private void page(HttpServletRequest r,HttpServletResponse s,String view)throws Exception{r.getRequestDispatcher("/WEB-INF/views/"+view+".jsp").forward(r,s);}
 private User user(HttpServletRequest r){return (User)r.getSession().getAttribute("user");}
 protected void doGet(HttpServletRequest r,HttpServletResponse s)throws IOException,ServletException{
  String path=r.getPathInfo();try{
   if(path==null||path.equals("/home")){r.setAttribute("products",shop.browse(ValidationUtil.clean(r.getParameter("q")),ValidationUtil.clean(r.getParameter("category"))));page(r,s,"home");}
   else if(path.equals("/login"))page(r,s,"login"); else if(path.equals("/register"))page(r,s,"register");
   else if(path.equals("/logout")){r.getSession().invalidate();s.sendRedirect(r.getContextPath()+"/app/home");}
   else if(path.equals("/cart")){r.setAttribute("items",shop.cart(user(r).id()));r.setAttribute("total",shop.total(shop.cart(user(r).id())));page(r,s,"cart");}
   else if(path.equals("/orders")){r.setAttribute("orders",orders.buyerOrders(user(r).id()));page(r,s,"orders");}
   else if(path.equals("/seller")){r.setAttribute("products",products.sellerProducts(user(r).id()));page(r,s,"seller");}
   else if(path.equals("/admin")){r.setAttribute("users",users.findAll());r.setAttribute("orders",orders.allOrders());r.setAttribute("products",products.search("",""));page(r,s,"admin");}
   else page(r,s,"home");
  }catch(Exception e){throw new ServletException(e);}
 }
 protected void doPost(HttpServletRequest r,HttpServletResponse s)throws IOException,ServletException{
  String path=r.getPathInfo();try{
   if(path.equals("/login")){String role=ValidationUtil.clean(r.getParameter("role")).toUpperCase();User u=auth.login(ValidationUtil.clean(r.getParameter("email")),r.getParameter("password"),role);if(u==null){r.setAttribute("error","Invalid credentials or wrong account type.");page(r,s,"login");return;}r.changeSessionId();r.getSession().setAttribute("user",u);String dest=role.equals("SELLER")?"/app/seller":role.equals("ADMIN")?"/app/admin":"/app/home";s.sendRedirect(r.getContextPath()+dest);}
   else if(path.equals("/register")){try{auth.register(ValidationUtil.clean(r.getParameter("name")),ValidationUtil.clean(r.getParameter("email")),r.getParameter("password"),ValidationUtil.clean(r.getParameter("role")).toUpperCase());r.setAttribute("success","Account created. Please log in.");page(r,s,"login");}catch(IllegalArgumentException e){r.setAttribute("error",e.getMessage());page(r,s,"register");}}
   else if(path.equals("/cart/add")){shop.cart.add(user(r).id(),Long.parseLong(r.getParameter("productId")),Integer.parseInt(r.getParameter("quantity")));s.sendRedirect(r.getContextPath()+"/app/cart");}
   else if(path.equals("/cart/update")){shop.cart.update(user(r).id(),Long.parseLong(r.getParameter("productId")),Integer.parseInt(r.getParameter("quantity")));s.sendRedirect(r.getContextPath()+"/app/cart");}
   else if(path.equals("/cart/remove")){shop.cart.remove(user(r).id(),Long.parseLong(r.getParameter("productId")));s.sendRedirect(r.getContextPath()+"/app/cart");}
   else if(path.equals("/checkout")){r.setAttribute("orderId",shop.checkout(user(r).id()));r.setAttribute("message","Mock payment confirmed. Your order has been placed.");page(r,s,"success");}
   else if(path.equals("/seller/product/save")){long id=Long.parseLong(r.getParameter("id"));String name=ValidationUtil.clean(r.getParameter("name"));String desc=ValidationUtil.clean(r.getParameter("description"));BigDecimal price=ValidationUtil.money(r.getParameter("price"));int stock=Integer.parseInt(r.getParameter("stock"));String cat=ValidationUtil.clean(r.getParameter("category"));String image=ValidationUtil.clean(r.getParameter("image"));if(id==0)products.create(user(r).id(),name,desc,price,stock,cat,image);else products.update(id,user(r).id(),name,desc,price,stock,cat,image);s.sendRedirect(r.getContextPath()+"/app/seller");}
   else if(path.equals("/seller/product/delete")){products.delete(Long.parseLong(r.getParameter("id")),user(r).id());s.sendRedirect(r.getContextPath()+"/app/seller");}
   else if(path.equals("/admin/product/delete")){products.moderateDelete(Long.parseLong(r.getParameter("id")));s.sendRedirect(r.getContextPath()+"/app/admin");}
   else if(path.equals("/admin/order/status")){orders.updateStatus(Long.parseLong(r.getParameter("id")),ValidationUtil.clean(r.getParameter("status")));s.sendRedirect(r.getContextPath()+"/app/admin");}
   else s.sendRedirect(r.getContextPath()+"/app/home");
  }catch(Exception e){r.setAttribute("error",e.getMessage());try{page(r,s,"error");}catch(Exception x){throw new ServletException(x);}}
 }
}
