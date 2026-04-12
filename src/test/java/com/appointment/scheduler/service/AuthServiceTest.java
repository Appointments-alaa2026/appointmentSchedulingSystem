package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Admin;
import com.appointment.scheduler.repository.AdminRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Test
    void testLoginSuccess() {
        AdminRepository adminRepository = mock(AdminRepository.class);
        Admin admin = new Admin("Alaa", "1234");

        when(adminRepository.getAdmin()).thenReturn(admin);

        AuthService authService = new AuthService(adminRepository);

        boolean result = authService.login("Alaa", "1234");

        assertTrue(result);
        assertTrue(authService.isLoggedIn());
    }

    @Test
    void testLoginFailWithWrongUsername() {
        AdminRepository adminRepository = mock(AdminRepository.class);
        Admin admin = new Admin("Alaa", "1234");

        when(adminRepository.getAdmin()).thenReturn(admin);

        AuthService authService = new AuthService(adminRepository);

        boolean result = authService.login("wrong", "1234");

        assertFalse(result);
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testLoginFailWithWrongPassword() {
        AdminRepository adminRepository = mock(AdminRepository.class);
        Admin admin = new Admin("Alaa", "1234");

        when(adminRepository.getAdmin()).thenReturn(admin);

        AuthService authService = new AuthService(adminRepository);

        boolean result = authService.login("Alaa", "wrongPassword");

        assertFalse(result);
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testLogout() {
        AdminRepository adminRepository = mock(AdminRepository.class);
        Admin admin = new Admin("Alaa", "1234");

        when(adminRepository.getAdmin()).thenReturn(admin);

        AuthService authService = new AuthService(adminRepository);

        authService.login("Alaa", "1234");
        assertTrue(authService.isLoggedIn());

        authService.logout();
        assertFalse(authService.isLoggedIn());
    }
}