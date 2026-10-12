
package com.vitalloop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    private static final String SEARCH_SQL =
        "SELECT donor_id, blood_group, city " +
        "FROM donors " +
        "WHERE availability = 'AVAILABLE' " +
        "AND consent_to_contact = TRUE " +
        "AND blood_group = ? " +
        "AND LOWER(city) = LOWER(?) " +
        "ORDER BY created_at DESC";

    public List<Donor> searchAvailableDonors(
            String bloodGroup, String city) throws SQLException {

        List<Donor> donors = new ArrayList<>();

        if (bloodGroup == null || city == null
                || city.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Blood group and city are required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(SEARCH_SQL)) {

            statement.setString(1, bloodGroup.trim());
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
}
