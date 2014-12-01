package com.vamekh.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.transaction.UserTransaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.vamekh.client.FileStoreService;
import com.vamekh.shared.FileStore;
import com.vamekh.shared.FileStoreDTO;
import com.vamekh.shared.IllegalFileNameException;
import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.InstitutionNotFoundException;
import com.vamekh.shared.InstitutionType;
import com.vamekh.shared.InstitutionTypeDTO;
import com.vamekh.shared.Return;
import com.vamekh.shared.ReturnDTO;
import com.vamekh.shared.ReturnNotFoundException;
import com.vamekh.shared.Template;
import com.vamekh.shared.TemplateDTO;
import com.vamekh.shared.TemplateNotFoundException;

public class FileStoreServiceImpl extends RemoteServiceServlet implements FileStoreService{

	private static final long serialVersionUID = 1L;

	FileStoreEJBService fileStoreService;
	@Resource(name="Java:UserTransaction")UserTransaction ut;
	
	@EJB
	public void setFileStoreService(FileStoreEJBService fileStoreService){
		this.fileStoreService = fileStoreService;
	}
	
	public FileStoreDTO addFile(FileStoreDTO fileStore) throws InstitutionNotFoundException, TemplateNotFoundException {
		
		FileStore file = new FileStore(fileStore);
		String[] fileNameParts = fileStore.getFileName().split(".");
		
		try {
			fileStoreService.addFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fileStore;
	}

	public boolean deleteFile(int id) {
		fileStoreService.deleteFile(id);
		return true;
	}

	public boolean deleteFiles(List<FileStoreDTO> fileStores) {
		
		for(FileStoreDTO fileStore : fileStores){
			fileStoreService.deleteFile(fileStore.getId());
		}
		
		return true;
	}

	public ArrayList<FileStoreDTO> getFiles() {
		ArrayList<FileStore> fileStores = fileStoreService.getFiles();
		ArrayList<FileStoreDTO> fileStoreDTOs = new ArrayList<FileStoreDTO>(fileStores != null ? fileStores.size() : 0);
		
		for(FileStore fileStore : fileStores){
			fileStoreDTOs.add(createFileStoreDTO(fileStore));
		}
		
		return fileStoreDTOs;
	}

	public FileStoreDTO getFile(int id) {
		FileStore fileStore = fileStoreService.getFile(id);
		return createFileStoreDTO(fileStore);
	}

	public FileStoreDTO updateFile(FileStoreDTO fileStore) {
		FileStore file = new FileStore(fileStore);
		fileStoreService.updateFile(file);
		return fileStore;
	}

	public boolean isFileUnique(String fileName) {
		return fileStoreService.isFileUnique(fileName);
	}

	public PagingLoadResult<FileStoreDTO> getFiles(PagingLoadConfig config) {
		
		ArrayList<FileStoreDTO> fileStoreDTOs = getFiles();
		ArrayList<FileStoreDTO> sublist = new ArrayList<FileStoreDTO>();

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(fileStoreDTOs, sort.getSortDir().comparator(new Comparator<FileStoreDTO>() {
						public int compare(FileStoreDTO fs1, FileStoreDTO fs2) {
							if (sortField.equals("fileName")) {
								return fs1.getFileName().compareTo(fs2.getFileName());	
							}
							
							return 0;
							
						}
					}));
				}
			}
		}

		int start = config.getOffset();
		int limit = fileStoreDTOs.size();

		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}

		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(fileStoreDTOs.get(i));
		}

		return new PagingLoadResultBean<FileStoreDTO>(sublist,
				fileStoreDTOs.size(), config.getOffset());
	
	}
	
	
	
	public boolean checkFileStoreByName(String fileName) throws IllegalFileNameException, InstitutionNotFoundException, TemplateNotFoundException, ReturnNotFoundException {
		
		String[] fileNameParts = fileName.split("\\.");
		
		if(!fileStoreService.checkInstitutionCode(fileNameParts[0])) 
			throw new InstitutionNotFoundException();
		if(!fileStoreService.checkTemplateCode(fileNameParts[1]))
			throw new TemplateNotFoundException();
		if(!fileStoreService.checkForReturn(fileNameParts[0], fileNameParts[1]))
			throw new ReturnNotFoundException();
		return true;
	}

	private FileStoreDTO createFileStoreDTO(FileStore fileStore){
		InstitutionDTO instDTO = createInstitutionDTO(fileStore.getInstitution());
		TemplateDTO tempDTO = createTemplateDTO(fileStore.getTemplate());
		return new FileStoreDTO(fileStore.getId(), fileStore.getFileName(), instDTO, tempDTO, fileStore.getScheduleTime(), fileStore.getUploadDate(), fileStore.getFileContent());
	}
	
	private InstitutionDTO createInstitutionDTO(Institution inst){
		InstitutionTypeDTO typeDTO = createInstitutionTypeDTO(inst.getType());
		return new InstitutionDTO(inst.getId(), inst.getCode(), inst.getName(), inst.getAddress(), inst.getPhone(), inst.getEmail(), inst.getFax(), typeDTO, inst.getRegDate());
	}
	
	private InstitutionTypeDTO createInstitutionTypeDTO(InstitutionType instType){
		return new InstitutionTypeDTO(instType.getId(), instType.getCode(), instType.getName());
	}
	
	private TemplateDTO createTemplateDTO(Template template){
		return new TemplateDTO(template.getId(), template.getCode(), template.getDescription(), template.getSchedule());
	}

}
