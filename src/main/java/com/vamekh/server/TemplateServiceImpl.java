package com.vamekh.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.transaction.UserTransaction;

import oracle.jdbc.driver.ArrayLocatorResultSet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.vamekh.client.TemplateService;
import com.vamekh.shared.Return;
import com.vamekh.shared.Template;
import com.vamekh.shared.TemplateDTO;

public class TemplateServiceImpl extends RemoteServiceServlet implements TemplateService{

	private static final long serialVersionUID = 3L;

	TemplateEJBService templateService;
	@Resource(name="java:UserTransaction") UserTransaction ut;
	
	@EJB
	public void setTemplateService(TemplateEJBService templateService){
		this.templateService = templateService;
	}
	
	public TemplateDTO addTemplate(TemplateDTO templateDTO) {
		
		Template template = new Template(templateDTO);
		templateService.addTemplate(template);
		
		return templateDTO;
		
	}

	public boolean deleteTemplate(int id) {
		
		templateService.deleteTemplate(id);
		
		return true;
		
	}

	public boolean deleteTemplates(List<TemplateDTO> templates) {
		
		ArrayList<TemplateDTO> usedTemplates = checkTemplates(templates);
		
		for(TemplateDTO tmp : templates){
			if(!usedTemplates.contains(tmp))
				deleteTemplate(tmp.getId());
		}
		
		return true;
		
	}

	public ArrayList<TemplateDTO> getTemplates() {
		
		ArrayList<Template> templates = templateService.getTemplates();
		ArrayList<TemplateDTO> templateDTOs = new ArrayList<TemplateDTO>( templates != null ? templates.size() : 0);
		if(templates != null){
			for(Template template : templates){
				templateDTOs.add(createTemplateDTO(template));
			}
		}
		
		return templateDTOs;
		
	}

	public TemplateDTO getTemplate(int id) {
		
		Template template = templateService.getTemplate(id);
		TemplateDTO templateDTO = createTemplateDTO(template);
		
		return templateDTO;
		
	}

	public TemplateDTO updateTemplate(TemplateDTO templateDTO) {
		
		Template template = new Template(templateDTO);
		templateService.updateTemplate(template);
		return templateDTO;
		
	}
	
	public boolean codeIsUnique(String code, int id){
		
		return templateService.codeIsUnique(code, id);
		
	}
	
	public PagingLoadResult<TemplateDTO> getTemplates(
			PagingLoadConfig config) {

		ArrayList<TemplateDTO> templateDTOs = getTemplates();
		ArrayList<TemplateDTO> sublist = new ArrayList<TemplateDTO>();

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(templateDTOs, sort.getSortDir().comparator(new Comparator<TemplateDTO>() {
						public int compare(TemplateDTO t1, TemplateDTO t2) {
							if (sortField.equals("code")) {
								return t1.getCode().compareTo(t2.getCode());	
							} else if (sortField.equals("description")) {
								return t1.getDescription().compareTo(t2.getDescription());
							}
							
							return 0;
							
						}
					}));
				}
			}
		}

		int start = config.getOffset();
		int limit = templateDTOs.size();

		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}

		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(templateDTOs.get(i));
		}

		return new PagingLoadResultBean<TemplateDTO>(sublist,
				templateDTOs.size(), config.getOffset());

	}
	
	private TemplateDTO createTemplateDTO(Template template){
		return new TemplateDTO(template.getId(), template.getCode(), template.getDescription(), template.getSchedule());
	}
	
	public boolean templateIsUsed(TemplateDTO templateDTO) {
		
		Template template = new Template(templateDTO);
		
		return templateService.templateIsUsed(template);
		
	}

	public ArrayList<TemplateDTO> checkTemplates(List<TemplateDTO> templateDTOs) {
		
		ArrayList<TemplateDTO> usedTemplates = new ArrayList<TemplateDTO>();
		
		for(TemplateDTO templateDTO : templateDTOs){
			if(templateIsUsed(templateDTO)){
				usedTemplates.add(templateDTO);
			}
		}
		return usedTemplates;
	}

}
