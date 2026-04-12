package com.appointment.scheduler.app;

import java.time.LocalDateTime;

import com.appointment.scheduler.model.*;
import com.appointment.scheduler.service.*;

public class Main {
    public static void main(String[] args) {

        // 🔐 LOGIN
        Admin admin = new Admin(
                "1", "CEO", "Alaa@setup.com",
                "Alaa", "1234"
        );

        AuthService authService = new AuthService();

        boolean success = authService.login(admin, "Alaa", "1234");

        if (success) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }

        authService.logout();

        // 👤 USER
        User user = new User("1", "Ali", "ali@test.com");

        // ⏰ TIME SLOT
        TimeSlot slot = new TimeSlot(
                LocalDateTime.of(2026, 4, 10, 10, 0),
                LocalDateTime.of(2026, 4, 10, 11, 0)
        );

        // 📸 APPOINTMENT
        Appointment appointment = new Appointment(
                "1",
                user, // ✅ مهم
                slot,
                AppointmentStatus.AVAILABLE,
                AppointmentType.INDIVIDUAL,
                1
        );

        // ⚙️ SERVICE
        AppointmentService service = new AppointmentService();

        service.addAppointment(appointment);

        // 👀 عرض المتاح
        System.out.println("Available appointments:");
        for (Appointment a : service.getAvailableAppointments()) {
            System.out.println(a.getTimeSlot().getStart());
        }

        // 📸 حجز
        boolean booked = service.bookAppointment("1", user);

        System.out.println("Booking success: " + booked);

        // 👀 بعد الحجز
        System.out.println("After booking:");
        for (Appointment a : service.getAvailableAppointments()) {
            System.out.println(a.getTimeSlot().getStart());
        }
    }
}