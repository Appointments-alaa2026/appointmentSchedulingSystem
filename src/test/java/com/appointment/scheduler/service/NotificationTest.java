package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.observer.Observer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class NotificationTest {

    @Test
    void testNotificationSentAfterBooking() {

        // 🔥 نعمل mock بدل الإيميل الحقيقي
        Observer mockObserver = mock(Observer.class);

        // 🔥 نمرره للـ service
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

        // 🔥 تنفيذ الحجز
        service.bookAppointment("1");

        // 🔥 التأكد أن الإشعار انرسل
        verify(mockObserver).notify(user, "Your photo session is booked successfully!");
    }
}