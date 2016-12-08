package com.example.myapplication.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Member;
import com.example.myapplication.home.HomeFragment;
import com.example.myapplication.join.JoinFragment;
import com.example.myapplication.login.LoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Administrator on 2016-07-07.
 */
public class Network4 {

    public static void join(final String id, final android.support.v4.app.Fragment fragment) {
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
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
                    conn.disconnect();
                    Log.i("mylog",json);


                }catch(Exception e){
                    e.printStackTrace();
                }

                try{
                    JSONObject jo  = new JSONObject(json);

                    if(jo.getString("dupCheck").equals("success")){
                        return true;
                    }else{
                        return false;
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Button btnDupCheck = (Button)fragment.getView().findViewById(R.id.btnDupCheck);
                EditText txtId = (EditText)fragment.getView().findViewById(R.id.txtId);
                EditText txtPassword1 = (EditText)fragment.getView().findViewById(R.id.txtPassword1);
                TextView txtDupCheck = (TextView) fragment.getView().findViewById(R.id.txtDupCheck);
                if(aBoolean == true){
                    JoinFragment.dupcheck = true;
                    btnDupCheck.setText("가능");
                    txtPassword1.requestFocus();
                }else{
                    JoinFragment.dupcheck = false;
                    txtDupCheck.setText("*해당 아이디는 중복됩니다*");
                    txtId.setText("");
                }
            }
        };

        asyncTask.execute(Network.site+"member/duplicate?mid="+id);
    }

    public static void join2(final Member member, final android.support.v4.app.Fragment fragment, final Bitmap bitmap){
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                try {
                    // 데이터 구분 문자
                    String boundary = "----" + System.currentTimeMillis();

                    // 데이터 경계선
                    String delimiter = "\r\n--" + boundary + "\r\n";

                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection","Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


                    conn.connect();

                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

                    //문자열 데이터 전송
                    StringBuffer postDataBuilder = new StringBuffer();
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mid",member.getMid()));

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mpwd",member.getMpwd()));

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mbirthday",member.getMbirth().toString()));

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setFile("mprofile",member.getMprofile()));

                    out.writeUTF(postDataBuilder.toString());

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                    byte[] byteArray = new byte[1024];
                    int readByteNum = -1;
                    while((readByteNum = bs.read(byteArray)) != -1) {
                        out.write(byteArray, 0, readByteNum);
                    }
                    bs.close();

                    out.writeUTF("\r\n--"+boundary+"--\r\n");
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
                        Log.i("mylog",json);
                    }
                    //연결 끊기
                    conn.disconnect();
                }catch(Exception e){
                    e.printStackTrace();
                }

                try{
                    JSONObject jo  = new JSONObject(json);

                    if(jo.getString("joinResult").equals("success")){
                        return true;
                    }else{
                        return false;
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean result) {
                if(result){
                            fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .commit();
                }else{
                    Log.i("mylog","fail");
                }
            }
        };

        asyncTask.execute(Network.site+"member/join");
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

    public static void revise(final Member member, final android.support.v4.app.Fragment fragment, final Bitmap bitmap){
        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                String json = "";
                try {
                    // 데이터 구분 문자
                    String boundary = "----" + System.currentTimeMillis();

                    // 데이터 경계선
                    String delimiter = "\r\n--" + boundary + "\r\n";

                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection","Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


                    conn.connect();

                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

                    //문자열 데이터 전송
                    StringBuffer postDataBuilder = new StringBuffer();

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mid",member.getMid()));

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setValue("mpwd",member.getMpwd()));

                    postDataBuilder.append(delimiter);
                    postDataBuilder.append(setFile("mprofile",member.getMprofile()));

                    out.writeUTF(postDataBuilder.toString());

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                    byte[] byteArray = new byte[1024];
                    int readByteNum = -1;
                    while((readByteNum = bs.read(byteArray)) != -1) {
                        out.write(byteArray, 0, readByteNum);
                    }
                    bs.close();

                    out.writeUTF("\r\n--"+boundary+"--\r\n");
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
                        Log.i("mylog",json);
                    }
                    //연결 끊기
                    conn.disconnect();
                }catch(Exception e){
                    e.printStackTrace();
                }

                try{
                    JSONObject jo  = new JSONObject(json);

                    if(jo.getString("reviseResult").equals("success")){
                        return true;
                    }else{
                        return false;
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean result) {
                if(result){
                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new HomeFragment())
                            .commit();

                    ImageView profile = (ImageView)fragment.getActivity().findViewById(R.id.navProfile);
                    Network5.getBitmap(MainActivity.member.getMprofile(),profile);
                }else{
                    Log.i("mylog","fail");
                }
            }
        };

        asyncTask.execute(Network.site+"member/revise");
    }

    public static void out(final Member member){
        AsyncTask<String,Void,Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                String json="";
                try{

                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);

                    String param = "mid="+member.getMid();

                    OutputStream os = conn.getOutputStream();
                    //Writer writer = new OutputStreamWriter(os);

                    os.write(param.getBytes("UTF-8"));
                    //writer.write(param);
                    os.flush();
                    os.close();

                    conn.getResponseCode();


                }catch(Exception e){
                    e.printStackTrace();

                }

                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {

            }
        };

        asyncTask.execute(Network.site+"member/out");

    }
}

















