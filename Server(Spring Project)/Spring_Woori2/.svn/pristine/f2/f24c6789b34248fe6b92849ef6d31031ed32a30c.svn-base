package com.mycompany.myapp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.mycompany.myapp.dao.*;
import com.mycompany.myapp.dto.*;

@Component
public class TeamService {

	@Autowired
	private TeamDao teamDao;
	
	public List<Teams> getTeamListByMid(String mid){
		List<Teams> teamList = null;
		teamList = teamDao.selectListByMid(mid);
		return teamList;
	}
	
	public List<Teams> getTeamListByKeyword(String keyword, String mid){
		List<Teams> teamList = null;
		teamList = teamDao.selectListByKeyword(keyword,mid);
		return teamList;
	}
	@Transactional
	public int write(Teams team){
		int tid = teamDao.insert(team);
		return tid;
	}
	
	public Teams getTeam(int tid) {
		return teamDao.selectByTid(tid);
	}
	
	public List<Member> getTeamMemberListByTid(int tid) {
		return teamDao.selectTeamMemberListByTid(tid);
	}
	
	public int withdrawFromTeam(String mid, int tid) {
		return teamDao.deleteTeamMember(mid, tid);
	}
}
