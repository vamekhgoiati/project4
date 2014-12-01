package com.vamekh.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.vamekh.shared.FileStoreDTO;

public class FileStoreUpdatedEvent extends GwtEvent<FileStoreUpdatedEventHandler>{
	public static Type<FileStoreUpdatedEventHandler> TYPE = new Type<FileStoreUpdatedEventHandler>();

	
	@Override
	public Type<FileStoreUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FileStoreUpdatedEventHandler handler) {
		handler.onFileStoreUpdated(this);
	}
	
	
	
	
}
