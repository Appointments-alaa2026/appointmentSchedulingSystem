package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Admin;
import com.appointment.scheduler.repository.AdminRepository;

/**
 * Service responsible for administrator authentication and session management.
 * <p>
 * This service delegates administrator data retrieval to an AdminRepository
 * and handles only authentication-related operations such as login, logout,
 * and session status.
 * </p>
 */
public class AuthService {

    private final AdminRepository adminRepository;
    private Admin loggedInAdmin;

    /**
     * Constructs an AuthService with the specified administrator repository.
     *
     * @param adminRepository the repository used to retrieve administrator data
     */
    public AuthService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * Authenticates an administrator using the provided username and password.
     *
     * @param username the username entered by the administrator
     * @param password the password entered by the administrator
     * @return true if the credentials are correct, false otherwise
     */
    public boolean login(String username, String password) {
        Admin storedAdmin = adminRepository.getAdmin();

        if (storedAdmin.getUsername().equals(username)
                && storedAdmin.getPassword().equals(password)) {
            loggedInAdmin = storedAdmin;
            return true;
        }

        return false;
    }

    /**
     * Logs out the currently authenticated administrator.
     */
    public void logout() {
        loggedInAdmin = null;
    }

    /**
     * Checks whether an administrator is currently logged in.
     *
     * @return true if an administrator is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return loggedInAdmin != null;
    }

    /**
     * Returns the currently logged-in administrator.
     *
     * @return the logged-in Admin object, or null if no administrator is logged in
     */
    public Admin getLoggedInAdmin() {
        return loggedInAdmin;
    }
}