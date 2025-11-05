# Script to find and display recent Tomcat errors
Write-Host "Searching for Tomcat logs..." -ForegroundColor Cyan

# Common Tomcat locations
$possiblePaths = @(
    "$env:USERPROFILE\AppData\Local\NetBeans\Cache\*\tomcat*\logs",
    "$env:USERPROFILE\AppData\Roaming\NetBeans\*\tomcat*\logs",
    "$env:USERPROFILE\Downloads\*tomcat*\logs",
    "C:\apache-tomcat*\logs",
    "C:\Program Files\Apache*Tomcat*\logs"
)

$logFiles = @()
foreach ($path in $possiblePaths) {
    $found = Get-ChildItem -Path $path -Filter "*.log" -ErrorAction SilentlyContinue | 
             Where-Object { $_.LastWriteTime -gt (Get-Date).AddMinutes(-30) }
    if ($found) {
        $logFiles += $found
    }
}

if ($logFiles.Count -eq 0) {
    Write-Host "No recent Tomcat log files found." -ForegroundColor Yellow
    Write-Host "`nPlease check NetBeans output window for errors." -ForegroundColor Yellow
    exit
}

Write-Host "Found $($logFiles.Count) recent log file(s)" -ForegroundColor Green

foreach ($log in $logFiles | Sort-Object LastWriteTime -Descending | Select-Object -First 3) {
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host "File: $($log.FullName)" -ForegroundColor Cyan
    Write-Host "Modified: $($log.LastWriteTime)" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    
    # Get last 100 lines and filter for errors
    $content = Get-Content $log.FullName -Tail 100 -ErrorAction SilentlyContinue
    
    $errorLines = $content | Select-String -Pattern "SEVERE|ERROR|Exception|Failed|Cannot" -Context 3,3
    
    if ($errorLines) {
        Write-Host "`nERRORS FOUND:" -ForegroundColor Red
        $errorLines | ForEach-Object { 
            Write-Host $_.Line -ForegroundColor White
        }
    } else {
        Write-Host "No obvious errors in this log file" -ForegroundColor Green
    }
}

Write-Host "`n`nTo see full logs, check:" -ForegroundColor Yellow
$logFiles | Select-Object -First 1 | ForEach-Object {
    Write-Host $_.DirectoryName -ForegroundColor Cyan
}

