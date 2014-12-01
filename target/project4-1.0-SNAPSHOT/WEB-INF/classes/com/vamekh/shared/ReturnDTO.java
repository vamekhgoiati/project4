package com.vamekh.shared;

import java.io.Serializable;

public class ReturnDTO implements Serializable{

	int id;
	
	private InstitutionDTO institution;
	private TemplateDTO template;
	private String institutionCode;
	private String templateCode;
	private String schedule;
	
	public ReturnDTO(){}
	
	public ReturnDTO(InstitutionDTO institution, TemplateDTO template){
		this(0, institution, template);
	}
	
	public ReturnDTO(int id, InstitutionDTO institution, TemplateDTO template){
		
		this.id = id;
		this.institution = institution;
		this.template = template;
		
	}
	
	public int getId(){ return id; }
	
	public void setId(int id){ this.id = id; }
	
	public InstitutionDTO getInstitution(){ return institution; }
	
	public void setInstitution(InstitutionDTO institution){ this.institution = institution; }
	
	public TemplateDTO getTemplate(){ return template; }
	
	public void setTemplate(TemplateDTO template){ this.template = template; }
	
	public String getInstitutionCode(){ return getInstitution().getCode(); }
	
	public String getTemplateCode(){ return getTemplate().getCode(); }
	
	public String getSchedule(){ return getTemplate().getSchedule().toString(); }
	
}
