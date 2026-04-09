package com.appointment.scheduler.model;

public class Admin extends User {

    private String username;
    private String password;

    public Admin(String id, String name, String email,
                         String username, String password) {
        super(id, name, email);
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}