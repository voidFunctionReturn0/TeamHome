package com.mycompany.myapp.dao;

import java.util.*;

import org.mybatis.spring.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dto.*;

@Component
public class TeamDao {
	@Autowired
	private SqlSessionTemplate sst;
	
	public List<Teams> selectListByMid(String mid){
		return sst.selectList("TeamMapper.selectListByMid",mid);
		
	}
	
	public List<Teams> selectListByKeyword(String keyword, String mid){
		
		keyword = "%" + keyword + "%";
		Map<String, String> map = new HashMap<>();
		map.put("keyword", keyword);
		map.put("mid", mid);
		return sst.selectList("TeamMapper.selectListByKeyword",map);
		
	}
	public int insert(Teams team){
		sst.insert("TeamMapper.insert" , team);
		sst.insert("TeamMapper.insert2" , team);
		return team.getTid();
	}
	
	public Teams selectByTid(int tid) {
		return sst.selectOne("TeamMapper.selectByTid", tid);
	}
	
	public List<Member> selectTeamMemberListByTid(int tid) {
		return sst.selectList("TeamMapper.selectTeamMemberListByTid", tid);
	}
	
	public int deleteTeamMember(String mid, int tid) {
		Map map = new HashMap<>();
		map.put("mid", mid);
		map.put("tid", tid);
		
		return sst.delete("TeamMapper.deleteTeamMember", map);
	}
}
