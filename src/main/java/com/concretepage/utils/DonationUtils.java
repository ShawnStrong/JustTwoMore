package com.concretepage.utils;

public class DonationUtils {
	/**
	 * Thought we needed this, might be useful later
	 * @param date
	 * @return modified date
	 */
	public static String modifyDate(String date) {
		String[] ds = date.split("-");
		return ds[2] + "-" + ds [0] + "-" + ds[1];
	}
}
