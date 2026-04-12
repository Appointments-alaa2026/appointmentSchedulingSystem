package com.appointment.scheduler.model;

/**
<<<<<<< HEAD
 * Represents an appointment in the scheduling system.
=======
 * Represents an appointment in the system.
>>>>>>> 5ae5ac0 (Sprint 5 done)
 */
public class Appointment {

    private String id;
    private User user;
    private TimeSlot timeSlot;
    private AppointmentStatus status;
    private AppointmentType type;
    private int participants;

    /**
<<<<<<< HEAD
     * Constructs a new Appointment object.
     *
     * @param id the appointment ID
=======
     * Creates a new appointment.
     *
     * @param id the appointment id
>>>>>>> 5ae5ac0 (Sprint 5 done)
     * @param user the user who owns the appointment
     * @param timeSlot the appointment time slot
     * @param status the appointment status
     * @param type the appointment type
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

<<<<<<< HEAD
    /**
     * Returns the appointment ID.
     *
     * @return the appointment ID
     */
    public String getId() {
        return id;
    }
=======
    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public AppointmentType getType() {
        return type;
    }

    public int getParticipants() {
        return participants;
    }
>>>>>>> 5ae5ac0 (Sprint 5 done)

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

<<<<<<< HEAD
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
=======
>>>>>>> 5ae5ac0 (Sprint 5 done)
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}