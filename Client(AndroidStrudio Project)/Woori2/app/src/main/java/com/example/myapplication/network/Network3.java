package com.example.myapplication.network;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.actionbar.navi.teamInfo.TeamInfoFragment;
import com.example.myapplication.actionbar.navi.teamInfo.TeamMembersListViewAdapter;
import com.example.myapplication.dto.Member;
import com.example.myapplication.dto.News;
import com.example.myapplication.dto.Schedule;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.home.HomeFragment;
import com.example.myapplication.team.TeamHomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Network3 {

    // 팀별-월별 일정 리스트 가져오기
    public static List<Schedule> getScheduleListByTidAndDate(int tid, Calendar sdate) {
        List<Schedule> list = null;
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);

        try {
            AsyncTask<String, Void, List<Schedule>> asynctask = new AsyncTask<String, Void, List<Schedule>>() {
                @Override
                protected List<Schedule> doInBackground(String... params) {
                    String json = "";
                    List<Schedule> list = null;

                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(1000);
                        conn.connect();

                        // 요청보내고 정상응답이 왔는지 확인하기
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            // 정상응답인 경우 응답Http본문에서 일정 리스트 가져오기
                            InputStream is = conn.getInputStream();
                            Reader reader = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(reader);

                            while (true) {
                                String line = br.readLine();
                                json += line;
                                if (line == null) break;
                            }
                            br.close();
                            reader.close();
                            is.close();

                            // JSON파싱
                            JSONArray schedulesJSON = new JSONArray(json);

                            list = new ArrayList<>();

                            for (int i = 0; i < schedulesJSON.length(); i++) {
                                JSONObject sJSON = schedulesJSON.getJSONObject(i);
                                Schedule s = new Schedule();

                                s.setSid(sJSON.getInt("sid"));
                                s.setSname(sJSON.getString("sname"));
                                s.setSicon(sJSON.getString("sicon"));
                                s.setSstartdate(sJSON.getString("sstartdate"));
                                s.setSenddate(sJSON.getString("senddate"));
                                //if(sJSON.isNull("stime")==false)        s.setStime(sJSON.getString("stime"));
                                if(sJSON.isNull("sdescr")==false)       s.setSdescr(sJSON.getString("sdescr"));
                                if(sJSON.isNull("slocation")==false)    s.setSlocation(sJSON.getString("slocation"));
                                s.setTid(sJSON.getInt("tid"));

                                list.add(s);
                            }
                        }
                        conn.disconnect();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    // 정상응답이면 list, 아니면 null반환
                    Log.d("list1", list.size()+"");
                    return list;
                }
            };

            // 연도가 -1900 되어있는 것을 +1900 해줌
            java.sql.Date dateDateType = new java.sql.Date(sdate.getTime().getTime());
            dateDateType.setYear(dateDateType.getYear()+1900);

            // 네트워크작업 후 일정리스트 받아옴
            list = asynctask.execute(Network.site + "schedule/getYearListByTid?tid=" + tid + "&sdate=" + dateDateType.toString()).get();

        } catch(InterruptedException | ExecutionException e) { e.printStackTrace(); }

        Log.d("list2", list.size()+"");
        dialog.dismiss();
        return list;
    }


    // 일정 추가
    public static boolean addSchedule(Schedule s) {
        boolean addResult = false;
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);

        try {
            // Schedule -> JSONObject 변환
            final JSONObject sJSON = new JSONObject();
            sJSON.put("sid", s.getSid());
            sJSON.put("sname", s.getSname());
            sJSON.put("sicon", s.getSicon());
            sJSON.put("sstartdate", s.getSstartdate());
            sJSON.put("senddate", s.getSenddate());
            //sJSON.put("stime", s.getStime());
            sJSON.put("sdescr", s.getSdescr());
            sJSON.put("slocation", s.getSlocation());
            sJSON.put("tid", s.getTid());

            AsyncTask<String, Void, Boolean> asynctask = new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    boolean addResult = false;
                    String json = "";

                    try {
                        // 데이터 구분 문자
                        String boundary = "----" + System.currentTimeMillis();

                        // 데이터 경계선
                        String delimiter = "\r\n--" + boundary + "\r\n";

                        // 커넥션 생성 및 설정
                        URL url = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(1000);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                        // 연결하기
                        conn.connect();

                        //출력 스트림 얻기
                        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

                        //문자열 데이터 전송
                        StringBuffer postDataBuilder = new StringBuffer();
                        postDataBuilder.append(delimiter);
                        postDataBuilder.append(Network5.setValue("schedule", sJSON.toString()));
                        out.writeUTF(postDataBuilder.toString());

                        //종료 구분자 넣기
                        out.writeUTF("\r\n--" + boundary + "--\r\n");

                        //출력스트림 닫기
                        out.flush();
                        out.close();

                        // 요청보내고 정상응답이 왔는지 확인하기
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            // 정상응답인 경우 응답Http본문에서 일정 리스트 가져오기
                            InputStream is = conn.getInputStream();
                            Reader reader = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(reader);

                            while (true) {
                                String line = br.readLine();
                                json += line;
                                if (line == null) break;
                            }
                            br.close();
                            reader.close();
                            is.close();

                            // JSON파싱
                            JSONObject addResultJSON = new JSONObject(json);

                            String addResultStr = addResultJSON.getString("addResult");
                            if(addResultStr.equals("success")) { addResult = true; }
                        }
                        conn.disconnect();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return addResult;
                }
            };

            addResult = asynctask.execute(Network.site + "schedule/addSchedule").get();

        } catch(InterruptedException | ExecutionException | JSONException e) { e.printStackTrace(); }

        dialog.dismiss();
        return addResult;
    }


    // 일정 추가
    public static int updateSchedule(Schedule s) {
        int addResult = -1;
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);

        try {
            // Schedule -> JSONObject 변환
            final JSONObject sJSON = new JSONObject();
            sJSON.put("sid", s.getSid());
            sJSON.put("sname", s.getSname());
            Log.d("sname", s.getSname());
            sJSON.put("sicon", s.getSicon());
            sJSON.put("sstartdate", s.getSstartdate());
            sJSON.put("senddate", s.getSenddate());
            sJSON.put("sdescr", s.getSdescr());
            sJSON.put("slocation", s.getSlocation());
            sJSON.put("tid", s.getTid());

            AsyncTask<String, Void, Integer> asynctask = new AsyncTask<String, Void, Integer>() {
                @Override
                protected Integer doInBackground(String... params) {
                    int updateResult = -1;
                    String json = "";

                    try {
                        // 데이터 구분 문자
                        String boundary = "----" + System.currentTimeMillis();

                        // 데이터 경계선
                        String delimiter = "\r\n--" + boundary + "\r\n";

                        // 커넥션 생성 및 설정
                        URL url = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(1000);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                        // 연결하기
                        conn.connect();

                        //출력 스트림 얻기
                        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

                        //문자열 데이터 전송
                        StringBuffer postDataBuilder = new StringBuffer();
                        postDataBuilder.append(delimiter);
                        postDataBuilder.append(Network5.setValue("schedule", sJSON.toString()));
                        out.writeUTF(postDataBuilder.toString());

                        //종료 구분자 넣기
                        out.writeUTF("\r\n--" + boundary + "--\r\n");

                        //출력스트림 닫기
                        out.flush();
                        out.close();

                        // 요청보내고 정상응답이 왔는지 확인하기
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            // 정상응답인 경우 응답Http본문에서 일정 리스트 가져오기
                            InputStream is = conn.getInputStream();
                            Reader reader = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(reader);

                            while (true) {
                                String line = br.readLine();
                                json += line;
                                if (line == null) break;
                            }
                            br.close();
                            reader.close();
                            is.close();

                            // JSON파싱
                            JSONObject updateResultJSON = new JSONObject(json);

                            updateResult = updateResultJSON.getInt("updateResult");
                        }
                        conn.disconnect();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return updateResult;
                }
            };

            addResult = asynctask.execute(Network.site + "schedule/updateSchedule").get();

        } catch(InterruptedException | ExecutionException | JSONException e) { e.printStackTrace(); }

        dialog.dismiss();
        return addResult;
    }


    // 일정 삭제
    public static int removeSchedule(int sid) {
        int removeResult = -1;
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);

        try {
            // sid -> JSONObject 변환
            final JSONObject sidJSON = new JSONObject();
            sidJSON.put("sid", sid);

            AsyncTask<String, Void, Integer> asynctask = new AsyncTask<String, Void, Integer>() {
                @Override
                protected Integer doInBackground(String... params) {
                    int removeResult = -1;
                    String resultJSON = "";

                    try {
                        // 데이터 구분 문자
                        String boundary = "----" + System.currentTimeMillis();

                        // 데이터 경계선
                        String delimiter = "\r\n--" + boundary + "\r\n";

                        // 커넥션 생성 및 설정
                        URL url = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(1000);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                        // 연결하기
                        conn.connect();

                        //출력 스트림 얻기
                        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

                        //문자열 데이터 전송
                        StringBuffer postDataBuilder = new StringBuffer();
                        postDataBuilder.append(delimiter);
                        postDataBuilder.append(Network5.setValue("sid", sidJSON.toString()));
                        out.writeUTF(postDataBuilder.toString());

                        //종료 구분자 넣기
                        out.writeUTF("\r\n--" + boundary + "--\r\n");

                        //출력스트림 닫기
                        out.flush();
                        out.close();

                        // 요청보내고 정상응답이 왔는지 확인하기
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            // 정상응답인 경우 응답Http본문에서 일정 리스트 가져오기
                            InputStream is = conn.getInputStream();
                            Reader reader = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(reader);

                            while (true) {
                                String line = br.readLine();
                                resultJSON += line;
                                if (line == null) break;
                            }
                            br.close();
                            reader.close();
                            is.close();

                            // JSON파싱
                            JSONObject removeResultJSON = new JSONObject(resultJSON);

                            removeResult = removeResultJSON.getInt("removeResult");
                        }
                        conn.disconnect();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return removeResult;
                }
            };

            removeResult = asynctask.execute(Network.site + "schedule/removeSchedule").get();

        } catch(InterruptedException | ExecutionException | JSONException e) { e.printStackTrace(); }

        dialog.dismiss();
        return removeResult;

    }


    // 팀정보 가져오기
    public static Teams getTeamInfo(int tid) {
        Teams teamInfo = null;
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);
        try {
            AsyncTask<String, Void, Teams> asynctask = new AsyncTask<String, Void, Teams>() {
                @Override
                protected Teams doInBackground(String... params) {
                    String json = "";
                    Teams teamInfo = null;
                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(1000);
                        conn.connect();

                        // 요청보내고 정상응답이 왔는지 확인하기
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            // 정상응답인 경우 응답Http본문에서 일정 리스트 가져오기
                            InputStream is = conn.getInputStream();
                            Reader reader = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(reader);

                            while (true) {
                                String line = br.readLine();
                                json += line;
                                if (line == null) break;
                            }
                            br.close();
                            reader.close();
                            is.close();

                            // JSON파싱
                            JSONObject teamInfoJSON = new JSONObject(json);
                            teamInfo = new Teams();
                            teamInfo.setTid(teamInfoJSON.getInt("tid"));
                            teamInfo.setTname(teamInfoJSON.getString("tname"));
                            teamInfo.setMid(teamInfoJSON.getString("mid"));
                            if (!teamInfoJSON.isNull("tprofile")) {
                                teamInfo.setTprofile(teamInfoJSON.getString("tprofile"));
                            }
                            if (!teamInfoJSON.isNull("tdescr")) {
                                teamInfo.setTdescr(teamInfoJSON.getString("tdescr"));
                            }

                        }
                        conn.disconnect();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return teamInfo;
                }
            };

            teamInfo = asynctask.execute(Network.site + "/team/getTeamInfo?tid=" + tid).get();
        } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
        }

        dialog.dismiss();
        return teamInfo;
    }


    // 팀멤버리스트 가져오기
    public static List<Member> getTeamMembers(int tid, final TeamMembersListViewAdapter membersAdapter, final TextView txtNumMember) {
        List<Member> memberList = null;
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);
        Log.d("getTeamMembers", "1");
        try {
            AsyncTask<String, Void, List<Member>> asynctask = new AsyncTask<String, Void, List<Member>>() {
                @Override
                protected List<Member> doInBackground(String... params) {
                    String json = "";
                    List<Member> memberList = null;

                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(1000);
                        conn.connect();

                        // 요청보내고 정상응답이 왔는지 확인하기
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            Log.d("getTeamMembers", "2");
                            // 정상응답인 경우 응답Http본문에서 팀 멤버 리스트 가져오기
                            InputStream is = conn.getInputStream();
                            Reader reader = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(reader);

                            while (true) {
                                String line = br.readLine();
                                json += line;
                                if (line == null) break;
                            }
                            br.close();
                            reader.close();
                            is.close();

                            // JSON파싱
                            JSONArray teamMembersJSON = new JSONArray(json);

                            memberList = new ArrayList<>();

                            for (int i = 0; i < teamMembersJSON.length(); i++) {
                                JSONObject teamMemberJSON = teamMembersJSON.getJSONObject(i);
                                Member m = new Member();

                                m.setMid(teamMemberJSON.getString("mid"));
                                m.setMpwd(teamMemberJSON.getString("mpwd"));
                                if(!teamMemberJSON.isNull("mprofile")) { m.setMprofile(teamMemberJSON.getString("mprofile")); }
                                if(!teamMemberJSON.isNull("mbirth")) {
                                    String dateStr = teamMemberJSON.getString("mbirth");
                                    String[] dateArr = dateStr.split("-");
                                    java.sql.Date date = new java.sql.Date(Integer.parseInt(dateArr[0])-1900, Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]));
                                    m.setMbirth(date);
                                }

                                memberList.add(m);
                            }
                        }
                        Log.d("getTeamMembers", "3");
                        conn.disconnect();

                    } catch (IOException | JSONException e) {
                        Log.d("getTeamMembers", "4");
                        e.printStackTrace();
                    }
                    // 정상응답이면 list, 아니면 null반환
                    Log.d("getTeamMembers", "5");
                    return memberList;

                }

                @Override
                protected void onPostExecute(List<Member> memberList) {
                    // 팀 멤버 리스트 세팅

                    if(memberList != null) {
                        // 자신을 0번으로

                        for(int i=0; i < memberList.size(); i++) {
                            if(memberList.get(i).getMid().equals(MainActivity.member.getMid())) {

                                Member temp = memberList.get(i);
                                memberList.remove(i);
                                memberList.add(0, temp);

                            }

                        }

                        txtNumMember.setText(memberList.size()+"");

                        membersAdapter.setList(memberList);
                        membersAdapter.notifyDataSetChanged();
                    }
                }
            };

            // 네트워크작업 후 일정리스트 받아옴
            memberList = asynctask.execute(Network.site + "team/getTeamMembers?tid=" + tid).get();

        } catch(InterruptedException | ExecutionException e) { e.printStackTrace(); Log.d("getTeamMembers", "6");}

        // 정상응답이면 list, 아니면 null반환
        dialog.dismiss();
        return memberList;
    }




    // JSONObject -> Calendar에 시간 데이터를 넣어서 반환하기
    public static Calendar getTimeFromJSON(JSONObject json, String key) {

        Calendar time = null;

        try {
            // 공백으로 구분하여 변환
            String[] strArr = json.getString(key).split(" ");
            // 시간 관련 문자열을 더한 후 콜론으로 구분하여 변환
            String[] strArr2 = (strArr[3]+strArr[4]).split(":");

            // 시간만 달력에 저장
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(0, 0, 0, Integer.parseInt(strArr2[0]), Integer.parseInt(strArr2[1]));

        } catch (JSONException e) { e.printStackTrace(); }

        return time;
    }

    // "연-월-일" 형식의 문자열의 요일얻기
    public static String getDayOfWeek(String strDate) {

        String day = "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date nDate = dateFormat.parse(strDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);

            switch(dayNum) {
                case 1:
                    day = "일";
                    break ;
                case 2:
                    day = "월";
                    break ;
                case 3:
                    day = "화";
                    break ;
                case 4:
                    day = "수";
                    break ;
                case 5:
                    day = "목";
                    break ;
                case 6:
                    day = "금";
                    break ;
                case 7:
                    day = "토";
                    break ;
            }
        } catch(ParseException e) { e.printStackTrace(); }

        return day;
    }


    // "연-월-일"을 util.Date객체로 변경
    public static java.util.Date strToUtilDate(String dateStr) {
        Date date = null;

        try {
            String[] strArr = dateStr.split("-");
            date = new Date(Integer.parseInt(strArr[0])-1900, Integer.parseInt(strArr[1])-1, Integer.parseInt(strArr[2]));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return date;
    }


    // util.Date를 "연-월-일"로 변경
    public static String utilDateToStr(Date date) {
        String strDate = null;
        try {
            strDate = (date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" + date.getDate();
            Log.d("strDate", strDate);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    // 소식추가
    public static void addNews(final News news , final Bitmap bitmap, final File file, final android.support.v4.app.FragmentActivity activity){
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                    "잠시만 기다려 주세요 ...", true);
            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                String result = "fail";
                try {
                    // 데이터 구분 문자
                    String boundary = "----" + System.currentTimeMillis();

                    // 데이터 경계선
                    String delimiter = "\r\n--" + boundary + "\r\n";

                    // 커넥션 생성 및 설정
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(1000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    // 연결하기
                    conn.connect();

                    // 출력 스트림 얻기
                    OutputStream os = conn.getOutputStream();

                    // News 데이터를 문자열 데이터 전송
                    StringBuffer postDataBuilder = new StringBuffer();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String date = sdf.format(news.getNdate());
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("tid", String.valueOf(news.getTid()) ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mid", news.getMid() ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("ndate", date ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("nisnotice", String.valueOf(news.getNisnotice()) ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("ncontent", news.getNcontent() ));

                    Log.d("tid", String.valueOf(news.getTid()) );
                    Log.d("nisnotice", String.valueOf(news.getNisnotice()) );
                    Log.d("ncontent", news.getNcontent() + "#");

                    os.write(postDataBuilder.toString().getBytes());


                    // 이미지 데이터 전송
                    if(bitmap != null) {
                        StringBuffer imgDataBuilder = new StringBuffer();
                        imgDataBuilder.append(delimiter);
                        imgDataBuilder.append(setImage("nphotoname", news.getNphotoname() ));
                        os.write(imgDataBuilder.toString().getBytes());

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                        byte[] byteArray = new byte[1024];
                        int readByteNum = -1;
                        while ((readByteNum = bs.read(byteArray)) != -1) {
                            os.write(byteArray, 0, readByteNum);
                        }
                        bs.close();
                        bos.close();
                    }


                    // 파일 데이터 전송
                    if(file != null) {
                        StringBuffer fileDataBuilder = new StringBuffer();
                        fileDataBuilder.append(delimiter);
                        fileDataBuilder.append(setFile("nfilename", news.getNdataname() ));
                        os.write(fileDataBuilder.toString().getBytes());

                        FileInputStream fis = new FileInputStream(file);
                        byte[] byteArray = new byte[1024];
                        int readByteNum = -1;
                        while ((readByteNum = fis.read(byteArray)) != -1) {
                            os.write(byteArray, 0, readByteNum);
                        }
                        fis.close();
                    }


                    // 종료 구분자 넣기
                    os.write(("\r\n--" + boundary + "--\r\n").getBytes());


                    // 출력스트림 닫기
                    os.flush();


                    // 응답 코드 확인
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        /*InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            json +=line;
                        }
                        br.close(); reader.close(); is.close();*/
                    }


                    // 서버와 연결 끊기
                    conn.disconnect();


                    // 결과 json 파싱
                    /*JSONObject teamJson = new JSONObject(json);


                    // 성공
                    if(teamJson.getString("addteamresult").equals("success")){
                        return true;


                    } else {
                        // 실패
                        return false;
                    }*/
                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                // 소식추가 성공
                if(aBoolean) {
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamHomeFragment())
                            .commit();

                    MainActivity.presentFragment = "TeamHomeFragment";
                    activity.invalidateOptionsMenu();

                } else {
                    // 소식추가 실패
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
                    alt_bld.setMessage("소식 추가를 실패했습니다.").setCancelable(
                            false).setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Action for 'Yes' Button
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    // Title for AlertDialog
                    alert.setTitle("에러");
                    // Icon for AlertDialog
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.show();

                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamHomeFragment())
                            .commit();

                    MainActivity.presentFragment = "TeamHomeFragment";
                    activity.invalidateOptionsMenu();

                }
            }
        };

        asyncTask.execute(Network.site + "team/news/addNews");
    }

    public static String setValue(String key, String value) {
        String str = "Content-Disposition: form-data; name=\"" + key + "\"";
        str += "\r\n\r\n";
        str += value;
        return str;
    }

    public static String setImage(String key, String photoName) {
        String str = "Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + photoName + "\"";
        str += "\r\n";
        str += "Content-Type: image/png";
        str += "\r\n\r\n";
        return str;
    }

    public static String setFile(String key, String fileName) {
        String str = "Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"";
        str += "\r\n";
        str += "Content-Type: application/octet-stream";
        str += "\r\n\r\n";
        return str;
    }


    public static void withdrawFromTeam(final String mid, final int tid, final FragmentActivity activity)  {
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                    "잠시만 기다려 주세요 ...", true);
            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                String result = "fail";
                try {

                    // 커넥션 생성 및 설정
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(1000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);

                    String param = "mid="+ mid + "&tid=" + tid;

                    OutputStream os = conn.getOutputStream();

                    os.write(param.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // 응답 코드 확인
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            json +=line;
                        }

                        br.close(); reader.close(); is.close();

                        // 결과 json 파싱
                        JSONObject teamJson = new JSONObject(json);

                        // 성공
                        if(teamJson.getString("withdrawResult").equals("success")){
                            conn.disconnect();
                            return true;


                        } else {
                            // 실패
                            conn.disconnect();
                            return false;
                        }
                    } else {
                        // 응답이 비정상
                        conn.disconnect();
                        return false;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                // 탈퇴 성공
                if(aBoolean) {
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new HomeFragment())
                            .commit();

                    MainActivity.presentFragment = "HomeFragment";
                    activity.invalidateOptionsMenu();

                } else {
                    // 탈퇴 실패
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
                    alt_bld.setMessage("탈퇴에 실패했습니다.").setCancelable(
                            false).setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Action for 'Yes' Button
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    // Title for AlertDialog
                    alert.setTitle("에러");
                    // Icon for AlertDialog
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.show();

                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamInfoFragment())
                            .commit();
                }
            }
        };

        asyncTask.execute(Network.site + "team/withdrawFromTeam");
    }


    public static void transferAdmin(final String mid, final int tid, final FragmentActivity activity) {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);

        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                String result = "fail";
                try {

                    // 커넥션 생성 및 설정
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(1000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);

                    String param = "mid="+ mid + "&tid=" + tid;

                    OutputStream os = conn.getOutputStream();

                    os.write(param.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // 응답 코드 확인
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            json +=line;
                        }

                        br.close(); reader.close(); is.close();

                        // 결과 json 파싱
                        JSONObject teamJson = new JSONObject(json);

                        // 성공
                        if(teamJson.getString("withdrawResult").equals("success")){
                            conn.disconnect();
                            return true;


                        } else {
                            // 실패
                            conn.disconnect();
                            return false;
                        }
                    } else {
                        // 응답이 비정상
                        conn.disconnect();
                        return false;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                // 팀장임명 성공
                if(aBoolean) {
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamInfoFragment())
                            .commit();


                } else {
                    // 팀장임명 실패
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
                    alt_bld.setMessage("팀장임명을 실패했습니다.").setCancelable(
                            false).setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Action for 'Yes' Button
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    // Title for AlertDialog
                    alert.setTitle("에러");
                    // Icon for AlertDialog
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.show();

                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamInfoFragment())
                            .commit();
                }
            }

        };

        asyncTask.execute(Network.site + "team/transferAdmin");
    }

    public static boolean updateTeamInfo(final Teams teamInfo) {
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);

        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                String result = "fail";
                try {

                    // 커넥션 생성 및 설정
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(1000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);

                    // 서버로 보낼 데이터 세팅
                    JSONObject teamInfoJSON = new JSONObject();
                    teamInfoJSON.put("tid", teamInfo.getTid());
                    teamInfoJSON.put("mid", teamInfo.getMid());
                    teamInfoJSON.put("tname", teamInfo.getTname());
                    teamInfoJSON.put("tprofile", teamInfo.getTprofile());
                    teamInfoJSON.put("tdescr", teamInfo.getTdescr());

                    String param = "teamInfo=" + teamInfoJSON.toString();

                    OutputStream os = conn.getOutputStream();

                    os.write(param.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // 응답 코드 확인
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            json +=line;
                        }

                        br.close(); reader.close(); is.close();

                        // 결과 json 파싱
                        JSONObject teamJson = new JSONObject(json);

                        // 성공
                        if(teamJson.getString("updateResult").equals("success")){
                            conn.disconnect();
                            return true;


                        } else {
                            // 실패
                            conn.disconnect();
                            return false;
                        }
                    } else {
                        // 응답이 비정상
                        conn.disconnect();
                        return false;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        dialog.dismiss();

        try {
            return asyncTask.execute(Network.site + "team/updateTeamInfo").get();
        } catch(InterruptedException | ExecutionException e) {
            return false;
        }
    }

    public static boolean updateProfile(final String tprofile, final Bitmap bitmap) {
        ProgressDialog dialog = ProgressDialog.show(MainActivity.getInstance(), "",
                "잠시만 기다려 주세요 ...", true);
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                String result = "fail";
                try {
                    // 데이터 구분 문자
                    String boundary = "----" + System.currentTimeMillis();

                    // 데이터 경계선
                    String delimiter = "\r\n--" + boundary + "\r\n";

                    // 커넥션 생성 및 설정
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    // 연결하기
                    conn.connect();

                    // 출력 스트림 얻기
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

                    // 이미지 데이터 전송
                    if(bitmap != null) {
                        StringBuffer imgDataBuilder = new StringBuffer();
                        imgDataBuilder.append(delimiter);
                        imgDataBuilder.append(setImage("tprofile", tprofile ));
                        out.writeUTF(imgDataBuilder.toString());

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                        byte[] byteArray = new byte[1024];
                        int readByteNum = -1;
                        while ((readByteNum = bs.read(byteArray)) != -1) {
                            out.write(byteArray, 0, readByteNum);
                        }
                        bs.close();
                        bos.close();
                    }

                    // 종료 구분자 넣기
                    out.writeUTF("\r\n--" + boundary + "--\r\n");


                    // 출력스트림 닫기
                    out.flush();
                    out.close();


                    // 응답 코드 확인
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        /*InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            json +=line;
                        }
                        br.close(); reader.close(); is.close();*/
                    }


                    // 서버와 연결 끊기
                    conn.disconnect();


                    // 결과 json 파싱
                    JSONObject teamJson = new JSONObject(json);


                    // 성공
                    if(teamJson.getString("updateResult").equals("success")){
                        return true;


                    } else {
                        // 실패
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        };

        dialog.dismiss();
        try {
            return asyncTask.execute(Network.site + "team/updateTprofileFile").get();
        } catch(Exception e) { e.printStackTrace(); return false; }
    }
}
