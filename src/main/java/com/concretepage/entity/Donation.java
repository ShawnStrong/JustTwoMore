package com.concretepage.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Donation {

	@Id
	@GeneratedValue
	private int order_id;
	
	private int org_id;
	
	private String org_name;
	
	private String category;
	
	private int weight;
	
	private int donation;
	
	public Donation() {}
	
	public Donation(int order_id, int org_id, 
			String org_name, String category,
			int weight, int donation) {
		this.order_id = order_id;
		this.org_id = org_id;
		this.org_name = org_name;
		this.category = category;
		this.weight = weight;
		this.donation = donation;
	}
	
	public int getOrderId() {
		return order_id;
	}
	
	public void setOrderId(int id) {
		this.org_id = id;
	}
	
	public int getOrgId() {
		return org_id;
	}
	
	public void setOrgId(int id) {
		this.org_id = id;
	}
	
	public String getOrgName() {
		return org_name;
	}
	
	public void setOrgName(String org) {
		this.org_name = org;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getDonation() {
		return donation;
	}
	
	public void setDonation(int donation) {
		this.donation = donation;
	}
}