package com.concretepage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;//sounds cool
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.concretepage.dao.IntDonationDAO;
import com.concretepage.entity.Donation;
import com.concretepage.entity.OrgStats;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class DonationService implements IntDonationService {
	@Autowired
	private IntDonationDAO donationDAO;


	@Override
	public String getQuickBooksReport(int donation, String start_date){
		List<Donation> qbList = donationDAO.getQuickBooksDonation(donation, start_date);
		//{"data":[{"2018":40,"2017":0,"org_name":"C. C's Bakery"},{"2018":483,"2017":0,"org_name":"Metcalfe's Sentry"},
		// {"2018":0,"2017":30,"org_name":"Org A"},{"2018":0,"2017":10,"org_name":"Org B"}],"columns":["org_name","2017","2018"]}

		//retuned
		//{"data":[{"Organization":"C. C''s Bakery","Category":"deli","Description":"deli","2018-01-08":"20","Amount":"60",
		// "Date":"2018-01-08","Due Date":"2018-01-08","Rate":"3","Bill No":"1"},{"Organization":"C. C''s Bakery","Category":"dairy",
		// "Description":"dairy","2018-01-08":"20","Amount":"60","Date":"2018-01-08","Due Date":"2018-01-08","Rate":"3","Bill No":"2"},
		// {"Organization":"Metcalfe''s Sentry","Category":"deli","Description":"deli","2018-01-08":"220","Amount":"660",
		// "Date":"2018-01-08","Due Date":"2018-01-08","Rate":"3","Bill No":"3"},{"Organization":"Metcalfe''s Sentry","Category":"bakery","Description":"bakery","2018-01-08":"72","Amount":"216","Date":"2018-01-08","Due Date":"2018-01-08","Rate":"3","Bill No":"4"},{"Organization":"Metcalfe''s Sentry","Category":"pantry","Description":"pantry","2018-01-08":"35","Amount":"105","Date":"2018-01-08","Due Date":"2018-01-08","Rate":"3","Bill No":"5"},{"Organization":"Metcalfe''s Sentry","Category":"meat","Description":"meat","2018-01-08":"21","Amount":"63","Date":"2018-01-08","Due Date":"2018-01-08","Rate":"3","Bill No":"6"},{"Organization":"Metcalfe''s Sentry","Category":"dairy","Description":"dairy","2018-01-08":"135","Amount":"405","Date":"2018-01-08","Due Date":"2018-01-08","Rate":"3","Bill No":"7"},],
		// {"columns":["Organization","Category","Description","2018-01-08","Amount","Date","Due Date","Rate","Bill No"]}
		String Data = "{\"data\":[";
		String columns ="";
		if (donation == 1) {
			columns = "],\"columns\":[\"Organization\",\"Category\",\"Description\",\"" + start_date + "" +
					"\",\"Amount\",\"Date\",\"Due Date\",\"Rate\",\"Bill No\"]";
			int BillNo = 1;
			int count = 0;
			String orgNameSaved = qbList.get(0).getOrgName();
			for (Donation x : qbList) {
				String org = x.getOrgName();
				System.out.println(org);
				String category = x.getCategory();
				int weight = x.getWeight();
				if(category.equals("family_meals_2"))
                {
                    weight = weight*2;
                }
                if(category.equals("family_meals_4"))
                {
                    weight = weight*5;
                }
				int Amount = weight * 3;
				String weight2String = Integer.toString(weight);
				String Amount2String = Integer.toString(Amount);
				String rate = "3";
				if (!(orgNameSaved.equals(x.getOrgName()))) {
					orgNameSaved = x.getOrgName();
					BillNo++;
				}
				String Bill2String = Integer.toString(BillNo);
				Data = Data + "{\"Organization\":\"" + org + "\",\"Category\":\"" + category + "\"," +
						"\"Description\":\"" + category + "\",\"" + start_date + "\":\"" + weight2String + "\"," +
						"\"Amount\":\"" + Amount2String + "\",\"Date\":\"" + start_date + "\",\"Due Date\":\"" + start_date + "\"," +
						"\"Rate\":\"" + rate + "\",\"Bill No\":\"" + Bill2String + "\"}";
				count++;
				if (count != qbList.size()) {
					Data = Data + ",";
				}
			}
		}
		else
		{
			columns = "],\"columns\":[\"Billing Address Line 1\",\"Line Item\",\"Line Item Description\",\"" + start_date + "" +
					"\",\"Line Item Amount\",\"Sales Receipt Date\",\"Deposit To\",\"Line Item Rate\",\"Payment Method\",\"Customer\",\"Sales Receipt No\"]";
			//String Data = "{\"data\":[";
			int BillNo = 1;
			int count = 0;
			String orgNameSaved = qbList.get(0).getOrgName();
			for (Donation x : qbList) {
				String org = x.getOrgName();
				System.out.println(org);
				String category = x.getCategory();
				int weight = x.getWeight();
                if(category.equals("family_meals_2"))
                {
                    weight = weight*2;
                }
                if(category.equals("family_meals_4"))
                {
                    weight = weight*5;
                }
				int Amount = weight * 3;
				String weight2String = Integer.toString(weight);
				String Amount2String = Integer.toString(Amount);
				String rate = "3";
				String Payment = "E-Check";
				String Deposit = "12000 Undeposited Funds";
				if (!(orgNameSaved.equals(x.getOrgName()))) {
					orgNameSaved = x.getOrgName();
					BillNo++;
				}
				String Bill2String = Integer.toString(BillNo);
				Data = Data + "{\"Billing Address Line 1\":\"" + org + "\",\"Line Item\":\"" + category + "\"," +
						"\"Line Item Description\":\"" + category + "\",\"" + start_date + "\":\"" + weight2String + "\"," +
						"\"Line Item Amount\":\"" + Amount2String + "\",\"Sales Receipt Date\":\"" + start_date + "\",\"Deposit To\":\"" + Deposit + "\"," +
						"\"Line Item Rate\":\"" + rate + "\",\"Payment Method\":\"" + Payment + "\",\"Customer\":\"" + org + "\",\"Sales Receipt No\":\"" + BillNo + "\"}";
				count++;
				if (count != qbList.size()) {
					Data = Data + ",";
				}
			}
		}
		return Data + columns + "}";
	}

	/**
	 * Create separate donations from bulk donation payload from UI
	 */
	@Override
	public String separateDonations(int donation, String org_name, String user_name,
			int deli, int dairy, int meat, int produce, int pantry, int bakery, 
			int pet_food, int nonfood, int family_meals_2, int family_meals_4, String date, String page)
	{
		boolean enteredInfo = false;
		// separate donations
		Map<String, Integer> mp = new HashMap<String, Integer>();
		mp.put("deli", deli);
		mp.put("dairy", dairy);
		mp.put("meat", meat);
		mp.put("produce", produce);
		mp.put("pantry", pantry);
		mp.put("bakery", bakery);
		mp.put("pet_food", pet_food);
		mp.put("nonfood", nonfood);
		mp.put("family_meals_2", family_meals_2);
		mp.put("family_meals_4", family_meals_4);
		
		//iterate over mp to check for donations with values > 0. If so, input to database
		for (String category : mp.keySet()){
			int weight = mp.get(category);
			if (weight > 0) {
				donationDAO.inputDonation(org_name, user_name, category, weight, donation, date);
				enteredInfo = true;
			}
        }
        if (enteredInfo && user_name != "")
		{
			donationDAO.inputPage(user_name, page);
		}

		return "ok";
	}

	@Override
	public List<String> findWidgetTimes(String username) {
		System.out.println("User Name passed to service Layer: " + username);
		Calendar todaysDate = Calendar.getInstance();
		Calendar temp = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		int currentDay = todaysDate.get(Calendar.DAY_OF_WEEK);
		List<String> datesToQuery = new ArrayList<String>();
		String dateToAdd = "";
		for (int i = 0; i < 4; i++)
		{
			temp.add(Calendar.DATE, -7);//last week
			dateToAdd = format1.format(temp.getTime());
			datesToQuery.add(dateToAdd);
		}

		//select distinct org_name from donation_table where (ts between '2017-10-15 00:00:00' and '2017-10-15 23:59:59');
		List<List<String>> uniqueOrgs = new ArrayList<List<String>>();
		for (int i = 0; i < 4; i++)
		{
			uniqueOrgs.add(donationDAO.getWidgetOrgs(datesToQuery.get(i), username));
		}

		List<String> UON = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
		{
			UON = getUniqueOrgs(UON, uniqueOrgs.get(i));
		}

		List<String> addedToday = new ArrayList<String>();
		String today ="";
		today = format1.format(todaysDate.getTime());
		addedToday = donationDAO.getWidgetOrgs(today, username);
		System.out.println("Today List: " + addedToday);

		int[][] orgRanks = new int[UON.size()][4];
		orgRanks = getOrgRanks(uniqueOrgs, UON);

		System.out.println(UON);
		for (int i = 0; i<UON.size();i++)
		{
			for (int j = 0; j<4;j++)
			{
				System.out.println(orgRanks[i][j]);
			}
		}

		double[][] expectedValuesAndT = new double[UON.size()][2];
		expectedValuesAndT = getExpectedValues(orgRanks, UON.size());
		for (int i = 0; i<UON.size();i++)
		{
			for (int j = 0; j<2;j++)
			{
				System.out.println(expectedValuesAndT[i][j]);
			}
		}

		List<OrgStats> orgStatsList = new ArrayList<OrgStats>();
		//make list of orgStats with Org name, expected value, and t
		for (int i =0; i<UON.size();i++)
		{
			orgStatsList.add(new OrgStats(UON.get(i), expectedValuesAndT[i][0], expectedValuesAndT[i][1], 0, 0, 0));
			System.out.println("Org Name: " + orgStatsList.get(i).getOrgName() + " Expected Value: " +
								orgStatsList.get(i).getEV() + " t: " + orgStatsList.get(i).getT());
		}

		//set weights
		for (int i=0;i<UON.size();i++)
		{
			int countWeight = 0;
			for (int j = 1; j<5;j++)
			{
				if (orgRanks[i][j - 1] != 0)
				{
					countWeight += (5 - j);
				}
			}
			orgStatsList.get(i).setWeight(countWeight);
		}

		Collections.sort(orgStatsList, new Comparator<OrgStats>(){
			public int compare(OrgStats o1,OrgStats o2){
				if (o1.getEV() == o2.getEV())
					return 0;
				return o1.getEV() < o2.getEV() ? -1 : 1;
			}
		});
		//set ranks
		for (int i =0; i<orgStatsList.size();i++)
		{
			orgStatsList.get(i).setRank(i+1);
		}

		//set UM
		for (int i = 0; i<orgStatsList.size();i++)
		{
			orgStatsList.get(i).calculateUM(orgStatsList.get(i).getRank(), orgStatsList.get(i).getT(), orgStatsList.get(i).getWeight());
			System.out.println("UM: " + orgStatsList.get(i).getUMResult());
		}

		Collections.sort(orgStatsList, new Comparator<OrgStats>() {
			public int compare(OrgStats o1, OrgStats o2) {
				if (o1.getEV() == o2.getEV())
					return 0;
				return o1.getUMResult() > o2.getUMResult() ? -1 : 1;
			}
		});

		List<String> listToReturn = new ArrayList<String>();
		for (int i = 0; i<orgStatsList.size(); i++){
			System.out.println("Org: " + orgStatsList.get(i).getOrgName());
			listToReturn.add(orgStatsList.get(i).getOrgName());
		}

		//cross reference lists
		for (int i = 0 ; i < addedToday.size();i++)
		{
			int index = 0;
			if (listToReturn.contains(addedToday.get(i)))
			{
				index = listToReturn.indexOf(addedToday.get(i));
				listToReturn.remove(index);
			}
		}

		System.out.println("List To Return" + listToReturn);
		return listToReturn;
	}

	@Override
	public int reportTabPrediction(String user_name, int timeRange, int inOut, int sumDisDick)
	{
		if (user_name == "")
		{
			return 0;
		}
		String tr ="";
		String io ="";
		String sd ="";
		if (timeRange == 2)
		{
			tr = "year";
		}
		else if(timeRange == 1)
		{
			tr = "month";
		}
		else if(timeRange == 0)
		{
			tr ="week";
		}

		if (inOut == 1)
		{
			io = "incoming";
		}
		else if (inOut == 0)
		{
			io = "outgoing";
		}

		if (sumDisDick == 1)
		{
			sd = "summary";
		}
		else if (sumDisDick == 0)
		{
			sd = "descriptive";
		}

		return donationDAO.inputReportPrediction(user_name, tr, io, sd);

	}

	private List<String> getUniqueOrgs(List<String> UON, List<String> uniqueOrgsAtWeekI)
	{
		for (String x : uniqueOrgsAtWeekI)
		{
			if (!UON.contains(x))
				UON.add(x);
		}
		return UON;
	}

	private int[][] getOrgRanks(List<List<String>> uniqueOrgs, List<String> UON)
	{
		int[][] orgRanks = new int[UON.size()][4];
		for (int i = 0; i< UON.size(); i++)
		{
			for (int j = 0; j < 4; j++) {
				if (uniqueOrgs.get(j).indexOf(UON.get(i)) != - 1) {
					orgRanks[i][j] = uniqueOrgs.get(j).indexOf(UON.get(i)) + 1;
				}
				else
				{
					orgRanks[i][j] = 0;
				}
			}
		}
		return orgRanks;
	}

	private double[][] getExpectedValues(int[][] orgRanks, int firstArraySize)
	{
		double[][] expectedValuesAndT = new double[firstArraySize][2];
		for (int i = 0; i<firstArraySize;i++)
		{
			double t = 0;
			for (int j = 0; j< 4;j++)
			{
				if (orgRanks[i][j] != 0)
					t++;
			}
			expectedValuesAndT[i][1] = t;
			expectedValuesAndT[i][0] = expectedValue(t, orgRanks[i]);
		}
		return expectedValuesAndT;
	}

	private double expectedValue(double t, int[] orgRanksAtI)
	{
		double sum = 0;
		for (int i = 0; i < 4; i++)
		{
			sum += orgRanksAtI[i];
		}
		return (sum/t);
	}

	public String findUserPage(String username) {
		int reports = 0;
		int donations = 0;
		int orgs = 0;
		
		Calendar temp = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		List<String> datesToQuery = new ArrayList<String>();
		String dateToAdd = "";
		
		for (int i = 0; i < 4; i++) {
			temp.add(Calendar.DATE, -7);//last week
			dateToAdd = format1.format(temp.getTime());
			datesToQuery.add(dateToAdd);
		}
		
		List<String> pages = new ArrayList<String>();
		
		for (int i = 0; i < 4; i++) {
			pages.add(donationDAO.getUserPage(username, datesToQuery.get(i)));
		}
		
		// currently the way to deal with no user data in redirection_table
		if (pages.get(0).equals("a")) {
			return "a";
		}
		
		for (int i = 0; i < 4; i++) {
			String page = pages.get(i);
			if (page.equals("reports")) {
				reports += 4 - i;
			} else if (page.equals("donations")) {
				donations += 4 - i;
			} else if (page.equals("orgs")) {
				orgs += 4 - i;
			}
		}
		
		// some crazy unneccessary sorting to find the highest weighted page
		Map<String, Integer> mp = new HashMap<String, Integer>();
		mp.put("reports", reports);
		mp.put("donations", donations);
		mp.put("orgs", orgs);
		
		Set<Entry<String, Integer>> set = mp.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        
        return list.get(0).getKey();
	}

	@Override
	public JSONObject convertToJSON(int reportType, List<Donation> donationList, String[] timeArray) throws JSONException {
		JSONArray array = new JSONArray();
		String name = "";
		String category = "";
		int weight = 0;
		JSONObject item = null;
		int timeArrayPos = 0;
		for (Donation x: donationList) {
			// descriptive type, includes category
			if (reportType == 0) {
				// check if need to make new JSONObject
				if (!name.equals(x.getOrgName()) || !category.equals(x.getCategory())) {
					// checks for first org. if not, put it into array
					if (item != null) {
						array.put(item);
					}
					// make a new jsonobject
					item = new JSONObject();
					name = x.getOrgName();
					category = x.getCategory();
					weight = x.getWeight();
					item.put("org_name", name);
					item.put("category", category);
					timeArrayPos = 0;
					item.put(timeArray[timeArrayPos], weight);
					timeArrayPos++;
					// check if this is the last x in donationList. if so, input it into array.
					if (x.equals(donationList.get(donationList.size() - 1))) {
						array.put(item);
					}
				} else {
					weight = x.getWeight();
					item.put(timeArray[timeArrayPos], weight);
					timeArrayPos++;
					// check if this is the last x in donationList. if so, input it into array.
					if (x.equals(donationList.get(donationList.size() - 1))) {
						array.put(item);
					}
				}
			// summary type
			} else {
				if (!name.equals(x.getOrgName())) {
					if (item != null) {
						array.put(item);
					}
					item = new JSONObject();
					name = x.getOrgName();
					weight = x.getWeight();
					//System.out.println("Org name: " + name + " weight: " + weight + " ArrayIndex: " + timeArrayPos);
					item.put("org_name", name);
					timeArrayPos = 0;
					item.put(timeArray[timeArrayPos], weight);
					timeArrayPos++;
					// check if this is the last x in donationList. if so, input it into array.
					if (x.equals(donationList.get(donationList.size() - 1))) {
						array.put(item);
					}
				} else {
					weight = x.getWeight();
					//System.out.println("Org name: " + name + " weight: " + weight + " ArrayIndex: " + timeArrayPos);
					item.put(timeArray[timeArrayPos], weight);
					timeArrayPos++;
					// check if this is the last x in donationList. if so, input it into array.
					if (x.equals(donationList.get(donationList.size() - 1))) {
						array.put(item);
					}
				}
			}
		}
		
		//System.out.println(array.toString());
		
		JSONObject allData = new JSONObject();
		System.out.println("Time Array: " + timeArray[0]);
		if (reportType == 0) {
			String[] descriptive = new String[timeArray.length + 2];
			for (int i = 2; i <= timeArray.length + 1; i++) {
				descriptive[i] = timeArray[i-2];
			} 
			descriptive[0] = "org_name";
			descriptive[1] = "category";
			System.out.println("Time Array: " + descriptive[2]);
			allData.put("columns", descriptive);
		} else {
			String[] summary = new String[timeArray.length + 1];
			for (int i = 1; i <= timeArray.length; i++) {
				summary[i] = timeArray[i-1];
			} 
			summary[0] = "org_name";
			allData.put("columns", summary);
		}
		allData.put("data", array);
		
		return allData;
	}
	
	public JSONObject constructReport(int donation, int time, 
			int type, String start_date, String end_date) {

		List<Donation> donations = donationDAO.getDonations(type, donation, start_date, end_date);
		for (Donation x: donations)
		{
			System.out.println("Data returned by query: " + x.getOrgName() + " Category: " + x.getCategory() + " Weight: " + x.getWeight() + "" +
					" Date: " + x.getDate());
		}
		List<Donation> donationTimesSorted = donationDAO.getDonationTimesSorted(donation, start_date, end_date);
		for (Donation x: donationTimesSorted)
		{
			System.out.println("Data returned by query: " + x.getOrgName() + " Category: " + x.getCategory() + " Weight: " + x.getWeight() + "" +
					" Date: " + x.getDate());
		}
		String[] dates = findDateArray(time, start_date, end_date, donationTimesSorted);
		for (int i =0 ; i< dates.length;i++)
		{
			System.out.println("Dates Array: " + dates[i]);
		}
		List<Donation> correctlyFormattedDonationList = formatDonationList(donations, dates, type, time);
		for (Donation x: correctlyFormattedDonationList)
		{
			System.out.println("Data returned by formatDonationList: " + x.getOrgName() + " Category: " + x.getCategory() + " Weight: " + x.getWeight() + "" +
					" Date: " + x.getDate());
		}
		correctlyFormattedDonationList = insertZeros(correctlyFormattedDonationList, dates);
		JSONObject x;
		try{
			x = convertToJSON(type, correctlyFormattedDonationList, dates);
		} catch (JSONException e)
		{
			System.out.print("JSON conversion failed");
			return null;
		}
		return x;

	}

	private List<Donation> formatDonationList(List<Donation> donations, String[] dates, int type, int time)
	{
		List<Donation> correctlyFormattedDonationList = new ArrayList<Donation>();
		if (type == 1)//Summary Report
		{
			correctlyFormattedDonationList = formatSummaryDonationList(donations, dates, time);
		}
		else//Descriptive Report
		{
			correctlyFormattedDonationList = formatDescriptiveDonationList(donations, dates, time);
		}
		return correctlyFormattedDonationList;
	}

	private List<Donation> addWeightsSummary(List<Donation> formattedDonationListWeightsNotAdded)
	{
		List<Donation> formattedDonationListWeightsAdded = new ArrayList<Donation>();
		int cnt = 0;
		String tempDateRange ="";
		int tempWeight =0;
		String tempOrgName ="";
		for (Donation x:formattedDonationListWeightsNotAdded)
		{
			if (cnt == 0)
			{
				tempDateRange = x.getDate();
				tempWeight = x.getWeight();
				tempOrgName = x.getOrgName();
				System.out.println("First Donation Passed into addWeightsSummary: " +
						"" + tempOrgName + " Weight: " + tempWeight + " Date Range: " + tempDateRange);
				cnt++;
			}
			else if(cnt == formattedDonationListWeightsNotAdded.size() -1)
			{
				System.out.println("Last Donation Passed into addWeightsSummary: " +
						"" + x.getOrgName() + " Weight: " + x.getWeight() + " Date Range: " + x.getDate());
				if (x.getOrgName().equals(tempOrgName) && x.getDate().equals(tempDateRange))
				{
					tempWeight += x.getWeight();
					cnt++;
					Donation tempDonation = new Donation(tempOrgName, "", tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
				}
				else {
					Donation tempDonation = new Donation(tempOrgName, "", tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
					tempDonation = new Donation(x.getOrgName(), "", x.getWeight(), x.getDate());
					formattedDonationListWeightsAdded.add(tempDonation);
					cnt++;
				}
			}
			else
			{
				System.out.println("Donation Passed into addWeightsSummary: " +
						"" + tempOrgName + " Weight: " + tempWeight + " Date Range: " + tempDateRange);
				if (x.getOrgName().equals(tempOrgName) && x.getDate().equals(tempDateRange))
				{
					tempWeight += x.getWeight();
					cnt++;
				}
				else
				{
					Donation tempDonation = new Donation(tempOrgName, "", tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
					tempDateRange = x.getDate();
					tempWeight = x.getWeight();
					tempOrgName = x.getOrgName();
					cnt++;
				}
			}
		}
		
		if (cnt == 1) {
			Donation tempDonation = new Donation(tempOrgName, "", tempWeight, tempDateRange);
			formattedDonationListWeightsAdded.add(tempDonation);
		}
		
		for (Donation x: formattedDonationListWeightsAdded)
		{
			System.out.println("formattedDonationListWeightsAdded: " + x.getOrgName() + "" +
					" Weight: " + x.getWeight() + " Date Range: " + x.getDate());
		}
		return formattedDonationListWeightsAdded;
	}

	private List<Donation> addWeightsDescriptive(List<Donation> formattedDonationListWeightsNotAdded)
	{
		List<Donation> formattedDonationListWeightsAdded = new ArrayList<Donation>();
		int cnt = 0;
		String tempDateRange ="";
		int tempWeight =0;
		String tempOrgName ="";
		String tempCategory ="";
		for (Donation x:formattedDonationListWeightsNotAdded)
		{
			if (cnt == 0)
			{
				tempDateRange = x.getDate();
				tempWeight = x.getWeight();
				tempOrgName = x.getOrgName();
				tempCategory = x.getCategory();
				cnt++;
			}
			/*
			 * else if(cnt == formattedDonationListWeightsNotAdded.size() -1)
			{
				System.out.println("Last Donation Passed into addWeightsSummary: " +
						"" + x.getOrgName() + " Weight: " + x.getWeight() + " Date Range: " + x.getDate());
				if (x.getOrgName().equals(tempOrgName) && x.getDate().equals(tempDateRange))
				{
					tempWeight += x.getWeight();
					cnt++;
					Donation tempDonation = new Donation(tempOrgName, "", tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
				}
				else {
					Donation tempDonation = new Donation(tempOrgName, "", tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
					tempDonation = new Donation(x.getOrgName(), "", x.getWeight(), x.getDate());
					formattedDonationListWeightsAdded.add(tempDonation);
				}
			}
			 */
			else if(cnt == formattedDonationListWeightsNotAdded.size() - 1)
			{
				if (x.getOrgName().equals(tempOrgName) && x.getDate().equals(tempDateRange) && x.getCategory().equals(tempCategory))
				{
					tempWeight += x.getWeight();
					cnt++;
					Donation tempDonation = new Donation(tempOrgName, x.getCategory(), tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
				}
				else {
					Donation tempDonation = new Donation(tempOrgName, tempCategory, tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
					tempDonation = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), x.getDate());
					formattedDonationListWeightsAdded.add(tempDonation);
					cnt++;
				}
			}
			else
			{
				if (x.getOrgName().equals(tempOrgName) && x.getDate().equals(tempDateRange) && x.getCategory().equals(tempCategory))
				{
					tempWeight += x.getWeight();
					cnt++;
				}
				else
				{
					Donation tempDonation = new Donation(tempOrgName, tempCategory, tempWeight, tempDateRange);
					formattedDonationListWeightsAdded.add(tempDonation);
					tempDateRange = x.getDate();
					tempWeight = x.getWeight();
					tempOrgName = x.getOrgName();
					tempCategory = x.getCategory();
					cnt++;
				}
			}
		}
		if (cnt == 1) {
			Donation tempDonation = new Donation(tempOrgName, tempCategory, tempWeight, tempDateRange);
			formattedDonationListWeightsAdded.add(tempDonation);
		}
		return formattedDonationListWeightsAdded;
	}

	private List<Donation> formatSummaryDonationList(List<Donation> donations, String[] dates, int time)
	{
		//Create a new Donation List with correct date parameter from the dates array
		String dateRange ="";
		List<Donation> formattedDonationListWeightsNotAdded = new ArrayList<Donation>();
		List<Donation> formattedDonationListWeightsAdded = new ArrayList<Donation>();
		for (Donation x: donations)
		{
			if(x.getCategory().equals("family_meals_2"))
			{
				int tweight = x.getWeight();
				tweight = tweight*2;
				x.setWeight(tweight);
			}
			if(x.getCategory().equals("family_meals_4"))
			{
				int tweight = x.getWeight();
				tweight = tweight*5;
				x.setWeight(tweight);
			}
		}
		if (time == 2)//yearly
		{
			for (Donation x:donations)
			{
				dateRange = x.getDate().substring(0,4);
				Donation temp = new Donation(x.getOrgName(), "", x.getWeight(), dateRange);
				System.out.println("Passing Donation: " + temp.getOrgName() + " category: " +
						temp.getCategory() + " weight: " + temp.getWeight() + "" +
						" Date Range: " + temp.getDate() + " into formattedDonationListWeightsNotAdded");
				formattedDonationListWeightsNotAdded.add(temp);
			}
			//add Weights
			formattedDonationListWeightsAdded = addWeightsSummary(formattedDonationListWeightsNotAdded);
		}
		else if (time == 1)//monthly
		{
			for (Donation x:donations)
			{
				dateRange = x.getDate().substring(0,4) + "-" + x.getDate().substring(5,7);
				Donation temp = new Donation(x.getOrgName(), "", x.getWeight(), dateRange);
				System.out.println("Passing Donation: " + temp.getOrgName() + " category: " +
						temp.getCategory() + " weight: " + temp.getWeight() + "" +
						" Date Range: " + temp.getDate() + " into formattedDonationListWeightsNotAdded");
				formattedDonationListWeightsNotAdded.add(temp);
			}
			formattedDonationListWeightsAdded = addWeightsSummary(formattedDonationListWeightsNotAdded);
		}
		else if (time == 3) {
			for (Donation x:donations)
			{
				dateRange = x.getDate();
				Donation temp = new Donation(x.getOrgName(), "", x.getWeight(), dateRange);
				System.out.println("Passing Donation: " + temp.getOrgName() + " category: " +
						temp.getCategory() + " weight: " + temp.getWeight() + "" +
						" Date Range: " + temp.getDate() + " into formattedDonationListWeightsNotAdded");
				formattedDonationListWeightsNotAdded.add(temp);
			}
			formattedDonationListWeightsAdded = addWeightsSummary(formattedDonationListWeightsNotAdded);
		}
		else//weekly
		{
			for (Donation x:donations)
			{
				dateRange = x.getDate().substring(0,4) + "-" + x.getDate().substring(5,7) + "-" +  x.getDate().substring(8,10);
				Donation temp = new Donation(x.getOrgName(), "", x.getWeight(), dateRange);
				System.out.println("Passing Donation: " + temp.getOrgName() + " category: " +
						temp.getCategory() + " weight: " + temp.getWeight() + "" +
						" Date Range: " + temp.getDate() + " into formattedDonationListWeightsNotAdded");
				formattedDonationListWeightsNotAdded.add(temp);
			}
			//format times correctly
			
			List<Donation> formattedDonationListWeightsNotAddedTemp = new ArrayList<Donation>();
			for (Donation x:formattedDonationListWeightsNotAdded)
			{
				Donation temp = new Donation(x.getOrgName(), "", x.getWeight(), x.getDate());
				formattedDonationListWeightsNotAddedTemp.add(temp);
			}
			formattedDonationListWeightsNotAdded = new ArrayList<Donation>();
			for (Donation x:formattedDonationListWeightsNotAddedTemp)
			{
				//extract date
				System.out.println("Using Donation in formattedDonationListWeightsNotAdded: " + x.getOrgName() + " category: " +
						x.getCategory() + " weight: " + x.getWeight() + "" +
						" Date Range: " + x.getDate());
				String tempYear = x.getDate().substring(0,4);
				String tempMonth = x.getDate().substring(5,7);
				String tempDay = x.getDate().substring(8,10);
				Calendar tempCalendarDate = Calendar.getInstance();
				tempCalendarDate.set(Integer.parseInt(tempYear), Integer.parseInt(tempMonth)-1, Integer.parseInt(tempDay));
				for (int i =0; i < dates.length; i++)
				{
					String datesTempYear = dates[i].substring(0,4);
					String datesTempMonth = dates[i].substring(5,7);
					String datesTempDay = dates[i].substring(8,10);
					System.out.println("TempYear: " + tempYear + " datesTempYear: " + datesTempYear);
					System.out.println("TempYear: " + tempYear + " 2nd Year: " + dates[i].substring(13,17));
					Calendar datesTempCalendarDate = Calendar.getInstance();
					datesTempCalendarDate.set(Integer.parseInt(datesTempYear), Integer.parseInt(datesTempMonth)-1, Integer.parseInt(datesTempDay));
					if (tempYear.equals(datesTempYear))
					{
						if (tempCalendarDate.get(Calendar.WEEK_OF_YEAR) == datesTempCalendarDate.get(Calendar.WEEK_OF_YEAR))
						{
							Donation temp = new Donation(x.getOrgName(), "", x.getWeight(), dates[i]);
							formattedDonationListWeightsNotAdded.add(temp);
							break;
						}
					}
					else if(tempYear.equals(dates[i].substring(13,17)))
					{
						System.out.println("Entered else if statement");
						datesTempYear = dates[i].substring(13,17);
						datesTempMonth = dates[i].substring(18,20);
						datesTempDay = dates[i].substring(21,23);
						datesTempCalendarDate = Calendar.getInstance();
						datesTempCalendarDate.set(Integer.parseInt(datesTempYear), Integer.parseInt(datesTempMonth)-1, Integer.parseInt(datesTempDay));
						if (tempCalendarDate.get(Calendar.WEEK_OF_YEAR) == datesTempCalendarDate.get(Calendar.WEEK_OF_YEAR))
						{
							Donation temp = new Donation(x.getOrgName(), "", x.getWeight(), dates[i]);
							formattedDonationListWeightsNotAdded.add(temp);
							break;
						}
					}
				}
			}
			//add weights together
			for (Donation x: formattedDonationListWeightsNotAdded)
			{
				System.out.println("Placing Donation in formattedDonationListWeightsNotAdded: " + x.getOrgName() + " category: " +
						x.getCategory() + " weight: " + x.getWeight() + "" +
						" Date Range: " + x.getDate() + " into addWeightsSummary");
			}
			formattedDonationListWeightsAdded = addWeightsSummary(formattedDonationListWeightsNotAdded);
		}
		return formattedDonationListWeightsAdded;
	}

	private List<Donation> formatDescriptiveDonationList(List<Donation> donations, String[] dates, int time)
	{
		//Create a new Donation List with correct date parameter from the dates array
		String dateRange ="";
		List<Donation> formattedDonationListWeightsNotAdded = new ArrayList<Donation>();
		List<Donation> formattedDonationListWeightsAdded = new ArrayList<Donation>();
		if (time == 2)//yearly
		{
			for (Donation x:donations)
			{
				dateRange = x.getDate().substring(0,4);
				Donation temp = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), dateRange);
				System.out.println("Passing Donation: " + temp.getOrgName() + " category: " +
						temp.getCategory() + " weight: " + temp.getWeight() + "" +
						" Date Range: " + temp.getDate() + " into formattedDonationListWeightsNotAdded");
				formattedDonationListWeightsNotAdded.add(temp);
			}
			//add Weights
			formattedDonationListWeightsAdded = addWeightsDescriptive(formattedDonationListWeightsNotAdded);
		}
		else if (time == 1)//monthly
		{
			for (Donation x:donations)
			{
				dateRange = x.getDate().substring(0,4) + "-" + x.getDate().substring(5,7);
				Donation temp = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), dateRange);
				formattedDonationListWeightsNotAdded.add(temp);
			}
			formattedDonationListWeightsAdded = addWeightsDescriptive(formattedDonationListWeightsNotAdded);
		}
		else if (time == 3)
		{
			for (Donation x:donations)
			{
				dateRange = x.getDate();
				Donation temp = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), dateRange);
				System.out.println("Passing Donation: " + temp.getOrgName() + " category: " +
						temp.getCategory() + " weight: " + temp.getWeight() + "" +
						" Date Range: " + temp.getDate() + " into formattedDonationListWeightsNotAdded");
				formattedDonationListWeightsNotAdded.add(temp);
			}
			formattedDonationListWeightsAdded = addWeightsDescriptive(formattedDonationListWeightsNotAdded);
		}
		else//weekly
		{
			for (Donation x:donations)
			{
				dateRange = x.getDate().substring(0,4) + "-" + x.getDate().substring(5,7) + "-" +  x.getDate().substring(8,10);
				Donation temp = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), dateRange);
				formattedDonationListWeightsNotAdded.add(temp);
			}
			//format times correctly
			List<Donation> formattedDonationListWeightsNotAddedTemp = new ArrayList<Donation>();
			for (Donation x:formattedDonationListWeightsNotAdded)
			{
				Donation temp = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), x.getDate());
				formattedDonationListWeightsNotAddedTemp.add(temp);
				System.out.println("Temp: " + temp.getOrgName() + " Category: " + temp.getCategory());
			}
			formattedDonationListWeightsNotAdded = new ArrayList<Donation>();
			for (Donation x:formattedDonationListWeightsNotAddedTemp)
			{
				//extract date
				String tempYear = x.getDate().substring(0,4);
				String tempMonth = x.getDate().substring(5,7);
				String tempDay = x.getDate().substring(8,10);
				Calendar tempCalendarDate = Calendar.getInstance();
				tempCalendarDate.set(Integer.parseInt(tempYear), Integer.parseInt(tempMonth)-1, Integer.parseInt(tempDay));
				for (int i =0; i < dates.length; i++)
				{
					String datesTempYear = dates[i].substring(0,4);
					String datesTempMonth = dates[i].substring(5,7);
					String datesTempDay = dates[i].substring(8,10);
					Calendar datesTempCalendarDate = Calendar.getInstance();
					datesTempCalendarDate.set(Integer.parseInt(datesTempYear), Integer.parseInt(datesTempMonth)-1, Integer.parseInt(datesTempDay));
					if (tempYear.equals(datesTempYear))
					{
						if (tempCalendarDate.get(Calendar.WEEK_OF_YEAR) == datesTempCalendarDate.get(Calendar.WEEK_OF_YEAR))
						{
							Donation temp = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), dates[i]);
							System.out.println("Temp: " + temp.getOrgName() + " Category: " + temp.getCategory());
							formattedDonationListWeightsNotAdded.add(temp);
							break;
						}
					}
					else if(tempYear.equals(dates[i].substring(13,17)))
					{
						datesTempYear = dates[i].substring(13,17);
						datesTempMonth = dates[i].substring(18,20);
						datesTempDay = dates[i].substring(21,23);
						datesTempCalendarDate = Calendar.getInstance();
						datesTempCalendarDate.set(Integer.parseInt(datesTempYear), Integer.parseInt(datesTempMonth)-1, Integer.parseInt(datesTempDay));
						if (tempCalendarDate.get(Calendar.WEEK_OF_YEAR) == datesTempCalendarDate.get(Calendar.WEEK_OF_YEAR))
						{
							Donation temp = new Donation(x.getOrgName(), x.getCategory(), x.getWeight(), dates[i]);
							formattedDonationListWeightsNotAdded.add(temp);
							break;
						}
					}
				}
			}
			//add weights together
			formattedDonationListWeightsAdded = addWeightsDescriptive(formattedDonationListWeightsNotAdded);
		}
		return formattedDonationListWeightsAdded;
	}

	private String[] findDateArray(int time, String start_date, String end_date, List<Donation> donationsTimeSorted) {
		String[] timeRangeArray = new String[1];
		String firstTs = donationsTimeSorted.get(0).getDate();
		String firstTsYear = firstTs.substring(0,4);
		String firstTsMonth = firstTs.substring(5,7);
		String firstTsDay = firstTs.substring(8,10);
		String lastTs = donationsTimeSorted.get(donationsTimeSorted.size() - 1).getDate();
		String lastTsYear = lastTs.substring(0,4);
		String lastTsMonth = lastTs.substring(5,7);
		String lastTsDay = lastTs.substring(8,10);
		if (time == 0)
		{
			Calendar[] calendarArray = new Calendar[1];
			calendarArray = getCalendarArray(firstTsYear, firstTsMonth, firstTsDay, lastTsYear, lastTsMonth, lastTsDay);
			List<String> timeList = new ArrayList<String>();
			timeList = getWeeklyTimeRange(calendarArray[0], calendarArray[1]);
			System.out.println(timeList);
			timeRangeArray = new String[timeList.size()];
			timeRangeArray = timeList2Array(timeList);
		}
		else if (time == 1)
		{
			int MonthsSpanned = 0;
			MonthsSpanned = getMonthsSpanned(firstTsYear, lastTsYear, firstTsMonth, lastTsMonth);
			timeRangeArray = new String[MonthsSpanned+1];//timeRangeArray is used to check which year to put weights into.
			timeRangeArray = getMonthTimeRange(MonthsSpanned, firstTsYear, firstTsMonth, firstTs, lastTs);
		}
		else if (time == 3)
		{
			timeRangeArray = new String[1];
			timeRangeArray = getTimeArrayDaily(firstTs, lastTs);
		}
		else
		{
			timeRangeArray = getTimeArrayYearly(firstTsYear, lastTsYear);
		}
		return timeRangeArray;
	}
	
	private String[] getTimeArrayDaily(String firstDate, String lastDate)
	{
		Calendar cStart = Calendar.getInstance();
		cStart.set(Integer.parseInt(firstDate.substring(0,4)), Integer.parseInt(firstDate.substring(5,7))-1, Integer.parseInt(firstDate.substring(8,10)));
		Calendar cFinish = Calendar.getInstance();
		cFinish.set(Integer.parseInt(lastDate.substring(0,4)), Integer.parseInt(lastDate.substring(5,7))-1, Integer.parseInt(lastDate.substring(8,10)));
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] timeArray = new String[1];
		if (firstDate.equals(lastDate))
		{
			timeArray[0] = firstDate;
		}
		else
		{
			List<String> timeList = new ArrayList<String>();
			while(!(cStart.equals(cFinish)))
			{
				String dateToAdd = format1.format(cStart.getTime());
				timeList.add(dateToAdd);
				cStart.add(Calendar.DATE, 1);
			}
			String dateToAdd = format1.format(cStart.getTime());
			timeList.add(dateToAdd);
			int numDays = timeList.size();
			timeArray = new String[numDays];
			for (int i = 0; i < numDays; i++)
			{
				timeArray[i] = timeList.get(i);
			}
		}
		return timeArray;
	}

	private String[] getTimeArrayYearly(String firstTsYear, String lastTsYear)
	{
		int YearsSpanned = Integer.parseInt(lastTsYear) - Integer.parseInt(firstTsYear);
		String timeRangeArray[] = new String[YearsSpanned+1];
		if (YearsSpanned == 0)
		{
			timeRangeArray[0] = lastTsYear;
		}
		else
		{
			int tempYear = Integer.parseInt(firstTsYear);
			for (int i = 0; i<= YearsSpanned;i++)
			{
				tempYear += i;
				timeRangeArray[i] = Integer.toString(tempYear);
			}
		}
		return timeRangeArray;
	}

	private int getMonthsSpanned(String firstTsYear, String lastTsYear, String firstTsMonth, String lastTsMonth)
	{
		int MonthsSpanned = 0;
		int lastYear = Integer.parseInt(lastTsYear);
		int firstYear = Integer.parseInt(firstTsYear);
		if(Integer.parseInt(lastTsYear) - Integer.parseInt(firstTsYear)<0)
		{
			return -1;
		}
		if(Integer.parseInt(lastTsYear) - Integer.parseInt(firstTsYear) !=0)
		{
			MonthsSpanned = Integer.parseInt(lastTsMonth) + (Integer.parseInt(lastTsYear) - Integer.parseInt(firstTsYear) - 1)*12;
		}
		if (Integer.parseInt(lastTsMonth) - Integer.parseInt(firstTsMonth) < 0)
		{
			MonthsSpanned += (12 - Integer.parseInt(firstTsMonth));
		}
		else
		{
			MonthsSpanned += (Integer.parseInt(lastTsMonth) - Integer.parseInt(firstTsMonth));
		}
		return MonthsSpanned;
	}

	private Calendar[] getCalendarArray(String firstTsYear, String firstTsMonth, String firstTsDay, String lastTsYear, String lastTsMonth, String lastTsDay)
	{
		Calendar cStart = Calendar.getInstance();
		cStart.set(Integer.parseInt(firstTsYear), Integer.parseInt(firstTsMonth)-1, Integer.parseInt(firstTsDay));
		Calendar cFinish = Calendar.getInstance();
		cFinish.set(Integer.parseInt(lastTsYear), Integer.parseInt(lastTsMonth)-1, Integer.parseInt(lastTsDay));
		Calendar[] calendarArray = new Calendar[2];
		calendarArray[0] = cStart;
		calendarArray[1] = cFinish;
		return calendarArray;
	}


	private String[] timeList2Array(List<String> timeList)
	{
		String timeRangeArray[] = new String[timeList.size()];
		for (int i = 0; i < timeList.size(); i++)
		{
			timeRangeArray[i] = timeList.get(i);
		}
		return timeRangeArray;
	}

	private String[] getMonthTimeRange(int MonthsSpanned, String firstTsYear, String firstTsMonth, String firstTs, String lastTs)
	{
		String timeRangeArray[] = new String[MonthsSpanned+1];
		if (MonthsSpanned == 0)
		{
			timeRangeArray[0] = lastTs.substring(0,7);
		}
		else
		{
			System.out.println("Months Spanned = " + MonthsSpanned);
			int tempYear = Integer.parseInt(firstTsYear);
			int tempMonth = Integer.parseInt(firstTsMonth);
			for (int i = 0; i<= MonthsSpanned;i++)
			{
				if (i == 0)
				{
					timeRangeArray[0] = firstTs.substring(0,7);
				}
				else
				{
					tempMonth++;
					if(tempMonth == 13)
					{
						tempMonth = 1;
						tempYear++;
					}
					if (tempMonth < 10)
					{
						timeRangeArray[i] = (Integer.toString(tempYear) + "-0" + Integer.toString(tempMonth));
					}
					else
					{
						timeRangeArray[i] = (Integer.toString(tempYear) + "-" + Integer.toString(tempMonth));
					}
				}
			}
		}
		for (int i = 0;i<timeRangeArray.length;i++)
		{
			System.out.println(timeRangeArray[i]);
		}
		return timeRangeArray;
	}

	private List<String> getWeeklyTimeRange(Calendar cStart, Calendar cFinish)
	{
		int dayOfWeek = cStart.get(Calendar.DAY_OF_WEEK);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedBeginning = format1.format(cStart.getTime());
		String formattedEnd ="";
		String toAdd="";
		String timePrintStart ="";
		List<String> timeList = new ArrayList<String>();
		timePrintStart = format1.format(cStart.getTime());
		System.out.println(timePrintStart);
		String timePrintFinish = format1.format(cFinish.getTime());
		System.out.println(timePrintFinish);
		int count = 0;
		boolean enteredWhile = false;
		while (!timePrintStart.equals(timePrintFinish))
		{
			enteredWhile = true;
			cStart.add(Calendar.DATE, 1);
			timePrintStart = format1.format(cStart.getTime());
			System.out.println(timePrintStart);
			if (cStart.get(Calendar.DAY_OF_WEEK) == 7)
			{
				formattedEnd = format1.format(cStart.getTime());
				toAdd = formattedBeginning + " - " + formattedEnd;
				timeList.add(toAdd);
			}
			if (cStart.get(Calendar.DAY_OF_WEEK) == 1)
			{
				formattedBeginning = format1.format(cStart.getTime());
			}
			if (timePrintStart.equals(timePrintFinish))
			{
				formattedEnd = format1.format(cStart.getTime());
				toAdd = formattedBeginning + " - " + formattedEnd;
				timeList.add(toAdd);
			}

		}
		if (!enteredWhile)
		{
			formattedEnd = format1.format(cStart.getTime());
			toAdd = formattedBeginning + " - " + formattedEnd;
			timeList.add(toAdd);
		}
		return timeList;
	}
