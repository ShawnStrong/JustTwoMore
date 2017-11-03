package com.concretepage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.Org;
import com.concretepage.entity.User;

// DAOs - Data Access Objects. These guys are the objects that execute MySQL statements using the
// Query object. In here you will most likely be receiving or giving java bean objects (i.e. Partner) 
// to or from controllers or services. Any data processing is done in the Services layer for whatever
// reason, not sure yet.
// More about Query here:
// https://www.thoughts-on-java.org/jpa-native-queries/
@Transactional
@Repository
public class OrgDAO implements IntOrgDAO {
	
	// http://docs.oracle.com/javaee/6/api/javax/persistence/EntityManager.html
	// Definitely read this about EntityManager
	@PersistenceContext	
	private EntityManager entityManager;

	@Override
	public int createOrg(String org_name, String contact_name, String contact_number, String contact_email, String notes) {
		// error codes:
		// 0 for successful create
		// 1 for org already exists
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM org_table WHERE org_name='" +
				org_name + "';", Org.class);
		
		List<Org> orgs = query.getResultList();
		
		// check if org with org_name is already made
		if (orgs.isEmpty()) {
			query = entityManager.createNativeQuery(
					"INSERT INTO org_table (org_name,contact_name,contact_number,contact_email,notes) " +
		            " VALUES(?,?,?,?,?)");
		        query.setParameter(1, org_name);
		        query.setParameter(2, contact_name);
		        query.setParameter(3, contact_number);
		        query.setParameter(4, contact_email);
		        query.setParameter(5, notes);
		        query.executeUpdate();
		        
		    return 0;
		} else {
			return 1;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Org> listPartner() {
		
		Query q = entityManager.createNativeQuery("SELECT * FROM org_table;", Org.class);
		List<Org> partners = q.getResultList();
		return partners;
		
	}

	@Override
	public void initPartnerTable() {
		Query query = entityManager.createNativeQuery(
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
	}

	@Override
	public int updateOrg(String org_name, String contact_name, String contact_number, String contact_email,
			String notes) {
				// error codes:
				// 0 for successful update
				// 1 for org not created yet
				Query query = entityManager.createNativeQuery(
						"SELECT * FROM org_table WHERE org_name='" +
						org_name + "';", Org.class);
				
				List<Org> orgs = query.getResultList();
				
				// check if org is made
				if (!orgs.isEmpty()) {
					
					// update
					query = entityManager.createNativeQuery("UPDATE org_table SET "
							+ "org_name='" + org_name
							+ "', contact_name='" + contact_name
							+ "', contact_number='" + contact_number
							+ "', contact_email='" + contact_email
							+ "', notes='" + notes
							+ "' WHERE org_name='" + org_name + "';");

			        query.executeUpdate();
			        
			        return 0;
				} else {
					return 1;
				}
	}

}
