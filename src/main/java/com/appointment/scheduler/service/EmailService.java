package com.appointment.scheduler.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

/**
 * Sends real emails using Gmail SMTP.
 * Credentials are loaded from email.properties in the classpath.
 *
 * @author Appointment Scheduling System
 * @version 1.0
 */
public class EmailService {

    private final String username;
    private final String password;

    /**
     * Default constructor — loads credentials from email.properties (classpath).
     */
    public EmailService() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("email.properties")) {

            if (input == null) {
                throw new RuntimeException(
                    "email.properties not found in classpath. " +
                    "Make sure it exists in src/main/resources/"
                );
            }

            Properties props = new Properties();
            props.load(input);

            this.username = props.getProperty("EMAIL_USERNAME");
            this.password = props.getProperty("EMAIL_PASSWORD");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load email credentials: " + e.getMessage(), e);
        }
    }

    /**
     * Constructor for testing or manual credential injection.
     *
     * @param username Gmail address
     * @param password Gmail App Password
     */
    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Sends an email via Gmail SMTP.
     *
     * @param to      recipient email address
     * @param subject email subject
     * @param body    email body text
     */
    public void sendEmail(String to, String subject, String body) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalStateException(
                "Email credentials are missing. Check email.properties file."
            );
        }

        Properties smtpProps = new Properties();
        smtpProps.put("mail.smtp.auth", "true");
        smtpProps.put("mail.smtp.starttls.enable", "true");
        smtpProps.put("mail.smtp.host", "smtp.gmail.com");
        smtpProps.put("mail.smtp.port", "587");
        smtpProps.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(smtpProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to " + to);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}