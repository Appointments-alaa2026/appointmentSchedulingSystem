package com.appointment.scheduler.observer;

import com.appointment.scheduler.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages notification observers and sends messages to them.
 */
public class NotificationManager {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param observer the observer to add
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers with a message.
     *
     * @param user the user to notify
     * @param message the message content
     */
    public void notifyAllObservers(User user, String message) {
        for (Observer observer : observers) {
            observer.notify(user, message);
        }
    }
}