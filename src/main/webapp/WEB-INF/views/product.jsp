<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="static com.tharunika.tharunikamart.util.ValidationUtil.esc" %>
<%@ page import="static com.tharunika.tharunikamart.util.ValidationUtil.imageSrc" %>
<%@ include file="header.jsp" %>
<% com.tharunika.tharunikamart.model.Product p=(com.tharunika.tharunikamart.model.Product)request.getAttribute("product");
   java.util.List<com.tharunika.tharunikamart.model.Review> productReviews=(java.util.List<com.tharunika.tharunikamart.model.Review>)request.getAttribute("productReviews");
   double avg=0;
   if(productReviews!=null&&!productReviews.isEmpty()){
     int total=0;
     for(com.tharunika.tharunikamart.model.Review rv:productReviews){ total += rv.rating(); }
     avg=(double)total/productReviews.size();
   }
%>
<div class="product-detail">
<div class="product-detail-img"><img src="<%=imageSrc(p.imageUrl(),ctx)%>" alt="<%=esc(p.name())%>"></div>
<div class="product-detail-body">
<span class="tag <%=p.isRental()?"tag-rent":""%>"><%=esc(p.category())%> · <%=p.isRental()?"For rent":"For sale"%></span>
<h1><%=esc(p.name())%></h1>
<% if(!productReviews.isEmpty()){%><span class="rating">★ <%=String.format("%.1f",avg)%> (<%=productReviews.size()%> reviews)</span><%}%>
<p class="muted"><%=esc(p.description())%></p>
<div class="product-meta"><strong>₹<%=p.price()%><%=p.isRental()?"<small>/day</small>":""%></strong><span><%=p.stockQty()%> in stock</span></div>
<% if(current!=null&&"BUYER".equals(current.role())&&p.stockQty()>0){%><form method="post" action="<%=ctx%>/app/cart/add"><input type="hidden" name="csrf" value="<%=csrf%>"><input type="hidden" name="productId" value="<%=p.id()%>"><input type="hidden" name="quantity" value="1"><button class="btn full"><%=p.isRental()?"Rent this":"Add to cart"%></button></form><%}else if(current==null){%><a class="btn full" href="<%=ctx%>/app/login">Login to buy</a><%}%>
<h2>Reviews</h2>
<% if(productReviews.isEmpty()){%><p class="muted">No reviews yet.</p><%} for(com.tharunika.tharunikamart.model.Review rv:productReviews){%><div class="review-item"><b><%=esc(rv.userName())%></b> <span class="rating">★<%=rv.rating()%></span><p><%=esc(rv.comment())%></p></div><%}%>
</div>
</div>
<%@ include file="footer.jsp" %>
