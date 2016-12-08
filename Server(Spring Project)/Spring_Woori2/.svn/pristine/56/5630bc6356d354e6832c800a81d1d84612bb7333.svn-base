package com.mycompany.myapp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dao.*;
import com.mycompany.myapp.dto.*;

@Component
public class DataService {
	
	@Autowired
	private DataDao dataDao;
	
	public List<News> getDataList(int tid) {
		return dataDao.selectListByTid(tid); 
	}

}
