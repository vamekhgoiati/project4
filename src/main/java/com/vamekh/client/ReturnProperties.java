package com.vamekh.client;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.vamekh.shared.ReturnDTO;

public interface ReturnProperties extends PropertyAccess<ReturnDTO>{
	
	ValueProvider<ReturnDTO, String> institutionCode();
	
	ValueProvider<ReturnDTO, String> templateCode();
	
	ValueProvider<ReturnDTO, String> schedule();
	
}
