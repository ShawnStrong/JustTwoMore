package com.concretepage.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Frequency{

    @Id
    private String category;

    private int freq;

    public Frequency() {}

    public Frequency(String category, int freq)
    {
        this.category = category;

        this.freq = freq;
    }

    public void setFreq(int freq) {this.freq = freq;}

    public int getFreq(){return this.freq;}

    public void setCategory(String category) {this.category = category;}

    public String getCategory(){return this.category;}

}