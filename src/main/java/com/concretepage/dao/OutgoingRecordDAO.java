package com.concretepage.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.OutgoingRecord;

@Transactional
@Repository
public class OutgoingRecordDAO implements IntOutgoingRecordDAO{

	@PersistenceContext	
	private EntityManager entityManager;
	
	@Override
	public void addOutgoingRecord(OutgoingRecord outgoingRecord) {
		Query query = entityManager.createNativeQuery("INSERT INTO outgoingrecords (name,food,weight) " +
	            " VALUES(?,?,?)");
	        query.setParameter(1, outgoingRecord.getName());
	        query.setParameter(2, outgoingRecord.getFood());
	        query.setParameter(3, outgoingRecord.getWeight());
	        query.executeUpdate();
	}
	
	@Override
	public void initOutgoingRecordTable() {
		Query query = entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS `OutgoingRecords` (" + 
				"  `id` int(5) NOT NULL AUTO_INCREMENT," + 
				"  `name` varchar(200) NOT NULL," + 
				"  `food` varchar(100) NOT NULL," + 
				"  `weight` int(20) NOT NULL," +
				"  PRIMARY KEY (`id`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		query.executeUpdate();
	}
}