package com.tharunika.tharunikamart.controller;
import com.tharunika.tharunikamart.dao.*;import com.tharunika.tharunikamart.model.*;import com.tharunika.tharunikamart.service.*;import com.tharunika.tharunikamart.util.ValidationUtil;
import javax.servlet.*;import javax.servlet.http.*;import java.io.*;import java.math.*;import java.util.*;
public class AppServlet extends HttpServlet {
 private AuthService auth; private ShopService shop; private ProductDAO products; private OrderDAO orders; private UserDAO users; private ReviewDAO reviews;
 public void init(){javax.sql.DataSource ds=(javax.sql.DataSource)getServletContext().getAttribute("ds");users=new UserDAO(ds);products=new ProductDAO(ds);orders=new OrderDAO(ds);reviews=new ReviewDAO(ds);auth=new AuthService(users);shop=new ShopService(products,new CartDAO(ds),orders);}
 private void page(HttpServletRequest r,HttpServletResponse s,String view)throws Exception{r.getRequestDispatcher("/WEB-INF/views/"+view+".jsp").forward(r,s);}
 private User user(HttpServletRequest r){return (User)r.getSession().getAttribute("user");}
 /** Every state-changing request must carry the per-session token minted in header.jsp, or it is rejected as a forged (CSRF) request. */
 private boolean csrfValid(HttpServletRequest r){
  Object expected=r.getSession().getAttribute("csrf"); String supplied=r.getParameter("csrf");
  return expected!=null && expected.equals(supplied);
 }
 protected void doGet(HttpServletRequest r,HttpServletResponse s)throws IOException,ServletException{
  String path=r.getPathInfo();try{
   if(path==null||path.equals("/home")){
    String type=ValidationUtil.clean(r.getParameter("type")).toUpperCase();
    if(!type.equals("SALE")&&!type.equals("RENT"))type="";
    List<Product> list=shop.browse(ValidationUtil.clean(r.getParameter("q")),ValidationUtil.clean(r.getParameter("category")),type);
    r.setAttribute("products",list);r.setAttribute("ratings",reviews.summaryByProduct());page(r,s,"home");
   }
   else if(path.equals("/login"))page(r,s,"login"); else if(path.equals("/register"))page(r,s,"register");
   else if(path.equals("/logout")){r.getSession().invalidate();s.sendRedirect(r.getContextPath()+"/app/home");}
   else if(path.equals("/cart")){r.setAttribute("items",shop.cart(user(r).id()));r.setAttribute("total",shop.total(shop.cart(user(r).id())));page(r,s,"cart");}
   else if(path.equals("/checkout")){ List<CartItem> items=shop.cart(user(r).id()); if(items.isEmpty()){s.sendRedirect(r.getContextPath()+"/app/cart");return;} r.setAttribute("items",items);r.setAttribute("total",shop.total(items));page(r,s,"checkout");}
   else if(path.equals("/orders")){r.setAttribute("orders",orders.buyerOrders(user(r).id()));page(r,s,"orders");}
   else if(path.equals("/seller")){r.setAttribute("products",products.sellerProducts(user(r).id()));page(r,s,"seller");}
   else if(path.equals("/admin")){r.setAttribute("users",users.findAll());r.setAttribute("orders",orders.allOrders());r.setAttribute("products",products.search("",""));page(r,s,"admin");}
   else page(r,s,"home");
  }catch(Exception e){throw new ServletException(e);}
 }
 protected void doPost(HttpServletRequest r,HttpServletResponse s)throws IOException,ServletException{
  String path=r.getPathInfo();
  if(!csrfValid(r)){r.setAttribute("error","Your session has expired. Please refresh the page and try again.");try{page(r,s,"error");}catch(Exception x){throw new ServletException(x);}return;}
  try{
    if(path.equals("/login")){String role=ValidationUtil.clean(r.getParameter("role")).toUpperCase();User u=auth.login(ValidationUtil.clean(r.getParameter("email")),r.getParameter("password"),role);if(u==null){r.setAttribute("error","Invalid credentials or wrong account type.");page(r,s,"login");return;}HttpSession oldSession=r.getSession();Object csrf=oldSession.getAttribute("csrf");oldSession.invalidate();HttpSession newSession=r.getSession(true);if(csrf!=null)newSession.setAttribute("csrf",csrf);newSession.setAttribute("user",u);String dest=role.equals("SELLER")?"/app/seller":role.equals("ADMIN")?"/app/admin":"/app/home";s.sendRedirect(r.getContextPath()+dest);}
   else if(path.equals("/register")){try{auth.register(ValidationUtil.clean(r.getParameter("name")),ValidationUtil.clean(r.getParameter("email")),r.getParameter("password"),ValidationUtil.clean(r.getParameter("role")).toUpperCase());r.setAttribute("success","Account created. Please log in.");page(r,s,"login");}catch(IllegalArgumentException e){r.setAttribute("error",e.getMessage());page(r,s,"register");}}
   else if(path.equals("/cart/add")){addToCart(r);s.sendRedirect(r.getContextPath()+"/app/cart");}
   else if(path.equals("/cart/update")){updateCart(r);s.sendRedirect(r.getContextPath()+"/app/cart");}
   else if(path.equals("/cart/remove")){shop.cart.remove(user(r).id(),Long.parseLong(r.getParameter("productId")));s.sendRedirect(r.getContextPath()+"/app/cart");}
   else if(path.equals("/checkout")){String name=ValidationUtil.clean(r.getParameter("shippingName"));String address=ValidationUtil.clean(r.getParameter("shippingAddress"));String phone=ValidationUtil.clean(r.getParameter("shippingPhone"));String paymentMethod=ValidationUtil.clean(r.getParameter("paymentMethod")).toUpperCase();if(name.isEmpty()||address.isEmpty()||phone.isEmpty())throw new IllegalArgumentException("Please enter your delivery name, address and phone number.");if(!paymentMethod.equals("UPI")&&!paymentMethod.equals("CARD")&&!paymentMethod.equals("COD"))throw new IllegalArgumentException("Please choose a valid payment method.");long orderId=shop.checkout(user(r).id(),name,address,phone,paymentMethod);r.setAttribute("orderId",orderId);r.setAttribute("shippingName",name);r.setAttribute("shippingAddress",address);r.setAttribute("paymentMethod",paymentMethod);r.setAttribute("message","Mock payment confirmed via "+paymentMethod+". Your order has been placed.");page(r,s,"success");}
   else if(path.equals("/orders/cancel")){orders.cancel(Long.parseLong(r.getParameter("id")),user(r).id());s.sendRedirect(r.getContextPath()+"/app/orders");}
   else if(path.equals("/review/add")){addReview(r);s.sendRedirect(r.getContextPath()+"/app/home");}
   else if(path.equals("/seller/product/save")){saveProduct(r);s.sendRedirect(r.getContextPath()+"/app/seller");}
   else if(path.equals("/seller/product/delete")){products.delete(Long.parseLong(r.getParameter("id")),user(r).id());s.sendRedirect(r.getContextPath()+"/app/seller");}
   else if(path.equals("/admin/product/delete")){products.moderateDelete(Long.parseLong(r.getParameter("id")));s.sendRedirect(r.getContextPath()+"/app/admin");}
   else if(path.equals("/admin/order/status")){orders.updateStatus(Long.parseLong(r.getParameter("id")),ValidationUtil.clean(r.getParameter("status")));s.sendRedirect(r.getContextPath()+"/app/admin");}
   else s.sendRedirect(r.getContextPath()+"/app/home");
  }catch(Exception e){r.setAttribute("error",e.getMessage());try{page(r,s,"error");}catch(Exception x){throw new ServletException(x);}}
 }
 /** Rejects an add-to-cart request for a product that no longer exists and never lets the requested quantity exceed live stock. */
 private void addToCart(HttpServletRequest r)throws Exception{
  User u=user(r); if(u==null)throw new IllegalArgumentException("Please log in again before adding items to your cart.");
  long productId=Long.parseLong(r.getParameter("productId"));
  Product p=products.find(productId).orElseThrow(()->new IllegalArgumentException("That listing is no longer available."));
  int requested=ValidationUtil.intOr(r.getParameter("quantity"),1);
  int qty=ValidationUtil.clampQty(requested,p.stockQty());
  if(qty<1)throw new IllegalArgumentException("That listing is out of stock.");
  shop.cart.add(u.id(),productId,qty);
 }
 private void updateCart(HttpServletRequest r)throws Exception{
  User u=user(r); if(u==null)throw new IllegalArgumentException("Please log in again before updating your cart.");
  long productId=Long.parseLong(r.getParameter("productId"));
  Product p=products.find(productId).orElseThrow(()->new IllegalArgumentException("That listing is no longer available."));
  int requested=ValidationUtil.intOr(r.getParameter("quantity"),1);
  int qty=ValidationUtil.clampQty(requested,p.stockQty());
  if(qty<1)shop.cart.remove(u.id(),productId); else shop.cart.update(u.id(),productId,qty);
 }
 private void addReview(HttpServletRequest r)throws Exception{
  User u=user(r); if(u==null||!"BUYER".equals(u.role()))throw new IllegalArgumentException("Only buyers can leave a rating.");
  long productId=Long.parseLong(r.getParameter("productId"));
  int rating=ValidationUtil.clampRating(ValidationUtil.intOr(r.getParameter("rating"),5));
  String comment=ValidationUtil.clean(r.getParameter("comment"));
  if(comment.length()>1000)comment=comment.substring(0,1000);
  reviews.upsert(productId,u.id(),rating,comment);
 }
 /** Validates a seller's listing before it is written: no blank fields, no negative price/stock, and only SALE or RENT as the listing type. */
 private void saveProduct(HttpServletRequest r)throws Exception{
  long id=Long.parseLong(r.getParameter("id"));
  String name=ValidationUtil.clean(r.getParameter("name"));String desc=ValidationUtil.clean(r.getParameter("description"));
  BigDecimal price=ValidationUtil.money(r.getParameter("price"));
  int stock=ValidationUtil.intOr(r.getParameter("stock"),-1);
  String cat=ValidationUtil.clean(r.getParameter("category"));String image=ValidationUtil.clean(r.getParameter("image"));
  String listingType=ValidationUtil.clean(r.getParameter("listingType")).toUpperCase();
  if(!listingType.equals("RENT"))listingType="SALE";
  if(name.isEmpty()||desc.isEmpty()||cat.isEmpty())throw new IllegalArgumentException("Please fill in the listing name, description and category.");
  if(price==null||price.signum()<0)throw new IllegalArgumentException("Please enter a valid, non-negative price.");
  if(stock<0)throw new IllegalArgumentException("Please enter a valid, non-negative stock quantity.");
  if(id==0)products.create(user(r).id(),name,desc,price,stock,cat,image,listingType);else products.update(id,user(r).id(),name,desc,price,stock,cat,image,listingType);
 }
}
