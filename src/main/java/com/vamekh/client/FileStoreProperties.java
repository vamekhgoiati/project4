package com.vamekh.client;

import java.util.Date;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.vamekh.shared.FileStoreDTO;

public interface FileStoreProperties extends PropertyAccess<FileStoreDTO>{

	ValueProvider<FileStoreDTO, String> fileName();
	
	ValueProvider<FileStoreDTO, Date> uploadDate();
	
}
