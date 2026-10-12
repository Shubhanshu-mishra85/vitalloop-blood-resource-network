
package com.vitalloop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    // Search donors by blood group and city.
    // Only donors who are marked available and have
    // consented to contact are returned.
    private static final String SEARCH_SQL =
        "SELECT donor_id, blood_group, city " +
        "FROM donors " +
        "WHERE availability = 'AVAILABLE' " +
        "AND consent_to_contact = TRUE " +
        "AND blood_group = ? " +
        "AND LOWER(city) = LOWER(?) " +
        "ORDER BY created_at DESC";

    public List<Donor> searchAvailableDonors(
            String bloodGroup,
            String city) throws SQLException {

        validateBloodGroup(bloodGroup);
        validateCity(city);

        List<Donor> donors = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(SEARCH_SQL)) {

            statement.setString(1, bloodGroup);
            statement.setString(2, city.trim());

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Donor donor = new Donor(
                        result.getInt("donor_id"),
                        result.getString("blood_group"),
                        result.getString("city")
                    );

                    donors.add(donor);
                }
            }
        }

        return donors;
    }

    // Register a donor linked to an existing user account.
    // New donor profiles default to UNAVAILABLE.
    public int registerDonor(
            int userId,
            String bloodGroup,
            String city,
            boolean consentToContact) throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                "A valid user ID is required."
            );
        }

        validateBloodGroup(bloodGroup);
        validateCity(city);

        if (!consentToContact) {
            throw new IllegalArgumentException(
                "Contact consent is required."
            );
        }

        String sql =
            "INSERT INTO donors " +
            "(user_id, blood_group, city, availability, " +
            "consent_to_contact) " +
            "VALUES (?, ?, ?, 'UNAVAILABLE', ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);
            statement.setString(2, bloodGroup);
            statement.setString(3, city.trim());
            statement.setBoolean(4, consentToContact);

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException(
                    "Donor registration failed."
                );
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException(
            "Donor ID could not be generated."
        );
    }

    // Validate supported blood groups.
    private void validateBloodGroup(String bloodGroup) {
        List<String> validGroups = List.of(
            "A+", "A-", "B+", "B-",
            "AB+", "AB-", "O+", "O-"
        );

        if (bloodGroup == null ||
                !validGroups.contains(bloodGroup.trim())) {
            throw new IllegalArgumentException(
                "Invalid blood group."
            );
        }
    }

    // Validate city input.
    private void validateCity(String city) {
        if (city == null ||
                city.trim().isEmpty() ||
                city.trim().length() > 100) {
            throw new IllegalArgumentException(
                "City must contain 1 to 100 characters."
            );
        }
    }
}
