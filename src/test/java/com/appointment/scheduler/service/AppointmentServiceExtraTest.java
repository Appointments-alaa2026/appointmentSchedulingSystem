package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.observer.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Additional tests to cover uncovered branches in AppointmentService and EmailService.
 *
 * @author Appointment Scheduling System
 * @version 1.0
 */
public class AppointmentServiceExtraTest {

    private AppointmentService service;
    private Observer mockObserver;
    private User user;
    private TimeSlot futureSlot;
    private TimeSlot pastSlot;

    @BeforeEach
    void setUp() {
        mockObserver = mock(Observer.class);
        NotificationManager manager = new NotificationManager();
        manager.addObserver(mockObserver);
        service = new AppointmentService(manager);

        user = new User("1", "Ali", "ali@test.com");
        futureSlot = new TimeSlot(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
        );
        pastSlot = new TimeSlot(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusHours(1)
        );
    }

    // ─── cancelAppointment — missing branches ────────────────────────────────

    @Test
    @DisplayName("Cancel past confirmed appointment fails")
    void cancel_pastConfirmed_fails() {
        Appointment appt = new Appointment("C1", user, pastSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        assertFalse(service.cancelAppointment("C1"));
        assertEquals("Only future appointments can be cancelled.", service.getLastErrorMessage());
    }

    @Test
    @DisplayName("Cancel sends notification after success")
    void cancel_sendsNotification() {
        Appointment appt = new Appointment("C2", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        service.cancelAppointment("C2");

        verify(mockObserver).notify(eq(user), contains("cancelled"));
    }

    // ─── modifyAppointment — missing branches ────────────────────────────────

    @Test
    @DisplayName("Modify sends notification after success")
    void modify_sendsNotification() {
        Appointment appt = new Appointment("M1", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
        );

        service.modifyAppointment("M1", newSlot);
        verify(mockObserver).notify(eq(user), contains("modified"));
    }

    @Test
    @DisplayName("Modify fails when appointment not found")
    void modify_notFound_fails() {
        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
        );
        assertFalse(service.modifyAppointment("NONE", newSlot));
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    // ─── adminModifyAppointment ──────────────────────────────────────────────

    @Test
    @DisplayName("Admin modify succeeds and sends notification")
    void adminModify_success_sendsNotification() {
        User admin = new User("99", "admin", "admin@test.com");
        Appointment appt = new Appointment("AM1", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
        );

        assertTrue(service.adminModifyAppointment(admin, "AM1", newSlot));
        verify(mockObserver).notify(eq(user), contains("administrator"));
    }

    @Test
    @DisplayName("Admin modify fails when non-admin attempts")
    void adminModify_nonAdmin_fails() {
        User nonAdmin = new User("2", "Bob", "bob@test.com");
        Appointment appt = new Appointment("AM2", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
        );

        assertFalse(service.adminModifyAppointment(nonAdmin, "AM2", newSlot));
        assertEquals("Only administrators can perform this action.", service.getLastErrorMessage());
    }

    @Test
    @DisplayName("Admin modify fails when appointment not found")
    void adminModify_notFound_fails() {
        User admin = new User("99", "admin", "admin@test.com");
        TimeSlot newSlot = new TimeSlot(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1)
        );
        assertFalse(service.adminModifyAppointment(admin, "NONE", newSlot));
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    // ─── adminSendCustomMessage ──────────────────────────────────────────────

    @Test
    @DisplayName("Admin custom message succeeds")
    void adminCustomMessage_success() {
        User admin = new User("99", "admin", "admin@test.com");
        Appointment appt = new Appointment("CM1", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        assertTrue(service.adminSendCustomMessage(admin, "CM1", "Bring your ID."));
        verify(mockObserver).notify(eq(user), eq("Bring your ID."));
    }

    @Test
    @DisplayName("Admin custom message fails for non-admin")
    void adminCustomMessage_nonAdmin_fails() {
        User nonAdmin = new User("2", "Bob", "bob@test.com");
        Appointment appt = new Appointment("CM2", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        assertFalse(service.adminSendCustomMessage(nonAdmin, "CM2", "Hello"));
        assertEquals("Only administrators can perform this action.", service.getLastErrorMessage());
    }

    @Test
    @DisplayName("Admin custom message fails when message is empty")
    void adminCustomMessage_emptyMessage_fails() {
        User admin = new User("99", "admin", "admin@test.com");
        Appointment appt = new Appointment("CM3", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        assertFalse(service.adminSendCustomMessage(admin, "CM3", "   "));
        assertEquals("Message cannot be empty.", service.getLastErrorMessage());
    }

    @Test
    @DisplayName("Admin custom message fails when appointment not found")
    void adminCustomMessage_notFound_fails() {
        User admin = new User("99", "admin", "admin@test.com");
        assertFalse(service.adminSendCustomMessage(admin, "NONE", "Hello"));
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    // ─── sendReminder ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Send reminder succeeds and notifies observer")
    void sendReminder_success() {
        Appointment appt = new Appointment("R1", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        assertTrue(service.sendReminder("R1"));
        verify(mockObserver).notify(eq(user), contains("upcoming appointment"));
    }

    @Test
    @DisplayName("Send reminder fails when not found")
    void sendReminder_notFound_fails() {
        assertFalse(service.sendReminder("NONE"));
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    // ─── getAvailableAppointments ────────────────────────────────────────────

    @Test
    @DisplayName("Returns only AVAILABLE appointments")
    void getAvailable_returnsOnlyAvailable() {
        Appointment a1 = new Appointment("V1", user, futureSlot,
                AppointmentStatus.AVAILABLE, AppointmentType.INDIVIDUAL, 1);
        Appointment a2 = new Appointment("V2", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        Appointment a3 = new Appointment("V3", user, futureSlot,
                AppointmentStatus.CANCELLED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(a1);
        service.addAppointment(a2);
        service.addAppointment(a3);

        assertEquals(1, service.getAvailableAppointments().size());
        assertEquals("V1", service.getAvailableAppointments().get(0).getId());
    }

    @Test
    @DisplayName("Returns empty list when no available appointments")
    void getAvailable_emptyWhenNoneAvailable() {
        Appointment appt = new Appointment("V4", user, futureSlot,
                AppointmentStatus.CONFIRMED, AppointmentType.INDIVIDUAL, 1);
        service.addAppointment(appt);

        assertTrue(service.getAvailableAppointments().isEmpty());
    }

    // ─── EmailService ────────────────────────────────────────────────────────

    @Test
    @DisplayName("EmailService constructor with credentials stores them")
    void emailService_constructorWithCredentials() {
        EmailService emailService = new EmailService("test@gmail.com", "testpass");
        assertNotNull(emailService);
    }

    @Test
    @DisplayName("EmailService throws when credentials are missing")
    void emailService_throwsWhenCredentialsMissing() {
        EmailService emailService = new EmailService("", "");
        assertThrows(IllegalStateException.class, () ->
                emailService.sendEmail("to@test.com", "Subject", "Body")
        );
    }

    @Test
    @DisplayName("EmailService throws when username is null")
    void emailService_throwsWhenUsernameNull() {
        EmailService emailService = new EmailService(null, "pass");
        assertThrows(IllegalStateException.class, () ->
                emailService.sendEmail("to@test.com", "Subject", "Body")
        );
    }

    @Test
    @DisplayName("EmailService throws when password is null")
    void emailService_throwsWhenPasswordNull() {
        EmailService emailService = new EmailService("user@gmail.com", null);
        assertThrows(IllegalStateException.class, () ->
                emailService.sendEmail("to@test.com", "Subject", "Body")
        );
    }
}