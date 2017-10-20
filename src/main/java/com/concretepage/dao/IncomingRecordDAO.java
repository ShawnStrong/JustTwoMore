package com.concretepage.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.IncomingRecord;

@Transactional
@Repository
public class IncomingRecordDAO implements IIncomingRecordDAO{

	@PersistenceContext	
	private EntityManager entityManager;
	
	@Override
	public void addIncomingRecord(IncomingRecord incomingRecord) {
		Query query = entityManager.createNativeQuery("INSERT INTO incomingrecords (name,food,weight) " +
	            " VALUES(?,?,?)");
	        query.setParameter(1, incomingRecord.getName());
	        query.setParameter(2, incomingRecord.getFood());
	        query.setParameter(3, incomingRecord.getWeight());
	        query.executeUpdate();
	}
	
	@Override
	public void initIncomingRecordTable() {
		Query query = entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS `IncomingRecords` (" + 
				"  `id` int(5) NOT NULL AUTO_INCREMENT," + 
				"  `name` varchar(200) NOT NULL," + 
				"  `food` varchar(100) NOT NULL," + 
				"  `weight` int(20) NOT NULL," +
				"  PRIMARY KEY (`id`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		query.executeUpdate();
	}

}
