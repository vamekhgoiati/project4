package com.vamekh.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.vamekh.client.InstitutionServiceAsync;
import com.vamekh.client.ReturnServiceAsync;
import com.vamekh.client.event.EditReturnCancelledEvent;
import com.vamekh.client.event.ReturnUpdatedEvent;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.ReturnDTO;
import com.vamekh.shared.TemplateDTO;

public class EditReturnPresenter implements Presenter{
	
	public interface Display{
		
		HasSelectHandlers getSaveButton();

		HasSelectHandlers getCancelButton();
		
		InstitutionDTO getInstitution();
		
		void setInstitution(InstitutionDTO inst);
		
		TemplateDTO getTemplate();
		
		void setTemplate(TemplateDTO template);
		
		Widget asWidget();
		
	}
	
	private ReturnServiceAsync rpcService;
	private InstitutionServiceAsync institutionRpcService;
	private HandlerManager eventBus;
	private Display display;
	private ReturnDTO returnDTO;
	
	public EditReturnPresenter(ReturnServiceAsync rpcService, InstitutionServiceAsync institutionRpcService, HandlerManager eventBus, Display display, int fi){
		
		this.rpcService = rpcService;
		this.institutionRpcService = institutionRpcService;
		this.eventBus = eventBus;
		this.display = display;
		returnDTO = new ReturnDTO();
		bind();
		
		institutionRpcService.getInstitution(fi, new AsyncCallback<InstitutionDTO>() {

			public void onFailure(Throwable caught) {
				Window.alert("Error retrieving institution");
			}

			public void onSuccess(InstitutionDTO result) {
				EditReturnPresenter.this.display.setInstitution(result);
			}
			
			
			
		});
		
	}
	
	public EditReturnPresenter(ReturnServiceAsync rpcService, HandlerManager eventBus, Display display, int fi, int id){
		
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		bind();
		
		rpcService.getReturn(id, new AsyncCallback<ReturnDTO>() {
			
			public void onSuccess(ReturnDTO result) {
				returnDTO = result;
				EditReturnPresenter.this.display.setInstitution(returnDTO.getInstitution());
				EditReturnPresenter.this.display.setTemplate(returnDTO.getTemplate());
			}
			
			public void onFailure(Throwable caught) {
				Window.alert("Error retrieving return");
			}
		});
		
	}
	
	private void bind() {
		
		this.display.getSaveButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				doSave();
			}
		});
		
		this.display.getCancelButton().addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				eventBus.fireEvent(new EditReturnCancelledEvent(EditReturnPresenter.this.display.getInstitution()));
			}
		});
		
	}

	
	private void doSave(){
		
		returnDTO.setInstitution(display.getInstitution());
		returnDTO.setTemplate(display.getTemplate());
		if(returnDTO.getId() > 0){
			rpcService.updateReturn(returnDTO, new AsyncCallback<ReturnDTO>() {
				
				public void onSuccess(ReturnDTO result) {
					eventBus.fireEvent(new ReturnUpdatedEvent(result));
				}
				
				public void onFailure(Throwable caught) {
					Window.alert("Error updating return");
				}
			});
		} else {
			rpcService.addReturn(returnDTO, new AsyncCallback<ReturnDTO>() {

				public void onFailure(Throwable caught) {
					Window.alert("Error adding return");
				}

				public void onSuccess(ReturnDTO result) {
					eventBus.fireEvent(new ReturnUpdatedEvent(result));
				}
				
			});
		}
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
