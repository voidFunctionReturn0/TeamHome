package com.mycompany.myapp.dao;

import java.util.*;

import org.mybatis.spring.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dto.*;

@Component
public class CommentDao {
	
	@Autowired
	private SqlSessionTemplate sst;
	
	public List<Newscomment> selectListByNid(int nid) {
		return sst.selectList("CommentMapper.selectListByNid", nid);
	}
	
	public int insert(Newscomment newscomment) {
		//newscomment에 cid없는 상태
		sst.insert("CommentMapper.insert", newscomment);
		//Mappter에서 newscomment에 cid를 넣어서 반환
		//여기에서는 newscomment에 cid를 갖고 있는 상태
		return newscomment.getCid();
	}
	
	public int deleteByPk(int cid) {
		return sst.delete("CommentMapper.deleteByPk", cid);
	}
	
	public int update(Newscomment newscomment) {
		return sst.update("CommentMapper.update", newscomment);
	}
	
	public Newscomment selectByCid(int cid) {
		return sst.selectOne("CommentMapper.selectByCid", cid);
	}
	
}
