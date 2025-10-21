# Animal Adoption System

A comprehensive web-based animal adoption platform built with Java, JSP/Servlets, and MySQL. This system connects potential adopters with animal shelters, featuring intelligent pet matching based on user profiles and animal characteristics.

## Features

### For Adopters
- Create detailed profiles with home information, lifestyle preferences, and experience level
- Search available animals with advanced filtering (species, breed, size, color, compatibility)
- Intelligent recommendation system that matches your profile to compatible pets
- Save favorite animals to a watchlist
- Submit adoption applications online
- Track application status in real-time

### For Shelters
- Manage animal listings with detailed information
- Upload and manage animal photos
- Review and process adoption applications
- Update animal status (available, pending, adopted)
- Track application history

### For Administrators
- User account management
- Shelter verification system
- System-wide reporting and analytics

## Technology Stack

- **Backend**: Java 8+
- **Web Server**: Apache Tomcat 9+
- **Web Framework**: JSP/Servlets (MVC Architecture)
- **Database**: MySQL 8.0+
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 4/5
- **Security**: BCrypt password hashing, session-based authentication
- **Connection Pooling**: Apache DBCP2 or HikariCP

## Project Structure

```
AnimalAdoption/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── animaladoption/
│       │           ├── controller/      # Servlets
│       │           ├── model/          # Entity classes
│       │           ├── dao/            # Data Access Objects
│       │           ├── service/        # Business logic
│       │           ├── util/           # Utility classes
│       │           └── filter/         # Servlet filters
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml            # Deployment descriptor
│           │   └── lib/               # JAR dependencies
│           ├── css/                   # Stylesheets
│           ├── js/                    # JavaScript files
│           ├── images/                # Static images
│           ├── adopter/               # Adopter JSP pages
│           ├── shelter/               # Shelter JSP pages
│           ├── admin/                 # Admin JSP pages
│           ├── index.jsp              # Homepage
│           ├── search.jsp             # Search page
│           └── login.jsp              # Login page
├── database/
│   ├── schema.sql                     # Database schema
│   └── sample_data.sql                # Sample data for testing
├── SYSTEM_DESIGN.md                   # Detailed system design
└── README.md                          # This file
```

## Prerequisites

1. **JDK 8 or higher**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Set JAVA_HOME environment variable

