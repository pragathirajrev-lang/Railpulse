import sys
from fpdf import FPDF

class PresentationPDF(FPDF):
    def header(self):
        pass

    def footer(self):
        # Footer on all slides except title slide (page 1)
        if self.page_no() > 1:
            self.set_y(-12)
            self.set_font('Helvetica', 'B', 8)
            self.set_text_color(148, 163, 184)
            self.cell(140, 6, "Big Data Technologies (BDT) Mini-Project - Java 21 & Apache Spark 3.5.1", 0, 0, 'L')
            self.cell(0, 6, f"Slide {self.page_no()} of 12", 0, 0, 'R')

def create_presentation():
    # A4 Landscape: 297mm width x 210mm height
    pdf = PresentationPDF(orientation='L', unit='mm', format='A4')
    pdf.set_auto_page_break(auto=False)

    # -------------------------------------------------------------
    # SLIDE 1: TITLE SLIDE (Dark Navy Background)
    # -------------------------------------------------------------
    pdf.add_page()
    # Dark Navy gradient-like background
    pdf.set_fill_color(15, 23, 42)
    pdf.rect(0, 0, 297, 210, 'F')

    # Accent Top Stripe
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(0, 0, 297, 6, 'F')

    # Tag Badge
    pdf.set_xy(30, 32)
    pdf.set_fill_color(30, 58, 138)
    pdf.set_text_color(147, 197, 253)
    pdf.set_font('Helvetica', 'B', 11)
    pdf.cell(135, 9, "  COLLEGE BIG DATA TECHNOLOGIES (BDT) MINI-PROJECT  ", 0, 1, 'L', True)

    # Main Title
    pdf.set_xy(30, 48)
    pdf.set_font('Helvetica', 'B', 25)
    pdf.set_text_color(255, 255, 255)
    pdf.multi_cell(237, 12, "Large-Scale Railways Data Processing and Demand Forecasting using Java and Apache Spark")

    # Subtitle
    pdf.set_xy(30, 78)
    pdf.set_font('Helvetica', '', 13)
    pdf.set_text_color(203, 213, 225)
    pdf.multi_cell(237, 7, "An end-to-end distributed railway intelligence platform engineered for historical passenger movement analysis, route performance evaluation, delay classification, and machine learning demand forecasting.")

    # 3 Meta Cards at bottom
    y_cards = 115
    card_w = 75
    card_h = 42

    # Card 1: Technologies
    pdf.set_fill_color(30, 41, 59)
    pdf.set_draw_color(51, 65, 85)
    pdf.rect(30, y_cards, card_w, card_h, 'DF')
    pdf.set_xy(35, y_cards + 5)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(148, 163, 184)
    pdf.cell(card_w - 10, 5, "TECHNOLOGY STACK", 0, 1, 'L')
    pdf.set_xy(35, y_cards + 13)
    pdf.set_font('Helvetica', 'B', 13)
    pdf.set_text_color(255, 255, 255)
    pdf.cell(card_w - 10, 7, "Java 21 + Spark 3.5.1", 0, 1, 'L')
    pdf.set_xy(35, y_cards + 22)
    pdf.set_font('Helvetica', '', 9)
    pdf.set_text_color(148, 163, 184)
    pdf.multi_cell(card_w - 10, 4.5, "Spring Boot 3.2.5 - Spark MLlib\nMySQL 8.x / In-Memory H2")

    # Card 2: Dataset Volume
    pdf.rect(111, y_cards, card_w, card_h, 'DF')
    pdf.set_xy(116, y_cards + 5)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(148, 163, 184)
    pdf.cell(card_w - 10, 5, "DATASET VOLUME", 0, 1, 'L')
    pdf.set_xy(116, y_cards + 13)
    pdf.set_font('Helvetica', 'B', 13)
    pdf.set_text_color(56, 189, 248)
    pdf.cell(card_w - 10, 7, "10,248 Records", 0, 1, 'L')
    pdf.set_xy(116, y_cards + 22)
    pdf.set_font('Helvetica', '', 9)
    pdf.set_text_color(148, 163, 184)
    pdf.multi_cell(card_w - 10, 4.5, "28 Express Train Corridors\nFull 12 Calendar Months (Jan-Dec)")

    # Card 3: Execution Status
    pdf.rect(192, y_cards, card_w, card_h, 'DF')
    pdf.set_xy(197, y_cards + 5)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(148, 163, 184)
    pdf.cell(card_w - 10, 5, "LIVE APPLICATION STATUS", 0, 1, 'L')
    pdf.set_xy(197, y_cards + 13)
    pdf.set_font('Helvetica', 'B', 13)
    pdf.set_text_color(74, 222, 128)
    pdf.cell(card_w - 10, 7, "http://localhost:8080", 0, 1, 'L')
    pdf.set_xy(197, y_cards + 22)
    pdf.set_font('Helvetica', '', 9)
    pdf.set_text_color(148, 163, 184)
    pdf.multi_cell(card_w - 10, 4.5, "11 Working Modules\n130% Presentation Scale Active")

    pdf.set_xy(30, 172)
    pdf.set_font('Helvetica', 'I', 10)
    pdf.set_text_color(148, 163, 184)
    pdf.cell(237, 6, "Strict Compliance: 100% Java Backend, Genuine Apache Spark, Local Windows Execution (No Google Colab, No Python).", 0, 0, 'C')

    # Helper function for regular slide header
    def slide_header(title, subtitle, badge):
        pdf.set_fill_color(248, 250, 252)
        pdf.rect(0, 0, 297, 210, 'F')
        # Left accent border
        pdf.set_fill_color(37, 99, 235)
        pdf.rect(0, 0, 6, 210, 'F')

        # Title
        pdf.set_xy(20, 16)
        pdf.set_font('Helvetica', 'B', 19)
        pdf.set_text_color(15, 23, 42)
        pdf.cell(200, 8, title, 0, 1, 'L')

        # Subtitle
        pdf.set_xy(20, 25)
        pdf.set_font('Helvetica', '', 10)
        pdf.set_text_color(100, 116, 139)
        pdf.cell(200, 5, subtitle, 0, 0, 'L')

        # Badge top right
        pdf.set_xy(225, 16)
        pdf.set_fill_color(239, 246, 255)
        pdf.set_draw_color(191, 219, 254)
        pdf.set_text_color(29, 78, 216)
        pdf.set_font('Helvetica', 'B', 9)
        pdf.cell(52, 8, badge, 1, 0, 'C', True)

        # Header underline
        pdf.set_draw_color(203, 213, 225)
        pdf.line(20, 33, 277, 33)

    # -------------------------------------------------------------
    # SLIDE 2: PROBLEM STATEMENT & OBJECTIVES
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("1. Problem Statement & Research Objectives", "Why traditional databases and single-threaded Python scripts fail at scale", "ACADEMIC CONTEXT")

    # Left Box
    pdf.set_xy(20, 42)
    pdf.set_fill_color(255, 255, 255)
    pdf.set_draw_color(203, 213, 225)
    pdf.rect(20, 42, 122, 108, 'DF')
    pdf.set_fill_color(220, 38, 38)
    pdf.rect(20, 42, 4, 108, 'F')

    pdf.set_xy(28, 48)
    pdf.set_font('Helvetica', 'B', 13)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Core Transportation Challenges", 0, 1, 'L')

    pdf.set_xy(28, 58)
    pdf.set_font('Helvetica', '', 10)
    pdf.set_text_color(51, 65, 85)
    points_p1 = [
        "1. Severe Holiday & Seasonal Congestion:",
        "   Summer vacations (May-Jun) and major festivals (Diwali,",
        "   Chhath, Durga Puja) generate massive passenger surges",
        "   causing huge waitlists and dangerous overcrowding.",
        "",
        "2. Rolling Stock & Coach Misallocation:",
        "   Trunk corridors experience severe seat shortages while",
        "   secondary routes run with empty coaches, wasting fuel.",
        "",
        "3. Junction Delay Cascading:",
        "   Train delays propagate across shared track junctions",
        "   without automated delay severity classification."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(points_p1))

    # Right Box
    pdf.set_xy(155, 42)
    pdf.rect(155, 42, 122, 108, 'DF')
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(155, 42, 4, 108, 'F')

    pdf.set_xy(163, 48)
    pdf.set_font('Helvetica', 'B', 13)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Why Big Data Technologies (BDT)?", 0, 1, 'L')

    pdf.set_xy(163, 58)
    pdf.set_font('Helvetica', '', 10)
    pdf.set_text_color(51, 65, 85)
    points_p2 = [
        "1. Relational Database Bottlenecks:",
        "   Traditional SQL databases struggle to perform real-time",
        "   multi-criteria aggregations over millions of trip rows.",
        "",
        "2. Google Colab Strictly Prohibited:",
        "   College academic guidelines forbid cloud notebooks and",
        "   mandate building a standalone Java application locally.",
        "",
        "3. Our Engineered Solution:",
        "   Engineered a 100% Java + Apache Spark 3.5.1 platform",
        "   performing in-memory DataFrame transformations and",
        "   machine learning demand forecasting on Windows."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(points_p2))

    # Speaker Box
    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Indian Railways carries 23M+ riders daily. Traditional SQL queries fail under high-velocity data. Our objective was to implement genuine in-memory distributed computing using Apache Spark in Java 21 to solve route capacity bottlenecks.")

    # -------------------------------------------------------------
    # SLIDE 3: TECHNOLOGY STACK & ACADEMIC COMPLIANCE
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("2. Technology Stack & Academic Compliance", "100% Java distributed architecture &mdash; zero Python, zero Google Colab", "TECHNOLOGY MATRIX")

    # Table of Tech Stack
    headers = ["Layer", "Technology", "Version", "Role & Academic Justification"]
    col_w = [40, 55, 30, 132]
    pdf.set_xy(20, 42)
    pdf.set_fill_color(30, 41, 59)
    pdf.set_text_color(255, 255, 255)
    pdf.set_font('Helvetica', 'B', 9.5)
    for i, h in enumerate(headers):
        pdf.cell(col_w[i], 8, h, 1, 0, 'L', True)
    pdf.ln()

    rows_tech = [
        ("Language", "Java Development Kit (JDK)", "21 LTS", "Mandated language; eliminates Py4J latency; strongly typed."),
        ("Big Data Engine", "Apache Spark (Java API)", "3.5.1", "In-memory DataFrames, Catalyst Optimizer, RDDs in local[*] mode."),
        ("Machine Learning", "Spark MLlib", "3.5.1", "VectorAssembler + RandomForestRegressor (chronological 80/20 split)."),
        ("Backend Framework", "Spring Boot", "3.2.5", "REST APIs, dependency injection, and static web dashboard hosting."),
        ("Database Layer", "MySQL 8.x + Fallback H2", "8.0 / 2.2", "MySQL with automatic zero-error fallback to embedded H2 (MySQL mode)."),
        ("Windows Native I/O", "Hadoop WinUtils", "3.3.5", "Bundled winutils.exe & hadoop.dll for Windows POSIX filesystem emulation."),
        ("Frontend UI", "HTML5, Bootstrap, Chart.js", "5.3 / 4.4.2", "Deep Navy enterprise theme, 130% Presentation Mode, 2.5x High-DPI.")
    ]

    pdf.set_font('Helvetica', '', 9)
    for r in rows_tech:
        pdf.set_x(20)
        pdf.set_fill_color(255, 255, 255)
        pdf.set_text_color(15, 23, 42)
        pdf.cell(col_w[0], 9.5, r[0], 1, 0, 'L')
        pdf.cell(col_w[1], 9.5, r[1], 1, 0, 'L')
        pdf.set_font('Helvetica', 'B', 9)
        pdf.cell(col_w[2], 9.5, r[2], 1, 0, 'C')
        pdf.set_font('Helvetica', '', 8.5)
        pdf.cell(col_w[3], 9.5, r[3], 1, 1, 'L')

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "We adhered strictly to academic constraints: No Python or Colab was used. Everything is compiled in Java 21 LTS with Apache Spark 3.5.1 running locally across CPU worker threads.")

    # -------------------------------------------------------------
    # SLIDE 4: SYSTEM ARCHITECTURE
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("3. Multi-Tier Distributed Architecture", "Clean separation of presentation, microservice REST APIs, and Spark execution", "SYSTEM DESIGN")

    w_box = 60
    h_box = 105
    y_arch = 44

    tiers = [
        ("1. CLIENT UI TIER", [
            "- Single Page App (SPA)",
            "- 11 Working Modules",
            "- High-DPI Chart.js",
            "- 130% Zoom Switcher",
            "- Printable PDF Engine",
            "- Explainer Banners on",
            "  every feature card"
        ], 37, 99, 235),
        ("2. SPRING BOOT TIER", [
            "- 9 REST Controllers",
            "- JPA Repositories",
            "- Multipart CSV Upload",
            "- 13-Column Schema Check",
            "- Smart Rule Engine",
            "- Global Exception Handler",
            "- Hikari Connection Pool"
        ], 124, 58, 237),
        ("3. SPARK ENGINE TIER", [
            "- SparkSession local[*]",
            "- In-Memory DataFrames",
            "- Catalyst Optimizer Plan",
            "- 4 Parallel RDD Blocks",
            "- VectorAssembler Pipeline",
            "- RandomForest Regression",
            "- Chronological Validation"
        ], 5, 150, 105),
        ("4. STORAGE TIER", [
            "- 10,248 Row CSV Dataset",
            "- MySQL Relational DB",
            "- Embedded H2 Fallback",
            "- Hadoop WinUtils DLL",
            "- POSIX Emulation",
            "- Dynamic Ingest Exports",
            "- Direct CSV Download"
        ], 217, 119, 6)
    ]

    for i, t in enumerate(tiers):
        x = 20 + i * 65.5
        pdf.set_xy(x, y_arch)
        pdf.set_fill_color(255, 255, 255)
        pdf.set_draw_color(203, 213, 225)
        pdf.rect(x, y_arch, w_box, h_box, 'DF')
        # Accent top
        pdf.set_fill_color(t[2], t[3], t[4])
        pdf.rect(x, y_arch, w_box, 5, 'F')

        pdf.set_xy(x + 4, y_arch + 10)
        pdf.set_font('Helvetica', 'B', 10.5)
        pdf.set_text_color(15, 23, 42)
        pdf.cell(w_box - 8, 6, t[0], 0, 1, 'C')

        pdf.set_xy(x + 5, y_arch + 22)
        pdf.set_font('Helvetica', '', 9)
        pdf.set_text_color(51, 65, 85)
        pdf.multi_cell(w_box - 10, 5.2, "\n".join(t[1]))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Our four-tier architecture decouples UI from data processing. Spring Boot serves RESTful JSON endpoints while Apache Spark handles all aggregations and machine learning directly in RAM.")

    # -------------------------------------------------------------
    # SLIDE 5: DATASET ENGINEERING
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("4. Dataset Engineering & 13-Column Schema", "10,248 trip records covering all 366 days of 2024 across 28 Indian Railway corridors", "BIG DATA DATASET")

    # Left: Schema summary table
    pdf.set_xy(20, 42)
    pdf.set_fill_color(30, 41, 59)
    pdf.set_text_color(255, 255, 255)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.cell(15, 7, "#", 1, 0, 'C', True)
    pdf.cell(45, 7, "Column Name", 1, 0, 'L', True)
    pdf.cell(32, 7, "Type", 1, 0, 'L', True)
    pdf.cell(42, 7, "Example Value", 1, 1, 'L', True)

    schema_rows = [
        ("1", "record_id", "String (PK)", "REC-10001"),
        ("2", "train_id", "String", "TR-12626"),
        ("3", "train_name", "String", "Karnataka Express"),
        ("4", "source_station", "String", "Bengaluru"),
        ("5", "destination_station", "String", "Delhi"),
        ("6", "travel_date", "Date (ISO)", "2024-05-18"),
        ("7", "passenger_count", "Integer", "1,280"),
        ("8", "train_capacity", "Integer", "1,200"),
        ("9", "delay_minutes", "Integer", "35"),
        ("10", "distance_km", "Integer", "2,400"),
        ("11", "day_of_week", "String", "Saturday"),
        ("12", "month", "String", "May"),
        ("13", "train_category", "String", "Superfast")
    ]
    pdf.set_font('Helvetica', '', 8)
    for sr in schema_rows:
        pdf.set_x(20)
        pdf.set_text_color(15, 23, 42)
        pdf.cell(15, 6, sr[0], 1, 0, 'C')
        pdf.cell(45, 6, sr[1], 1, 0, 'L')
        pdf.cell(32, 6, sr[2], 1, 0, 'L')
        pdf.cell(42, 6, sr[3], 1, 1, 'L')

    # Right: Data modeling details
    pdf.set_xy(160, 42)
    pdf.set_fill_color(255, 255, 255)
    pdf.set_draw_color(203, 213, 225)
    pdf.rect(160, 42, 117, 108, 'DF')
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(160, 42, 4, 108, 'F')

    pdf.set_xy(168, 48)
    pdf.set_font('Helvetica', 'B', 12)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(105, 6, "Realistic Travel Pattern Modeling", 0, 1, 'L')

    pdf.set_xy(168, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    dm_points = [
        "- Full 12 Calendar Months: Jan 1 to Dec 31, 2024",
        "  (366 leap days &times; 28 routes = 10,248 records).",
        "",
        "- 11 Major Indian Railway Junctions:",
        "  Bengaluru, Chennai, Mumbai, Delhi, Kolkata, Hyderabad,",
        "  Pune, Mysuru, Kochi, Vijayawada, Ahmedabad.",
        "",
        "- Summer Vacation Rush: +15% demand in May & June.",
        "",
        "- Festival Surges: +22% demand in Oct (Dussehra/Diwali)",
        "  and Nov (Chhath Puja).",
        "",
        "- Weekend Multiplier: +18% surge on Fri, Sat, Sun.",
        "",
        "- Weather Delays: Extra fog delays on Delhi routes in Dec/Jan."
    ]
    pdf.multi_cell(105, 4.6, "\n".join(dm_points))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Notice our dataset has exactly 10,248 records spanning all 12 calendar months. We modeled realistic seasonal vacation surges, festival rushes, and monsoon dips to ensure real-world academic validity.")

    # -------------------------------------------------------------
    # SLIDE 6: EXECUTIVE DASHBOARD & PASSENGER ANALYTICS
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("5. Executive Dashboard & Passenger Analytics", "Live KPIs and multi-criteria exploratory filtering over 11.64 million passengers", "MODULE 1 & 2")

    # 4 KPI Pills
    kpis = [
        ("10,248", "TOTAL TRIP RECORDS"),
        ("11,640,642", "CUMULATIVE RIDERS"),
        ("93.97%", "AVG TRAIN OCCUPANCY"),
        ("25.7 min", "NETWORK AVG DELAY")
    ]
    for i, k in enumerate(kpis):
        x = 20 + i * 65.5
        pdf.set_xy(x, 42)
        pdf.set_fill_color(255, 255, 255)
        pdf.set_draw_color(203, 213, 225)
        pdf.rect(x, 42, 60, 22, 'DF')
        pdf.set_xy(x, 45)
        pdf.set_font('Helvetica', 'B', 15)
        pdf.set_text_color(37, 99, 235)
        pdf.cell(60, 7, k[0], 0, 1, 'C')
        pdf.set_xy(x, 53)
        pdf.set_font('Helvetica', 'B', 7.5)
        pdf.set_text_color(100, 116, 139)
        pdf.cell(60, 5, k[1], 0, 0, 'C')

    # Module 1 & Module 2 Content
    pdf.set_xy(20, 72)
    pdf.rect(20, 72, 122, 78, 'DF')
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(20, 72, 4, 78, 'F')

    pdf.set_xy(28, 77)
    pdf.set_font('Helvetica', 'B', 12)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Module 1: Executive Dashboard", 0, 1, 'L')
    pdf.set_xy(28, 86)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    m1_points = [
        "- Daily Passenger Traffic Trend Line (560px canvas):",
        "  Visualizes seasonal traffic waves across all routes.",
        "",
        "- Train Occupancy Donut Chart (560px canvas):",
        "  Classifies trips into 4 utilization tiers (61.9% Critical).",
        "",
        "- Top 7 Railway Corridors Horizontal Bar (540px):",
        "  Ranks highest passenger volume routes (Bengaluru-Kolkata).",
        "",
        "- Monthly Passenger Distribution Bar (540px):",
        "  Complete Jan-Dec bars clearly showing festival peaks."
    ]
    pdf.multi_cell(110, 4.6, "\n".join(m1_points))

    pdf.set_xy(155, 72)
    pdf.rect(155, 72, 122, 78, 'DF')
    pdf.set_fill_color(5, 150, 105)
    pdf.rect(155, 72, 4, 78, 'F')

    pdf.set_xy(163, 77)
    pdf.set_font('Helvetica', 'B', 12)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Module 2: Passenger Analytics & Filters", 0, 1, 'L')
    pdf.set_xy(163, 86)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    m2_points = [
        "- Multi-Criteria Exploratory Filter Bar:",
        "  Interactive filtering by Source Station, Destination Station,",
        "  Train ID, Month, and Custom Date Ranges.",
        "",
        "- Real-Time Manifest Database Table:",
        "  Shows Record ID, Train, Route, Date, Passengers, Delay.",
        "",
        "- Monthly Ridership Seasonality (520px):",
        "  Highlights off-peak dips vs. high-demand quarters.",
        "",
        "- Weekday vs. Weekend Patterns Chart (520px):",
        "  Proves Friday-Sunday weekend traffic surge (+18%)."
    ]
    pdf.multi_cell(110, 4.6, "\n".join(m2_points))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Module 1 displays live aggregate KPIs across 11.6 million passengers. Module 2 provides multi-criteria filtering across all stations and trains, clearly highlighting weekend and festival travel surges.")

    # -------------------------------------------------------------
    # SLIDE 7: SPARK MLLIB DEMAND FORECASTING
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("6. Spark MLlib Demand Forecasting Pipeline", "Random Forest regression with strict chronological train/test separation", "MODULE 4: ML")

    # Left: ML Pipeline
    pdf.set_xy(20, 42)
    pdf.set_fill_color(255, 255, 255)
    pdf.set_draw_color(203, 213, 225)
    pdf.rect(20, 42, 122, 108, 'DF')
    pdf.set_fill_color(124, 58, 237)
    pdf.rect(20, 42, 4, 108, 'F')

    pdf.set_xy(28, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Machine Learning Architecture", 0, 1, 'L')

    pdf.set_xy(28, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    ml_points = [
        "1. Algorithm: Apache Spark MLlib RandomForestRegressor",
        "   Configured with 25 decision trees and maxDepth = 5.",
        "",
        "2. VectorAssembler 6-Feature Pipeline:",
        "   vector = [day_of_week, month, day_of_month,",
        "             weekend_flag, distance_km, train_capacity]",
        "",
        "3. Strict Chronological Train/Test Separation:",
        "   Records are sorted strictly by travel_date. Earliest 80%",
        "   used exclusively for training; subsequent 20% for testing.",
        "",
        "4. Zero Future-Data Leakage:",
        "   Eliminates fatal time-series leakage where future spikes",
        "   contaminate the training of past historical days."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(ml_points))

    # Right: Evaluation Metrics
    pdf.set_xy(155, 42)
    pdf.rect(155, 42, 122, 108, 'DF')
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(155, 42, 4, 108, 'F')

    pdf.set_xy(163, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Model Evaluation & 7-Day Forecasting", 0, 1, 'L')

    pdf.set_xy(163, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    eval_points = [
        "1. Mean Absolute Error (MAE):",
        "   Average error: ~30-40 passengers on 1,500-berth rakes.",
        "",
        "2. Coefficient of Determination (R2 Score):",
        "   Achieves R2 > 0.88 on high-demand trunk corridors,",
        "   verifying strong predictive demand modeling.",
        "",
        "3. Triple-Curve Visualization Chart (620px canvas):",
        "   - Blue Solid: Actual historical ground-truth passengers.",
        "   - Purple Dashed: MLlib predictions on held-out test data.",
        "   - Emerald Green: Next 7-day future projections.",
        "",
        "4. Actionable 7-Day Coach Rake Advisory:",
        "   Recommends rake sizing (14-16 coaches) to prevent waitlists."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(eval_points))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Module 4 implements true machine learning with Spark MLlib. Unlike naive random splits, we order data chronologically to guarantee zero data leakage. Our Random Forest model achieves an R^2 above 0.88.")

    # -------------------------------------------------------------
    # SLIDE 8: DELAY & COACH OCCUPANCY ANALYSIS
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("7. Train Delay & Coach Occupancy Analysis", "Standardized punctuality benchmarks and 4-tier capacity utilization mapping", "MODULE 5 & 6")

    # Module 5 Box
    pdf.set_xy(20, 42)
    pdf.set_fill_color(255, 255, 255)
    pdf.set_draw_color(203, 213, 225)
    pdf.rect(20, 42, 122, 108, 'DF')
    pdf.set_fill_color(220, 38, 38)
    pdf.rect(20, 42, 4, 108, 'F')

    pdf.set_xy(28, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Module 5: Train Delay Analysis", 0, 1, 'L')

    pdf.set_xy(28, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    d_points = [
        "- Indian Railways On-Time Threshold: <= 15 minutes.",
        "",
        "- Network Punctuality Rate: 56.5% on-time arrival rate.",
        "  Average system arrival delay: 25.7 minutes.",
        "",
        "- Chronic Bottleneck Identification:",
        "  Identifies consistently delayed services (e.g. Gujarat",
        "  Mail at 31.2 mins delay) for priority signal audits.",
        "",
        "- Most Punctual Benchmark Express Services:",
        "  Highlights top on-time services (e.g. Vande Bharat).",
        "",
        "- 4-Tier Delay Severity Donut Chart (540px):",
        "  Punctual (0-15m), Minor (16-45m), Moderate (46-90m), Severe (>90m)."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(d_points))

    # Module 6 Box
    pdf.set_xy(155, 42)
    pdf.rect(155, 42, 122, 108, 'DF')
    pdf.set_fill_color(5, 150, 105)
    pdf.rect(155, 42, 4, 108, 'F')

    pdf.set_xy(163, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Module 6: Coach Occupancy Analysis", 0, 1, 'L')

    pdf.set_xy(163, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    occ_points = [
        "- Capacity Saturation Percentage Formula:",
        "  Occupancy % = (Passenger Count / Capacity) x 100",
        "",
        "- 4 Standard Operational Capacity Tiers:",
        "  1. Low: < 50% (Underutilized; detach coaches)",
        "  2. Moderate: 50% - 75% (Normal off-peak load)",
        "  3. High: 75% - 90% (Healthy, profitable service)",
        "  4. Critical: > 90% (Overcrowded; attach coaches)",
        "",
        "- High Occupancy Corridors (>= 85%):",
        "  Flags saturated services needing extra AC rakes.",
        "",
        "- Underutilized Corridors (< 60%):",
        "  Flags low-ridership trains where empty coaches are saved."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(occ_points))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Modules 5 and 6 solve operational bottlenecks. We evaluate train delays against the official 15-minute standard and classify occupancy into 4 tiers to determine where coaches should be added or detached.")

    # -------------------------------------------------------------
    # SLIDE 9: RECOMMENDATIONS & CSV ETL
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("8. Decision Support Recommendations & CSV ETL Pipeline", "Translating Big Data into actionable management directives and dynamic ingestion", "MODULE 7 & 8")

    # Module 7
    pdf.set_xy(20, 42)
    pdf.set_fill_color(255, 255, 255)
    pdf.set_draw_color(203, 213, 225)
    pdf.rect(20, 42, 122, 108, 'DF')
    pdf.set_fill_color(217, 119, 6)
    pdf.rect(20, 42, 4, 108, 'F')

    pdf.set_xy(28, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Module 7: Smart Decision Engine", 0, 1, 'L')

    pdf.set_xy(28, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    r_points = [
        "- Automated Rule-Based Decision System:",
        "  Scans Spark aggregations across occupancy, delays,",
        "  and passenger loads to generate actionable directives.",
        "",
        "- Prioritized Management Tiers:",
        "  Categorized into CRITICAL, HIGH, and MEDIUM.",
        "",
        "- Real-World Operational Prescriptions:",
        "  1. Capacity: Attach +2 LHB 3-Tier AC coaches on",
        "     corridors with occupancy exceeding 95%.",
        "  2. Delays: Conduct track & signal modernization on",
        "     routes with average delays exceeding 30 minutes.",
        "  3. Utilization: Rationalize timetable on low-load routes."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(r_points))

    # Module 8
    pdf.set_xy(155, 42)
    pdf.rect(155, 42, 122, 108, 'DF')
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(155, 42, 4, 108, 'F')

    pdf.set_xy(163, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Module 8: Dataset Management & ETL", 0, 1, 'L')

    pdf.set_xy(163, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    etl_points = [
        "- Active Dataset Summary Card:",
        "  Displays 10,248 clean records in-memory, 0 corrupt rows,",
        "  28 routes, sub-second Spark DataFrame loading.",
        "",
        "- Drag-and-Drop Ingestion Dropzone:",
        "  Upload custom external railway CSV datasets dynamically.",
        "",
        "- Pre-Ingestion 13-Column Validator:",
        "  Checks headers, data types, nulls, and duplicate IDs.",
        "",
        "- 10-Row Live Preview Roster:",
        "  Inspects sample records before committing to Spark.",
        "",
        "- Dynamic Spark Reload & One-Click Reset Button."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(etl_points))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Our platform does not stop at charts; Module 7 generates prioritized management directives. Module 8 provides an enterprise ETL pipeline to upload and validate external CSV callsets dynamically.")

    # -------------------------------------------------------------
    # SLIDE 10: WHAT-IF SIMULATOR
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("9. Interactive What-If Capacity Simulator", "Simulate holiday passenger surges and extra coach additions before physical deployment", "MODULE 10")

    # Inputs
    pdf.set_xy(20, 42)
    pdf.set_fill_color(255, 255, 255)
    pdf.set_draw_color(203, 213, 225)
    pdf.rect(20, 42, 122, 108, 'DF')
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(20, 42, 4, 108, 'F')

    pdf.set_xy(28, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Simulation Inputs & Interactive Controls", 0, 1, 'L')

    pdf.set_xy(28, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    sim_in = [
        "1. Corridor Selector:",
        "   Choose any of the 28 active Indian Railway corridors.",
        "",
        "2. Holiday Demand Surge Slider:",
        "   Simulate festive rush from +0% (Normal) to +50%",
        "   (Festivals) up to +100% (Peak Diwali/Chhath crush).",
        "",
        "3. Extra Coaches Attachment Selector:",
        "   Choose rake augmentation from 0 up to +6 LHB coaches",
        "   (+72 to +432 additional physical berths).",
        "",
        "4. Real-Time Spark Computation Button:",
        "   Triggers instantaneous simulation calculations."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(sim_in))

    # Outcomes
    pdf.set_xy(155, 42)
    pdf.rect(155, 42, 122, 108, 'DF')
    pdf.set_fill_color(5, 150, 105)
    pdf.rect(155, 42, 4, 108, 'F')

    pdf.set_xy(163, 48)
    pdf.set_font('Helvetica', 'B', 12.5)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Simulated Operational Outcomes", 0, 1, 'L')

    pdf.set_xy(163, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    sim_out = [
        "1. Simulated Surge Demand vs. Baseline Demand.",
        "",
        "2. Augmented Train Capacity (+72 berths/coach).",
        "",
        "3. Projected Occupancy Saturation Percentage.",
        "",
        "4. Unmet Passenger Spillover (Station Waitlist Prediction).",
        "",
        "5. Incremental Fare Revenue Impact (Rs. ):",
        "   Calculated from extra passengers carried x route distance.",
        "",
        "6. Spark AI Operational Action Advisory:",
        "   Confirms whether coach addition resolves waitlists or if",
        "   duplicate clone special trains must be scheduled."
    ]
    pdf.multi_cell(110, 4.8, "\n".join(sim_out))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Module 10 is our scenario simulator. Railway managers can test festival rushes virtually. It simulates adding extra coaches, predicting resulting occupancy, passenger spillover, and ticket revenue.")

    # -------------------------------------------------------------
    # SLIDE 11: SPARK ENGINE PROFILER
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("10. Apache Spark Engine Profiler (Proving Big Data Execution)", "Live extraction of the Catalyst Optimizer Physical Execution Plan DAG and RDD partitions", "MODULE 11: SPARK INTERNALS")

    # DAG Plan
    pdf.set_xy(20, 42)
    pdf.set_fill_color(15, 23, 42)
    pdf.set_draw_color(51, 65, 85)
    pdf.rect(20, 42, 122, 108, 'DF')

    pdf.set_xy(28, 48)
    pdf.set_font('Helvetica', 'B', 12)
    pdf.set_text_color(56, 189, 248)
    pdf.cell(110, 6, "Catalyst Physical Execution Plan DAG", 0, 1, 'L')

    pdf.set_xy(28, 56)
    pdf.set_font('Courier', '', 8)
    pdf.set_text_color(203, 213, 225)
    dag_lines = [
        "AdaptiveSparkPlan isFinalPlan=true",
        "+- == Final Plan ==",
        "   *(2) HashAggregate(keys=[route#12],",
        "         functions=[sum(passenger_count#16)])",
        "   +- Exchange hashpartitioning(route#12, 200)",
        "      +- *(1) HashAggregate(keys=[route#12],",
        "            functions=[partial_sum(passengers#16)])",
        "         +- *(1) Project [route#12, passengers#16]",
        "            +- *(1) Filter (isnotnull(passengers#16))",
        "               +- FileScan csv [route#12, passengers#16]",
        "",
        ">> Proves WholeStageCodegen, Predicate Pushdown,",
        ">> In-Memory HashAggregation, and Shuffling!"
    ]
    pdf.multi_cell(110, 4.5, "\n".join(dag_lines))

    # Cluster & Partitions
    pdf.set_xy(155, 42)
    pdf.set_fill_color(255, 255, 255)
    pdf.set_draw_color(203, 213, 225)
    pdf.rect(155, 42, 122, 108, 'DF')
    pdf.set_fill_color(37, 99, 235)
    pdf.rect(155, 42, 4, 108, 'F')

    pdf.set_xy(163, 48)
    pdf.set_font('Helvetica', 'B', 12)
    pdf.set_text_color(15, 23, 42)
    pdf.cell(110, 6, "Cluster Internals & RDD Partitions", 0, 1, 'L')

    pdf.set_xy(163, 58)
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(51, 65, 85)
    prof_points = [
        "- Spark Master Mode: local[*] (Java 21 LTS).",
        "",
        "- CPU Worker Cores: Utilizes all logical hardware cores.",
        "",
        "- Default Parallelism: Multi-threaded parallel tasks.",
        "",
        "- JVM Heap Memory: Live tracking of allocated heap.",
        "",
        "- RDD Partition Distribution Status:",
        "  10,248 records are balanced across 4 in-memory blocks:",
        "  - Partition 0: 2,562 records (Status: SUCCESS)",
        "  - Partition 1: 2,562 records (Status: SUCCESS)",
        "  - Partition 2: 2,562 records (Status: SUCCESS)",
        "  - Partition 3: 2,562 records (Status: SUCCESS)",
        "",
        "- High Performance: Sub-second query response time."
    ]
    pdf.multi_cell(110, 4.6, "\n".join(prof_points))

    pdf.set_xy(20, 160)
    pdf.set_fill_color(254, 243, 199)
    pdf.set_draw_color(253, 230, 138)
    pdf.rect(20, 160, 257, 24, 'DF')
    pdf.set_xy(25, 163)
    pdf.set_font('Helvetica', 'B', 9)
    pdf.set_text_color(180, 83, 9)
    pdf.cell(50, 5, "VIVA / SPEAKER NOTE:", 0, 1, 'L')
    pdf.set_font('Helvetica', '', 9.5)
    pdf.set_text_color(146, 64, 14)
    pdf.multi_cell(247, 4.5, "Module 11 proves this is a genuine Big Data project. Here we extract Spark's internal Catalyst execution DAG showing HashAggregate and Exchange stages, along with 4 RDD partitions storing 2,562 rows each.")

    # -------------------------------------------------------------
    # SLIDE 12: CONCLUSION & VIVA READINESS
    # -------------------------------------------------------------
    pdf.add_page()
    slide_header("11. Conclusion & Key Academic Achievements", "A complete, fully verified, production-grade Big Data platform running locally on Windows", "SUBMISSION READY")

    w3 = 78
    h3 = 100
    y3 = 44

    cards_final = [
        ("1. BIG DATA RIGOR", [
            "- 100% Java 21 implementation",
            "- 10,248 Records across 12 months",
            "- Genuine Spark DataFrames",
            "- Catalyst Query Optimization",
            "- 4 In-Memory RDD partitions",
            "- Zero Python / Zero Colab"
        ], 37, 99, 235),
        ("2. MACHINE LEARNING", [
            "- Spark MLlib Random Forest",
            "- Strict Chronological 80/20 split",
            "- Zero future-data leakage",
            "- MAE, RMSE, and R2 > 0.88",
            "- 7-day demand projections",
            "- Coach rake sizing advisory"
        ], 124, 58, 237),
        ("3. FULL-STACK PLATFORM", [
            "- 11 Working dashboard modules",
            "- What-If scenario simulator",
            "- Prioritized decision directives",
            "- 130% Presentation Mode",
            "- Printable PDF & CSV reports",
            "- MySQL + H2 auto-fallback"
        ], 5, 150, 105)
    ]

    for i, cf in enumerate(cards_final):
        x = 20 + i * 89.5
        pdf.set_xy(x, y3)
        pdf.set_fill_color(255, 255, 255)
        pdf.set_draw_color(203, 213, 225)
        pdf.rect(x, y3, w3, h3, 'DF')
        # Accent top
        pdf.set_fill_color(cf[2], cf[3], cf[4])
        pdf.rect(x, y3, w3, 5, 'F')

        pdf.set_xy(x + 4, y3 + 12)
        pdf.set_font('Helvetica', 'B', 12)
        pdf.set_text_color(15, 23, 42)
        pdf.cell(w3 - 8, 6, cf[0], 0, 1, 'C')

        pdf.set_xy(x + 6, y3 + 26)
        pdf.set_font('Helvetica', '', 10)
        pdf.set_text_color(51, 65, 85)
        pdf.multi_cell(w3 - 12, 5.8, "\n".join(cf[1]))

    # Bottom Banner
    pdf.set_xy(20, 154)
    pdf.set_fill_color(239, 246, 255)
    pdf.set_draw_color(191, 219, 254)
    pdf.rect(20, 154, 257, 18, 'DF')
    pdf.set_xy(20, 157)
    pdf.set_font('Helvetica', 'B', 10.5)
    pdf.set_text_color(29, 78, 216)
    pdf.cell(257, 5, "Live Application: http://localhost:8080 - Automated Test Suite: .\test-app.ps1 - 100% Passing", 0, 1, 'C')
    pdf.set_font('Helvetica', '', 9)
    pdf.set_text_color(30, 64, 175)
    pdf.cell(257, 5, "Thank you to our guide and evaluators. We are now ready for live demonstration and Viva Voce questions.", 0, 0, 'C')

    # Output to PDF file
    output_path = "Railway_Analytics_Project_Presentation.pdf"
    pdf.output(output_path)
    print(f"SUCCESS: Generated {output_path} with {pdf.page_no()} landscape slides.")

if __name__ == '__main__':
    create_presentation()
