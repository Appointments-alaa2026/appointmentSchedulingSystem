package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;

public class EmailNotification implements Observer {

    @Override
    public void notify(User user, String message) {
        System.out.println("Sending email to " + user.getEmail());
        System.out.println("Message: " + message);
    }
}