package com.example.myapplication.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.home.CircleBitmap;
import com.example.myapplication.team.TeamHomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016-07-08.
 */
public class Network5 {
    public static String BASE_URL = Network.site;

    public static void addteam(final Teams team, final Bitmap bitmap, final Context context, final android.support.v4.app.Fragment fragment) {
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            // new AsyncTask<Void, Integer, String>() {
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
                    URL url = new URL(BASE_URL + "team/addteam");
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

                    //문자열 데이터 전송
                    StringBuffer postDataBuilder = new StringBuffer();

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("tname", team.getTname()));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("tdescr", team.getTdescr()));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mid", MainActivity.member.getMid()));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setFile("tprofile", team.getTprofile()));
                    out.writeUTF(postDataBuilder.toString());

                    //파일 데이터 전송
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

                    //종료 구분자 넣기
                    out.writeUTF("\r\n--" + boundary + "--\r\n");

                    //출력스트림 닫기
                    out.flush();
                    out.close();

                    //응답 코드 확인
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
                    }
                    //연결 끊기
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                // 응답http json 파싱
                try {
                    JSONObject teamJson = new JSONObject(json);

                    // 팀생성 성공시
                    if(teamJson.getString("addteamresult").equals("success")){
                        int tid = teamJson.getInt("tid");
                        MainActivity.tid = tid;
                        Log.i("mylog",String.valueOf(tid));
                        return true;
                    } else {
                        // login 실패시
                        return false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("팀생성하기");

                    // Setting Dialog Message
                    builder.setMessage("팀생성 성공");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamHomeFragment())
                            .commit();
                    MainActivity.presentFragment = "TeamHomeFragment";
                    fragment.getActivity().invalidateOptionsMenu();
                }else{

                }
            }
        };
        asyncTask.execute();
    }

    //image 서버에서 다운받기
    public static void getBitmap(final String imageName , final ImageView btnProfile){

        AsyncTask<String, Void, Bitmap> asyncTask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap result = null;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream is = conn.getInputStream();

                        result = BitmapFactory.decodeStream(is);
                        is.close();
                    }else{
                        result = null;
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
                CircleBitmap make = new CircleBitmap();
                if(bitmap == null){
                        if(btnProfile.getId() == R.id.team_grid_imageView || btnProfile.getId() == R.id.team_search_imageView){
                            //팀기본이미지
                            bitmap =  BitmapFactory.decodeResource( btnProfile.getResources(),R.drawable.base_team);
                        }else{
                            //사람기본이미지
                            bitmap =  BitmapFactory.decodeResource( btnProfile.getResources(),R.drawable.join_base_person);
                        }


                }else{

                }
                btnProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                btnProfile.setImageBitmap(bitmap);

                if(MainActivity.navProfile != null) {
                    MainActivity.navProfile.setImageBitmap(bitmap);
                }

                //btnProfile.setAdjustViewBounds(true);

            }
        };
        String url = Network.site + "memberdown/image?imagename="+ imageName;

        asyncTask.execute(url);
    }

    public static void memberInfo(final Teams team, final Bitmap bitmap, final Context context, final android.support.v4.app.Fragment fragment) {
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            // new AsyncTask<Void, Integer, String>() {
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
                    URL url = new URL(BASE_URL + "/addteam");
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

                    //문자열 데이터 전송
                    StringBuffer postDataBuilder = new StringBuffer();

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("tname", team.getTname()));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("tdescr", team.getTdescr()));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mid", MainActivity.member.getMid()));
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setFile("tprofile", team.getTprofile()));
                    out.writeUTF(postDataBuilder.toString());

                    //파일 데이터 전송
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

                    //종료 구분자 넣기
                    out.writeUTF("\r\n--" + boundary + "--\r\n");

                    //출력스트림 닫기
                    out.flush();
                    out.close();

                    //응답 코드 확인
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
                    }
                    //연결 끊기
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                // 응답http json 파싱
                try {
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
                }

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("팀생성하기");

                    // Setting Dialog Message
                    builder.setMessage("팀생성 성공");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamHomeFragment())
                            .commit();
                }else{

                }
            }
        };
        asyncTask.execute();
    }
    public static String setValue(String key, String value) {
        String str = "Content-Disposition: form-data; name=\"" + key + "\"";
        str += "\r\n\r\n";
        str += value;
        return str;
    }

    public static String setFile(String key, String fileName) {
        String str = "Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"";
        str += "\r\n";
        str += "Content-Type: image/png";
        str += "\r\n\r\n";
        return str;
    }
}
