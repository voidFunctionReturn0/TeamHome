package com.example.myapplication.team;


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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.network.Network5;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTeamFragment extends Fragment {
    private ImageButton imageButton;
    private ImageButton imageButton2;
    private Button btnAddteam;
    private EditText txtTname;
    private EditText txtTdescr;

    int addFragmentId;

    public AddTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_team, container, false);
        addFragmentId = this.getId();
        imageButton = (ImageButton)view.findViewById(R.id.imageButton);
        imageButton2 = (ImageButton)view.findViewById(R.id.imageButton2);
        btnAddteam =(Button)view.findViewById(R.id.btnAddteam);
        txtTdescr =(EditText)view.findViewById(R.id.txtTdescr);
        txtTname =(EditText)view.findViewById(R.id.txtTname);



        imageButton.setOnClickListener(new View.OnClickListener() {
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
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items2 = {"사진촬영", "사진앨범에서 선택"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("팀 프로필")        // 제목 설정
                        .setItems(items2, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
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

        btnAddteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teams team = new Teams();
                team.setMid(MainActivity.member.getMid());
                team.setTdescr(txtTdescr.getText().toString());
                team.setTname(txtTname.getText().toString());
                String fileName = team.getTname()+ ".png";
                team.setTprofile(fileName);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageButton.getDrawable();
                Bitmap bitmap;
                if( imageButton.getTag() == null){
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.userbg);
                    bitmap = getResizedBitmap(bitmap, 200);
                    bitmap = getCircleBitmap(bitmap, 150);
                    fileName = "base_team.png";
                }else{
                    bitmap = bitmapDrawable.getBitmap();
                    bitmap = getResizedBitmap(bitmap, 900);
                }




                String strtxtTname = txtTname.getText().toString();
                if(TextUtils.isEmpty(strtxtTname)) {
                    txtTname.setError("팀이름을 입력해주세요");
                    return;
                }else{
                    Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentById(addFragmentId);
                    //Network5.addteam(txtTdescr.getText().toString(), txtTname.getText().toString(), fragment);
                    Network5.addteam(team, bitmap, getActivity(),fragment);
                }
            }
        });

        return view;


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==1){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                //bitmap = getCircleBitmap(bitmap, 150);
                String str = "camera";
                imageButton.setTag(str);
                imageButton.setImageBitmap(bitmap);
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if(requestCode == 2) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    //bitmap = getCircleBitmap(bitmap, 150);
                    String str = "gallery";
                    imageButton.setTag(str);
                    imageButton.setImageBitmap(bitmap);
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

}
