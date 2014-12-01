package com.vamekh.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditFileStoreCancelledEvent extends GwtEvent<EditFileStoreCancelledEventHandler>{
	
	public static Type<EditFileStoreCancelledEventHandler> TYPE = new Type<EditFileStoreCancelledEventHandler>();

	@Override
	public Type<EditFileStoreCancelledEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditFileStoreCancelledEventHandler handler) {
		handler.onEditFileStoreCancelled(this);
	}
	
	

}
