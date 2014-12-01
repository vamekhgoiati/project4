package com.vamekh.server;

import java.util.ArrayList;
import java.util.List;

import com.vamekh.shared.Template;

public interface TemplateEJBService {

	void addTemplate(Template template);
	
	void deleteTemplate(int id);
	
	ArrayList<Template> getTemplates();
	
	Template getTemplate(int id);
	
	boolean templateIsUsed(Template template);
	
	void updateTemplate(Template template);
	
	boolean codeIsUnique(String code, int id);
	
	ArrayList<Template> checkTemplates(List<Template> templates);
	
	Template getTemplateByCode(String code);
	
}
