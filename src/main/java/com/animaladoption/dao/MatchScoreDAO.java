package com.animaladoption.dao;

import com.animaladoption.model.MatchScore;
import com.animaladoption.model.Animal;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for MatchScore operations.
 */
public class MatchScoreDAO extends BaseDAO {

    public int create(MatchScore matchScore) throws SQLException {
        String sql = "INSERT INTO match_scores (adopter_id, animal_id, match_score, score_details) " +
                     "VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, matchScore.getAdopterId());
            stmt.setInt(2, matchScore.getAnimalId());
            stmt.setDouble(3, matchScore.getMatchScore());
            stmt.setString(4, mapToJson(matchScore.getScoreDetails()));

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

    public MatchScore findById(int scoreId) throws SQLException {
        String sql = "SELECT * FROM match_scores WHERE score_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scoreId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMatchScore(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<MatchScore> findByAdopterId(int adopterId, int limit) throws SQLException {
        String sql = "SELECT ms.*, " +
                     "a.name AS animal_name, a.species, a.breed, a.age_years, a.age_months, " +
                     "a.size, a.primary_image_url, a.status " +
                     "FROM match_scores ms " +
                     "JOIN animals a ON ms.animal_id = a.animal_id " +
                     "WHERE ms.adopter_id = ? AND a.status = 'available' " +
                     "ORDER BY ms.match_score DESC " +
                     "LIMIT ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adopterId);
            stmt.setInt(2, limit);
            rs = stmt.executeQuery();

            List<MatchScore> scores = new ArrayList<>();
            while (rs.next()) {
                scores.add(mapResultSetToMatchScore(rs));
            }
            return scores;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public MatchScore findByAdopterAndAnimal(int adopterId, int animalId) throws SQLException {
        String sql = "SELECT * FROM match_scores WHERE adopter_id = ? AND animal_id = ?";

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
                return mapResultSetToMatchScore(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<MatchScore> getTopMatches(int adopterId, int limit) throws SQLException {
        String sql = "SELECT ms.*, " +
                     "a.name AS animal_name, a.species, a.breed, a.age_years, a.age_months, " +
                     "a.size, a.primary_image_url, a.status, a.shelter_id " +
                     "FROM match_scores ms " +
                     "JOIN animals a ON ms.animal_id = a.animal_id " +
                     "WHERE ms.adopter_id = ? AND a.status = 'available' AND ms.match_score >= 60 " +
                     "ORDER BY ms.match_score DESC " +
                     "LIMIT ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adopterId);
            stmt.setInt(2, limit);
            rs = stmt.executeQuery();

            List<MatchScore> scores = new ArrayList<>();
            while (rs.next()) {
                scores.add(mapResultSetToMatchScore(rs));
            }
            return scores;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public boolean update(MatchScore matchScore) throws SQLException {
        String sql = "UPDATE match_scores SET match_score = ?, score_details = ?, " +
                     "calculated_at = CURRENT_TIMESTAMP WHERE score_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setDouble(1, matchScore.getMatchScore());
            stmt.setString(2, mapToJson(matchScore.getScoreDetails()));
            stmt.setInt(3, matchScore.getScoreId());

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public boolean upsert(MatchScore matchScore) throws SQLException {
        MatchScore existing = findByAdopterAndAnimal(matchScore.getAdopterId(), matchScore.getAnimalId());

        if (existing != null) {
            matchScore.setScoreId(existing.getScoreId());
            return update(matchScore);
        } else {
            int id = create(matchScore);
            return id > 0;
        }
    }

    public boolean delete(int scoreId) throws SQLException {
        String sql = "DELETE FROM match_scores WHERE score_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scoreId);

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public int deleteByAdopterId(int adopterId) throws SQLException {
        String sql = "DELETE FROM match_scores WHERE adopter_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adopterId);

            return stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }

    public int deleteByAnimalId(int animalId) throws SQLException {
        String sql = "DELETE FROM match_scores WHERE animal_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, animalId);

            return stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }

    public int countByAdopter(int adopterId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM match_scores WHERE adopter_id = ?";

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

    private MatchScore mapResultSetToMatchScore(ResultSet rs) throws SQLException {
        MatchScore score = new MatchScore();
        score.setScoreId(rs.getInt("score_id"));
        score.setAdopterId(rs.getInt("adopter_id"));
        score.setAnimalId(rs.getInt("animal_id"));
        score.setMatchScore(rs.getDouble("match_score"));
        score.setCalculatedAt(rs.getTimestamp("calculated_at"));

        // Parse JSON score_details
        String scoreDetailsJson = rs.getString("score_details");
        if (scoreDetailsJson != null) {
            score.setScoreDetails(jsonToMap(scoreDetailsJson));
        }

        // Map animal information if available
        try {
            Animal animal = new Animal();
            animal.setAnimalId(rs.getInt("animal_id"));
            animal.setName(rs.getString("animal_name"));
            animal.setSpecies(rs.getString("species"));
            animal.setBreed(rs.getString("breed"));
            animal.setAgeYears(rs.getInt("age_years"));
            animal.setAgeMonths(rs.getInt("age_months"));
            animal.setSize(rs.getString("size"));
            animal.setPrimaryImageUrl(rs.getString("primary_image_url"));
            animal.setStatus(rs.getString("status"));
            animal.setShelterId(rs.getInt("shelter_id"));
            score.setAnimal(animal);
        } catch (SQLException e) {
            // Animal columns not available
        }

        return score;
    }

    private String mapToJson(Map<String, Double> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        JSONObject json = new JSONObject(map);
        return json.toString();
    }

    private Map<String, Double> jsonToMap(String json) {
        Map<String, Double> map = new HashMap<>();
        if (json == null || json.trim().isEmpty() || json.equals("{}")) {
            return map;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            for (String key : jsonObj.keySet()) {
                map.put(key, jsonObj.getDouble(key));
            }
        } catch (Exception e) {
            // Return empty map if parsing fails
        }

        return map;
    }
}
