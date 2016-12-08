package com.example.myapplication.network;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.News;
import com.example.myapplication.dto.Newscomment;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.team.TeamHomeFragment;
import com.example.myapplication.team.data.DataItemViewAdapter;
import com.example.myapplication.team.news.CommentItemViewAdapter;
import com.example.myapplication.team.news.DetailNewsFragment;
import com.example.myapplication.team.news.NewsItemViewAdapter;
import com.example.myapplication.team.photo.PhotoItemViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-07-08.
 */
public class Network1 {


    //파일 다운로드
    public static void downloadData(String dataName, final Context context) {
        final String finalDataName = dataName;
        AsyncTask<String, String, Boolean> asynctask = new AsyncTask<String, String, Boolean>() {

            private ProgressDialog pDlg;
            @Override
            protected void onPreExecute() {
                pDlg = new ProgressDialog(context);
                pDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDlg.setMessage("Start");
                pDlg.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                int count=0;
                try{
                    Thread.sleep(100);
                    URL url = new URL(params[0]);
                    HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                    int lenghtOfFile = conn.getContentLength();
                    conn.connect();

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();

                        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), finalDataName);
                        OutputStream os = new FileOutputStream(file);
                        byte data[] = new byte[1024];
                        long total = 0;
                        while ((count = is.read(data)) != -1) {
                            Thread.sleep(10);
                            total += count;
                            publishProgress("progress","" + (int) ((total * 100) / lenghtOfFile), "다운로드 중...");
                            os.write(data, 0, count);
                        }
                        publishProgress("max","" + (int) ((total * 100) / lenghtOfFile), "다운로드 완료");
                        os.flush();

                        os.close();
                        is.close();
                        conn.disconnect();
                        return true;
                    }
                    conn.disconnect();
                }catch (Exception e) { e.printStackTrace(); return false;}
                return false;
            }


            @Override
            protected void onProgressUpdate(String... progress) {
                if (progress[0].equals("progress")) {
                    pDlg.setProgress(Integer.parseInt(progress[1]));
                    pDlg.setMessage(progress[2]);
                } else if (progress[0].equals("max")) {
                    pDlg.setMax(Integer.parseInt(progress[1]));
                    pDlg.setMessage(progress[2]);
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean) {
                    pDlg.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    builder.setTitle("Data Dialog");

                    // Setting Dialog Message
                    builder.setMessage("파일 다운로드 성공");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else {
                    pDlg.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    builder.setTitle("Photo Dialog");

                    // Setting Dialog Message
                    builder.setMessage("파일 다운로드 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };
        try {
            dataName = URLEncoder.encode(dataName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        asynctask.execute(Network.site+"team/data/download?dataName="+dataName);
    }

    //사진 다운로드
    public static void downloadPhoto(String photoName, final Context context) {
        final String finalPhotoName = photoName;
        AsyncTask<String, String, Boolean> asynctask = new AsyncTask<String, String, Boolean>() {

            private ProgressDialog pDlg;
            @Override
            protected void onPreExecute() {
                pDlg = new ProgressDialog(context);
                pDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDlg.setMessage("Start");
                pDlg.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                int count=0;
                try{
                    Thread.sleep(100);
                    URL url = new URL(params[0]);
                    HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                    int lenghtOfFile = conn.getContentLength();
                    conn.connect();

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        InputStream is = conn.getInputStream();
                        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), finalPhotoName);

                        OutputStream os = new FileOutputStream(file);
                        byte data[] = new byte[1024];
                        long total = 0;
                        while ((count = is.read(data)) != -1) {
                            Thread.sleep(10);
                            total += count;
                            publishProgress("progress","" + (int) ((total * 100) / lenghtOfFile), "다운로드 중...");
                            os.write(data, 0, count);
                        }
                        publishProgress("max","" + (int) ((total * 100) / lenghtOfFile), "다운로드 완료!!");
                        os.flush();

                        os.close();
                        is.close();
                        conn.disconnect();
                        return true;
                    }
                    conn.disconnect();
                }catch (Exception e) { e.printStackTrace(); return false;}
                return false;
            }


            @Override
            protected void onProgressUpdate(String... progress) {
                if (progress[0].equals("progress")) {
                    pDlg.setProgress(Integer.parseInt(progress[1]));
                    pDlg.setMessage(progress[2]);
                } else if (progress[0].equals("max")) {
                    pDlg.setMax(Integer.parseInt(progress[1]));
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean) {
                    pDlg.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    builder.setTitle("Photo Dialog");

                    // Setting Dialog Message
                    builder.setMessage("사진 다운로드 성공");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else {
                    pDlg.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    builder.setTitle("Photo Dialog");

                    // Setting Dialog Message
                    builder.setMessage("사진 다운로드 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };
        try {
            photoName = URLEncoder.encode(photoName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        asynctask.execute(Network.site+"team/photo/download?photoName="+photoName);
    }



    //앨범 리스트 가져오기
    public static void getPhotoList(int tid, final android.support.v4.app.FragmentActivity activity, final PhotoItemViewAdapter photoItemViewAdapter) {
        final List<News> photoList = new ArrayList<>();


        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {

                String json="";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    //통신 성공시
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) {
                                break;
                            }
                            json += line;

                        }
                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();
                }catch (Exception e) { e.printStackTrace(); return false;}

                try {
                    //응답Http 파싱
                    JSONObject root = new JSONObject(json);
                    if(root.getString("getPhotoListResult").equals("success")) {
                        JSONArray photoArray=new JSONArray(root.getString("photoList"));
                        for(int i=0; i<photoArray.length(); i++) {
                            JSONObject photoJson = photoArray.getJSONObject(i);
                            News news = new News();

                            news.setNid(photoJson.getInt("nid"));
                            news.setMid(photoJson.getString("mid"));
                            news.setMprofile(photoJson.getString("mprofile"));

                            //String newsDate = newsJson.getString("ndate");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = sdf.parse(photoJson.getString("ndate"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            news.setNdate(date);
                            news.setNcontent(photoJson.getString("ncontent"));
                            news.setNphotoname(photoJson.getString("nphotoname"));
                            news.setNdataname(photoJson.getString("ndataname"));
                            news.setNisnotice(photoJson.getInt("nisnotice"));
                            news.setTid(photoJson.getInt("tid"));
                            photoList.add(news);
                        }
                        return true;
                    } else {
                        //통신 실패시
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }


            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean) {
                    photoItemViewAdapter.setList(photoList);
                    photoItemViewAdapter.notifyDataSetChanged();
                }else {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    // Setting Dialog Title
                    builder.setTitle("Photo Dialog");

                    // Setting Dialog Message
                    builder.setMessage("사진 불러오기 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();*/
                }
            }
        };

        asyncTask.execute(Network.site+"team/photo/list?tid="+tid);

    }

    //자료 리스트 가져오기
    public static void getDataList(int tid, final android.support.v4.app.FragmentActivity activity, final DataItemViewAdapter dataItemViewAdapter) {
        final List<News> dataList = new ArrayList<>();


        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {

                String json="";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    //통신 성공시
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) {
                                break;
                            }
                            json += line;

                        }
                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();
                }catch (Exception e) { e.printStackTrace(); return false;}

                try {
                    //응답Http 파싱
                    JSONObject root = new JSONObject(json);
                    if(root.getString("getDataListResult").equals("success")) {
                        JSONArray dataArray=new JSONArray(root.getString("dataList"));
                        for(int i=0; i<dataArray.length(); i++) {
                            JSONObject dataJson = dataArray.getJSONObject(i);
                            News news = new News();

                            news.setNid(dataJson.getInt("nid"));
                            news.setMid(dataJson.getString("mid"));
                            news.setMprofile(dataJson.getString("mprofile"));

                            //String newsDate = newsJson.getString("ndate");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = sdf.parse(dataJson.getString("ndate"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            news.setNdate(date);
                            news.setNcontent(dataJson.getString("ncontent"));
                            news.setNphotoname(dataJson.getString("nphotoname"));
                            news.setNdataname(dataJson.getString("ndataname"));
                            news.setNisnotice(dataJson.getInt("nisnotice"));
                            news.setTid(dataJson.getInt("tid"));

                            dataList.add(news);
                        }
                        return true;
                    } else {
                        //통신 실패시
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }


            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean) {

                    dataItemViewAdapter.setList(dataList);
                    dataItemViewAdapter.notifyDataSetChanged();
                }else {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);


                    // Setting Dialog Title
                    builder.setTitle("Data Dialog");

                    // Setting Dialog Message
                    builder.setMessage("자료 불러오기 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();*/
                }
            }
        };
        asyncTask.execute(Network.site+"team/data/list?tid="+tid);

    }

    //소식 리스트 가져오기
    public static void getNewsList(final int tid, final android.support.v4.app.FragmentActivity activity, final NewsItemViewAdapter newsItemViewAdapter) {
        final List<News> newsList = new ArrayList<>();

        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                // 소식리스트 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                //Log.i("mylog", json);

                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // 통신 성공시 newsList세팅
                    if(root.getString("newsResult").equals("success")){
                        JSONArray newsJsonArray = new JSONArray(root.getString("list"));


                        for(int i=0; i<newsJsonArray.length(); i++) {

                            News news = new News();
                            JSONObject newsJson = newsJsonArray.getJSONObject(i);

                            news.setNid(newsJson.getInt("nid"));
                            news.setMid(newsJson.getString("mid"));
                            news.setMprofile(newsJson.getString("mprofile"));


                            //String newsDate = newsJson.getString("ndate");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = sdf.parse(newsJson.getString("ndate"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            news.setNdate(date);
                            news.setNcontent(newsJson.getString("ncontent"));
                            news.setNphotoname(newsJson.getString("nphotoname"));
                            news.setNdataname(newsJson.getString("ndataname"));
                            news.setNisnotice(newsJson.getInt("nisnotice"));
                            news.setTid(newsJson.getInt("tid"));


                            newsList.add(news);
                        }
                        return true;


                    } else {
                        // 통신 실패시
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
                    newsItemViewAdapter.setList(newsList);
                    newsItemViewAdapter.notifyDataSetChanged();
                }else{
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    // Setting Dialog Title
                    builder.setTitle("News Dialog");

                    // Setting Dialog Message
                    builder.setMessage("소식 불러오기 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();*/
                }
            }
        };

        asyncTask.execute(Network.site+"team/news/list?tid="+ tid);

    }

    //소식 선택 시 소식 상세화면에서의 news객체
    public static void getNews(int nid, final android.support.v4.app.FragmentActivity activity) {
        final News news = new News();

        AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String json="";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while(true) {
                            String line = br.readLine();
                            if(line==null) {break;}
                            json += line;
                        }
                        br.close();
                        reader.close();
                        is.close();

                        //응답Http Json 파싱
                        JSONObject root = new JSONObject(json);
                        if(root.getString("newsResult").equals("success")) {
                            JSONObject newsJson = new JSONObject(root.getString("list"));
                            news.setNid(newsJson.getInt("nid"));
                            news.setTid(newsJson.getInt("tid"));
                            news.setMid(newsJson.getString("mid"));
                            news.setMprofile(newsJson.getString("mprofile"));

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = sdf.parse(newsJson.getString("ndate"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            news.setNdate(date);
                            news.setNcontent(newsJson.getString("ncontent"));
                            news.setNphotoname(newsJson.getString("nphotoname"));
                            news.setNdataname(newsJson.getString("ndataname"));
                            news.setNisnotice(newsJson.getInt("nisnotice"));
                        }

                        return true;
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean) {
                    DetailNewsFragment detailNewsFragment = new DetailNewsFragment();
                    detailNewsFragment.setNews(news);
                    activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, detailNewsFragment).commit();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    // Setting Dialog Title
                    builder.setTitle("News Dialog");

                    // Setting Dialog Message
                    builder.setMessage("소식 보기 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };
        asyncTask.execute(Network.site+"team/news/getNews?nid="+nid);
    }

    //소식 삭제
    public static void deleteNews(int nid, final android.support.v4.app.Fragment fragment) {

        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                // 뉴스 삭제 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // 통신 성공시
                    Log.i("mylog",root.toString());
                    if(root.getString("deleteNewsResult").equals("success")){
                        return true;
                    } else {
                        // 통신 실패시
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
                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TeamHomeFragment())
                            .commit();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("News Dialog");

                    // Setting Dialog Message
                    builder.setMessage("소식 삭제 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };
        asyncTask.execute(Network.site+"team/news/deleteNews?nid=" + nid);
    }

    //소식 수정
    public static void modifyNews(final int nid, String newscontent, final android.support.v4.app.Fragment fragment) {
        final News news=new News();
        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(String... params) {

                // 소식 수정 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }


                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // 통신 성공시
                    Log.i("mylog",root.toString());
                    if(root.getString("newsResult").equals("success")){
                        JSONObject newsJson = new JSONObject(root.getString("list"));
                        news.setNid(newsJson.getInt("nid"));
                        news.setTid(newsJson.getInt("tid"));
                        news.setMid(newsJson.getString("mid"));
                        news.setMprofile(newsJson.getString("mprofile"));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = sdf.parse(newsJson.getString("ndate"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        news.setNdate(date);
                        news.setNcontent(newsJson.getString("ncontent"));
                        news.setNphotoname(newsJson.getString("nphotoname"));
                        news.setNdataname(newsJson.getString("ndataname"));
                        news.setNisnotice(newsJson.getInt("nisnotice"));
                        return true;
                    } else {
                        // 통신 실패시
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
                    DetailNewsFragment detailNewsFragment = new DetailNewsFragment();
                    detailNewsFragment.setNews(news);
                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, detailNewsFragment)
                            .commit();
                    Log.i("mylog", "소식 수정 성공");
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("News Dialog");

                    // Setting Dialog Message
                    builder.setMessage("소식 수정 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };

        try {
            newscontent = URLEncoder.encode(newscontent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        asyncTask.execute(Network.site+"team/news/modifyNews?nid="+ nid +"&newscontent=" + newscontent);
    }



    //일반 사진
    public static void getBitmap(String url, final ImageView imageView) {
        AsyncTask<String, Void, Bitmap> asynctask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap=null;
                try{
                    URL url = new URL(params[0]);
                    HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                    conn.connect();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                    }
                    conn.disconnect();

                }catch (Exception e) { e.printStackTrace(); }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        };
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        asynctask.execute(Network.site+"team/news/image?fileName="+url);
    }

    //teamMain화면 사진
    public static void getBitmap(int tid, final CollapsingToolbarLayout toolbar_layout) {
        AsyncTask<String, Void, Bitmap> asynctask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap=null;
                try{
                    URL url = new URL(params[0]);
                    HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                    conn.connect();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                    }
                    conn.disconnect();

                }catch (Exception e) { e.printStackTrace(); }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                TeamHomeFragment.visibleParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, toolbar_layout.getMeasuredHeight());
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                toolbar_layout.setBackgroundDrawable(bitmapDrawable);
            }
        };
        asynctask.execute(Network.site+"team/image?tid="+tid);
    }

    //News detail 화면 사진
    public static void getBitmap(String url, final CollapsingToolbarLayout toolbar_layout) {
        AsyncTask<String, Void, Bitmap> asynctask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap=null;
                try{
                    URL url = new URL(params[0]);
                    HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                    conn.connect();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                    }
                    conn.disconnect();

                }catch (Exception e) { e.printStackTrace(); }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                toolbar_layout.setBackgroundDrawable(bitmapDrawable);
            }
        };
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        asynctask.execute(Network.site+"team/news/image?fileName="+url);
    }



    //소식 상세화면 안의 댓글 리스트 가져오기
    public static void getCommentList(final int nid, final android.support.v4.app.Fragment fragment, final CommentItemViewAdapter commentItemViewAdapter) {
        final List<Newscomment> commentList = new ArrayList<>();

        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                // 댓글 가져오는 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // 통신 성공시 commentList세팅
                    //Log.i("mylog",root.toString());
                    if(root.getString("commentListResult").equals("success")){
                        JSONArray commentJsonArray = new JSONArray(root.getString("list"));


                        for(int i=0; i<commentJsonArray.length(); i++) {

                            Newscomment newscomment = new Newscomment();
                            JSONObject commentJson = commentJsonArray.getJSONObject(i);

                            newscomment.setCid(commentJson.getInt("cid"));
                            newscomment.setCcontent(commentJson.getString("ccontent"));

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = sdf.parse(commentJson.getString("cdate"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            newscomment.setCdate(date);
                            newscomment.setNid(commentJson.getInt("nid"));
                            newscomment.setMid(commentJson.getString("mid"));
                            newscomment.setMprofile(commentJson.getString("mprofile"));

                            commentList.add(newscomment);
                        }
                        return true;


                    } else {
                        // 통신 실패시
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
                    commentItemViewAdapter.setCommentList(commentList);
                    commentItemViewAdapter.notifyDataSetChanged();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("Comment Dialog");

                    // Setting Dialog Message
                    builder.setMessage("댓글 불러오기 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };
        asyncTask.execute(Network.site+"team/news/commentList?nid="+ nid);
    }

    //댓글 작성
    public static void writeComment(String ccontent, int nid, final android.support.v4.app.Fragment fragment) {

        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                // 댓글 쓰기 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // 통신 성공시 commentList세팅
                    Log.i("mylog",root.toString());
                    if(root.getString("writeCommentResult").equals("success")){
                        return true;
                    } else {
                        // 통신 실패시
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
                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .detach(fragment)
                            .attach(fragment)
                            .commit();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("Comment Dialog");

                    // Setting Dialog Message
                    builder.setMessage("댓글 등록 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };

        try {
            ccontent = URLEncoder.encode(ccontent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        asyncTask.execute(Network.site+"team/news/writeComment?mid="+ MainActivity.member.getMid() +"&ccontent=" + ccontent + "&nid=" + nid);
    }

    //댓글 삭제
    public static void deleteComment(int cid, final android.support.v4.app.Fragment fragment) {

        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                // 댓글 삭제 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // 통신 성공시
                    //Log.i("mylog",root.toString());
                    if(root.getString("deleteCommentResult").equals("success")){
                        return true;
                    } else {
                        // 통신 실패시
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
                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .detach(fragment)
                            .attach(fragment)
                            .commit();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("Comment Dialog");

                    // Setting Dialog Message
                    builder.setMessage("댓글 삭제 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };
        asyncTask.execute(Network.site+"team/news/deleteComment?cid=" + cid);
    }

    //댓글 수정
    public static void modifyComment(final int cid, String commentcontent, final android.support.v4.app.Fragment fragment) {
        //final Newscomment newscomment = new Newscomment();
        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {

                // 댓글 수정 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);

                    // 통신 성공시
                    if(root.getString("modifyCommentResult").equals("success")){
                        /*JSONObject commentJson = new JSONObject(root.getString("list"));

                        newscomment.setCid(commentJson.getInt("cid"));
                        newscomment.setCcontent(commentJson.getString("ccontent"));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = sdf.parse(commentJson.getString("cdate"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        newscomment.setCdate(date);
                        newscomment.setNid(commentJson.getInt("nid"));
                        newscomment.setMid(commentJson.getString("mid"));
                        newscomment.setMprofile(commentJson.getString("mprofile"))*/;

                        return true;
                    } else {
                        // 통신 실패시
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
                    fragment.getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .detach(fragment)
                            .attach(fragment)
                            .commit();
                    Log.i("mylog", "댓글 수정 성공");
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

                    // Setting Dialog Title
                    builder.setTitle("Comment Dialog");

                    // Setting Dialog Message
                    builder.setMessage("댓글 수정 실패");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };

        try {
            commentcontent = URLEncoder.encode(commentcontent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        asyncTask.execute(Network.site+"team/news/modifyComment?cid="+ cid +"&commentcontent=" + commentcontent);
    }

    //댓글 갯수
    public static void getTotalCommentNo(int nid, final TextView commentNumber) {
        final AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
            int count=0;
            @Override
            protected Boolean doInBackground(String... params) {
                // 댓글 갯수 네트워킹
                String json = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        Reader reader = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(reader);

                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            json += line;
                        }

                        br.close();
                        reader.close();
                        is.close();
                    }else {
                        conn.disconnect();
                        return false;
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                // 응답http json 파싱
                try {
                    JSONObject root = new JSONObject(json);
                    // 통신 성공시
                    count = root.getInt("getTotalCommentNoResult");

                    return true;
                } catch (JSONException e) {
                    //통신 실패시
                    e.printStackTrace();
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){
                    String str=null;
                    if(count==0) {
                        str = "댓글";
                    }else {
                        str = String.valueOf(count)+"개 댓글";
                    }
                    commentNumber.setText(str);
                }else{
                    Log.i("mylog", "카운트 실패");
                }
            }
        };
        asyncTask.execute(Network.site+"team/news/getTotalCommentNo?nid=" + nid);

    }


    // 팀메인 화면에서 팀사진 위에 팀정보 표시를 위한 팀정보 가져오기
    public static void getTeamInfo(int tid, final TextView teamName, final TextView teamDescr) {
        Teams teamInfo = null;
        try {
            AsyncTask<String, Void, Teams> asynctask = new AsyncTask<String, Void, Teams>() {
                @Override
                protected Teams doInBackground(String... params) {
                    String json = "";
                    Teams teamInfo = null;
                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                @Override
                protected void onPostExecute(Teams teams) {
                    teamName.setText(teams.getTname());
                    teamDescr.setText(teams.getTdescr());
                }
            };
            asynctask.execute(Network.site + "/team/getTeamInfo?tid=" + tid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}