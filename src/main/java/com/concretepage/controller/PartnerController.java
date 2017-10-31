package com.concretepage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntPartnerDAO;
import com.concretepage.entity.Partner;

@Controller
@RequestMapping("org")
public class PartnerController {
	
	// @Autowired - When beans are being collected by Spring at runtime, it looks for properties 
	// within the Java class that represents properties in the bean. @Autowired sets the dependency
	// property for the 'PartnerController' bean to be dependent with the 'IntPartnerDAO' bean.
	// This allows POST traffic to essentially be carried from the client to this controller,
	// which creates any 'Partner' Java beans to throw at the DAO to query the DB.
	// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html
	@Autowired
	private IntPartnerDAO partnerDAO;
	
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
	public @ResponseBody List<Partner> listPartner() {
		List<Partner> lop = partnerDAO.listPartner();
		return lop;
	}
	
	@GetMapping("init")
	public @ResponseBody String initIncomingRecordTable() {
		partnerDAO.initPartnerTable();
		return "all good";
	}
}