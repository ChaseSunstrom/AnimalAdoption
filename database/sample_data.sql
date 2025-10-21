-- Sample Data for Animal Adoption System
-- This file provides test data for development and demonstration

USE animal_adoption;

-- ============================================================================
-- Sample Users
-- ============================================================================
-- Password for all test users: password123
-- BCrypt hash: $2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu

-- Adopters
INSERT INTO users (email, password_hash, user_type, first_name, last_name, phone, is_active) VALUES
('john.doe@email.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'adopter', 'John', 'Doe', '555-0101', TRUE),
('jane.smith@email.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'adopter', 'Jane', 'Smith', '555-0102', TRUE),
('mike.johnson@email.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'adopter', 'Mike', 'Johnson', '555-0103', TRUE),
('sarah.williams@email.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'adopter', 'Sarah', 'Williams', '555-0104', TRUE),
('david.brown@email.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'adopter', 'David', 'Brown', '555-0105', TRUE);

-- Shelters
INSERT INTO users (email, password_hash, user_type, first_name, last_name, phone, is_active) VALUES
('contact@happypaws.org', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'shelter', 'Happy', 'Paws', '555-1001', TRUE),
('info@secondchance.org', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'shelter', 'Second', 'Chance', '555-1002', TRUE),
('rescue@furryfriendsrescue.org', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLHJ0NNu', 'shelter', 'Furry', 'Friends', '555-1003', TRUE);

-- ============================================================================
-- Adopter Profiles
-- ============================================================================

INSERT INTO adopter_profiles (
    user_id, address_line1, city, state, zip_code,
    home_type, home_size, has_yard, has_children, children_ages,
    has_other_pets, other_pets_description,
    experience_level, activity_level, preferences_notes
) VALUES
-- John Doe: Family with kids, experienced, active
(2, '123 Oak Street', 'Springfield', 'Illinois', '62701',
 'house', 'large', TRUE, TRUE, '8, 12',
 TRUE, 'One golden retriever named Max',
 'very_experienced', 'high', 'Looking for a dog that is good with children and other dogs'),

-- Jane Smith: Apartment dweller, first-time owner, moderate activity
(3, '456 Elm Avenue Apt 2B', 'Chicago', 'Illinois', '60601',
 'apartment', 'small', FALSE, FALSE, NULL,
 FALSE, NULL,
 'first_time', 'moderate', 'Prefer small to medium cats, quiet and affectionate'),

-- Mike Johnson: House with yard, experienced with cats
(4, '789 Maple Drive', 'Naperville', 'Illinois', '60540',
 'house', 'medium', TRUE, FALSE, NULL,
 TRUE, 'Two senior cats',
 'some_experience', 'moderate', 'Want a calm dog that gets along with cats'),

-- Sarah Williams: Active lifestyle, no kids
(5, '321 Pine Lane', 'Aurora', 'Illinois', '60505',
 'condo', 'medium', FALSE, FALSE, NULL,
 FALSE, NULL,
 'some_experience', 'high', 'Looking for an energetic dog for hiking and running'),

-- David Brown: Retired couple, experienced
(6, '654 Birch Road', 'Joliet', 'Illinois', '60435',
 'house', 'large', TRUE, FALSE, NULL,
 FALSE, NULL,
 'very_experienced', 'low', 'Prefer senior dogs or cats, calm and low maintenance');

-- ============================================================================
-- Shelter Profiles
-- ============================================================================

INSERT INTO shelters (
    user_id, shelter_name, address_line1, city, state, zip_code,
    phone, email, website, description, verified
) VALUES
(7, 'Happy Paws Animal Shelter',
 '100 Rescue Lane', 'Springfield', 'Illinois', '62702',
 '555-1001', 'contact@happypaws.org', 'www.happypaws.org',
 'A no-kill shelter serving Central Illinois since 1995. We care for dogs, cats, and small animals.',
 TRUE),

(8, 'Second Chance Animal Rescue',
 '200 Hope Street', 'Chicago', 'Illinois', '60602',
 '555-1002', 'info@secondchance.org', 'www.secondchance.org',
 'Dedicated to rescuing and rehabilitating abandoned animals throughout Chicagoland.',
 TRUE),

(9, 'Furry Friends Rescue',
 '300 Compassion Avenue', 'Naperville', 'Illinois', '60563',
 '555-1003', 'rescue@furryfriendsrescue.org', 'www.furryfriendsrescue.org',
 'Specializing in foster-based rescue for dogs and cats of all ages.',
 TRUE);

-- ============================================================================
-- Sample Animals
-- ============================================================================

-- Happy Paws Animals
INSERT INTO animals (
    shelter_id, name, species, breed, age_years, age_months, gender, size, color, weight_lbs,
    description, medical_notes, behavioral_notes,
    good_with_kids, good_with_dogs, good_with_cats,
    requires_experienced_owner, requires_yard, energy_level,
    status, intake_date, primary_image_url
) VALUES
-- Dogs
(1, 'Buddy', 'dog', 'Golden Retriever Mix', 3, 6, 'male', 'large', 'Golden', 65.5,
 'Buddy is a sweet and friendly boy who loves everyone he meets. He enjoys playing fetch and going for walks.',
 'Neutered, up to date on vaccinations',
 'Very social, knows basic commands, house-trained',
 TRUE, TRUE, TRUE, FALSE, TRUE, 'high', 'available', '2024-11-15', '/images/animals/buddy.jpg'),

(1, 'Luna', 'dog', 'Labrador Retriever', 5, 0, 'female', 'large', 'Black', 70.0,
 'Luna is a calm and gentle girl, perfect for a family. She loves to cuddle and is great with children.',
 'Spayed, heart-worm negative',
 'Excellent with kids, crate-trained, knows sit and stay',
 TRUE, TRUE, FALSE, FALSE, FALSE, 'moderate', 'available', '2024-10-20', '/images/animals/luna.jpg'),

(1, 'Max', 'dog', 'German Shepherd', 2, 3, 'male', 'large', 'Black and Tan', 75.0,
 'Max is an energetic and intelligent dog who needs an active family. He loves training and mental stimulation.',
 'Neutered, all vaccinations current',
 'High energy, needs experienced owner, leash trained',
 FALSE, FALSE, FALSE, TRUE, TRUE, 'high', 'available', '2024-12-01', '/images/animals/max.jpg'),

(1, 'Daisy', 'dog', 'Beagle', 4, 0, 'female', 'medium', 'Tri-color', 28.0,
 'Daisy is a sweet hound with a gentle temperament. She enjoys sniffing around the yard and relaxing indoors.',
 'Spayed, recently treated for ear infection (resolved)',
 'Good with everyone, moderate energy, loves treats',
 TRUE, TRUE, TRUE, FALSE, FALSE, 'moderate', 'available', '2024-11-01', '/images/animals/daisy.jpg'),

(1, 'Rocky', 'dog', 'Pit Bull Terrier Mix', 6, 0, 'male', 'large', 'Gray', 68.0,
 'Rocky is a loyal and loving companion who adores his people. He would do best as the only pet.',
 'Neutered, has mild arthritis managed with supplements',
 'Prefers to be only pet, well-behaved, gentle with people',
 TRUE, FALSE, FALSE, FALSE, FALSE, 'low', 'available', '2024-09-15', '/images/animals/rocky.jpg'),

-- Cats
(1, 'Whiskers', 'cat', 'Domestic Shorthair', 2, 0, 'male', 'medium', 'Orange Tabby', 12.0,
 'Whiskers is a playful and affectionate cat who loves attention. He purrs constantly and enjoys being petted.',
 'Neutered, FIV/FeLV negative',
 'Very social, litter box trained, loves to play with toys',
 TRUE, TRUE, TRUE, FALSE, FALSE, 'moderate', 'available', '2024-11-20', '/images/animals/whiskers.jpg'),

(1, 'Mittens', 'cat', 'Domestic Longhair', 7, 0, 'female', 'medium', 'Gray and White', 10.5,
 'Mittens is a sweet senior lady looking for a quiet home. She enjoys napping in sunny spots and gentle affection.',
 'Spayed, mild hyperthyroidism on medication',
 'Calm, independent, prefers quiet environment',
 TRUE, FALSE, TRUE, FALSE, FALSE, 'low', 'available', '2024-10-05', '/images/animals/mittens.jpg'),

-- Second Chance Animals
(2, 'Charlie', 'dog', 'Corgi Mix', 3, 0, 'male', 'small', 'Brown and White', 22.0,
 'Charlie is an adorable little guy with a big personality. He loves to play and is always ready for an adventure.',
 'Neutered, vaccinations current',
 'Friendly, energetic, good with most dogs',
 TRUE, TRUE, FALSE, FALSE, FALSE, 'high', 'available', '2024-12-10', '/images/animals/charlie.jpg'),

(2, 'Bella', 'dog', 'Chihuahua', 5, 6, 'female', 'small', 'Tan', 6.5,
 'Bella is a tiny sweetheart who prefers a calm household. She loves to snuggle and be close to her person.',
 'Spayed, dental cleaning completed',
 'Can be shy at first, best as only pet, house-trained',
 FALSE, FALSE, FALSE, FALSE, FALSE, 'low', 'available', '2024-11-05', '/images/animals/bella.jpg'),

(2, 'Shadow', 'cat', 'Domestic Shorthair', 1, 6, 'male', 'medium', 'Black', 9.0,
 'Shadow is a playful kitten full of energy. He loves chasing toys and exploring every corner of the house.',
 'Neutered, all kitten vaccinations complete',
 'Very playful, social, good with other cats',
 TRUE, FALSE, TRUE, FALSE, FALSE, 'high', 'available', '2024-12-01', '/images/animals/shadow.jpg'),

(2, 'Snowball', 'cat', 'Persian Mix', 4, 0, 'female', 'medium', 'White', 11.0,
 'Snowball is a beautiful and gentle cat. She requires regular grooming due to her long coat.',
 'Spayed, requires regular grooming',
 'Sweet and calm, enjoys being brushed',
 TRUE, FALSE, TRUE, FALSE, FALSE, 'low', 'available', '2024-10-15', '/images/animals/snowball.jpg'),

-- Furry Friends Animals
(3, 'Zeus', 'dog', 'Great Dane', 4, 0, 'male', 'extra_large', 'Black', 140.0,
 'Zeus is a gentle giant who thinks he is a lap dog. Despite his size, he is calm and well-mannered.',
 'Neutered, healthy',
 'Calm, house-trained, needs large home',
 TRUE, TRUE, FALSE, FALSE, TRUE, 'low', 'available', '2024-11-25', '/images/animals/zeus.jpg'),

(3, 'Peanut', 'dog', 'Dachshund', 8, 0, 'male', 'small', 'Brown', 14.0,
 'Peanut is a senior gentleman looking for a loving retirement home. He enjoys short walks and lots of naps.',
 'Neutered, has mild back issues (common for breed)',
 'Senior dog, very mellow, house-trained',
 TRUE, TRUE, FALSE, FALSE, FALSE, 'low', 'available', '2024-09-30', '/images/animals/peanut.jpg'),

(3, 'Ginger', 'cat', 'Domestic Shorthair', 3, 0, 'female', 'medium', 'Orange', 9.5,
 'Ginger is a sweet and independent cat. She enjoys playtime but also values her alone time.',
 'Spayed, healthy',
 'Independent, litter trained, moderately active',
 TRUE, FALSE, TRUE, FALSE, FALSE, 'moderate', 'available', '2024-11-10', '/images/animals/ginger.jpg'),

(3, 'Oliver', 'cat', 'Siamese Mix', 2, 0, 'male', 'medium', 'Seal Point', 10.0,
 'Oliver is a talkative and social cat who loves interaction. He will follow you around and "help" with everything.',
 'Neutered, FIV/FeLV negative',
 'Very vocal, social, needs attention and interaction',
 TRUE, FALSE, TRUE, FALSE, FALSE, 'high', 'available', '2024-12-05', '/images/animals/oliver.jpg'),

(3, 'Smokey', 'rabbit', 'Holland Lop', 0, 8, 'male', 'small', 'Gray', 3.5,
 'Smokey is an adorable bunny with soft gray fur. He is litter trained and loves fresh vegetables.',
 'Neutered, healthy',
 'Gentle, litter trained, needs daily exercise outside cage',
 TRUE, FALSE, FALSE, FALSE, FALSE, 'moderate', 'available', '2024-11-15', '/images/animals/smokey.jpg');

-- ============================================================================
-- Sample Adoption Applications
-- ============================================================================

INSERT INTO adoption_applications (
    animal_id, adopter_id, shelter_id, status,
    why_adopt, previous_pet_experience,
    veterinarian_name, veterinarian_phone,
    reference1_name, reference1_phone, reference1_relationship,
    reference2_name, reference2_phone, reference2_relationship,
    submitted_at
) VALUES
-- John Doe applying for Buddy (good match)
(1, 2, 1, 'submitted',
 'We are looking for a friendly dog to join our family. Our kids would love a playful companion, and we have a large yard.',
 'I have owned dogs my entire life, including golden retrievers. Currently have a 6-year-old golden retriever.',
 'Dr. Smith', '555-2001',
 'Tom Anderson', '555-2101', 'Neighbor',
 'Lisa Chen', '555-2102', 'Coworker',
 NOW() - INTERVAL 2 DAY),

-- Jane Smith applied for Whiskers (approved)
(6, 3, 1, 'approved',
 'I have always wanted a cat. My apartment allows pets and I work from home, so I can give lots of attention.',
 'This will be my first pet, but I have done extensive research on cat care.',
 'Dr. Johnson', '555-2002',
 'Emily White', '555-2103', 'Friend',
 'Mark Davis', '555-2104', 'Friend',
 NOW() - INTERVAL 5 DAY),

-- Sarah Williams applying for Charlie (under review)
(8, 5, 2, 'under_review',
 'I am very active and looking for a companion for my daily runs and weekend hikes. Charlie seems perfect!',
 'I had a border collie growing up and have experience with high-energy dogs.',
 'Dr. Martinez', '555-2003',
 'Jennifer Lopez', '555-2105', 'Running partner',
 'Robert King', '555-2106', 'Coworker',
 NOW() - INTERVAL 1 DAY),

-- David Brown applying for Peanut (good match - senior for senior)
(13, 6, 3, 'submitted',
 'We are retired and looking for a calm companion. Peanut would be perfect for our lifestyle.',
 'We have had dogs throughout our marriage, including dachshunds. Very familiar with senior dog care.',
 'Dr. Wilson', '555-2004',
 'Nancy Thompson', '555-2107', 'Friend',
 'George Miller', '555-2108', 'Neighbor',
 NOW() - INTERVAL 3 DAY);

-- ============================================================================
-- Saved Animals (Favorites)
-- ============================================================================

INSERT INTO saved_animals (user_id, animal_id, notes) VALUES
(2, 4, 'Daisy seems great for kids too'),
(2, 6, 'Beautiful cat, kids might like'),
(3, 10, 'Shadow is adorable!'),
(3, 11, 'Snowball is so pretty'),
(4, 2, 'Luna looks calm enough for my cats'),
(5, 1, 'Buddy seems very energetic'),
(6, 5, 'Rocky might be a good senior dog'),
(6, 7, 'Mittens is beautiful and calm');

-- ============================================================================
-- Sample Match Scores (Pre-calculated recommendations)
-- ============================================================================

-- Note: In production, these would be calculated by the RecommendationService
-- These are sample scores to demonstrate the matching system

INSERT INTO match_scores (adopter_id, animal_id, match_score, score_details) VALUES
-- John Doe (experienced, active, kids, has dog, large house with yard)
(2, 1, 95.0, '{"compatibility": 100, "home_size": 100, "activity": 100, "experience": 100, "location": 100}'), -- Buddy - perfect match
(2, 2, 90.0, '{"compatibility": 100, "home_size": 100, "activity": 85, "experience": 100, "location": 100}'),  -- Luna - great match
(2, 4, 92.0, '{"compatibility": 100, "home_size": 100, "activity": 90, "experience": 100, "location": 100}'),  -- Daisy - great match
(2, 6, 88.0, '{"compatibility": 100, "home_size": 100, "activity": 80, "experience": 100, "location": 100}'),  -- Whiskers

-- Jane Smith (first-time, moderate activity, small apartment, no yard, no pets, no kids)
(3, 6, 85.0, '{"compatibility": 100, "home_size": 90, "activity": 85, "experience": 80, "location": 100}'),  -- Whiskers - good match
(3, 7, 92.0, '{"compatibility": 100, "home_size": 95, "activity": 100, "experience": 85, "location": 100}'), -- Mittens - perfect for her
(3, 9, 65.0, '{"compatibility": 80, "home_size": 70, "activity": 60, "experience": 60, "location": 100}'),   -- Bella - needs more experience
(3, 11, 88.0, '{"compatibility": 100, "home_size": 90, "activity": 95, "experience": 75, "location": 100}'), -- Snowball

-- Mike Johnson (some experience, moderate activity, medium house with yard, has cats)
(4, 2, 90.0, '{"compatibility": 100, "home_size": 95, "activity": 90, "experience": 85, "location": 75}'),  -- Luna - good with cats
(4, 4, 92.0, '{"compatibility": 100, "home_size": 95, "activity": 95, "experience": 85, "location": 75}'),  -- Daisy
(4, 5, 88.0, '{"compatibility": 80, "home_size": 95, "activity": 100, "experience": 85, "location": 75}'),  -- Rocky - not good with cats but low energy

-- Sarah Williams (some experience, high activity, medium condo, no yard, no pets, no kids)
(5, 1, 85.0, '{"compatibility": 85, "home_size": 85, "activity": 100, "experience": 80, "location": 75}'),  -- Buddy - needs yard
(5, 3, 75.0, '{"compatibility": 70, "home_size": 80, "activity": 100, "experience": 100, "location": 100}'), -- Max - needs experienced owner
(5, 8, 92.0, '{"compatibility": 100, "home_size": 95, "activity": 100, "experience": 85, "location": 100}'), -- Charlie - perfect

-- David Brown (very experienced, low activity, large house with yard, no pets, no kids)
(6, 5, 95.0, '{"compatibility": 100, "home_size": 100, "activity": 100, "experience": 100, "location": 75}'), -- Rocky - perfect senior dog
(6, 7, 98.0, '{"compatibility": 100, "home_size": 100, "activity": 100, "experience": 100, "location": 100}'), -- Mittens - perfect senior cat
(6, 13, 97.0, '{"compatibility": 100, "home_size": 100, "activity": 100, "experience": 100, "location": 75}'), -- Peanut - perfect senior dog
(6, 12, 88.0, '{"compatibility": 100, "home_size": 100, "activity": 85, "experience": 100, "location": 75}'); -- Zeus - calm giant

-- ============================================================================
-- End of sample_data.sql
-- ============================================================================
