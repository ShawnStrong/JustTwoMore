package com.concretepage.service;

public interface IntDonationService {

	String separateDonations(int donation, String org_name, String user_name, int deli, int dairy, int meat,
			int produce, int pantry, int bakery);

}
