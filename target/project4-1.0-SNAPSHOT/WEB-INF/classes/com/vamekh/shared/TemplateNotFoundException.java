package com.vamekh.shared;

import java.io.Serializable;

public class TemplateNotFoundException extends Exception implements Serializable{

	private static final long serialVersionUID = 9L;

	public TemplateNotFoundException(){
		super("Template not found");
	}
	
}
