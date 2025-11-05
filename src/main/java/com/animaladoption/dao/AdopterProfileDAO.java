package com.animaladoption.dao;

import com.animaladoption.model.AdopterProfile;

import java.sql.*;

/**
 * DAO for AdopterProfile operations.
 */
public class AdopterProfileDAO extends BaseDAO {

    public int create(AdopterProfile profile) throws SQLException {
        String sql = "INSERT INTO adopter_profiles (user_id, address_line1, address_line2, city, state, zip_code, " +
                     "home_type, home_size, has_yard, has_children, children_ages, has_other_pets, " +
                     "other_pets_description, experience_level, activity_level, preferences_notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, profile.getUserId());
            stmt.setString(2, profile.getAddressLine1());
            stmt.setString(3, profile.getAddressLine2());
            stmt.setString(4, profile.getCity());
            stmt.setString(5, profile.getState());
            stmt.setString(6, profile.getZipCode());
            stmt.setString(7, profile.getHomeType());
            stmt.setString(8, profile.getHomeSize());
            stmt.setBoolean(9, profile.isHasYard());
            stmt.setBoolean(10, profile.isHasChildren());
            stmt.setString(11, profile.getChildrenAges());
            stmt.setBoolean(12, profile.isHasOtherPets());
            stmt.setString(13, profile.getOtherPetsDescription());
            stmt.setString(14, profile.getExperienceLevel());
            stmt.setString(15, profile.getActivityLevel());
            stmt.setString(16, profile.getPreferencesNotes());

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

    public AdopterProfile findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM adopter_profiles WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProfile(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public AdopterProfile findById(int profileId) throws SQLException {
        String sql = "SELECT * FROM adopter_profiles WHERE profile_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profileId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProfile(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public boolean update(AdopterProfile profile) throws SQLException {
        String sql = "UPDATE adopter_profiles SET " +
                     "address_line1 = ?, address_line2 = ?, city = ?, state = ?, zip_code = ?, " +
                     "home_type = ?, home_size = ?, has_yard = ?, has_children = ?, children_ages = ?, " +
                     "has_other_pets = ?, other_pets_description = ?, experience_level = ?, " +
                     "activity_level = ?, preferences_notes = ? " +
                     "WHERE profile_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, profile.getAddressLine1());
            stmt.setString(2, profile.getAddressLine2());
            stmt.setString(3, profile.getCity());
            stmt.setString(4, profile.getState());
            stmt.setString(5, profile.getZipCode());
            stmt.setString(6, profile.getHomeType());
            stmt.setString(7, profile.getHomeSize());
            stmt.setBoolean(8, profile.isHasYard());
            stmt.setBoolean(9, profile.isHasChildren());
            stmt.setString(10, profile.getChildrenAges());
            stmt.setBoolean(11, profile.isHasOtherPets());
            stmt.setString(12, profile.getOtherPetsDescription());
            stmt.setString(13, profile.getExperienceLevel());
            stmt.setString(14, profile.getActivityLevel());
            stmt.setString(15, profile.getPreferencesNotes());
            stmt.setInt(16, profile.getProfileId());

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean delete(int profileId) throws SQLException {
        String sql = "DELETE FROM adopter_profiles WHERE profile_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profileId);

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean profileExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM adopter_profiles WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private AdopterProfile mapResultSetToProfile(ResultSet rs) throws SQLException {
        AdopterProfile profile = new AdopterProfile();
        profile.setProfileId(rs.getInt("profile_id"));
        profile.setUserId(rs.getInt("user_id"));
        profile.setAddressLine1(rs.getString("address_line1"));
        profile.setAddressLine2(rs.getString("address_line2"));
        profile.setCity(rs.getString("city"));
        profile.setState(rs.getString("state"));
        profile.setZipCode(rs.getString("zip_code"));
        profile.setHomeType(rs.getString("home_type"));
        profile.setHomeSize(rs.getString("home_size"));
        profile.setHasYard(rs.getBoolean("has_yard"));
        profile.setHasChildren(rs.getBoolean("has_children"));
        profile.setChildrenAges(rs.getString("children_ages"));
        profile.setHasOtherPets(rs.getBoolean("has_other_pets"));
        profile.setOtherPetsDescription(rs.getString("other_pets_description"));
        profile.setExperienceLevel(rs.getString("experience_level"));
        profile.setActivityLevel(rs.getString("activity_level"));
        profile.setPreferencesNotes(rs.getString("preferences_notes"));
        return profile;
    }
}
