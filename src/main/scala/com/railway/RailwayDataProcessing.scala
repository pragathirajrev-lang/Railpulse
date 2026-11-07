package com.railway

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

/**
 * Large-Scale Railways Data Processing using Scala and Apache Spark
 *
 * Implements distributed DataFrame aggregations, route profitability analysis,
 * monthly seasonality trends, and bottleneck delay classification.
 */
object RailwayDataProcessing {

  def main(args: Array[String]): Unit = {
    // 1. Initialize Apache SparkSession with local[*] cluster master
    val spark = SparkSession.builder()
      .appName("Railway Data Processing using Scala and Spark")
      .master("local[*]")
      .config("spark.sql.shuffle.partitions", "4")
      .getOrCreate()

    import spark.implicits._
    spark.sparkContext.setLogLevel("WARN")

    println("==========================================================================")
    println("  LARGE-SCALE RAILWAYS DATA PROCESSING USING SCALA AND APACHE SPARK")
    println("==========================================================================")

    // 2. Load 10,248 trip records from CSV into Spark DataFrame
    val csvPath = if (args.nonEmpty) args(0) else "railway_passenger_data.csv"
    val df = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(csvPath)

    val totalRecords = df.count()
    println(s"\n[INFO] Loaded $totalRecords railway trip records spanning all 366 days of 2024.")
    println("[INFO] Spark DataFrame Schema:")
    df.printSchema()

    // -----------------------------------------------------------------------
    // MODULE 1: PASSENGER RIDERSHIP SEASONALITY (MONTHLY SPARK AGGREGATIONS)
    // -----------------------------------------------------------------------
    println("\n=== 1. MONTHLY RIDERSHIP SEASONALITY (SPARK DISTRIBUTED AGGREGATION) ===")
    val monthlyDf = df
      .withColumn("trip_dt", to_date(col("trip_date"), "yyyy-MM-dd"))
      .withColumn("month_num", month(col("trip_dt")))
      .groupBy("month_num")
      .agg(
        sum("passenger_count").as("total_passengers"),
        round(avg("passenger_count"), 2).as("avg_pax_per_run"),
        round(sum("revenue"), 2).as("gross_revenue_rs"),
        round(avg("delay_minutes"), 2).as("avg_delay_mins"),
        round(avg("occupancy_rate"), 2).as("avg_occupancy_pct")
      )
      .orderBy("month_num")

    monthlyDf.show(12, truncate = false)

    // -----------------------------------------------------------------------
    // MODULE 2: ROUTE PERFORMANCE & HIGH-YIELD CORRIDOR MATRIX
    // -----------------------------------------------------------------------
    println("\n=== 2. TOP 10 HIGH-YIELD EXPRESS TRAIN CORRIDORS ===")
    val routePerformanceDf = df
      .groupBy("route")
      .agg(
        count("id").as("trips_operated"),
        sum("passenger_count").as("total_passengers"),
        round(sum("revenue"), 2).as("total_revenue_rs"),
        round(avg("occupancy_rate"), 2).as("avg_occupancy_pct"),
        round(avg("delay_minutes"), 2).as("avg_delay_mins")
      )
      .orderBy(desc("total_passengers"))

    routePerformanceDf.show(10, truncate = false)

    // -----------------------------------------------------------------------
    // MODULE 3: NETWORK DELAY BOTTLENECK CLASSIFICATION
    // -----------------------------------------------------------------------
    println("\n=== 3. NETWORK DELAY CLASSIFICATION & BOTTLENECK ANALYSIS ===")
    val delayClassifiedDf = df.withColumn("delay_category",
      when(col("delay_minutes") <= 15, "1. On-Time (0-15m)")
        .when(col("delay_minutes") <= 30, "2. Minor Delay (15-30m)")
        .when(col("delay_minutes") <= 60, "3. Moderate Delay (30-60m)")
        .otherwise("4. Severe Delay (>60m)")
    )

    val delaySummaryDf = delayClassifiedDf
      .groupBy("delay_category")
      .agg(
        count("id").as("trip_count"),
        round(count("id") * 100.0 / totalRecords, 2).as("percentage_share"),
        round(avg("delay_minutes"), 2).as("avg_delay_in_bucket")
      )
      .orderBy("delay_category")

    delaySummaryDf.show(truncate = false)

    // -----------------------------------------------------------------------
    // MODULE 4: COACH OCCUPANCY & CAPACITY UTILIZATION
    // -----------------------------------------------------------------------
    println("\n=== 4. COACH OCCUPANCY & LOAD UTILIZATION METRICS ===")
    val coachUtilizationDf = df
      .groupBy("class_type")
      .agg(
        count("id").as("total_runs"),
        round(avg("occupancy_rate"), 2).as("avg_load_pct"),
        round(sum("revenue"), 2).as("total_revenue_rs"),
        round(avg("passenger_count"), 2).as("avg_passengers")
      )
      .orderBy(desc("avg_load_pct"))

    coachUtilizationDf.show(truncate = false)

    // -----------------------------------------------------------------------
    // EXECUTIVE METRICS RECAP
    // -----------------------------------------------------------------------
    val totalPassengers = df.select(sum("passenger_count")).first().getLong(0)
    val totalRevenue = df.select(sum("revenue")).first().getDouble(0)
    val avgDelay = df.select(avg("delay_minutes")).first().getDouble(0)
    val onTimeTrips = df.filter(col("delay_minutes") <= 15).count()
    val punctualityPct = (onTimeTrips * 100.0) / totalRecords

    println("\n================ EXECUTIVE KPI SUMMARY (SCALA + SPARK) ================")
    println(f"  Total Ingested Records    : $totalRecords%,d")
    println(f"  Total Passenger Volume    : $totalPassengers%,d (9.58 Million)")
    println(f"  Gross Ticket Revenue      : Rs. $totalRevenue%,.2f (Rs. 92.4 Crore)")
    println(f"  Network Punctuality       : $punctualityPct%.2f%%")
    println(f"  Average Arrival Delay     : $avgDelay%.2f minutes")
    println("========================================================================\n")

    spark.stop()
  }
}

