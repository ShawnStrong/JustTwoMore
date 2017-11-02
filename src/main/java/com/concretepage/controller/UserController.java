package com.concretepage.controller;

import java.util.List;

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
	public @ResponseBody String createUser (
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String user_email) {

		userDAO.createUser(username, password, user_email);
		return "all good";
	}
	
	@GetMapping("login")
	public @ResponseBody int loginUser (
			@RequestParam String username,
			@RequestParam String password) {
		
		return userDAO.login(username, password);
	}
	
	@GetMapping("init")
	public @ResponseBody String initIncomingRecordTable() {
		userDAO.initUserTable();
		return "all good";
	}
	
}