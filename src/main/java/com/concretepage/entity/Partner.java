package com.concretepage.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// @Entity - This is a bean. Beans are the guys that live between Java and MySQL. A bean object in Java 
// (usually) represents a row in a table. Every variable in here is collected by Spring at runtime and 
// compiled into a bean called 'Partner'. Pretty neat, huh?
@Entity
public class Partner {
	
	// @Id designates the next variable (id in this case) to be the primary key value of the table that the
	// Partner bean belongs to. @GeneratedValue allows id to take on the 'auto-increment' property in MySQL
    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String contactname;
    
    private String contactphone;
    
    private String contactemail;
    
    private String description;
    
    public Partner() {}
    
    public Partner(int id, String name, String contactName, 
    		String contactPhone, String contactEmail, String description) {
    	this.id = id;
    	this.name = name;
    	this.contactname = contactName;
    	this.contactphone = contactPhone;
    	this.contactemail = contactEmail;
    	this.description = description;
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

    public String getContactName() {
    	return contactname;
    }
    
    public void setContactName(String contactName) {
    	this.contactname = contactName;
    }
    
    public String getContactPhone() {
    	return contactphone;
    }
    
    public void setContactPhone(String contactPhone) {
    	this.contactphone = contactPhone;
    }
    
    public String getContactEmail() {
    	return contactemail;
    }
    
    public void setContactEmail(String contactEmail) {
    	this.contactemail = contactEmail;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
}
