package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Admin;
import com.appointment.scheduler.repository.ConfigAdminRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminRepoTest {

    @Test
    void testGetAdminNotNull() {
        ConfigAdminRepository repo = new ConfigAdminRepository();
        Admin admin = repo.getAdmin();

        assertNotNull(admin);
    }

    @Test
    void testUsernameAndPasswordLoaded() {
        ConfigAdminRepository repo = new ConfigAdminRepository();
        Admin admin = repo.getAdmin();

        assertNotNull(admin.getUsername());
        assertNotNull(admin.getPassword());
    }

    @Test
    void testCorrectValuesFromFile() {
        ConfigAdminRepository repo = new ConfigAdminRepository();
        Admin admin = repo.getAdmin();

        assertEquals("Alaa", admin.getUsername());
        assertEquals("1234", admin.getPassword());
    }
}