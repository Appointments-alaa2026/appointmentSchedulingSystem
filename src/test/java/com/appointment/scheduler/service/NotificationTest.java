package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.observer.Observer;
import com.appointment.scheduler.strategy.DurationRule;
import com.appointment.scheduler.strategy.ParticipantLimitRule;
import com.appointment.scheduler.strategy.TypeRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class NotificationTest {

    @Test
    void testNotificationSentAfterBooking() {

        Observer mockObserver = mock(Observer.class);

        NotificationManager notificationManager = new NotificationManager();
        notificationManager.addObserver(mockObserver);

        AppointmentService service = new AppointmentService(notificationManager);

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

        service.bookAppointment("1");

        verify(mockObserver).notify(user, "Your appointment has been booked successfully!");
    }

    @Test
    void testReminderNotificationSent() {
        Observer mockObserver = mock(Observer.class);

        NotificationManager notificationManager = new NotificationManager();
        notificationManager.addObserver(mockObserver);

        AppointmentService service = new AppointmentService(notificationManager);

        User user = new User("2", "Sara", "sara@test.com");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 4, 11, 9, 0),
                LocalDateTime.of(2026, 4, 11, 10, 0)
        );

        Appointment appointment = new Appointment(
                "2",
                user,
                slot,
                AppointmentStatus.CONFIRMED,
                AppointmentType.INDIVIDUAL,
                1
        );

        service.addAppointment(appointment);

        service.sendReminder("2");

        verify(mockObserver).notify(user, "Reminder: you have an upcoming appointment.");
    }
}