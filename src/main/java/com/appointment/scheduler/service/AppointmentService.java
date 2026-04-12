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

    // ✅ NEW: store last error
    private String lastErrorMessage;

    public AppointmentService() {
        this.notificationManager = new NotificationManager();
    }

    public AppointmentService(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void addRule(BookingRule rule) {
        rules.add(rule);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

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
     * Returns last error message.
     */
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    /**
     * Books an appointment if all rules pass.
     */
    public boolean bookAppointment(String appointmentId) {

        // reset error
        lastErrorMessage = null;

        for (Appointment appointment : appointments) {

            if (appointment.getId().equals(appointmentId)) {

                if (appointment.getStatus() != AppointmentStatus.AVAILABLE) {
                    lastErrorMessage = "Appointment is not available.";
                    return false;
                }

                for (BookingRule rule : rules) {
                    if (!rule.isValid(appointment)) {
                        lastErrorMessage = "Booking failed due to rule violation.";
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

        lastErrorMessage = "Appointment not found.";
        return false;
    }

    public boolean cancelAppointment(String appointmentId) {

        lastErrorMessage = null;

        for (Appointment appointment : appointments) {

            if (appointment.getId().equals(appointmentId)) {

                if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {

                    appointment.setStatus(AppointmentStatus.CANCELLED);

                    notificationManager.notifyAllObservers(
                            appointment.getUser(),
                            "Your appointment has been cancelled."
                    );

                    return true;
                } else {
                    lastErrorMessage = "Only confirmed appointments can be cancelled.";
                    return false;
                }
            }
        }

        lastErrorMessage = "Appointment not found.";
        return false;
    }

    public boolean sendReminder(String appointmentId) {

        lastErrorMessage = null;

        for (Appointment appointment : appointments) {

            if (appointment.getId().equals(appointmentId)) {

                notificationManager.notifyAllObservers(
                        appointment.getUser(),
                        "Reminder: you have an upcoming appointment."
                );

                return true;
            }
        }

        lastErrorMessage = "Appointment not found.";
        return false;
    }
}