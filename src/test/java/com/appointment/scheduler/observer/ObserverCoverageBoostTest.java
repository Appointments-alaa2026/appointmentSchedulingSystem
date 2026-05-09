package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;
import com.appointment.scheduler.service.EmailService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Extra tests to improve coverage for observer classes.
 */
public class ObserverCoverageBoostTest {

    @Test
    void notificationManagerShouldNotifyAllObservers() {
        NotificationManager manager = new NotificationManager();

        Observer observer1 = mock(Observer.class);
        Observer observer2 = mock(Observer.class);

        User user = new User("U1", "Ali", "ali@test.com");

        manager.addObserver(observer1);
        manager.addObserver(observer2);

        manager.notifyAllObservers(user, "Test message");

        verify(observer1).notify(user, "Test message");
        verify(observer2).notify(user, "Test message");
    }

    @Test
    void notificationManagerShouldNotNotifyRemovedObserver() {
        NotificationManager manager = new NotificationManager();

        Observer observer = mock(Observer.class);
        User user = new User("U2", "Sara", "sara@test.com");

        manager.addObserver(observer);
        manager.removeObserver(observer);

        manager.notifyAllObservers(user, "Message after remove");

        verify(observer, never()).notify(any(User.class), anyString());
    }

    @Test
    void emailNotificationShouldUseEmailService() {
        EmailService emailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(emailService);

        User user = new User("U3", "Mona", "mona@test.com");

        notification.notify(user, "Hello Mona");

        verify(emailService).sendEmail(
                "mona@test.com",
                "Appointment Notification",
                "Hello Mona"
        );
    }

    @Test
    void emailNotificationShouldRejectNullUser() {
        EmailService emailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(emailService);

        assertThrows(
                IllegalArgumentException.class,
                () -> notification.notify(null, "Message")
        );

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void emailNotificationShouldRejectUserWithNullEmail() {
        EmailService emailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(emailService);

        User user = new User("U4", "Noor", null);

        assertThrows(
                IllegalArgumentException.class,
                () -> notification.notify(user, "Message")
        );

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void emailNotificationShouldRejectUserWithEmptyEmail() {
        EmailService emailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(emailService);

        User user = new User("U5", "Lina", "   ");

        assertThrows(
                IllegalArgumentException.class,
                () -> notification.notify(user, "Message")
        );

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}