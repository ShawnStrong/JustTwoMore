package com.concretepage.dao;

import com.concretepage.entity.IncomingRecord;

public interface IIncomingRecordDAO {
	void addIncomingRecord(IncomingRecord incomingRecord);
	public void initIncomingRecordTable();
}
