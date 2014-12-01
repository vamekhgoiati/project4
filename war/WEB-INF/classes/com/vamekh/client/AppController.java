package com.vamekh.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vamekh.client.event.AddFileStoreEvent;
import com.vamekh.client.event.AddFileStoreEventHandler;
import com.vamekh.client.event.AddInstitutionEvent;
import com.vamekh.client.event.AddInstitutionEventHandler;
import com.vamekh.client.event.AddInstitutionTypeEvent;
import com.vamekh.client.event.AddInstitutionTypeEventHandler;
import com.vamekh.client.event.AddReturnEvent;
import com.vamekh.client.event.AddReturnEventHandler;
import com.vamekh.client.event.AddTemplateEvent;
import com.vamekh.client.event.AddTemplateEventHandler;
import com.vamekh.client.event.EditFileStoreCancelledEvent;
import com.vamekh.client.event.EditFileStoreCancelledEventHandler;
import com.vamekh.client.event.EditFileStoreEvent;
import com.vamekh.client.event.EditFileStoreEventHandler;
import com.vamekh.client.event.EditInstitutionCancelledEvent;
import com.vamekh.client.event.EditInstitutionCancelledEventHandler;
import com.vamekh.client.event.EditInstitutionEvent;
import com.vamekh.client.event.EditInstitutionEventHandler;
import com.vamekh.client.event.EditInstitutionTypeCancelledEvent;
import com.vamekh.client.event.EditInstitutionTypeCancelledEventHandler;
import com.vamekh.client.event.EditInstitutionTypeEvent;
import com.vamekh.client.event.EditInstitutionTypeEventHandler;
import com.vamekh.client.event.EditReturnCancelledEvent;
import com.vamekh.client.event.EditReturnCancelledEventHandler;
import com.vamekh.client.event.EditReturnEvent;
import com.vamekh.client.event.EditReturnEventHandler;
import com.vamekh.client.event.EditTemplateCancelledEvent;
import com.vamekh.client.event.EditTemplateCancelledEventHandler;
import com.vamekh.client.event.EditTemplateEvent;
import com.vamekh.client.event.EditTemplateEventHandler;
import com.vamekh.client.event.FileStoreUpdatedEvent;
import com.vamekh.client.event.FileStoreUpdatedEventHandler;
import com.vamekh.client.event.InstitutionTypeUpdatedEvent;
import com.vamekh.client.event.InstitutionTypeUpdatedEventHandler;
import com.vamekh.client.event.InstitutionUpdatedEvent;
import com.vamekh.client.event.InstitutionUpdatedEventHandler;
import com.vamekh.client.event.ReturnUpdatedEvent;
import com.vamekh.client.event.ReturnUpdatedEventHandler;
import com.vamekh.client.event.TemplateUpdatedEvent;
import com.vamekh.client.event.TemplateUpdatedEventHandler;
import com.vamekh.client.presenter.EditFileStorePresenter;
import com.vamekh.client.presenter.EditInstitutionPresenter;
import com.vamekh.client.presenter.EditInstitutionTypePresenter;
import com.vamekh.client.presenter.EditReturnPresenter;
import com.vamekh.client.presenter.EditTemplatePresenter;
import com.vamekh.client.presenter.MainPresenter;
import com.vamekh.client.presenter.Presenter;
import com.vamekh.client.view.EditFileStoreView;
import com.vamekh.client.view.EditInstitutionTypeView;
import com.vamekh.client.view.EditInstitutionView;
import com.vamekh.client.view.EditReturnView;
import com.vamekh.client.view.EditTemplateView;
import com.vamekh.client.view.MainView;

public class AppController implements Presenter, ValueChangeHandler<String>{

	private InstitutionTypeServiceAsync typeRpcService;
	private InstitutionServiceAsync instRpcService;
	private TemplateServiceAsync templateRpcService;
	private ReturnServiceAsync returnRpcService;
	private FileStoreServiceAsync fsRpcService;
	private HandlerManager eventBus;
	private HasWidgets container;
	
	public AppController(InstitutionTypeServiceAsync typeRpcService, InstitutionServiceAsync instRpcService, TemplateServiceAsync templateRpcService, ReturnServiceAsync returnRpcService, FileStoreServiceAsync fsRpcService, 
			HandlerManager eventBus) {
		
		this.typeRpcService = typeRpcService;
		this.instRpcService = instRpcService;
		this.templateRpcService = templateRpcService;
		this.returnRpcService = returnRpcService;
		this.fsRpcService = fsRpcService;
		this.eventBus = eventBus;
		
		bind();
	}
	
