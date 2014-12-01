package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import com.vamekh.shared.InstitutionType;

public interface InstitutionTypeEJBService {

	void addInstitutionType(InstitutionType type);
	
	void deleteInstitutionType(int id);
		
	ArrayList<InstitutionType> getInstitutionTypes();
	
	InstitutionType getInstitutionType(int id);
	
	void updateInstitutionType(InstitutionType type);
	
	boolean typeIsUsed(InstitutionType type);
	
	boolean isTypeUnique(String code, int id);
	
	ArrayList<InstitutionType> checkTypes(List<InstitutionType> types);
	
}
