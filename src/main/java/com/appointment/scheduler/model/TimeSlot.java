package com.appointment.scheduler.model;

import java.time.LocalDateTime;

/**
 * Represents a time slot for an appointment.
 */
public class TimeSlot {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Constructs a TimeSlot object.
     *
     * @param startTime the start time of the slot
     * @param endTime the end time of the slot
     */
    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the start time of the slot.
     *
     * @return start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the slot.
     *
     * @return end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Updates the start time.
     *
     * @param startTime new start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Updates the end time.
     *
     * @param endTime new end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}