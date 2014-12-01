package com.vamekh.server;

import java.util.ArrayList;

import com.vamekh.shared.FileStore;
import com.vamekh.shared.IllegalFileNameException;
import com.vamekh.shared.InstitutionNotFoundException;
import com.vamekh.shared.TemplateNotFoundException;

public interface FileStoreEJBService {

	void addFile(FileStore fileStore) throws IllegalFileNameException, InstitutionNotFoundException, TemplateNotFoundException;
	
	void deleteFile(int id);

	ArrayList<FileStore> getFiles();
	
	FileStore getFile(int id);
	
	void updateFile(FileStore fileStore);
	
	boolean isFileUnique(String fileName);
	
	boolean checkInstitutionCode(String code);
	
	boolean checkTemplateCode(String code);
	
	boolean checkForReturn(String instCode, String templCode);
}
