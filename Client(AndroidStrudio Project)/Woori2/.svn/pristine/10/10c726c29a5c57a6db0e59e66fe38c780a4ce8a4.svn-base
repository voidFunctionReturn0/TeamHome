package com.example.myapplication.team.news;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.team.news.filechooser.FileOperation;
import com.example.myapplication.team.news.filechooser.FileSelector;
import com.example.myapplication.team.news.filechooser.OnHandleFileListener;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewsFragment extends Fragment {

    private Button btnAddAttachFile;
    private Button btnAddAttachPhoto;
    public static TextView txtAttachFileName;
    public static TextView txtAttachPhotoName;
    private ImageView addAttachImageView;
    private CheckBox chkNotice;
    public static MultiAutoCompleteTextView addNewsMultiTextView;
    private Button btnAddNews;
    private String realFilePath = null;
    public static String fileName = null;
    public static String photoName = null;
    public static Bitmap bitmap = null;
    public static File file = null;
    private Button btnCancelFile;
    private Button btnCancelPhoto;
    private Button btnPlus;
    private LinearLayout photoAndFileLayout;
    private RelativeLayout topLayout;

    private boolean isShowPhotoAndFileLayout;
    private InputMethodManager imm;
    private int heightDiff;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public AddNewsFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isShowPhotoAndFileLayout = false;

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        MainActivity.presentFragment = "AddNewsFragment";
        getActivity().invalidateOptionsMenu();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_news, container, false);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                heightDiff = view.getRootView().getHeight()-view.getHeight();
            }
        });



        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        btnPlus = (Button)view.findViewById(R.id.btnPlus);
        photoAndFileLayout = (LinearLayout)view.findViewById(R.id.photoAndFileLayout);
        topLayout = (RelativeLayout)view.findViewById(R.id.topLayout);
        btnAddAttachFile = (Button) view.findViewById(R.id.btnAddAttachFile);
        btnAddAttachFile.setOnClickListener(onBtnAttachClickListener);
        btnAddAttachPhoto = (Button) view.findViewById(R.id.btnAddAttachPhoto);
        btnAddAttachPhoto.setOnClickListener(onBtnPhotoClickListener);

        txtAttachFileName = (TextView) view.findViewById(R.id.txtAttachFileName);
        txtAttachPhotoName = (TextView) view.findViewById(R.id.txtAttachPhotoName);
        //btnAddNews = (Button) view.findViewById(R.id.btnAddNews);
        //addAttachImageView = (ImageView) view.findViewById(R.id.addAttachImageView);
        addNewsMultiTextView = (MultiAutoCompleteTextView) view.findViewById(R.id.addNewsMultiTextView);
        //chkNotice = (CheckBox) view.findViewById(R.id.chkNotice);
        btnCancelFile = (Button) view.findViewById(R.id.btnCancelFile);
        btnCancelPhoto = (Button) view.findViewById(R.id.btnCancelPhoto);

        // 버튼 이벤트 처리
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isShowPhotoAndFileLayout == false) {
                    hideKeyboard();
                    for(int i=0; i<10000000; i++) {}
                    photoAndFileLayout.getLayoutParams().height = 400;
                    topLayout.requestLayout();
                    isShowPhotoAndFileLayout = true;
                    btnPlus.setBackgroundResource(R.drawable.btn_addnews_keypad);
                }else{
                    showKeyboard();
                    photoAndFileLayout.getLayoutParams().height = 1;
                    topLayout.requestLayout();
                    isShowPhotoAndFileLayout = false;
                    btnPlus.setBackgroundResource(R.drawable.btn_addnews_attach);
                }

            }
        });

        addNewsMultiTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowPhotoAndFileLayout == true){
                    photoAndFileLayout.getLayoutParams().height = 1;
                    topLayout.requestLayout();
                    isShowPhotoAndFileLayout = false;
                    btnPlus.setBackgroundResource(R.drawable.btn_addnews_attach);
                }
            }
        });




      // btnAddNews.setOnClickListener(onBtnAddNewsClickListener);
        btnCancelFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file = null;
                txtAttachFileName.setTag(txtAttachFileName.getId(), "none");
                txtAttachFileName.setText(null);
            }
        });
        btnCancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                addAttachImageView.setImageBitmap(null);
                txtAttachPhotoName.setTag(txtAttachPhotoName.getId(), "none");
                txtAttachPhotoName.setText(null);
            }
        });

        // 태그 초기화
        /*txtAttachFileName.setTag(txtAttachFileName.getId(), "none");
        txtAttachPhotoName.setTag(txtAttachPhotoName.getId(), "none");*/

        return view;
    }

    private void hideKeyboard(){

        imm.hideSoftInputFromWindow(addNewsMultiTextView.getWindowToken(), 0);

    }

    private void showKeyboard(){

        imm.showSoftInput(addNewsMultiTextView, 0);

    }


    //fileChooser 버튼 클릭
   final String[] mFileFilter = { "*.*", ".jpeg", ".txt", ".png" };
    private View.OnClickListener onBtnAttachClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 권한을 획득하기전에 현재 Acivity에서 지정된 권한을 사용할 수 있는지 여부 체크
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Log.i("mylog", "체크");
            } else {
                new FileSelector(getActivity(), FileOperation.LOAD, mLoadFileListener, mFileFilter).show();
            }
        }
    };

    OnHandleFileListener mLoadFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            Toast.makeText(getActivity(), "Load: " + filePath, Toast.LENGTH_SHORT).show();
            int lastSlashIdx = filePath.lastIndexOf("/");
            fileName = filePath.substring(lastSlashIdx+1);
            txtAttachFileName.setText(fileName);
            realFilePath = filePath;
            file = new File(realFilePath);
            txtAttachFileName.setTag(txtAttachFileName.getId(),"file");
        }
    };

    //소식추가 버튼 클릭
  /* private View.OnClickListener onBtnAddNewsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //공백제거 후 내용이 없으면서 파일이나 사진이 첨부되지 않았다면 내용을 입력하라는 다이얼로그 띄우기
            //파일이나 사진이 첨부되면 addAttachImageView의 Tag를 photo 혹은 file로 변경하도록 처리해 놨음
            if(addNewsMultiTextView.getText().toString().trim().length() < 1
                    && txtAttachPhotoName.getTag(txtAttachPhotoName.getId()).equals("none")
                    && txtAttachFileName.getTag(txtAttachFileName.getId()).equals("none")
                    ){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Setting Dialog Title
                builder.setTitle("News Dialog");
                // Setting Dialog Message
                builder.setMessage("내용을 입력해 주세요 ");
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.tick);
                // Setting OK Button
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else {
                //서버로 입력받은 값 전달
                long now = System.currentTimeMillis();
                Date date = new Date(now);

                int check = 0; // 공지사항인지 체크
                if(chkNotice.isChecked()){
                    check = 1;
                }

                News news = new News();
                news.setMid(MainActivity.member.getMid());
                news.setMprofile(MainActivity.member.getMprofile());
                news.setNcontent(addNewsMultiTextView.getText().toString());
                news.setTid(MainActivity.tid);
                news.setNdate(date);
                news.setNphotoname(photoName);
                news.setNdataname(fileName);
                news.setNisnotice(check);

                if(bitmap != null) {
                    bitmap = getResizedBitmap(bitmap, 900);
                }
                Network3.addNews(news,bitmap,file, getActivity());


            }
        }
    };*/


    // 사진추가 버튼이벤트
    private View.OnClickListener onBtnPhotoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final CharSequence[] items = {"사진촬영", "앨범에서 선택"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("사진선택").setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    switch (which) {
                        case 0:
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                            break;
                        case 1:
                            Intent intent2 = new Intent(Intent.ACTION_PICK);
                            intent2.setType("image*//*");
                            startActivityForResult(intent2, 2);
                            break;
                    }


                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

            /*

            *//*final CharSequence[] items = {"사진촬영", "앨범에서 선택"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("이미지 추가")        // 제목 설정
                    .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                        public void onClick(DialogInterface dialog, int index){
                               *//**//* Toast.makeText(getActivity().getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();*//**//*
                            if(index==0){
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,1);
                            }else{
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image*//**//*");
                                startActivityForResult(intent,2);
                            }
                        }
                    });

            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기*//*

        }
    };*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==1){
                //촬영
                bitmap = (Bitmap)data.getExtras().get("data");
                //addAttachImageView.setImageBitmap(bitmap);
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                //CircleBitmap make = new CircleBitmap();
                //bitmap = make.getCircleBitmap(bitmap, 150);
                String str = "camera";
                btnAddAttachPhoto.setTag(str);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnAddAttachPhoto.setBackground(new BitmapDrawable(bitmap));
                }
                //btnAddAttachPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);*//*
            } else if(requestCode == 2) {
                //앨범에서 선택

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    bitmap = getResizedBitmap(bitmap, 300);
                    //addAttachImageView.setImageBitmap(bitmap);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    //CircleBitmap make = new CircleBitmap();
                    //bitmap = make.getCircleBitmap(bitmap, 150);
                    String str = "gallery";
                    btnAddAttachPhoto.setTag(str);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btnAddAttachPhoto.setBackground(new BitmapDrawable(bitmap));
                        //btnAddAttachPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);*//*
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long now = System.currentTimeMillis();
            photoName = now + "_" + MainActivity.member.getMid() + ".png";
            txtAttachPhotoName.setTag(txtAttachPhotoName.getId(),"photo");
            txtAttachPhotoName.setText(photoName);
        }
    }


    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
