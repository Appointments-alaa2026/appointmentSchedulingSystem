package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.strategy.DurationRule;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DurationRuleTest {

    @Test
    void testValidDuration() {
        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 4, 10, 10, 0),
                LocalDateTime.of(2026, 4, 10, 12, 0)
        );

        Appointment appointment = new Appointment(
                "1", null, slot,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL, 1
        );

        DurationRule rule = new DurationRule();

        assertTrue(rule.isValid(appointment));
    }

    @Test
    void testInvalidDuration() {
        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 4, 10, 10, 0),
                LocalDateTime.of(2026, 4, 10, 15, 0)
        );

        Appointment appointment = new Appointment(
                "1", null, slot,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL, 1
        );

        DurationRule rule = new DurationRule();

        assertFalse(rule.isValid(appointment));
    }
}