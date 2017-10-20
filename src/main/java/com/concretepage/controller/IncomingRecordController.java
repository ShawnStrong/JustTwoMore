package com.concretepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IArticleDAO;
import com.concretepage.dao.IIncomingRecordDAO;
import com.concretepage.entity.IncomingRecord;

@Controller
@RequestMapping("inc")
public class IncomingRecordController {
	@Autowired
	private IIncomingRecordDAO incomingRecordDAO;
	
	@GetMapping("add")
	public @ResponseBody String addNewIncomingRecord (
			@RequestParam String name,
			@RequestParam String food,
			@RequestParam Integer weight) {
		IncomingRecord ir = new IncomingRecord();
		ir.setName(name);
		ir.setFood(food);
		ir.setWeight(weight);
		incomingRecordDAO.addIncomingRecord(ir);
		return "all good";
	}
	
	@GetMapping("init")
	public @ResponseBody String initIncomingRecordTable() {
		incomingRecordDAO.initIncomingRecordTable();
		return "all good";
	}

}
