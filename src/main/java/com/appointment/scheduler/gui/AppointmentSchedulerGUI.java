package com.appointment.scheduler.gui;

import com.appointment.scheduler.model.Appointment;
import com.appointment.scheduler.model.AppointmentStatus;
import com.appointment.scheduler.model.AppointmentType;
import com.appointment.scheduler.model.TimeSlot;
import com.appointment.scheduler.model.User;
import com.appointment.scheduler.observer.InMemoryNotification;
import com.appointment.scheduler.observer.NotificationManager;
import com.appointment.scheduler.repository.ConfigAdminRepository;
import com.appointment.scheduler.service.AppointmentService;
import com.appointment.scheduler.service.AuthService;
import com.appointment.scheduler.strategy.AppointmentTypeRule;
import com.appointment.scheduler.strategy.DurationRule;
import com.appointment.scheduler.strategy.ParticipantLimitRule;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Swing GUI for the Appointment Scheduling System.
 *
 * @author Appointment Scheduling System Team
 * @version 2.0
 */
public class AppointmentSchedulerGUI extends JFrame {

    /** Formatter for date/time display. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** Service for appointment business logic. */
    private final AppointmentService appointmentService;

    /** Service for admin authentication. */
    private final AuthService authService;

    /** Mock notification observer. */
    private final InMemoryNotification notificationObserver;

    /** Main tabs. */
    private final JTabbedPane tabs = new JTabbedPane();

    /** Available appointments table. */
    private final JTable availableTable = new JTable();

    /** User appointments table. */
    private final JTable userTable = new JTable();

    /** Admin appointments table. */
    private final JTable adminTable = new JTable();

    /** Booking name field. */
    private final JTextField nameField = new JTextField();

    /** Booking and search email field. */
    private final JTextField emailField = new JTextField();

    /** Participants field. */
    private final JTextField participantsField = new JTextField("1");

    /** Appointment type box. */
    private final JComboBox<AppointmentType> typeBox = new JComboBox<>(AppointmentType.values());

    /** Admin username field. */
    private final JTextField adminUsernameField = new JTextField();

    /** Admin password field. */
    private final JPasswordField adminPasswordField = new JPasswordField();

    /** Notifications display area. */
    private final JTextArea notificationsArea = new JTextArea(7, 70);

    /**
     * Constructs the GUI.
     */
    public AppointmentSchedulerGUI() {
        NotificationManager notificationManager = new NotificationManager();
        notificationObserver = new InMemoryNotification();
        notificationManager.addObserver(notificationObserver);

        appointmentService = new AppointmentService(notificationManager);
        appointmentService.addRule(new DurationRule());
        appointmentService.addRule(new ParticipantLimitRule());
        appointmentService.addRule(new AppointmentTypeRule());

        authService = new AuthService(new ConfigAdminRepository());

        seedSlots();
        buildGUI();
        refreshTables();
    }

    /**
     * Main method for GUI.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentSchedulerGUI().setVisible(true));
    }

    /**
     * Builds the main GUI frame.
     */
    private void buildGUI() {
        setTitle("Appointment Scheduling System - Phase 2");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabs.addTab("Book Appointment", buildBookingPanel());
        tabs.addTab("My Appointments", buildUserPanel());
        tabs.addTab("Admin Login", buildAdminLoginPanel());
        tabs.addTab("Admin Management", buildAdminPanel());
        tabs.setEnabledAt(3, false);

        notificationsArea.setEditable(false);

        add(tabs, BorderLayout.CENTER);
        add(new JScrollPane(notificationsArea), BorderLayout.SOUTH);
    }

    /**
     * Builds booking panel.
     *
     * @return booking panel
     */
    private JPanel buildBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Type:"));
        form.add(typeBox);
        form.add(new JLabel("Participants:"));
        form.add(participantsField);

