# Academic Project Report & Viva Voce Preparation Guide

**Subject:** Big Data Technologies (BDT) Mini-Project  
**Title:** Large-Scale Railways Data Processing and Demand Forecasting using Java and Apache Spark  
**Technology Stack:** Java 21 LTS, Apache Spark 3.5.1, Spring Boot 3.2.5, MySQL 8.x / H2, Bootstrap 5, Chart.js  

---

## 1. Abstract & Problem Statement

Modern railway transportation networks transport millions of passengers daily across complex multi-station networks. Effective railway network management requires deep insights into passenger travel demand, coach capacity utilization, punctuality bottlenecks, and future passenger volume trends.

Traditional relational database queries and single-threaded batch processing scripts struggle to process large-scale, high-velocity transportation datasets efficiently. This project addresses this challenge by designing and implementing an end-to-end Big Data railway intelligence and forecasting platform using **Java 21** and **Apache Spark 3.5.1**.

The system ingests, cleans, transforms, and analyzes large-scale railway records using **Spark SQL DataFrames**, evaluates train punctuality and coach occupancy, trains a **Spark MLlib Random Forest Regression** model to forecast future 7-day passenger demand, and surfaces actionable operational recommendations through an enterprise full-stack web dashboard.

---

## 2. System Architecture

```text
+-----------------------------------------------------------------------------------+
|                            PRESENTATION TIER (UI)                                 |
|   HTML5  |  CSS3 (Deep Navy Theme)  |  Bootstrap 5.3  |  Chart.js 4.4 Visualizations |
|   Single Page App (SPA)  |  Multi-Filter  |  Drag-and-Drop CSV Ingestion  |  Print |
+-----------------------------------------------------------------------------------+
                                        |  RESTful JSON APIs
                                        v
+-----------------------------------------------------------------------------------+
|                        APPLICATION & API TIER (Spring Boot 3)                     |
|  Controllers: Dashboard, Analytics, RoutePerformance, Forecast, Delay, Occupancy |
|  Services: SparkProcessing, Forecasting, Recommendation, Dataset, Report          |
+-----------------------------------------------------------------------------------+
             |                                                  |
             | JDBC / Spring Data JPA                           | Spark Java API
             v                                                  v
+----------------------------------------+  +---------------------------------------+
|            PERSISTENCE TIER            |  |         BIG DATA ENGINE (Spark 3.5)   |
|  - MySQL 8.x Server (Production)       |  |  - Local Cluster: SparkSession local[*]
|  - In-Memory H2 (Resilient Fallback)   |  |  - In-Memory DataFrame Caching        |
|  - Indexed on Route, Date, TrainId     |  |  - Spark SQL Aggregations & Grouping  |
|  - schema.sql DDL Script               |  |  - Spark MLlib VectorAssembler & RF   |
+----------------------------------------+  +---------------------------------------+
                                                                |
                                                                v
                                            +---------------------------------------+
                                            |           DATA STORAGE TIER           |
                                            |  - railway_passenger_data.csv (10,248)|
                                            |  - Multipart Upload Ingestion (CSV)   |
                                            |  - Windows winutils.exe & hadoop.dll  |
                                            +---------------------------------------+
```

---

## 3. Mathematical Formulations & Data Processing Logic

### 3.1 Coach Occupancy Rate
Train occupancy represents the proportion of available passenger capacity filled during a specific trip:

$$\text{Occupancy Percentage} = \left( \frac{\text{Passenger Count}}{\text{Train Capacity}} \right) \times 100$$

Occupancy is categorized into four operational tiers:
- **Low:** $\text{Occupancy} < 50\%$ (Asset underutilization; consider coach rationalization)
- **Moderate:** $50\% \le \text{Occupancy} \le 75\%$ (Normal off-peak operation)
- **High:** $75\% < \text{Occupancy} \le 90\%$ (Healthy, profitable service)
- **Critical:** $\text{Occupancy} > 90\%$ (Risk of passenger waitlisting and overcrowding; review additional coach allocation)

### 3.2 Spark MLlib Feature Engineering Pipeline
The machine learning feature vector $\mathbf{x}$ is assembled using `org.apache.spark.ml.feature.VectorAssembler`:

$$\mathbf{x} = \begin{bmatrix} x_{\text{dow}} \\ x_{\text{month}} \\ x_{\text{dom}} \\ x_{\text{weekend}} \\ x_{\text{dist}} \\ x_{\text{cap}} \end{bmatrix} = \begin{bmatrix} \text{Day of Week (1 to 7)} \\ \text{Month of Year (1 to 12)} \\ \text{Day of Month (1 to 31)} \\ \text{Weekend Indicator } (1 \text{ if Fri/Sat/Sun, else } 0) \\ \text{Route Distance in KM} \\ \text{Train Capacity} \end{bmatrix}$$

### 3.3 Chronological Train / Test Separation
To ensure real-world validity and eliminate **future-data leakage**, the dataset is chronologically sorted by `travel_date`:
- **Training Set ($\mathcal{D}_{\text{train}}$):** The initial $80\%$ of chronological observations.
- **Testing Set ($\mathcal{D}_{\text{test}}$):** The subsequent $20\%$ of chronological observations.

### 3.4 Regression Evaluation Metrics
Model accuracy on the test set is evaluated using three standard metrics:

1. **Mean Absolute Error (MAE):**
   $$\text{MAE} = \frac{1}{n} \sum_{i=1}^{n} |y_i - \hat{y}_i|$$

2. **Root Mean Squared Error (RMSE):**
   $$\text{RMSE} = \sqrt{\frac{1}{n} \sum_{i=1}^{n} (y_i - \hat{y}_i)^2}$$

