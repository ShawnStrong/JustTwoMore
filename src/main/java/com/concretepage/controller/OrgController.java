package com.concretepage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntOrgDAO;
import com.concretepage.entity.Org;
import com.google.gson.Gson;

@Controller
@RequestMapping("org")
public class OrgController {
	
	// @Autowired - When beans are being collected by Spring at runtime, it looks for properties 
	// within the Java class that represents properties in the bean. @Autowired sets the dependency
	// property for the 'PartnerController' bean to be dependent with the 'IntPartnerDAO' bean.
	// This allows POST traffic to essentially be carried from the client to this controller,
	// which creates any 'Partner' Java beans to throw at the DAO to query the DB.
	// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html
	@Autowired
	private IntOrgDAO partnerDAO;
	
	@GetMapping("create")
	public @ResponseBody int createOrg (
			@RequestParam String org_name,
			@RequestParam String contact_name,
			@RequestParam String contact_number,
			@RequestParam String contact_email,
			@RequestParam String notes) {

		return partnerDAO.createOrg(org_name, contact_name, contact_number, contact_email, notes);
	}
	
	@GetMapping("update")
	public @ResponseBody int updateOrg (
			@RequestParam String org_name,
			@RequestParam String contact_name,
			@RequestParam String contact_number,
			@RequestParam String contact_email,
			@RequestParam String notes) {

		return partnerDAO.updateOrg(org_name, contact_name, contact_number, contact_email, notes);
	}
	
	@GetMapping("list")
	public @ResponseBody List<Org> listPartner() {
		List<Org> lop = partnerDAO.listPartner();
		return lop;
	}
	
//	Still need to test but supposedly Spring converts Lists to json automatically (thanks Spring!)
// 	
//	@RequestMapping("/carlist.json")
//	public @ResponseBody List<String> getCarList() {
//	    return carService.getAllCars();
//	}
	@GetMapping("list2")
	public @ResponseBody String listPartner2() {
		List<Org> lop = partnerDAO.listPartner();
		String json = new Gson().toJson(lop);
		return json;
	}
	
	@GetMapping("init")
	public @ResponseBody String initIncomingRecordTable() {
		partnerDAO.initPartnerTable();
		return "Org table created";
	}
}