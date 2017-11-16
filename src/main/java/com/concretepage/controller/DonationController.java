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
import com.concretepage.service.IntDonationService;


@Controller
@RequestMapping("donation")
public class DonationController {

	@Autowired
	private IntDonationDAO donationDAO;
	
	@Autowired
	private IntDonationService donationService;
	
	// Bakery, Deli, Dairy, Meat, Produce, & Pantry
	@GetMapping("input")
	public @ResponseBody String inputDonation (
			@RequestParam int donation,
			@RequestParam String org_name,
			@RequestParam int deli,
			@RequestParam int dairy,
			@RequestParam int meat,
			@RequestParam int produce,
			@RequestParam int pantry,
			@RequestParam int bakery) {
		
		return donationService.separateDonations(donation, org_name, deli, dairy, meat, produce, pantry, bakery);
	}
	
//	@GetMapping("input")
//	public @ResponseBody int inputDonation (
//			@RequestParam String org_name,
//			@RequestParam String category,
//			@RequestParam int weight,
//			@RequestParam int donation) {
//		
//		return donationDAO.inputDonation(org_name, category, weight, donation);
//	}
	
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

	@GetMapping("frequency")
	public @ResponseBody List<String> frequency(@RequestParam String org_name) {

		List<String> categories = donationDAO.getFrequency(org_name);
		return categories;
	}

	@GetMapping("report")
	public @ResponseBody List<Donation> getReport(
			@RequestParam int donation,
			@RequestParam int time,
			@RequestParam int type,
			@RequestParam String start_date,
			@RequestParam String end_date) {

		System.out.print(start_date);
		
		return donationDAO.getReport(donation, time, type, start_date, end_date);
		//return donationDAO.getReport(donation, time, start_date, end_date);
	}
}
