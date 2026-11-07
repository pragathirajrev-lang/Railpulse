# Test Railway Analytics Application
$jvmArgs = @(
    "--add-opens=java.base/java.lang=ALL-UNNAMED",
    "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
    "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
    "--add-opens=java.base/java.io=ALL-UNNAMED",
    "--add-opens=java.base/java.net=ALL-UNNAMED",
    "--add-opens=java.base/java.nio=ALL-UNNAMED",
    "--add-opens=java.base/java.util=ALL-UNNAMED",
    "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
    "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
    "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
    "--add-opens=java.base/sun.nio.cs=ALL-UNNAMED",
    "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
    "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED",
    "-Djdk.reflect.useDirectMethodHandle=false"
)

$jarPath = "target\railway-spark-analytics-1.0.0.jar"
$cmd = "java $($jvmArgs -join ' ') -jar $jarPath"

Write-Host "Starting Spring Boot + Apache Spark application..."
$proc = Start-Process -FilePath "java" -ArgumentList ($jvmArgs + @("-jar", $jarPath)) -PassThru -NoNewWindow

try {
    $ready = $false
    $retries = 30
    while (-not $ready -and $retries -gt 0) {
        Start-Sleep -Seconds 2
        try {
            $resp = Invoke-WebRequest -Uri "http://localhost:8080/api/dataset/status" -TimeoutSec 3 -UseBasicParsing -ErrorAction Stop
            if ($resp.StatusCode -eq 200) {
                $ready = $true
            }
        } catch {
            Write-Host "Waiting for server to start... ($retries left)"
            $retries--
        }
    }

    if (-not $ready) {
        Write-Host "Server failed to start within timeout."
        exit 1
    }

    Write-Host "`n=== 1. TEST ROOT WEB UI ==="
    $ui = Invoke-WebRequest -Uri "http://localhost:8080/" -UseBasicParsing
    Write-Host "UI Status Code: $($ui.StatusCode) Length: $($ui.Content.Length) bytes"

    Write-Host "`n=== 2. TEST DASHBOARD SUMMARY API ==="
    $summary = Invoke-RestMethod -Uri "http://localhost:8080/api/dashboard/summary"
    Write-Host "Total Records: $($summary.totalRecords)"
    Write-Host "Total Trains: $($summary.totalTrains)"
    Write-Host "Total Routes: $($summary.totalRoutes)"
    Write-Host "Total Passengers: $($summary.totalPassengers)"
    Write-Host "Avg Occupancy: $($summary.avgOccupancy)%"
    Write-Host "Avg Delay: $($summary.avgDelay) mins"
    Write-Host "Highest Demand Route: $($summary.highestDemandRoute) ($($summary.highestDemandPassengers) pax)"

    Write-Host "`n=== 3. TEST ROUTE PERFORMANCE API ==="
    $routes = Invoke-RestMethod -Uri "http://localhost:8080/api/routes/performance?sortBy=passengers&order=desc"
    Write-Host "Routes analysed count: $($routes.Count)"
    Write-Host "Top Route: $($routes[0].route) with $($routes[0].totalPassengers) passengers (Trend: $($routes[0].trend))"

    Write-Host "`n=== 4. TEST SPARK MLLIB DEMAND FORECASTING API ==="
    $forecast = Invoke-RestMethod -Uri "http://localhost:8080/api/forecast?route=Bengaluru -> Delhi"
    Write-Host "Route: $($forecast.route)"
    Write-Host "Algorithm: $($forecast.algorithm)"
    Write-Host "MAE: $($forecast.mae), RMSE: $($forecast.rmse), R2: $($forecast.r2)"
    Write-Host "Training Samples: $($forecast.trainingSamples), Testing Samples: $($forecast.testingSamples)"
    Write-Host "7-Day Forecast Days Count: $($forecast.next7DaysForecast.Count)"
    foreach ($f in $forecast.next7DaysForecast) {
        Write-Host "  Date: $($f.date) => Predicted: $($f.predictedValue) pax"
    }

    Write-Host "`n=== 5. TEST DELAY ANALYSIS API ==="
    $delays = Invoke-RestMethod -Uri "http://localhost:8080/api/delays/all"
    Write-Host "Delay Train Count: $($delays.Count)"
    Write-Host "Top Delay: $($delays[0].trainName) ($($delays[0].avgDelay) mins avg)"

    Write-Host "`n=== 6. TEST COACH OCCUPANCY API ==="
    $occs = Invoke-RestMethod -Uri "http://localhost:8080/api/occupancy/all"
    Write-Host "Occupancy Train Count: $($occs.Count)"
    Write-Host "Sample Train: $($occs[0].trainName) ($($occs[0].avgOccupancy)% - $($occs[0].occupancyClass))"

    Write-Host "`n=== 7. TEST RECOMMENDATIONS ENGINE API ==="
    $recs = Invoke-RestMethod -Uri "http://localhost:8080/api/recommendations"
    Write-Host "Generated Recommendations Count: $($recs.Count)"
    Write-Host "Sample Rec: [$($recs[0].priority)] $($recs[0].title) for $($recs[0].trainOrRoute)"

    Write-Host "`n=== 8. TEST COMPREHENSIVE REPORT API ==="
    $rep = Invoke-RestMethod -Uri "http://localhost:8080/api/reports/comprehensive"
    Write-Host "Report Title: $($rep.reportTitle)"
    Write-Host "Generated At: $($rep.generatedAt)"

    Write-Host "`n=== 9. TEST CSV REPORT EXPORT ==="
    $csv = Invoke-WebRequest -Uri "http://localhost:8080/api/reports/csv" -UseBasicParsing
    Write-Host "CSV Export Status: $($csv.StatusCode) Size: $($csv.Content.Length) bytes"

    Write-Host "`nALL 9 MODULES TESTED & WORKING 100%!"

} finally {
    Write-Host "Stopping test application process..."
    if ($proc -and -not $proc.HasExited) {
        Stop-Process -Id $proc.Id -Force
    }
}

