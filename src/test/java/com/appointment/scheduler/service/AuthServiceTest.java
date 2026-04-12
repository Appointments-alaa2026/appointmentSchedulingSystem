package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Admin;
import com.appointment.scheduler.repository.AdminRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for AuthService class.
 */
public class AuthServiceTest {

    @Test
    @DisplayName("Admin should log in successfully with correct username and password")
    void adminCanLoginWithValidCredentials() {
        AdminRepository repo = mock(AdminRepository.class);
        when(repo.getAdmin()).thenReturn(new Admin("Alaa", "1234"));

        AuthService service = new AuthService(repo);

        boolean loggedIn = service.login("Alaa", "1234");

        assertTrue(loggedIn);
        assertTrue(service.isLoggedIn());
        assertNotNull(service.getLoggedInAdmin());
    }

    @Test
    @DisplayName("Login should fail if username is incorrect")
    void loginFailsWhenUsernameIsWrong() {
        AdminRepository repo = mock(AdminRepository.class);
        when(repo.getAdmin()).thenReturn(new Admin("Alaa", "1234"));

        AuthService service = new AuthService(repo);

        boolean loggedIn = service.login("Ahmad", "1234");

        assertFalse(loggedIn);
        assertFalse(service.isLoggedIn());
        assertNull(service.getLoggedInAdmin());
    }

    @Test
    @DisplayName("Login should fail if password is incorrect")
    void loginFailsWhenPasswordIsWrong() {
        AdminRepository repo = mock(AdminRepository.class);
        when(repo.getAdmin()).thenReturn(new Admin("Alaa", "1234"));

        AuthService service = new AuthService(repo);

        boolean loggedIn = service.login("Alaa", "9999");

        assertFalse(loggedIn);
        assertFalse(service.isLoggedIn());
        assertNull(service.getLoggedInAdmin());
    }

    @Test
    @DisplayName("Admin logout should end the session")
    void logoutClearsAdminSession() {
        AdminRepository repo = mock(AdminRepository.class);
        when(repo.getAdmin()).thenReturn(new Admin("Alaa", "1234"));

        AuthService service = new AuthService(repo);

        service.login("Alaa", "1234");
        assertTrue(service.isLoggedIn());

        service.logout();

        assertFalse(service.isLoggedIn());
        assertNull(service.getLoggedInAdmin());
    }

    @Test
    @DisplayName("System should stay logged out before any login attempt")
    void systemStartsWithNoLoggedInAdmin() {
        AdminRepository repo = mock(AdminRepository.class);
        when(repo.getAdmin()).thenReturn(new Admin("Alaa", "1234"));

        AuthService service = new AuthService(repo);

        assertFalse(service.isLoggedIn());
        assertNull(service.getLoggedInAdmin());
    }
}