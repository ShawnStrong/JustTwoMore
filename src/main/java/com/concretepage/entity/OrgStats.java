package com.concretepage.entity;

public class OrgStats{

    private String orgName;

    private double EV;

    private double t;

    private int rank;

    private int weight;

    private double UMResult;

    public OrgStats(){}

    public OrgStats(String orgName, double EV, double t, int rank, int weight, double UMResult)
    {
        this.orgName = orgName;

        this.EV = EV;

        this.t = t;

        this.rank = rank;

        this.weight=weight;

        this.UMResult=UMResult;
    }

    public void setOrgName(String orgName){this.orgName=orgName;}

    public String getOrgName(){return this.orgName;}

    public void setEV(double EV){this.EV=EV;}

    public double getEV(){return this.EV;}

    public void setT(int t){this.t=t;}

    public double getT(){return this.t;}

    public void setRank(int rank){this.rank=rank;}

    public int getRank(){return this.rank;}

    public void setWeight(int weight){this.weight=weight;}

    public int getWeight(){return this.weight;}

    public void setUMResult(double UMResult){this.UMResult=UMResult;}

    public double getUMResult(){return this.UMResult;}

    public void calculateUM(int rank, double t, int weight)
    {
        double dubrank = (double) rank;
        double dubweight = (double) weight;
        System.out.println("passed rank: " + dubrank + " Passsed t: " + t + " Passed Weight: " + dubweight);
        double UMResult1 = 0;
        UMResult1 = (1/(dubrank + 1))*(t/4)*(dubweight);
        System.out.println("UMResult: " + UMResult1);
        setUMResult(UMResult1);
    }
}