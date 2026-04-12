package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import com.appointment.scheduler.strategy.TypeRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTypeTest {

    @Test
    void testAppointmentTypeStoredCorrectly() {
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
                AppointmentType.VIRTUAL,
                1
        );

        assertEquals(AppointmentType.VIRTUAL, appointment.getType());
    }

    @Test
    void testGroupTypeRuleApplied() {
        Appointment appointment = new Appointment(
                "2",
                null,
                null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.GROUP,
                1
        );

        TypeRule rule = new TypeRule();

        assertFalse(rule.isValid(appointment));
    }
}