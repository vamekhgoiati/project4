package com.vamekh.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.transaction.UserTransaction;
import javax.annotation.Resource;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.vamekh.client.InstitutionTypeService;
import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.InstitutionType;
import com.vamekh.shared.InstitutionTypeDTO;

public class InstitutionTypeServiceImpl extends RemoteServiceServlet implements
		InstitutionTypeService {

	private static final long serialVersionUID = 2L;

	InstitutionTypeEJBService typeService;
	@Resource(name="java:UserTransaction") UserTransaction ut;
	
	@EJB
	public void setTypeService(InstitutionTypeEJBService typeService){
		this.typeService = typeService;
	}
	

	public InstitutionTypeDTO addInstitutionType(InstitutionTypeDTO typeDTO) {

		InstitutionType type = new InstitutionType(typeDTO);
		typeService.addInstitutionType(type);

		return typeDTO;

	}

	public boolean deleteInstitutionType(int id) {

		typeService.deleteInstitutionType(id);

		return true;

	}

	public boolean deleteInstitutionTypes(
			List<InstitutionTypeDTO> types) {

		for (InstitutionTypeDTO type : types) {
			if(!typeIsUsed(type)){
				deleteInstitutionType(type.getId());
			}
		}

		return true;
	}

	public ArrayList<InstitutionTypeDTO> getInstitutionTypes() {

		ArrayList<InstitutionType> types = typeService.getInstitutionTypes();
		ArrayList<InstitutionTypeDTO> typeDTOs = new ArrayList<InstitutionTypeDTO>(
				types != null ? types.size() : 0);
		if (types != null) {
			for (InstitutionType type : types) {
				typeDTOs.add(createInstitutionTypeDTO(type));
			}
		}

		return typeDTOs;

	}

	public InstitutionTypeDTO getInstitutionType(int id) {

		InstitutionType type = typeService.getInstitutionType(id);
		InstitutionTypeDTO typeDTO = createInstitutionTypeDTO(type);

		return typeDTO;

	}

	public InstitutionTypeDTO updateInstitutionType(InstitutionTypeDTO typeDTO) {

		InstitutionType type = new InstitutionType(typeDTO);
		typeService.updateInstitutionType(type);

		return typeDTO;

	}

	public boolean isTypeUnique(String code, int id) {

		return typeService.isTypeUnique(code, id);

	}

	private InstitutionTypeDTO createInstitutionTypeDTO(InstitutionType instType) {
		return new InstitutionTypeDTO(instType.getId(), instType.getCode(),
				instType.getName());
	}

	public PagingLoadResult<InstitutionTypeDTO> getInstitutionTypes(
			PagingLoadConfig config) {

		ArrayList<InstitutionTypeDTO> typeDTOs = getInstitutionTypes();
		ArrayList<InstitutionTypeDTO> sublist = new ArrayList<InstitutionTypeDTO>();

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(typeDTOs, sort.getSortDir().comparator(new Comparator<InstitutionTypeDTO>() {
						public int compare(InstitutionTypeDTO t1, InstitutionTypeDTO t2) {
							if (sortField.equals("code")) {
								return t1.getCode().compareTo(t2.getCode());	
							} else if (sortField.equals("name")) {
								return t1.getName().compareTo(t2.getName());
							}
							
							return 0;
							
						}
					}));
				}
			}
		}

		int start = config.getOffset();
		int limit = typeDTOs.size();

		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}

		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(typeDTOs.get(i));
		}

		return new PagingLoadResultBean<InstitutionTypeDTO>(sublist,
				typeDTOs.size(), config.getOffset());

	}

	
	
	public boolean typeIsUsed(InstitutionTypeDTO type) {
		
		InstitutionType instType = new InstitutionType(type);
		
		return typeService.typeIsUsed(instType);
	}

	public ArrayList<InstitutionTypeDTO> checkTypes(
			List<InstitutionTypeDTO> typeDTOs) {
		
		ArrayList<InstitutionTypeDTO> usedTypes = new ArrayList<InstitutionTypeDTO>();
		
		for(InstitutionTypeDTO typeDTO : typeDTOs){
			if(typeIsUsed(typeDTO)){
				usedTypes.add(typeDTO);
			}
		}
		return usedTypes;
	}
}
