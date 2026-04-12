package com.appointment.scheduler.strategy;

import com.appointment.scheduler.model.Appointment;

import java.time.Duration;

/**
 * Rule that checks if appointment duration does not exceed 2 hours.
 */
public class DurationRule implements BookingRule {

    @Override
    public boolean isValid(Appointment appointment) {
        long minutes = Duration.between(
                appointment.getTimeSlot().getStartTime(),
                appointment.getTimeSlot().getEndTime()
        ).toMinutes();

        return minutes > 0 && minutes <= 120;
    }
}