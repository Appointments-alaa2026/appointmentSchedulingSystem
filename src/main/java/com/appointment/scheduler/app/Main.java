package com.appointment.scheduler.app;

import com.appointment.scheduler.repository.AdminRepository;
import com.appointment.scheduler.repository.ConfigAdminRepository;
import com.appointment.scheduler.service.AuthService;

public class Main {
    public static void main(String[] args) {
        AdminRepository adminRepository = new ConfigAdminRepository();
        AuthService authService = new AuthService(adminRepository);

        boolean loggedIn = authService.login("admin", "1234");

        if (loggedIn) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid username or password.");
        }
    }
}