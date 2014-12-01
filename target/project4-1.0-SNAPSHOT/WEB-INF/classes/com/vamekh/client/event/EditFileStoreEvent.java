package com.vamekh.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditFileStoreEvent extends GwtEvent<EditFileStoreEventHandler>{
	
	public static Type<EditFileStoreEventHandler> TYPE = new Type<EditFileStoreEventHandler>();
	private final int id;
	
	public EditFileStoreEvent(int id){
		this.id = id;
	}
	
	public int getId(){ return id; }
	
	@Override
	public Type<EditFileStoreEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditFileStoreEventHandler handler) {
		handler.onEditFileStore(this);
	}
	
	

}
