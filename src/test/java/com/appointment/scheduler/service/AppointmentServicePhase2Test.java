package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.observer.Observer;
import com.appointment.scheduler.strategy.AppointmentTypeRule;
import com.appointment.scheduler.strategy.DurationRule;
import com.appointment.scheduler.strategy.ParticipantLimitRule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for Phase 2 appointment service features.
 *
 * These tests focus on service-layer logic, not the Swing GUI.
 *
 * @author Appointment Scheduling System Team
 * @version 2.0
 */
public class AppointmentServicePhase2Test {

    private AppointmentService service;
    private Observer mockObserver;

    @BeforeEach
    void setUp() {
        mockObserver = mock(Observer.class);

        NotificationManager notificationManager = new NotificationManager();
        notificationManager.addObserver(mockObserver);

        service = new AppointmentService(notificationManager);
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());
        service.addRule(new AppointmentTypeRule());
    }

    @Test
    void bookAppointmentWithRealUserShouldConfirmAppointment() {
        User user = new User("U1", "Ali", "ali@test.com");

        Appointment appointment = new Appointment(
                "A1",
                new User("0", "Available", "available@test.com"),
                new TimeSlot(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.bookAppointment(
                "A1",
                user,
                AppointmentType.INDIVIDUAL,
                1
        );

        assertTrue(result);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
        assertEquals("Ali", appointment.getUser().getName());

        verify(mockObserver).notify(eq(user), contains("confirmed"));
    }

    @Test
    void bookAppointmentShouldRejectUnavailableSlot() {
        User user = new User("U1", "Ali", "ali@test.com");

        Appointment appointment = new Appointment(
                "A2",
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.bookAppointment(
                "A2",
                user,
                AppointmentType.INDIVIDUAL,
                1
        );

        assertFalse(result);
        assertEquals("Appointment is not available.", service.getLastErrorMessage());
    }

    @Test
    void bookAppointmentShouldRejectInvalidParticipants() {
        User user = new User("U2", "Sara", "sara@test.com");

        Appointment appointment = new Appointment(
                "A3",
                new User("0", "Available", "available@test.com"),
                new TimeSlot(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.GROUP,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.bookAppointment(
                "A3",
                user,
                AppointmentType.GROUP,
                10
        );

        assertFalse(result);
        assertEquals("Booking failed due to rule violation.", service.getLastErrorMessage());
    }

    @Test
    void cancelAppointmentByUserShouldMakeSlotAvailableAgain() {
        User user = new User("U3", "Lina", "lina@test.com");

        Appointment appointment = new Appointment(
                "A4",
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(2).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.cancelAppointmentByUser("A4", "lina@test.com");

        assertTrue(result);
        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
    }

    @Test
    void cancelAppointmentByWrongUserShouldFail() {
        User user = new User("U4", "Mona", "mona@test.com");

        Appointment appointment = new Appointment(
                "A5",
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(2).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.cancelAppointmentByUser("A5", "wrong@test.com");

        assertFalse(result);
        assertEquals("This appointment does not belong to this user.", service.getLastErrorMessage());
    }

    @Test
    void adminCanCancelAppointment() {
        User user = new User("U5", "Huda", "huda@test.com");
        User admin = new User("ADMIN", "admin", "admin@test.com");

        Appointment appointment = new Appointment(
                "A6",
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.adminCancelAppointment(admin, "A6");

        assertTrue(result);
        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
    }

    @Test
    void nonAdminCannotCancelAppointment() {
        User user = new User("U6", "Noor", "noor@test.com");
        User notAdmin = new User("X1", "normalUser", "normal@test.com");

        Appointment appointment = new Appointment(
                "A7",
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.adminCancelAppointment(notAdmin, "A7");

        assertFalse(result);
        assertEquals("Only administrators can perform this action.", service.getLastErrorMessage());
    }

    @Test
    void sendReminderShouldNotifyObserver() {
        User user = new User("U7", "Rami", "rami@test.com");

        Appointment appointment = new Appointment(
                "A8",
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.sendReminder("A8");

        assertTrue(result);
        verify(mockObserver).notify(eq(user), contains("Reminder"));
    }
}