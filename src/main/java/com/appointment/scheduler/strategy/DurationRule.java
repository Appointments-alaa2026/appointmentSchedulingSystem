package com.appointment.scheduler.strategy;

import com.appointment.scheduler.model.Appointment;
import java.time.Duration;

public class DurationRule implements BookingRule {

    @Override
    public boolean isValid(Appointment appointment) {

        long hours = Duration.between(
                appointment.getTimeSlot().getStart(),
                appointment.getTimeSlot().getEnd()
        ).toHours();

        return hours <= 2;
    }
}