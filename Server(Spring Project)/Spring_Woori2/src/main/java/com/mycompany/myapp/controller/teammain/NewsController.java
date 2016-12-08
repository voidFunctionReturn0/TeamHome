package com.mycompany.myapp.controller.teammain;

import java.io.*;
import java.nio.charset.Charset;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.mycompany.myapp.dto.*;
import com.mycompany.myapp.service.*;

@Controller
public class NewsController {
	Logger log = LoggerFactory.getLogger(NewsController.class);
	
	@Autowired
	private NewsService newsService;
	//소식 추가하기
	@RequestMapping("/team/news/addNews")
	public void addNews(String tid, String mid, String ndate, String ncontent, String nisnotice, MultipartFile nphotoname, MultipartFile nfilename, HttpServletRequest request , Model model){
				
		// ndate를 String -> Date 변환
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(ndate);
		} catch (ParseException e1) { e1.printStackTrace(); }
		
		
		// News객체(DTO) 생성
		News news = new News();
		news.setTid(Integer.parseInt(tid));
		log.debug(tid);
		log.debug(nisnotice);
		news.setMid(mid);
		news.setNdate(date);
		news.setNcontent(ncontent);
		news.setNisnotice(Integer.parseInt(nisnotice.trim())); // 파일,사진이 없는 경우 trim해줘야함
		
		if(nphotoname != null) {
			// 사진파일이름 = 저장시간_올린사람아이디.png
			String photoName = nphotoname.getOriginalFilename();
			news.setNphotoname(photoName);
			
			// 사진파일 저장
			ServletContext application = request.getServletContext();
			String savePath = application.getRealPath("/WEB-INF/upload/" + photoName);
			File file = new File(savePath);
			try {
				nphotoname.transferTo(file);
			}  catch (Exception e) { e.printStackTrace(); }
		} 
		
		
		if(nfilename != null) {
			// 파일이름 = 저장시간_파일명
			String filename = System.currentTimeMillis() + "_" + nfilename.getOriginalFilename();
			news.setNdataname(filename);

			// 파일 저장
			try {
				ServletContext application = request.getServletContext();
				String savePath = application.getRealPath("/WEB-INF/upload/" + filename);
				FileOutputStream fos = new FileOutputStream(savePath);
				fos.write(nfilename.getBytes());
				fos.flush();
				fos.close();
				
			} catch(IOException e) { e.printStackTrace(); }
			
		}
		
		newsService.addNews(news);
		
		/*	
		
		String originalFileName = nphotoname.getOriginalFilename();
		long size = nphotoname.getSize();
		String contentType = nphotoname.getContentType();

		
		
		
		//dao에 filename 추가 코드
		
		if(news.getNphotoname() != null){
			ServletContext application = request.getServletContext();
			String realPath = application.getRealPath("/WEB-INF/upload/" + originalFileName);
			File file = new File(realPath);
			try {
				nphotoname.transferTo(file);
			}  catch (Exception e) {}		
		}
		newsService.addNews(news);
		//db의 not read테이블에 해당 nid 추가하기  
		
		*/
	
	}
	//소식 리스트 얻기
	@RequestMapping("/team/news/list")
	public void newsList(int tid, HttpServletResponse response) {
		List<News> list = newsService.getNewsListByTid(tid);
		String mimeType = "application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if((list==null)||(list.size()==0)) {
			root.put("newsResult", "fail");
		}else {
			root.put("newsResult", "success");
			JSONArray newsArray = new JSONArray();
			for(News news:list) {
				JSONObject newsJson = new JSONObject();
				newsJson.put("nid", news.getNid());
				newsJson.put("mid", news.getMid());
				newsJson.put("mprofile", news.getMprofile() != null?news.getMprofile():"");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				newsJson.put("ndate", sdf.format(news.getNdate()));
				newsJson.put("ncontent", news.getNcontent());
				newsJson.put("nphotoname", news.getNphotoname() != null?news.getNphotoname():"");
				newsJson.put("ndataname", news.getNdataname() != null?news.getNdataname():"");
				newsJson.put("nisnotice", news.getNisnotice());
				newsJson.put("tid", news.getTid());
				newsArray.put(newsJson);
			}
			root.put("list", newsArray);
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
	
	@RequestMapping("/team/news/getNews")
	public void getNews(int nid, HttpServletResponse response) {
		News news = newsService.getNews(nid);
		
		String mimeType = "application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if(news==null) {
			root.put("newsResult", "fail");
		}else {
			root.put("newsResult", "success");
			JSONObject newsJson = new JSONObject();
			newsJson.put("nid", news.getNid());
			newsJson.put("mid", news.getMid());
			newsJson.put("mprofile", news.getMprofile() != null?news.getMprofile():"");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			newsJson.put("ndate", sdf.format(news.getNdate()));
			newsJson.put("ncontent", news.getNcontent());
			newsJson.put("nphotoname", news.getNphotoname() != null?news.getNphotoname():"");
			newsJson.put("ndataname", news.getNdataname() != null?news.getNdataname():"");
			newsJson.put("nisnotice", news.getNisnotice());
			newsJson.put("tid", news.getTid());
			root.put("list", newsJson);
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
	

	
	@RequestMapping("/team/news/deleteNews")
	public void deleteNews(int nid, HttpServletResponse response) {
		
		int deleteNewsResult = newsService.removeNews(nid);
		
		String mimeType="application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if(deleteNewsResult==0) {
			root.put("deleteNewsResult", "fail");
		}else {
			root.put("deleteNewsResult", "success");
		}
		
		// 출력 스트림 얻기
		OutputStream os;
		try {
			os = response.getOutputStream();
			String str = root.toString();
			os.write(str.getBytes());
			os.flush();
			os.close();
		} catch (IOException e) { e.printStackTrace(); }		
	}
	
	
	@RequestMapping("/team/news/modifyNews")
	public String modifyNews(int nid, String newscontent, HttpServletRequest response) {
		News news = new News();
		news.setNid(nid);
		news.setNcontent(newscontent);
		newsService.modify(news);
		
		return "redirect:/team/news/getNews?nid="+news.getNid();
	}
	
	@RequestMapping("/team/news/image")
	public void image(String fileName, HttpServletRequest request, HttpServletResponse response) {
		ServletContext application = request.getServletContext();
		String realPath = application.getRealPath("/WEB-INF/upload/" + fileName);
		
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
