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
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1)
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
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1)
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
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(3)
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
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
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
        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
        assertNull(service.getLastErrorMessage());
    }

    @Test
    void testCancelAppointmentFailIfNotConfirmed() {
        AppointmentService service = new AppointmentService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
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

    @Test
    void testModifyFutureAppointmentSuccess() {
        AppointmentService service = new AppointmentService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot oldSlot = new TimeSlot(
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(4).plusHours(1)
        );

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(1)
        );

        Appointment appointment = new Appointment(
                "10",
                user,
                oldSlot,
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.modifyAppointment("10", newSlot);

        assertTrue(result);
        assertEquals(newSlot, appointment.getTimeSlot());
        assertNull(service.getLastErrorMessage());
    }

    @Test
    void testModifyPastAppointmentFails() {
        AppointmentService service = new AppointmentService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot oldSlot = new TimeSlot(
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(2).plusHours(1)
        );

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
        );

        Appointment appointment = new Appointment(
                "11",
                user,
                oldSlot,
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.modifyAppointment("11", newSlot);

        assertFalse(result);
        assertEquals("Only future appointments can be modified.", service.getLastErrorMessage());
    }

    @Test
    void testAdminCancelAppointmentSuccess() {
        AppointmentService service = new AppointmentService();

        User admin = new User("99", "admin", "admin@test.com");
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1)
        );

        Appointment appointment = new Appointment(
                "20",
                user,
                slot,
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.adminCancelAppointment(admin, "20");

        assertTrue(result);
        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
        assertNull(service.getLastErrorMessage());
    }

    @Test
    void testAdminCancelAppointmentFailsForNonAdmin() {
        AppointmentService service = new AppointmentService();

        User notAdmin = new User("98", "Ali", "ali@test.com");
        User user = new User("1", "Sara", "sara@test.com");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1)
        );

        Appointment appointment = new Appointment(
                "21",
                user,
                slot,
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.adminCancelAppointment(notAdmin, "21");

        assertFalse(result);
        assertEquals("Only administrators can perform this action.", service.getLastErrorMessage());
    }
}