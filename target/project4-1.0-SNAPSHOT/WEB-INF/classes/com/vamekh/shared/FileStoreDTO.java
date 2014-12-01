package com.vamekh.shared;

import java.io.Serializable;
import java.util.Date;

public class FileStoreDTO implements Serializable{

	private int id;
	
	private String fileName;
	
	private InstitutionDTO institution;
	
	private TemplateDTO template;
	
	private String scheduleTime;
	
	private Date uploadDate;
	
	private byte[] fileContent;
	
	public FileStoreDTO(){}
	
	public FileStoreDTO(String fileName, InstitutionDTO institution, TemplateDTO template, String scheduleTime, Date uploadDate, byte[] fileContent){
		this(0, fileName, institution, template, scheduleTime, uploadDate, fileContent);
	}
	
	public FileStoreDTO(int id, String fileName, InstitutionDTO institution, TemplateDTO template, String scheduleTime, Date uploadDate, byte[] fileContent){
		
		this.id = id;
		this.fileName = fileName;
		this.institution = institution;
		this.template = template;
		this.scheduleTime = scheduleTime;
		this.uploadDate = uploadDate;
		this.fileContent = fileContent;
		
	}
	
	public int getId(){ return id; }
	
	public void setId(int id){ this.id = id; }
	
	public String getFileName(){ return fileName; }
	
	public void setFileName(String fileName){ this.fileName = fileName; }
	
	public InstitutionDTO getInstitution(){ return institution; }
	
	public void setInstitution(InstitutionDTO institution){ this.institution = institution; }
	
	public TemplateDTO getTemplate(){ return template; }
	
	public void setTemplate(TemplateDTO template){ this.template = template; }
	
	public String getScheduleTime(){ return scheduleTime; }
	
	public void setScheduleTime(String scheduleTime){ this.scheduleTime = scheduleTime; }
	
	public Date getUploadDate(){ return uploadDate; }
	
	public void setUploadDate(Date uploadDate){ this.uploadDate = uploadDate; }
	
	public byte[] getFileContent(){ return fileContent; }
	
	public void setFileContent(byte[] fileContent){ this.fileContent = fileContent; }
	
}
