package com.mycompany.myapp.controller.teammain;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.http.*;

import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.mycompany.myapp.dto.*;
import com.mycompany.myapp.service.*;

@Controller
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@RequestMapping("/team/news/commentList")
	public void commentList(int nid, HttpServletResponse response) {
		List<Newscomment> commentlist = commentService.getCommentListByNid(nid);
		String mimeType = "application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if(commentlist==null) {
			root.put("commentListResult", "fail");
		}else {
			root.put("commentListResult", "success");
			JSONArray commentArray = new JSONArray();
			for(Newscomment comment:commentlist) {
				JSONObject commentJson = new JSONObject();
				commentJson.put("cid", comment.getCid());
				commentJson.put("ccontent", comment.getCcontent()!= null?comment.getCcontent():"");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				commentJson.put("cdate", sdf.format(comment.getCdate()));
				commentJson.put("nid", comment.getNid());
				commentJson.put("mid", comment.getMid());				
				commentJson.put("mprofile", comment.getMprofile() != null?comment.getMprofile():"");
				commentArray.put(commentJson);
			}
			root.put("list", commentArray);
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
	
	
	@RequestMapping("/team/news/writeComment")
	public void writeComment(String mid, String ccontent ,int nid, HttpServletResponse response) {
		Newscomment newsComment = new Newscomment(ccontent, new Date(), nid, mid);
		
		int writeCommentResult = commentService.writeComment(newsComment);
		
		String mimeType = "application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if((writeCommentResult==0)) {
			root.put("writeCommentResult", "fail");
		}else {
			root.put("writeCommentResult", "success");
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
	
	
	@RequestMapping("/team/news/deleteComment")
	public void deleteComment(int cid, HttpServletResponse response) {

		int deleteCommentResult = commentService.removeComment(cid);
		
		String mimeType = "application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if((deleteCommentResult==0)) {
			root.put("deleteCommentResult", "fail");
		}else {
			root.put("deleteCommentResult", "success");
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
	
	
	@RequestMapping("/team/news/modifyComment")
	public void modifyComment(int cid, String commentcontent, HttpServletResponse response) {
		
		Newscomment newscomment = new Newscomment();
		newscomment.setCid(cid);
		newscomment.setCcontent(commentcontent);
		int modifyCommentResult = commentService.modifyComment(newscomment);
		
		
		String mimeType = "application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if((modifyCommentResult==0)) {
			root.put("modifyCommentResult", "fail");
		}else {
			root.put("modifyCommentResult", "success");
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
	
	
	@RequestMapping("/team/news/getComment")
	public void getComment(int cid, HttpServletResponse response) {
		Newscomment newscomment = commentService.getComment(cid);
		
		String mimeType = "application/json";
		response.setContentType(mimeType);
		
		JSONObject root = new JSONObject();
		if(newscomment==null) {
			root.put("modifyCommentResult", "fail");
		}else {
			root.put("modifyCommentResult", "success");
			JSONObject commentJson = new JSONObject();
			commentJson.put("cid", newscomment.getCid());
			commentJson.put("ccontent", newscomment.getCcontent()!= null?newscomment.getCcontent():"");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			commentJson.put("cdate", sdf.format(newscomment.getCdate()));
			commentJson.put("nid", newscomment.getNid());
			commentJson.put("mid", newscomment.getMid());				
			commentJson.put("mprofile", newscomment.getMprofile() != null?newscomment.getMprofile():"");
			root.put("list", commentJson);
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

}
