package com.vamekh.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.HasRowClickHandlers;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.vamekh.client.FileStoreProperties;
import com.vamekh.client.FileStoreServiceAsync;
import com.vamekh.client.InstitutionServiceAsync;
import com.vamekh.client.InstitutionTypeServiceAsync;
import com.vamekh.client.TemplateServiceAsync;
import com.vamekh.client.event.AddFileStoreEvent;
import com.vamekh.client.event.AddInstitutionEvent;
import com.vamekh.client.event.AddInstitutionTypeEvent;
import com.vamekh.client.event.AddTemplateEvent;
import com.vamekh.client.event.EditInstitutionEvent;
import com.vamekh.client.event.EditInstitutionTypeEvent;
import com.vamekh.client.event.EditTemplateEvent;
import com.vamekh.client.event.InstitutionTypeDeletedEvent;
import com.vamekh.client.event.InstitutionTypeUpdatedEvent;
import com.vamekh.shared.FileStore;
import com.vamekh.shared.FileStoreDTO;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.InstitutionTypeDTO;
import com.vamekh.shared.TemplateDTO;

public class MainPresenter implements Presenter {

	private List<InstitutionTypeDTO> typeList;
	private List<InstitutionDTO> institutionList;
	private List<TemplateDTO> templateList;

	public interface Display {
		
		HasSelectHandlers getAddTypeButton();
		
		HasSelectHandlers getEditTypeButton();

		HasSelectHandlers getDeleteTypeButton();
		
		HasSelectHandlers getAddInstButton();

		HasSelectHandlers getEditInstButton();
		
		HasSelectHandlers getDeleteInstButton();
		
		HasSelectHandlers getAddTemplateButton();
		
		HasSelectHandlers getEditTemplateButton();

		HasSelectHandlers getDeleteTemplateButton();
		
		HasSelectHandlers getAddFileButton();
		
		HasSelectHandlers getDownloadFileButton();
		
		HasSelectHandlers getDeleteFileButton();

		void refreshTypeData();

		void refreshInstitutionData();

		void refreshTemplateData();
		
		void refreshFileData();

		List<InstitutionTypeDTO> getSelectedTypeRows();
		
		List<InstitutionDTO> getSelectedInstitutionRows();
		
		List<TemplateDTO> getSelectedTemplateRows();
		
		List<FileStoreDTO> getSelectedFileRows();
		
		Widget asWidget();
		
	}

	private InstitutionTypeServiceAsync typeRpcService;
	private InstitutionServiceAsync instRpcService;
	private TemplateServiceAsync templateRpcService;
	private FileStoreServiceAsync fsRpcService;
	private HandlerManager eventBus;
	private Display display;
	private PagingLoadConfig config;

	public MainPresenter(InstitutionTypeServiceAsync typeRpcService,
			InstitutionServiceAsync instRpcService,
			TemplateServiceAsync templateRpcService, FileStoreServiceAsync fsRpcService, HandlerManager eventBus,
			Display display) {
		this.typeRpcService = typeRpcService;
		this.instRpcService = instRpcService;
		this.templateRpcService = templateRpcService;
		this.fsRpcService = fsRpcService;
		this.eventBus = eventBus;
		this.display = display;

	}

