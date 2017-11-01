package com.concretepage.dao;

import java.util.List;

import com.concretepage.entity.Org;

public interface IntOrgDAO {
	
	public List<Org> listPartner();
	
	public void initPartnerTable();

	public int createOrg(String org_name, String contact_name, String contact_number, String contact_email, String notes);

	public int updateOrg(String org_name, String contact_name, String contact_number, String contact_email,
			String notes);
	
}
