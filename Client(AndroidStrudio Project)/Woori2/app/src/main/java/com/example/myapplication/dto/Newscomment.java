package com.example.myapplication.dto;

import java.util.*;

public class Newscomment {
	
	private int cid;
	private String ccontent;
	private Date cdate;
	private int nid;
	private String mid;
	private String mprofile;

	public String getMprofile() {
		return mprofile;
	}

	public void setMprofile(String mprofile) {
		this.mprofile = mprofile;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getCcontent() {
		return ccontent;
	}
	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	
}