/*
	private List<SummaryReport> fillWeeklySummaryList(List<String> timeList, List<Donation> donationsSummary)
	{
		int donationListSize = donationsSummary.size();
		List<SummaryReport> reportList = new ArrayList<SummaryReport>();
		String currentOrgsTimeRangeYear = "";
		String currentOrgsTimeRangeMonth = "";
		String currentOrgsTimeRangeDay = "";
		String timeListYear = "";
		String timeListMonth = "";
		String timeListDay = "";
		Calendar cTimeList = Calendar.getInstance();
		int tempWeight = 0;
		int reportListIndex = 0;
		for (int i = 0; i < donationListSize; i++)
		{

			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			SummaryReport tempSummary = new SummaryReport();
			currentOrgsTimeRangeYear = donationsSummary.get(i).getDate();
			currentOrgsTimeRangeYear = currentOrgsTimeRangeYear.substring(0,4);
			currentOrgsTimeRangeMonth = donationsSummary.get(i).getDate();
			currentOrgsTimeRangeMonth = currentOrgsTimeRangeMonth.substring(5,7);
			currentOrgsTimeRangeDay = donationsSummary.get(i).getDate();
			currentOrgsTimeRangeDay = currentOrgsTimeRangeDay.substring(8,10);
			Calendar cTemp = Calendar.getInstance();
			cTemp.set(Integer.parseInt(currentOrgsTimeRangeYear), Integer.parseInt(currentOrgsTimeRangeMonth)-1, Integer.parseInt(currentOrgsTimeRangeDay));
			System.out.println("cTemp time: " + format2.format(cTemp.getTime()));
			int tempIdx = 0;
			for (tempIdx = 0; tempIdx < timeList.size(); tempIdx++)
			{
				timeListYear = timeList.get(tempIdx);
				timeListYear = timeListYear.substring(0,4);
				timeListMonth = timeList.get(tempIdx);
				timeListMonth = timeListMonth.substring(5,7);
				timeListDay = timeList.get(tempIdx);
				timeListDay = timeListDay.substring(8,10);
				cTimeList.set(Integer.parseInt(timeListYear), Integer.parseInt(timeListMonth)-1, Integer.parseInt(timeListDay));
				if (cTimeList.get(Calendar.WEEK_OF_YEAR) == cTemp.get(Calendar.WEEK_OF_YEAR))
				{
					tempSummary.setTimeRange(timeList.get(tempIdx));
					break;
				}
			}
			if (i == 0)
			{
				tempSummary.setOrg(donationsSummary.get(i).getOrgName());
				tempSummary.setWeight(donationsSummary.get(i).getWeight());
				reportList.add(tempSummary);
			}
			else
			{
				System.out.println(i);
				System.out.println("Report List Index: " + reportListIndex);
				System.out.println("Temp Summary Time Range: " + tempSummary.getTimeRange());
				if (donationsSummary.get(i).getOrgName().equals(reportList.get(reportListIndex).getOrg()) && tempSummary.getTimeRange().equals(reportList.get(reportListIndex).getTimeRange()))
				{
					tempWeight = reportList.get(reportListIndex).getWeight();
					tempWeight += donationsSummary.get(i).getWeight();
					reportList.get(reportListIndex).setWeight(tempWeight);
				}
				else
				{
					reportListIndex++;
					tempSummary.setOrg(donationsSummary.get(i).getOrgName());
					tempSummary.setWeight(donationsSummary.get(i).getWeight());
					reportList.add(tempSummary);
				}
			}
		}
		return reportList;
	}
	private List<DetailedReport> fillWeeklyDetailedList(List<String> timeList, List<Donation> donations)
	{
		int donationListSize = donations.size();
		List<DetailedReport> reportList = new ArrayList<DetailedReport>();
		String currentOrgsTimeRangeYear = "";
		String currentOrgsTimeRangeMonth = "";
		String currentOrgsTimeRangeDay = "";
		String timeListYear = "";
		String timeListMonth = "";
		String timeListDay = "";
		Calendar cTimeList = Calendar.getInstance();
		int tempWeight = 0;
		int reportListIndex = 0;
		for (int i = 0; i < donationListSize; i++)
		{

			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			DetailedReport tempDetailed = new DetailedReport();
			currentOrgsTimeRangeYear = donations.get(i).getDate();
			currentOrgsTimeRangeYear = currentOrgsTimeRangeYear.substring(0,4);
			currentOrgsTimeRangeMonth = donations.get(i).getDate();
			currentOrgsTimeRangeMonth = currentOrgsTimeRangeMonth.substring(5,7);
			currentOrgsTimeRangeDay = donations.get(i).getDate();
			currentOrgsTimeRangeDay = currentOrgsTimeRangeDay.substring(8,10);
			Calendar cTemp = Calendar.getInstance();
			cTemp.set(Integer.parseInt(currentOrgsTimeRangeYear), Integer.parseInt(currentOrgsTimeRangeMonth)-1, Integer.parseInt(currentOrgsTimeRangeDay));
			System.out.println("cTemp time: " + format2.format(cTemp.getTime()));
			int tempIdx = 0;
			for (tempIdx = 0; tempIdx < timeList.size(); tempIdx++)
			{
				timeListYear = timeList.get(tempIdx);
				timeListYear = timeListYear.substring(0,4);
				timeListMonth = timeList.get(tempIdx);
				timeListMonth = timeListMonth.substring(5,7);
				timeListDay = timeList.get(tempIdx);
				timeListDay = timeListDay.substring(8,10);
				cTimeList.set(Integer.parseInt(timeListYear), Integer.parseInt(timeListMonth)-1, Integer.parseInt(timeListDay));
				if (cTimeList.get(Calendar.WEEK_OF_YEAR) == cTemp.get(Calendar.WEEK_OF_YEAR))
				{
					tempDetailed.setTimeRange(timeList.get(tempIdx));
					break;
				}
			}
			if (i == 0)
			{
				tempDetailed.setOrg(donations.get(i).getOrgName());
				tempDetailed.setWeight(donations.get(i).getWeight());
				tempDetailed.setCategory(donations.get(i).getCategory());
				reportList.add(tempDetailed);
			}
			else
			{
				System.out.println(i);
				System.out.println("Report List Index: " + reportListIndex);
				System.out.println("Temp Detailed Time Range: " + tempDetailed.getTimeRange());
				if (donations.get(i).getOrgName().equals(reportList.get(reportListIndex).getOrg()) && donations.get(i).getCategory().equals(reportList.get(reportListIndex).getCategory()) && tempDetailed.getTimeRange().equals(reportList.get(reportListIndex).getTimeRange()))
				{
					tempWeight = reportList.get(reportListIndex).getWeight();
					tempWeight += donations.get(i).getWeight();
					reportList.get(reportListIndex).setWeight(tempWeight);
				}
				else
				{
					reportListIndex++;
					tempDetailed.setOrg(donations.get(i).getOrgName());
					tempDetailed.setWeight(donations.get(i).getWeight());
					tempDetailed.setCategory(donations.get(i).getCategory());
					reportList.add(tempDetailed);
				}
			}
		}
		return reportList;
	}
*/
	private List<Donation> insertZeros(List<Donation> donationListToReturn, String[] timeRangeArray)
	{
		for (int i = 0; i < timeRangeArray.length; i++)
		{
			System.out.println("Time Range Array Passed into insert Zeros" + timeRangeArray[i]);
		}
		int donationListSize = donationListToReturn.size();
		int donationListIdx = 0;
		int timeRangeArrayIdx = 0;
		int timeRangeArrayIdxLength = timeRangeArray.length;
		Donation lastElem = new Donation();
		lastElem = donationListToReturn.get(donationListSize-1);
		Donation lastObjectUsed = new Donation();
		lastObjectUsed = donationListToReturn.get(0);
		if (donationListSize == 1) {
			return donationListToReturn;
		}
		while (!donationListToReturn.get(donationListIdx).equals(lastElem))
		{
			if (!(lastObjectUsed.getOrgName().equals(donationListToReturn.get(donationListIdx).getOrgName())) || !(lastObjectUsed.getCategory().equals(donationListToReturn.get(donationListIdx).getCategory())))
			{
				for (int i = timeRangeArrayIdx; i< timeRangeArrayIdxLength; i++)
				{
					Donation tempDonation = new Donation();
					tempDonation.setOrgName(lastObjectUsed.getOrgName());
					tempDonation.setDate(timeRangeArray[timeRangeArrayIdx]);
					tempDonation.setCategory(lastObjectUsed.getCategory());
					tempDonation.setWeight(0);
					donationListToReturn.add(donationListIdx, tempDonation);
					timeRangeArrayIdx++;
					lastObjectUsed = donationListToReturn.get(donationListIdx);
					donationListIdx++;
				}
			}
			if (timeRangeArrayIdx == timeRangeArrayIdxLength)
			{
				timeRangeArrayIdx = 0;
			}
			boolean foundTime = false;
			while (!foundTime)
			{
				//test for Shawn breaking my code
				System.out.println("Ts: " + donationListToReturn.get(donationListIdx).getDate());
				System.out.println("Donation List To return Size: " + donationListToReturn.size() + " Donation List IDX = " + donationListIdx + " TimeRange Array Length = " + timeRangeArray.length + "Time Range Array IDX = " + timeRangeArrayIdx);
				if(donationListToReturn.get(donationListIdx).getDate().equals(timeRangeArray[timeRangeArrayIdx]))
				{
					lastObjectUsed = donationListToReturn.get(donationListIdx);
					foundTime = true;
					timeRangeArrayIdx++;
					donationListIdx++;
				}
				else
				{
					Donation tempDonation = new Donation();
					tempDonation.setOrgName(donationListToReturn.get(donationListIdx).getOrgName());
					tempDonation.setDate(timeRangeArray[timeRangeArrayIdx]);
					tempDonation.setCategory(donationListToReturn.get(donationListIdx).getCategory());
					tempDonation.setWeight(0);
					donationListToReturn.add(donationListIdx, tempDonation);
					timeRangeArrayIdx++;
					lastObjectUsed = donationListToReturn.get(donationListIdx);
					donationListIdx++;
				}
			}
		}
		
		
		Donation currentElem = donationListToReturn.get(donationListIdx-1);
		// this fills out the rest of the second to last row
		if (!(currentElem.getOrgName().equals(lastElem.getOrgName()) && currentElem.getCategory().equals(lastElem.getCategory()))) {
			for (int i = timeRangeArrayIdx; i < timeRangeArray.length; i++) {
				Donation temp = new Donation(currentElem.getOrgName(), currentElem.getCategory(), 0, timeRangeArray[i]);
				donationListToReturn.add(donationListIdx, temp);
				donationListIdx++;
			}
			for (int i = 0; i < timeRangeArray.length; i++) {
				if (!timeRangeArray[i].equals(lastElem.getDate())) {
					Donation temp = new Donation(lastElem.getOrgName(), lastElem.getCategory(), 0, timeRangeArray[i]);
					donationListToReturn.add(donationListIdx, temp);
					donationListIdx++;
				}
				else {
					donationListIdx++;
				}
			}
		}
		else {
			int index2use = 0;
			for (int i = 0; i < timeRangeArray.length; i++) {
				if (currentElem.getDate().equals(timeRangeArray[i])) {
					index2use = i;
					break;
				}
			}
			for (int i = index2use + 1; i < timeRangeArray.length; i++) {
				if (!timeRangeArray[i].equals(lastElem.getDate())) {
					Donation temp = new Donation(currentElem.getOrgName(), currentElem.getCategory(), 0, timeRangeArray[i]);
					donationListToReturn.add(donationListIdx, temp);
					donationListIdx++;
				}
				else {
					donationListIdx++;
				}
			}
		}
		
		//search through list for the last elements org name and category
		/*
		int index2use = donationListToReturn.size()-1;
		while(donationListToReturn.get(index2use).getOrgName().equals(lastElem.getOrgName()) && donationListToReturn.get(index2use).getCategory().equals(lastElem.getCategory()))
		{
			index2use--;
		}
		for(int i = 0; i < timeRangeArrayIdxLength; i++)
		{
			if(!(donationListToReturn.get(index2use).getDate().equals(timeRangeArray[i])))
			{
				Donation temp = new Donation(lastElem.getOrgName(), lastElem.getCategory(), 0, timeRangeArray[i]);
				donationListToReturn.add(index2use, temp);
				index2use++;
			}
			else
			{
				index2use++;
			}
		}
		for (int i = timeRangeArrayIdx; i< timeRangeArrayIdxLength; i++)
		{
			Donation tempDonation = new Donation();
			tempDonation.setOrgName(lastObjectUsed.getOrgName());
			tempDonation.setDate(timeRangeArray[timeRangeArrayIdx]);
			System.out.println("Temp Donation is :" + tempDonation.getDate());
			System.out.println("Last Element is :" + lastElem.getDate());
			if (tempDonation.getDate().equals(lastElem.getDate()))
			{
				break;
			}
			tempDonation.setCategory(lastObjectUsed.getCategory());
			tempDonation.setWeight(0);
			donationListToReturn.add(donationListIdx, tempDonation);
			timeRangeArrayIdx++;
			lastObjectUsed = donationListToReturn.get(donationListIdx);
			donationListIdx++;
		}*/
		return donationListToReturn;
	}
}