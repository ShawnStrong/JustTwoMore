package com.concretepage.dao;

import java.util.List;

import com.concretepage.entity.Partner;

public interface IntPartnerDAO {

	public void addPartner(Partner partner);
	
	public List<Partner> listPartner();
	
	public void initPartnerTable();
	
}
