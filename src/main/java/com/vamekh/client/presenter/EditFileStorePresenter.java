package com.vamekh.client.presenter;


import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.vamekh.client.FileStoreServiceAsync;
import com.vamekh.client.InstitutionServiceAsync;
import com.vamekh.client.TemplateServiceAsync;
import com.vamekh.client.event.EditFileStoreCancelledEvent;
import com.vamekh.client.event.FileStoreUpdatedEvent;
import com.vamekh.shared.FileStoreDTO;
import com.vamekh.shared.IllegalFileNameException;
import com.vamekh.shared.InstitutionNotFoundException;
import com.vamekh.shared.ReturnNotFoundException;
import com.vamekh.shared.TemplateNotFoundException;

public class EditFileStorePresenter implements Presenter{

	public interface Display {
		
		HasSelectHandlers getUploadButton();
		
		HasSelectHandlers getCancelButton();
		
		String getFileName();
		
		void setFileName(String fileName);
		
		byte[] getFileContent();
		
		void setFileContent(byte[] fileContent);
		
		FormPanel getForm();
		
		FileUploadField getFileUploadField();
		
		Widget asWidget();
		
	}
	
	private FileStoreServiceAsync fsRpcService;
	private InstitutionServiceAsync instRpcService;
	private TemplateServiceAsync templateRpcService;
	private HandlerManager eventBus;
	private Display display;
	private FileStoreDTO fileStore;
	
	public EditFileStorePresenter(FileStoreServiceAsync fsRpcService, InstitutionServiceAsync instRpcService, TemplateServiceAsync templateRpcService, HandlerManager eventBus, Display display){
		
		this.fsRpcService = fsRpcService;
		this.instRpcService = instRpcService;
		this.templateRpcService = templateRpcService;
		this.eventBus = eventBus;
		this.display = display;
		fileStore = new FileStoreDTO();
		bind();
		
	}
	
	private void bind() {
				
		this.display.getUploadButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				
				String fullPath = display.getFileUploadField().getValue();
				
				String fileName = fullPath.contains("/") ? fullPath.substring(fullPath.lastIndexOf('/') + 1) : fullPath.substring(fullPath.lastIndexOf('\\') + 1);
				
				String regexStr = "[A-Za-z0-9]+\\.[A-Za-z0-9]+\\.[0-9]{8}\\.xml";
				if(fileName.matches(regexStr)){
					fsRpcService.checkFileStoreByName(fileName, new AsyncCallback<Boolean>() {
	
						public void onFailure(Throwable caught) {
							if(caught instanceof InstitutionNotFoundException){
								Window.alert("Institution not found");
							} else if(caught instanceof TemplateNotFoundException){
								Window.alert("Template not found");
							} else if(caught instanceof ReturnNotFoundException){
								Window.alert("Institution with this template does not exist");
							} else {
								Window.alert("File upload failed due to unknown error " + caught.getMessage());
							}
							
						}
	
						public void onSuccess(Boolean result) {
							if(result){
								display.getForm().submit();
							}
						}
						
					});
				} else {
					Window.alert("File name: \"" + fileName + "\" does not match patern ([Insitution Code].[Template Code].[Schedule Time].xml)");
				}
			}
		});
		
		this.display.getCancelButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				eventBus.fireEvent(new EditFileStoreCancelledEvent());
			}
		});
		
		this.display.getForm().addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			public void onSubmitComplete(SubmitCompleteEvent event) {
				eventBus.fireEvent(new FileStoreUpdatedEvent());
			}
		});
		
	}
	
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
	
}
