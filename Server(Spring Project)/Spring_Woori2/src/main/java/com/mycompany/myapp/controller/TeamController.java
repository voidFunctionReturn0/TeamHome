package com.mycompany.myapp.controller;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.mycompany.myapp.dto.*;
import com.mycompany.myapp.service.*;

@Controller
public class TeamController {
	private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

	@Autowired
	private TeamService teamService;
	
	/*@RequestMapping("/team/addteam")
	public String addteam(String tname, String tdescr,String mid) {
		
		System.out.println(tname);
		System.out.println(tdescr);
		System.out.println(mid);
		
		Teams team = new Teams();
		team.setTname(tname);
		team.setTdescr(tdescr);
		team.setMid(mid);
		//team.setTprofile("");
		teamService.write(team);
		
		return "team/addteam";
	}*/
	
	@RequestMapping("/team/addteam")
	public String addteam(String tname,String tdescr, String mid ,MultipartFile tprofile , HttpServletRequest request , Model model ){
		String originalFileName = tprofile.getOriginalFilename();
		long size = tprofile.getSize();
		String contentType = tprofile.getContentType();
		String savedFileName = mid+"_"+originalFileName;
		
		Teams team = new Teams();
		team.setTname(tname);
		team.setTdescr(tdescr);
		team.setMid(mid);
		team.setTprofile(savedFileName);
		
		teamService.write(team);

		logger.info("tname: " + tname);
		logger.info("tdescr: " + tdescr);
		logger.info("size: " + size);
		logger.info("contentType: " + contentType);
		logger.info("savedFileName: " + savedFileName);
		
		ServletContext application = request.getServletContext();
		String realPath = application.getRealPath("/WEB-INF/upload/" + savedFileName);
		File file = new File(realPath);
		try {
			tprofile.transferTo(file);
		}  catch (Exception e) {}
		
		
		model.addAttribute("addteamresult","success" );				
		model.addAttribute("tname", team.getTname());
		model.addAttribute("tdescr", team.getTdescr());
		model.addAttribute("tprofile",team.getTprofile());
		
		return "team/addteam";
	}
	
	
	@RequestMapping("/team/image")
	public void image(int tid, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(tid);		
		Teams team = teamService.getTeam(tid);
		String fileName = team.getTprofile();
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
	
	
	@RequestMapping("/team/getTeamInfo")
	public void getTeamInfo(int tid, HttpServletResponse response) {
		try {
			// tid에 해당하는 팀정보 가져오기
			Teams teamInfo = teamService.getTeam(tid);
			logger.debug("teamID", teamInfo.getTid());
			
			// 팀정보를 JSON으로 변환
			JSONObject teamInfoJSON = new JSONObject();
			teamInfoJSON.put("tid", teamInfo.getTid());
			teamInfoJSON.put("mid", teamInfo.getMid());
			teamInfoJSON.put("tname", teamInfo.getTname());
			if(teamInfo.getTprofile() != null) { teamInfoJSON.put("tprofile", teamInfo.getTprofile()); }
			if(teamInfo.getTdescr() != null) { teamInfoJSON.put("tdescr", teamInfo.getTdescr()); }
			
			// 응답HTTP 본문에 팀정보 보내기
			OutputStream os = response.getOutputStream();
			os.write(teamInfoJSON.toString().getBytes());
			os.flush();
			os.close();
			
		} catch(IOException e) { e.printStackTrace(); }
	}
	
	
	@RequestMapping("/team/getTeamMembers")
	public void getTeamMembers(int tid, HttpServletResponse response) {
		try {
			// tid에 해당하는 팀 멤버 리스트를 DB에서 가져오기
			List<Member> teamMembers = teamService.getTeamMemberListByTid(tid);
			
			
			// 팀 멤버 리스트 JSONArray로 변환하기
			if(teamMembers != null) {
				JSONArray teamMembersJSON = new JSONArray();
				for(Member member : teamMembers) {
					// 각 멤버를 JSONObject로 변환하기
					JSONObject memberJSON = new JSONObject();
					memberJSON.put("mid", member.getMid());
					memberJSON.put("mpwd", member.getMpwd());
					if(member.getMprofile() != null) { memberJSON.put("mprofile", member.getMprofile()); }
					if(member.getMbirth() != null) { memberJSON.put("mbirth", member.getMbirth()); }
					
					
					// 변환한 일정을 JSONArray에 넣기
					teamMembersJSON.put(memberJSON);
				}
				

				// 응답Http 본문에 List를 추가하기
				OutputStream os = response.getOutputStream();
				os.write(teamMembersJSON.toString().getBytes());
				os.flush();
				os.close();
			}			
		} catch(IOException e) { e.printStackTrace(); }
	}
	
	@RequestMapping("/team/withdrawFromTeam")
	public void withdrawFromTeam(String mid, int tid, HttpServletResponse response) {
		try {
			// mid, tid로 팀 탈퇴
			int result = 0;
			result = teamService.withdrawFromTeam(mid, tid);
			
			// 결과 JSON만들기
			JSONObject resultJSON = new JSONObject();
			if(result == 1) {
				resultJSON.put("withdrawResult", "success");
			} else {
				resultJSON.put("withdrawResult", "fail");
			}
			
			OutputStream out = response.getOutputStream();
			out.write(resultJSON.toString().getBytes());
			out.flush();
			out.close();
			
		} catch(IOException e) { e.printStackTrace(); }
	}
}
