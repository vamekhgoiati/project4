package com.vamekh.server;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vamekh.shared.FileStore;
import com.vamekh.shared.IllegalFileNameException;
import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionNotFoundException;
import com.vamekh.shared.Return;
import com.vamekh.shared.Template;
import com.vamekh.shared.TemplateNotFoundException;

@Stateless
@Local(FileStoreEJBService.class)
public class FileStoreEJBServiceBean implements FileStoreEJBService{

	@PersistenceContext(unitName="persistenceUnit")
	EntityManager em;
	
	@EJB
	InstitutionEJBService institutionService;
	
	@EJB
	TemplateEJBService templateService;
	
	public void addFile(FileStore fileStore) throws IllegalFileNameException, InstitutionNotFoundException, TemplateNotFoundException{
		em.persist(fileStore);
	}

	public void deleteFile(int id) {
		em.remove(em.find(FileStore.class, id));
	}	

	public ArrayList<FileStore> getFiles() {
		ArrayList<FileStore> files = new ArrayList<FileStore>(em.createQuery("SELECT e FROM FileStore e", FileStore.class).getResultList());
		return files;
	}

	public FileStore getFile(int id) {
		return em.find(FileStore.class, id);
	}

	public void updateFile(FileStore fileStore) {
		em.merge(fileStore);
	}

	public boolean isFileUnique(String fileName) {
		ArrayList<FileStore> files = new ArrayList<FileStore>(em.createQuery("SELECT e FROM FileStore e WHERE e.fileName = :fileName", FileStore.class).setParameter("fileName", fileName).getResultList());
		
		return files.size() == 0;
	}
	
	public boolean checkInstitutionCode(String code){
		ArrayList<Institution> institutions = new ArrayList<Institution>(em.createQuery("SELECT e FROM Institution e WHERE e.code = :code", Institution.class).setParameter("code", code).getResultList());
		return institutions.size() == 1;
	}

	public boolean checkTemplateCode(String code) {
		ArrayList<Template> templates = new ArrayList<Template>(em.createQuery("SELECT e FROM Template e WHERE e.code = :code", Template.class).setParameter("code", code).getResultList());
		return templates.size() == 1;
	}

	public boolean checkForReturn(String instCode, String templCode) {
		Institution inst = institutionService.getInstitutionByCode(instCode);
		Template template = templateService.getTemplateByCode(templCode);
		ArrayList<Return> returns = new ArrayList<Return>(em.createQuery("SELECT e FROM Return e WHERE e.institution = :institution AND e.template = :template", Return.class).setParameter("institution", inst).setParameter("template", template).getResultList());
		return returns.size() > 0;
	}
	

}
