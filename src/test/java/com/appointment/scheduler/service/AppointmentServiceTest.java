package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    @Test
    void testBookingSuccess() {
        AppointmentService service = new AppointmentService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 4, 10, 10, 0),
                LocalDateTime.of(2026, 4, 10, 11, 0)
        );

        Appointment appointment = new Appointment(
                "1",
                user,
                slot,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.bookAppointment("1", user);

        assertTrue(result);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
    }

    @Test
    void testBookingFailIfAlreadyBooked() {
        AppointmentService service = new AppointmentService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 4, 10, 10, 0),
                LocalDateTime.of(2026, 4, 10, 11, 0)
        );

        Appointment appointment = new Appointment(
                "1",
                user,
                slot,
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.bookAppointment("1", user);

        assertFalse(result);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
    }
}