package com.vamekh.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddFileStoreEvent extends GwtEvent<AddFileStoreEventHandler>{

	public static Type<AddFileStoreEventHandler> TYPE = new Type<AddFileStoreEventHandler>();
	
	@Override
	public Type<AddFileStoreEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddFileStoreEventHandler handler) {
		handler.onAddFileStore(this);
	}
	

}
