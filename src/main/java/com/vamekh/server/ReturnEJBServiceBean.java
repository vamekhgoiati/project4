package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vamekh.shared.FileStore;
import com.vamekh.shared.Institution;
import com.vamekh.shared.Return;
import com.vamekh.shared.ReturnDTO;
import com.vamekh.shared.Template;

@Stateless
@Local(ReturnEJBService.class)
public class ReturnEJBServiceBean implements ReturnEJBService{

	@PersistenceContext(unitName="persistenceUnit")
	EntityManager em;
	
	public void addReturn(Return ret) {
		em.persist(ret);
	}

	public void deleteReturn(int id) {
		em.remove(em.find(Return.class, id));
	}

	public ArrayList<Return> getReturns() {
		ArrayList<Return> returns = new ArrayList<Return>(em.createQuery("SELECT e FROM Return e", Return.class).getResultList());
		return returns;
	}

	public Return getReturn(int id) {
		Return ret = em.find(Return.class, id);
		return ret;
	}

	public void updateReturn(Return ret) {
		em.merge(ret);
	}

	public ArrayList<Return> getReturnsForFI(Institution inst) {
		ArrayList<Return> returns = new ArrayList<Return>(em.createQuery("SELECT e FROM Return e where e.institution = :inst", Return.class).setParameter("inst", inst).getResultList());
		return returns;
	}

	public boolean checkInstitutionForTemplate(Institution institution, Template template) {
		ArrayList<Return> returns = new ArrayList<Return>(em.createQuery("SELECT e FROM Return e WHERE e.intitution = :institution AND e.template = :template", Return.class).setParameter("institution", institution).setParameter("template", template).getResultList());
		return returns.size() > 0;
	}

	public boolean returnIsUsed(Return ret) {
		
		ArrayList<FileStore> files = new ArrayList<FileStore>(em.createQuery("SELECT e FROM FileStore e WHERE e.institution = :institution AND e.template = :template", FileStore.class).setParameter("template", ret.getTemplate()).setParameter("institution", ret.getInstitution()).getResultList());
		
		return files.size() > 0;
		
	}

}
