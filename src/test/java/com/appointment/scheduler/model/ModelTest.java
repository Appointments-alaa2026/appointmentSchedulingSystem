package com.appointment.scheduler.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for model classes: User, TimeSlot, Appointment.
 *
 * @author Appointment Scheduling System
 * @version 1.0
 */
public class ModelTest {

    // ─── User ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("User constructor sets all fields correctly")
    void user_constructorSetsFields() {
        User user = new User("1", "Ali", "ali@test.com");

        assertEquals("1", user.getId());
        assertEquals("Ali", user.getName());
        assertEquals("ali@test.com", user.getEmail());
    }

    @Test
    @DisplayName("Two users with same data are independent objects")
    void user_independentObjects() {
        User u1 = new User("1", "Ali", "ali@test.com");
        User u2 = new User("1", "Ali", "ali@test.com");

        assertNotSame(u1, u2);
        assertEquals(u1.getId(), u2.getId());
    }

    // ─── TimeSlot ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TimeSlot constructor stores start and end times")
    void timeSlot_constructorStoresTimes() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2026, 5, 1, 11, 0);

        TimeSlot slot = new TimeSlot(start, end);

        assertEquals(start, slot.getStartTime());
        assertEquals(end,   slot.getEndTime());
    }

    @Test
    @DisplayName("TimeSlot setters update start and end times")
    void timeSlot_settersUpdateTimes() {
        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 5, 1, 10, 0),
                LocalDateTime.of(2026, 5, 1, 11, 0)
        );

        LocalDateTime newStart = LocalDateTime.of(2026, 6, 1, 9, 0);
        LocalDateTime newEnd   = LocalDateTime.of(2026, 6, 1, 10, 0);

        slot.setStartTime(newStart);
        slot.setEndTime(newEnd);

        assertEquals(newStart, slot.getStartTime());
        assertEquals(newEnd,   slot.getEndTime());
    }

    // ─── Appointment ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Appointment constructor sets all fields correctly")
    void appointment_constructorSetsFields() {
        User user = new User("1", "Sara", "sara@test.com");
        TimeSlot slot = new TimeSlot(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
        );

        Appointment appt = new Appointment(
                "A1", user, slot,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );

        assertEquals("A1", appt.getId());
        assertEquals(user, appt.getUser());
        assertEquals(slot, appt.getTimeSlot());
        assertEquals(AppointmentStatus.AVAILABLE, appt.getStatus());
        assertEquals(AppointmentType.INDIVIDUAL, appt.getType());
        assertEquals(1, appt.getParticipants());
    }

    @Test
    @DisplayName("Appointment setStatus updates status correctly")
    void appointment_setStatusUpdates() {
        Appointment appt = new Appointment(
                "A2", null, null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL, 1
        );

        appt.setStatus(AppointmentStatus.CONFIRMED);
        assertEquals(AppointmentStatus.CONFIRMED, appt.getStatus());

        appt.setStatus(AppointmentStatus.CANCELLED);
        assertEquals(AppointmentStatus.CANCELLED, appt.getStatus());
    }

    @Test
    @DisplayName("Appointment setType updates type correctly")
    void appointment_setTypeUpdates() {
        Appointment appt = new Appointment(
                "A3", null, null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL, 1
        );

        appt.setType(AppointmentType.VIRTUAL);
        assertEquals(AppointmentType.VIRTUAL, appt.getType());
    }

    @Test
    @DisplayName("Appointment setParticipants updates count correctly")
    void appointment_setParticipantsUpdates() {
        Appointment appt = new Appointment(
                "A4", null, null,
                AppointmentStatus.AVAILABLE,
                AppointmentType.GROUP, 2
        );

        appt.setParticipants(4);
        assertEquals(4, appt.getParticipants());
    }

    @Test
    @DisplayName("Appointment setTimeSlot updates time slot correctly")
    void appointment_setTimeSlotUpdates() {
        TimeSlot original = new TimeSlot(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
        );
        TimeSlot updated = new TimeSlot(
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1)
        );

        Appointment appt = new Appointment(
                "A5", null, original,
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL, 1
        );

        appt.setTimeSlot(updated);
        assertEquals(updated, appt.getTimeSlot());
    }

    // ─── AppointmentStatus enum ───────────────────────────────────────────────

    @Test
    @DisplayName("AppointmentStatus enum contains expected values")
    void appointmentStatus_enumValues() {
        assertNotNull(AppointmentStatus.AVAILABLE);
        assertNotNull(AppointmentStatus.CONFIRMED);
        assertNotNull(AppointmentStatus.CANCELLED);
        assertEquals(3, AppointmentStatus.values().length);
    }

    // ─── AppointmentType enum ─────────────────────────────────────────────────

    @Test
    @DisplayName("AppointmentType enum contains all expected types")
    void appointmentType_enumValues() {
        assertEquals(7, AppointmentType.values().length);
        assertNotNull(AppointmentType.INDIVIDUAL);
        assertNotNull(AppointmentType.GROUP);
        assertNotNull(AppointmentType.URGENT);
        assertNotNull(AppointmentType.FOLLOW_UP);
        assertNotNull(AppointmentType.ASSESSMENT);
        assertNotNull(AppointmentType.VIRTUAL);
        assertNotNull(AppointmentType.IN_PERSON);
    }
}