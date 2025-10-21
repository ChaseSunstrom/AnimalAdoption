package com.animaladoption.dao;

import com.animaladoption.model.Animal;
import com.animaladoption.model.SearchCriteria;
import com.animaladoption.model.Shelter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Animal operations.
 */
public class AnimalDAO extends BaseDAO {

    public List<Animal> search(SearchCriteria criteria, int offset, int limit) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT a.*, s.shelter_name, s.city AS shelter_city, s.state AS shelter_state, " +
            "s.phone AS shelter_phone, s.email AS shelter_email " +
            "FROM animals a JOIN shelters s ON a.shelter_id = s.shelter_id " +
            "WHERE a.status = 'available'"
        );

        List<Object> params = new ArrayList<>();

        if (criteria.getSpecies() != null && !criteria.getSpecies().isEmpty()) {
            sql.append(" AND a.species = ?");
            params.add(criteria.getSpecies());
        }
        if (criteria.getBreed() != null && !criteria.getBreed().isEmpty()) {
            sql.append(" AND a.breed LIKE ?");
            params.add("%" + criteria.getBreed() + "%");
        }
        if (criteria.getSize() != null && !criteria.getSize().isEmpty()) {
            sql.append(" AND a.size = ?");
            params.add(criteria.getSize());
        }
        if (criteria.getColor() != null && !criteria.getColor().isEmpty()) {
            sql.append(" AND a.color LIKE ?");
            params.add("%" + criteria.getColor() + "%");
        }
        if (criteria.getGender() != null && !criteria.getGender().isEmpty()) {
            sql.append(" AND a.gender = ?");
            params.add(criteria.getGender());
        }
        if (criteria.getGoodWithKids() != null && criteria.getGoodWithKids()) {
            sql.append(" AND a.good_with_kids = TRUE");
        }
        if (criteria.getGoodWithDogs() != null && criteria.getGoodWithDogs()) {
            sql.append(" AND a.good_with_dogs = TRUE");
        }
        if (criteria.getGoodWithCats() != null && criteria.getGoodWithCats()) {
            sql.append(" AND a.good_with_cats = TRUE");
        }
        if (criteria.getEnergyLevels() != null && !criteria.getEnergyLevels().isEmpty()) {
            sql.append(" AND a.energy_level IN (");
            for (int i = 0; i < criteria.getEnergyLevels().size(); i++) {
                sql.append(i == 0 ? "?" : ",?");
                params.add(criteria.getEnergyLevels().get(i));
            }
            sql.append(")");
        }
        if (criteria.getCity() != null && !criteria.getCity().isEmpty()) {
            sql.append(" AND s.city = ?");
            params.add(criteria.getCity());
        }
        if (criteria.getState() != null && !criteria.getState().isEmpty()) {
            sql.append(" AND s.state = ?");
            params.add(criteria.getState());
        }

        sql.append(" ORDER BY a.created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

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
            List<Animal> animals = new ArrayList<>();

            while (rs.next()) {
                animals.add(mapResultSetToAnimal(rs));
            }

            return animals;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public Animal findById(int animalId) throws SQLException {
        String sql = "SELECT a.*, s.shelter_name, s.city AS shelter_city, s.state AS shelter_state, " +
                     "s.phone AS shelter_phone, s.email AS shelter_email, s.address_line1, s.address_line2, " +
                     "s.zip_code AS shelter_zip " +
                     "FROM animals a JOIN shelters s ON a.shelter_id = s.shelter_id " +
                     "WHERE a.animal_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, animalId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAnimal(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public List<Animal> findByShelterId(int shelterId) throws SQLException {
        String sql = "SELECT * FROM animals WHERE shelter_id = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, shelterId);
            rs = stmt.executeQuery();

            List<Animal> animals = new ArrayList<>();
            while (rs.next()) {
                animals.add(mapResultSetToAnimalBasic(rs));
            }
            return animals;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    public int create(Animal animal) throws SQLException {
        String sql = "INSERT INTO animals (shelter_id, name, species, breed, age_years, age_months, " +
                     "gender, size, color, weight_lbs, description, medical_notes, behavioral_notes, " +
                     "good_with_kids, good_with_dogs, good_with_cats, requires_experienced_owner, " +
                     "requires_yard, energy_level, status, intake_date, primary_image_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, animal.getShelterId());
            stmt.setString(2, animal.getName());
            stmt.setString(3, animal.getSpecies());
            stmt.setString(4, animal.getBreed());
            stmt.setInt(5, animal.getAgeYears());
            stmt.setInt(6, animal.getAgeMonths());
            stmt.setString(7, animal.getGender());
            stmt.setString(8, animal.getSize());
            stmt.setString(9, animal.getColor());
            stmt.setDouble(10, animal.getWeightLbs());
            stmt.setString(11, animal.getDescription());
            stmt.setString(12, animal.getMedicalNotes());
            stmt.setString(13, animal.getBehavioralNotes());
            stmt.setBoolean(14, animal.isGoodWithKids());
            stmt.setBoolean(15, animal.isGoodWithDogs());
            stmt.setBoolean(16, animal.isGoodWithCats());
            stmt.setBoolean(17, animal.requiresExperiencedOwner());
            stmt.setBoolean(18, animal.requiresYard());
            stmt.setString(19, animal.getEnergyLevel());
            stmt.setString(20, animal.getStatus());
            stmt.setDate(21, animal.getIntakeDate());
            stmt.setString(22, animal.getPrimaryImageUrl());

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

    public boolean update(Animal animal) throws SQLException {
        String sql = "UPDATE animals SET name = ?, species = ?, breed = ?, age_years = ?, age_months = ?, " +
                     "gender = ?, size = ?, color = ?, weight_lbs = ?, description = ?, medical_notes = ?, " +
                     "behavioral_notes = ?, good_with_kids = ?, good_with_dogs = ?, good_with_cats = ?, " +
                     "requires_experienced_owner = ?, requires_yard = ?, energy_level = ?, status = ?, " +
                     "primary_image_url = ? WHERE animal_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, animal.getName());
            stmt.setString(2, animal.getSpecies());
            stmt.setString(3, animal.getBreed());
            stmt.setInt(4, animal.getAgeYears());
            stmt.setInt(5, animal.getAgeMonths());
            stmt.setString(6, animal.getGender());
            stmt.setString(7, animal.getSize());
            stmt.setString(8, animal.getColor());
            stmt.setDouble(9, animal.getWeightLbs());
            stmt.setString(10, animal.getDescription());
            stmt.setString(11, animal.getMedicalNotes());
            stmt.setString(12, animal.getBehavioralNotes());
            stmt.setBoolean(13, animal.isGoodWithKids());
            stmt.setBoolean(14, animal.isGoodWithDogs());
            stmt.setBoolean(15, animal.isGoodWithCats());
            stmt.setBoolean(16, animal.requiresExperiencedOwner());
            stmt.setBoolean(17, animal.requiresYard());
            stmt.setString(18, animal.getEnergyLevel());
            stmt.setString(19, animal.getStatus());
            stmt.setString(20, animal.getPrimaryImageUrl());
            stmt.setInt(21, animal.getAnimalId());

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt);
        }
    }

    public int countSearch(SearchCriteria criteria) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM animals a JOIN shelters s ON a.shelter_id = s.shelter_id " +
            "WHERE a.status = 'available'"
        );

        List<Object> params = new ArrayList<>();

        if (criteria.getSpecies() != null && !criteria.getSpecies().isEmpty()) {
            sql.append(" AND a.species = ?");
            params.add(criteria.getSpecies());
        }
        if (criteria.getBreed() != null && !criteria.getBreed().isEmpty()) {
            sql.append(" AND a.breed LIKE ?");
            params.add("%" + criteria.getBreed() + "%");
        }
        if (criteria.getSize() != null && !criteria.getSize().isEmpty()) {
            sql.append(" AND a.size = ?");
            params.add(criteria.getSize());
        }
        if (criteria.getColor() != null && !criteria.getColor().isEmpty()) {
            sql.append(" AND a.color LIKE ?");
            params.add("%" + criteria.getColor() + "%");
        }
        if (criteria.getGender() != null && !criteria.getGender().isEmpty()) {
            sql.append(" AND a.gender = ?");
            params.add(criteria.getGender());
        }
        if (criteria.getGoodWithKids() != null && criteria.getGoodWithKids()) {
            sql.append(" AND a.good_with_kids = TRUE");
        }
        if (criteria.getGoodWithDogs() != null && criteria.getGoodWithDogs()) {
            sql.append(" AND a.good_with_dogs = TRUE");
        }
        if (criteria.getGoodWithCats() != null && criteria.getGoodWithCats()) {
            sql.append(" AND a.good_with_cats = TRUE");
        }
        if (criteria.getEnergyLevels() != null && !criteria.getEnergyLevels().isEmpty()) {
            sql.append(" AND a.energy_level IN (");
            for (int i = 0; i < criteria.getEnergyLevels().size(); i++) {
                sql.append(i == 0 ? "?" : ",?");
                params.add(criteria.getEnergyLevels().get(i));
            }
            sql.append(")");
        }
        if (criteria.getCity() != null && !criteria.getCity().isEmpty()) {
            sql.append(" AND s.city = ?");
            params.add(criteria.getCity());
        }
        if (criteria.getState() != null && !criteria.getState().isEmpty()) {
            sql.append(" AND s.state = ?");
            params.add(criteria.getState());
        }

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
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private Animal mapResultSetToAnimal(ResultSet rs) throws SQLException {
        Animal animal = mapResultSetToAnimalBasic(rs);

        // Map shelter information if available
        try {
            Shelter shelter = new Shelter();
            shelter.setShelterName(rs.getString("shelter_name"));
            shelter.setCity(rs.getString("shelter_city"));
            shelter.setState(rs.getString("shelter_state"));
            shelter.setPhone(rs.getString("shelter_phone"));
            shelter.setEmail(rs.getString("shelter_email"));
            animal.setShelter(shelter);
        } catch (SQLException e) {
            // Shelter columns not available
        }

        return animal;
    }

    private Animal mapResultSetToAnimalBasic(ResultSet rs) throws SQLException {
        Animal animal = new Animal();
        animal.setAnimalId(rs.getInt("animal_id"));
        animal.setShelterId(rs.getInt("shelter_id"));
        animal.setName(rs.getString("name"));
        animal.setSpecies(rs.getString("species"));
        animal.setBreed(rs.getString("breed"));
        animal.setAgeYears(rs.getInt("age_years"));
        animal.setAgeMonths(rs.getInt("age_months"));
        animal.setGender(rs.getString("gender"));
        animal.setSize(rs.getString("size"));
        animal.setColor(rs.getString("color"));
        animal.setWeightLbs(rs.getDouble("weight_lbs"));
        animal.setDescription(rs.getString("description"));
        animal.setMedicalNotes(rs.getString("medical_notes"));
        animal.setBehavioralNotes(rs.getString("behavioral_notes"));
        animal.setGoodWithKids(rs.getBoolean("good_with_kids"));
        animal.setGoodWithDogs(rs.getBoolean("good_with_dogs"));
        animal.setGoodWithCats(rs.getBoolean("good_with_cats"));
        animal.setRequiresExperiencedOwner(rs.getBoolean("requires_experienced_owner"));
        animal.setRequiresYard(rs.getBoolean("requires_yard"));
        animal.setEnergyLevel(rs.getString("energy_level"));
        animal.setStatus(rs.getString("status"));
        animal.setIntakeDate(rs.getDate("intake_date"));
        animal.setAdoptionDate(rs.getDate("adoption_date"));
        animal.setPrimaryImageUrl(rs.getString("primary_image_url"));
        animal.setCreatedAt(rs.getTimestamp("created_at"));
        animal.setUpdatedAt(rs.getTimestamp("updated_at"));
        return animal;
    }
}
