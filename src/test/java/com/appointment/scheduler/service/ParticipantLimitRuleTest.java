package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.strategy.ParticipantLimitRule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantLimitRuleTest {

    @Test
    void testValidParticipants() {
        Appointment appointment = new Appointment(
                "1", null, null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL, 3
        );

        ParticipantLimitRule rule = new ParticipantLimitRule();

        assertTrue(rule.isValid(appointment));
    }

    @Test
    void testInvalidParticipants() {
        Appointment appointment = new Appointment(
                "1", null, null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL, 10
        );

        ParticipantLimitRule rule = new ParticipantLimitRule();

        assertFalse(rule.isValid(appointment));
    }
}