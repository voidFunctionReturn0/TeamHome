package com.mycompany.myapp.dao;

import java.util.*;

import org.mybatis.spring.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dto.*;

@Component
public class DataDao {
	
	@Autowired
	private SqlSessionTemplate sst;
	
	public List<News> selectListByTid(int tid) {
		return sst.selectList("DataMapper.selectListByTid", tid);
	}

}
