package com.appointment.scheduler.service;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.strategy.BookingRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing appointments.
 */
public class AppointmentService {

    private final List<Appointment> appointments = new ArrayList<>();
    private final List<BookingRule> rules = new ArrayList<>();

    /**
     * Adds a rule to the system (Strategy pattern).
     */
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

    public boolean bookAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {

                if (appointment.getStatus() != AppointmentStatus.AVAILABLE) {
                    return false;
                }

                // 🔥 أهم سطر: تطبيق rules
                for (BookingRule rule : rules) {
                    if (!rule.isValid(appointment)) {
                        return false;
                    }
                }

                appointment.setStatus(AppointmentStatus.CONFIRMED);
                return true;
            }
        }
        return false;
    }
}