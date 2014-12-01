package com.vamekh.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.ReturnDTO;
import com.vamekh.shared.TemplateDTO;

@RemoteServiceRelativePath("returnService")
public interface ReturnService extends RemoteService{

	ReturnDTO addReturn(ReturnDTO retDTO);
	
	boolean deleteReturn(int id);
	
	boolean deleteReturns(List<ReturnDTO> returns);
	
	ArrayList<ReturnDTO> getReturns();
	
	PagingLoadResult<ReturnDTO> getReturnsForFI(PagingLoadConfig loadConfig,
			InstitutionDTO currInst);
	
	ReturnDTO getReturn(int id);
	
	ReturnDTO updateReturn(ReturnDTO retDTO);
	
	PagingLoadResult<ReturnDTO> getReturns(
			PagingLoadConfig config);
	
	ArrayList<ReturnDTO> checkReturns(List<ReturnDTO> returnDTOs);

	ArrayList<ReturnDTO> getReturnsForFI(InstitutionDTO inst);
	
}
