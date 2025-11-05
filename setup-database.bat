@echo off
echo ================================================
echo Setting up Animal Adoption Database
echo ================================================

set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe
if not exist "%MYSQL_PATH%" (
    echo ERROR: MySQL not found at %MYSQL_PATH%
    echo Please update the path in this script
    pause
    exit /b 1
)

echo.
echo Creating database...
"%MYSQL_PATH%" -u root -p1234 -e "CREATE DATABASE IF NOT EXISTS animal_adoption CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

echo.
echo Creating tables...
"%MYSQL_PATH%" -u root -p1234 animal_adoption < database\schema.sql

echo.
echo Loading sample data...
"%MYSQL_PATH%" -u root -p1234 animal_adoption < database\sample_data.sql

echo.
echo ================================================
echo Database setup complete!
echo ================================================
echo.
echo You can now register new users and browse animals.
echo Default login credentials are in README.md
echo.
pause




