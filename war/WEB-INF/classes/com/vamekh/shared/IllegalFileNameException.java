package com.vamekh.shared;

import java.io.Serializable;

public class IllegalFileNameException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;

	public IllegalFileNameException(){
		super("Filename does not match pattern");
	}
	
}
