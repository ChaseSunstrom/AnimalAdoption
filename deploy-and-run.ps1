# Deploy and Run Animal Adoption App
$TOMCAT_HOME = "C:\Program Files\Apache Software Foundation\Tomcat 10.1"
$PROJECT_DIR = "C:\Projects\Java\AnimalAdoption"
$MAVEN_CMD = "C:\Users\chase\Downloads\netbeans-27-bin\netbeans\java\maven\bin\mvn.cmd"

Write-Host "=" -Repeat 60 -ForegroundColor Cyan
Write-Host "Deploying Animal Adoption System" -ForegroundColor Cyan
Write-Host "=" -Repeat 60 -ForegroundColor Cyan

# Step 1: Build (force clean with updated dependencies)
Write-Host "`n[1/4] Building WAR file (clean build with updated dependencies)..." -ForegroundColor Yellow
Set-Location $PROJECT_DIR
# Remove old compiled classes
Remove-Item -Path "target" -Recurse -Force -ErrorAction SilentlyContinue
# Clean build with dependency updates
& $MAVEN_CMD clean package -U -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "Build successful!" -ForegroundColor Green

# Step 2: Kill any process using ports 8080 and 8005
Write-Host "`n[2/4] Killing processes on ports 8080 and 8005..." -ForegroundColor Yellow

# Find and kill process on port 8080
$port8080 = netstat -ano | Select-String ":8080" | Select-String "LISTENING"
if ($port8080) {
    $processId = ($port8080 -split '\s+')[-1]
    Write-Host "Found process $processId using port 8080, killing it..." -ForegroundColor Yellow
    Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}

# Find and kill process on port 8005 (Tomcat shutdown port)
$port8005 = netstat -ano | Select-String ":8005" | Select-String "LISTENING"
if ($port8005) {
    $processId = ($port8005 -split '\s+')[-1]
    Write-Host "Found process $processId using port 8005, killing it..." -ForegroundColor Yellow
    Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}

Write-Host "Ports cleared!" -ForegroundColor Green

# Step 3: Deploy
Write-Host "`n[3/4] Deploying to Tomcat..." -ForegroundColor Yellow
Remove-Item "$TOMCAT_HOME\webapps\AnimalAdoption.war" -ErrorAction SilentlyContinue
Remove-Item "$TOMCAT_HOME\webapps\AnimalAdoption" -Recurse -Force -ErrorAction SilentlyContinue
Copy-Item "$PROJECT_DIR\target\AnimalAdoption-1.0-SNAPSHOT.war" "$TOMCAT_HOME\webapps\AnimalAdoption.war"
Write-Host "Deployed!" -ForegroundColor Green

# Step 4: Start Tomcat
Write-Host "`n[4/4] Starting Tomcat..." -ForegroundColor Yellow
Start-Process "$TOMCAT_HOME\bin\startup.bat" -WorkingDirectory "$TOMCAT_HOME\bin"
Write-Host "Tomcat starting..." -ForegroundColor Green

Write-Host "`n" -NoNewline
Write-Host "=" -Repeat 60 -ForegroundColor Cyan
Write-Host "DEPLOYMENT COMPLETE!" -ForegroundColor Green
Write-Host "=" -Repeat 60 -ForegroundColor Cyan

Write-Host "`nWaiting for Tomcat to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 8

Write-Host "`n" -NoNewline
Write-Host "Application URL: " -NoNewline -ForegroundColor Cyan
Write-Host "http://localhost:8080/AnimalAdoption/" -ForegroundColor White

Write-Host "`n" -NoNewline
Write-Host "Checking logs for errors..." -ForegroundColor Yellow
Start-Sleep -Seconds 2

# Check for errors in both catalina and localhost logs
$catalinaLog = Get-ChildItem "$TOMCAT_HOME\logs\catalina.*.log" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
$localhostLog = Get-ChildItem "$TOMCAT_HOME\logs\localhost.*.log" | Sort-Object LastWriteTime -Descending | Select-Object -First 1

$allErrors = @()
if ($catalinaLog) {
    $allErrors += Get-Content $catalinaLog.FullName -Tail 100 | Select-String "SEVERE|ERROR|Exception"
}
if ($localhostLog) {
    $allErrors += Get-Content $localhostLog.FullName -Tail 100 | Select-String "SEVERE|ERROR|Exception"
}

if ($allErrors) {
    Write-Host "`n" -NoNewline
    Write-Host "ERRORS FOUND IN LOGS:" -ForegroundColor Red
    Write-Host "-" -Repeat 60 -ForegroundColor Red
    $allErrors | Select-Object -First 15 | ForEach-Object { Write-Host $_.Line -ForegroundColor Red }
    Write-Host "`n" -NoNewline
    Write-Host "Full logs:" -ForegroundColor Yellow
    Write-Host "  Catalina: $($catalinaLog.FullName)" -ForegroundColor White
    Write-Host "  Localhost: $($localhostLog.FullName)" -ForegroundColor White
} else {
    Write-Host "No errors found! App should be running." -ForegroundColor Green
}

Write-Host "`n" -NoNewline
Write-Host "To view live logs, run:" -ForegroundColor Cyan
Write-Host "  Get-Content '$TOMCAT_HOME\logs\catalina.out' -Wait -Tail 50" -ForegroundColor White

Write-Host "`nPress any key to open the application in browser..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
Start-Process "http://localhost:8080/AnimalAdoption/"

