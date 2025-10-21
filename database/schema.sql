-- Animal Adoption System Database Schema
-- MySQL 8.0+

-- Create database
CREATE DATABASE IF NOT EXISTS animal_adoption
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE animal_adoption;

-- Drop tables if they exist (for clean reinstall)
DROP TABLE IF EXISTS match_scores;
DROP TABLE IF EXISTS saved_animals;
DROP TABLE IF EXISTS adoption_applications;
DROP TABLE IF EXISTS animal_images;
DROP TABLE IF EXISTS animals;
DROP TABLE IF EXISTS shelters;
DROP TABLE IF EXISTS adopter_profiles;
DROP TABLE IF EXISTS users;

-- ============================================================================
-- Core Tables
-- ============================================================================

-- Users table (base authentication)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    user_type ENUM('adopter', 'shelter', 'admin') NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_user_type (user_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Adopter profiles
CREATE TABLE adopter_profiles (
    profile_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,

    -- Address information
    address_line1 VARCHAR(100),
    address_line2 VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(50),
    zip_code VARCHAR(10),

    -- Home details
    home_type ENUM('apartment', 'house', 'condo', 'townhouse', 'other') NOT NULL,
    home_size ENUM('small', 'medium', 'large') NOT NULL,
    has_yard BOOLEAN DEFAULT FALSE,

    -- Household composition
    has_children BOOLEAN DEFAULT FALSE,
    children_ages VARCHAR(100), -- comma-separated ages
    has_other_pets BOOLEAN DEFAULT FALSE,
    other_pets_description TEXT,

    -- Adopter characteristics
    experience_level ENUM('first_time', 'some_experience', 'very_experienced') NOT NULL,
    activity_level ENUM('low', 'moderate', 'high') NOT NULL,
    preferences_notes TEXT,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Shelter profiles
CREATE TABLE shelters (
    shelter_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,

    shelter_name VARCHAR(100) NOT NULL,

    -- Address
    address_line1 VARCHAR(100) NOT NULL,
    address_line2 VARCHAR(100),
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,

    -- Contact information
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    website VARCHAR(200),

    description TEXT,
    verified BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_city_state (city, state)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Animals
CREATE TABLE animals (
    animal_id INT PRIMARY KEY AUTO_INCREMENT,
    shelter_id INT NOT NULL,

    -- Basic information
    name VARCHAR(50) NOT NULL,
    species ENUM('dog', 'cat', 'rabbit', 'bird', 'other') NOT NULL,
    breed VARCHAR(100),
    age_years INT,
    age_months INT,
    gender ENUM('male', 'female', 'unknown') NOT NULL,
    size ENUM('small', 'medium', 'large', 'extra_large') NOT NULL,
    color VARCHAR(50),
    weight_lbs DECIMAL(5,2),

    -- Descriptions
    description TEXT,
    medical_notes TEXT,
    behavioral_notes TEXT,

    -- Compatibility factors
    good_with_kids BOOLEAN DEFAULT FALSE,
    good_with_dogs BOOLEAN DEFAULT FALSE,
    good_with_cats BOOLEAN DEFAULT FALSE,
    requires_experienced_owner BOOLEAN DEFAULT FALSE,
    requires_yard BOOLEAN DEFAULT FALSE,
    energy_level ENUM('low', 'moderate', 'high') NOT NULL,

    -- Status and dates
    status ENUM('available', 'pending', 'adopted', 'not_available') DEFAULT 'available',
    intake_date DATE NOT NULL,
    adoption_date DATE NULL,

    -- Primary image
    primary_image_url VARCHAR(255),

    -- Timestamps
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id) ON DELETE CASCADE,
    INDEX idx_species_status (species, status),
    INDEX idx_shelter_status (shelter_id, status),
    INDEX idx_size_energy (size, energy_level),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Animal images (additional photos)
CREATE TABLE animal_images (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    animal_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    caption VARCHAR(200),
    display_order INT DEFAULT 0,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (animal_id) REFERENCES animals(animal_id) ON DELETE CASCADE,
    INDEX idx_animal_id (animal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Adoption applications
CREATE TABLE adoption_applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    animal_id INT NOT NULL,
    adopter_id INT NOT NULL,
    shelter_id INT NOT NULL,

    status ENUM('submitted', 'under_review', 'approved', 'rejected', 'withdrawn') DEFAULT 'submitted',

    -- Application details
    why_adopt TEXT NOT NULL,
    previous_pet_experience TEXT,
    veterinarian_name VARCHAR(100),
    veterinarian_phone VARCHAR(20),

    -- References
    reference1_name VARCHAR(100),
    reference1_phone VARCHAR(20),
    reference1_relationship VARCHAR(50),
    reference2_name VARCHAR(100),
    reference2_phone VARCHAR(20),
    reference2_relationship VARCHAR(50),

    -- Review information
    shelter_notes TEXT,

    -- Timestamps
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP NULL,
    reviewed_by INT NULL,

    FOREIGN KEY (animal_id) REFERENCES animals(animal_id) ON DELETE CASCADE,
    FOREIGN KEY (adopter_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id),
    INDEX idx_adopter_status (adopter_id, status),
    INDEX idx_shelter_status (shelter_id, status),
    INDEX idx_animal_id (animal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Saved animals (favorites/watchlist)
CREATE TABLE saved_animals (
    saved_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    animal_id INT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (animal_id) REFERENCES animals(animal_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_animal (user_id, animal_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Match scores (cached recommendations)
CREATE TABLE match_scores (
    score_id INT PRIMARY KEY AUTO_INCREMENT,
    adopter_id INT NOT NULL,
    animal_id INT NOT NULL,
    match_score DECIMAL(5,2) NOT NULL, -- 0-100
    score_details JSON, -- Breakdown of scoring factors
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (adopter_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (animal_id) REFERENCES animals(animal_id) ON DELETE CASCADE,
    UNIQUE KEY unique_adopter_animal (adopter_id, animal_id),
    INDEX idx_adopter_score (adopter_id, match_score DESC),
    INDEX idx_calculated_at (calculated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- Views for common queries
-- ============================================================================

-- Available animals with shelter information
CREATE VIEW available_animals_view AS
SELECT
    a.*,
    s.shelter_name,
    s.city AS shelter_city,
    s.state AS shelter_state,
    s.phone AS shelter_phone,
    s.email AS shelter_email
FROM animals a
JOIN shelters s ON a.shelter_id = s.shelter_id
WHERE a.status = 'available';

-- Application summary view
CREATE VIEW application_summary_view AS
SELECT
    app.application_id,
    app.status,
    app.submitted_at,
    a.animal_id,
    a.name AS animal_name,
    a.species,
    u.user_id AS adopter_id,
    CONCAT(u.first_name, ' ', u.last_name) AS adopter_name,
    u.email AS adopter_email,
    s.shelter_id,
    s.shelter_name
FROM adoption_applications app
JOIN animals a ON app.animal_id = a.animal_id
JOIN users u ON app.adopter_id = u.user_id
JOIN shelters s ON app.shelter_id = s.shelter_id;

-- ============================================================================
-- Initial Data
-- ============================================================================

-- Create default admin user (password: admin123)
-- Note: In production, change this password immediately!
INSERT INTO users (email, password_hash, user_type, first_name, last_name, is_active)
VALUES ('admin@animaladoption.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'admin', 'System', 'Administrator', TRUE);

-- ============================================================================
-- End of schema.sql
-- ============================================================================
