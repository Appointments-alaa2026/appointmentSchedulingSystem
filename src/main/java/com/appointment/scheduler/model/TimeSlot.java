package com.appointment.scheduler.model;

import java.time.LocalDateTime;

/**
 * Represents a time slot with a start and end time.
 */
public class TimeSlot {

    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Constructs a TimeSlot with a start and end time.
     *
     * @param start the start date and time
     * @param end the end date and time
     */
    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the start time of the slot.
     *
     * @return the start time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Gets the end time of the slot.
     *
     * @return the end time
     */
    public LocalDateTime getEnd() {
        return end;
    }
}