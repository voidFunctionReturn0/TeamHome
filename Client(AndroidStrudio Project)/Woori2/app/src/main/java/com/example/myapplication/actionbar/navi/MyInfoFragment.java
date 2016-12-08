package com.example.myapplication.actionbar.navi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Member;
import com.example.myapplication.home.CircleBitmap;
import com.example.myapplication.network.Network4;
import com.example.myapplication.network.Network5;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyInfoFragment extends Fragment {

    private ImageView btnProfile;
    private TextView txtId;
    private TextView txtPassword1;
    private TextView txtPassword2;
    private boolean pwdcheck = false;
    private ImageView passCheckImage;
    private Button btnRevise;
    int MyInfoFragment;


    public MyInfoFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MyInfoFragment = this.getId();
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);

        btnProfile = (ImageView)view.findViewById(R.id.navProfile);
        txtId = (TextView)view.findViewById(R.id.txtId);
        txtPassword1 = (TextView)view.findViewById(R.id.txtPassword1);
        txtPassword2 = (TextView)view.findViewById(R.id.txtPassword2);
        passCheckImage = (ImageView)view.findViewById(R.id.passCheckImage);
        btnRevise = (Button)view.findViewById(R.id.btnRevise);




        Network5.getBitmap(MainActivity.member.getMprofile(), btnProfile);
        txtId.setText(MainActivity.member.getMid());
        txtPassword1.setText(MainActivity.member.getMpwd());
        txtPassword2.setText(MainActivity.member.getMpwd());
        txtId.setEnabled(false);
        txtId.setClickable(false);



        txtPassword2.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtPassword2.getText().toString().equals(txtPassword1.getText().toString())){
                    passCheckImage.setBackgroundResource(R.drawable.repassword_ok_icon);
                    pwdcheck = true;
                }else{
                    pwdcheck = false;
                    passCheckImage.setBackgroundResource(R.drawable.repassword_reject_icon);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPassword1.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtPassword1.getText().toString().equals(txtPassword2.getText().toString())){
                    passCheckImage.setBackgroundResource(R.drawable.repassword_ok_icon);
                    pwdcheck = true;
                }else{
                    pwdcheck = false;
                    passCheckImage.setBackgroundResource(R.drawable.repassword_reject_icon);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"사진촬영", "사진앨범에서 선택"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("팀 프로필")        // 제목 설정
                        .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                            public void onClick(DialogInterface dialog, int index){
                               /* Toast.makeText(getActivity().getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();*/
                                if(index==0){
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent,1);
                                }else{
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent,2);
                                }
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기

            }
        });

        btnRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pwdcheck || txtPassword1.getText().toString().equals(txtPassword2.getText().toString())){

                    String fileName = txtId.getText().toString() + ".png";
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) btnProfile.getDrawable();
                    Member member = new Member();
                    Bitmap bitmap;
                    if (btnProfile.getTag() == null) {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.join_base_person);
                        bitmap = getResizedBitmap(bitmap, 600);
                        bitmap = getCircleBitmap(bitmap, 150);
                    } else {
                        bitmap = bitmapDrawable.getBitmap();
                        bitmap = getResizedBitmap(bitmap, 600);
                    }


                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(MyInfoFragment);
                    MainActivity.member.setMprofile(fileName);
                    member.setMprofile(fileName);
                    member.setMpwd(txtPassword2.getText().toString());
                    member.setMid(MainActivity.member.getMid());
                    //MainActivity.member.setMprofile(fileName);


                    Network4.revise(member,
                            fragment,
                            bitmap);
                }else{
                    Log.i("myinfo","수정 실패");
                }
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==1){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                CircleBitmap make = new CircleBitmap();
                bitmap = make.createFramedPhoto(bitmap, 200);
                String str = "camera";
                btnProfile.setTag(str);
                btnProfile.setImageBitmap(bitmap);
                btnProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if(requestCode == 2) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    CircleBitmap make = new CircleBitmap();
                    bitmap = make.createFramedPhoto(bitmap, 200);
                    String str = "gallery";
                    btnProfile.setTag(str);
                    btnProfile.setImageBitmap(bitmap);
                    btnProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getCircleBitmap(Bitmap scaleBitmapImage, int radiusDp) {
        int width = 0;
        int height = 0;
        int radius = (int)(radiusDp * getResources().getDisplayMetrics().density);
        if(scaleBitmapImage.getWidth()<scaleBitmapImage.getHeight()) {
            width = radius * 2;
            height =radius * 2 * scaleBitmapImage.getHeight()/scaleBitmapImage.getWidth();
        } else {
            width =radius * 2 * scaleBitmapImage.getWidth()/scaleBitmapImage.getHeight();
            height = radius * 2;
        }
        Bitmap targetBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(width/2, height/2, radius, Path.Direction.CCW);
        canvas.clipPath(path);

        canvas.drawBitmap(
                scaleBitmapImage,
                new Rect(0, 0, scaleBitmapImage.getWidth(), scaleBitmapImage.getHeight()),
                new Rect(0, 0, width, height),
                null
        );

        return targetBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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
