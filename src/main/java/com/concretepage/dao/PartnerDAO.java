package com.concretepage.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.Partner;

// DAOs - Data Access Objects. These guys are the objects that execute MySQL statements using the
// Query object. In here you will most likely be receiving or giving java bean objects (i.e. Partner) 
// to or from controllers or services. Any data processing is done in the Services layer for whatever
// reason, not sure yet.
@Transactional
@Repository
public class PartnerDAO implements IntPartnerDAO {
	
	@PersistenceContext	
	private EntityManager entityManager;

	@Override
	public void addPartner(Partner partner) {
		Query query = entityManager.createNativeQuery(
				"INSERT INTO partners (name,contactname,contactphone,contactemail,description) " +
	            " VALUES(?,?,?,?,?)");
	        query.setParameter(1, partner.getName());
	        query.setParameter(2, partner.getContactName());
	        query.setParameter(3, partner.getContactPhone());
	        query.setParameter(4, partner.getContactEmail());
	        query.setParameter(5, partner.getDescription());
	        query.executeUpdate();
	}

	@Override
	public void initPartnerTable() {
		Query query = entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS `partners` (" + 
				"  `id` int(5) NOT NULL AUTO_INCREMENT," + 
				"  `name` varchar(100) NOT NULL," + 
				"  `contactname` varchar(100) NOT NULL," + 
				"  `contactphone` varchar(20) NOT NULL," +
				"  `contactemail` varchar(20) NOT NULL," +
				"  `description` varchar(200) NOT NULL," +
				"  PRIMARY KEY (`id`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		query.executeUpdate();
	}

}
