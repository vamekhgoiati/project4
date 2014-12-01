package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionType;
import com.vamekh.shared.InstitutionTypeDTO;

@Stateless
@Local(InstitutionTypeEJBService.class)
public class InstitutionTypeEJBServiceBean implements InstitutionTypeEJBService{
	
	@PersistenceContext(unitName="persistenceUnit")
	EntityManager em;
	
	public void addInstitutionType(InstitutionType type) {
		em.persist(type);
	}

	public void deleteInstitutionType(int id) {
		em.remove(em.find(InstitutionType.class, id));
	}

	public ArrayList<InstitutionType> getInstitutionTypes() {
		ArrayList<InstitutionType> types = new ArrayList<InstitutionType>(em
				.createQuery("SELECT e FROM InstitutionType e",
						InstitutionType.class).getResultList());
		return types;
	}

	public InstitutionType getInstitutionType(int id) {
		InstitutionType type = em.find(InstitutionType.class, id);
		return type;
	}

	public void updateInstitutionType(InstitutionType type) {
		em.merge(type);
	}
	
	public boolean typeIsUsed(InstitutionType type) {
		
		ArrayList<Institution> institutions = new ArrayList<Institution>(em.createQuery("SELECT e FROM Institution e WHERE e.type = :type", Institution.class).setParameter("type", type).getResultList());
		
		return institutions.size() > 0;
	}

	public ArrayList<InstitutionType> checkTypes(List<InstitutionType> types) {
		
		ArrayList<InstitutionType> usedTypes = new ArrayList<InstitutionType>();
		
		for(InstitutionType type : types){
			if(typeIsUsed(type)){
				usedTypes.add(type);
			}
		}
		return usedTypes;
	}

	public boolean typeIsUsed(String code, int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTypeUnique(String code, int id) {
		ArrayList<InstitutionType> types = new ArrayList<InstitutionType>(
				em.createQuery(
						"SELECT e FROM InstitutionType e WHERE e.code = :typeCode AND id != :typeId",
						InstitutionType.class).setParameter("typeCode", code)
						.setParameter("typeId", id).getResultList());

		return types.size() == 0;
	}

}
