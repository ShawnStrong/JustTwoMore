package com.concretepage.dao;

import com.concretepage.entity.OutgoingRecord;

public interface IntOutgoingRecordDAO {

	void addOutgoingRecord(OutgoingRecord outgoingRecord);

	void initOutgoingRecordTable();

}
