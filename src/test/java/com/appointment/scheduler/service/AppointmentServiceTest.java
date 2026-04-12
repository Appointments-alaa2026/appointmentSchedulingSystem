package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import com.appointment.scheduler.strategy.DurationRule;
import com.appointment.scheduler.strategy.ParticipantLimitRule;
import com.appointment.scheduler.strategy.TypeRule;
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
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());
        service.addRule(new TypeRule());

        boolean result = service.bookAppointment("1");

        assertTrue(result);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
        assertNull(service.getLastErrorMessage());
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
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());
        service.addRule(new TypeRule());

        boolean result = service.bookAppointment("1");

        assertFalse(result);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
        assertEquals("Appointment is not available.", service.getLastErrorMessage());
    }

    @Test
    void testBookingFailIfRuleViolated() {
        AppointmentService service = new AppointmentService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 4, 10, 10, 0),
                LocalDateTime.of(2026, 4, 10, 13, 0)
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
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());
        service.addRule(new TypeRule());

        boolean result = service.bookAppointment("1");

        assertFalse(result);
        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
        assertEquals("Booking failed due to rule violation.", service.getLastErrorMessage());
    }

    @Test
    void testCancelAppointmentSuccess() {
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

        boolean result = service.cancelAppointment("1");

        assertTrue(result);
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertNull(service.getLastErrorMessage());
    }

    @Test
    void testCancelAppointmentFailIfNotConfirmed() {
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

        boolean result = service.cancelAppointment("1");

        assertFalse(result);
        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
        assertEquals("Only confirmed appointments can be cancelled.", service.getLastErrorMessage());
    }

    @Test
    void testBookingFailIfAppointmentNotFound() {
        AppointmentService service = new AppointmentService();
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());
        service.addRule(new TypeRule());

        boolean result = service.bookAppointment("999");

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }
}