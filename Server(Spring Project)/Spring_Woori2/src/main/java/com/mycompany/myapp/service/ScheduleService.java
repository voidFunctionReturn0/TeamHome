package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.ScheduleDao;
import com.mycompany.myapp.dto.Schedule;

@Component
public class ScheduleService {
	@Autowired
	private ScheduleDao scheduleDao;
	
	public List<Schedule> getListByTid(int tid) {
		return scheduleDao.selectByTid(tid);
	}

	// 성공시 PK, 실패시 -1 리턴
	public int addSchedule(Schedule schedule) {
		return scheduleDao.insert(schedule); 
	}

	
	public int removeSchedule(int sid) {
		return scheduleDao.delete(sid);
	}
	
	
	public int updateSchedule(Schedule s) {
		return scheduleDao.update(s);
	}
}
