package com.vamekh.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.vamekh.shared.FileStoreDTO;
import com.vamekh.shared.IllegalFileNameException;
import com.vamekh.shared.InstitutionNotFoundException;
import com.vamekh.shared.ReturnNotFoundException;
import com.vamekh.shared.TemplateNotFoundException;

@RemoteServiceRelativePath("fileStoreService")
public interface FileStoreService extends RemoteService{
	
	FileStoreDTO addFile(FileStoreDTO fileStore) throws InstitutionNotFoundException, TemplateNotFoundException;
	
	boolean deleteFile(int id);

	boolean deleteFiles(List<FileStoreDTO> fileStores);
	
	ArrayList<FileStoreDTO> getFiles();
	
	FileStoreDTO getFile(int id);
	
	FileStoreDTO updateFile(FileStoreDTO fileStore);
	
	boolean isFileUnique(String fileName);
	
	PagingLoadResult<FileStoreDTO> getFiles(
			PagingLoadConfig config);
	
	boolean checkFileStoreByName(String fileName) throws IllegalFileNameException, InstitutionNotFoundException, TemplateNotFoundException, ReturnNotFoundException;
	
}
