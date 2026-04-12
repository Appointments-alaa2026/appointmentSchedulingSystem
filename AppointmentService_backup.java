package com.appointment.scheduler.service;

import com.appointment.scheduler.strategy.*;
import com.appointment.scheduler.model.*;
import com.appointment.scheduler.observer.*;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    private List<Observer> observers = new ArrayList<>();
    private List<BookingRule> rules = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    public AppointmentService() {
        rules.add(new ParticipantLimitRule());
        rules.add(new DurationRule());
        observers.add(new EmailNotification());
        
    }
    public AppointmentService(List<Observer> observers) {
        this.observers = observers;
        rules.add(new ParticipantLimitRule());
        rules.add(new DurationRule());
    }
    
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Appointment> getAvailableAppointments() {
        List<Appointment> available = new ArrayList<>();

        for (Appointment a : appointments) {
            if (a.getStatus() == AppointmentStatus.AVAILABLE) {
                available.add(a);
            }
        }

        return available;
    }

    public boolean bookAppointment(String id, User user) {
        for (Appointment a : appointments) {
            if (a.getId().equals(id) &&
                a.getStatus() == AppointmentStatus.AVAILABLE) {

                for (BookingRule rule : rules) {
                    if (!rule.isValid(a)) {
                        return false;
                    }
                }

                a.setStatus(AppointmentStatus.CONFIRMED);

                for (Observer o : observers) {
                    o.notify(user, "Your photo session is booked successfully!");
                }

                return true;
            }
        }
        return false;
    }
}