package com.tharunika.tharunikamart;
import com.tharunika.tharunikamart.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class PasswordUtilTest {
 @Test void hashesAndVerifies(){String hash=PasswordUtil.hash("password");assertNotEquals("password",hash);assertTrue(PasswordUtil.matches("password",hash));assertFalse(PasswordUtil.matches("wrong",hash));}
}
