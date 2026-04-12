package com.appointment.scheduler.strategy;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentType;

/**
 * A booking rule that validates an appointment based on its type.
 * Different appointment types may require different participant constraints.
 *
 * @author Alaa
 * @version 1.0
 */
public class AppointmentTypeRule implements BookingRule {

    /**
     * Validates an appointment according to its type.
     *
     * @param appointment the appointment to validate
     * @return true if the appointment matches the required rules for its type,
     *         false otherwise
     */
    @Override
    public boolean isValid(Appointment appointment) {
        if (appointment == null || appointment.getType() == null) {
            return false;
        }

        AppointmentType type = appointment.getType();

        switch (type) {
            case INDIVIDUAL:
                return appointment.getParticipants() == 1;

            case GROUP:
                return appointment.getParticipants() > 1 && appointment.getParticipants() <= 5;

            case URGENT:
                return appointment.getTimeSlot() != null;

            case FOLLOW_UP:
                return appointment.getTimeSlot() != null;

            case ASSESSMENT:
                return appointment.getTimeSlot() != null;

            case VIRTUAL:
                return appointment.getTimeSlot() != null;

            case IN_PERSON:
                return appointment.getTimeSlot() != null;

            default:
                return false;
        }
    }
}