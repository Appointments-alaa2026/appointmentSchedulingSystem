package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetAvailableAppointmentsTest {

    @Test
    void testGetAvailableAppointments() {
        AppointmentService service = new AppointmentService();

        User user = new User("1", "Ali", "ali@test.com");

        Appointment a1 = new Appointment(
                "1",
                user,
                new TimeSlot(
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 11, 0)
                ),
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );

        Appointment a2 = new Appointment(
                "2",
                user,
                new TimeSlot(
                        LocalDateTime.of(2026, 4, 10, 12, 0),
                        LocalDateTime.of(2026, 4, 10, 13, 0)
                ),
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(a1);
        service.addAppointment(a2);

        List<Appointment> result = service.getAvailableAppointments();

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }
}