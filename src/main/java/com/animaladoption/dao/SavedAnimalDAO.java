package com.animaladoption.dao;

import com.animaladoption.model.Animal;
import com.animaladoption.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SavedAnimal operations.
 */
public class SavedAnimalDAO extends BaseDAO {
    
    /**
     * Save an animal for an adopter.
     */
    public boolean saveAnimal(int userId, int animalId) {
        String sql = "INSERT INTO saved_animals (user_id, animal_id) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE saved_at = CURRENT_TIMESTAMP";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, animalId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving animal: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Unsave an animal for an adopter.
     */
    public boolean unsaveAnimal(int userId, int animalId) {
        String sql = "DELETE FROM saved_animals WHERE user_id = ? AND animal_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, animalId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error unsaving animal: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if an animal is saved by a user.
     */
    public boolean isAnimalSaved(int userId, int animalId) {
        String sql = "SELECT COUNT(*) FROM saved_animals WHERE user_id = ? AND animal_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, animalId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking if animal is saved: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get all saved animals for a user with full animal details.
     */
    public List<Animal> getSavedAnimals(int userId) {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT a.*, s.shelter_name, sa.saved_at " +
                     "FROM saved_animals sa " +
                     "JOIN animals a ON sa.animal_id = a.animal_id " +
                     "JOIN shelters s ON a.shelter_id = s.shelter_id " +
                     "WHERE sa.user_id = ? " +
                     "ORDER BY sa.saved_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                AnimalDAO animalDAO = new AnimalDAO();
                while (rs.next()) {
                    animals.add(animalDAO.mapResultSetToAnimal(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting saved animals: " + e.getMessage());
        }
        
        return animals;
    }
    
    /**
     * Get count of saved animals for a user.
     */
    public int getSavedAnimalsCount(int userId) {
        String sql = "SELECT COUNT(*) FROM saved_animals WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting saved animals count: " + e.getMessage());
        }
        
        return 0;
    }
}

