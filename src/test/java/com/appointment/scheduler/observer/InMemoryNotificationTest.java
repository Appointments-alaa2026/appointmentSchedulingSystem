package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InMemoryNotification observer.
 *
 * These tests verify that notification messages are stored in memory
 * instead of being sent as real emails.
 *
 * @author Appointment Scheduling System Team
 * @version 2.0
 */
public class InMemoryNotificationTest {

    @Test
    void notifyShouldStoreMessageWithUserEmail() {
        InMemoryNotification notification = new InMemoryNotification();
        User user = new User("U1", "Ali", "ali@test.com");

        notification.notify(user, "Appointment confirmed.");

        List<String> messages = notification.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("ali@test.com"));
        assertTrue(messages.get(0).contains("Appointment confirmed."));
    }

    @Test
    void notifyShouldStoreMessageForUnknownUserWhenUserIsNull() {
        InMemoryNotification notification = new InMemoryNotification();

        notification.notify(null, "Reminder message.");

        List<String> messages = notification.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Unknown user"));
        assertTrue(messages.get(0).contains("Reminder message."));
    }

    @Test
    void notifyShouldStoreMessageForUnknownUserWhenEmailIsNull() {
        InMemoryNotification notification = new InMemoryNotification();
        User user = new User("U2", "Sara", null);

        notification.notify(user, "Appointment moved.");

        List<String> messages = notification.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Unknown user"));
        assertTrue(messages.get(0).contains("Appointment moved."));
    }

    @Test
    void getMessagesShouldReturnCopyNotOriginalList() {
        InMemoryNotification notification = new InMemoryNotification();
        User user = new User("U3", "Mona", "mona@test.com");

        notification.notify(user, "First message.");

        List<String> messages = notification.getMessages();
        messages.add("Fake external message");

        List<String> actualMessages = notification.getMessages();

        assertEquals(1, actualMessages.size());
        assertFalse(actualMessages.contains("Fake external message"));
    }

    @Test
    void notifyShouldStoreMultipleMessages() {
        InMemoryNotification notification = new InMemoryNotification();

        User user1 = new User("U1", "Ali", "ali@test.com");
        User user2 = new User("U2", "Sara", "sara@test.com");

        notification.notify(user1, "Message one.");
        notification.notify(user2, "Message two.");

        List<String> messages = notification.getMessages();

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("ali@test.com"));
        assertTrue(messages.get(1).contains("sara@test.com"));
    }
}