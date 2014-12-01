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
import com.vamekh.client.InstitutionService;
import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.InstitutionType;
import com.vamekh.shared.InstitutionTypeDTO;

public class InstitutionServiceImpl extends RemoteServiceServlet implements InstitutionService{

	private static final long serialVersionUID = 1L;
	

	InstitutionEJBService institutionService;
	@Resource(name="java:UserTransaction") UserTransaction ut;
	
	@EJB
	public void setInstitutionService(InstitutionEJBService institutionService){
		this.institutionService = institutionService;
	}
	
	public InstitutionDTO addInstitution(InstitutionDTO instDTO) {
		
		Institution inst = new Institution(instDTO);
		institutionService.addInstitution(inst);
		
		return instDTO;
		
	}

	public boolean deleteInstitution(int id) {
		
		institutionService.deleteInstitution(id);
		
		return true;
		
	}

	public boolean deleteInstitutions(List<InstitutionDTO> institutions) {
		
		ArrayList<InstitutionDTO> usedInstitutions = checkInstitutions(institutions);
		
		for(InstitutionDTO inst : institutions){
			if(!usedInstitutions.contains(inst))
				deleteInstitution(inst.getId());
		}
		
		return true;
	}

	public ArrayList<InstitutionDTO> getInstitutions() {
		
		ArrayList<Institution> institutions = institutionService.getInstitutions();
		ArrayList<InstitutionDTO> institutionDTOs = new ArrayList<InstitutionDTO>( institutions != null ? institutions.size() : 0);
		if(institutions != null){
			for(Institution inst : institutions){
				institutionDTOs.add(createInstitutionDTO(inst));
			}
		}
		
		return institutionDTOs;
		
	}

	public InstitutionDTO getInstitution(int id) {
		
		Institution inst = institutionService.getInstitution(id);
		InstitutionDTO instDTO = createInstitutionDTO(inst);
		
		return instDTO;
		
	}

	public InstitutionDTO updateInstitution(InstitutionDTO instDTO) {
		
		Institution inst = new Institution(instDTO);
		institutionService.updateInstitution(inst);
		return instDTO;
	
	}

	public boolean institutionCodeIsUnique(String code, int id) {
		
		return institutionService.institutionCodeIsUnique(code, id);
		
	}
	
	public PagingLoadResult<InstitutionDTO> getInstitutions(
			PagingLoadConfig config) {

		ArrayList<InstitutionDTO> instDTOs = getInstitutions();
		ArrayList<InstitutionDTO> sublist = new ArrayList<InstitutionDTO>();

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(instDTOs, sort.getSortDir().comparator(new Comparator<InstitutionDTO>() {
						public int compare(InstitutionDTO inst1, InstitutionDTO inst2) {
							if (sortField.equals("code")) {
								return inst1.getCode().compareTo(inst2.getCode());	
							} else if (sortField.equals("name")) {
								return inst1.getName().compareTo(inst2.getName());
							}
							
							return 0;
							
						}
					}));
				}
			}
		}

		int start = config.getOffset();
		int limit = instDTOs.size();

		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}

		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(instDTOs.get(i));
		}

		return new PagingLoadResultBean<InstitutionDTO>(sublist,
				instDTOs.size(), config.getOffset());

	}
	
	private InstitutionTypeDTO createInstitutionTypeDTO(InstitutionType instType){
		return new InstitutionTypeDTO(instType.getId(), instType.getCode(), instType.getName());
	}
	
	private InstitutionDTO createInstitutionDTO(Institution inst){
		InstitutionTypeDTO typeDTO = createInstitutionTypeDTO(inst.getType());
		return new InstitutionDTO(inst.getId(), inst.getCode(), inst.getName(), inst.getAddress(), inst.getPhone(), inst.getEmail(), inst.getFax(), typeDTO, inst.getRegDate());
	}

	public boolean institutionIsUsed(InstitutionDTO instDTO) {
		
		Institution inst = new Institution(instDTO);
		
		return institutionService.institutionIsUsed(inst);
	}

	public ArrayList<InstitutionDTO> checkInstitutions(
			List<InstitutionDTO> instDTOs) {
		
		ArrayList<InstitutionDTO> usedInstitutions = new ArrayList<InstitutionDTO>();
		
		for(InstitutionDTO instDTO : instDTOs){
			if(institutionIsUsed(instDTO)){
				usedInstitutions.add(instDTO);
			}
		}
		return usedInstitutions;

	}


}
