package com.animaladoption.dao;

import com.animaladoption.model.AdoptionApplication;
import com.animaladoption.model.Animal;
import com.animaladoption.model.User;
import com.animaladoption.model.Shelter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for AdoptionApplication operations.
 */
public class AdoptionApplicationDAO extends BaseDAO {

    public int create(AdoptionApplication application) throws SQLException {
        String sql = "INSERT INTO adoption_applications (animal_id, adopter_id, shelter_id, status, " +
                     "why_adopt, previous_pet_experience, veterinarian_name, veterinarian_phone, " +
                     "reference1_name, reference1_phone, reference1_relationship, " +
                     "reference2_name, reference2_phone, reference2_relationship) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, application.getAnimalId());
            stmt.setInt(2, application.getAdopterId());
            stmt.setInt(3, application.getShelterId());
            stmt.setString(4, application.getStatus());
            stmt.setString(5, application.getWhyAdopt());
            stmt.setString(6, application.getPreviousPetExperience());
            stmt.setString(7, application.getVeterinarianName());
            stmt.setString(8, application.getVeterinarianPhone());
            stmt.setString(9, application.getReference1Name());
            stmt.setString(10, application.getReference1Phone());
            stmt.setString(11, application.getReference1Relationship());
            stmt.setString(12, application.getReference2Name());
            stmt.setString(13, application.getReference2Phone());
            stmt.setString(14, application.getReference2Relationship());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public AdoptionApplication findById(int applicationId) throws SQLException {
        String sql = "SELECT aa.*, " +
                     "a.name AS animal_name, a.species, a.breed, a.primary_image_url, " +
                     "u.first_name AS adopter_first_name, u.last_name AS adopter_last_name, u.email AS adopter_email, " +
                     "s.shelter_name, s.email AS shelter_email, s.phone AS shelter_phone " +
                     "FROM adoption_applications aa " +
                     "JOIN animals a ON aa.animal_id = a.animal_id " +
                     "JOIN users u ON aa.adopter_id = u.user_id " +
                     "JOIN shelters s ON aa.shelter_id = s.shelter_id " +
                     "WHERE aa.application_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, applicationId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToApplication(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<AdoptionApplication> findByAdopterId(int adopterId) throws SQLException {
        String sql = "SELECT aa.*, " +
                     "a.name AS animal_name, a.species, a.breed, a.primary_image_url, " +
                     "s.shelter_name " +
                     "FROM adoption_applications aa " +
                     "JOIN animals a ON aa.animal_id = a.animal_id " +
                     "JOIN shelters s ON aa.shelter_id = s.shelter_id " +
                     "WHERE aa.adopter_id = ? " +
                     "ORDER BY aa.submitted_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adopterId);
            rs = stmt.executeQuery();

            List<AdoptionApplication> applications = new ArrayList<>();
            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
            return applications;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<AdoptionApplication> findByShelterId(int shelterId, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT aa.*, " +
            "a.name AS animal_name, a.species, a.breed, a.primary_image_url, " +
            "u.first_name AS adopter_first_name, u.last_name AS adopter_last_name, u.email AS adopter_email " +
            "FROM adoption_applications aa " +
            "JOIN animals a ON aa.animal_id = a.animal_id " +
            "JOIN users u ON aa.adopter_id = u.user_id " +
            "WHERE aa.shelter_id = ?"
        );

        if (status != null && !status.isEmpty()) {
            sql.append(" AND aa.status = ?");
        }

        sql.append(" ORDER BY aa.submitted_at DESC");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql.toString());
            stmt.setInt(1, shelterId);

            if (status != null && !status.isEmpty()) {
                stmt.setString(2, status);
            }

            rs = stmt.executeQuery();

            List<AdoptionApplication> applications = new ArrayList<>();
            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
            return applications;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<AdoptionApplication> findByAnimalId(int animalId) throws SQLException {
        String sql = "SELECT aa.*, " +
                     "u.first_name AS adopter_first_name, u.last_name AS adopter_last_name, u.email AS adopter_email " +
                     "FROM adoption_applications aa " +
                     "JOIN users u ON aa.adopter_id = u.user_id " +
                     "WHERE aa.animal_id = ? " +
                     "ORDER BY aa.submitted_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, animalId);
            rs = stmt.executeQuery();

            List<AdoptionApplication> applications = new ArrayList<>();
            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
            return applications;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public boolean updateStatus(int applicationId, String status, Integer reviewedBy, String shelterNotes) throws SQLException {
        String sql = "UPDATE adoption_applications " +
                     "SET status = ?, reviewed_at = CURRENT_TIMESTAMP, reviewed_by = ?, shelter_notes = ? " +
                     "WHERE application_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, status);
            if (reviewedBy != null) {
                stmt.setInt(2, reviewedBy);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, shelterNotes);
            stmt.setInt(4, applicationId);

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean update(AdoptionApplication application) throws SQLException {
        String sql = "UPDATE adoption_applications " +
                     "SET why_adopt = ?, previous_pet_experience = ?, " +
                     "veterinarian_name = ?, veterinarian_phone = ?, " +
                     "reference1_name = ?, reference1_phone = ?, reference1_relationship = ?, " +
                     "reference2_name = ?, reference2_phone = ?, reference2_relationship = ? " +
                     "WHERE application_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, application.getWhyAdopt());
            stmt.setString(2, application.getPreviousPetExperience());
            stmt.setString(3, application.getVeterinarianName());
            stmt.setString(4, application.getVeterinarianPhone());
            stmt.setString(5, application.getReference1Name());
            stmt.setString(6, application.getReference1Phone());
            stmt.setString(7, application.getReference1Relationship());
            stmt.setString(8, application.getReference2Name());
            stmt.setString(9, application.getReference2Phone());
            stmt.setString(10, application.getReference2Relationship());
            stmt.setInt(11, application.getApplicationId());

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean delete(int applicationId) throws SQLException {
        String sql = "DELETE FROM adoption_applications WHERE application_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, applicationId);

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public int countByAdopter(int adopterId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM adoption_applications WHERE adopter_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adopterId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public boolean hasAppliedForAnimal(int adopterId, int animalId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM adoption_applications " +
                     "WHERE adopter_id = ? AND animal_id = ? AND status NOT IN ('withdrawn', 'rejected')";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adopterId);
            stmt.setInt(2, animalId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private AdoptionApplication mapResultSetToApplication(ResultSet rs) throws SQLException {
        AdoptionApplication app = new AdoptionApplication();
        app.setApplicationId(rs.getInt("application_id"));
        app.setAnimalId(rs.getInt("animal_id"));
        app.setAdopterId(rs.getInt("adopter_id"));
        app.setShelterId(rs.getInt("shelter_id"));
        app.setStatus(rs.getString("status"));
        app.setWhyAdopt(rs.getString("why_adopt"));
        app.setPreviousPetExperience(rs.getString("previous_pet_experience"));
        app.setVeterinarianName(rs.getString("veterinarian_name"));
        app.setVeterinarianPhone(rs.getString("veterinarian_phone"));
        app.setReference1Name(rs.getString("reference1_name"));
        app.setReference1Phone(rs.getString("reference1_phone"));
        app.setReference1Relationship(rs.getString("reference1_relationship"));
        app.setReference2Name(rs.getString("reference2_name"));
        app.setReference2Phone(rs.getString("reference2_phone"));
        app.setReference2Relationship(rs.getString("reference2_relationship"));
        app.setShelterNotes(rs.getString("shelter_notes"));
        app.setSubmittedAt(rs.getTimestamp("submitted_at"));
        app.setReviewedAt(rs.getTimestamp("reviewed_at"));

        Integer reviewedBy = rs.getInt("reviewed_by");
        if (!rs.wasNull()) {
            app.setReviewedBy(reviewedBy);
        }

        // Map animal information if available
        try {
            Animal animal = new Animal();
            animal.setAnimalId(rs.getInt("animal_id"));
            animal.setName(rs.getString("animal_name"));
            animal.setSpecies(rs.getString("species"));
            animal.setBreed(rs.getString("breed"));
            animal.setPrimaryImageUrl(rs.getString("primary_image_url"));
            app.setAnimal(animal);
        } catch (SQLException e) {
            // Animal columns not available
        }

        // Map adopter information if available
        try {
            User adopter = new User();
            adopter.setUserId(rs.getInt("adopter_id"));
            adopter.setFirstName(rs.getString("adopter_first_name"));
            adopter.setLastName(rs.getString("adopter_last_name"));
            adopter.setEmail(rs.getString("adopter_email"));
            app.setAdopter(adopter);
        } catch (SQLException e) {
            // Adopter columns not available
        }

        // Map shelter information if available
        try {
            Shelter shelter = new Shelter();
            shelter.setShelterName(rs.getString("shelter_name"));
            shelter.setEmail(rs.getString("shelter_email"));
            shelter.setPhone(rs.getString("shelter_phone"));
            app.setShelter(shelter);
        } catch (SQLException e) {
            // Shelter columns not available
        }

        return app;
    }
}
