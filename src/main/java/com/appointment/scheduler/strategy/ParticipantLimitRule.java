package com.appointment.scheduler.strategy;

import com.appointment.scheduler.model.Appointment;

public class ParticipantLimitRule implements BookingRule {

    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getParticipants() <= 5;
    }
}