package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.strategy.AppointmentTypeRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for validating appointment type rules.
 * These tests verify that each appointment type follows the expected business constraints.
 *
 * @author Alaa
 * @version 1.0
 */
public class AppointmentTypeRuleTest {

    /**
     * Tests that an individual appointment is valid when it has exactly one participant.
     */
    @Test
    void testIndividualAppointmentIsValid() {
        Appointment appointment = new Appointment(
                "1",
                null,
                new TimeSlot(
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 11, 0)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );

        AppointmentTypeRule rule = new AppointmentTypeRule();
        assertTrue(rule.isValid(appointment));
    }

    /**
     * Tests that an individual appointment is invalid
     * when it has more than one participant.
     */
    @Test
    void testIndividualAppointmentFailsIfParticipantsMoreThanOne() {
        Appointment appointment = new Appointment(
                "2",
                null,
                new TimeSlot(
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 11, 0)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                3
        );

        AppointmentTypeRule rule = new AppointmentTypeRule();
        assertFalse(rule.isValid(appointment));
    }

    /**
     * Tests that a group appointment is valid
     * when the number of participants is within the allowed group limit.
     */
    @Test
    void testGroupAppointmentIsValid() {
        Appointment appointment = new Appointment(
                "3",
                null,
                new TimeSlot(
                        LocalDateTime.of(2026, 4, 10, 12, 0),
                        LocalDateTime.of(2026, 4, 10, 13, 0)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.GROUP,
                4
        );

        AppointmentTypeRule rule = new AppointmentTypeRule();
        assertTrue(rule.isValid(appointment));
    }

    /**
     * Tests that a group appointment is invalid
     * when it has only one participant.
     */
    @Test
    void testGroupAppointmentFailsIfParticipantsAreOne() {
        Appointment appointment = new Appointment(
                "4",
                null,
                new TimeSlot(
                        LocalDateTime.of(2026, 4, 10, 12, 0),
                        LocalDateTime.of(2026, 4, 10, 13, 0)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.GROUP,
                1
        );

        AppointmentTypeRule rule = new AppointmentTypeRule();
        assertFalse(rule.isValid(appointment));
    }
}