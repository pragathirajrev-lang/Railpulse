# Large-Scale Railways Data Processing and Demand Forecasting using Java and Apache Spark

An enterprise-grade Big Data Technologies (BDT) mini-project built entirely with **Java 21**, **Apache Spark 3.5.1**, **Spring Boot 3.2.5**, and **MySQL**.

---

## 1. Project Overview & Academic Compliance

This platform analyzes large-scale railway passenger movement patterns, route performance, network delay bottlenecks, and coach capacity utilization across major Indian railway corridors. It implements genuine distributed data processing using **Apache Spark DataFrames** and machine learning demand forecasting using **Apache Spark MLlib**.

### Academic Requirements Checklist
- [x] **No Google Colab**: 100% locally runnable on Windows.
- [x] **Java as Primary Language**: All backend processing, Spark jobs, and ML workflows are written in Java 21 (no Python / PySpark).
- [x] **Genuine Apache Spark Engine**: Utilizes embedded local SparkSession (`local[*]`), DataFrames, Spark SQL aggregations, and Spark MLlib pipelines.
- [x] **Realistic Big Data Volume**: Contains **10,248 structured passenger trip records** covering all 12 calendar months (Jan–Dec) across 28 major Indian railway routes.
- [x] **Spark MLlib Regression**: Employs `RandomForestRegressor` with chronological 80/20 train/test split to eliminate future-data leakage, reporting MAE, RMSE, and R² metrics alongside a 7-day future demand forecast.
- [x] **MySQL Persistence with Auto-Fallback**: Includes complete `schema.sql` for MySQL 8.x and an intelligent embedded H2 fallback (MySQL compatibility mode) ensuring seamless execution even if MySQL service is stopped.
- [x] **Interactive Full-Stack Web Interface**: Modern deep navy UI with Chart.js charts, multi-parameter filtering, CSV upload/validation, decision-support recommendations, and printable reports.

> **Dataset Notice:** *The included dataset is simulated for academic and demonstration purposes. It models realistic Indian Railways routes (Bengaluru, Chennai, Mumbai, Delhi, Hyderabad, Mysuru, Kolkata, Pune, Kochi, Vijayawada, Ahmedabad) but does not represent official government statistics.*

---

## 2. Technology Stack & Versions

| Layer | Component | Version | Purpose |
| :--- | :--- | :--- | :--- |
| **Language** | Java SE Development Kit | **21.0.12 LTS** | Core backend programming |
| **Big Data Engine** | Apache Spark (Core + SQL + MLlib) | **3.5.1** | Distributed DataFrame analytics & ML regression |
| **Hadoop Utilities** | Hadoop Windows Binaries (`winutils`) | **3.3.5** | Windows file system compatibility for Spark |
| **Application Framework**| Spring Boot | **3.2.5** | REST APIs, JPA persistence, embedded Tomcat |
| **Primary Database** | MySQL Server | **8.x** | Persistent application & records storage |
| **Fallback Database** | H2 Database Engine | **2.2.224** | Zero-configuration local fallback (MySQL mode) |
| **Build & Tooling** | Apache Maven | **3.9.16** | Dependency resolution & packaging (included) |
| **Frontend UI** | HTML5, CSS3, JavaScript, Bootstrap | **5.3.3** | Responsive dashboard with deep navy theme |
| **Data Visualization** | Chart.js | **4.4.2** | Dynamic interactive line, bar, and donut charts |

---

## 3. Project Directory Structure

