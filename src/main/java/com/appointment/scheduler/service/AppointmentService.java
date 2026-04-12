package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.strategy.BookingRule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing appointments.
 */
public class AppointmentService {

    private final List<Appointment> appointments = new ArrayList<>();
    private final List<BookingRule> rules = new ArrayList<>();
    private final NotificationManager notificationManager;
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

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public boolean bookAppointment(String appointmentId) {
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
                        buildBookingConfirmationMessage(appointment)
                );

                return true;
            }
        }

        lastErrorMessage = "Appointment not found.";
        return false;
    }

    public boolean cancelAppointment(String appointmentId) {
        lastErrorMessage = null;

        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            lastErrorMessage = "Appointment not found.";
            return false;
        }

        if (!isFutureAppointment(appointment)) {
            lastErrorMessage = "Only future appointments can be cancelled.";
            return false;
        }

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            lastErrorMessage = "Only confirmed appointments can be cancelled.";
            return false;
        }

        appointment.setStatus(AppointmentStatus.AVAILABLE);

        notificationManager.notifyAllObservers(
                appointment.getUser(),
                buildCancellationMessage(appointment)
        );

        return true;
    }

    public boolean modifyAppointment(String appointmentId, TimeSlot newTimeSlot) {
        lastErrorMessage = null;

        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            lastErrorMessage = "Appointment not found.";
            return false;
        }

        if (!isFutureAppointment(appointment)) {
            lastErrorMessage = "Only future appointments can be modified.";
            return false;
        }

        appointment.setTimeSlot(newTimeSlot);

        notificationManager.notifyAllObservers(
                appointment.getUser(),
                buildModificationMessage(appointment)
        );

        return true;
    }

    public boolean adminCancelAppointment(User admin, String appointmentId) {
        lastErrorMessage = null;

        if (!isAdmin(admin)) {
            lastErrorMessage = "Only administrators can perform this action.";
            return false;
        }

        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            lastErrorMessage = "Appointment not found.";
            return false;
        }

        appointment.setStatus(AppointmentStatus.AVAILABLE);

        notificationManager.notifyAllObservers(
                appointment.getUser(),
                "Your appointment has been cancelled by the administrator."
        );

        return true;
    }

    public boolean adminModifyAppointment(User admin, String appointmentId, TimeSlot newTimeSlot) {
        lastErrorMessage = null;

        if (!isAdmin(admin)) {
            lastErrorMessage = "Only administrators can perform this action.";
            return false;
        }

        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            lastErrorMessage = "Appointment not found.";
            return false;
        }

        appointment.setTimeSlot(newTimeSlot);

        notificationManager.notifyAllObservers(
                appointment.getUser(),
                buildAdminModificationMessage(appointment)
        );

        return true;
    }

    public boolean sendReminder(String appointmentId) {
        lastErrorMessage = null;

        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            lastErrorMessage = "Appointment not found.";
            return false;
        }

        notificationManager.notifyAllObservers(
                appointment.getUser(),
                buildReminderMessage(appointment)
        );

        return true;
    }

    public boolean adminSendCustomMessage(User admin, String appointmentId, String customMessage) {
        lastErrorMessage = null;

        if (!isAdmin(admin)) {
            lastErrorMessage = "Only administrators can perform this action.";
            return false;
        }

        if (customMessage == null || customMessage.trim().isEmpty()) {
            lastErrorMessage = "Message cannot be empty.";
            return false;
        }

        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            lastErrorMessage = "Appointment not found.";
            return false;
        }

        notificationManager.notifyAllObservers(
                appointment.getUser(),
                customMessage
        );

        return true;
    }

    private Appointment findAppointmentById(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    private boolean isFutureAppointment(Appointment appointment) {
        if (appointment.getTimeSlot() == null || appointment.getTimeSlot().getStartTime() == null) {
            return false;
        }
        return appointment.getTimeSlot().getStartTime().isAfter(LocalDateTime.now());
    }

    private boolean isAdmin(User user) {
        return user != null
                && user.getName() != null
                && user.getName().equalsIgnoreCase("admin");
    }

    private String buildBookingConfirmationMessage(Appointment appointment) {
        return "Dear " + appointment.getUser().getName()
                + ", your appointment has been confirmed for "
                + appointment.getTimeSlot().getStartTime() + ".";
    }

    private String buildCancellationMessage(Appointment appointment) {
        return "Dear " + appointment.getUser().getName()
                + ", your appointment scheduled for "
                + appointment.getTimeSlot().getStartTime()
                + " has been cancelled.";
    }

    private String buildModificationMessage(Appointment appointment) {
        return "Dear " + appointment.getUser().getName()
                + ", your appointment has been modified. New time: "
                + appointment.getTimeSlot().getStartTime() + ".";
    }

    private String buildAdminModificationMessage(Appointment appointment) {
        return "Dear " + appointment.getUser().getName()
                + ", your appointment has been modified by the administrator. New time: "
                + appointment.getTimeSlot().getStartTime() + ".";
    }

    private String buildReminderMessage(Appointment appointment) {
        return "Reminder: Dear " + appointment.getUser().getName()
                + ", you have an upcoming appointment at "
                + appointment.getTimeSlot().getStartTime() + ".";
    }
}