package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.observer.Observer;
import com.appointment.scheduler.strategy.DurationRule;
import com.appointment.scheduler.strategy.ParticipantLimitRule;
import com.appointment.scheduler.strategy.TypeRule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Extra tests to improve coverage for AppointmentService.
 * These tests cover failure paths and admin operations.
 */
public class AppointmentServiceCoverageBoostTest {

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
        service.addRule(new TypeRule());
    }

    private Appointment futureConfirmedAppointment(String id, User user) {
        return new Appointment(
                id,
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(2).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );
    }

    private Appointment futureAvailableAppointment(String id, User user) {
        return new Appointment(
                id,
                user,
                new TimeSlot(
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(2).plusHours(1)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );
    }

    @Test
    void bookAppointmentShouldFailWhenAppointmentNotFound() {
        boolean result = service.bookAppointment("UNKNOWN");

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void cancelAppointmentShouldFailWhenAppointmentNotFound() {
        boolean result = service.cancelAppointment("UNKNOWN");

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void cancelAppointmentShouldFailForPastAppointment() {
        User user = new User("U1", "Ali", "ali@test.com");

        Appointment appointment = new Appointment(
                "A1",
                user,
                new TimeSlot(
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(2).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        boolean result = service.cancelAppointment("A1");

        assertFalse(result);
        assertEquals("Only future appointments can be cancelled.", service.getLastErrorMessage());
    }

    @Test
    void cancelAppointmentShouldFailIfAppointmentIsNotConfirmed() {
        User user = new User("U2", "Sara", "sara@test.com");
        Appointment appointment = futureAvailableAppointment("A2", user);

        service.addAppointment(appointment);

        boolean result = service.cancelAppointment("A2");

        assertFalse(result);
        assertEquals("Only confirmed appointments can be cancelled.", service.getLastErrorMessage());
    }

    @Test
    void modifyAppointmentShouldFailWhenAppointmentNotFound() {
        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
        );

        boolean result = service.modifyAppointment("UNKNOWN", newSlot);

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void modifyAppointmentShouldFailForPastAppointment() {
        User user = new User("U3", "Mona", "mona@test.com");

        Appointment appointment = new Appointment(
                "A3",
                user,
                new TimeSlot(
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().minusDays(1).plusHours(1)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(4).plusHours(1)
        );

        boolean result = service.modifyAppointment("A3", newSlot);

        assertFalse(result);
        assertEquals("Only future appointments can be modified.", service.getLastErrorMessage());
    }

    @Test
    void modifyAppointmentShouldChangeTimeSlotAndNotifyUser() {
        User user = new User("U4", "Lina", "lina@test.com");
        Appointment appointment = futureConfirmedAppointment("A4", user);

        service.addAppointment(appointment);

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(1)
        );

        boolean result = service.modifyAppointment("A4", newSlot);

        assertTrue(result);
        assertEquals(newSlot, appointment.getTimeSlot());
        verify(mockObserver).notify(eq(user), contains("modified"));
    }

    @Test
    void adminCancelAppointmentShouldFailWhenAppointmentNotFound() {
        User admin = new User("ADMIN", "admin", "admin@test.com");

        boolean result = service.adminCancelAppointment(admin, "UNKNOWN");

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void adminModifyAppointmentShouldFailForNonAdmin() {
        User normalUser = new User("U5", "normal", "normal@test.com");

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(4).plusHours(1)
        );

        boolean result = service.adminModifyAppointment(normalUser, "A5", newSlot);

        assertFalse(result);
        assertEquals("Only administrators can perform this action.", service.getLastErrorMessage());
    }

    @Test
    void adminModifyAppointmentShouldFailWhenAppointmentNotFound() {
        User admin = new User("ADMIN", "admin", "admin@test.com");

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(4).plusHours(1)
        );

        boolean result = service.adminModifyAppointment(admin, "UNKNOWN", newSlot);

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void adminModifyAppointmentShouldChangeTimeSlotAndNotifyUser() {
        User user = new User("U6", "Noor", "noor@test.com");
        User admin = new User("ADMIN", "admin", "admin@test.com");

        Appointment appointment = futureConfirmedAppointment("A6", user);
        service.addAppointment(appointment);

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(6),
                LocalDateTime.now().plusDays(6).plusHours(1)
        );

        boolean result = service.adminModifyAppointment(admin, "A6", newSlot);

        assertTrue(result);
        assertEquals(newSlot, appointment.getTimeSlot());
        verify(mockObserver).notify(eq(user), contains("administrator"));
    }

    @Test
    void sendReminderShouldFailWhenAppointmentNotFound() {
        boolean result = service.sendReminder("UNKNOWN");

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void adminSendCustomMessageShouldFailForNonAdmin() {
        User normalUser = new User("U7", "normal", "normal@test.com");

        boolean result = service.adminSendCustomMessage(normalUser, "A7", "Hello");

        assertFalse(result);
        assertEquals("Only administrators can perform this action.", service.getLastErrorMessage());
    }

    @Test
    void adminSendCustomMessageShouldFailForEmptyMessage() {
        User admin = new User("ADMIN", "admin", "admin@test.com");

        boolean result = service.adminSendCustomMessage(admin, "A8", "   ");

        assertFalse(result);
        assertEquals("Message cannot be empty.", service.getLastErrorMessage());
    }

    @Test
    void adminSendCustomMessageShouldFailWhenAppointmentNotFound() {
        User admin = new User("ADMIN", "admin", "admin@test.com");

        boolean result = service.adminSendCustomMessage(admin, "UNKNOWN", "Important message");

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void adminSendCustomMessageShouldNotifyUser() {
        User user = new User("U8", "Rami", "rami@test.com");
        User admin = new User("ADMIN", "admin", "admin@test.com");

        Appointment appointment = futureConfirmedAppointment("A8", user);
        service.addAppointment(appointment);

        boolean result = service.adminSendCustomMessage(admin, "A8", "Please attend on time.");

        assertTrue(result);
        verify(mockObserver).notify(eq(user), contains("Please attend on time."));
    }
}