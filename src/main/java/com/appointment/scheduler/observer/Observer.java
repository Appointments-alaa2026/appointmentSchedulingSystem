package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;

public interface Observer {
    void notify(User user, String message);
}