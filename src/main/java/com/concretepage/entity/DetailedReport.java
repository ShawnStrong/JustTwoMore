package com.concretepage.entity;

public class DetailedReport{

    private String timeRange;

    private String org;

    private String category;

    private int weight;

    public DetailedReport(){}

    public DetailedReport(String timeRange, String org, String Category, int weight)
    {
        this.timeRange = timeRange;

        this.org = org;

        this.category = category;

        this.weight = weight;
    }

    public void setTimeRange(String timeRange) {this.timeRange = timeRange;}

    public String getTimeRange(){return this.timeRange;}

    public void setOrg(String org) {this.org = org;}

    public String getOrg(){return this.org;}

    public void setWeight(int weight) {this.weight = weight;}

    public int getWeight(){return this.weight;}

    public void setCategory(String category) {this.category = category;}

    public String getCategory(){return this.category;}

}