package com.vamekh.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.vamekh.shared.InstitutionDTO;

public class EditReturnCancelledEvent extends GwtEvent<EditReturnCancelledEventHandler>{
	
	public static Type<EditReturnCancelledEventHandler> TYPE = new Type<EditReturnCancelledEventHandler>();
	private InstitutionDTO currInst;
	
	public EditReturnCancelledEvent(InstitutionDTO currInst){
		this.currInst = currInst;
	}
	
	public int getInstitutionId(){
		return currInst.getId();
	}
	
	@Override
	public Type<EditReturnCancelledEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditReturnCancelledEventHandler handler) {
		handler.onEditReturnCancelled(this);
	}
	
	

}