```text
BDT project/
├── hadoop/
│   └── bin/
│       ├── hadoop.dll                  # Windows Hadoop native library
│       └── winutils.exe                # Windows Hadoop executable
├── tools/
│   └── maven/                          # Standalone Apache Maven 3.9.16
├── src/
│   ├── main/
│   │   ├── java/com/railway/
│   │   │   ├── RailwayApplication.java # Spring Boot main entrypoint
│   │   │   ├── config/
│   │   │   │   ├── SparkConfig.java    # SparkSession configuration bean
│   │   │   │   └── DatabaseConfig.java # MySQL & resilient H2 fallback DataSource
│   │   │   ├── controller/
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── AnalyticsController.java
│   │   │   │   ├── RoutePerformanceController.java
│   │   │   │   ├── ForecastController.java
│   │   │   │   ├── DelayAnalysisController.java
│   │   │   │   ├── OccupancyController.java
│   │   │   │   ├── RecommendationController.java
│   │   │   │   ├── SparkProfilerController.java   # Spark Catalyst & What-If simulator
│   │   │   │   ├── ReportController.java
│   │   │   │   └── DatasetController.java
│   │   │   ├── model/
│   │   │   │   ├── RailwayRecord.java   # JPA entity & data model
│   │   │   │   ├── DashboardSummary.java
│   │   │   │   ├── RouteStatistics.java
│   │   │   │   ├── ForecastResult.java
│   │   │   │   ├── DelayStatistics.java
│   │   │   │   ├── OccupancyStatistics.java
│   │   │   │   ├── Recommendation.java
│   │   │   │   └── ProcessingSummary.java
│   │   │   ├── repository/
│   │   │   │   └── RailwayRepository.java # Native JPA queries for MySQL/H2
│   │   │   ├── service/
│   │   │   │   ├── SparkProcessingService.java    # Spark DataFrame engine
│   │   │   │   ├── ForecastingService.java        # Spark MLlib RandomForest
│   │   │   │   ├── PassengerAnalyticsService.java
│   │   │   │   ├── RouteAnalyticsService.java
│   │   │   │   ├── DelayAnalysisService.java
│   │   │   │   ├── OccupancyService.java
│   │   │   │   ├── RecommendationService.java
│   │   │   │   ├── ReportService.java
│   │   │   │   └── DatasetService.java
│   │   │   └── util/
│   │   │       ├── DataValidationUtil.java        # CSV validation utility
│   │   │       └── RailwayDataGenerator.java      # 10,248 record generator
│   │   └── resources/
│   │       ├── application.properties             # App configuration
│   │       ├── data/
│   │       │   └── railway_passenger_data.csv     # 10,248 record dataset
│   │       └── static/
│   │           ├── index.html                     # Enterprise web dashboard
│   │           ├── css/styles.css                 # Custom styling & print CSS
│   │           └── js/
│   │               ├── app.js                     # State & API controller
│   │               └── charts.js                  # Chart.js visualization logic
│   └── test/
│       └── java/com/railway/
│           └── SparkProcessingServiceTest.java    # Integration & MLlib unit tests
├── railway_passenger_data.csv          # Root dataset copy (10,248 records)
├── schema.sql                          # MySQL database creation script
├── mvnw.cmd                            # Windows Maven wrapper
├── run.bat                             # One-click Windows launch batch script
├── run.ps1                             # PowerShell launch script
└── pom.xml                             # Maven project dependencies
```

---

## 4. Implemented Modules and Features

### Module 1: Railway Analytics Dashboard
- **8 Core KPI Tiles**: Total Records (10,248), Total Trains (28), Total Corridors (28), Total Passengers (11.6M+), Average Occupancy % (93.9%), Average Delay (25.7 mins), Highest-Demand Route, Lowest-Demand Route.
- **Traffic Trend Line Chart**: Daily passenger aggregate curve across the network.
- **Top 7 Corridors Bar Chart**: Ranked volume comparison.
- **Occupancy Donut Chart**: Breakdown into Low, Moderate, High, and Critical.
- **Monthly Volume Chart**: Seasonal ridership variations.

### Module 2: Passenger Demand Analysis
- **Multi-Parameter Filter**: Filter records by Source Station, Destination Station, Train ID, Month, and Date Range.
- **Paginated Table**: Displays Record ID, Train details, Route, Travel Date, Passengers, Capacity, Delay, Occupancy status, and Train Category.
- **Seasonality & Weekday Charts**: Visualizes weekday vs. weekend surges (Friday–Sunday peak).

### Module 3: Route Performance Analysis
- **Corridor Ranking Table**: Ranks routes by total ridership, trip count, average passengers/trip, occupancy %, delay, and distance.
- **Dynamic Sorting**: Instant sorting by any metric in Ascending or Descending order.
- **Trend Indicators**: Color-coded badges for *Increasing*, *Stable*, and *Decreasing* demand.

### Module 4: Passenger Demand Forecasting (Apache Spark MLlib)
- **Machine Learning Pipeline**: Uses `org.apache.spark.ml.regression.RandomForestRegressor` with `VectorAssembler`.
- **Feature Engineering**: Encodes Day of Week (1-7), Month (1-12), Day of Month (1-31), Weekend Indicator (0/1), Distance KM, and Capacity.
- **Zero-Data Leakage**: Enforces strict chronological 80% train / 20% test split.
- **Model Evaluation**: Displays MAE, RMSE, and R² scores.
- **Interactive Multi-Series Chart**:
  - Ground truth historical demand (Blue).
  - Spark MLlib test predictions (Purple dashed).
  - 7-Day Future Demand Projections (Emerald green).
- **Rake Advisory**: Estimates recommended coach rake allocations (e.g. ~14 coaches).

