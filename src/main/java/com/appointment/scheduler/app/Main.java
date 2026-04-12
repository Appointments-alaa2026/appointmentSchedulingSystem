package com.appointment.scheduler.app;

import com.appointment.scheduler.repository.AdminRepository;
import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.repository.ConfigAdminRepository;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import com.appointment.scheduler.observer.EmailNotification;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.service.AppointmentService;
import com.appointment.scheduler.service.AuthService;
import com.appointment.scheduler.strategy.DurationRule;
import com.appointment.scheduler.strategy.ParticipantLimitRule;
import com.appointment.scheduler.strategy.TypeRule;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        AdminRepository adminRepository = new ConfigAdminRepository();
        AuthService authService = new AuthService(adminRepository);

        boolean loggedIn = authService.login("admin", "1234");

        if (loggedIn) {
            System.out.println("Login successful.");

            NotificationManager notificationManager = new NotificationManager();
            notificationManager.addObserver(new EmailNotification());

            AppointmentService appointmentService = new AppointmentService(notificationManager);

            appointmentService.addRule(new DurationRule());
            appointmentService.addRule(new ParticipantLimitRule());
            appointmentService.addRule(new TypeRule());

            User user = new User("1", "Asma", "asma25loay@gmail.com");

            TimeSlot slot = new TimeSlot(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(1)
            );

            Appointment appointment = new Appointment(
                    "A1",
                    user,
                    slot,
                    AppointmentStatus.AVAILABLE,
                    AppointmentType.INDIVIDUAL,
                    1
            );

            appointmentService.addAppointment(appointment);

            boolean booked = appointmentService.bookAppointment("A1");

            if (booked) {
                System.out.println("Appointment booked successfully.");
                System.out.println("Confirmation email sent to: " + user.getEmail());
            } else {
                System.out.println("Booking failed: " + appointmentService.getLastErrorMessage());
            }

        } else {
            System.out.println("Invalid username or password.");
        }
    }
}