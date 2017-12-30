package com.concretepage.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.Donation;
import com.concretepage.entity.SummaryReport;
import com.concretepage.entity.DetailedReport;
import com.concretepage.entity.Frequency;

@Transactional
@Repository
public class DonationDAO implements IntDonationDAO {
	
	

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public int inputDonation(String org_name, String user_name, String category, int weight, int donation, String date) {
		
		Query query = entityManager.createNativeQuery(
				"INSERT INTO donation_table SET "
				+ "org_id=(SELECT org_id FROM org_table WHERE "
				+ "org_name = '" + org_name + "'), "
				+ "org_name = '" + org_name
				+ "', category = '" + category
				+ "', weight = '" + weight
				+ "', donation = '" + donation 
				+ "', user_name = '" + user_name 
				+ "', date = '" + date + "';");
		
		query.executeUpdate();
		return 0;
	}
//select distinct org_name from donation_table where (ts between '2017-10-15 00:00:00' and '2017-10-15 23:59:59') and user_name = 'Adam';
	@Override
	public List<String> getWidgetOrgs(String date, String username)
	{
		Query query = entityManager.createNativeQuery(
				"SELECT DISTINCT org_name FROM donation_table WHERE " +
						"(ts between '" + date + " 00:00:00' and '" + date + " 23:59:59') " +
						"and user_name = '" + username +"';");

		List<String> orgs = query.getResultList();
		return orgs;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> listOrg(int donation) {
		
		Query query = entityManager.createNativeQuery(
				"SELECT DISTINCT org_name FROM donation_table WHERE " +
				"donation='" + donation + "';");
		
		List<String> orgs = query.getResultList();
		return orgs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Donation> listInfo(String org_name, int donation) {
		
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM donation_table WHERE org_name='" + 
				org_name + "' AND donation='" + donation + "';", Donation.class);
		
		List<Donation> donations = query.getResultList();
		return donations;
	}

	@Override
	public void initDonationTable() {
		
		Query query = entityManager.createNativeQuery(
				"CREATE TABLE IF NOT EXISTS `donation_table` (" +
				" `order_id` int(5) NOT NULL AUTO_INCREMENT," +
				" `org_id` int(5) NOT NULL," +
				" `org_name` TINYTEXT NOT NULL," +
				" `category` TINYTEXT NOT NULL," +
				" `weight` int(7) NOT NULL," +
				" `donation` int(1) NOT NULL," +
				" PRIMARY KEY (`order_id`)," +
				" FOREIGN KEY (`org_id`) REFERENCES org_table(org_id)" +
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFrequency(String org_name)
	{

		org_name=org_name.replaceAll("%20"," ");
		org_name=org_name.replaceAll("org_name=","");
		Query query = entityManager.createNativeQuery(
				"SELECT category, count(*) freq FROM donation_table"
						+ " WHERE org_name='" + org_name + "' GROUP BY category ORDER BY freq DESC;", Frequency.class);

		List<Frequency> frequencyList = query.getResultList();
		List<String> categories = new ArrayList<String>();
		String[] categoryNames = new String[8];
		categoryNames[0] = "deli";
		categoryNames[1] = "dairy";
		categoryNames[2] = "meat";
		categoryNames[3] = "produce";
		categoryNames[4] = "pantry";
		categoryNames[5] = "bakery";
		categoryNames[6] = "pet food";
		categoryNames[7] = "nonfood";
		for (int i = 0; i < frequencyList.size(); i++)
		{
			categories.add(frequencyList.get(i).getCategory());
			//System.out.println(frequencyList.get(i).getCategory());
		}
		boolean broken = false;
		for (int i = 0; i<categoryNames.length;i++)
		{
			broken = false;
			for(String temp:categories)
			{
				if (temp.equalsIgnoreCase(categoryNames[i]))
				{
					broken=true;
					break;
				}
			}
			if (broken == false)
			{
				categories.add(categoryNames[i]);
			}
		}
		System.out.println("Org Name Entered: " + org_name);
		//System.out.println(categories.get(1));
		return categories;
	}

	@Override
	public int inputPage(String user_name, String page) {
		Query query = entityManager.createNativeQuery(
				"INSERT INTO redirection_table SET "
						+ "user_name = '" + user_name
						+ "', page = '" + page
						+ "';");

		query.executeUpdate();
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getUserPage(String user_name, String date) {
		Query query = entityManager.createNativeQuery(
				"SELECT DISTINCT page FROM redirection_table WHERE " +
				"(ts between '" + date + " 00:00:00' and '" + date + " 23:59:59') " +
				"and user_name = '" + user_name +"';");
		
		List<String> pages = query.getResultList();
		if (pages.isEmpty()) {
			System.out.println("Ooga booga");
			return "a";
		} else {
			return pages.get(0);
		}
	}

	@Override
	public int inputReportPrediction(String user_name, String tr, String io, String sd) {
		Query query = entityManager.createNativeQuery(
				"INSERT INTO reports_table SET "
						+ "user_name = '" + user_name
						+ "', tr_tab = '" + tr
						+ "', io_tab = '" + io
						+ "', sd_tab = '" + sd
						+ "';");

		query.executeUpdate();
		return 0;
	}
	
//	.___  __         .__  .__                  _________              __  .__               
//	|   |/  |______  |  | |__|____    ____    /   _____/ ____   _____/  |_|__| ____   ____  
//	|   \   __\__  \ |  | |  \__  \  /    \   \_____  \_/ __ \_/ ___\   __\  |/  _ \ /    \ 
//	|   ||  |  / __ \|  |_|  |/ __ \|   |  \  /        \  ___/\  \___|  | |  (  <_> )   |  \
//	|___||__| (____  /____/__(____  /___|  / /_______  /\___  >\___  >__| |__|\____/|___|  /
//	               \/             \/     \/          \/     \/     \/                    \/ 

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getReport(int donation, int time, int type, String start_date, String end_date) {
		//time == 2 means yearly report time == 1 means monthly report time == 0 means weekly report
		//type == 1 means they want a summary report i.e no food categories, just total weight
		//type == 0 means they want a descriptive report i.e they want to tsee food categories
		System.out.println("\n" + "Start Date: " + start_date);
		System.out.println("\n" + "end Date: " + end_date);
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM `donation_table`"
				+ " WHERE (date >= '" + start_date + "' AND date <= '"
				+ end_date + "') AND "
				+ "donation=" + donation + " ORDER BY org_name, category, date;", Donation.class);
		/*Query query = entityManager.createNativeQuery(
				"SELECT * FROM `donation_table`"
				+ " WHERE (ts BETWEEN '" + start_date + " 00:00:00' AND '"
				+ end_date + " 00:00:00') AND "
				+ "donation=" + donation + " ORDER BY org_name, category;", Donation.class);*/

		Query querySummary = entityManager.createNativeQuery(
				"SELECT * FROM `donation_table`"
						+ " WHERE (date >= '" + start_date + "' AND date <= '"
						+ end_date + "') AND "
						+ "donation=" + donation + " ORDER BY org_name, date;", Donation.class);

		Query queryTimeSorted = entityManager.createNativeQuery(
				"SELECT * FROM `donation_table`"
						+ " WHERE (date >= '" + start_date + " ' AND date <= '"
						+ end_date + "') AND "
						+ "donation=" + donation + " ORDER BY date;", Donation.class);

		List<Donation> donations = query.getResultList();
		List<Donation> donationsSummary = querySummary.getResultList();
		List<Donation> donationsTimeSorted = queryTimeSorted.getResultList();
		int donationListSize = 0;
		int dontationTSListSize = 0;
		String temp;
		String tempCategory = "";
		String firstTs = "";
		String lastTs ="";
		int currentOrgsWeight = 0;
		if (donations.isEmpty() || donationsSummary.isEmpty())
		{
			System.out.println("returned null");
			return null;
		}
		if(donations.get(0) != null)
		{
			donationListSize = donations.size();
			dontationTSListSize = donationsTimeSorted.size();
			//figure out the times
			/*
			firstTs = donations.get(0).getTs();
			String firstTsYear = firstTs.substring(0,4);
			String firstTsMonth = firstTs.substring(5,7);
			String firstTsDay = firstTs.substring(8,10);
			lastTs = donations.get(donationListSize - 1).getTs();
			String lastTsYear = lastTs.substring(0,4);
			String lastTsMonth = lastTs.substring(5,7);
			String lastTsDay = lastTs.substring(8,10);
			*/
			//testing
			firstTs = donationsTimeSorted.get(0).getDate();
			String firstTsYear = firstTs.substring(0,4);
			String firstTsMonth = firstTs.substring(5,7);
			String firstTsDay = firstTs.substring(8,10);
			lastTs = donationsTimeSorted.get(donationListSize - 1).getDate();
			String lastTsYear = lastTs.substring(0,4);
			String lastTsMonth = lastTs.substring(5,7);
			String lastTsDay = lastTs.substring(8,10);

			System.out.println("\n" + "first Year " + firstTsYear + " first month " + firstTsMonth + " first day " + firstTsDay);
			System.out.println("\n" + "last Year " + lastTsYear + " last month " + lastTsMonth + " last day " + lastTsDay);

			System.out.println("\n" + "Donation Size: " + donationListSize);

			temp = donations.get(0).getOrgName().toString();
			System.out.println("\n" + "Org Name: " + temp);
			currentOrgsWeight = donations.get(0).getWeight();
			System.out.println("\n" + "Current Org's Weight: " + currentOrgsWeight);

			if(time == 2 && type == 1)
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
				List<SummaryReport> reportList = new ArrayList<SummaryReport>();
				int reportListIndex = 0;
				int tempWeight = 0;
				String currentOrgsTimeRange ="";
				for (int i = 0; i < donationListSize; i++) {
					SummaryReport tempSummary = new SummaryReport();
					if (i == 0) {

						currentOrgsTimeRange = donationsSummary.get(i).getDate();
						currentOrgsTimeRange = currentOrgsTimeRange.substring(0,4);
						tempSummary.setOrg(donationsSummary.get(i).getOrgName());
						tempSummary.setWeight(donationsSummary.get(i).getWeight());
						tempSummary.setTimeRange(currentOrgsTimeRange);
						reportList.add(tempSummary);

					}
					else
					{
						if(reportList.get(reportListIndex).getOrg().equals(donationsSummary.get(i).getOrgName()) && reportList.get(reportListIndex).getTimeRange().equals(donationsSummary.get(i).getDate().substring(0,4))){
							tempWeight = reportList.get(reportListIndex).getWeight();
							tempWeight += donationsSummary.get(i).getWeight();
							reportList.get(reportListIndex).setWeight(tempWeight);
						} else {

							reportListIndex++;
							currentOrgsTimeRange = donationsSummary.get(i).getDate();
							currentOrgsTimeRange = currentOrgsTimeRange.substring(0,4);
							tempSummary.setOrg(donationsSummary.get(i).getOrgName());
							tempSummary.setWeight(donationsSummary.get(i).getWeight());
							tempSummary.setTimeRange(currentOrgsTimeRange);
							reportList.add(tempSummary);
						}
					}
				}
				List<Donation> donationListToReturn = new ArrayList<Donation>();
				for (int j = 0; j < reportList.size();j++){
					Donation tempDonation = new Donation();
					System.out.println("\n" + "Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " temp Weight: " + reportList.get(j).getWeight());
					tempDonation.setOrgName(reportList.get(j).getOrg());
					tempDonation.setDate(reportList.get(j).getTimeRange());
					tempDonation.setWeight(reportList.get(j).getWeight());
					tempDonation.setCategory("");
					donationListToReturn.add(tempDonation);
				}
				donationListToReturn = insertZeros(donationListToReturn, timeRangeArray);
				for (int i = 0; i< donationListToReturn.size();i++)
				{
					System.out.println("Donation List Org: " + donationListToReturn.get(i).getOrgName() + " Donation List Time Range: " + donationListToReturn.get(i).getDate() + " Donation List Weight: " + donationListToReturn.get(i).getWeight());
				}
				
				List<String> timeRangesList = Arrays.asList(timeRangeArray);
				
				List<Object> listsToReturn = new ArrayList<Object>();
				
				listsToReturn.add(timeRangesList);
				listsToReturn.add(donationListToReturn);
				
				return listsToReturn;
			}
			else if (time == 2 && type == 0)
			{
				int YearsSpanned = Integer.parseInt(lastTsYear) - Integer.parseInt(firstTsYear);
				List<DetailedReport> reportList = new ArrayList<DetailedReport>();
				String timeRangeArray[] = new String[YearsSpanned+1];//timeRangeArray is used to check which year to put weights into.
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
				//SummaryReport tempSummary = new SummaryReport();
				int reportListIndex = 0;
				int tempWeight = 0;
				String currentOrgsTimeRange ="";
				for (int i = 0; i < donationListSize; i++) {
					DetailedReport tempReport = new DetailedReport();
					if (i == 0) {
						tempReport.setOrg(donations.get(i).getOrgName());
						tempReport.setWeight(donations.get(i).getWeight());
						tempReport.setCategory(donations.get(i).getCategory());
						tempReport.setTimeRange(donations.get(i).getDate().substring(0,4));
						reportList.add(tempReport);
					}
					else
					{
						if (reportList.get(reportListIndex).getOrg().equalsIgnoreCase(donations.get(i).getOrgName().toString())) {
							if (reportList.get(reportListIndex).getCategory().equalsIgnoreCase(donations.get(i).getCategory()))
							{
								currentOrgsWeight = currentOrgsWeight + donations.get(i).getWeight();
								if (reportList.get(reportListIndex).getTimeRange().equals(donations.get(i).getDate().substring(0,4)))
								{
									tempWeight = reportList.get(reportListIndex).getWeight();
									tempWeight += donations.get(i).getWeight();
									reportList.get(reportListIndex).setWeight(tempWeight);
								}
								else
								{
									reportListIndex++;
									tempReport.setOrg(donations.get(i).getOrgName());
									tempReport.setWeight(donations.get(i).getWeight());
									tempReport.setCategory(donations.get(i).getCategory());
									tempReport.setTimeRange(donations.get(i).getDate().substring(0,4));
									reportList.add(tempReport);
								}
							}
							else
							{
								reportListIndex++;
								tempReport.setOrg(donations.get(i).getOrgName());
								tempReport.setWeight(donations.get(i).getWeight());
								tempReport.setCategory(donations.get(i).getCategory());
								tempReport.setTimeRange(donations.get(i).getDate().substring(0,4));
								reportList.add(tempReport);
							}

						} else {
							reportListIndex++;
							tempReport.setOrg(donations.get(i).getOrgName());
							tempReport.setWeight(donations.get(i).getWeight());
							tempReport.setCategory(donations.get(i).getCategory());
							tempReport.setTimeRange(donations.get(i).getDate().substring(0,4));
							reportList.add(tempReport);
						}
					}

				}
				List<Donation> donationListToReturn = new ArrayList<Donation>();
				for (int j = 0; j < reportList.size();j++){
					Donation tempDonation = new Donation();
					System.out.println("\n" + "Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " Category: " + reportList.get(j).getCategory() + " temp Weight: " + reportList.get(j).getWeight());
					tempDonation.setOrgName(reportList.get(j).getOrg());
					tempDonation.setDate(reportList.get(j).getTimeRange());
					tempDonation.setWeight(reportList.get(j).getWeight());
					tempDonation.setCategory(reportList.get(j).getCategory());
					donationListToReturn.add(tempDonation);
				}
				donationListToReturn = insertZeros(donationListToReturn, timeRangeArray);
				for (int i = 0; i< donationListToReturn.size();i++)
				{
					System.out.println("Donation List Org: " + donationListToReturn.get(i).getOrgName() + " Donation List Time Range: " + donationListToReturn.get(i).getDate() + " Donation List Category: "+donationListToReturn.get(i).getCategory() + " Donation List Weight: " + donationListToReturn.get(i).getWeight());
				}
				
				List<String> timeRangesList = Arrays.asList(timeRangeArray);
				
				List<Object> listsToReturn = new ArrayList<Object>();
				
				listsToReturn.add(timeRangesList);
				listsToReturn.add(donationListToReturn);
				
				return listsToReturn;
			}
			else if(time == 1 && type == 1)
			{
				int MonthsSpanned = 0;
				int lastYear = Integer.parseInt(lastTsYear);
				int firstYear = Integer.parseInt(firstTsYear);
				int tempWeight =0;
				if(Integer.parseInt(lastTsYear) - Integer.parseInt(firstTsYear)<0)
				{
					return null;
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
				String timeRangeArray[] = new String[MonthsSpanned+1];//timeRangeArray is used to check which year to put weights into.
				timeRangeArray = getMonthTimeRange(MonthsSpanned, firstTsYear, firstTsMonth, firstTs, lastTs);
				List<SummaryReport> reportList = new ArrayList<SummaryReport>();
				//SummaryReport tempSummary = new SummaryReport();
				int reportListIndex = 0;
				String currentOrgsTimeRange ="";
				for (int i = 0; i < donationListSize; i++) {
					SummaryReport tempSummary = new SummaryReport();
					if (i == 0) {
						currentOrgsTimeRange = donationsSummary.get(i).getDate();
						currentOrgsTimeRange = currentOrgsTimeRange.substring(0,7);
						tempSummary.setOrg(donationsSummary.get(i).getOrgName());
						tempSummary.setWeight(donationsSummary.get(i).getWeight());
						tempSummary.setTimeRange(currentOrgsTimeRange);
						reportList.add(tempSummary);
					}
					else
					{
						if(reportList.get(reportListIndex).getOrg().equalsIgnoreCase(donationsSummary.get(i).getOrgName()) && reportList.get(reportListIndex).getTimeRange().equals(donationsSummary.get(i).getDate().substring(0,7)))
						{
							tempWeight = reportList.get(reportListIndex).getWeight();
							tempWeight += donations.get(i).getWeight();
							reportList.get(reportListIndex).setWeight(tempWeight);
						}
						else
						{
							reportListIndex++;
							currentOrgsTimeRange = donationsSummary.get(i).getDate();
							currentOrgsTimeRange = currentOrgsTimeRange.substring(0,7);
							tempSummary.setOrg(donationsSummary.get(i).getOrgName());
							tempSummary.setWeight(donationsSummary.get(i).getWeight());
							tempSummary.setTimeRange(currentOrgsTimeRange);
							reportList.add(tempSummary);
						}
					}
				}
				List<Donation> donationListToReturn = new ArrayList<Donation>();
				for (int j = 0; j < reportList.size();j++){
					Donation tempDonation = new Donation();
					System.out.println("\n" + "Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " temp Weight: " + reportList.get(j).getWeight());
					tempDonation.setOrgName(reportList.get(j).getOrg());
					tempDonation.setDate(reportList.get(j).getTimeRange());
					tempDonation.setWeight(reportList.get(j).getWeight());
					tempDonation.setCategory("");
					donationListToReturn.add(tempDonation);
				}
				donationListToReturn = insertZeros(donationListToReturn, timeRangeArray);
				for (int i = 0; i< donationListToReturn.size();i++)
				{
					System.out.println("Donation List Org: " + donationListToReturn.get(i).getOrgName() + " Donation List Time Range: " + donationListToReturn.get(i).getDate() + " Donation List Weight: " + donationListToReturn.get(i).getWeight());
				}
				
				List<String> timeRangesList = Arrays.asList(timeRangeArray);
				
				List<Object> listsToReturn = new ArrayList<Object>();
				
				listsToReturn.add(timeRangesList);
				listsToReturn.add(donationListToReturn);
				
				return listsToReturn;
				//print csv code here
			}
			else if(time == 1 && type == 0)
			{
				int MonthsSpanned = 0;
				int lastYear = Integer.parseInt(lastTsYear);
				int firstYear = Integer.parseInt(firstTsYear);
				int tempWeight =0;
				if(Integer.parseInt(lastTsYear) - Integer.parseInt(firstTsYear)<0)
				{
					return null;
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
				List<DetailedReport> reportList = new ArrayList<DetailedReport>();
				String timeRangeArray[] = new String[MonthsSpanned+1];//timeRangeArray is used to check which year to put weights into.
				timeRangeArray = getMonthTimeRange(MonthsSpanned, firstTsYear, firstTsMonth, firstTs, lastTs);

				int reportListIndex = 0;
				for (int i = 0; i < donationListSize; i++)
				{
					DetailedReport tempReport = new DetailedReport();
					String tempOrgName = tempReport.getOrg();
					tempCategory = tempReport.getCategory();
					if (i == 0)
					{
						tempReport.setOrg(donations.get(i).getOrgName());
						tempReport.setCategory(donations.get(i).getCategory());
						tempReport.setWeight(donations.get(i).getWeight());
						tempReport.setTimeRange(donations.get(i).getDate().substring(0,7));
						reportList.add(tempReport);
					}
					else
					{
						if (donations.get(i).getOrgName().equals(reportList.get(reportListIndex).getOrg()) && donations.get(i).getCategory().equalsIgnoreCase(reportList.get(reportListIndex).getCategory()) && donations.get(i).getDate().substring(0, 7).equals(reportList.get(reportListIndex).getTimeRange()))
						{
							tempWeight = reportList.get(reportListIndex).getWeight();
							tempWeight += donations.get(i).getWeight();
							reportList.get(reportListIndex).setWeight(tempWeight);
						}
						else
						{
							tempReport.setOrg(donations.get(i).getOrgName());
							tempReport.setCategory(donations.get(i).getCategory());
							tempReport.setWeight(donations.get(i).getWeight());
							tempReport.setTimeRange(donations.get(i).getDate().substring(0,7));
							reportList.add(tempReport);
							reportListIndex++;
						}
					}
				}
				int timeArrayIndex = 0;
				String tempTimeRange = "";
				List<Donation> donationListToReturn = new ArrayList<Donation>();
				for (int i =0;i<reportList.size();i++)
				{
					Donation tempDonation = new Donation();
					System.out.println("Report List Org Name: " + reportList.get(i).getOrg() + " Category: " + reportList.get(i).getCategory() + " Date: " + reportList.get(i).getTimeRange() + " Weight: " + reportList.get(i).getWeight());
					tempDonation.setOrgName(reportList.get(i).getOrg());
					tempDonation.setDate(reportList.get(i).getTimeRange());
					tempDonation.setWeight(reportList.get(i).getWeight());
					tempDonation.setCategory(reportList.get(i).getCategory());
					donationListToReturn.add(tempDonation);
				}
				for (int i = 0; i< timeRangeArray.length;i++)
				{
					System.out.println("\n"+ timeRangeArray[i]);
				}
				donationListToReturn = insertZeros(donationListToReturn, timeRangeArray);
				for (int i = 0; i< donationListToReturn.size();i++)
				{
					System.out.println("Donation List Org: " + donationListToReturn.get(i).getOrgName() + " Donation List Time Range: " + donationListToReturn.get(i).getDate() + " Donation List Category: "+donationListToReturn.get(i).getCategory() + " Donation List Weight: " + donationListToReturn.get(i).getWeight());
				}
				
				List<String> timeRangesList = Arrays.asList(timeRangeArray);
				
				List<Object> listsToReturn = new ArrayList<Object>();
				
				listsToReturn.add(timeRangesList);
				listsToReturn.add(donationListToReturn);
				
				return listsToReturn;
				//print csv code here
			}
			else if(time == 0 && type == 1)
			{
				Calendar cStart = Calendar.getInstance();
				cStart.set(Integer.parseInt(firstTsYear), Integer.parseInt(firstTsMonth)-1, Integer.parseInt(firstTsDay));
				int dayOfWeek = cStart.get(Calendar.DAY_OF_WEEK);
				Calendar cFinish = Calendar.getInstance();
				cFinish.set(Integer.parseInt(lastTsYear), Integer.parseInt(lastTsMonth)-1, Integer.parseInt(lastTsDay));
				List<String> timeList = new ArrayList<String>();
				timeList = getWeeklyTimeRange(cStart, cFinish);
				System.out.println(timeList);
				String timeRangeArray[] = new String[timeList.size()];
				for (int i = 0; i < timeList.size(); i++)
				{
					timeRangeArray[i] = timeList.get(i);
				}
				List<SummaryReport> reportList = new ArrayList<SummaryReport>();
				reportList = fillWeeklySummaryList(timeList, donationsSummary);
				//check for week dates
				for (int i = 0; i < timeList.size();i++)
				{
					System.out.println("\n" + timeList.get(i));
				}
				List<Donation> donationListToReturn = new ArrayList<Donation>();
				for (int j = 0; j < reportList.size(); j++)
				{
					Donation tempDonation = new Donation();
					System.out.println("\n" + "Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " temp Weight: " + reportList.get(j).getWeight());
					tempDonation.setOrgName(reportList.get(j).getOrg());
					tempDonation.setDate(reportList.get(j).getTimeRange());
					tempDonation.setWeight(reportList.get(j).getWeight());
					tempDonation.setCategory("");
					donationListToReturn.add(tempDonation);
				}


				Calendar cTest1 = Calendar.getInstance();
				Calendar cTest2 = Calendar.getInstance();
				cTest1.set(2017, 8, 24);
				cTest2.set(2017, 8, 28);
				int weekYear = cTest1.get(Calendar.WEEK_OF_YEAR);
				int weekYear1 = cTest2.get(Calendar.WEEK_OF_YEAR);
				System.out.println("\nTest 1: " + weekYear + " Test 2: "+ weekYear1);
				donationListToReturn = insertZeros(donationListToReturn, timeRangeArray);
				for (int i = 0; i< donationListToReturn.size();i++)
				{
					System.out.println("Donation List Org: " + donationListToReturn.get(i).getOrgName() + " Donation List Time Range: " + donationListToReturn.get(i).getDate() + " Donation List Weight: " + donationListToReturn.get(i).getWeight());
				}
				
				List<String> timeRangesList = Arrays.asList(timeRangeArray);
				
				List<Object> listsToReturn = new ArrayList<Object>();
				
				listsToReturn.add(timeRangesList);
				listsToReturn.add(donationListToReturn);
				
				return listsToReturn;
			}
			else if(time == 0 && type == 0)
			{
				Calendar cStart = Calendar.getInstance();
				cStart.set(Integer.parseInt(firstTsYear), Integer.parseInt(firstTsMonth)-1, Integer.parseInt(firstTsDay));
				int dayOfWeek = cStart.get(Calendar.DAY_OF_WEEK);
				Calendar cFinish = Calendar.getInstance();
				cFinish.set(Integer.parseInt(lastTsYear), Integer.parseInt(lastTsMonth)-1, Integer.parseInt(lastTsDay));
				List<String> timeList = new ArrayList<String>();
				timeList = getWeeklyTimeRange(cStart, cFinish);
				System.out.println("Time List: " + timeList);
				String timeRangeArray[] = new String[timeList.size()];
				for (int i = 0; i < timeList.size(); i++)
				{
					timeRangeArray[i] = timeList.get(i);
				}
				List<DetailedReport> reportList = new ArrayList<DetailedReport>();
				reportList = fillWeeklyDetailedList(timeList, donations);
				List<Donation> donationListToReturn = new ArrayList<Donation>();
				for (int k = 0; k < reportList.size(); k++)
				{
					Donation tempDonation = new Donation();
					System.out.println("Report List Org Name: " + reportList.get(k).getOrg() + " Category: "
							+ reportList.get(k).getCategory() + " Weight: " + reportList.get(k).getWeight() +
							" time range: " + reportList.get(k).getTimeRange());
					tempDonation.setOrgName(reportList.get(k).getOrg());
					tempDonation.setDate(reportList.get(k).getTimeRange());
					tempDonation.setWeight(reportList.get(k).getWeight());
					tempDonation.setCategory(reportList.get(k).getCategory());
					donationListToReturn.add(tempDonation);
				}
				donationListToReturn = insertZeros(donationListToReturn, timeRangeArray);
				for (int i = 0; i< donationListToReturn.size();i++)
				{
					System.out.println("Donation List Org: " + donationListToReturn.get(i).getOrgName() + " Donation List Time Range: " + donationListToReturn.get(i).getDate() + " Donation List Category: "+donationListToReturn.get(i).getCategory() + " Donation List Weight: " + donationListToReturn.get(i).getWeight());
				}
				
				List<String> timeRangesList = Arrays.asList(timeRangeArray);
				
				List<Object> listsToReturn = new ArrayList<Object>();
				
				listsToReturn.add(timeRangesList);
				listsToReturn.add(donationListToReturn);
				
				return listsToReturn;
			}
		}

		return null;
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
		}
		return donationListToReturn;
	}
}