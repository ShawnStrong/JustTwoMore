package com.concretepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.concretepage.dao.IntOutgoingRecordDAO;
import com.concretepage.entity.OutgoingRecord;

@Controller
@RequestMapping("out")
public class OutgoingRecordController {
	@Autowired
	private IntOutgoingRecordDAO outgoingRecordDAO;
	
	@GetMapping("add")
	public @ResponseBody String addNewIncomingRecord (
			@RequestParam String name,
			@RequestParam String food,
			@RequestParam Integer weight) {
		OutgoingRecord or = new OutgoingRecord();
		or.setName(name);
		or.setFood(food);
		or.setWeight(weight);
		outgoingRecordDAO.addOutgoingRecord(or);
		return "all good";
	}
	
	@GetMapping("init")
	public @ResponseBody String initIncomingRecordTable() {
		outgoingRecordDAO.initOutgoingRecordTable();
		return "all good";
	}
}