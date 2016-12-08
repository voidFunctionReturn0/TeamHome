package com.example.myapplication.network;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Member;
import com.example.myapplication.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

/**
 * Created by Administrator on 2016-07-08.
 */
public class MemberNetwork {

    // id || pw 틀리면 null리턴
    public static void login(final String id, final String pwd, final android.support.v4.app.Fragment fragment) {
        final boolean isSuccess;

        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                // 로그인을 위한 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();

                        // 응답http 본문 데이터(결과 데이터) 받기
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

                        // 응답http json 파싱
                        JSONObject memberJson = new JSONObject(json);

                        // login 성공
                        if(memberJson.getString("loginresult").equals("success")) {

                            // sql.Date타입 JSON파싱
                            if(memberJson.isNull("mbirth")) {
                                Date mbirth = getDateFromJSON(memberJson, "mbirth");
                                MainActivity.member.setMbirth(mbirth);
                                Log.d("mbirth", mbirth.toString());
                            } else {
                                Log.d("mbirth", memberJson.isNull("mbirth")+"");
                            }

                            // 나머지 member 필드 세팅
                            MainActivity.member = new Member();
                            MainActivity.member.setMid(memberJson.getString("mid"));
                            MainActivity.member.setMpwd(memberJson.getString("mpwd"));
                            MainActivity.member.setMprofile(memberJson.getString("mprofile"));

                            is.close();

                            conn.disconnect();
                            return true;

                        } else {
                            // login 실패
                            conn.disconnect();
                            return false;
                        }
                    } else {
                        // 응답상태가 정상이 아님
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
                if(aBoolean){
                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new HomeFragment())
                            .commit();

                    MainActivity.presentFragment = "HomeFragment";
                    fragment.getActivity().invalidateOptionsMenu();
                    ImageView profile = (ImageView)fragment.getActivity().findViewById(R.id.navProfile);
                    Network5.getBitmap(MainActivity.member.getMprofile(),profile);
                    TextView textView = (TextView)fragment.getActivity().findViewById(R.id.textView7);
                    textView.setText(MainActivity.member.getMid());
                    MainActivity.navProfile = profile;

                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("로그인 성공");

                    // Setting Dialog Message
                    builder.setMessage("환영합니다.");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("로그인 실패");

                    // Setting Dialog Message
                    builder.setMessage("아이디 혹은 비밀번호가 틀렸습니다.");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EditText txtId =(EditText)fragment.getView().findViewById(R.id.txtId);
                            EditText txtPwd =(EditText)fragment.getView().findViewById(R.id.txtPwd);
                            txtId.setText("");
                            txtPwd.setText("");
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };

        asyncTask.execute(Network.site+"member/login?mid="+ id +"&mpwd=" + pwd);
    }

    // JSONObject -> sql.Date 타입 파싱
    public static Date getDateFromJSON(JSONObject memberJSON, String key) {
        Date date = null;
        try {
            String[] dateArr = memberJSON.getString(key).split("-");
            date = new Date(Integer.parseInt(dateArr[0]) - 1900, Integer.parseInt(dateArr[1]) - 1, Integer.parseInt(dateArr[2]));

        } catch(JSONException e) { e.printStackTrace(); }

        return date;
    }
}
