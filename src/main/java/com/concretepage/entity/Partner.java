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
    private int org_id;

    private String org_name;

    private String contact_name;
    
    private String contact_number;
    
    private String contact_email;
    
    private String notes;
    
    public Partner() {}
    
    public Partner(int id, String name, String contactName, 
    		String contactPhone, String contactEmail, String description) {
    	this.org_id = id;
    	this.org_name = name;
    	this.contact_name = contactName;
    	this.contact_number = contactPhone;
    	this.contact_email = contactEmail;
    	this.notes = description;
    }

	public int getId() {
        return org_id;
    }

    public void setId(int id) {
        this.org_id = id;
    }

    public String getName() {
        return org_name;
    }

    public void setName(String name) {
        this.org_name = name;
    }

    public String getContactName() {
    	return contact_name;
    }
    
    public void setContactName(String contactName) {
    	this.contact_name = contactName;
    }
    
    public String getContactNumber() {
    	return contact_number;
    }
    
    public void setContactNumber(String contactPhone) {
    	this.contact_number = contactPhone;
    }
    
    public String getContactEmail() {
    	return contact_email;
    }
    
    public void setContactEmail(String contactEmail) {
    	this.contact_email = contactEmail;
    }
    
    public String getNotes() {
    	return notes;
    }
    
    public void setDescription(String description) {
    	this.notes = description;
    }
}
