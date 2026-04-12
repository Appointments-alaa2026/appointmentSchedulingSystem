package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;
import com.appointment.scheduler.service.EmailService;

/**
 * Observer implementation that sends real email notifications.
 */
public class EmailNotification implements Observer {

    private final EmailService emailService;

    public EmailNotification() {
        this.emailService = new EmailService();
    }

    public EmailNotification(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notify(User user, String message) {
        if (user == null || user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email is missing.");
        }

        emailService.sendEmail(
                user.getEmail(),
                "Appointment Notification",
                message
        );
    }
}