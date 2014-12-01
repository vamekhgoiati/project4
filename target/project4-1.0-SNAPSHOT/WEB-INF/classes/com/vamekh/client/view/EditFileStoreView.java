package com.vamekh.client.view;

import gwtupload.client.MultiUploader;
import gwtupload.client.IFileInput.FileInputType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.vamekh.client.presenter.EditFileStorePresenter.Display;

public class EditFileStoreView extends Composite implements Display{
	
	private FramedPanel panel;
	private final FormPanel form;
	private final FileUploadField uploadField;
	private TextButton uploadButton;
	private TextButton cancelButton;
	private ButtonBar buttonPanel;
	private VerticalLayoutContainer fieldContainer;
	
	public EditFileStoreView(){
		
		panel = new FramedPanel();
		panel.setHeadingText("File Upload");
		panel.setWidth(400);
		panel.setLayoutData(new MarginData(20));

		initWidget(panel);
		
		fieldContainer = new VerticalLayoutContainer();
		buttonPanel = new ButtonBar();
		
		form = new FormPanel();
		form.setAction(GWT.getModuleBaseURL() + "/fileUpload");
		form.setMethod(Method.POST);
		form.setEncoding(Encoding.MULTIPART);
		
		form.setWidget(fieldContainer);
		
		uploadField = new FileUploadField();
		uploadField.setName("uploadFile");
		uploadField.setWidth(240);
		uploadField.getElement().setPropertyString("accept", ".xml");
		
		fieldContainer.add(uploadField);
		
		uploadButton = new TextButton("Upload");
		cancelButton = new TextButton("Cancel");
		
		buttonPanel.add(uploadButton);
		buttonPanel.add(cancelButton);
		
		fieldContainer.add(buttonPanel);
		
		panel.add(form);
		
	}

	public HasSelectHandlers getUploadButton() {
		return uploadButton;
	}

	public HasSelectHandlers getCancelButton() {
		return cancelButton;
	}

	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFileName(String fileName) {
		// TODO Auto-generated method stub
		
	}

	public byte[] getFileContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFileContent(byte[] fileContent) {
		// TODO Auto-generated method stub
		
	}

	public FormPanel getForm() {
		return form;
	}

	public FileUploadField getFileUploadField() {
		return uploadField;
	}

	public Widget asWidget(){
		return this;
	}
	
}
