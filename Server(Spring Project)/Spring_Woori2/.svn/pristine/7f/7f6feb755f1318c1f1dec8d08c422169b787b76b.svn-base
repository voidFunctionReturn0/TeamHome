package com.mycompany.myapp.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dao.*;
import com.mycompany.myapp.dto.*;

@Component
public class MemberService {
	
	@Autowired
	private MemberDao memberDao;
	
	public Member getMember(String mid){
		Member member = null;
		member = memberDao.selectByPk(mid);
		return member;
	}
	
	public void write(Member member){
		memberDao.insert(member);
	}
	public void joinTeam(String mid, int tid){
		memberDao.insertToTeam(mid, tid);
	}
	
	public void revise(Member member){
		memberDao.update(member);
	}
	
	public void remove(String mid){
		memberDao.delete(mid);
	}
}
