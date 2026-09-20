<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="static com.tharunika.tharunikamart.util.ValidationUtil.esc" %>
<%@ page import="static com.tharunika.tharunikamart.util.ValidationUtil.imageSrc" %>
<%@ include file="header.jsp" %>
<section class="hero"><div><div class="eyebrow">Campus marketplace</div><h1>Buy smarter.<br><em>Sell beautifully.</em></h1><p>Discover useful products from independent sellers — buy them outright, or rent for a while.</p><div class="hero-actions"><a class="btn" href="#products">Explore products</a><a class="btn btn-ghost" href="<%=ctx%>/app/register">Become a seller</a></div></div><div class="hero-card"><div class="hero-icon">✦</div><b>One marketplace.</b><span>Buyer · Seller · Admin</span><small>Secure sessions · BCrypt · JDBC</small></div></section>
<% String qVal=request.getParameter("q")==null?"":request.getParameter("q");
   String catVal=request.getParameter("category")==null?"":request.getParameter("category");
   String typeVal=request.getParameter("type")==null?"":request.getParameter("type").toUpperCase(); %>
<section class="toolbar"><form method="get" action="<%=ctx%>/app/home" class="search"><input name="q" placeholder="Search products..." value="<%=esc(qVal)%>"><select name="category"><option value="" <%=catVal.isEmpty()?"selected":""%>>All categories</option><option <%=catVal.equals("College Essentials")?"selected":""%>>College Essentials</option><option <%=catVal.equals("Fashion")?"selected":""%>>Fashion</option></select><select name="type"><option value="" <%=typeVal.isEmpty()?"selected":""%>>Buy or rent</option><option value="SALE" <%="SALE".equals(typeVal)?"selected":""%>>For sale</option><option value="RENT" <%="RENT".equals(typeVal)?"selected":""%>>For rent</option></select><button class="btn btn-small">Search</button></form></section>
<section id="products"><div class="section-head"><div><div class="eyebrow">Featured collection</div><h2>Fresh finds</h2></div><span class="muted"><%=((java.util.List)request.getAttribute("products")).size()%> products</span></div>
<div class="grid">
<% java.util.Map ratings=(java.util.Map)request.getAttribute("ratings");
   for(Object o:(java.util.List)request.getAttribute("products")){ com.tharunika.tharunikamart.model.Product p=(com.tharunika.tharunikamart.model.Product)o;
   double[] rating=(double[])ratings.get(p.id()); %>
<article class="product-card"><div class="product-img"><img src="<%=imageSrc(p.imageUrl(),ctx)%>" alt="<%=esc(p.name())%>"></div><div class="product-body"></div>
<span class="tag <%=p.isRental()?"tag-rent":""%>"><%=esc(p.category())%> · <%=p.isRental()?"For rent":"For sale"%></span>
<% if(rating!=null){%><span class="rating">★ <%=String.format("%.1f",rating[0])%> (<%=(int)rating[1]%>)</span><%}%>
<h3><%=esc(p.name())%></h3><p><%=esc(p.description())%></p>
<div class="product-meta"><strong>₹<%=p.price()%><%=p.isRental()?"<small>/day</small>":""%></strong><span><%=p.stockQty()%> in stock</span></div>
<% if(current!=null&&"BUYER".equals(current.role())&&p.stockQty()>0){%><form method="post" action="<%=ctx%>/app/cart/add"><input type="hidden" name="csrf" value="<%=csrf%>"><input type="hidden" name="productId" value="<%=p.id()%>"><input type="hidden" name="quantity" value="1"><button class="btn full"><%=p.isRental()?"Rent this":"Add to cart"%></button></form>
<form method="post" action="<%=ctx%>/app/review/add" class="review-form"><input type="hidden" name="csrf" value="<%=csrf%>"><input type="hidden" name="productId" value="<%=p.id()%>"><select name="rating"><option value="5">★★★★★</option><option value="4">★★★★</option><option value="3">★★★</option><option value="2">★★</option><option value="1">★</option></select><input name="comment" maxlength="1000" placeholder="Add a quick review..."><button class="btn btn-small btn-ghost">Rate</button></form>
<%}else if(current==null){%><a class="btn full" href="<%=ctx%>/app/login">Login to buy</a><%}%></div></article>
<% } %></div></section><%@ include file="footer.jsp" %>
