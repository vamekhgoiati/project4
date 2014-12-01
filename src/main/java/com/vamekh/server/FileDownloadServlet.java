package com.vamekh.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.vamekh.shared.FileStore;

/**
 * Servlet implementation class FileDownloadServlet
 */
public class FileDownloadServlet extends HttpServlet {
	
	@EJB
	FileStoreEJBService fileStoreService;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDownloadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String idStr = (String)request.getParameter("id");
		int id = Integer.parseInt(idStr);
		Logger.getLogger(this.getClass()).info("file id " + id);
		FileStore file = fileStoreService.getFile(id);
		
		response.setContentType("text/xml");
		response.setHeader("Content-disposition", "attachment; filename=" + file.getFileName());
		
		OutputStream outputStream = response.getOutputStream();
		
		outputStream.write(file.getFileContent());
		outputStream.flush();
		outputStream.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
