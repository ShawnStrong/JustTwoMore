package com.concretepage.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concretepage.dao.IntDonationDAO;

@Service
public class DonationService implements IntDonationService {
	@Autowired
	private IntDonationDAO donationDAO;

	/**
	 * Create separate donations from bulk donation payload from UI
	 */
	@Override
	public String separateDonations(int donation, String org_name,
			int deli, int dairy, int meat, int produce, int pantry, int bakery)
	{
		
		Map<String, Integer> mp = new HashMap<String, Integer>();
		mp.put("deli", deli);
		mp.put("dairy", dairy);
		mp.put("meat", meat);
		mp.put("produce", produce);
		mp.put("pantry", pantry);
		mp.put("bakery", bakery);
		
		//iterate over mp to check for donations with values > 0. If so, input to database
		for (String category : mp.keySet()){
			if (mp.get(category) > 0) {
				donationDAO.inputDonation(org_name, category, mp.get(category), donation);
			}
        }
		return "ok";
	}
}