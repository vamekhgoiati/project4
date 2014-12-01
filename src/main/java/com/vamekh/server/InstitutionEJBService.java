package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionType;
import com.vamekh.shared.Return;

public interface InstitutionEJBService {

	void addInstitution(Institution inst);
	
	void deleteInstitution(int id);
	
	ArrayList<Institution> getInstitutions();
	
	Institution getInstitution(int id);
	
	void updateInstitution(Institution inst);
		
	boolean institutionIsUsed(Institution inst);
	
	boolean institutionCodeIsUnique(String code, int id);
	
	ArrayList<Institution> checkInstitutions(List<Institution> insts);
	
	Institution getInstitutionByCode(String code);
			
}
