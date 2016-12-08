package com.mycompany.myapp.controller.teammain;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.*;
import org.slf4j.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.mycompany.myapp.dto.*;
import com.mycompany.myapp.service.*;
import com.sun.media.jfxmedia.logging.*;

@Controller
public class DataController {
	
	private static final Logger logger = LoggerFactory.getLogger(DataController.class);
	
	@Autowired
	private DataService dataService;
	
	@RequestMapping("/team/data/list")
	public void DataList(int tid, HttpServletResponse response) {
	
		List<News> dataList = dataService.getDataList(tid);
		JSONObject root = new JSONObject();
		if((dataList==null)|(dataList.size()==0)) {
			root.put("getDataListResult", "fail");
		}else {
			root.put("getDataListResult", "success");
			JSONArray list = new JSONArray();
			for(News news:dataList) {
				JSONObject dataJson = new JSONObject();
				dataJson.put("nid", news.getNid());
				dataJson.put("mid", news.getMid());
				dataJson.put("mprofile", news.getMprofile() != null?news.getMprofile():"");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				dataJson.put("ndate", sdf.format(news.getNdate()));
				dataJson.put("ncontent", news.getNcontent());
				dataJson.put("nphotoname", (news.getNphotoname()!=null)?news.getNphotoname():"");
				dataJson.put("ndataname", news.getNdataname());
				dataJson.put("nisnotice", news.getNisnotice());
				dataJson.put("tid", news.getTid());
				list.put(dataJson);
			}
			root.put("dataList", list);			
		}
		
		// 출력 스트림 얻기
		OutputStream os;
		try {
			os = response.getOutputStream();
			String str = root.toString();
			os.write(str.getBytes());
			
			os.flush();

			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
	}
	
	
	@RequestMapping("/team/data/download")
	public void dataDownload(String dataName, HttpServletRequest request, HttpServletResponse response) {
		
		ServletContext application = request.getServletContext();
		String realPath = application.getRealPath("/WEB-INF/upload/data/" + dataName);
		
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
	        outStream.flush();
	        inputStream.close();
	        outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}

	}
	
	
}
