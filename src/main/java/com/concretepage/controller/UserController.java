package com.concretepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntUserDAO;
import com.concretepage.service.DonationService;
import com.concretepage.service.IntDonationService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private IntUserDAO userDAO;
	
	@Autowired
	private IntDonationService donationService;
	
	@GetMapping("create")
	public @ResponseBody String createUser (
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String user_email) {

		userDAO.createUser(username, password, user_email);
		return "Created user " + username;
	}
	
	@GetMapping("login")
	public @ResponseBody String loginUser (
			@RequestParam String username,
			@RequestParam String password) {
		
		int success = userDAO.login(username, password);
		if (success == 0) {
			System.out.println(donationService.findUserPage(username));
			return donationService.findUserPage(username);
		} else {
			return "";
		}
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
	
}