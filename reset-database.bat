@echo off
echo ================================================
echo Resetting Animal Adoption Database
echo ================================================

set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe

echo.
echo Dropping existing database...
"%MYSQL_PATH%" -u root -p1234 -e "DROP DATABASE IF EXISTS animal_adoption;"

echo.
echo Creating fresh database...
"%MYSQL_PATH%" -u root -p1234 -e "CREATE DATABASE animal_adoption CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

echo.
echo Creating tables...
"%MYSQL_PATH%" -u root -p1234 animal_adoption < database\schema.sql

echo.
echo Loading sample data (with random cat pictures)...
"%MYSQL_PATH%" -u root -p1234 animal_adoption < database\sample_data.sql

echo.
echo ================================================
echo Database reset complete!
echo ================================================
echo.
echo Default login credentials:
echo   Adopter: john.smith@email.com / password123
echo   Shelter: happy.paws@email.com / shelter123
echo.
pause




