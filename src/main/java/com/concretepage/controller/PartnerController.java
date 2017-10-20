package com.concretepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntPartnerDAO;
import com.concretepage.entity.Partner;

@Controller
@RequestMapping("partner")
public class PartnerController {
	
	// @Autowired - When beans are being collected by Spring at runtime, it looks for properties 
	// within the Java class that represents properties in the bean. @Autowired sets the dependency
	// property for the 'PartnerController' bean to be dependent with the 'IntPartnerDAO' bean.
	// This allows POST traffic to essentially be carried from the client to this controller,
	// which creates any 'Partner' Java beans to throw at the DAO to query the DB.
	// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html
	@Autowired
	private IntPartnerDAO partnerDAO;
	
	@GetMapping("add")
	public @ResponseBody String addNewIncomingRecord (
			@RequestParam String name,
			@RequestParam String contactname,
			@RequestParam String contactphone,
			@RequestParam String contactemail,
			@RequestParam String description) {
		Partner part = new Partner();
		part.setName(name);
		part.setContactName(contactname);
		part.setContactPhone(contactphone);
		part.setContactEmail(contactemail);
		part.setDescription(description);
		partnerDAO.addPartner(part);
		return "all good";
	}
	
	// need sumkinda way to transport da partners over to html. A possible quest for Adam?
//	@GetMapping("list")
//	public 
	
	@GetMapping("init")
	public @ResponseBody String initIncomingRecordTable() {
		partnerDAO.initPartnerTable();
		return "all good";
	}
}