	private void bind(){
		
		display.getAddTypeButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				eventBus.fireEvent(new AddInstitutionTypeEvent());
			}
		});
		
		display.getEditTypeButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				List<InstitutionTypeDTO> selectedTypes = display.getSelectedTypeRows();
				if(selectedTypes.size() == 1){
					eventBus.fireEvent(new EditInstitutionTypeEvent(selectedTypes.get(0).getId()));
				} else if(selectedTypes.size() > 1) {
					Window.alert("Only one institution type can be edited!");
				} else {
					Window.alert("Nothing to edit!");
				}
			}
		});
		
		display.getDeleteTypeButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				deleteSelectedTypes();
			}
		});
		
		display.getAddInstButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				eventBus.fireEvent(new AddInstitutionEvent());
			}
		});
		
		display.getEditInstButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				List<InstitutionDTO> selectedInstitutions = display.getSelectedInstitutionRows();
				if(selectedInstitutions.size() == 1){
					eventBus.fireEvent(new EditInstitutionEvent(selectedInstitutions.get(0).getId()));
				} else if(selectedInstitutions.size() > 1) {
					Window.alert("Only one institution can be edited!");
				} else {
					Window.alert("Nothing to edit!");
				}
			}
		});
		
		display.getDeleteInstButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				deleteSelectedInstitutions();
			}
		});
		
		display.getAddTemplateButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				eventBus.fireEvent(new AddTemplateEvent());
			}
		});
		
		display.getEditTemplateButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				List<TemplateDTO> selectedTemplates = display.getSelectedTemplateRows();
				if(selectedTemplates.size() == 1){
					eventBus.fireEvent(new EditTemplateEvent(selectedTemplates.get(0).getId()));
				} else if(selectedTemplates.size() > 1) {
					Window.alert("Only one template can be edited!");
				} else {
					Window.alert("Nothing to edit!");
				}
			}
		});
		
		display.getDeleteTemplateButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				deleteSelectedTemplates();
			}
		});
		
		display.getAddFileButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				eventBus.fireEvent(new AddFileStoreEvent());
			}
		});
		
		display.getDownloadFileButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				List<FileStoreDTO> selectedFiles = display.getSelectedFileRows();
				if(selectedFiles.size() == 0){
					Window.alert("Please select file to download");
				} else if (selectedFiles.size() == 1) {
					String baseURL = GWT.getModuleBaseURL();
					String url = baseURL + "fileDownload?id=" + selectedFiles.get(0).getId();
					Window.open(url, "", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=no,toolbar=true, width=" + (Window.getClientWidth() / 2) + ",height=" + (Window.getClientHeight() / 2));
				} else {
					Window.alert("Please select one file");
				}
			}
		});
		
		display.getDeleteFileButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				deleteSelectedFiles();
			}
		});
		
		
	}
	
	private void deleteSelectedFiles(){
		final List<FileStoreDTO> selectedFiles = display.getSelectedFileRows();
		
		if(selectedFiles.size() == 0){
			Window.alert("Nothing to delete");
		} else {
			fsRpcService.deleteFiles(selectedFiles, new AsyncCallback<Boolean>() {

				public void onFailure(Throwable caught) {
					Window.alert("Error deleting files");
				}

				public void onSuccess(Boolean result) {
					display.refreshFileData();
				}
				
			});
		}
	}
	
	private void deleteSelectedTypes(){
		
		final List<InstitutionTypeDTO> selectedTypes = display.getSelectedTypeRows();
		
		if(selectedTypes.size() == 0){
			Window.alert("Nothing to delete");
		} else {
			typeRpcService.checkTypes(selectedTypes, new AsyncCallback<ArrayList<InstitutionTypeDTO>>() {

				public void onFailure(Throwable caught) {
					Window.alert("Error checking types");
				}

				public void onSuccess(ArrayList<InstitutionTypeDTO> result) {
					String usedTypes = "";
					if(result.size() > 0){
						for(InstitutionTypeDTO type : result){
							usedTypes += "Institution type with code " + type.getCode() + " is used and can't be deleted \n";
						}
						
					}
					
					typeRpcService.deleteInstitutionTypes(selectedTypes, new AsyncCallback<Boolean>() {
						
						public void onFailure(Throwable caught) {
							Window.alert("Error deleting institution types");
						}
			
						public void onSuccess(Boolean result) {
							if(result){
								display.refreshTypeData();
							}
						}
						
					});
					
					if(!usedTypes.equals("")){
						Window.alert(usedTypes);
					}
				}
			});
			
		}
		
	}
	
	private void deleteSelectedInstitutions(){
		
		final List<InstitutionDTO> selectedInstitutions = display.getSelectedInstitutionRows();
		
		if(selectedInstitutions.size() == 0){
			Window.alert("Nothing to delete");
		} else {
			instRpcService.checkInstitutions(selectedInstitutions, new AsyncCallback<ArrayList<InstitutionDTO>>() {

				public void onFailure(Throwable caught) {
					Window.alert("Error checking institutions");
				}

				public void onSuccess(ArrayList<InstitutionDTO> result) {
					String usedInstitutions = "";
					if(result.size() > 0){
						for(InstitutionDTO inst : result){
							usedInstitutions += "Institution with code " + inst.getCode() + " is used and can't be deleted \n";
						}
					}
					
					instRpcService.deleteInstitutions(selectedInstitutions, new AsyncCallback<Boolean>() {
						
						public void onFailure(Throwable caught) {
							Window.alert("Error deleting institutions");
						}
			
						public void onSuccess(Boolean result) {
							if(result){
								display.refreshInstitutionData();
							}
						}
						
					});
					
					if(!usedInstitutions.equals("")){
						Window.alert(usedInstitutions);
					}
				}
				
			});
			
		}
		
	}
	
	private void deleteSelectedTemplates(){
		
		final List<TemplateDTO> selectedTemplates = display.getSelectedTemplateRows();
		
		if(selectedTemplates.size() == 0){
			Window.alert("Nothing to delete");
		} else {
			templateRpcService.checkTemplates(selectedTemplates, new AsyncCallback<ArrayList<TemplateDTO>>() {

				public void onFailure(Throwable caught) {
					Window.alert("Error checking templates");
				}

				public void onSuccess(ArrayList<TemplateDTO> result) {
					String usedTemplates = "";
					if(result.size() > 0){
						for(TemplateDTO template : result){
							usedTemplates += "Template with code " + template.getCode() + " is used and can't be deleted \n";
						}
					}
					
					templateRpcService.deleteTemplates(selectedTemplates, new AsyncCallback<Boolean>() {
						
						public void onFailure(Throwable caught) {
							Window.alert("Error deleting templates");
						}
			
						public void onSuccess(Boolean result) {
							if(result){
								display.refreshTemplateData();
							}
						}
						
					});
					
					if(!usedTemplates.equals("")){
						Window.alert(usedTemplates);
					}
					
				}
				
			});
			
		}
	}
	
	
	
	public void setTemplates(List<TemplateDTO> templateDTOs){
		this.templateList = templateDTOs;
	}
	
	public TemplateDTO getTemplate(int index){
		return templateList.get(index);
	}
	
	public void setTypes(List<InstitutionTypeDTO> typeDTOs){
		this.typeList = typeDTOs;
	}
	
	public InstitutionTypeDTO getType(int index){
		return typeList.get(index);
	}
	
	public void setInstitutions(List<InstitutionDTO> institutionDTOs){
		this.institutionList = institutionDTOs;
	}
	
	public InstitutionDTO getInstitution(int index){
		return institutionList.get(index);
	}
	
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

}
