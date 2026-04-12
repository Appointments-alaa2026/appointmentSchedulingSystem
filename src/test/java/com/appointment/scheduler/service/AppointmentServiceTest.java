package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.strategy.DurationRule;
import com.appointment.scheduler.strategy.ParticipantLimitRule;
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

        // 🔥 إضافة rules (مهم جدًا)
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());

        boolean result = service.bookAppointment("1");

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

        // 🔥 حتى هون نضيف rules (للتوحيد)
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());

        boolean result = service.bookAppointment("1");

        assertFalse(result);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
    }
}