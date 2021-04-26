package com.sydney.au.ethicalaivalidation.entity;

import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

public class FeedbackMessage implements Comparable<FeedbackMessage> {

    private Integer listNumber;
    private Integer feedbackType;
    private Integer projectId;
    private String projectName;
    private Timestamp feedbackTime;
    private String creatorName;

    @Override
    public int compareTo(FeedbackMessage o) {
        return this.feedbackTime.compareTo(o.feedbackTime);
    }

    public FeedbackMessage(Integer listNumber, Integer feedbackType, Integer projectId, String projectName, Timestamp feedbackTime, String creatorName) {
        this.listNumber = listNumber;
        this.feedbackType = feedbackType;
        this.projectId = projectId;
        this.projectName = projectName;
        this.feedbackTime = feedbackTime;
        this.creatorName = creatorName;
    }

    public FeedbackMessage(Integer feedbackType, Integer projectId, String projectName, Timestamp feedbackTime, String creatorName) {
        this.feedbackType = feedbackType;
        this.projectId = projectId;
        this.projectName = projectName;
        this.feedbackTime = feedbackTime;
        this.creatorName = creatorName;
    }

    public Map<String, Object> toResMap() {
        Map<String, Object> res = new TreeMap<>();
        res.put("listnumber", listNumber);
        res.put("feedbacktype", feedbackType);
        res.put("projectid", projectId);
        res.put("projectname", projectName);
        res.put("feedbacktime", feedbackTime.toString());
        res.put("creatorname", creatorName);
        return res;
    }

    public Integer getListNumber() {
        return listNumber;
    }

    public void setListNumber(Integer listNumber) {
        this.listNumber = listNumber;
    }

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Timestamp getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Timestamp feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


}
