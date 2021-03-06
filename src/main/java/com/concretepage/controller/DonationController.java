package com.concretepage.controller;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntDonationDAO;
import com.concretepage.entity.Donation;
import com.concretepage.service.IntDonationService;
import com.google.gson.Gson;
import com.concretepage.service.DonationService;
import com.google.gson.Gson;

@Controller
@RequestMapping("donation")
public class DonationController {

	@Autowired
	private IntDonationDAO donationDAO;
	
	@Autowired
	private IntDonationService donationService;
	
	@GetMapping("input")
	public @ResponseBody String inputDonation (
			@RequestParam int donation,
			@RequestParam String org_name,
			@RequestParam String user_name,
			@RequestParam int deli,
			@RequestParam int dairy,
			@RequestParam int meat,
			@RequestParam int produce,
			@RequestParam int pantry,
			@RequestParam int bakery,
			@RequestParam int pet_food,
			@RequestParam int nonfood,
			@RequestParam int family_meals_2,
			@RequestParam int family_meals_4,
			@RequestParam String date,
			@RequestParam String page) {
		
		return donationService.separateDonations(donation, org_name, user_name, deli, 
				dairy, meat, produce, pantry, bakery, pet_food, nonfood, family_meals_2, family_meals_4, date, page);
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

	@GetMapping("frequency")
	public @ResponseBody List<String> frequency(@RequestParam String org_name) {

		List<String> categories = donationDAO.getFrequency(org_name);
		return categories;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("report")
	public @ResponseBody String getReport(
			@RequestParam int donation,
			@RequestParam int time,
			@RequestParam int type,
			@RequestParam String start_date,
			@RequestParam String end_date,
			@RequestParam String page,
			@RequestParam String user_name) throws JSONException {

		System.out.println(start_date);
		
		if (user_name != "")
		{
			donationDAO.inputPage(user_name, page);
			donationService.reportTabPrediction(user_name, time, donation, type);
		}
		JSONObject x = donationService.constructReport(donation, time, type, start_date, end_date);
		if (x.equals(null))
		{
			x = new JSONObject().put("JSON", "error");
		}
		String y = "";
		y = x.toString();
		y = y.replaceAll("''", "'");
		System.out.println(y);
		return y;
	}

	@GetMapping("widget")
	public @ResponseBody List<String> widget(
			@RequestParam String username) {
		System.out.println(username);
		List<String> widgetTimes = donationService.findWidgetTimes(username);

		return widgetTimes;
	}

	@GetMapping("showDonations")
	public @ResponseBody String showDonations(@RequestParam String org_name){
		org_name = org_name.substring(9);
		String replaceString=org_name.replace("%20"," ");
		replaceString = replaceString.replaceAll("'", "''");
		System.out.println(replaceString);
		List<Donation> listOfDonations = donationDAO.showDonations(replaceString);
		for (Donation x : listOfDonations) {
			String name = x.getOrgName();
			name = name.replaceAll("''", "'");
			x.setOrgName(name);
		}
		String json = new Gson().toJson(listOfDonations);
		System.out.println(json);
		return json;
	}

	@GetMapping("deleteDonation")
	public @ResponseBody String deleteDonation(@RequestParam String order_id){
		System.out.println(order_id);
		int response = donationDAO.deleteDonation(order_id);
		String json = new Gson().toJson(response);
		return json;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("quickbooksreport")
	public @ResponseBody String getQuickBooksReport(
			@RequestParam int donation,
			@RequestParam int time,
			@RequestParam int type,
			@RequestParam String start_date,
			@RequestParam String end_date,
			@RequestParam String page,
			@RequestParam String user_name) throws JSONException {

		String x = donationService.getQuickBooksReport(donation, start_date);
		x = x.replaceAll("''", "'");
		return x;
	}
}
