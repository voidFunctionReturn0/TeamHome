package com.mycompany.myapp.controller.search;

import java.io.*;
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

import com.mycompany.myapp.controller.*;
import com.mycompany.myapp.dto.*;
import com.mycompany.myapp.service.*;

@Controller
public class SearchController {
	private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private MemberService memberService;
	
	//팀 검색 리스트
	@RequestMapping("/team/search")
	public void list(String keyword, String mid, HttpServletResponse response){
		List<Teams> teamList = teamService.getTeamListByKeyword(keyword, mid);
/*		Ch14FileInfo fileInfo = (Ch14FileInfo) session.getAttribute("fileInfo");
		
		String realPath = application.getRealPath("/WEB-INF/upload/" + fileInfo.getSavedFileName());*/
		//String mimeType = fileInfo.getContentType()
		String mimeType = "application/json";
		//헤더행 추가
		response.setContentType(mimeType);
//		String filename = new String(fileInfo.getOriginalFileName().getBytes("UTF-8") , "ISO-8859-1");
//		response.addHeader("Content-Disposition", "attachment;filename=\"" + filename+"\"");
			
		
		// 본문에 파일 데이터 출력	
		// 입력 스트림 얻기
		JSONObject root = new JSONObject();
		if(teamList == null || teamList.size() == 0){
			root.put("result", "fail");
		}else{
			root.put("result", "success");
		}
		
		JSONArray array = new JSONArray();		
		for(Teams team : teamList){
			JSONObject data = new JSONObject();
			data.put("tid", team.getTid());
			data.put("tname", team.getTname());
			data.put("tdescr", team.getTdescr() != null ? team.getTdescr() : "");
			data.put("mid", team.getMid());
			data.put("tprofile", team.getTprofile() != null ? team.getTprofile() : "");
			data.put("tnum", team.getTnum());
			data.put("isJoin", team.getIsJoin());
			array.put(data);
		}
		root.put("list", array);
	
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
	
	@RequestMapping("/member/joinTeam")
	public void joinTeam(String mid, int tid, HttpServletRequest request){
		memberService.joinTeam(mid, tid);
	}
}
