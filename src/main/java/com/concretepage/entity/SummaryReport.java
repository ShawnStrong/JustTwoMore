package com.concretepage.entity;

public class SummaryReport{

    private String timeRange;

    private String org;

    private int weight;

    public SummaryReport(){}

    public SummaryReport(String timeRange, String org, int weight)
    {
        this.timeRange = timeRange;

        this.org = org;

        this.weight = weight;
    }

    public void setTimeRange(String timeRange) {this.timeRange = timeRange;}

    public String getTimeRange(){return this.timeRange;}

    public void setOrg(String org) {this.org = org;}

    public String getOrg(){return this.org;}

    public void setWeight(int weight) {this.weight = weight;}

    public int getWeight(){return this.weight;}

}