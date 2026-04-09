package com.appointment.scheduler.model;

public class Appointment {

    private String id;
    private User user;
    private TimeSlot timeSlot;
    private AppointmentStatus status;
    private AppointmentType type;
    private int participants;

    public Appointment(String id, User user, TimeSlot timeSlot,
                       AppointmentStatus status, AppointmentType type, int participants) {
        this.id = id;
        this.user = user;
        this.timeSlot = timeSlot;
        this.status = status;
        this.type = type;
        this.participants = participants;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public AppointmentStatus getStatus() { return status; }
    public AppointmentType getType() { return type; }
    public int getParticipants() { return participants; }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}