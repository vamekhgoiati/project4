package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import com.vamekh.shared.Institution;
import com.vamekh.shared.Return;
import com.vamekh.shared.ReturnDTO;
import com.vamekh.shared.Template;

public interface ReturnEJBService {

	void addReturn(Return ret);
	
	void deleteReturn(int id);
	
	ArrayList<Return> getReturns();
	
	ArrayList<Return> getReturnsForFI(Institution inst);
	
	Return getReturn(int id);
	
	void updateReturn(Return ret);
	
	boolean returnIsUsed(Return ret);
	
	boolean checkInstitutionForTemplate(Institution institution, Template template);
	
}
