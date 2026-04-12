package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Admin;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
	@Test
    void testLoginSuccess() {
        Admin admin = new Admin("1", "CEO", "email@test.com", "Alaa", "1234");
        AuthService authService = new AuthService();

        boolean result = authService.login(admin, "Alaa", "1234");

        assertTrue(result);
    }
	
	 @Test
	    void testLoginFail() {
	        Admin admin = new Admin("1", "CEO", "email@test.com", "Alaa", "1234");
	        AuthService authService = new AuthService();

	        boolean result = authService.login(admin, "wrong", "1234");

	        assertFalse(result);
	    }
}
