package com.appointment.scheduler.app;

import com.appointment.scheduler.model.Admin;
import com.appointment.scheduler.service.AuthService;

public class Main {
    public static void main(String[] args) {

        Admin admin = new Admin(
                "1", "Admin", "admin@test.com",
                "admin", "1234"
        );

        AuthService authService = new AuthService();

        boolean success = authService.login(admin, "admin", "1234");

        if (success) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }

        authService.logout();
    }
}