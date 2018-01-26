package com.concretepage.service;

import java.util.List;

import org.json.JSONObject;
import org.json.JSONException;

import com.concretepage.entity.Donation;

public interface IntDonationService {

	List<String> findWidgetTimes(String username);

	int reportTabPrediction(String user_name, int timeRange, int inOut, int sumDisDick);
	
	String findUserPage(String username);

	JSONObject convertToJSON(int reportType, List<Donation> donationList, String[] timeArray) throws JSONException;

	String separateDonations(int donation, String org_name, String user_name, int deli, int dairy, int meat,
			int produce, int pantry, int bakery, int pet_food, int nonfood, String date, String page);
	
	public JSONObject constructReport(int donation, int time, 
			int type, String start_date, String end_date);

	public String getQuickBooksReport(int donation, String start_date);
}
