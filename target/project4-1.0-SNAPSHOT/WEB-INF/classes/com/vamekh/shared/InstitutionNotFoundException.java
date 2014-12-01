package com.vamekh.shared;

import java.io.Serializable;

public class InstitutionNotFoundException extends Exception implements Serializable{

	private static final long serialVersionUID = 7L;

	public InstitutionNotFoundException(){
		super("Institution not found");
	}
	
}
