package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vamekh.shared.FileStore;
import com.vamekh.shared.Return;
import com.vamekh.shared.Template;
import com.vamekh.shared.TemplateDTO;

@Stateless
@Local(TemplateEJBService.class)
public class TemplateEJBServiceBean implements TemplateEJBService{

	@PersistenceContext(unitName="persistenceUnit")
	EntityManager em;
	
	public void addTemplate(Template template) {
		em.persist(template);
	}

	public void deleteTemplate(int id) {
		em.remove(em.find(Template.class, id));
	}

	public ArrayList<Template> getTemplates() {
		
		ArrayList<Template> templates = new ArrayList<Template>(em.createQuery("SELECT e FROM Template e", Template.class).getResultList());
		
		return templates;
	}

	public Template getTemplate(int id) {
		return em.find(Template.class, id);
	}

	public void updateTemplate(Template template) {
		em.merge(template);
	}

	public boolean codeIsUnique(String code, int id) {
		ArrayList<Template> templates = new ArrayList<Template>(em.createQuery("SELECT e FROM Template e WHERE e.code = :tmpCode AND id != :tmpId", Template.class).setParameter("tmpCode", code).setParameter("tmpId", id).getResultList());
		
		return templates.size() == 0;
	}

	public ArrayList<Template> checkTemplates(List<Template> templates) {
		ArrayList<Template> usedTemplates = new ArrayList<Template>();
		
		for(Template template : templates){
			usedTemplates.add(template);
		}
		return usedTemplates;
	}

	public boolean templateIsUsed(Template template) {
		
		ArrayList<Return> returns = new ArrayList<Return>(em.createQuery("SELECT e FROM Return e where e.template = :template", Return.class).setParameter("template", template).getResultList());
		ArrayList<FileStore> files = new ArrayList<FileStore>(em.createQuery("SELECT e FROM FileStore e WHERE e.template = :template", FileStore.class).setParameter("template", template).getResultList());
		
		return (returns.size() > 0 || files.size() > 0);
	}

	public Template getTemplateByCode(String code) {
		Template template = em.createQuery("SELECT e FROM Template e WHERE e.code = :code", Template.class).setParameter("code", code).getSingleResult();
		return template;
	}

}
