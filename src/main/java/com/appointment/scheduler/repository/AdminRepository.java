package com.appointment.scheduler.repository;

import com.appointment.scheduler.model.Admin;

/**
 * Repository interface for retrieving administrator data.
 * <p>
 * This interface abstracts the source of administrator credentials,
 * allowing the authentication service to remain independent from
 * configuration files or any future data source.
 * </p>
 */
public interface AdminRepository {

    /**
     * Retrieves the administrator credentials from the data source.
     *
     * @return an Admin object containing the stored administrator credentials
     */
    Admin getAdmin();
}