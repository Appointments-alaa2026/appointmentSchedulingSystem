package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.strategy.BookingRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing appointments.
 */
public class AppointmentService {

    private final List<Appointment> appointments = new ArrayList<>();
    private final List<BookingRule> rules = new ArrayList<>();
    private final NotificationManager notificationManager;

    /**
     * Default constructor.
     * Creates AppointmentService with an empty NotificationManager.
     */
    public AppointmentService() {
        this.notificationManager = new NotificationManager();
    }

    /**
     * Constructor with injected NotificationManager.
     *
     * @param notificationManager the notification manager
     */
    public AppointmentService(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    /**
     * Adds a rule to the system (Strategy pattern).
     *
     * @param rule booking rule
     */
    public void addRule(BookingRule rule) {
        rules.add(rule);
    }

    /**
     * Adds an appointment to the system.
     *
     * @param appointment the appointment to add
     */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Returns all available appointments.
     *
     * @return list of available appointments
     */
    public List<Appointment> getAvailableAppointments() {
        List<Appointment> available = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == AppointmentStatus.AVAILABLE) {
                available.add(appointment);
            }
        }

        return available;
    }

    /**
     * Books an appointment if available and all rules pass.
     *
     * @param appointmentId the appointment id
     * @return true if booking succeeds, false otherwise
     */
    public boolean bookAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {

                if (appointment.getStatus() != AppointmentStatus.AVAILABLE) {
                    return false;
                }

                for (BookingRule rule : rules) {
                    if (!rule.isValid(appointment)) {
                        return false;
                    }
                }

                appointment.setStatus(AppointmentStatus.CONFIRMED);

                notificationManager.notifyAllObservers(
                        appointment.getUser(),
                        "Your appointment has been booked successfully!"
                );

                return true;
            }
        }
        return false;
    }

    /**
     * Cancels an appointment if it exists.
     *
     * @param appointmentId the appointment id
     * @return true if cancelled, false otherwise
     */
    public boolean cancelAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {
                if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
                    appointment.setStatus(AppointmentStatus.CANCELLED);

                    notificationManager.notifyAllObservers(
                            appointment.getUser(),
                            "Your appointment has been cancelled."
                    );

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sends a reminder for a given appointment.
     *
     * @param appointmentId the appointment id
     * @return true if reminder sent, false otherwise
     */
    public boolean sendReminder(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {
                notificationManager.notifyAllObservers(
                        appointment.getUser(),
                        "Reminder: you have an upcoming appointment."
                );
                return true;
            }
        }
        return false;
    }
}