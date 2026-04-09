package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Admin;

/**
 * Handles admin login and logout
 */
public class AuthService {

    private Admin loggedInAdmin;

    public boolean login(Admin admin, String username, String password) {
        if (admin.getUsername().equals(username) &&
            admin.getPassword().equals(password)) {

            loggedInAdmin = admin;
            return true;
        }
        return false;
    }

    public void logout() {
        loggedInAdmin = null;
    }

    public boolean isLoggedIn() {
        return loggedInAdmin != null;
    }
}