package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.strategy.TypeRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TypeRuleTest {

    @Test
    void testValidIndividualAppointment() {
        Appointment appointment = new Appointment(
                "1",
                null,
                null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );

        TypeRule rule = new TypeRule();

        assertTrue(rule.isValid(appointment));
    }

    @Test
    void testInvalidIndividualAppointment() {
        Appointment appointment = new Appointment(
                "2",
                null,
                null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                3
        );

        TypeRule rule = new TypeRule();

        assertFalse(rule.isValid(appointment));
    }

    @Test
    void testValidGroupAppointment() {
        Appointment appointment = new Appointment(
                "3",
                null,
                null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.GROUP,
                4
        );

        TypeRule rule = new TypeRule();

        assertTrue(rule.isValid(appointment));
    }

    @Test
    void testInvalidGroupAppointment() {
        Appointment appointment = new Appointment(
                "4",
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