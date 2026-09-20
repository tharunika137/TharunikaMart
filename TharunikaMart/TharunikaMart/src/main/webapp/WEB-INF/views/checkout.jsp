<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<div class="section-head"><div><div class="eyebrow">Almost there</div><h1>Checkout</h1></div></div>
<div class="cart-layout">
<div class="list-card">
<h2>Delivery address</h2>
<form method="post" action="<%=ctx%>/app/checkout" class="form-grid">
<input type="hidden" name="csrf" value="<%=csrf%>">
<label>Full name<input name="shippingName" required maxlength="120"></label>
<label>Address<textarea name="shippingAddress" required maxlength="500"></textarea></label>
<label>Phone number<input name="shippingPhone" type="tel" required maxlength="20"></label>
<h2>Payment method</h2>
<label class="payment-option"><input type="radio" name="paymentMethod" value="COD" checked> Cash on Delivery</label>
<label class="payment-option"><input type="radio" name="paymentMethod" value="UPI"> UPI</label>
<label class="payment-option"><input type="radio" name="paymentMethod" value="CARD"> Credit / Debit Card</label>
<p class="muted">This is a demo checkout — no real payment is processed regardless of the option chosen.</p>
<button class="btn full">Place order</button>
</form>
</div>
<aside class="summary">
<span>Order total</span><strong>₹<%=request.getAttribute("total")%></strong>
<% for(com.tharunika.tharunikamart.model.CartItem i:(java.util.List<com.tharunika.tharunikamart.model.CartItem>)request.getAttribute("items")){ %>
<p><%=esc(i.productName())%> × <%=i.quantity()%> — ₹<%=i.total()%></p>
<% } %>
</aside>
</div>
<%@ include file="footer.jsp" %>
