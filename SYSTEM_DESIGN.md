# Animal Adoption System - Design Document

## 1. System Overview

### Purpose
A web-based animal adoption platform that connects potential adopters with shelters, featuring intelligent matching based on user preferences and pet characteristics.

### Technology Stack
- **Backend**: Java 8+, Apache Tomcat 9+
- **Web Framework**: JSP/Servlets
- **Database**: MySQL 8.0+
- **Frontend**: HTML5, CSS3, JavaScript (Bootstrap for responsive design)
- **Connection Pooling**: Apache DBCP2 or HikariCP

---

## 2. System Architecture

### Architecture Pattern
**MVC (Model-View-Controller)** with layered architecture:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│     (JSP Pages + JavaScript)            │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Controller Layer                │
│          (Servlets)                     │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Service Layer                   │
│      (Business Logic)                   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Data Access Layer               │
│           (DAO Pattern)                 │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Database Layer                  │
│            (MySQL)                      │
└─────────────────────────────────────────┘
```

### Key Components

#### User Types
1. **Adopters**: Browse and search for pets, create profiles, submit applications
2. **Shelter Staff**: Manage animal listings, review applications, update animal status
3. **Administrators**: System-wide management, user management, reports

---

## 3. Database Schema

### Core Tables

#### users
```sql
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
);
```

#### adopter_profiles
```sql
CREATE TABLE adopter_profiles (
    profile_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    address_line1 VARCHAR(100),
    address_line2 VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(50),
    zip_code VARCHAR(10),
    home_type ENUM('apartment', 'house', 'condo', 'townhouse', 'other') NOT NULL,
    home_size ENUM('small', 'medium', 'large') NOT NULL,
    has_yard BOOLEAN DEFAULT FALSE,
    has_children BOOLEAN DEFAULT FALSE,
    children_ages VARCHAR(100), -- comma-separated ages
    has_other_pets BOOLEAN DEFAULT FALSE,
    other_pets_description TEXT,
    experience_level ENUM('first_time', 'some_experience', 'very_experienced') NOT NULL,
    activity_level ENUM('low', 'moderate', 'high') NOT NULL,
    preferences_notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

#### shelters
```sql
CREATE TABLE shelters (
    shelter_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    shelter_name VARCHAR(100) NOT NULL,
    address_line1 VARCHAR(100) NOT NULL,
    address_line2 VARCHAR(100),
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    website VARCHAR(200),
    description TEXT,
    verified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_city_state (city, state)
);
```

#### animals
```sql
CREATE TABLE animals (
    animal_id INT PRIMARY KEY AUTO_INCREMENT,
    shelter_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    species ENUM('dog', 'cat', 'rabbit', 'bird', 'other') NOT NULL,
    breed VARCHAR(100),
    age_years INT,
    age_months INT,
    gender ENUM('male', 'female', 'unknown') NOT NULL,
    size ENUM('small', 'medium', 'large', 'extra_large') NOT NULL,
    color VARCHAR(50),
    weight_lbs DECIMAL(5,2),
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

    -- Status
    status ENUM('available', 'pending', 'adopted', 'not_available') DEFAULT 'available',
    intake_date DATE NOT NULL,
    adoption_date DATE NULL,

    -- Images
    primary_image_url VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id) ON DELETE CASCADE,
    INDEX idx_species_status (species, status),
    INDEX idx_shelter_status (shelter_id, status),
    INDEX idx_size_energy (size, energy_level)
);
```

#### animal_images
```sql
CREATE TABLE animal_images (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    animal_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    caption VARCHAR(200),
    display_order INT DEFAULT 0,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (animal_id) REFERENCES animals(animal_id) ON DELETE CASCADE,
    INDEX idx_animal_id (animal_id)
);
```

#### adoption_applications
```sql
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

    shelter_notes TEXT,

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
);
```

#### saved_animals (Favorites/Watchlist)
```sql
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
);
```

#### match_scores (Cached recommendation scores)
```sql
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
    INDEX idx_adopter_score (adopter_id, match_score DESC)
);
```

---

## 4. Java Package Structure

```
com.animaladoption
├── controller
│   ├── AuthServlet.java
│   ├── UserProfileServlet.java
│   ├── AnimalSearchServlet.java
│   ├── AnimalDetailServlet.java
│   ├── ApplicationServlet.java
│   ├── ShelterManagementServlet.java
│   └── RecommendationServlet.java
├── model
│   ├── User.java
│   ├── AdopterProfile.java
│   ├── Shelter.java
│   ├── Animal.java
│   ├── AnimalImage.java
│   ├── AdoptionApplication.java
│   ├── SavedAnimal.java
│   └── MatchScore.java
├── dao
│   ├── BaseDAO.java
│   ├── UserDAO.java
│   ├── AdopterProfileDAO.java
│   ├── ShelterDAO.java
│   ├── AnimalDAO.java
│   ├── ApplicationDAO.java
│   └── MatchScoreDAO.java
├── service
│   ├── AuthenticationService.java
│   ├── UserService.java
│   ├── AnimalService.java
│   ├── SearchService.java
│   ├── RecommendationService.java
│   ├── ApplicationService.java
│   └── EmailService.java
├── util
│   ├── DatabaseUtil.java
│   ├── PasswordUtil.java
│   ├── ValidationUtil.java
│   └── DateUtil.java
└── filter
    ├── AuthenticationFilter.java
    ├── CharacterEncodingFilter.java
    └── CORSFilter.java
```

---

## 5. Key Features & Workflows

### 5.1 User Authentication & Registration

**Registration Flow**:
1. User selects account type (Adopter/Shelter)
2. Provides basic information (email, password, name)
3. Password is hashed using BCrypt
4. Email verification sent (optional enhancement)
5. Redirect to profile completion

**Login Flow**:
1. User enters email/password
2. System validates credentials
3. Session created with user information
4. Redirect based on user type

**Classes Involved**:
- `AuthServlet.java`
- `AuthenticationService.java`
- `UserDAO.java`
- `PasswordUtil.java` (BCrypt hashing)

### 5.2 Adopter Profile Management

**Profile Creation/Update**:
- Home details (type, size, yard)
- Household composition (children, other pets)
- Experience level and activity preferences
- Special requirements or notes

**Form Validation**:
- Required fields enforcement
- Data type validation
- Logical consistency checks

### 5.3 Animal Listing (Shelter Functionality)

**Adding Animals**:
1. Shelter staff fills out animal information form
2. Upload primary image and additional images
3. Set compatibility flags (kids, dogs, cats)
4. Define energy level and special requirements
5. Submit and set status to 'available'

**Managing Animals**:
- Edit animal details
- Update status (available, pending, adopted)
- Add behavioral/medical notes
- Delete listings

**Classes Involved**:
- `ShelterManagementServlet.java`
- `AnimalService.java`
- `AnimalDAO.java`

### 5.4 Animal Search & Filtering

**Search Criteria**:
- Species (dog, cat, etc.)
- Breed (text search or dropdown)
- Size (small, medium, large, extra_large)
- Color
- Age range
- Gender
- Location (city, state, zip code radius)
- Compatibility filters:
  - Good with kids
  - Good with dogs
  - Good with cats
  - Energy level

**Search Implementation**:
```java
// Dynamic SQL query building based on filters
SELECT a.* FROM animals a
JOIN shelters s ON a.shelter_id = s.shelter_id
WHERE a.status = 'available'
  AND a.species = ?
  [AND a.size = ?]
  [AND a.good_with_kids = TRUE]
  [AND a.good_with_dogs = TRUE]
  [AND a.energy_level IN (?)]
  [AND s.city = ? OR s.state = ?]
ORDER BY a.created_at DESC
LIMIT ? OFFSET ?
```

**Pagination**:
- Page size: 12 animals per page
- Efficient offset-based pagination
- Total count for page navigation

**Classes Involved**:
- `AnimalSearchServlet.java`
- `SearchService.java`
- `AnimalDAO.java`

### 5.5 Recommendation Engine

**Matching Algorithm**:

The recommendation system calculates a match score (0-100) based on compatibility factors:

```
Match Score Components:
1. Compatibility Requirements (40% weight)
   - Has kids → Requires good_with_kids
   - Has dogs → Requires good_with_dogs
   - Has cats → Requires good_with_cats
   - Small home → Penalty for requires_yard
   - First-time owner → Penalty for requires_experienced_owner

2. Home Size Match (20% weight)
   - Small home: Small/Medium animals preferred
   - Medium home: Any size acceptable
   - Large home: Any size, bonus for large animals

3. Activity Level Match (20% weight)
   - Low activity adopter: Low/Moderate energy pets
   - Moderate activity: Any energy level
   - High activity: Moderate/High energy pets

4. Experience Match (10% weight)
   - First-time: Bonus for easy animals
   - Experienced: Can handle all

5. Location Proximity (10% weight)
   - Same city: 100%
   - Same state: 75%
   - Different state: 50%
```

**Implementation**:

```java
public class RecommendationService {

    public List<MatchScore> calculateMatches(AdopterProfile profile, List<Animal> animals) {
        List<MatchScore> scores = new ArrayList<>();

        for (Animal animal : animals) {
            double score = calculateMatchScore(profile, animal);
            MatchScore match = new MatchScore(profile.getUserId(), animal.getAnimalId(), score);
            scores.add(match);
        }

        scores.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        return scores;
    }

    private double calculateMatchScore(AdopterProfile profile, Animal animal) {
        double score = 100.0;
        Map<String, Double> breakdown = new HashMap<>();

        // 1. Compatibility requirements (deal-breakers)
        if (profile.hasChildren() && !animal.isGoodWithKids()) {
            score -= 30;
            breakdown.put("kids_incompatible", -30.0);
        }
        if (profile.hasOtherPets() && containsDogs(profile) && !animal.isGoodWithDogs()) {
            score -= 30;
            breakdown.put("dogs_incompatible", -30.0);
        }
        if (profile.hasOtherPets() && containsCats(profile) && !animal.isGoodWithCats()) {
            score -= 30;
            breakdown.put("cats_incompatible", -30.0);
        }
        if (!profile.hasYard() && animal.requiresYard()) {
            score -= 20;
            breakdown.put("no_yard", -20.0);
        }
        if (profile.getExperienceLevel().equals("first_time") && animal.requiresExperiencedOwner()) {
            score -= 25;
            breakdown.put("experience_mismatch", -25.0);
        }

        // 2. Home size match
        score += calculateHomeSizeScore(profile.getHomeSize(), animal.getSize());

        // 3. Activity level match
        score += calculateActivityScore(profile.getActivityLevel(), animal.getEnergyLevel());

        // 4. Location proximity
        score += calculateLocationScore(profile, animal.getShelter());

        return Math.max(0, Math.min(100, score));
    }
}
```

**Recommendation Display**:
- Top 10 matches on dashboard
- Match percentage displayed
- "Why this match?" tooltip with breakdown
- Filter by minimum match score

**Classes Involved**:
- `RecommendationServlet.java`
- `RecommendationService.java`
- `MatchScoreDAO.java`

### 5.6 Adoption Application Workflow

**Application Submission**:
1. Adopter views animal detail page
2. Clicks "Apply to Adopt"
3. Fills out application form:
   - Why they want to adopt
   - Previous pet experience
   - Veterinarian information
   - Two personal references
4. Submits application

**Application Review (Shelter Staff)**:
1. View list of pending applications
2. Review adopter profile and application details
3. Contact references if needed
4. Approve or reject with notes
5. System sends notification email to adopter

**Application States**:
- `submitted`: Initial state
- `under_review`: Shelter is reviewing
- `approved`: Ready for adoption
- `rejected`: Not suitable
- `withdrawn`: Adopter cancelled

**Classes Involved**:
- `ApplicationServlet.java`
- `ApplicationService.java`
- `ApplicationDAO.java`
- `EmailService.java`

---

## 6. Security Considerations

### Authentication
- Password hashing with BCrypt (salt rounds: 12)
- Session-based authentication
- Session timeout: 30 minutes of inactivity
- Secure session cookies (HttpOnly, Secure flags)

### Authorization
- Role-based access control
- Servlet filters for protected resources
- Adopters: Can only view their own profiles/applications
- Shelters: Can only manage their own animals/applications
- Admins: Full system access

### Input Validation
- Server-side validation for all inputs
- Prepared statements to prevent SQL injection
- XSS prevention in JSP pages (JSTL escaping)
- File upload restrictions (type, size)

### SQL Injection Prevention
```java
// Always use PreparedStatement
PreparedStatement pstmt = connection.prepareStatement(
    "SELECT * FROM animals WHERE species = ? AND size = ?");
pstmt.setString(1, species);
pstmt.setString(2, size);
```

---

## 7. JSP Page Structure

### Public Pages
- `/index.jsp` - Homepage with search
- `/search.jsp` - Animal search results
- `/animal-detail.jsp` - Individual animal details
- `/about.jsp` - About the platform
- `/login.jsp` - Login page
- `/register.jsp` - Registration page

### Adopter Pages
- `/adopter/dashboard.jsp` - Dashboard with recommendations
- `/adopter/profile.jsp` - Profile management
- `/adopter/saved-animals.jsp` - Favorites list
- `/adopter/applications.jsp` - Application history
- `/adopter/apply.jsp` - Application form

### Shelter Pages
- `/shelter/dashboard.jsp` - Application queue
- `/shelter/animals.jsp` - Animal listings management
- `/shelter/add-animal.jsp` - Add new animal
- `/shelter/edit-animal.jsp` - Edit animal details
- `/shelter/applications.jsp` - Review applications
- `/shelter/profile.jsp` - Shelter profile

### Admin Pages
- `/admin/dashboard.jsp` - System overview
- `/admin/users.jsp` - User management
- `/admin/shelters.jsp` - Shelter verification
- `/admin/reports.jsp` - System reports

---

## 8. Configuration Files

### web.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <display-name>Animal Adoption System</display-name>

    <!-- Context Parameters -->
    <context-param>
        <param-name>db.url</param-name>
        <param-value>jdbc:mysql://localhost:3306/animal_adoption</param-value>
    </context-param>
    <context-param>
        <param-name>db.username</param-name>
        <param-value>adoption_user</param-value>
    </context-param>
    <context-param>
        <param-name>db.password</param-name>
        <param-value>secure_password</param-value>
    </context-param>

    <!-- Filters -->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>com.animaladoption.filter.CharacterEncodingFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <filter>
        <filter-name>AuthenticationFilter</filter-name>
        <filter-class>com.animaladoption.filter.AuthenticationFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>AuthenticationFilter</filter-name>
        <url-pattern>/adopter/*</url-pattern>
        <url-pattern>/shelter/*</url-pattern>
        <url-pattern>/admin/*</url-pattern>
    </filter-mapping>

    <!-- Servlets -->
    <servlet>
        <servlet-name>AuthServlet</servlet-name>
        <servlet-class>com.animaladoption.controller.AuthServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>AuthServlet</servlet-name>
        <url-pattern>/auth/*</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>AnimalSearchServlet</servlet-name>
        <servlet-class>com.animaladoption.controller.AnimalSearchServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>AnimalSearchServlet</servlet-name>
        <url-pattern>/search</url-pattern>
    </servlet-mapping>

    <!-- Session Configuration -->
    <session-config>
        <session-timeout>30</session-timeout>
        <cookie-config>
            <http-only>true</http-only>
            <secure>false</secure> <!-- Set to true in production with HTTPS -->
        </cookie-config>
    </session-config>

    <!-- Welcome Files -->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

    <!-- Error Pages -->
    <error-page>
        <error-code>404</error-code>
        <location>/error/404.jsp</location>
    </error-page>
    <error-page>
        <error-code>500</error-code>
        <location>/error/500.jsp</location>
    </error-page>
</web-app>
```

### context.xml (Tomcat connection pooling)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context>
    <Resource name="jdbc/AnimalAdoptionDB"
              auth="Container"
              type="javax.sql.DataSource"
              maxTotal="20"
              maxIdle="10"
              maxWaitMillis="10000"
              username="adoption_user"
              password="secure_password"
              driverClassName="com.mysql.cj.jdbc.Driver"
              url="jdbc:mysql://localhost:3306/animal_adoption?useSSL=false&amp;serverTimezone=UTC"/>
</Context>
```

---

## 9. Key Service Classes

### AuthenticationService.java
```java
public class AuthenticationService {
    private UserDAO userDAO;

    public User login(String email, String password) throws AuthenticationException;
    public User register(User user, String password, String userType) throws RegistrationException;
    public void logout(HttpSession session);
    public boolean validateSession(HttpSession session);
    public void updateLastLogin(int userId);
}
```

### SearchService.java
```java
public class SearchService {
    private AnimalDAO animalDAO;

    public SearchResult searchAnimals(SearchCriteria criteria, int page, int pageSize);
    public Animal getAnimalById(int animalId);
    public List<String> getUniqueBreeds(String species);
    public Map<String, Integer> getAnimalCountBySpecies();
}

public class SearchCriteria {
    private String species;
    private String breed;
    private String size;
    private String color;
    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private Boolean goodWithKids;
    private Boolean goodWithDogs;
    private Boolean goodWithCats;
    private List<String> energyLevels;
    private String city;
    private String state;
    private Integer zipCode;
    private Integer radiusMiles;
    // Getters and setters
}
```

### RecommendationService.java
```java
public class RecommendationService {
    private AnimalDAO animalDAO;
    private MatchScoreDAO matchScoreDAO;
    private AdopterProfileDAO profileDAO;

    public List<MatchScore> getTopMatches(int adopterId, int limit);
    public MatchScore calculateMatchScore(AdopterProfile profile, Animal animal);
    public void refreshMatchScores(int adopterId);
    public Map<String, Double> getScoreBreakdown(int adopterId, int animalId);
}
```

### ApplicationService.java
```java
public class ApplicationService {
    private ApplicationDAO applicationDAO;
    private EmailService emailService;

    public AdoptionApplication submitApplication(AdoptionApplication application);
    public List<AdoptionApplication> getApplicationsByAdopter(int adopterId);
    public List<AdoptionApplication> getApplicationsByShelter(int shelterId);
    public void approveApplication(int applicationId, int reviewerId, String notes);
    public void rejectApplication(int applicationId, int reviewerId, String notes);
    public void withdrawApplication(int applicationId);
    public boolean hasExistingApplication(int adopterId, int animalId);
}
```

---

## 10. Implementation Phases

### Phase 1: Foundation (Week 1-2)
- [ ] Database schema creation
- [ ] Base project structure and package setup
- [ ] Database connection utility and DAO base class
- [ ] User model and UserDAO
- [ ] Authentication system (login/register/logout)
- [ ] Session management and filters
- [ ] Basic JSP templates and layout

### Phase 2: User Profiles (Week 2-3)
- [ ] Adopter profile creation and editing
- [ ] Shelter profile creation
- [ ] Profile validation utilities
- [ ] User dashboard pages

### Phase 3: Animal Management (Week 3-4)
- [ ] Animal model and AnimalDAO
- [ ] Animal listing creation (shelter)
- [ ] Animal image upload and storage
- [ ] Animal editing and status management
- [ ] Animal detail page (public view)

### Phase 4: Search Functionality (Week 4-5)
- [ ] Search criteria form
- [ ] Dynamic search query building
- [ ] Search results page with pagination
- [ ] Filter combinations and sorting
- [ ] Saved animals (favorites) feature

### Phase 5: Recommendation Engine (Week 5-6)
- [ ] Matching algorithm implementation
- [ ] Match score calculation and storage
- [ ] Recommendation display on dashboard
- [ ] Match explanation tooltips
- [ ] Score recalculation triggers

### Phase 6: Adoption Applications (Week 6-7)
- [ ] Application form and submission
- [ ] Application review interface (shelter)
- [ ] Application status workflow
- [ ] Email notifications
- [ ] Application history views

### Phase 7: Polish & Enhancements (Week 7-8)
- [ ] Responsive design refinement
- [ ] Form validation improvements
- [ ] Error handling and user feedback
- [ ] Performance optimization
- [ ] Admin dashboard and reports
- [ ] Testing and bug fixes

---

## 11. Testing Strategy

### Unit Testing
- Service layer methods
- Matching algorithm calculations
- Validation utilities
- Password hashing

### Integration Testing
- DAO operations with test database
- Servlet request/response handling
- Authentication flow
- Application workflow

### User Acceptance Testing
- End-to-end user journeys
- Cross-browser compatibility
- Mobile responsiveness
- Performance under load

---

## 12. Future Enhancements

### Short-term
- Email verification on registration
- Password reset functionality
- Advanced image gallery for animals
- Real-time notifications
- Application messaging system

### Medium-term
- Adoption fee payment integration
- Adoption contract generation
- Foster program support
- Volunteer management
- Donation system

### Long-term
- Mobile app (Android/iOS)
- Social media integration
- Animal behavior assessments
- Success story sharing
- Multi-language support
- Analytics dashboard

---

## 13. Performance Considerations

### Database Optimization
- Proper indexing on frequently queried columns
- Query optimization for search
- Connection pooling (HikariCP recommended)
- Caching frequently accessed data (e.g., breeds list)

### Application Performance
- Lazy loading of images
- Pagination for large result sets
- Asynchronous email sending
- Session management efficiency
- Static resource caching (CSS, JS, images)

### Scalability
- Stateless servlet design
- Database read replicas for search queries
- CDN for static assets and animal images
- Load balancing ready architecture

---

## End of Design Document

This design provides a comprehensive foundation for building a production-ready animal adoption system using Java, JSP/Servlets, and MySQL.