	private void bind() {
		
		History.addValueChangeHandler(this);
		
		eventBus.addHandler(AddInstitutionTypeEvent.TYPE,
				new AddInstitutionTypeEventHandler() {
					
					public void onAddInstitutionType(AddInstitutionTypeEvent event) {
						doAddNewInsitutionType();
					}
				});
				
		eventBus.addHandler(EditInstitutionTypeEvent.TYPE,
				new EditInstitutionTypeEventHandler() {
					
					public void onEditInstitutionType(EditInstitutionTypeEvent event) {
						doEditInstitutionType(event.getId());
					}
				});
		
		eventBus.addHandler(EditInstitutionTypeCancelledEvent.TYPE,
				new EditInstitutionTypeCancelledEventHandler() {
					
					public void onEditInstitutionTypeCancelled(
							EditInstitutionTypeCancelledEvent event) {
						doEditInstitutionTypeCancelled();
					}
				});
		
		eventBus.addHandler(InstitutionTypeUpdatedEvent.TYPE,
				new InstitutionTypeUpdatedEventHandler() {
					
					public void onInstitutionTypeUpdated(InstitutionTypeUpdatedEvent event) {
						doInstitutionTypeUpdated();
					}
				});
		
		eventBus.addHandler(AddInstitutionEvent.TYPE,
				new AddInstitutionEventHandler() {
					
					public void onAddInstitution(AddInstitutionEvent event) {
						doAddNewInsitution();
					}
				});
				
		eventBus.addHandler(EditInstitutionEvent.TYPE,
				new EditInstitutionEventHandler() {
					
					public void onEditInstitution(EditInstitutionEvent event) {
						doEditInstitution(event.getId());
					}
				});
		
		eventBus.addHandler(EditInstitutionCancelledEvent.TYPE,
				new EditInstitutionCancelledEventHandler() {
					public void onEditInstitutionCancelled(
							EditInstitutionCancelledEvent event) {
						doEditInstitutionCancelled();
					}
				});
		
		eventBus.addHandler(InstitutionUpdatedEvent.TYPE,
				new InstitutionUpdatedEventHandler() {
					public void onInstitutionUpdated(
							InstitutionUpdatedEvent event) {
						doInstitutionUpdated();
					}
				});
		
		eventBus.addHandler(AddTemplateEvent.TYPE,
				new AddTemplateEventHandler() {

					public void onAddTemplate(AddTemplateEvent event) {
						doAddNewTemplate();
					}
				});

		eventBus.addHandler(EditTemplateEvent.TYPE,
				new EditTemplateEventHandler() {

					public void onEditTemplate(EditTemplateEvent event) {
						doEditTemplate(event.getId());
					}
				});

		eventBus.addHandler(EditTemplateCancelledEvent.TYPE,
				new EditTemplateCancelledEventHandler() {

					public void onEditTemplateCancelled(
							EditTemplateCancelledEvent event) {
						doEditTemplateCancelled();
					}
				});

		eventBus.addHandler(TemplateUpdatedEvent.TYPE,
				new TemplateUpdatedEventHandler() {

					public void onTemplateUpdated(TemplateUpdatedEvent event) {
						doTemplateUpdated();
					}
				});
		
		eventBus.addHandler(AddReturnEvent.TYPE,
				new AddReturnEventHandler() {

					public void onAddReturn(AddReturnEvent event) {
						doAddNewReturn(event.getId());
					}
				});

		eventBus.addHandler(EditReturnEvent.TYPE,
				new EditReturnEventHandler() {

					public void onEditReturn(EditReturnEvent event) {
						doEditReturn(event.getId());
					}
				});

		eventBus.addHandler(EditReturnCancelledEvent.TYPE,
				new EditReturnCancelledEventHandler() {

					public void onEditReturnCancelled(
							EditReturnCancelledEvent event) {
						doEditReturnCancelled(event.getInstitutionId());
					}
				});

		eventBus.addHandler(ReturnUpdatedEvent.TYPE,
				new ReturnUpdatedEventHandler() {

					public void onReturnUpdated(ReturnUpdatedEvent event) {
						doReturnUpdated(event.getUpdatedReturn().getInstitution().getId());
					}
				});
		
		eventBus.addHandler(AddFileStoreEvent.TYPE,
				new AddFileStoreEventHandler() {

					public void onAddFileStore(AddFileStoreEvent event) {
						doAddNewFileStore(event);
					}
				});

		eventBus.addHandler(EditFileStoreEvent.TYPE,
				new EditFileStoreEventHandler() {

					public void onEditFileStore(EditFileStoreEvent event) {
						doEditFileStore(event.getId());
					}
				});

		eventBus.addHandler(EditFileStoreCancelledEvent.TYPE,
				new EditFileStoreCancelledEventHandler() {

					public void onEditFileStoreCancelled(EditFileStoreCancelledEvent event) {
						doEditFileStoreCancelled();
					}
				});

		eventBus.addHandler(FileStoreUpdatedEvent.TYPE,
				new FileStoreUpdatedEventHandler() {

					public void onFileStoreUpdated(FileStoreUpdatedEvent event) {
						doFileStoreUpdated();
					}
				});
				
	}

	private void doFileStoreUpdated() {
		History.newItem("listFileStore");
	}

	private void doEditFileStoreCancelled() {
		History.newItem("listFileStore");
	}

	private void doEditFileStore(int id) {
		
	}

