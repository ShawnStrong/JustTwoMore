package com.concretepage.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IncomingRecord {
    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String food;

    private Integer weight;
    
    public IncomingRecord() {}
    
    public IncomingRecord(String name, String food, Integer weight) {
    	this.name = name;
    	this.food = food;
    	this.weight = weight;
    }

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String email) {
        this.food = email;
    }

    public Integer getWeight() { return weight; }

    public void setWeight(Integer weight) { this.weight = weight; }
}
