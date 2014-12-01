package com.vamekh.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class FileStoreDeletedEvent extends GwtEvent<FileStoreDeletedEventHandler>{
	public static Type<FileStoreDeletedEventHandler> TYPE = new Type<FileStoreDeletedEventHandler>();

	@Override
	public Type<FileStoreDeletedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FileStoreDeletedEventHandler handler) {
		handler.onFileStoreDeleted(this);
	}
	
	
		
}
