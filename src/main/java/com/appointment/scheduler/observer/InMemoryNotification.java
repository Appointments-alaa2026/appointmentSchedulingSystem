package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock notification observer that stores messages in memory.
 * This is useful for GUI demo and test mode without sending real emails.
 *
 * @author Appointment Scheduling System Team
 * @version 2.0
 */
public class InMemoryNotification implements Observer {

    /** Stored notification messages. */
    private final List<String> messages = new ArrayList<>();

    /**
     * Stores a notification message in memory.
     *
     * @param user user who receives the message
     * @param message notification message
     */
    @Override
    public void notify(User user, String message) {
        String receiver = "Unknown user";

        if (user != null && user.getEmail() != null) {
            receiver = user.getEmail();
        }

        messages.add("To: " + receiver + " | " + message);
    }

    /**
     * Returns all stored messages.
     *
     * @return notification messages
     */
    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}