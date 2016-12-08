package com.example.myapplication.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.actionbar.search.SearchItemViewAdapter;
import com.example.myapplication.dto.News;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.home.CircleBitmap;
import com.example.myapplication.home.ItemViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016-07-07.
 */
public class Network2 {
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

    public static void addNews(final News news , final Bitmap bitmap, final File file, final android.support.v4.app.FragmentActivity activity){
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

                    //연결하기
                    conn.connect();

                    //출력 스트림 얻기
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

                    //News 데이터를 문자열 데이터 전송
                    StringBuffer postDataBuilder = new StringBuffer();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String date = sdf.format(news.getNdate());
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("nid", String.valueOf(news.getNid()) ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("tid", String.valueOf(news.getTid()) ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mid", news.getMid() ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("ndate", date ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("ncontent", news.getNcontent() ));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("nisnotice", String.valueOf(news.getNisnotice()) ));

                    if(bitmap != null) {

                    }
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setImage("nphotoname", news.getNphotoname() ));




                    //    postDataBuilder.append(setFile("tprofile", team.getTprofile()));
                    out.writeUTF(postDataBuilder.toString());

                    //이미지 데이터 전송
                    if(bitmap != null) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                        byte[] byteArray = new byte[1024];
                        int readByteNum = -1;
                        while ((readByteNum = bs.read(byteArray)) != -1) {
                            out.write(byteArray, 0, readByteNum);
                        }
                        bs.close();
                    }
                    //setFile 작성해야함
                   /* postDataBuilder.append(setFile("ndataname", String.valueOf(news.getNdataname()) ));
                    postDataBuilder.append(delimiter);*/


                    //종료 구분자 넣기
                    out.writeUTF("\r\n--" + boundary + "--\r\n");

                    //출력스트림 닫기
                    out.flush();
                    out.close();

                    //응답 코드 확인
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                       /* InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            json +=line;
                        }
                        br.close(); reader.close(); is.close();*/
                    }
                    //연결 끊기
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                // 응답http json 파싱
               /* try {
                    JSONObject teamJson = new JSONObject(json);

                    // 팀생성 성공시
                    if(teamJson.getString("addteamresult").equals("success")){
                        return true;
                    } else {
                        // login 실패시
                        return false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }*/
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){

                }else{

                }
            }
        };
        String url = Network.site + "team/news/addNews";
        asyncTask.execute(url);
    }
    public static void setMemberToTeam(String mid, int tid){
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try{
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    return false;
                }catch(Exception e){

                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){

                }
            }
        };
        String url = Network.site + "member/joinTeam?mid="+ mid +"&tid="+tid;
        asyncTask.execute(url);
    }
    //서버로부터, 지정한 mid가 가입된 팀리스트 불러오기 (home화면)
    public static void getTeamListByMid(String mid, final Fragment fragment, final ItemViewAdapter itemViewAdapter){

        AsyncTask<String, Void, List<Teams>> asyncTask = new AsyncTask<String, Void, List<Teams>>() {
            @Override
            protected List<Teams> doInBackground(String... params) {
                //mid에 의한 team list 조회 네트워킹
                List<Teams> list = new ArrayList<Teams>();
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();

                        byte[] bytes = new byte[10240];
                        byte data;
                        int index = 0;
                        while (true) {
                            data = (byte) is.read();
                            if (data == -1) {
                                break;
                            }
                            bytes[index] = data;
                            index++;
                        }
                        json = new String(bytes);


                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }


                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // getList 성공시 team 세팅?
                    if (root.getString("result").equals("success")) {
                        // Date 타입 파싱

                        JSONArray jsonarray = root.getJSONArray("list");
                        for(int i = 0 ; i < jsonarray.length(); i++){
                            JSONObject jsonObject = jsonarray.getJSONObject(i);
                            Teams team = new Teams();
                            team.setTid( Integer.parseInt( jsonObject.getString("tid") ) );
                            team.setTname( jsonObject.getString("tname") );
                            team.setTdescr( jsonObject.getString("tdescr") );
                            team.setMid( jsonObject.getString("mid") );
                            team.setTprofile( jsonObject.getString("tprofile") );
                            list.add(team);
                        }
                    }
                    Teams addteam = new Teams();
                    addteam.setTid( 0 );
                    addteam.setTname( "새 팀 추가");
                    addteam.setTdescr( "팀을 새로 생성 합니다" );
                    addteam.setMid("" );
                    addteam.setTprofile( "" );
                    list.add(addteam);
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return list;
                }
            }

            @Override
            protected void onPostExecute(List<Teams> list) {
                //list homefragment에 출력
                GridView gridView = (GridView) fragment.getView().findViewById(R.id.gridView);
                itemViewAdapter.setList(list);
                gridView.setAdapter(itemViewAdapter);
            }
        };
        asyncTask.execute(Network.site+"team/list?mid="+ mid);
    }
    // 팀명, 팀소개, 팀장id에서 keyword가 포함된 팀리스트 조회 (search화면)
    public static void getTeamListByKeyword(String mid, String keyword, final Fragment fragment, final SearchItemViewAdapter itemViewAdapter){


        AsyncTask<String, Void, List<Teams>> asyncTask = new AsyncTask<String, Void, List<Teams>>() {
            @Override
            protected List<Teams> doInBackground(String... params) {
                //mid에 의한 team list 조회 네트워킹
                // 로그인을 위한 네트워킹
                List<Teams> list = new ArrayList<Teams>();
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();

                        byte[] bytes = new byte[10240];
                        byte data;
                        int index = 0;
                        while (true) {
                            data = (byte) is.read();
                            if (data == -1) {
                                break;
                            }
                            bytes[index] = data;
                            index++;
                        }
                        json = new String(bytes);


                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }


                // 응답http json 파싱
                try {

                    JSONObject root = new JSONObject(json);

                    // getList 성공시 team 세팅?
                    if (root.getString("result").equals("success")) {
                        // Date 타입 파싱

                        JSONArray jsonarray = root.getJSONArray("list");

                        for(int i = 0 ; i < jsonarray.length(); i++){
                            JSONObject jsonObject = jsonarray.getJSONObject(i);

                            Teams team = new Teams();

                            team.setTid( Integer.parseInt( jsonObject.getString("tid") ) );
                            team.setTname( jsonObject.getString("tname") );
                            team.setTdescr( jsonObject.getString("tdescr") );
                            team.setMid( jsonObject.getString("mid") );
                            team.setTprofile( jsonObject.getString("tprofile") );
                            team.setTnum( Integer.parseInt( jsonObject.getString("tnum") ) );
                            team.setIsJoin( Integer.parseInt( jsonObject.getString("isJoin")) );
                            list.add(team);

                        }
                    }

                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return list;
                }
            }

            @Override
            protected void onPostExecute(List<Teams> list) {
                //list searchfragment에 출력
                ListView listView = (ListView) fragment.getView().findViewById(R.id.searchListView);
                itemViewAdapter.setList(list);
                itemViewAdapter.notifyDataSetChanged();


            }
        };
        asyncTask.execute(Network.site+"team/search?keyword="+ keyword+"&mid=" + mid);
    }
    public static void getMember(final String id, final String pwd) {
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        /*InputStream is = conn.getInputStream();

                        byte[] bytes = new byte[1024];
                        byte data;
                        int index=0;
                        while(true) {
                            data = (byte)is.read();
                            if(data==-1) {
                                break;
                            }
                            bytes[index] = data;
                            index++;
                        }
                        json=new String(bytes);


                        is.close();*/
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 응답http json 파싱
                /*try {
                    JSONObject memberJson = new JSONObject(json);

                    // login 성공시 member 세팅?
                    if(memberJson.getString("loginresult").equals("success")){
                        // Date 타입 파싱
                        String[] dateArr =  memberJson.getString("mbirth").split("-");
                        Date mbirth = new Date(Integer.parseInt(dateArr[0])-1900, Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]));

                        MainActivity.member = new Member();
                        MainActivity.member.setMid(memberJson.getString("mid"));
                        MainActivity.member.setMbirth(mbirth);
                        MainActivity.member.setMprofile(memberJson.getString("mprofile"));
                        MainActivity.member.setMpwd(memberJson.getString("mpwd"));

                        return true;


                    } else {
                        // login 실패시
                        return false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }*/
                return json;
            }

            @Override
            protected void onPostExecute(String json) {

            }
        };
        asyncTask.execute(Network.site+"login?mid="+ id +"&mpwd=" + pwd);
    }


    //image 서버에서 다운받기
    public static void getBitmap(final String imageName , final ImageView imageView){

        AsyncTask<String, Void, Bitmap> asyncTask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap result = null;
                if(imageName.length() < 1){
                    return result;
                }
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream is = conn.getInputStream();

                        result = BitmapFactory.decodeStream(is);
                        is.close();
                    }
                    conn.disconnect();
                }catch (Exception e){
                    e.printStackTrace();

                }
                return result;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {  //메인스레드가 실행
                //db상에 이미지 파일명이 존재하나 서버에 해당하는 이름의 파일이 존재하지 않을 때 기본를 출력하기 위한 코드
                int width = 150;
                CircleBitmap make = new CircleBitmap();
                if(bitmap == null){
                    if(imageView.getId() == R.id.team_grid_imageView || imageView.getId() == R.id.team_search_imageView){
                        //팀기본이미지
                        bitmap =  BitmapFactory.decodeResource( imageView.getResources(),R.drawable.base_team);
                    }else{
                        //사람기본이미지
                        bitmap =  BitmapFactory.decodeResource( imageView.getResources(),R.drawable.base_person);
                    }


                }else{

                }
               /* bitmap = make.getCircleBitmap(bitmap, width);*/
                bitmap = make.createFramedPhoto(bitmap, width);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(bitmap);
                //simageView.setAdjustViewBounds(true);

            }
        };
        String url = Network.site + "download/image?imagename="+ imageName;

        asyncTask.execute(url);
    }

}
