package com.vamekh.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.vamekh.shared.FileStoreDTO;

public interface FileStoreServiceAsync {

	void addFile(FileStoreDTO fileStore, AsyncCallback<FileStoreDTO> callback);

	void deleteFile(int id, AsyncCallback<Boolean> callback);

	void deleteFiles(List<FileStoreDTO> fileStores,
			AsyncCallback<Boolean> callback);

	void getFiles(AsyncCallback<ArrayList<FileStoreDTO>> callback);

	void getFile(int id, AsyncCallback<FileStoreDTO> callback);

	void updateFile(FileStoreDTO fileStore, AsyncCallback<FileStoreDTO> callback);

	void isFileUnique(String fileName, AsyncCallback<Boolean> callback);

	void getFiles(PagingLoadConfig config,
			AsyncCallback<PagingLoadResult<FileStoreDTO>> callback);

	void checkFileStoreByName(String fileName, AsyncCallback<Boolean> callback);

}
