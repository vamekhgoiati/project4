package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vamekh.shared.FileStore;
import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.Return;

@Stateless
@Local(InstitutionEJBService.class)
public class InstitutionEJBServiceBean implements InstitutionEJBService{

	@PersistenceContext(unitName="persistenceUnit")
	EntityManager em;
	
	public void addInstitution(Institution inst) {
		em.persist(inst);
	}

	public void deleteInstitution(int id) {
		em.remove(em.find(Institution.class, id));
	}

	public ArrayList<Institution> getInstitutions() {
		ArrayList<Institution> institutions = new ArrayList<Institution>(em.createQuery("SELECT e FROM Institution e", Institution.class).getResultList());
		
		return institutions;
	}

	public Institution getInstitution(int id) {
		return em.find(Institution.class, id);
	}

	public void updateInstitution(Institution inst) {
		em.merge(inst);
	}

	public boolean institutionIsUsed(Institution inst) {
		
		ArrayList<Return> returns = new ArrayList<Return>(em.createQuery("SELECT e FROM Return e where e.institution = :institution", Return.class).setParameter("institution", inst).getResultList());
		ArrayList<FileStore> files = new ArrayList<FileStore>(em.createQuery("SELECT e FROM FileStore e WHERE e.institution = :institution", FileStore.class).setParameter("institution", inst).getResultList());
		
		return (returns.size() > 0 || files.size() > 0);
	
	}

	public boolean institutionCodeIsUnique(String code, int id) {
		ArrayList<Institution> institutions = new ArrayList<Institution>(em.createQuery("SELECT e FROM Institution e WHERE e.code = :instCode AND id != :instId", Institution.class).setParameter("instCode", code).setParameter("instId", id).getResultList());
		
		return institutions.size() == 0;
	}

	public ArrayList<Institution> checkInstitutions(List<Institution> insts) {
		ArrayList<Institution> usedInstitutions = new ArrayList<Institution>();
		
		for(Institution inst : insts){
			usedInstitutions.add(inst);
		}
		return usedInstitutions;
	}

	public Institution getInstitutionByCode(String code) {
		Institution institution = em.createQuery("SELECT e FROM Institution e WHERE e.code = :code", Institution.class).setParameter("code", code).getSingleResult();
		return institution;
	}

}
