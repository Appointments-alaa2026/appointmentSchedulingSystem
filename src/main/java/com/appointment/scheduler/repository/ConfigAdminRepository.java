package com.appointment.scheduler.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.appointment.scheduler.model.Admin;

/**
 * Repository implementation that loads administrator credentials
 * from a configuration file located in the resources folder.
 */
public class ConfigAdminRepository implements AdminRepository {

    private static final String CONFIG_FILE = "admin.properties";
    private static final String USERNAME_KEY = "admin.username";
    private static final String PASSWORD_KEY = "admin.password";

    /**
     * Retrieves the administrator credentials from the configuration file.
     *
     * @return an Admin object containing the configured username and password
     * @throws RuntimeException if the configuration file is missing
     *                          or cannot be loaded
     */
    @Override
    public Admin getAdmin() {
        Properties properties = loadProperties();

        String username = properties.getProperty(USERNAME_KEY);
        String password = properties.getProperty(PASSWORD_KEY);

        if (username == null || password == null) {
            throw new RuntimeException("Admin credentials are missing in the configuration file.");
        }

        return new Admin(username, password);
        
    }

    /**
     * Loads the configuration properties from the resources folder.
     *
     * @return a Properties object containing the loaded values
     * @throws RuntimeException if the file cannot be found or read
     */
    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Configuration file '" + CONFIG_FILE + "' was not found.");
            }

            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file '" + CONFIG_FILE + "'.", e);
        }
    }
}