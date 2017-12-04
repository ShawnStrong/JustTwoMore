package com.concretepage.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class TestDAO implements IntTestDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void initTest() {
		String[] org_name = new String[7];
		org_name[0] = "Org A";
		org_name[1] = "Org B";
		org_name[2] = "Org C";
		org_name[3] = "Org D";
		org_name[4] = "Org E";
		org_name[5] = "Org X";
		org_name[6] = "Org Y";
		
		// DROP donation table
		Query query = entityManager.createNativeQuery("DROP TABLE donation_table;");
		query.executeUpdate();
		
		// DROP user table
		query = entityManager.createNativeQuery("DROP TABLE user_table;");
		query.executeUpdate();
		
		// DROP org table
		query = entityManager.createNativeQuery("DROP TABLE org_table");
		query.executeUpdate();
		
		// create user and org tables
		
		query = entityManager.createNativeQuery(
				"CREATE TABLE IF NOT EXISTS `org_table` (" + 
				"  `org_id` int(5) NOT NULL AUTO_INCREMENT," + 
				"  `org_name` TINYTEXT NOT NULL," + 
				"  `contact_name` TINYTEXT NOT NULL," + 
				"  `contact_number` TINYTEXT NOT NULL," +
				"  `contact_email` TINYTEXT NOT NULL," +
				"  `notes` MEDIUMTEXT NOT NULL," +
				"  PRIMARY KEY (`org_id`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		query.executeUpdate();
		
		query = entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS `user_table` (" + 
				"  `user_id` int(5) NOT NULL AUTO_INCREMENT," + 
				"  `username` TINYTEXT NOT NULL," + 
				"  `password` TINYTEXT NOT NULL," + 
				"  `user_email` TINYTEXT NOT NULL," +
				"  PRIMARY KEY (`user_id`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		query.executeUpdate();
		
		// create test user
		query = entityManager.createNativeQuery(
				"INSERT INTO user_table (username,password,user_email) " +
				" VALUES(?,?,?)");
        	query.setParameter(1, "test");
        	query.setParameter(2, "test");
        	query.setParameter(3, "test");
        	query.executeUpdate();
        
        // create 7 orgs
		for (int i = 0; i < 7; i++) {
			query = entityManager.createNativeQuery(
					"INSERT INTO org_table (org_name,contact_name,contact_number,contact_email,notes) " +
		            " VALUES(?,?,?,?,?)");
		        query.setParameter(1, org_name[i]);
		        query.setParameter(2, "bob");
		        query.setParameter(3, "bob");
		        query.setParameter(4, "bob");
		        query.setParameter(5, "bob");
		        query.executeUpdate();
		}
	}
	
	@Override
	public void initTestThree() {
		String[] org_name = new String[7];
		org_name[0] = "Org A";
		org_name[1] = "Org B";
		org_name[2] = "Org X";
		org_name[3] = "Org C";
		org_name[4] = "Org D";
		org_name[5] = "Org Y";
		org_name[6] = "Org E";
		
		String[] dates = new String[4];
		dates[0] = "2017-11-05";
		dates[1] = "2017-11-12";
		dates[2] = "2017-11-19";
		dates[3] = "2017-11-26";
		
		String[] times = new String[7];
		times[0] = " 14:00:00";
		times[1] = " 14:01:00";
		times[2] = " 14:03:00";
		times[3] = " 14:04:00";
		times[4] = " 14:06:00";
		times[5] = " 14:02:00";
		times[6] = " 14:05:00";
		
		String[] categories = new String[7];
		categories[0] = "deli";
		categories[1] = "pantry";
		categories[2] = "meat";
		categories[3] = "dairy";
		categories[4] = "bakery";
		categories[5] = "produce";
		categories[6] = "pantry";
		
		Query query;
		
		// create donation table
		query = entityManager.createNativeQuery(
				"CREATE TABLE IF NOT EXISTS `donation_table` (" +
				" `order_id` int(5) NOT NULL AUTO_INCREMENT," +
				" `org_id` int(5) NOT NULL," +
				" `org_name` TINYTEXT NOT NULL," +
				" `category` TINYTEXT NOT NULL," +
				" `weight` int(7) NOT NULL," +
				" `donation` int(1) NOT NULL," +
				" `user_name` TINYTEXT NOT NULL," +
				" `ts` TIMESTAMP," +
				" PRIMARY KEY (`order_id`)," +
				" FOREIGN KEY (`org_id`) REFERENCES org_table(org_id)" +
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		
		query.executeUpdate();
		
		for (int i = 0; i < 28; i++) {
			if (i < 7) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%7] + "'), "
						+ "org_name = '" + org_name[i%7]
						+ "', category = '" + categories[i%7]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[0] + times[i%7] + "';");
				
				query.executeUpdate();
			} else if (i < 14) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%7] + "'), "
						+ "org_name = '" + org_name[i%7]
						+ "', category = '" + categories[i%7]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[1] + times[i%7] + "';");
				
				query.executeUpdate();
			} else if (i < 21) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%7] + "'), "
						+ "org_name = '" + org_name[i%7]
						+ "', category = '" + categories[i%7]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[2] + times[i%7] + "';");
				
				query.executeUpdate();
			} else if (i < 28) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%7] + "'), "
						+ "org_name = '" + org_name[i%7]
						+ "', category = '" + categories[i%7]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[3] + times[i%7] + "';");
				
				query.executeUpdate();
			}
		}
	}
	
	@Override
	public void initTestTwo() {
		String[] org_name = new String[5];
		org_name[0] = "Org A";
		org_name[1] = "Org B";
		org_name[2] = "Org C";
		org_name[3] = "Org D";
		org_name[4] = "Org E";
		
		String[] dates = new String[4];
		dates[0] = "2017-11-07";
		dates[1] = "2017-11-14";
		dates[2] = "2017-11-21";
		dates[3] = "2017-11-28";
		
		String[] times = new String[5];
		times[0] = " 14:00:00";
		times[1] = " 14:01:00";
		times[2] = " 14:03:00";
		times[3] = " 14:04:00";
		times[4] = " 14:06:00";
		
		String[] categories = new String[5];
		categories[0] = "deli";
		categories[1] = "pantry";
		categories[2] = "dairy";
		categories[3] = "bakery";
		categories[4] = "pantry";
		
		Query query;
		
		// create donation table
		query = entityManager.createNativeQuery(
				"CREATE TABLE IF NOT EXISTS `donation_table` (" +
				" `order_id` int(5) NOT NULL AUTO_INCREMENT," +
				" `org_id` int(5) NOT NULL," +
				" `org_name` TINYTEXT NOT NULL," +
				" `category` TINYTEXT NOT NULL," +
				" `weight` int(7) NOT NULL," +
				" `donation` int(1) NOT NULL," +
				" `user_name` TINYTEXT NOT NULL," +
				" `ts` TIMESTAMP," +
				" PRIMARY KEY (`order_id`)," +
				" FOREIGN KEY (`org_id`) REFERENCES org_table(org_id)" +
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		
		query.executeUpdate();
		
		for (int i = 0; i < 20; i++) {
			if (i < 5) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%5] + "'), "
						+ "org_name = '" + org_name[i%5]
						+ "', category = '" + categories[i%5]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[0] + times[i%5] + "';");
				
				query.executeUpdate();
			} else if (i < 10) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%5] + "'), "
						+ "org_name = '" + org_name[i%5]
						+ "', category = '" + categories[i%5]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[1] + times[i%5] + "';");
				
				query.executeUpdate();
			} else if (i < 15) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%5] + "'), "
						+ "org_name = '" + org_name[i%5]
						+ "', category = '" + categories[i%5]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[2] + times[i%5] + "';");
				
				query.executeUpdate();
			} else if (i < 20) {
				query = entityManager.createNativeQuery(
						"INSERT INTO donation_table SET "
						+ "org_id=(SELECT org_id FROM org_table WHERE "
						+ "org_name = '" + org_name[i%5] + "'), "
						+ "org_name = '" + org_name[i%5]
						+ "', category = '" + categories[i%5]
						+ "', weight = '10"
						+ "', donation = '1" 
						+ "', user_name = 'test"
						+ "', ts = '" + dates[3] + times[i%5] + "';");
				
				query.executeUpdate();
			}
		}
	}
}