package com.mycompany.myapp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.mycompany.myapp.dao.*;
import com.mycompany.myapp.dto.*;

@Component
public class CommentService {
	
	@Autowired
	private CommentDao commentDao;
	
	
	public List<Newscomment> getCommentListByNid(int nid) {
		return commentDao.selectListByNid(nid);
	}
	
	public int writeComment(Newscomment newscomment) {
		return commentDao.insert(newscomment);
	}
	
	public int removeComment(int cid) {
		return commentDao.deleteByPk(cid);
	}
	
	public int modifyComment(Newscomment newscomment) {
		return commentDao.update(newscomment);
	}
	
	public Newscomment getComment(int cid) {
		return commentDao.selectByCid(cid);
	}

}
