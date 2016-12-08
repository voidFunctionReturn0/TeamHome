package com.mycompany.myapp.controller;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;


@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	
	@RequestMapping("/")
	public String home() {
		logger.info("home()");
		return "index";
	}
	
	@RequestMapping("/download/image")
	public void imageDownload(String imagename, HttpServletRequest request, HttpServletResponse response) {

		ServletContext application = request.getServletContext();
		String realPath = application.getRealPath("/WEB-INF/upload/" + imagename);
	
        File downloadFile = new File(realPath);

        FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(downloadFile);
			  // get MIME type of the file
	        ServletContext context = request.getSession().getServletContext();
	        String mimeType = context.getMimeType(realPath);

	        if (mimeType == null) {
	            // set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	        // check the mime type
	       
	        // set content attributes for the response
	        response.setContentType(mimeType);
	        response.setContentLength((int) downloadFile.length());


	        // set headers for the response
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                downloadFile.getName());

	        response.setHeader(headerKey, headerValue);
	        // get output stream of the response
	        OutputStream outStream = response.getOutputStream();
	        byte[] buffer = new byte[1024];
	        int bytesRead = -1;
	        // write bytes read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	        inputStream.close();
	        outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}

	}
}
