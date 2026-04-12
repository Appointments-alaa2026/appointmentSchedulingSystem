package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.observer.Observer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@Disabled("Observer not implemented yet")
public class NotificationTest {

    @Test
    void testNotificationSentAfterBooking() {

        Observer mockObserver = mock(Observer.class);

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

        service.bookAppointment("1");

        verify(mockObserver).notify(user, "Your photo session is booked successfully!");
    }
}