        JButton bookButton = new JButton("Book Selected Slot");
        bookButton.addActionListener(e -> bookSelectedSlot());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTables());

        form.add(bookButton);
        form.add(refreshButton);

        configureTable(availableTable);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(availableTable), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Builds user appointment management panel.
     *
     * @return user panel
     */
    private JPanel buildUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton loadButton = new JButton("Load My Appointments");
        loadButton.addActionListener(e -> loadUserAppointments());

        JButton cancelButton = new JButton("Cancel Selected");
        cancelButton.addActionListener(e -> cancelUserAppointment());

        JButton moveButton = new JButton("Move To Available Slot");
        moveButton.addActionListener(e -> moveUserAppointment());

        buttons.add(new JLabel("Enter your email in the booking email field, then:"));
        buttons.add(loadButton);
        buttons.add(cancelButton);
        buttons.add(moveButton);

        configureTable(userTable);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Builds admin login panel.
     *
     * @return admin login panel
     */
    private JPanel buildAdminLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 250, 40, 250));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginAdmin());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logoutAdmin());

        panel.add(new JLabel("Username:"));
        panel.add(adminUsernameField);

        panel.add(new JLabel("Password:"));
        panel.add(adminPasswordField);

        panel.add(loginButton);
        panel.add(logoutButton);

        panel.add(new JLabel("Default:"));
        panel.add(new JLabel("Alaa / 1234"));

        return panel;
    }

    /**
     * Builds admin management panel.
     *
     * @return admin panel
     */
    private JPanel buildAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTables());

        JButton cancelButton = new JButton("Cancel Selected");
        cancelButton.addActionListener(e -> adminCancelAppointment());

        JButton moveButton = new JButton("Move Selected");
        moveButton.addActionListener(e -> adminMoveAppointment());

        JButton reminderButton = new JButton("Send Reminder");
        reminderButton.addActionListener(e -> sendReminder());

        buttons.add(refreshButton);
        buttons.add(cancelButton);
        buttons.add(moveButton);
        buttons.add(reminderButton);

        configureTable(adminTable);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(adminTable), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Books the selected available slot.
     */
    private void bookSelectedSlot() {
        String appointmentId = selectedId(availableTable);

        if (appointmentId == null) {
            showError("Please select an available slot.");
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        int participants;

        try {
            participants = Integer.parseInt(participantsField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Participants must be a number.");
            return;
        }

        User user = new User("U" + System.currentTimeMillis(), name, email);
        AppointmentType type = (AppointmentType) typeBox.getSelectedItem();

        boolean success = appointmentService.bookAppointment(appointmentId, user, type, participants);

        if (success) {
            showInfo("Appointment booked successfully.");
        } else {
            showError(appointmentService.getLastErrorMessage());
        }

        refreshTables();
    }

    /**
     * Loads appointments for the email written in the email field.
     */
    private void loadUserAppointments() {
        String email = emailField.getText().trim();
        List<Appointment> appointments = appointmentService.getAppointmentsByUserEmail(email);
        fillTable(userTable, appointments);
    }

    /**
     * Cancels the selected user appointment.
     */
    private void cancelUserAppointment() {
        String appointmentId = selectedId(userTable);

        if (appointmentId == null) {
            showError("Please select one of your appointments.");
            return;
        }

        boolean success = appointmentService.cancelAppointmentByUser(
                appointmentId,
                emailField.getText().trim()
        );

        if (success) {
            showInfo("Appointment cancelled successfully.");
        } else {
            showError(appointmentService.getLastErrorMessage());
        }

        refreshTables();
        loadUserAppointments();
    }

    /**
     * Moves a user appointment to another available slot.
     */
    private void moveUserAppointment() {
        String oldId = selectedId(userTable);

        if (oldId == null) {
            showError("Please select your confirmed appointment first.");
            return;
        }

        String newId = askForAvailableSlotId();

        if (newId == null) {
            return;
        }

        boolean success = appointmentService.rescheduleAppointmentByUser(
                oldId,
                newId,
                emailField.getText().trim()
        );

        if (success) {
            showInfo("Appointment moved successfully.");
        } else {
            showError(appointmentService.getLastErrorMessage());
        }

        refreshTables();
        loadUserAppointments();
    }

    /**
     * Logs in the administrator.
     */
    private void loginAdmin() {
        String username = adminUsernameField.getText().trim();
        String password = new String(adminPasswordField.getPassword());

        boolean success = authService.login(username, password);

        if (success) {
            tabs.setEnabledAt(3, true);
            showInfo("Admin login successful.");
            refreshTables();
        } else {
            showError("Invalid username or password.");
        }
    }

    /**
     * Logs out the administrator.
     */
    private void logoutAdmin() {
        authService.logout();
        tabs.setEnabledAt(3, false);
        showInfo("Admin logged out.");
    }

    /**
     * Admin cancels selected appointment.
     */
    private void adminCancelAppointment() {
        if (!authService.isLoggedIn()) {
            showError("Admin must login first.");
            return;
        }

        String appointmentId = selectedId(adminTable);

        if (appointmentId == null) {
            showError("Please select an appointment.");
            return;
        }

        boolean success = appointmentService.adminCancelAppointment(adminUser(), appointmentId);

        if (success) {
            showInfo("Appointment cancelled by admin.");
        } else {
            showError(appointmentService.getLastErrorMessage());
        }

        refreshTables();
    }

    /**
     * Admin moves selected appointment.
     */
    private void adminMoveAppointment() {
        if (!authService.isLoggedIn()) {
            showError("Admin must login first.");
            return;
        }

        String oldId = selectedId(adminTable);

        if (oldId == null) {
            showError("Please select a confirmed appointment.");
            return;
        }

        String newId = askForAvailableSlotId();

        if (newId == null) {
            return;
        }

        boolean success = appointmentService.adminRescheduleAppointment(adminUser(), oldId, newId);

        if (success) {
            showInfo("Appointment moved by admin.");
        } else {
            showError(appointmentService.getLastErrorMessage());
        }

        refreshTables();
    }

    /**
     * Sends reminder for selected admin appointment.
     */
    private void sendReminder() {
        String appointmentId = selectedId(adminTable);

        if (appointmentId == null) {
            showError("Please select an appointment.");
            return;
        }

        boolean success = appointmentService.sendReminder(appointmentId);

        if (success) {
            showInfo("Reminder generated.");
        } else {
            showError(appointmentService.getLastErrorMessage());
        }

        refreshNotifications();
    }

    /**
     * Refreshes available and admin tables.
     */
    private void refreshTables() {
        fillTable(availableTable, appointmentService.getAvailableAppointments());
        fillTable(adminTable, appointmentService.getAllAppointments());
        refreshNotifications();
    }

    /**
     * Refreshes notification text area.
     */
    private void refreshNotifications() {
        StringBuilder builder = new StringBuilder();

        for (String message : notificationObserver.getMessages()) {
            builder.append(message).append("\n");
        }

        notificationsArea.setText(builder.toString());
    }

    /**
     * Fills a table with appointments.
     *
     * @param table target table
     * @param appointments appointment list
     */
    private void fillTable(JTable table, List<Appointment> appointments) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "User", "Email", "Start", "End", "Type", "Participants", "Status"},
                0
        );

        for (Appointment appointment : appointments) {
            String userName = "";
            String email = "";

            if (appointment.getUser() != null) {
                userName = appointment.getUser().getName();
                email = appointment.getUser().getEmail();
            }

            model.addRow(new Object[]{
                    appointment.getId(),
                    userName,
                    email,
                    FORMATTER.format(appointment.getTimeSlot().getStartTime()),
                    FORMATTER.format(appointment.getTimeSlot().getEndTime()),
                    appointment.getType(),
                    appointment.getParticipants(),
                    appointment.getStatus()
            });
        }

        table.setModel(model);
    }

    /**
     * Returns selected appointment ID from table.
     *
     * @param table table
     * @return appointment ID or null
     */
    private String selectedId(JTable table) {
        int row = table.getSelectedRow();

        if (row < 0) {
            return null;
        }

        return table.getValueAt(row, 0).toString();
    }

    /**
     * Asks user/admin for an available slot ID.
     *
     * @return slot ID or null
     */
    private String askForAvailableSlotId() {
        List<Appointment> available = appointmentService.getAvailableAppointments();

        if (available.isEmpty()) {
            showError("No available slots.");
            return null;
        }

        StringBuilder message = new StringBuilder("Available slots:\n");

        for (Appointment appointment : available) {
            message.append(appointment.getId())
                    .append(" | ")
                    .append(FORMATTER.format(appointment.getTimeSlot().getStartTime()))
                    .append(" - ")
                    .append(FORMATTER.format(appointment.getTimeSlot().getEndTime()))
                    .append("\n");
        }

        return JOptionPane.showInputDialog(this, message + "\nEnter slot ID:");
    }

    /**
     * Configures table behavior.
     *
     * @param table table
     */
    private void configureTable(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
    }

    /**
     * Creates default future appointment slots.
     */
    private void seedSlots() {
        User availableUser = new User("0", "Available", "available@example.com");

        for (int i = 1; i <= 8; i++) {
            LocalDateTime start = LocalDateTime.now()
                    .plusDays(i)
                    .withHour(9 + (i % 5))
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);

            LocalDateTime end = start.plusHours(1);

            Appointment appointment = new Appointment(
                    "A" + i,
                    availableUser,
                    new TimeSlot(start, end),
                    AppointmentStatus.AVAILABLE,
                    AppointmentType.INDIVIDUAL,
                    1
            );

            appointmentService.addAppointment(appointment);
        }
    }

    /**
     * Creates admin user object for authorization checks.
     *
     * @return admin user
     */
    private User adminUser() {
        return new User("ADMIN", "admin", "admin@example.com");
    }

    /**
     * Shows error message.
     *
     * @param message message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows information message.
     *
     * @param message message
     */
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}