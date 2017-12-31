package com.concretepage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.User;

// DAOs - Data Access Objects. These guys are the objects that execute MySQL statements using the
// Query object. In here you will most likely be receiving or giving java bean objects (i.e. Partner) 
// to or from controllers or services. Any data processing is done in the Services layer for whatever
// reason, not sure yet.
// More about Query here:
// https://www.thoughts-on-java.org/jpa-native-queries/
@Transactional
@Repository
public class UserDAO implements IntUserDAO {
	@PersistenceContext	
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public int createUser(String username, String password, String user_email) {
		// error codes:
		// 0 for successful creation
		// 1 for user already exists
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM user_table WHERE username='" +
				username + "';", User.class);
		
		List<User> users = query.getResultList();

		if (users.isEmpty()) {
			query = entityManager.createNativeQuery(
					"INSERT INTO user_table (username,password,user_email) " +
					" VALUES(?,?,?)");
	        	query.setParameter(1, username);
	        	query.setParameter(2, password);
	        	query.setParameter(3, user_email);
	        	query.executeUpdate();
	        
	        return 0;
		} else {
			return 1;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int login(String username, String password) {
		// error codes:
		// 0 for successful login
		// 1 for wrong password
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM user_table WHERE username='" +
				username + "';", User.class);
		
		List<User> users = query.getResultList();
		if (!users.isEmpty()) {
			return users.get(0).getPassword().equals(password)? 0 : 1;
		} else {
			return 1;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int changeUserPassword(String username, String oldpassword, String newpassword) {
		// error codes:
		// 0 for successful change
		// 1 for wrong password
		// verify old password, then insert new password
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM user_table WHERE username='" +
				username + "';", User.class);
		
		List<User> user = query.getResultList();
		String passwordFromDB = user.get(0).getPassword();
		
		if (passwordFromDB.equals(oldpassword)) {
			Query query2 = entityManager.createNativeQuery(
				"UPDATE user_table SET password = '" + newpassword + "' " + 
				"WHERE username = '" + username + "';", User.class);
			
			query2.executeUpdate();
			return 0;
		}
		
		return 1; 
		
	}

	@Override
	public void initUserTable() {
		Query query = entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS `user_table` (" + 
				"  `user_id` int(5) NOT NULL AUTO_INCREMENT," + 
				"  `username` TINYTEXT NOT NULL," + 
				"  `password` TINYTEXT NOT NULL," + 
				"  `user_email` TINYTEXT NOT NULL," +
				"  PRIMARY KEY (`user_id`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		query.executeUpdate();
	}
}