package com.vamekh.client;

import com.vamekh.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class project4 implements EntryPoint {
 
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
	  
	InstitutionTypeServiceAsync typeRpcService = GWT.create(InstitutionTypeService.class);
	InstitutionServiceAsync instRpcService = GWT.create(InstitutionService.class);
	TemplateServiceAsync templateRpcService = GWT.create(TemplateService.class);
	ReturnServiceAsync returnRpcService = GWT.create(ReturnService.class);
	FileStoreServiceAsync fsRpcService = GWT.create(FileStoreService.class);
	HandlerManager eventBus = new HandlerManager(null);
	AppController appViewer = new AppController(typeRpcService, instRpcService, templateRpcService, returnRpcService, fsRpcService, eventBus);
	appViewer.go(RootPanel.get());
	  
  }
}
