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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for Phase 2 rescheduling and retrieval methods.
 */
public class AppointmentServiceRescheduleTest {

    private AppointmentService service;
    private Observer mockObserver;

    @BeforeEach
    void setUp() {
        mockObserver = mock(Observer.class);

        NotificationManager manager = new NotificationManager();
        manager.addObserver(mockObserver);

        service = new AppointmentService(manager);
        service.addRule(new DurationRule());
        service.addRule(new ParticipantLimitRule());
        service.addRule(new AppointmentTypeRule());
    }

    private Appointment confirmedAppointment(String id, User user) {
        LocalDateTime start = LocalDateTime.now().plusDays(2).withNano(0);
        LocalDateTime end = start.plusHours(1);

        return new Appointment(
                id,
                user,
                new TimeSlot(start, end),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );
    }

    private Appointment availableAppointment(String id) {
        LocalDateTime start = LocalDateTime.now().plusDays(3).withNano(0);
        LocalDateTime end = start.plusHours(1);

        return new Appointment(
                id,
                new User("0", "Available", "available@test.com"),
                new TimeSlot(start, end),
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );
    }

    @Test
    void getAllAppointmentsShouldReturnAllAppointments() {
        User user = new User("U1", "Ali", "ali@test.com");

        Appointment appointment1 = confirmedAppointment("A1", user);
        Appointment appointment2 = availableAppointment("A2");

        service.addAppointment(appointment1);
        service.addAppointment(appointment2);

        List<Appointment> result = service.getAllAppointments();

        assertEquals(2, result.size());
        assertTrue(result.contains(appointment1));
        assertTrue(result.contains(appointment2));
    }

