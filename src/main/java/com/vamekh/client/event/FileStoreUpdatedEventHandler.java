package com.vamekh.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface FileStoreUpdatedEventHandler extends EventHandler{
	void onFileStoreUpdated(FileStoreUpdatedEvent even);
}
