package com.vamekh.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddReturnEvent extends GwtEvent<AddReturnEventHandler>{
	
	public static Type<AddReturnEventHandler> TYPE = new Type<AddReturnEventHandler>();
	private int id;
	
	public AddReturnEvent(int id){
		this.id = id;
	}
	
	public int getId(){ return id; }
	
	@Override
	public Type<AddReturnEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddReturnEventHandler handler) {
		handler.onAddReturn(this);
	}
	
	

}
