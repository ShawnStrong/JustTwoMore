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

	@Override
	public List<String> listOrg(int donation) {
		Query query = entityManager.createNativeQuery(
				"SELECT DISTINCT org_name FROM donation_table WHERE " +
				"donation='" + donation + "';");
		
		List<String> orgs = query.getResultList();
		return orgs;
	}

	@Override
	public List<Donation> listInfo(String org_name, int donation) {
		// TODO Auto-generated method stub
		
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
}