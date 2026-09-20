<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tharunika.tharunikamart.model.User" %>
<%@ page import="static com.tharunika.tharunikamart.util.ValidationUtil.esc" %>
<% User current=(User)session.getAttribute("user"); String ctx=request.getContextPath();
   String csrf=(String)session.getAttribute("csrf");
   if(csrf==null){csrf=java.util.UUID.randomUUID().toString();session.setAttribute("csrf",csrf);}
%>
<!doctype html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title><%= request.getAttribute("title")==null?"TharunikaMart":request.getAttribute("title") %></title>
<link rel="icon" type="image/png" href="<%=ctx%>/assets/img/logo.png">
<link rel="stylesheet" href="<%=ctx%>/assets/css/style.css"></head><body>
<nav class="nav"><a class="brand" href="<%=ctx%>/app/home"><img class="brand-mark" src="<%=ctx%>/assets/img/logo.png" alt="TharunikaMart logo"> Tharunika<span>Mart</span></a>
<div class="nav-links"><a href="<%=ctx%>/app/home">Shop</a>
<% if(current!=null && "BUYER".equals(current.role())) { %><a href="<%=ctx%>/app/orders">Orders</a><a href="<%=ctx%>/app/cart">Cart</a><% } %>
<% if(current!=null && "SELLER".equals(current.role())) { %><a href="<%=ctx%>/app/seller">Seller Studio</a><% } %>
<% if(current!=null && "ADMIN".equals(current.role())) { %><a href="<%=ctx%>/app/admin">Admin</a><% } %>
<% if(current==null){%><a class="btn btn-small" href="<%=ctx%>/app/login">Login</a><%}else{%><span class="user-pill"><%=esc(current.name())%></span><a href="<%=ctx%>/app/logout">Logout</a><%}%>
</div></nav><main class="container">
