package com.vamekh.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.vamekh.client.InstitutionTypeService;
import com.vamekh.client.InstitutionTypeServiceAsync;
import com.vamekh.client.ReturnProperties;
import com.vamekh.client.ReturnService;
import com.vamekh.client.ReturnServiceAsync;
import com.vamekh.client.TypeProperties;
import com.vamekh.client.presenter.EditInstitutionPresenter.Display;
import com.vamekh.client.presenter.EditInstitutionPresenter;
import com.vamekh.shared.FileStoreDTO;
import com.vamekh.shared.InstitutionDTO;
import com.vamekh.shared.InstitutionTypeDTO;
import com.vamekh.shared.Return;
import com.vamekh.shared.ReturnDTO;
import com.vamekh.shared.TemplateDTO;

public class EditInstitutionView extends Composite implements
		EditInstitutionPresenter.Display {

	private FramedPanel panel;
	private TextButton saveButton;
	private TextButton cancelButton;
	private TextButton addReturnButton;
	private TextButton deleteReturnButton;
	private TextField code;
	private TextField name;
	private TextField address;
	private TextField phone;
	private TextField email;
	private TextField fax;
	private DateField regDate;
	private InstitutionTypeDTO type;
	private InstitutionDTO currInst;
	private ComboBox<InstitutionTypeDTO> types;
	private VerticalLayoutContainer fieldContainer;
	private VerticalLayoutContainer gridContainer;
	private ButtonBar returnButtonPanel;
	private ButtonBar buttonPanel;
	private TypeProperties typeProps;
	private ReturnProperties returnProps;
	private CheckBoxSelectionModel<ReturnDTO> sm;
	private ListStore<ReturnDTO> ls;
	private ColumnModel<ReturnDTO> cm;
	private PagingLoader<PagingLoadConfig, PagingLoadResult<ReturnDTO>> loader;
	private Grid<ReturnDTO> returnGrid;
	private ReturnServiceAsync returnRpcService;

	public EditInstitutionView() {

		panel = new FramedPanel();
		panel.setHeadingText("Financial Institution");
		panel.setWidth(400);
		panel.setLayoutData(new MarginData(20));

		initWidget(panel);

		typeProps = GWT.create(TypeProperties.class);
		returnProps = GWT.create(ReturnProperties.class);

		initFields();

	}

	private void initFields() {
		InstitutionTypeServiceAsync typeRpcService = GWT
				.create(InstitutionTypeService.class);
		
		returnRpcService = GWT.create(ReturnService.class);

		fieldContainer = new VerticalLayoutContainer();
		buttonPanel = new ButtonBar();
		gridContainer = new VerticalLayoutContainer();
		returnButtonPanel = new ButtonBar();

		code = new TextField();
		code.addValidator(new RegExValidator("[A-Za-z0-9]+",
				"Code contains invalid characters"));
		code.addValidator(new MinLengthValidator(1));
		code.addValidator(new MaxLengthValidator(20));
		code.setAutoValidate(true);

		name = new TextField();
		name.addValidator(new RegExValidator("[A-Za-z0-9 \\s]+",
				"Name contains invalid characters"));
		name.addValidator(new MinLengthValidator(1));
		name.addValidator(new MaxLengthValidator(255));
		name.setAutoValidate(true);

		address = new TextField();
		address.addValidator(new MaxLengthValidator(255));

		phone = new TextField();
		phone.addValidator(new RegExValidator("[0-9 \\s]+",
				"Phone contains invalid characters"));
		phone.addValidator(new MaxLengthValidator(255));
		phone.setAutoValidate(true);

		email = new TextField();
		email.addValidator(new RegExValidator(
				"[A-Za-z0-9._%+-][A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3}",
				"Email contains invalid characters"));
		email.addValidator(new MaxLengthValidator(255));

		fax = new TextField();
		fax.addValidator(new RegExValidator("[0-9 \\s]+",
				"Fax contains invalid characters"));
		fax.addValidator(new MaxLengthValidator(255));

		regDate = new DateField();
		regDate.setAutoValidate(true);

		final ListStore<InstitutionTypeDTO> typeStore = new ListStore<InstitutionTypeDTO>(
				new ModelKeyProvider<InstitutionTypeDTO>() {

					public String getKey(InstitutionTypeDTO item) {
						return "" + item.getId();
					}

				});

		typeRpcService
				.getInstitutionTypes(new AsyncCallback<ArrayList<InstitutionTypeDTO>>() {

					public void onSuccess(ArrayList<InstitutionTypeDTO> result) {
						typeStore.addAll(result);
						type = typeStore.get(0);
						types.setValue(type);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Error loading types");
					}
				});

		types = new ComboBox<InstitutionTypeDTO>(typeStore,
				typeProps.codeLabel());
		types.setEditable(false);
		types.setForceSelection(true);
		types.setTypeAhead(true);
		types.setTriggerAction(TriggerAction.ALL);

		types.addSelectionHandler(new SelectionHandler<InstitutionTypeDTO>() {

			public void onSelection(SelectionEvent<InstitutionTypeDTO> event) {
				type = event.getSelectedItem();
			}
		});

		ls = new ListStore<ReturnDTO>(new ModelKeyProvider<ReturnDTO>() {

			public String getKey(ReturnDTO item) {
				return "" + item.getId();
			}
			
		});
		
		refreshReturns();
		
		IdentityValueProvider<ReturnDTO> identity = new IdentityValueProvider<ReturnDTO>();
		sm = new CheckBoxSelectionModel<ReturnDTO>(identity){
			
			@Override
			protected void onRefresh(RefreshEvent event) {
				// this code selects all rows when paging if the header checkbox
				// is selected
				if (isSelectAllChecked()) {
					selectAll();
				}
				super.onRefresh(event);
			}
			
		};
		
		ColumnConfig<ReturnDTO, String> templateColumn = new ColumnConfig<ReturnDTO, String>(returnProps.templateCode(), 150, "Template Code");
		ColumnConfig<ReturnDTO, String> scheduleColumn = new ColumnConfig<ReturnDTO, String>(returnProps.schedule(), 150, "Schedule");
		
		List<ColumnConfig<ReturnDTO, ?>> l = new ArrayList<ColumnConfig<ReturnDTO,?>>();
		l.add(sm.getColumn());
		l.add(templateColumn);
		l.add(scheduleColumn);
		
		cm = new ColumnModel<ReturnDTO>(l);
		
		returnGrid = new Grid<ReturnDTO>(ls, cm);
		returnGrid.setBorders(true);

		
		saveButton = new TextButton("Save");
		cancelButton = new TextButton("Cancel");
		addReturnButton = new TextButton("Add");
		deleteReturnButton = new TextButton("Delete");

		returnButtonPanel.add(addReturnButton);
		returnButtonPanel.add(deleteReturnButton);
		gridContainer.add(returnButtonPanel);
		gridContainer.add(returnGrid, new VerticalLayoutData(1, 1));
		gridContainer.setHeight("100px");
		gridContainer.setBorders(true);;
		
		
		fieldContainer.add(new FieldLabel(code, "Code"));
		fieldContainer.add(new FieldLabel(name, "Name"));
		fieldContainer.add(new FieldLabel(address, "Address"));
		fieldContainer.add(new FieldLabel(phone, "Phone"));
		fieldContainer.add(new FieldLabel(email, "Email"));
		fieldContainer.add(new FieldLabel(fax, "Fax"));
		fieldContainer.add(new FieldLabel(types, "Type"));
		fieldContainer.add(new FieldLabel(regDate, "Date"));
		fieldContainer.add(gridContainer);
		
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		fieldContainer.add(buttonPanel);
		

		panel.add(fieldContainer);
	}

	public HasSelectHandlers getSaveButton() {
		return saveButton;
	}

	public HasSelectHandlers getCancelButton() {
		return cancelButton;
	}

	public HasValue<String> getCode() {
		return code;
	}

	public HasValue<String> getName() {
		return name;
	}

	public HasValue<String> getAddress() {
		return address;
	}

	public HasValue<String> getPhone() {
		return phone;
	}

	public HasValue<String> getEmail() {
		return email;
	}

	public HasValue<String> getFax() {
		return fax;
	}

	public InstitutionTypeDTO getType() {
		return type;
	}
	
	public InstitutionDTO getInstitution() {
		return currInst;
	}
	
	public void setInstitution(InstitutionDTO inst) {
		this.currInst = inst;
		refreshReturns();
	}

	private void refreshReturns() {
		if (currInst != null) {
			
			RpcProxy<PagingLoadConfig, PagingLoadResult<ReturnDTO>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<ReturnDTO>>() {

				@Override
				public void load(PagingLoadConfig loadConfig,
						AsyncCallback<PagingLoadResult<ReturnDTO>> callback) {
					returnRpcService.getReturnsForFI(loadConfig, currInst, callback);

				}
			};
			
			PagingLoader<PagingLoadConfig, PagingLoadResult<ReturnDTO>> pageLoader = new PagingLoader<PagingLoadConfig, PagingLoadResult<ReturnDTO>>(proxy);
			
			loader = pageLoader;
			loader.setRemoteSort(true);
			loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, ReturnDTO, PagingLoadResult<ReturnDTO>>(ls));
			
			returnGrid.setLoader(loader);
			
			Timer t = new Timer() {

				@Override
				public void run() {
					loader.load();
				}
			};
			t.schedule(100);
			
		}
	}

	public void setType(InstitutionTypeDTO type) {
		this.type = type;
		types.setValue(type);
	}

	public HasValue<Date> getRegDate() {
		return regDate;
	}

	public Widget asWidget() {
		return this;
	}

	public HasSelectHandlers getAddReturnButton() {
		return addReturnButton;
	}

	public HasSelectHandlers getDeleteReturnsButton() {
		return deleteReturnButton;
	}

	public List<ReturnDTO> getSelectedReturnRows() {
		return returnGrid.getSelectionModel().getSelectedItems();
	}

	public void refreshReturnData() {
		returnGrid.getLoader().load();
	}

}
