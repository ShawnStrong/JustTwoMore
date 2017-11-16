package com.concretepage.dao;

import java.util.List;
import java.util.ArrayList;

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
	public int inputDonation(String org_name, String category, int weight, int donation) {
		
		Query query = entityManager.createNativeQuery(
				"INSERT INTO donation_table SET "
				+ "org_id=(SELECT org_id FROM org_table WHERE "
				+ "org_name = '" + org_name + "'), "
				+ "org_name = '" + org_name
				+ "', category = '" + category
				+ "', weight = '" + weight
				+ "', donation = '" + donation + "';");
		
		query.executeUpdate();
		return 0;
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
		Query query = entityManager.createNativeQuery(
				"SELECT category, count(*) freq FROM donation_table"
						+ " WHERE org_name='" + org_name + "' GROUP BY category ORDER BY freq DESC;", Frequency.class);

		List<Frequency> frequencyList = query.getResultList();
		List<String> categories = new ArrayList<String>();
		String[] categoryNames = new String[6];
		categoryNames[0] = "deli";
		categoryNames[1] = "dairy";
		categoryNames[2] = "meat";
		categoryNames[3] = "produce";
		categoryNames[4] = "pantry";
		categoryNames[5] = "bakery";
		for (int i = 0; i < frequencyList.size(); i++)
		{
			categories.add(frequencyList.get(i).getCategory());
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
		return categories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Donation> getReport(int donation, int time, int type, String start_date, String end_date) {
		//time == 2 means yearly report time == 1 means monthly report time == 0 means weekly report
		//type == 1 means they want a summary report i.e no food categories, just total weight
		//type == 0 means they want a descriptive report i.e they want to tsee food categories
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM `donation_table`"
				+ " WHERE (ts BETWEEN '" + start_date + " 00:00:00' AND '" 
				+ end_date + " 00:00:00') AND "
				+ "donation=" + donation + " ORDER BY org_name, category;", Donation.class);


		Query queryTimeSorted = entityManager.createNativeQuery(
				"SELECT * FROM `donation_table`"
						+ " WHERE (ts BETWEEN '" + start_date + " 00:00:00' AND '"
						+ end_date + " 00:00:00') AND "
						+ "donation=" + donation + " ORDER BY ts;", Donation.class);

		List<Donation> donations = query.getResultList();
		List<Donation> donationsTimeSorted = queryTimeSorted.getResultList();
		int donationListSize = 0;
		int dontationTSListSize = 0;
		String temp;
		String tempCategory = "";
		String firstTs = "";
		String lastTs ="";
		int currentOrgsWeight = 0;
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
			firstTs = donationsTimeSorted.get(0).getTs();
			String firstTsYear = firstTs.substring(0,4);
			String firstTsMonth = firstTs.substring(5,7);
			String firstTsDay = firstTs.substring(8,10);
			lastTs = donationsTimeSorted.get(donationListSize - 1).getTs();
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
				List<SummaryReport> reportList = new ArrayList<SummaryReport>();
				//SummaryReport tempSummary = new SummaryReport();
				int reportListIndex = 0;
				int tempWeight = 0;
				String currentOrgsTimeRange ="";
				for (int i = 0; i < donationListSize; i++) {
					SummaryReport tempSummary = new SummaryReport();
					if (i == 0) {

						currentOrgsTimeRange = donations.get(i).getTs();
						currentOrgsTimeRange = currentOrgsTimeRange.substring(0,4);
						tempSummary.setOrg(donations.get(i).getOrgName());
						tempSummary.setWeight(donations.get(i).getWeight());
						tempSummary.setTimeRange(currentOrgsTimeRange);
						reportList.add(tempSummary);

					}
					else
					{
						if(reportList.get(reportListIndex).getOrg().equals(donations.get(i).getOrgName())){
							tempWeight = reportList.get(reportListIndex).getWeight();
							tempWeight += donations.get(i).getWeight();
							reportList.get(reportListIndex).setWeight(tempWeight);
						} else {

							reportListIndex++;
							currentOrgsTimeRange = donations.get(i).getTs();
							currentOrgsTimeRange = currentOrgsTimeRange.substring(0,4);
							tempSummary.setOrg(donations.get(i).getOrgName());
							tempSummary.setWeight(donations.get(i).getWeight());
							tempSummary.setTimeRange(currentOrgsTimeRange);
							reportList.add(tempSummary);
						}
					}
				}
				for (int j = 0; j < reportList.size();j++){
					System.out.println("\n" + "Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " temp Weight: " + reportList.get(j).getWeight());
				}
				/*
				USED TO TEST getFrequency()
				List<String> cats = new ArrayList<String>();
				cats = getFrequency("Org A");
				for (String temp1: cats)
				{
					System.out.println(temp1);
				}
				*/
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
						tempReport.setTimeRange(donations.get(i).getTs().substring(0,4));
						reportList.add(tempReport);
					}
					else
					{
						if (reportList.get(reportListIndex).getOrg().equalsIgnoreCase(donations.get(i).getOrgName().toString())) {
							if (reportList.get(reportListIndex).getCategory().equalsIgnoreCase(donations.get(i).getCategory()))
							{
								currentOrgsWeight = currentOrgsWeight + donations.get(i).getWeight();
								if (reportList.get(reportListIndex).getTimeRange().equals(donations.get(i).getTs().substring(0,4)))
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
									tempReport.setTimeRange(donations.get(i).getTs().substring(0,4));
									reportList.add(tempReport);
								}
							}
							else
							{
								reportListIndex++;
								tempReport.setOrg(donations.get(i).getOrgName());
								tempReport.setWeight(donations.get(i).getWeight());
								tempReport.setCategory(donations.get(i).getCategory());
								tempReport.setTimeRange(donations.get(i).getTs().substring(0,4));
								reportList.add(tempReport);
							}

						} else {
							reportListIndex++;
							tempReport.setOrg(donations.get(i).getOrgName());
							tempReport.setWeight(donations.get(i).getWeight());
							tempReport.setCategory(donations.get(i).getCategory());
							tempReport.setTimeRange(donations.get(i).getTs().substring(0,4));
							reportList.add(tempReport);
						}
					}

				}
				for (int j = 0; j < reportList.size();j++){
					System.out.println("\n" + "Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " Category: " + reportList.get(j).getCategory() + " temp Weight: " + reportList.get(j).getWeight());
				}
			}
			else if(time == 1 && type == 1)
			{
				List<SummaryReport> reportList = new ArrayList<SummaryReport>();
				//SummaryReport tempSummary = new SummaryReport();
				int reportListIndex = 0;
				int tempWeight = 0;
				String currentOrgsTimeRange ="";
				for (int i = 0; i < donationListSize; i++) {
					SummaryReport tempSummary = new SummaryReport();
					if (i == 0) {
						currentOrgsTimeRange = donations.get(i).getTs();
						currentOrgsTimeRange = currentOrgsTimeRange.substring(0,7);
						tempSummary.setOrg(donations.get(i).getOrgName());
						tempSummary.setWeight(donations.get(i).getWeight());
						tempSummary.setTimeRange(currentOrgsTimeRange);
						reportList.add(tempSummary);
					}
					else
					{
						if(reportList.get(reportListIndex).getOrg().equals(donations.get(i).getOrgName()) && reportList.get(reportListIndex).getTimeRange().equals(donations.get(i).getTs().substring(0,7)))
						{
							tempWeight = reportList.get(reportListIndex).getWeight();
							tempWeight += donations.get(i).getWeight();
							reportList.get(reportListIndex).setWeight(tempWeight);
						}
						else
						{
							reportListIndex++;
							currentOrgsTimeRange = donations.get(i).getTs();
							currentOrgsTimeRange = currentOrgsTimeRange.substring(0,7);
							tempSummary.setOrg(donations.get(i).getOrgName());
							tempSummary.setWeight(donations.get(i).getWeight());
							tempSummary.setTimeRange(currentOrgsTimeRange);
							reportList.add(tempSummary);
						}
					}
				}
				for (int j = 0; j < reportList.size();j++){
					System.out.println("\n" + "Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " temp Weight: " + reportList.get(j).getWeight());
				}
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
						tempReport.setTimeRange(donations.get(i).getTs().substring(0,7));
						reportList.add(tempReport);
					}
					else
					{
						if (donations.get(i).getOrgName().equals(reportList.get(reportListIndex).getOrg()) && donations.get(i).getCategory().equalsIgnoreCase(reportList.get(reportListIndex).getCategory()) && donations.get(i).getTs().substring(0, 7).equals(reportList.get(reportListIndex).getTimeRange()))
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
							tempReport.setTimeRange(donations.get(i).getTs().substring(0,7));
							reportList.add(tempReport);
							reportListIndex++;
						}
					}
				}
				int timeArrayIndex = 0;
				String tempTimeRange = "";
				for(int i = 0; i<reportList.size();i++)
				{
					DetailedReport tempReport = new DetailedReport();
					tempReport=reportList.get(i);
					if(i==0)
					{
						System.out.print("\n");
						for (int j = 0; j<timeRangeArray.length; j++)
						{
							if (tempReport.getTimeRange().equals(timeRangeArray[j]))
							{
								System.out.print("Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " Category: " + reportList.get(j).getCategory() + " temp Weight: " + reportList.get(j).getWeight() + ",");
								timeArrayIndex = j;
								break;
							}
							else
							{
								System.out.print(",");
							}
						}
					}
					else
					{
						if(tempReport.getCategory().equalsIgnoreCase(reportList.get(i-1).getCategory()) && tempReport.getOrg().equals(reportList.get(i-1).getOrg()))
						{
							for (int j = timeArrayIndex; j<timeRangeArray.length; j++)
							{
								if (tempReport.getTimeRange().equals(timeRangeArray[j]))
								{
									System.out.print("Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " Category: " + reportList.get(j).getCategory() + " temp Weight: " + reportList.get(j).getWeight() + ",");
									timeArrayIndex = j;
									break;
								}
								else
								{
									System.out.print(",");
								}
							}
						}
						else
						{
							System.out.print("\n");
							for (int j = 0; j<timeRangeArray.length; j++)
							{
								if (tempReport.getTimeRange().equals(timeRangeArray[j]))
								{
									System.out.print("Temp Org Name: " + reportList.get(j).getOrg() + " Temp Time Range: " + reportList.get(j).getTimeRange() + " Category: " + reportList.get(j).getCategory() + " temp Weight: " + reportList.get(j).getWeight() + ",");
									timeArrayIndex = j;
									break;
								}
								else
								{
									System.out.print(",");
								}
							}
						}
					}
				}
				for (int i =0;i<reportList.size();i++)
				{
					System.out.println("Org Name: " + reportList.get(i).getOrg() + " Category: " + reportList.get(i).getCategory() + " Date: " + reportList.get(i).getTimeRange() + " Weight: " + reportList.get(i).getWeight());
				}
			}
			else if(time == 0 && type == 1)
			{

			}
		}

		return donations;
	}
}