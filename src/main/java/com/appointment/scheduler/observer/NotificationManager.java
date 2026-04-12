package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages observers and dispatches notifications.
 */
public class NotificationManager {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers(User user, String message) {
        for (Observer observer : observers) {
            observer.notify(user, message);
        }
    }
}