	private void doAddNewFileStore(AddFileStoreEvent event) {
		History.newItem("addFileStore");
	}

	private void doReturnUpdated(int id) {
		History.newItem("editInstitution", false);
		Presenter presenter = new EditInstitutionPresenter(instRpcService, returnRpcService, eventBus,
				new EditInstitutionView(), id);
		presenter.go(container);
	}

	private void doEditReturnCancelled(int id) {
		History.newItem("editInstitution", false);
		Presenter presenter = new EditInstitutionPresenter(instRpcService, returnRpcService, eventBus,
				new EditInstitutionView(), id);
		presenter.go(container);
	}

	private void doEditReturn(int id) {
		History.newItem("editReturn", false);
		Presenter presenter = new EditReturnPresenter(returnRpcService, eventBus,
				new EditReturnView(), 0, id);
		presenter.go(container);
	}

	private void doAddNewReturn(int id) {
		History.newItem("addReturn", false);
		Presenter presenter = new EditReturnPresenter(returnRpcService, instRpcService, eventBus, new EditReturnView(), id);
		presenter.go(container);
	}

	private void doInstitutionTypeUpdated() {
		History.newItem("listType");
	}

	private void doEditInstitutionTypeCancelled() {
		History.newItem("listType");
	}

	private void doEditInstitutionType(int id) {
		History.newItem("editType", false);
		Presenter presenter = new EditInstitutionTypePresenter(typeRpcService, eventBus,
				new EditInstitutionTypeView(), id);
		presenter.go(container);
	}

	private void doAddNewInsitutionType() {
		History.newItem("addInstitutionType");
	}
	
	private void doInstitutionUpdated() {
		History.newItem("listInstitution");
	}

	private void doEditInstitutionCancelled() {
		History.newItem("listInstitution");
	}

	private void doEditInstitution(int id) {
		History.newItem("editInstitution", false);
		Presenter presenter = new EditInstitutionPresenter(instRpcService, returnRpcService, eventBus,
				new EditInstitutionView(), id);
		presenter.go(container);
	}

	private void doAddNewInsitution() {
		History.newItem("addInstitution");
	}
	
	private void doTemplateUpdated() {
		History.newItem("listTemplate");
	}

	private void doEditTemplateCancelled() {
		History.newItem("listTemplate");
	}

	private void doEditTemplate(int id) {
		History.newItem("editTemplate", false);
		Presenter presenter = new EditTemplatePresenter(templateRpcService, eventBus,
				new EditTemplateView(), id);
		presenter.go(container);
	}

	private void doAddNewTemplate() {
		History.newItem("addTemplate");
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
		
		String token = event.getValue();
		
		if (token != null) {
			Presenter presenter = null;

			if (token.equals("list")) {
				presenter = new MainPresenter(typeRpcService, instRpcService, templateRpcService, fsRpcService, eventBus,
						new MainView());
			}
			
			if (token.equals("listType")) {
				presenter = new MainPresenter(typeRpcService, instRpcService, templateRpcService, fsRpcService, eventBus,
						new MainView());
			}
			
			if (token.equals("listInstitution")) {
				presenter = new MainPresenter(typeRpcService, instRpcService, templateRpcService, fsRpcService, eventBus,
						new MainView(1));
			}
			
			if (token.equals("listTemplate")) {
				presenter = new MainPresenter(typeRpcService, instRpcService, templateRpcService, fsRpcService, eventBus,
						new MainView(2));
			}
			
			if (token.equals("listFileStore")) {
				presenter = new MainPresenter(typeRpcService, instRpcService, templateRpcService, fsRpcService, eventBus,
						new MainView(3));
			}
			
			if (token.equals("addInstitutionType")) {
				presenter = new EditInstitutionTypePresenter(typeRpcService, eventBus, new EditInstitutionTypeView());	
			}
			
			if (token.equals("addInstitution")) {
				presenter = new EditInstitutionPresenter(instRpcService, returnRpcService, eventBus, new EditInstitutionView());	
			}
			
			if (token.equals("editType")) {
				presenter = new EditInstitutionTypePresenter(typeRpcService, eventBus, new EditInstitutionTypeView());	
			}
			
			if (token.equals("editInstitution")) {
				presenter = new EditInstitutionPresenter(instRpcService, returnRpcService, eventBus, new EditInstitutionView());	
			}
			
			if (token.equals("addTemplate")) {
				presenter = new EditTemplatePresenter(templateRpcService, eventBus, new EditTemplateView());
			}
			
			if (token.equals("editTemplate")) {
				presenter = new EditTemplatePresenter(templateRpcService, eventBus, new EditTemplateView());
			}
			
			if (token.equals("addFileStore")) {
				presenter = new EditFileStorePresenter(fsRpcService, instRpcService, templateRpcService, eventBus, new EditFileStoreView());
			}

			if (presenter != null) {
				presenter.go(container);
			}
		}
		
	}

	public void go(HasWidgets container) {
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem("list");
		} else {
			History.fireCurrentHistoryState();
		}
	}

}
