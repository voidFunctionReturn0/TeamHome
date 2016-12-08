package com.mycompany.myapp.controller;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.json.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.myapp.dto.Schedule;
import com.mycompany.myapp.service.ScheduleService;

@Controller
public class ScheduleController {
	
	private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);
	
	@Autowired
	private ScheduleService scheduleService;
	
	
	// 팀별-월별 일정리스트 가져오기 ( tid에 해당하는 리스트 가져와서 같은 연, 월인 일정 리스트 넘김 )
	@SuppressWarnings("deprecation")
	@RequestMapping("/schedule/getMonthListByTid")
	public void getMonthListByTid(HttpServletResponse response, int tid, String sdate) {
		try {
			log.debug("tid: " + tid);
			log.debug("sdate: " + sdate);
			
			// 문자열 date를 sql.Date클래스로 변환
			java.sql.Date date = ScheduleController.strToDate(sdate);
			
			// tid에 해당하는 일정 리스트를 DB에서 가져오기
			List<Schedule> schedules = scheduleService.getListByTid(tid);
			log.debug("list1: " + schedules.size());
			
			// Date(연, 월)에 해당하는 리스트 거르기
			List<Schedule> schedulesByDate = new ArrayList<>();
			for(Schedule s : schedules) {
				java.sql.Date startDate = ScheduleController.strToDate(s.getSstartdate());
				java.sql.Date endDate = ScheduleController.strToDate(s.getSenddate());
				
				if(startDate.getYear()-1900 == date.getYear()-1900 || endDate.getYear()-1900 == date.getYear()-1900 ) {
					if(startDate.getMonth()-1 == date.getMonth()-1 || endDate.getMonth()-1 == date.getMonth()-1) {
						schedulesByDate.add(s);
					}
				}
			}
			log.debug("list2: " + schedulesByDate.size());
									
			// 일정리스트 JSONArray로 변환하기
			if(schedulesByDate != null) {
				JSONArray schedulesJSON = new JSONArray();
				
				for(Schedule s : schedulesByDate) {
					// 각 일정을 JSONObject로 변환하기
					JSONObject sJSON = new JSONObject();
					sJSON.put("sid", s.getSid());
					sJSON.put("sname", s.getSname());
					sJSON.put("sicon", s.getSicon());
					sJSON.put("sstartdate", s.getSstartdate());
					sJSON.put("senddate", s.getSenddate());
					sJSON.put("stime", s.getStime());
					sJSON.put("sdescr", s.getSdescr());
					sJSON.put("slocation", s.getSlocation());
					sJSON.put("tid", s.getTid());
					
					// 변환한 일정을 JSONArray에 넣기
					schedulesJSON.put(sJSON);
				}
				
				log.debug("list3: " + schedulesJSON.length());
				
				// 응답Http 본문에 List를 추가하기
				OutputStream os = response.getOutputStream();
				os.write(schedulesJSON.toString().getBytes());
				os.flush();
				os.close();
			}			
		} catch(IOException e) { e.printStackTrace(); }
	}
	
	
	// 팀별-연별 일정리스트 가져오기 ( tid에 해당하는 리스트 가져와서 같은 연도인 일정 리스트 넘김 )
		@SuppressWarnings("deprecation")
		@RequestMapping("/schedule/getYearListByTid")
		public void getYearListByTid(HttpServletResponse response, int tid, String sdate) {
			try {
				log.debug("tid: " + tid);
				log.debug("sdate: " + sdate);
				
				// 문자열 date를 sql.Date클래스로 변환
				java.sql.Date date = ScheduleController.strToDate(sdate);
				
				// tid에 해당하는 일정 리스트를 DB에서 가져오기
				List<Schedule> schedules = scheduleService.getListByTid(tid);
				log.debug("list1: " + schedules.size());
				
				// Date(연도)에 해당하는 리스트 거르기
				List<Schedule> schedulesByDate = new ArrayList<>();
				for(Schedule s : schedules) {
					java.sql.Date startDate = ScheduleController.strToDate(s.getSstartdate());
					java.sql.Date endDate = ScheduleController.strToDate(s.getSenddate());
					
					if(startDate.getYear()-1900 == date.getYear()-1900 || endDate.getYear()-1900 == date.getYear()-1900 ) {
						schedulesByDate.add(s);
					}
				}
				log.debug("list2: " + schedulesByDate.size());
										
				// 일정리스트 JSONArray로 변환하기
				if(schedulesByDate != null) {
					JSONArray schedulesJSON = new JSONArray();
					
					for(Schedule s : schedulesByDate) {
						// 각 일정을 JSONObject로 변환하기
						JSONObject sJSON = new JSONObject();
						sJSON.put("sid", s.getSid());
						sJSON.put("sname", s.getSname());
						sJSON.put("sicon", s.getSicon());
						sJSON.put("sstartdate", s.getSstartdate());
						sJSON.put("senddate", s.getSenddate());
						sJSON.put("stime", s.getStime());
						sJSON.put("sdescr", s.getSdescr());
						sJSON.put("slocation", s.getSlocation());
						sJSON.put("tid", s.getTid());
						
						// 변환한 일정을 JSONArray에 넣기
						schedulesJSON.put(sJSON);
					}
					
					log.debug("list3: " + schedulesJSON.length());
					
					// 응답Http 본문에 List를 추가하기
					OutputStream os = response.getOutputStream();
					os.write(schedulesJSON.toString().getBytes());
					os.flush();
					os.close();
				}			
			} catch(IOException e) { e.printStackTrace(); }
		}
		
	
	// 팀별 일정리스트 생성
	@RequestMapping("/schedule/addSchedule")
	public void addSchedule(String schedule, HttpServletResponse response) {
		// 문자열 schedule을 Schedule클래스로 JSON파싱
		JSONObject scheduleJSON = new JSONObject(schedule);
				
		// JSON파싱 (null 가능한 속성은 "" 저장)
		Schedule s = new Schedule();
		s.setSid(scheduleJSON.getInt("sid"));
		s.setSname(scheduleJSON.getString("sname"));
		s.setSicon(scheduleJSON.getString("sicon"));
		s.setSstartdate(scheduleJSON.getString("sstartdate"));
		s.setSenddate(scheduleJSON.getString("senddate"));
		if(scheduleJSON.isNull("stime")) { s.setStime(""); }
		else { s.setStime(scheduleJSON.getString("stime")); }
		if(scheduleJSON.isNull("sdescr")) { s.setSdescr(""); }
		else { s.setSdescr(scheduleJSON.getString("sdescr")); }
		if(scheduleJSON.isNull("slocation")) { s.setSlocation(""); } 
		else { s.setSlocation(scheduleJSON.getString("slocation")); }
		s.setTid(scheduleJSON.getInt("tid"));
		
		// 성공시 PK, 실패시 -1 리턴
		int addResult = scheduleService.addSchedule(s);
		
		// 결과 보내기
		try {
			JSONObject addResultJSON = new JSONObject();
			addResultJSON.put("addResult", addResult);
			OutputStream os = response.getOutputStream();
			os.write(addResultJSON.toString().getBytes());
			os.flush();
			os.close();
		} catch(IOException e) { e.printStackTrace(); }
		
	}
	
	
	// 일정 삭제
	@RequestMapping("/schedule/removeSchedule")
	public void removeSchedule(String sid, HttpServletResponse response) {
		// JSON을 파싱
		JSONObject sidJSON = new JSONObject(sid);
		int sidInt  = sidJSON.getInt("sid");
	
		
		int removeResult = scheduleService.removeSchedule(sidInt);
		
		// 결과 보내기
		try {
			JSONObject removeResultJSON = new JSONObject();
			removeResultJSON.put("removeResult", removeResult);
			OutputStream os = response.getOutputStream();
			os.write(removeResultJSON.toString().getBytes());
			os.flush();
			os.close();
		} catch(IOException e) { e.printStackTrace(); }
	}
	
	
	// 일정 수정
	@RequestMapping("/schedule/updateSchedule")
	public void updateSchedule(String schedule, HttpServletResponse response) {
		
		Schedule s = new Schedule();
		JSONObject sJSON = new JSONObject(schedule);
		s.setSid(sJSON.getInt("sid"));
		log.debug(s.getSid()+"?");
		s.setSname(sJSON.getString("sname"));
		s.setSicon(sJSON.getString("sicon"));
		s.setSstartdate(sJSON.getString("sstartdate"));
		s.setSenddate(sJSON.getString("senddate"));
		s.setSdescr(sJSON.getString("sdescr"));
		s.setSlocation(sJSON.getString("slocation"));
		s.setTid(sJSON.getInt("tid"));
		
		
		int updateResult = scheduleService.updateSchedule(s);
		
		// 결과 보내기
		try {
			JSONObject updateResultJSON = new JSONObject();
			updateResultJSON.put("updateResult", updateResult);
			OutputStream os = response.getOutputStream();
			os.write(updateResultJSON.toString().getBytes());
			os.flush();
			os.close();
		} catch(IOException e) { e.printStackTrace(); }
	}
	
	
	// String -> sql.Date 변환
	public static java.sql.Date strToDate(String strDate) {
		String[] strArr = strDate.split("-");
		java.sql.Date date = new java.sql.Date(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]), Integer.parseInt(strArr[2]));
		return date;
	}
}
