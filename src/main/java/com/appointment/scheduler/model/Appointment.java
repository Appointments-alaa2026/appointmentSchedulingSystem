package com.appointment.scheduler.model;

/**
 * Represents an appointment in the scheduling system.
 *
 * This class holds all the essential data related to an appointment,
 * including the user, time slot, status, type, and number of participants.
 *
 * It is part of the domain layer and is used by the service layer
 * to manage booking, cancellation, and modification operations.
 *
 * @author Fatima
 * @version 1.0
 */
public class Appointment {

    // Unique identifier for the appointment
    private String id;

    // The user who owns the appointment
    private User user;

    // The scheduled time slot for the appointment
    private TimeSlot timeSlot;

    // Current status of the appointment (AVAILABLE, CONFIRMED, etc.)
    private AppointmentStatus status;

    // Type of the appointment (INDIVIDUAL, GROUP, VIRTUAL, etc.)
    private AppointmentType type;

    // Number of participants in the appointment
    private int participants;

    /**
     * Constructs a new Appointment object.
     *
     * @param id unique identifier of the appointment
     * @param user the user who owns the appointment
     * @param timeSlot the scheduled time slot
     * @param status current status of the appointment
     * @param type type of the appointment
     * @param participants number of participants
     */
    public Appointment(String id, User user, TimeSlot timeSlot,
                       AppointmentStatus status, AppointmentType type, int participants) {
        this.id = id;
        this.user = user;
        this.timeSlot = timeSlot;
        this.status = status;
        this.type = type;
        this.participants = participants;
    }

    /**
     * Returns the appointment ID.
     *
     * @return the appointment ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the appointment user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the appointment time slot.
     *
     * @return the time slot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Returns the appointment status.
     *
     * @return the status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Returns the appointment type.
     *
     * @return the appointment type
     */
    public AppointmentType getType() {
        return type;
    }

    /**
     * Returns the number of participants.
     *
     * @return the number of participants
     */
    public int getParticipants() {
        return participants;
    }

    /**
     * Updates the appointment status.
     *
     * @param status the new status
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Updates the appointment type.
     *
     * @param type the new appointment type
     */
    public void setType(AppointmentType type) {
        this.type = type;
    }

    /**
     * Updates the number of participants.
     *
     * @param participants the new number of participants
     */
    public void setParticipants(int participants) {
        this.participants = participants;
    }

    /**
     * Updates the appointment time slot.
     *
     * @param timeSlot the new time slot
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}