3. **Coefficient of Determination ($R^2$):**
   $$R^2 = 1 - \frac{\sum_{i=1}^{n} (y_i - \hat{y}_i)^2}{\sum_{i=1}^{n} (y_i - \bar{y})^2}$$

---

## 4. Viva Voce Q&A Preparation (Frequently Asked Questions)

### Q1: What makes this a genuine Big Data application rather than a basic web app?
**Answer:** The core data transformation, cleaning, aggregation, and machine learning pipelines are executed via the **Apache Spark distributed computing engine**. The application initializes a real `SparkSession`, creates distributed `Dataset<Row>` DataFrames, performs DAG-optimized execution via the Catalyst Optimizer, caches intermediate DataFrames in memory, and trains machine learning models via **Spark MLlib**. The Spring Boot backend serves as a web interface to query and trigger these Spark jobs.

### Q2: Why did you use `local[*]` as the Spark master?
**Answer:** In development and academic demonstration environments, `local[*]` tells Apache Spark to run locally using all available logical CPU cores on the laptop/workstation. It spawns local executor threads, partitioning and processing data in parallel across multiple CPU cores. In a multi-node production deployment, the code remains identical; only the master URL is changed (e.g., `spark://master:7077`, YARN, or Kubernetes).

### Q3: Why is Java used instead of Python/PySpark?
**Answer:** The academic requirements mandated **Java** as the primary language. Using Java with Apache Spark avoids Py4J serialization/deserialization overhead between JVM and Python processes, allows seamless integration with Spring Boot's dependency injection container, and compiles down to clean, strongly-typed bytecode running on the modern **Java 21 LTS** virtual machine.

### Q4: How did you prevent data leakage in time-series passenger demand forecasting?
**Answer:** In time-series forecasting, standard random cross-validation or random splits (such as `trainTestSplit(0.8, 0.2)`) cause severe **data leakage** because the model is trained on future days to predict past days. To prevent this, we implemented **chronological separation**: the records are ordered strictly by `travel_date`, the earliest $80\%$ are used exclusively for training, and the model is evaluated solely on the subsequent $20\%$ future test records.

### Q5: What is the role of `winutils.exe` and `hadoop.dll` on Windows?
**Answer:** Apache Spark depends on Apache Hadoop's filesystem abstraction layer (`Hadoop FileSystem`). On Windows operating systems, Hadoop requires `winutils.exe` and `hadoop.dll` to manage Windows file permissions (POSIX emulation) and native I/O. Without these, Spark throws `java.io.IOException: Could not locate executable null\bin\winutils.exe`. We resolved this by bundling Hadoop 3.3.5 binaries inside `./hadoop/bin` and programmatically setting `hadoop.home.dir` in `SparkConfig.java`.

### Q6: How does the application handle database persistence with MySQL?
**Answer:** The application uses Spring Data JPA and Hibernate. A complete `schema.sql` DDL script is provided for MySQL 8.x. To ensure the application runs smoothly during evaluation even if the examiner does not have MySQL running on port 3306, `DatabaseConfig.java` tests the MySQL connection on startup; if MySQL is unavailable, it gracefully falls back to an embedded in-memory H2 database running in MySQL emulation mode.

### Q7: What are the benefits of Spark DataFrames over raw RDDs?
**Answer:** While RDDs (Resilient Distributed Datasets) provide low-level control, DataFrames provide schema awareness and leverage the **Catalyst Query Optimizer** and **Tungsten execution engine**. Catalyst performs predicate pushdown, projection pruning, and expression simplification, generating highly optimized JVM bytecode for aggregations and filtering.

### Q8: What does the Spark Engine Profiler module show?
**Answer:** The Spark Engine Profiler (`#spark-profiler`) is a live diagnostic console demonstrating genuine distributed execution under the hood. It exposes:
1. **Cluster Master & Parallelism:** Confirms `local[*]` running on Java 21 LTS with parallel CPU worker threads matching the hardware's logical cores.
2. **Catalyst Physical Execution Plan:** The actual DAG tree generated by Spark (`AdaptiveSparkPlan`, `HashAggregate`, `Exchange hashpartitioning`, and `FileScan csv`).
3. **In-Memory RDD Partitions:** Displays 10,248 records evenly partitioned across 4 in-memory blocks (2,562 records per partition) cached for sub-second retrieval.

### Q9: How does the What-If Passenger Demand Simulator work?
**Answer:** The What-If Simulator (`#capacity-simulator`) allows railway operators to perform scenario modeling before committing physical rolling stock. By specifying a route corridor, holiday demand surge (`+0%` to `+100%`), and extra coach additions (`+1` to `+6` LHB coaches with 72 berths each), the Spark engine calculates projected occupancy, passenger waitlist spillover, and net revenue impact.

---

## 5. Summary of Demonstration Assets

- **Default Dataset:** `railway_passenger_data.csv` (10,248 records across 12 full calendar months)
- **Valid Custom Upload Sample:** `sample_upload_valid.csv` (1,000 records)
- **Invalid Upload Validation Sample:** `sample_upload_invalid.csv` (contains missing columns, invalid dates, and negative counts)
- **One-Click Launchers:** `run.bat` (Command Prompt / Explorer) and `run.ps1` (PowerShell)
- **Automated Verification Script:** `test-app.ps1` (verifies all REST APIs and UI status)
- **Interactive UI Modes:** Includes Topbar Display Zoom Controls (100% standard, 115% enlarged, and 130% Presentation Mode for projectors).

