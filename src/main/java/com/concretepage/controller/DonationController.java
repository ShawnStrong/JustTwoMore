package com.concretepage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntDonationDAO;
import com.concretepage.entity.Donation;


@Controller
@RequestMapping("donation")
public class DonationController {

	@Autowired
	private IntDonationDAO donationDAO;
	
	@GetMapping("input")
	public @ResponseBody int inputDonation (
			@RequestParam String org_name,
			@RequestParam String category,
			@RequestParam int weight,
			@RequestParam int donation) {
		
		return donationDAO.inputDonation(org_name, category, weight, donation);
	}
	
	@GetMapping("listOrg")
	public @ResponseBody List<String> listOrg (
			@RequestParam int donation) {
		
		return donationDAO.listOrg(donation);
	}
	
	@GetMapping("listInfo")
	public @ResponseBody List<Donation> listInfo (
			@RequestParam String org_name,
			@RequestParam int donation) {
		
		return donationDAO.listInfo(org_name, donation);
	}
	
	@GetMapping("init")
	public @ResponseBody String initDonationTable() {
		donationDAO.initDonationTable();
		
		return "Donation table created";
	}
}