    @Test
    void getAppointmentsByUserEmailShouldReturnOnlyConfirmedAppointmentsForUser() {
        User ali = new User("U1", "Ali", "ali@test.com");
        User sara = new User("U2", "Sara", "sara@test.com");

        Appointment aliAppointment = confirmedAppointment("A1", ali);
        Appointment saraAppointment = confirmedAppointment("A2", sara);
        Appointment availableAppointment = availableAppointment("A3");

        service.addAppointment(aliAppointment);
        service.addAppointment(saraAppointment);
        service.addAppointment(availableAppointment);

        List<Appointment> result = service.getAppointmentsByUserEmail("ali@test.com");

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getId());
    }

    @Test
    void getAppointmentsByUserEmailShouldReturnEmptyListForBlankEmail() {
        List<Appointment> result = service.getAppointmentsByUserEmail("   ");

        assertTrue(result.isEmpty());
    }

    @Test
    void rescheduleAppointmentByUserShouldMoveAppointmentToAvailableSlot() {
        User user = new User("U1", "Ali", "ali@test.com");

        Appointment oldAppointment = confirmedAppointment("A1", user);
        Appointment newSlot = availableAppointment("A2");

        service.addAppointment(oldAppointment);
        service.addAppointment(newSlot);

        boolean result = service.rescheduleAppointmentByUser(
                "A1",
                "A2",
                "ali@test.com"
        );

        assertTrue(result);

        assertEquals(AppointmentStatus.AVAILABLE, oldAppointment.getStatus());
        assertEquals(AppointmentStatus.CONFIRMED, newSlot.getStatus());
        assertEquals("Ali", newSlot.getUser().getName());
        assertEquals("ali@test.com", newSlot.getUser().getEmail());

        verify(mockObserver).notify(eq(user), contains("moved"));
    }

    @Test
    void rescheduleAppointmentByUserShouldFailWhenOldAppointmentNotFound() {
        Appointment newSlot = availableAppointment("A2");
        service.addAppointment(newSlot);

        boolean result = service.rescheduleAppointmentByUser(
                "UNKNOWN",
                "A2",
                "ali@test.com"
        );

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void rescheduleAppointmentByUserShouldFailWhenNewSlotNotFound() {
        User user = new User("U1", "Ali", "ali@test.com");
        Appointment oldAppointment = confirmedAppointment("A1", user);

        service.addAppointment(oldAppointment);

        boolean result = service.rescheduleAppointmentByUser(
                "A1",
                "UNKNOWN",
                "ali@test.com"
        );

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void rescheduleAppointmentByUserShouldFailForWrongEmail() {
        User user = new User("U1", "Ali", "ali@test.com");

        Appointment oldAppointment = confirmedAppointment("A1", user);
        Appointment newSlot = availableAppointment("A2");

        service.addAppointment(oldAppointment);
        service.addAppointment(newSlot);

        boolean result = service.rescheduleAppointmentByUser(
                "A1",
                "A2",
                "wrong@test.com"
        );

        assertFalse(result);
        assertEquals("This appointment does not belong to this user.", service.getLastErrorMessage());
    }

    @Test
    void rescheduleAppointmentByUserShouldFailWhenNewSlotIsNotAvailable() {
        User user = new User("U1", "Ali", "ali@test.com");
        User otherUser = new User("U2", "Sara", "sara@test.com");

        Appointment oldAppointment = confirmedAppointment("A1", user);
        Appointment occupiedSlot = confirmedAppointment("A2", otherUser);

        service.addAppointment(oldAppointment);
        service.addAppointment(occupiedSlot);

        boolean result = service.rescheduleAppointmentByUser(
                "A1",
                "A2",
                "ali@test.com"
        );

        assertFalse(result);
        assertEquals("The new slot is not available.", service.getLastErrorMessage());
    }

    @Test
    void adminRescheduleAppointmentShouldMoveConfirmedAppointment() {
        User user = new User("U1", "Ali", "ali@test.com");
        User admin = new User("ADMIN", "admin", "admin@test.com");

        Appointment oldAppointment = confirmedAppointment("A1", user);
        Appointment newSlot = availableAppointment("A2");

        service.addAppointment(oldAppointment);
        service.addAppointment(newSlot);

        boolean result = service.adminRescheduleAppointment(
                admin,
                "A1",
                "A2"
        );

        assertTrue(result);

        assertEquals(AppointmentStatus.AVAILABLE, oldAppointment.getStatus());
        assertEquals(AppointmentStatus.CONFIRMED, newSlot.getStatus());
        assertEquals("Ali", newSlot.getUser().getName());

        verify(mockObserver).notify(eq(user), contains("administrator"));
    }

    @Test
    void adminRescheduleAppointmentShouldFailForNonAdmin() {
        User normalUser = new User("U1", "Normal", "normal@test.com");

        boolean result = service.adminRescheduleAppointment(
                normalUser,
                "A1",
                "A2"
        );

        assertFalse(result);
        assertEquals("Only administrators can perform this action.", service.getLastErrorMessage());
    }

    @Test
    void adminRescheduleAppointmentShouldFailWhenAppointmentNotFound() {
        User admin = new User("ADMIN", "admin", "admin@test.com");

        boolean result = service.adminRescheduleAppointment(
                admin,
                "UNKNOWN",
                "A2"
        );

        assertFalse(result);
        assertEquals("Appointment not found.", service.getLastErrorMessage());
    }

    @Test
    void adminRescheduleAppointmentShouldFailWhenOldAppointmentIsNotConfirmed() {
        User admin = new User("ADMIN", "admin", "admin@test.com");

        Appointment availableOldAppointment = availableAppointment("A1");
        Appointment newSlot = availableAppointment("A2");

        service.addAppointment(availableOldAppointment);
        service.addAppointment(newSlot);

        boolean result = service.adminRescheduleAppointment(
                admin,
                "A1",
                "A2"
        );

        assertFalse(result);
        assertEquals("Only confirmed appointments can be moved.", service.getLastErrorMessage());
    }

    @Test
    void adminRescheduleAppointmentShouldFailWhenNewSlotIsNotAvailable() {
        User user = new User("U1", "Ali", "ali@test.com");
        User otherUser = new User("U2", "Sara", "sara@test.com");
        User admin = new User("ADMIN", "admin", "admin@test.com");

        Appointment oldAppointment = confirmedAppointment("A1", user);
        Appointment occupiedSlot = confirmedAppointment("A2", otherUser);

        service.addAppointment(oldAppointment);
        service.addAppointment(occupiedSlot);

        boolean result = service.adminRescheduleAppointment(
                admin,
                "A1",
                "A2"
        );

        assertFalse(result);
        assertEquals("The new slot is not available.", service.getLastErrorMessage());
    }
}