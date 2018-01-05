package com.concretepage.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntUserDAO;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private IntUserDAO userDAO;
	
	@GetMapping("create")
	public @ResponseBody int createUser (
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String user_email) {

		return userDAO.createUser(username, password, user_email);
	}
	
	@GetMapping("login")
	public @ResponseBody int loginUser (
			@RequestParam String username,
			@RequestParam String password) {
		
		return userDAO.login(username, password);
	}
	
	@GetMapping("changePassword")
	public @ResponseBody int changeUserPassword(
			@RequestParam String username,
			@RequestParam String oldpassword,
			@RequestParam String newpassword) {
			
		return userDAO.changeUserPassword(username, oldpassword, newpassword);
	}
	
	@GetMapping("init")
	public @ResponseBody String initIncomingRecordTable() {
		userDAO.initUserTable();
		return "all good";
	}
	
	@GetMapping("findIp")
	public @ResponseBody String findIp() {
		InetAddress server = null;
		try {
			server = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String serverAddress = server.getHostAddress().trim();
		System.out.println(serverAddress);
		
		return serverAddress;
	}
}