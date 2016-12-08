package com.mycompany.myapp.dao;

import java.util.*;

import org.mybatis.spring.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dto.*;

@Component
public class NewsDao {
	@Autowired
	private SqlSessionTemplate sst;
	
	public void insert(News news){
		sst.insert("NewsMapper.insert", news);
	}
	
	public List<News> selectListByTid(int tid){
		return sst.selectList("NewsMapper.selectListByTid",tid);
	}
	
	public int deleteByNid(int nid) {
		System.out.println(nid);
		return sst.delete("NewsMapper.deleteByNid", nid);
	}
	
	public News selectByPk(int nid) {
		return sst.selectOne("NewsMapper.selectByPk",nid);
	}
	
	public int update(News news) {
		return sst.update("NewsMapper.update",news);
	}
}
