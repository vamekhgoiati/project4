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
import com.vamekh.client.ReturnService;
import com.vamekh.shared.Institution;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.InstitutionType;
import com.vamekh.shared.InstitutionTypeDTO;
import com.vamekh.shared.Return;
import com.vamekh.shared.ReturnDTO;
import com.vamekh.shared.Template;
import com.vamekh.shared.TemplateDTO;

public class ReturnServiceImpl extends RemoteServiceServlet implements ReturnService{

	private static final long serialVersionUID = 4L;

	ReturnEJBService returnService;
	@Resource(name="java:UserTransaction") UserTransaction ut;
	
	@EJB
	public void setReturnService(ReturnEJBService returnService){
		this.returnService = returnService;
	}
	
	public ReturnDTO addReturn(ReturnDTO retDTO) {
		
		Return ret = new Return(retDTO);
		returnService.addReturn(ret);
		return retDTO;
		
	}

	public boolean deleteReturn(int id) {
		
		returnService.deleteReturn(id);
		
		return true;
	
	}

	public boolean deleteReturns(List<ReturnDTO> returns) {
		
		ArrayList<ReturnDTO> usedReturns = checkReturns(returns);
		
		for(ReturnDTO ret : returns){
			if(!usedReturns.contains(ret))
				deleteReturn(ret.getId());
		}
		
		return true;
	}

	public ArrayList<ReturnDTO> getReturns() {
		
		ArrayList<Return> returns = returnService.getReturns();
		ArrayList<ReturnDTO> returnDTOs = new ArrayList<ReturnDTO>( returns != null ? returns.size() : 0);
		if(returns != null){
			for(Return ret : returns){
				returnDTOs.add(createReturnDTO(ret));
			}
		}
		
		return returnDTOs;
	
	}

	public ReturnDTO getReturn(int id) {
		
		Return ret = returnService.getReturn(id);
		ReturnDTO retDTO = createReturnDTO(ret);
		
		return retDTO;
		
	}

	public ReturnDTO updateReturn(ReturnDTO retDTO) {
		
		Return ret = new Return(retDTO);
		returnService.updateReturn(ret);
		
		return retDTO;
		
	}

	private ReturnDTO createReturnDTO(Return ret){
		InstitutionDTO instDTO = createInstitutionDTO(ret.getInstitution());
		TemplateDTO tempDTO = createTemplateDTO(ret.getTemplate());
		return new ReturnDTO(ret.getId(), instDTO, tempDTO);
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

	public PagingLoadResult<ReturnDTO> getReturns(PagingLoadConfig config) {
		
		ArrayList<ReturnDTO> returnDTOs = getReturns();
		ArrayList<ReturnDTO> sublist = new ArrayList<ReturnDTO>();

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(returnDTOs, sort.getSortDir().comparator(new Comparator<ReturnDTO>() {
						public int compare(ReturnDTO r1, ReturnDTO r2) {
							
							return 0;
							
						}
					}));
				}
			}
		}

		int start = config.getOffset();
		int limit = returnDTOs.size();

		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}

		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(returnDTOs.get(i));
		}

		return new PagingLoadResultBean<ReturnDTO>(sublist,
				returnDTOs.size(), config.getOffset());

	}

	public ArrayList<ReturnDTO> getReturnsForFI(InstitutionDTO inst) {
		
		Institution institution = new Institution(inst);
		ArrayList<Return> returns = returnService.getReturnsForFI(institution);
		ArrayList<ReturnDTO> returnDTOs = new ArrayList<ReturnDTO>(returns != null ? returns.size() : 0);
		if(returns != null){
			for(Return ret : returns){
				returnDTOs.add(createReturnDTO(ret));
			}
		}
		
		return returnDTOs;
	}

	public boolean returnIsUsed(ReturnDTO retDTO){
		
		Return ret = new Return(retDTO);
		
		return returnService.returnIsUsed(ret);
	}
	
	public ArrayList<ReturnDTO> checkReturns(List<ReturnDTO> returnDTOs) {
		ArrayList<ReturnDTO> usedReturns = new ArrayList<ReturnDTO>();
		
		for(ReturnDTO retDTO : returnDTOs){
			if(returnIsUsed(retDTO)){
				usedReturns.add(retDTO);
			}
		}
		return usedReturns;
	}

	public PagingLoadResult<ReturnDTO> getReturnsForFI(
			PagingLoadConfig config, InstitutionDTO currInst) {
		ArrayList<ReturnDTO> returnDTOs = getReturnsForFI(currInst);
		ArrayList<ReturnDTO> sublist = new ArrayList<ReturnDTO>();

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(returnDTOs, sort.getSortDir().comparator(new Comparator<ReturnDTO>() {
						public int compare(ReturnDTO r1, ReturnDTO r2) {
							
							return 0;
							
						}
					}));
				}
			}
		}

		int start = config.getOffset();
		int limit = returnDTOs.size();

		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}

		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(returnDTOs.get(i));
		}

		return new PagingLoadResultBean<ReturnDTO>(sublist,
				returnDTOs.size(), config.getOffset());
	
	}
	
	

}
