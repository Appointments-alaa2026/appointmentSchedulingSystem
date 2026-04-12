package com.appointment.scheduler.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void testDefaultConstructor() {
        Admin admin = new Admin();

        assertNull(admin.getUsername());
        assertNull(admin.getPassword());
    }

    @Test
    void testParameterizedConstructor() {
        Admin admin = new Admin("Alaa", "1234");

        assertEquals("Alaa", admin.getUsername());
        assertEquals("1234", admin.getPassword());
    }

    @Test
    void testSetters() {
        Admin admin = new Admin();
        admin.setUsername("Lama");
        admin.setPassword("abcd");

        assertEquals("Lama", admin.getUsername());
        assertEquals("abcd", admin.getPassword());
    }
}