2. **Apache Tomcat 9+**
   - Download from [Apache Tomcat](https://tomcat.apache.org/download-90.cgi)
   - Extract to a directory (e.g., C:\apache-tomcat-9.0.x)

3. **MySQL 8.0+**
   - Download from [MySQL Community Downloads](https://dev.mysql.com/downloads/mysql/)
   - Install and remember your root password

4. **IDE (Optional but Recommended)**
   - [NetBeans](https://netbeans.apache.org/download/index.html) with Java EE support
   - [Eclipse IDE for Enterprise Java Developers](https://www.eclipse.org/downloads/)
   - [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## Installation & Setup

### 1. Database Setup

1. **Start MySQL server**

2. **Create the database and user**
   ```sql
   -- Login to MySQL as root
   mysql -u root -p

   -- Create database
   CREATE DATABASE animal_adoption CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

   -- Create user
   CREATE USER 'adoption_user'@'localhost' IDENTIFIED BY 'your_secure_password';

   -- Grant privileges
   GRANT ALL PRIVILEGES ON animal_adoption.* TO 'adoption_user'@'localhost';
   FLUSH PRIVILEGES;

   -- Exit MySQL
   EXIT;
   ```

3. **Run the schema script**
   ```bash
   mysql -u adoption_user -p animal_adoption < database/schema.sql
   ```

4. **Load sample data (optional, for testing)**
   ```bash
   mysql -u adoption_user -p animal_adoption < database/sample_data.sql
   ```

### 2. Project Configuration

1. **Update database connection settings**

   Edit `src/main/webapp/WEB-INF/web.xml`:
   ```xml
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
       <param-value>your_secure_password</param-value>
   </context-param>
   ```

   Or edit `src/main/webapp/META-INF/context.xml` if using JNDI:
   ```xml
   <Resource name="jdbc/AnimalAdoptionDB"
             auth="Container"
             type="javax.sql.DataSource"
             username="adoption_user"
             password="your_secure_password"
             driverClassName="com.mysql.cj.jdbc.Driver"
             url="jdbc:mysql://localhost:3306/animal_adoption?useSSL=false&amp;serverTimezone=UTC"/>
   ```

2. **Add required JAR files to WEB-INF/lib/**
   - MySQL Connector/J: `mysql-connector-java-8.x.x.jar`
   - BCrypt: `jbcrypt-0.4.jar`
   - JSTL: `jstl-1.2.jar`
   - Commons DBCP2: `commons-dbcp2-2.x.x.jar` (if using DBCP)
   - Commons Pool2: `commons-pool2-2.x.x.jar` (dependency of DBCP)

### 3. Build and Deploy

#### Using NetBeans:
1. Open the project in NetBeans
2. Right-click the project → Properties → Run
3. Set the server to your Tomcat installation
4. Right-click the project → Run
5. The application will deploy and open in your browser

#### Using Command Line:
1. **Compile the Java classes**
   ```bash
   javac -d build/classes -cp "lib/*:${TOMCAT_HOME}/lib/*" src/main/java/com/animaladoption/**/*.java
   ```

2. **Create WAR file**
   ```bash
   jar -cvf AnimalAdoption.war -C src/main/webapp/ .
   ```

3. **Deploy to Tomcat**
   - Copy `AnimalAdoption.war` to `${TOMCAT_HOME}/webapps/`
   - Start Tomcat: `${TOMCAT_HOME}/bin/startup.sh` (Unix) or `startup.bat` (Windows)
   - Access the application at: `http://localhost:8080/AnimalAdoption/`

## Default Test Accounts

After loading sample data, you can use these test accounts:

### Administrator
- **Email**: admin@animaladoption.com
- **Password**: admin123
- **Note**: Change this password in production!

### Adopter Accounts
- **Email**: john.doe@email.com | **Password**: password123
- **Email**: jane.smith@email.com | **Password**: password123
- **Email**: sarah.williams@email.com | **Password**: password123

### Shelter Accounts
- **Email**: contact@happypaws.org | **Password**: password123
- **Email**: info@secondchance.org | **Password**: password123

## Key Features Explained

### Recommendation Algorithm

The system calculates match scores (0-100) based on:
- **Compatibility Requirements (40%)**: Kids, other pets, yard requirements
- **Home Size Match (20%)**: Small/medium/large home vs. animal size
- **Activity Level Match (20%)**: Adopter activity level vs. animal energy level
- **Experience Match (10%)**: First-time vs. experienced owner requirements
- **Location Proximity (10%)**: Same city, state, or different state

### Search Filters

Adopters can search for animals using:
- Species (dog, cat, rabbit, bird, other)
- Breed (text search or dropdown)
- Size (small, medium, large, extra_large)
- Color
- Age range (years and months)
- Gender
- Compatibility factors (good with kids/dogs/cats)
- Energy level (low, moderate, high)
- Location (city, state, zip code radius)

### Application Workflow

1. Adopter views animal details
2. Clicks "Apply to Adopt"
3. Fills out application form
4. Shelter receives notification
5. Shelter reviews application and profile
6. Shelter approves or rejects
7. Adopter receives notification
8. If approved, adoption is finalized

## Security Features

- **Password Security**: BCrypt hashing with 12 salt rounds
- **SQL Injection Prevention**: Prepared statements throughout
- **XSS Prevention**: JSTL escaping in all JSP pages
- **Session Security**: HttpOnly and Secure cookie flags
- **Role-Based Access Control**: Filter-based authorization
- **Input Validation**: Server-side validation for all forms

## API Endpoints (Servlet Mappings)

- `/auth/login` - POST: User login
- `/auth/register` - POST: User registration
- `/auth/logout` - GET: User logout
- `/search` - GET: Search animals
- `/animal/detail` - GET: View animal details
- `/adopter/profile` - GET/POST: View/update adopter profile
- `/adopter/applications` - GET: View applications
- `/adopter/apply` - POST: Submit application
- `/adopter/favorites` - GET/POST: Manage saved animals
- `/shelter/animals` - GET: Manage animal listings
- `/shelter/add-animal` - POST: Add new animal
- `/shelter/applications` - GET: Review applications
- `/admin/users` - GET: User management

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running: `mysql -u adoption_user -p`
- Check connection string in web.xml or context.xml
- Ensure MySQL Connector/J JAR is in WEB-INF/lib
- Check firewall settings for port 3306

### Tomcat Deployment Issues
- Check Tomcat logs: `${TOMCAT_HOME}/logs/catalina.out`
- Verify JAVA_HOME is set correctly
- Ensure port 8080 is not in use
- Clear Tomcat work directory if redeploying

### Login Issues
- Verify user exists in database
- Check password hash matches (BCrypt)
- Clear browser cookies and cache
- Check session timeout settings

## Performance Optimization

1. **Database Indexing**: Already configured in schema.sql
2. **Connection Pooling**: Use HikariCP for best performance
3. **Caching**: Implement match score caching (already in schema)
4. **Image Optimization**: Use appropriate image sizes and formats
5. **Query Optimization**: Use views for complex queries

## Future Enhancements

See `SYSTEM_DESIGN.md` Section 12 for detailed future enhancements including:
- Email verification and password reset
- Payment integration for adoption fees
- Mobile applications
- Social media integration
- Multi-language support
- Analytics dashboard

## Development Workflow

### Adding a New Feature

1. **Database**: Add tables/columns to `database/schema.sql`
2. **Model**: Create entity class in `com.animaladoption.model`
3. **DAO**: Create data access class in `com.animaladoption.dao`
4. **Service**: Create business logic in `com.animaladoption.service`
5. **Controller**: Create servlet in `com.animaladoption.controller`
6. **View**: Create JSP page in appropriate directory
7. **Testing**: Test the feature thoroughly
8. **Documentation**: Update this README and SYSTEM_DESIGN.md

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is for educational purposes. Please consult with your instructor or organization regarding usage and distribution.

## Support

For issues or questions:
1. Check `SYSTEM_DESIGN.md` for detailed architecture information
2. Review Tomcat logs for error messages
3. Verify database connection and schema
4. Contact your project administrator or instructor

## Acknowledgments

- Apache Tomcat team for the excellent servlet container
- MySQL team for the reliable database system
- Bootstrap team for the responsive CSS framework
- BCrypt library maintainers for password security

---

**Note**: This is a student/educational project. For production deployment, additional security hardening, testing, and compliance measures should be implemented.
