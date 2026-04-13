package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;
import com.appointment.scheduler.observer.EmailNotification;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.observer.Observer;
import com.appointment.scheduler.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for NotificationManager and EmailNotification.
 *
 * @author Appointment Scheduling System
 * @version 1.0
 */
public class ObserverTest {

    // ─── NotificationManager ─────────────────────────────────────────────────

    @Test
    @DisplayName("NotificationManager notifies all registered observers")
    void notificationManager_notifiesAllObservers() {
        Observer o1 = mock(Observer.class);
        Observer o2 = mock(Observer.class);
        User user = new User("1", "Ali", "ali@test.com");

        NotificationManager manager = new NotificationManager();
        manager.addObserver(o1);
        manager.addObserver(o2);

        manager.notifyAllObservers(user, "Test message");

        verify(o1).notify(user, "Test message");
        verify(o2).notify(user, "Test message");
    }

    @Test
    @DisplayName("NotificationManager does not notify removed observer")
    void notificationManager_removedObserverNotNotified() {
        Observer observer = mock(Observer.class);
        User user = new User("1", "Ali", "ali@test.com");

        NotificationManager manager = new NotificationManager();
        manager.addObserver(observer);
        manager.removeObserver(observer);

        manager.notifyAllObservers(user, "Test message");

        verify(observer, never()).notify(any(), any());
    }

    @Test
    @DisplayName("NotificationManager with no observers does nothing")
    void notificationManager_noObservers_doesNothing() {
        NotificationManager manager = new NotificationManager();
        // Should not throw any exception
        assertDoesNotThrow(() ->
            manager.notifyAllObservers(new User("1", "Ali", "ali@test.com"), "msg")
        );
    }

    // ─── EmailNotification ───────────────────────────────────────────────────

    @Test
    @DisplayName("EmailNotification calls EmailService with correct arguments")
    void emailNotification_callsEmailService() {
        EmailService mockEmailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(mockEmailService);
        User user = new User("1", "Ali", "ali@test.com");

        notification.notify(user, "Your appointment is confirmed.");

        verify(mockEmailService).sendEmail(
                "ali@test.com",
                "Appointment Notification",
                "Your appointment is confirmed."
        );
    }

    @Test
    @DisplayName("EmailNotification throws exception when user is null")
    void emailNotification_throwsWhenUserIsNull() {
        EmailService mockEmailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(mockEmailService);

        assertThrows(IllegalArgumentException.class, () ->
            notification.notify(null, "msg")
        );
    }

    @Test
    @DisplayName("EmailNotification throws exception when user email is null")
    void emailNotification_throwsWhenEmailIsNull() {
        EmailService mockEmailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(mockEmailService);
        User user = new User("1", "Ali", null);

        assertThrows(IllegalArgumentException.class, () ->
            notification.notify(user, "msg")
        );
    }

    @Test
    @DisplayName("EmailNotification throws exception when user email is empty")
    void emailNotification_throwsWhenEmailIsEmpty() {
        EmailService mockEmailService = mock(EmailService.class);
        EmailNotification notification = new EmailNotification(mockEmailService);
        User user = new User("1", "Ali", "   ");

        assertThrows(IllegalArgumentException.class, () ->
            notification.notify(user, "msg")
        );
    }
}