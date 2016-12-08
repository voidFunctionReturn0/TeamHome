package com.mycompany.myapp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dao.*;
import com.mycompany.myapp.dto.*;

@Component
public class NewsService {
	
	@Autowired
	private NewsDao newsDao;
	
	public void addNews(News news){
		newsDao.insert(news);
	}
	
	public List<News> getNewsListByTid(int tid) {
		List<News> newsList = null;
		newsList = newsDao.selectListByTid(tid);
		return newsList;
	}
	
	public News getNews(int nid) {
		return newsDao.selectByPk(nid);
	}
	
	
	public int removeNews(int nid) {
		return newsDao.deleteByNid(nid);
	}
	
	public int modify(News news) {
		return newsDao.update(news);
	}
	
	

}
