package com.appointment.scheduler.model;

/**
 * Represents an administrator in the system.
 */
public class Admin {

    private String username;
    private String password;

    /**
     * Default constructor.
     */
    public Admin() {
    }

    /**
     * Parameterized constructor to initialize admin credentials.
     *
     * @param username the admin username
     * @param password the admin password
     */
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}