package com.example.myapplication.home;

/**
 * Created by Administrator on 2016-07-05.
 */
public class Team {
    String tname;
    String tadmin;
    int tprofile;
    String tdescr;
    int tmemberNum;


    public Team(String tname, String tadmin, int tprofile, String tdescr, int tmemberNum) {
        this.tname = tname;
        this.tadmin = tadmin;
        this.tprofile = tprofile;
        this.tdescr = tdescr;
        this.tmemberNum = tmemberNum;
    }


    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTadmin() {
        return tadmin;
    }

    public void setTadmin(String tadmin) {
        this.tadmin = tadmin;
    }

    public int getTprofile() {
        return tprofile;
    }

    public void setTprofile(int tprofile) {
        this.tprofile = tprofile;
    }

    public String getTdescr() {
        return tdescr;
    }

    public void setTdescr(String tdescr) {
        this.tdescr = tdescr;
    }

    public int getTmemberNum() {
        return tmemberNum;
    }

    public void setTmemberNum(int tmemberNum) {
        this.tmemberNum = tmemberNum;
    }
}
