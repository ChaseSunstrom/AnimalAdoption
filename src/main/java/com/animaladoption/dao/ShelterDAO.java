package com.animaladoption.dao;

import com.animaladoption.model.Shelter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Shelter operations.
 */
public class ShelterDAO extends BaseDAO {

    public int create(Shelter shelter) throws SQLException {
        String sql = "INSERT INTO shelters (user_id, shelter_name, address_line1, address_line2, " +
                     "city, state, zip_code, phone, email, website, description, verified) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, shelter.getUserId());
            stmt.setString(2, shelter.getShelterName());
            stmt.setString(3, shelter.getAddressLine1());
            stmt.setString(4, shelter.getAddressLine2());
            stmt.setString(5, shelter.getCity());
            stmt.setString(6, shelter.getState());
            stmt.setString(7, shelter.getZipCode());
            stmt.setString(8, shelter.getPhone());
            stmt.setString(9, shelter.getEmail());
            stmt.setString(10, shelter.getWebsite());
            stmt.setString(11, shelter.getDescription());
            stmt.setBoolean(12, shelter.isVerified());

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

    public Shelter findById(int shelterId) throws SQLException {
        String sql = "SELECT * FROM shelters WHERE shelter_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, shelterId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToShelter(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public Shelter findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM shelters WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToShelter(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<Shelter> findAll(boolean verifiedOnly) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM shelters");

        if (verifiedOnly) {
            sql.append(" WHERE verified = TRUE");
        }

        sql.append(" ORDER BY shelter_name");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();

            List<Shelter> shelters = new ArrayList<>();
            while (rs.next()) {
                shelters.add(mapResultSetToShelter(rs));
            }
            return shelters;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<Shelter> findByLocation(String city, String state) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM shelters WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (city != null && !city.isEmpty()) {
            sql.append(" AND city = ?");
            params.add(city);
        }

        if (state != null && !state.isEmpty()) {
            sql.append(" AND state = ?");
            params.add(state);
        }

        sql.append(" ORDER BY shelter_name");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            rs = stmt.executeQuery();

            List<Shelter> shelters = new ArrayList<>();
            while (rs.next()) {
                shelters.add(mapResultSetToShelter(rs));
            }
            return shelters;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public boolean update(Shelter shelter) throws SQLException {
        String sql = "UPDATE shelters SET " +
                     "shelter_name = ?, address_line1 = ?, address_line2 = ?, " +
                     "city = ?, state = ?, zip_code = ?, phone = ?, email = ?, " +
                     "website = ?, description = ?, verified = ? " +
                     "WHERE shelter_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, shelter.getShelterName());
            stmt.setString(2, shelter.getAddressLine1());
            stmt.setString(3, shelter.getAddressLine2());
            stmt.setString(4, shelter.getCity());
            stmt.setString(5, shelter.getState());
            stmt.setString(6, shelter.getZipCode());
            stmt.setString(7, shelter.getPhone());
            stmt.setString(8, shelter.getEmail());
            stmt.setString(9, shelter.getWebsite());
            stmt.setString(10, shelter.getDescription());
            stmt.setBoolean(11, shelter.isVerified());
            stmt.setInt(12, shelter.getShelterId());

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean updateVerificationStatus(int shelterId, boolean verified) throws SQLException {
        String sql = "UPDATE shelters SET verified = ? WHERE shelter_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setBoolean(1, verified);
            stmt.setInt(2, shelterId);

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean delete(int shelterId) throws SQLException {
        String sql = "DELETE FROM shelters WHERE shelter_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, shelterId);

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean shelterExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM shelters WHERE user_id = ?";

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

    public int countAnimals(int shelterId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM animals WHERE shelter_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, shelterId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private Shelter mapResultSetToShelter(ResultSet rs) throws SQLException {
        Shelter shelter = new Shelter();
        shelter.setShelterId(rs.getInt("shelter_id"));
        shelter.setUserId(rs.getInt("user_id"));
        shelter.setShelterName(rs.getString("shelter_name"));
        shelter.setAddressLine1(rs.getString("address_line1"));
        shelter.setAddressLine2(rs.getString("address_line2"));
        shelter.setCity(rs.getString("city"));
        shelter.setState(rs.getString("state"));
        shelter.setZipCode(rs.getString("zip_code"));
        shelter.setPhone(rs.getString("phone"));
        shelter.setEmail(rs.getString("email"));
        shelter.setWebsite(rs.getString("website"));
        shelter.setDescription(rs.getString("description"));
        shelter.setVerified(rs.getBoolean("verified"));
        return shelter;
    }
}
