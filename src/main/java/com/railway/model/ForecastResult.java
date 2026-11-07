package com.railway.model;

import java.util.ArrayList;
import java.util.List;

public class ForecastResult {

    public static class DataPoint {
        private String date;
        private double actualValue;
        private double predictedValue;

        public DataPoint() {}

        public DataPoint(String date, double actualValue, double predictedValue) {
            this.date = date;
            this.actualValue = actualValue;
            this.predictedValue = predictedValue;
        }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public double getActualValue() { return actualValue; }
        public void setActualValue(double actualValue) { this.actualValue = actualValue; }

        public double getPredictedValue() { return predictedValue; }
        public void setPredictedValue(double predictedValue) { this.predictedValue = predictedValue; }
    }

    private String route;
    private double mae;
    private double rmse;
    private double r2;
    private int trainingSamples;
    private int testingSamples;
    private String algorithm;
    private String status;
    private String warningMessage;
    private List<DataPoint> testEvaluationPoints = new ArrayList<>();
    private List<DataPoint> next7DaysForecast = new ArrayList<>();

    public ForecastResult() {}

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public double getMae() { return mae; }
    public void setMae(double mae) { this.mae = mae; }

    public double getRmse() { return rmse; }
    public void setRmse(double rmse) { this.rmse = rmse; }

    public double getR2() { return r2; }
    public void setR2(double r2) { this.r2 = r2; }

    public int getTrainingSamples() { return trainingSamples; }
    public void setTrainingSamples(int trainingSamples) { this.trainingSamples = trainingSamples; }

    public int getTestingSamples() { return testingSamples; }
    public void setTestingSamples(int testingSamples) { this.testingSamples = testingSamples; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWarningMessage() { return warningMessage; }
    public void setWarningMessage(String warningMessage) { this.warningMessage = warningMessage; }

    public List<DataPoint> getTestEvaluationPoints() { return testEvaluationPoints; }
    public void setTestEvaluationPoints(List<DataPoint> testEvaluationPoints) { this.testEvaluationPoints = testEvaluationPoints; }

    public List<DataPoint> getNext7DaysForecast() { return next7DaysForecast; }
    public void setNext7DaysForecast(List<DataPoint> next7DaysForecast) { this.next7DaysForecast = next7DaysForecast; }
}

