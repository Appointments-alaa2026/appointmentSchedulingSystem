package com.appointment.scheduler.service;

import com.appointment.scheduler.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing appointments.
 */
public class AppointmentService {

    private List<Appointment> appointments = new ArrayList<>();

    /**
     * Adds a new appointment to the system.
     */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Returns only available appointment slots.
     */
    public List<Appointment> getAvailableAppointments(List<Appointment> appointments) {
        List<Appointment> available = new ArrayList<>();

        for (Appointment appt : appointments) {
            if (appt.getStatus() == AppointmentStatus.AVAILABLE) {
                available.add(appt);
            }
        }

        return available;
    }

    /**
     * Books an appointment if it is available.
     *
     * @param appointmentId the ID of the appointment
     * @param user the user booking the appointment
     * @return true if booking is successful, false otherwise
     */
    public boolean bookAppointment(String appointmentId, User user) {

        for (Appointment appt : appointments) {
            if (appt.getId().equals(appointmentId)) {

                if (appt.getStatus() == AppointmentStatus.AVAILABLE) {
                    appt.setStatus(AppointmentStatus.CONFIRMED);
                    return true;
                }

                return false;
            }
        }

        return false;
    }
}