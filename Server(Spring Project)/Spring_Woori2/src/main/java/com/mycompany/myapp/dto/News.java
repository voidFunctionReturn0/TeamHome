package com.mycompany.myapp.dto;

import java.util.*;

public class News {
	
	private int nid;
	private int tid;
	private String mid;
	private String mprofile;
	private Date ndate;
	private String ncontent;
	private String nphotoname;
	private String ndataname;
	private int nisnotice;//0=f 1=t
	
	
	public String getMprofile() {
		return mprofile;
	}
	public void setMprofile(String mprofile) {
		this.mprofile = mprofile;
	}
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public Date getNdate() {
		return ndate;
	}
	public void setNdate(Date ndate) {
		this.ndate = ndate;
	}
	public String getNcontent() {
		return ncontent;
	}
	public void setNcontent(String ncontent) {
		this.ncontent = ncontent;
	}
	public String getNphotoname() {
		return nphotoname;
	}
	public void setNphotoname(String nphotoname) {
		this.nphotoname = nphotoname;
	}
	public String getNdataname() {
		return ndataname;
	}
	public void setNdataname(String ndataname) {
		this.ndataname = ndataname;
	}
	public int getNisnotice() {
		return nisnotice;
	}
	public void setNisnotice(int nisnotice) {
		this.nisnotice = nisnotice;
	}
	
}
