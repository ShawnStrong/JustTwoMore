package com.concretepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntTestDAO;

@Controller
@RequestMapping("test")
public class TestController {

	@Autowired
	private IntTestDAO testDAO;
	
	@GetMapping("init")
	public @ResponseBody int init() {
		testDAO.initTest();
		return 0;
	}
	
	@GetMapping("test2")
	public @ResponseBody int test2() {
		testDAO.initTestTwo();
		return 0;
	}
	
	@GetMapping("test3")
	public @ResponseBody int test3() {
		testDAO.initTestThree();
		return 0;
	}
	
	@GetMapping("pretest")
	public @ResponseBody int pretest() {
		testDAO.pretest();
		return 0;
	}
}
