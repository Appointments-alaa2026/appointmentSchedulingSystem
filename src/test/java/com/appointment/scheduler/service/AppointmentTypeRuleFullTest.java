package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.strategy.AppointmentTypeRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full coverage tests for AppointmentTypeRule — all appointment types.
 *
 * @author Appointment Scheduling System
 * @version 1.0
 */
public class AppointmentTypeRuleFullTest {

    private static final TimeSlot VALID_SLOT = new TimeSlot(
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(1)
    );

    private Appointment makeAppointment(AppointmentType type, int participants) {
        return new Appointment("X", null, VALID_SLOT,
                AppointmentStatus.AVAILABLE, type, participants);
    }

    // ─── null checks ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns false when appointment is null")
    void rule_nullAppointment_returnsFalse() {
        assertFalse(new AppointmentTypeRule().isValid(null));
    }

    @Test
    @DisplayName("Returns false when appointment type is null")
    void rule_nullType_returnsFalse() {
        Appointment appt = new Appointment("X", null, VALID_SLOT,
                AppointmentStatus.AVAILABLE, null, 1);
        assertFalse(new AppointmentTypeRule().isValid(appt));
    }

    // ─── INDIVIDUAL ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("INDIVIDUAL with 1 participant is valid")
    void individual_oneParticipant_valid() {
        assertTrue(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.INDIVIDUAL, 1)));
    }

    @Test
    @DisplayName("INDIVIDUAL with 2 participants is invalid")
    void individual_twoParticipants_invalid() {
        assertFalse(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.INDIVIDUAL, 2)));
    }

    // ─── GROUP ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GROUP with 3 participants is valid")
    void group_threeParticipants_valid() {
        assertTrue(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.GROUP, 3)));
    }

    @Test
    @DisplayName("GROUP with 1 participant is invalid")
    void group_oneParticipant_invalid() {
        assertFalse(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.GROUP, 1)));
    }

    @Test
    @DisplayName("GROUP with 6 participants exceeds limit — invalid")
    void group_sixParticipants_invalid() {
        assertFalse(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.GROUP, 6)));
    }

    // ─── URGENT ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("URGENT with valid time slot is valid")
    void urgent_validSlot_valid() {
        assertTrue(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.URGENT, 1)));
    }

    @Test
    @DisplayName("URGENT with null time slot is invalid")
    void urgent_nullSlot_invalid() {
        Appointment appt = new Appointment("X", null, null,
                AppointmentStatus.AVAILABLE, AppointmentType.URGENT, 1);
        assertFalse(new AppointmentTypeRule().isValid(appt));
    }

    // ─── FOLLOW_UP ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("FOLLOW_UP with valid time slot is valid")
    void followUp_validSlot_valid() {
        assertTrue(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.FOLLOW_UP, 1)));
    }

    @Test
    @DisplayName("FOLLOW_UP with null time slot is invalid")
    void followUp_nullSlot_invalid() {
        Appointment appt = new Appointment("X", null, null,
                AppointmentStatus.AVAILABLE, AppointmentType.FOLLOW_UP, 1);
        assertFalse(new AppointmentTypeRule().isValid(appt));
    }

    // ─── ASSESSMENT ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("ASSESSMENT with valid time slot is valid")
    void assessment_validSlot_valid() {
        assertTrue(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.ASSESSMENT, 1)));
    }

    @Test
    @DisplayName("ASSESSMENT with null time slot is invalid")
    void assessment_nullSlot_invalid() {
        Appointment appt = new Appointment("X", null, null,
                AppointmentStatus.AVAILABLE, AppointmentType.ASSESSMENT, 1);
        assertFalse(new AppointmentTypeRule().isValid(appt));
    }

    // ─── VIRTUAL ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("VIRTUAL with valid time slot is valid")
    void virtual_validSlot_valid() {
        assertTrue(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.VIRTUAL, 1)));
    }

    @Test
    @DisplayName("VIRTUAL with null time slot is invalid")
    void virtual_nullSlot_invalid() {
        Appointment appt = new Appointment("X", null, null,
                AppointmentStatus.AVAILABLE, AppointmentType.VIRTUAL, 1);
        assertFalse(new AppointmentTypeRule().isValid(appt));
    }

    // ─── IN_PERSON ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("IN_PERSON with valid time slot is valid")
    void inPerson_validSlot_valid() {
        assertTrue(new AppointmentTypeRule().isValid(makeAppointment(AppointmentType.IN_PERSON, 1)));
    }

    @Test
    @DisplayName("IN_PERSON with null time slot is invalid")
    void inPerson_nullSlot_invalid() {
        Appointment appt = new Appointment("X", null, null,
                AppointmentStatus.AVAILABLE, AppointmentType.IN_PERSON, 1);
        assertFalse(new AppointmentTypeRule().isValid(appt));
    }
}