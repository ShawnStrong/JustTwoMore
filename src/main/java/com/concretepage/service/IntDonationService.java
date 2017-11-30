package com.concretepage.service;

import java.util.List;

public interface IntDonationService {

	String separateDonations(int donation, String org_name, String user_name, int deli, int dairy, int meat,
			int produce, int pantry, int bakery, String page);


	List<String> findWidgetTimes(String username);

	int reportTabPrediction(String user_name, int timeRange, int inOut, int sumDisDick);
}
