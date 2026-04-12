package com.appointment.scheduler.strategy;

import com.appointment.scheduler.model.Appointment;

public interface BookingRule {
    boolean isValid(Appointment appointment);
}