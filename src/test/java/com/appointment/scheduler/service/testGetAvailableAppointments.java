package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    @Test
    void testGetAvailableAppointments() {

        User user = new User("1", "Alaa", "email@test.com");

        TimeSlot slot1 = new TimeSlot("10:00");
        TimeSlot slot2 = new TimeSlot("11:00");

        Appointment a1 = new Appointment("1", user, slot1,
                AppointmentStatus.AVAILABLE, AppointmentType.ONLINE, 1);

        Appointment a2 = new Appointment("2", user, slot2,
                AppointmentStatus.BOOKED, AppointmentType.ONLINE, 1);

        AppointmentService service = new AppointmentService();

        List<Appointment> result = service.getAvailableAppointments(List.of(a1, a2));

        assertEquals(1, result.size());
        assertEquals(AppointmentStatus.AVAILABLE, result.get(0).getStatus());
    }
}