package com.concretepage.dao;


public interface IntUserDAO {
	
	public int createUser(String username, String password, String user_email);
	
	public int login(String username, String password);
	
	public int changeUserPassword(int user_id, String oldpassword, String newpassword);
	
	public void initUserTable();

}
