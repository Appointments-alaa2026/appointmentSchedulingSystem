package com.appointment.scheduler.strategy;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentType;

/**
 * Rule that validates appointments based on their type.
 */
public class TypeRule implements BookingRule {

    @Override
    public boolean isValid(Appointment appointment) {
        AppointmentType type = appointment.getType();

        if (type == AppointmentType.GROUP) {
            return appointment.getParticipants() > 1;
        }

        if (type == AppointmentType.INDIVIDUAL) {
            return appointment.getParticipants() == 1;
        }

        return true;
    }
}