### Module 5: Train Delay Analysis
- **Punctuality Metrics**: Network on-time rate (&le;15 min delay threshold), average delay, and maximum delay.
- **Most Delayed Trains**: Identifies chronic operational bottlenecks.
- **Most Punctual Trains**: Benchmarks top-performing trains (e.g., Vande Bharat, Deccan Queen).
- **Severity Distribution**: Donut breakdown of On-Time, Minor, Moderate, and Severe delays.

### Module 6: Coach Occupancy Intelligence
- **Formula**: `Occupancy Percentage = (Passenger Count / Train Capacity) × 100`.
- **Classification Thresholds**:
  - *Low*: < 50%
  - *Moderate*: 50% – 75%
  - *High*: 75% – 90%
  - *Critical*: > 90%
- **Interactive Roster**: Searchable by train or route with occupancy tier filter.
- **Corridor Highlights**: Quick cards showing over-capacity vs. underutilized services.

### Module 7: Smart Resource Recommendations
- **Rule-Based Decision Support Engine**: Evaluates actual Spark metrics against operational thresholds.
- **Actionable Cards**:
  - **High Priority (Capacity)**: Augment rake composition with 2-3 additional coaches on critical corridors.
  - **Medium Priority (Utilization)**: Rationalize timetable and reallocate surplus rakes on low-occupancy routes.
  - **High Priority (Delay)**: Conduct sectional track and signaling audit for trains exceeding 35 min avg delay.
  - **Medium Priority (Demand)**: Monitor growing corridors for long-term capacity expansion.
- **Category Filter Pills**: Filter recommendations by Capacity, Utilization, Delays, or Demand.

### Module 8: Dataset Management & Spark Processing
- **CSV Upload & Drag-and-Drop**: Upload new custom datasets.
- **Pre-Ingestion Validation**: Verifies headers, data types, negative counts, missing values, and duplicate IDs.
- **10-Row Live Preview**: Inspect data before triggering the Spark engine.
- **Spark Processing Trigger**: Re-runs DataFrame ingestion, computes aggregations, updates JPA repository, and refreshes the dashboard in real time.
- **One-Click Reset**: Instant rollback to the default 10,248 record dataset.

### Module 9: Intelligence Reports & Exports
- **CSV Export**: Direct download of comprehensive route statistics (`/api/reports/csv`).
- **Printable Executive Report**: Clean CSS-styled printable report (`window.print()`) suitable for submission or PDF export.

### Module 10: Interactive What-If Demand Surge & Coach Capacity Simulator
- **Interactive Operational Planning**: Allows selecting any corridor, adjusting holiday/festival demand surge (+0% to +100%), and attaching extra LHB coach rakes (0 to +6 coaches).
- **Instant Spark Simulation**: Evaluates augmented capacity against surge demand, calculating:
  - Simulated Demand vs. Baseline Demand
  - Augmented Capacity (+72 berths/coach)
  - Projected Occupancy Saturation %
  - Unmet Passenger Spillover (Waitlist prediction)
  - Estimated Revenue Impact (₹)
  - AI Operational Action Advisory Recommendation.

### Module 11: Apache Spark 3.5.1 Cluster Engine & Catalyst Execution Plan Profiler
- **Live JVM & Cluster Metrics**: Reports active CPU Cores (e.g. 8 cores), Default Parallelism (8 threads), Heap Memory tracker (Used / Max allocated), and active RDD Partition count.
- **Spark SQL Catalyst Optimizer Physical Execution Plan**: Real-time extraction of the Spark SQL Catalyst Physical Execution Tree (`AdaptiveSparkPlan`, `HashAggregate`, `Exchange hashpartitioning`, `InMemoryTableScan`, `FileScan csv`). Demonstrates internal WholeStageCodegen and partitioned execution.
- **Partition Roster**: Displays 10,248 records balanced across 4 in-memory partitions (2,562 records each) with deserialized storage status.

### Presentation Mode & Display Scale Switcher
- **One-Click Zoom Control**: Integrated in the topbar (`100% Normal`, `120% Large`, `130% Presentation Mode` &mdash; active by default).
- **Projector & Lab Ready**: Effortlessly scales all typography, stat numbers (up to `3.1rem` / 65px+), cards, tables, and chart canvas heights (`520px` – `620px`) for crystal-clear presentation during academic evaluations and vivas.

---

## 5. Quick Start Instructions (Windows)

### Prerequisites
1. **Java 21 LTS** installed (Verify with `java -version`).
2. (Optional) **MySQL 8.x** running on `localhost:3306`. If MySQL is not running, the application will automatically use embedded H2 in MySQL compatibility mode.

