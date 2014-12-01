package com.vamekh.shared;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity(name="FileStore")
@Table(name="FileStore")
public class FileStore implements Serializable{

	@TableGenerator(name="File_Gen",
			table="ID_GEN",
			pkColumnName="GEN_NAME",
			valueColumnName="GEN_VAL")
	
	@Id
	@GeneratedValue(generator="File_Gen")
	private int id;
	
	@Column(name="FILENAME", unique=true)
	private String fileName;
	
	@ManyToOne
	@JoinColumn(name="FI_ID")
	private Institution institution;
	
	@ManyToOne
	@JoinColumn(name="TEMPLATE_ID")
	private Template template;
	
	@Column(name="SCHEDULE_TIME")
	private String scheduleTime;
	
	@Column(name="UPLOAD_DATE")
	private Date uploadDate;
	
	@Lob
	@Column(name="FILE_CONTENT")
	private byte[] fileContent;
	
	public FileStore(){}
	
	public FileStore(String fileName, Institution institution, Template template, String scheduleTime, Date uploadDate, byte[] fileContent){
		this(0, fileName, institution, template, scheduleTime, uploadDate, fileContent);
	}
	
	public FileStore(int id, String fileName, Institution institution, Template template, String scheduleTime, Date uploadDate, byte[] fileContent){
		
		this.id = id;
		this.fileName = fileName;
		this.institution = institution;
		this.template = template;
		this.scheduleTime = scheduleTime;
		this.uploadDate = uploadDate;
		this.fileContent = fileContent;
		
	}
	
	public FileStore(FileStoreDTO fileStoreDTO){
		
		this.id = fileStoreDTO.getId();
		this.fileName = fileStoreDTO.getFileName();
		this.institution = new Institution(fileStoreDTO.getInstitution());
		this.template = new Template(fileStoreDTO.getTemplate());
		this.scheduleTime = fileStoreDTO.getScheduleTime();
		this.uploadDate = fileStoreDTO.getUploadDate();
		this.fileContent = fileStoreDTO.getFileContent();
		
	}
	
	public int getId(){ return id; }
	
	public void setId(int id){ this.id = id; }
	
	public String getFileName(){ return fileName; }
	
	public void setFileName(String fileName){ this.fileName = fileName; }
	
	public Institution getInstitution(){ return institution; }
	
	public void setInstitution(Institution institution){ this.institution = institution; }
	
	public Template getTemplate(){ return template; }
	
	public void setTemplate(Template template){ this.template = template; }
	
	public String getScheduleTime(){ return scheduleTime; }
	
	public void setScheduleTime(String scheduleTime){ this.scheduleTime = scheduleTime; }
	
	public Date getUploadDate(){ return uploadDate; }
	
	public void setUploadDate(Date uploadDate){ this.uploadDate = uploadDate; }
	
	public byte[] getFileContent(){ return fileContent; }
	
	public void setFileContent(byte[] fileContent){ this.fileContent = fileContent; }
	
}
