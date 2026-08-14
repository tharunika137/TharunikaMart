<%@ page import="com.tharunika.tharunikamart.model.User" %>
<% User current=(User)session.getAttribute("user"); String ctx=request.getContextPath(); %>
<!doctype html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title><%= request.getAttribute("title")==null?"TharunikaMart":request.getAttribute("title") %></title>
<link rel="stylesheet" href="<%=ctx%>/assets/css/style.css"></head><body>
<nav class="nav"><a class="brand" href="<%=ctx%>/app/home"><span class="brand-mark">T</span> Tharunika<span>Mart</span></a>
<div class="nav-links"><a href="<%=ctx%>/app/home">Shop</a>
<% if(current!=null && "BUYER".equals(current.role())) { %><a href="<%=ctx%>/app/orders">Orders</a><a href="<%=ctx%>/app/cart">Cart</a><% } %>
<% if(current!=null && "SELLER".equals(current.role())) { %><a href="<%=ctx%>/app/seller">Seller Studio</a><% } %>
<% if(current!=null && "ADMIN".equals(current.role())) { %><a href="<%=ctx%>/app/admin">Admin</a><% } %>
<% if(current==null){%><a class="btn btn-small" href="<%=ctx%>/app/login">Login</a><%}else{%><span class="user-pill"><%=current.name()%></span><a href="<%=ctx%>/app/logout">Logout</a><%}%>
</div></nav><main class="container">
