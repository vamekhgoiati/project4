package com.vamekh.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;

import javax.annotation.*;

import com.vamekh.shared.FileStore;
import com.vamekh.shared.IllegalFileNameException;
import com.vamekh.shared.InstitutionNotFoundException;
import com.vamekh.shared.TemplateNotFoundException;

public class FileUploadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private HashMap<String, byte[]> files;
	
	@EJB
	private FileStoreEJBService fileStoreService;
	
	@EJB
	private InstitutionEJBService institutionService;
	
	@EJB
	private TemplateEJBService templateService;
	
	@Resource(name="Java:UserTransaction")UserTransaction ut;
	
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = "";
		byte[] fileContent = null;
		
		FileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				
				if (!item.isFormField()) {
					fileName = item.getName();
					if (fileName != null) {
						fileName = FilenameUtils.getName(fileName);
					}
					String contentType = item.getContentType();
					if (!contentType.equals("text/xml")) {
						throw new IllegalFileNameException();
					}

					InputStream in = item.getInputStream();
					fileContent = new byte[in.available()];
					in.read(fileContent);
					fileName = item.getName();
					FileStore newFile = createFileStore(fileName, fileContent);
					
					try {
						ut.begin();
						fileStoreService.addFile(newFile);
						ut.commit();
					} catch (Exception e) {
						ut.rollback();
						throw e;
					}
					response.setStatus(HttpServletResponse.SC_CREATED);
					response.getWriter().print("The file was created successfully.");
				}
			}
			
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).info("File upload failed, with message " + e.getMessage() + "\n cause: " + e.getCause());
		}
	}


	private FileStore createFileStore(String fileName, byte[] fileContent) {
		
		FileStore newFile = null;
		String[] fileNameParts = fileName.split("\\.");
		
		newFile = new FileStore();
		newFile.setFileName(fileName);
		newFile.setFileContent(fileContent);
		newFile.setInstitution(institutionService.getInstitutionByCode(fileNameParts[0]));
		newFile.setTemplate(templateService.getTemplateByCode(fileNameParts[1]));
		newFile.setScheduleTime(fileNameParts[2]);
		newFile.setUploadDate(new Date());
		
		return newFile;
	}
	
}
