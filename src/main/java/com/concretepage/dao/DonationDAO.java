package com.concretepage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.Donation;

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
	public List<Donation> getReport(int donation, String start_date, String end_date) {
		
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM `donation_table`"
				+ " WHERE (ts BETWEEN '" + start_date + " 00:00:00' AND '" 
				+ end_date + " 00:00:00') AND "
				+ "donation=" + donation + " ORDER BY org_name;", Donation.class);


		List<Donation> donations = query.getResultList();
		int donationListSize = 0;
		String temp;
		int currentOrgsWeight = 0;
		if(donations.get(0) != null)
		{
			donationListSize = donations.size();
			System.out.println("\n" + "Donation Size: " + donationListSize);

			temp = donations.get(0).getOrgName().toString();
			System.out.println("\n" + "Org Name: " + temp);
			currentOrgsWeight = donations.get(0).getWeight();
			System.out.println("\n" + "Current Org's Weight: " + currentOrgsWeight);

			for(int i=0; i<donationListSize;i++)
			{
				if(i == 0)
				{
					temp = donations.get(i).getOrgName().toString();
					currentOrgsWeight = donations.get(i).getWeight();
				}
				else
				{
					if(temp.equals(donations.get(i).getOrgName().toString()))
					{
						currentOrgsWeight = currentOrgsWeight + donations.get(i).getWeight();
					}
					else
					{
						//write temp to csv, then write current weight to csv
						System.out.println("\n" + "Org Name: " + temp + " Weight: " + currentOrgsWeight);
						temp = donations.get(i).getOrgName().toString();
						currentOrgsWeight = donations.get(i).getWeight();
					}
				}
				if(i == donationListSize - 1)
				{
					System.out.println("\n" + "Org Name: " + temp + " Weight: " + currentOrgsWeight);
				}
			}

		}

		return donations;
	}
}