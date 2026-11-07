package com.railway.model;

public class Recommendation {
    private String id;
    private String title;
    private String trainOrRoute;
    private String reason;
    private String supportingData;
    private String suggestedAction;
    private String priority; // HIGH, MEDIUM, LOW
    private String category; // CAPACITY, DELAY, OCCUPANCY, DEMAND

    public Recommendation() {}

    public Recommendation(String id, String title, String trainOrRoute, String reason,
                          String supportingData, String suggestedAction, String priority, String category) {
        this.id = id;
        this.title = title;
        this.trainOrRoute = trainOrRoute;
        this.reason = reason;
        this.supportingData = supportingData;
        this.suggestedAction = suggestedAction;
        this.priority = priority;
        this.category = category;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTrainOrRoute() { return trainOrRoute; }
    public void setTrainOrRoute(String trainOrRoute) { this.trainOrRoute = trainOrRoute; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getSupportingData() { return supportingData; }
    public void setSupportingData(String supportingData) { this.supportingData = supportingData; }

    public String getSuggestedAction() { return suggestedAction; }
    public void setSuggestedAction(String suggestedAction) { this.suggestedAction = suggestedAction; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

