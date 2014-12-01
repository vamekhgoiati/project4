package com.vamekh.shared;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name="Return")
@Table(name="RETURNS",
	    uniqueConstraints=
	        @UniqueConstraint(columnNames={"FI_ID", "TEMPLATE_ID"}))
public class Return {
	
	@Id
	@GeneratedValue
	int id;
	
	@ManyToOne
	@JoinColumn(name="FI_ID")
	Institution institution;
	
	@ManyToOne
	@JoinColumn(name="TEMPLATE_ID")
	Template template;
	
	public Return(){}
	
	public Return(Institution institution, Template template){
		this(0, institution, template);
	}
	
	public Return(int id, Institution institution, Template template){
		
		this.id = id;
		this.institution = institution;
		this.template = template;
		
	}
	
	public Return(ReturnDTO retDTO){
		
		this.id = retDTO.getId();
		this.institution = new Institution(retDTO.getInstitution());
		this.template = new Template(retDTO.getTemplate());
		
	}
	
	public int getId(){ return id; }
	
	public void setId(int id){ this.id = id; }
	
	public Institution getInstitution(){ return institution; }
	
	public void setInstitution(Institution institution){ this.institution = institution; }

	public Template getTemplate(){ return template; }
	
	public void setTemplate(Template template){ this.template = template; }
	
}
