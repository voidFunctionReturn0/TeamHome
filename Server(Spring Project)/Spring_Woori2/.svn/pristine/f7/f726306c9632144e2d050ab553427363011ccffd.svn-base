package com.mycompany.myapp.controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.mycompany.myapp.dto.*;
import com.mycompany.myapp.service.*;

@Controller
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/member/login")
	public String login(String mid, String mpwd, Model model) {
		
		Member member = memberService.getMember(mid);
		
		if(member != null){
			if(member.getMpwd().equals(mpwd) == true) {
				
				model.addAttribute("loginresult","success" );				
				model.addAttribute("mid", member.getMid());
				model.addAttribute("mpwd", member.getMpwd());
				model.addAttribute("mprofile", member.getMprofile());
										
				if(member.getMbirth() != null) {
					model.addAttribute("mbirth", member.getMbirth().toString());
				} 
				
			} else {
				model.addAttribute("loginresult", "fail");
				model.addAttribute("mid", "");
				model.addAttribute("mpwd", "");
				model.addAttribute("mprofile", "");
				model.addAttribute("mbirth", "");
			}
		}else{
			model.addAttribute("loginresult", "fail");
			model.addAttribute("mid", "");
			model.addAttribute("mpwd", "");
			model.addAttribute("mprofile", "");
			model.addAttribute("mbirth", "");
		}
		
		return "member/login";
	}
	
	
	@RequestMapping("/member/duplicate")
	public String duplicate(String mid, Model model){
		
		Member member = memberService.getMember(mid);
		
		if(member == null){
			model.addAttribute("dupCheck","success");
		}else{
			model.addAttribute("dupCheck","fail");
		}
		return "member/duplicate";
		
	}
	
	@RequestMapping("/member/join")
	public String join(String mid, String mpwd, String mbirthday, MultipartFile mprofile, HttpServletRequest request, Model model){
		
		
/*		long size = mprofile.getSize();
		String contentType = mprofile.getContentType();*/
		
		
		Member member = new Member();
		String originalFileName = mprofile.getOriginalFilename();
		String savedFileName = originalFileName;
		ServletContext application = request.getServletContext();
		String realPath = application.getRealPath("/WEB-INF/upload/" + savedFileName);
		File file = new File(realPath);
		try {
			mprofile.transferTo(file);
		}  catch (Exception e) {}
		
		logger.info(mid);
		logger.info(mpwd);
		logger.info(mbirthday);
		logger.info(mprofile.getOriginalFilename());
		
		model.addAttribute("joinResult","success");
		String[] birthArray = mbirthday.split("-");
		String year = birthArray[0];
		String month = birthArray[1];
		String day = birthArray[2];
		
		java.sql.Date birthday = new java.sql.Date(Integer.parseInt(year)-1900, Integer.parseInt(month)-1, Integer.parseInt(day));
		
		member.setMid(mid);
		member.setMpwd(mpwd);
		member.setMbirth(birthday);
		member.setMprofile(originalFileName);
		
		memberService.write(member);
		
		
		return "member/join";
	}
	@RequestMapping("/memberdown/image")
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
	
	@RequestMapping("/member/revise")
	public String join(String mid, String mpwd, MultipartFile mprofile, HttpServletRequest request, Model model){
		
		
		Member member = new Member();
		String originalFileName = mprofile.getOriginalFilename();
		String savedFileName = originalFileName;
		ServletContext application = request.getServletContext();
		String realPath = application.getRealPath("/WEB-INF/upload/" + savedFileName);
		File file = new File(realPath);
		try {
			mprofile.transferTo(file);
		}  catch (Exception e) {}
		
		logger.info(mpwd);
		logger.info(mprofile.getOriginalFilename());
		
		model.addAttribute("reviseResult","success");
		
		
		member.setMpwd(mpwd);
		member.setMid(mid);
		
		memberService.revise(member);
		
		
		
		return "member/revise";
	}
	
	@RequestMapping("/member/out")
	public void out(String mid){
		
		logger.info(mid);
		memberService.remove(mid);
		
	}
}






