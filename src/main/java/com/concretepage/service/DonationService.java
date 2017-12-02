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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concretepage.dao.IntDonationDAO;
import com.concretepage.entity.OrgStats;

@Service
public class DonationService implements IntDonationService {
	@Autowired
	private IntDonationDAO donationDAO;

	/**
	 * Create separate donations from bulk donation payload from UI
	 */
	@Override
	public String separateDonations(int donation, String org_name, String user_name,
			int deli, int dairy, int meat, int produce, int pantry, int bakery, String page)
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
		
		//iterate over mp to check for donations with values > 0. If so, input to database
		for (String category : mp.keySet()){
			int weight = mp.get(category);
			if (weight > 0) {
				donationDAO.inputDonation(org_name, user_name, category, weight, donation);
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
}