package com.tharunika.tharunikamart;
import com.tharunika.tharunikamart.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ValidationUtilTest {
 @Test void cleanTrimsAndHandlesNull(){assertEquals("",ValidationUtil.clean(null));assertEquals("hi",ValidationUtil.clean("  hi  "));}
 @Test void validatesEmailShape(){assertTrue(ValidationUtil.validEmail("a@b.com"));assertFalse(ValidationUtil.validEmail("not-an-email"));assertFalse(ValidationUtil.validEmail(null));}
 @Test void moneyRejectsGarbageInsteadOfThrowing(){assertNull(ValidationUtil.money("not-a-number"));assertEquals(0,new java.math.BigDecimal("12.30").compareTo(ValidationUtil.money("12.3")));}
 @Test void intOrFallsBackOnBadInput(){assertEquals(5,ValidationUtil.intOr("5",1));assertEquals(1,ValidationUtil.intOr("oops",1));assertEquals(1,ValidationUtil.intOr(null,1));}
 @Test void clampQtyNeverExceedsStockOrGoesBelowOne(){assertEquals(3,ValidationUtil.clampQty(3,10));assertEquals(10,ValidationUtil.clampQty(999,10));assertEquals(1,ValidationUtil.clampQty(-5,10));assertEquals(0,ValidationUtil.clampQty(1,0));}
 @Test void clampRatingStaysInFiveStarRange(){assertEquals(1,ValidationUtil.clampRating(0));assertEquals(5,ValidationUtil.clampRating(9));assertEquals(3,ValidationUtil.clampRating(3));}
 @Test void escNeutralizesHtmlMetacharacters(){
  assertEquals("&lt;script&gt;alert(1)&lt;/script&gt;",ValidationUtil.esc("<script>alert(1)</script>"));
  assertEquals("Tom &amp; Jerry&#39;s &quot;shop&quot;",ValidationUtil.esc("Tom & Jerry's \"shop\""));
  assertEquals("",ValidationUtil.esc(null));
 }
}