### Step 1: Clone or Open the Workspace
Open a PowerShell or Command Prompt terminal in the project directory:
```powershell
cd "c:\Users\praga_w1n\OneDrive\Desktop\BDT project"
```

### Step 2: (Optional) Initialize MySQL Database
If using local MySQL Server:
1. Open MySQL Workbench or MySQL CLI:
   ```bash
   mysql -u root -p < schema.sql
   ```
2. Verify credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/railway_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=
   ```

### Step 3: Run the Application
You can run the application using any of the following methods:

#### Option A: One-Click Launch (Recommended)
Double-click `run.bat` or execute in PowerShell:
```powershell
.\run.bat
```

#### Option B: Launch Packaged JAR
```powershell
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.cs=ALL-UNNAMED --add-opens=java.base/sun.security.action=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED -Djdk.reflect.useDirectMethodHandle=false -jar target\railway-spark-analytics-1.0.0.jar
```

#### Option C: Run via Maven Wrapper
```powershell
.\mvnw.cmd spring-boot:run
```

### Step 4: Access the Dashboard
Open your web browser and navigate to:
```
http://localhost:8080
```

---

## 6. Testing & Verification Results

All automated integration and Spark MLlib tests have passed:
```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.railway.SparkProcessingServiceTest
...
2026-11-07 14:11:42 INFO c.r.service.SparkProcessingService - Spark dataset processing completed successfully: Processed 7500 records across 28 railway routes successfully in 20441ms.
2026-11-07 14:11:51 INFO c.railway.service.ForecastingService - Spark MLlib forecasting succeeded for 'Bengaluru -> Kolkata'. MAE=74.5, RMSE=86.7, R²=0.74
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 40.73 s -- in com.railway.SparkProcessingServiceTest
[INFO] BUILD SUCCESS
```

### Verified REST API Endpoints:
| Endpoint | Method | Result | Sample Output |
| :--- | :--- | :--- | :--- |
| `GET /` | HTML | HTTP 200 | Enterprise SPA UI loaded |
| `GET /api/dashboard/summary` | JSON | HTTP 200 | Total Records: 10,248; Passengers: 11,640,642; Avg Occupancy: 93.97% |
| `GET /api/dashboard/traffic-trend` | JSON | HTTP 200 | Daily passenger time-series for Chart.js |
| `GET /api/routes/performance` | JSON | HTTP 200 | 28 corridors ranked by total ridership |
| `GET /api/forecast?route=...` | JSON | HTTP 200 | MLlib RandomForest model evaluation + 7 future day predictions |
| `GET /api/delays/all` | JSON | HTTP 200 | 28 train punctuality and delay statistics |
| `GET /api/occupancy/all` | JSON | HTTP 200 | Categorized into Low, Moderate, High, Critical |
| `GET /api/recommendations` | JSON | HTTP 200 | 56 prioritized operational recommendations |
| `GET /api/reports/csv` | CSV | HTTP 200 | Formatted CSV download |
| `GET /api/spark/profiler` | JSON | HTTP 200 | Catalyst execution plan, JVM memory & 4 RDD partitions |
| `GET /api/spark/simulate` | JSON | HTTP 200 | Surge demand vs augmented capacity simulation & advisory |
| `POST /api/dataset/preview` | JSON | HTTP 200 | In-flight CSV validation & 10-row preview |
| `POST /api/dataset/upload` | JSON | HTTP 200 | Ingests new CSV and executes Spark DataFrames |

---

## 7. Troubleshooting Guide

1. **`java.lang.reflect.InaccessibleObjectException` on Java 21**:
   - *Cause*: Java 21 modularity restricts reflection into `sun.nio.ch` or `java.lang`.
   - *Solution*: The required `--add-opens` flags are pre-configured in `pom.xml`, `run.bat`, and `run.ps1`. Always run using `run.bat` or with the specified JVM arguments.
2. **`Could not locate executable null\bin\winutils.exe`**:
   - *Cause*: Spark running on Windows looking for Hadoop binaries.
   - *Solution*: `winutils.exe` and `hadoop.dll` are pre-bundled in the `hadoop/bin/` folder, and `SparkConfig.java` automatically sets `hadoop.home.dir`. No system environment variable configuration is required.
3. **MySQL Connection Refused**:
   - *Cause*: MySQL Server is not running on port 3306.
   - *Solution*: `DatabaseConfig.java` will automatically detect that MySQL is offline and switch to embedded H2 (MySQL Mode). All features, analytics, and persistence remain 100% active. To use MySQL, start the MySQL service and restart the app.
4. **Port 8080 already in use**:
   - Change `server.port=8081` in `src/main/resources/application.properties` and access at `http://localhost:8081`.

