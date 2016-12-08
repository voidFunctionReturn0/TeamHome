package com.mycompany.myapp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dao.*;
import com.mycompany.myapp.dto.*;

@Component
public class PhotoService {
	
	@Autowired
	private PhotoDao photoDao;
	
	public List<News> getPhotoList(int tid) {
		return photoDao.selectListByTid(tid); 
	}

}
