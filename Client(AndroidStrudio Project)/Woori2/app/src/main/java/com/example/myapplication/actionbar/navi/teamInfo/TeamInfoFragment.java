package com.example.myapplication.actionbar.navi.teamInfo;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Member;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.home.HomeFragment;
import com.example.myapplication.network.Network3;
import com.example.myapplication.network.Network5;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamInfoFragment extends Fragment {
    private ImageView imgViewTeamProfile;
    private EditText txtTeamName;
    private EditText txtTeamDescr;
    private Button btnCorrectTeamName;
    private Button btnCorrectTeamDescr;
    private Button btnEditMember;
    private Button btnWithdraw;
    private ListView membersListView;
    private TextView txtNumMember;
    private boolean isShowTransferAdim;
    private boolean editableTname;
    private boolean editableTdescr;
    private Button btnCancelUpdateTname;
    private Button btnCancelUpdateTdescr;
    private Teams teamInfo;
    private ImageButton cameraImageButton;

    // 수정 실패시 다시 보여줄 이름
    private String prevTname;
    private String prevTdescr;

    private List<Member> memberList = null;

    public TeamInfoFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.presentFragment = "TeamInfoFragment";
        getActivity().invalidateOptionsMenu();
        MainActivity.toggle.setDrawerIndicatorEnabled(true);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_team_info, container, false);
        imgViewTeamProfile = (ImageView) view.findViewById(R.id.imgViewTeamProfile);
        txtTeamName = (EditText) view.findViewById(R.id.txtTeamName);
        txtTeamDescr = (EditText) view.findViewById(R.id.txtTeamDescr);
        btnCorrectTeamName = (Button) view.findViewById(R.id.btnCorrectTeamName);
        btnCorrectTeamDescr = (Button) view.findViewById(R.id.btnCorrectTeamDescr);
        btnEditMember = (Button) view.findViewById(R.id.btnEditMember);
        btnWithdraw = (Button) view.findViewById(R.id.btnWithdraw);
        membersListView = (ListView) view.findViewById(R.id.membersListView);
        txtNumMember = (TextView) view.findViewById(R.id.txtNumMember);
        btnCancelUpdateTname = (Button) view.findViewById(R.id.btnCancelUpdateTname);
        btnCancelUpdateTdescr = (Button) view.findViewById(R.id.btnCancelUpdateTdescr);
        cameraImageButton = (ImageButton)view.findViewById(R.id.cameraImageButton);

        txtTeamName.setEnabled(false);
        txtTeamDescr.setEnabled(false);
        btnCancelUpdateTname.setVisibility(View.INVISIBLE);
        btnCancelUpdateTdescr.setVisibility(View.INVISIBLE);



        // 팀장임명버튼 보여주는 버튼을 위한 플래그
        isShowTransferAdim = false;
        // 팀이름수정 플래그
        editableTname = false;
        // 팀설명수정 플래그
        editableTdescr = false;


        // 팀 정보 수정하는 버튼(+초대하기) 숨기기
        btnCorrectTeamName.setVisibility(View.INVISIBLE);
        btnCorrectTeamDescr.setVisibility(View.INVISIBLE);
        btnEditMember.setVisibility(View.INVISIBLE);
        cameraImageButton.setVisibility(View.INVISIBLE);


        // 서버에서 팀 정보 가져오기
        teamInfo  = Network3.getTeamInfo(MainActivity.tid);
        Log.d("teamInfo", teamInfo.getTid()+teamInfo.getMid()+teamInfo.getTname()+teamInfo.getTprofile()+teamInfo.getTdescr());


        // 팀 정보 세팅
        if(teamInfo.getTprofile() != null) {
            /*int tprofileId = getResources().getIdentifier(teamInfo.getTprofile(), "drawable", getActivity().getPackageName());
            imgViewTeamProfile.setBackgroundResource(tprofileId);*/
            Network5.getBitmap(teamInfo.getTprofile(), imgViewTeamProfile);

        }
        txtTeamName.setText(teamInfo.getTname());
        if(teamInfo.getTdescr() != null) { txtTeamDescr.setText(teamInfo.getTdescr()); }
        else { txtTeamDescr.setText(null); }



        // 팀 멤버 리스트 가져오기
        Log.d("tid", teamInfo.getTid()+"");


        // 리스트뷰 세팅
        final TeamMembersListViewAdapter membersAdapter = new TeamMembersListViewAdapter(getActivity());
        membersAdapter.setAdminId(teamInfo.getMid());
        Network3.getTeamMembers(teamInfo.getTid(),  membersAdapter, txtNumMember);


        // 팀장인 경우 팀 정보 수정하는 버튼 활성화
        if(teamInfo.getMid().equals(MainActivity.member.getMid())) {
            btnCorrectTeamName.setVisibility(View.VISIBLE);
            btnCorrectTeamDescr.setVisibility(View.VISIBLE);
            btnEditMember.setVisibility(View.VISIBLE);
            cameraImageButton.setVisibility(View.VISIBLE);

            // 팀 멤버가 자신뿐이면 팀 정보 수정하는 버튼 비활성화

        }




        //   membersAdapter.setList(memberList);
        membersListView.setAdapter(membersAdapter);
        membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                membersAdapter.getItem(position);
            }
        });

        cameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"사진촬영", "사진앨범에서 선택"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

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

                android.app.AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기

            }
        });

        // 탈퇴 버튼 이벤트 처리
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 멤버가 자신뿐이면 탈퇴 후 팀이 사라진다고 알려주기
                if(membersAdapter.getCount() == 1) {
                    AlertDialog.Builder alt_bld1 = new AlertDialog.Builder(getActivity());
                    alt_bld1.setMessage("탈퇴하시면 팀이 사라집니다. 팀에서 탈퇴하시겠습니까?").setCancelable(
                            false).setPositiveButton("네",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Network3.withdrawFromTeam(MainActivity.member.getMid(), MainActivity.tid, getActivity());
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragmentContainer, new HomeFragment())
                                            .commit();

                                    Toast.makeText(getActivity(), "팀에서 탈퇴했습니다.", Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert1 = alt_bld1.create();
                    // Title for AlertDialog
                    alert1.setTitle("탈퇴");
                    // Icon for AlertDialog
                    alert1.setIcon(android.R.drawable.ic_dialog_alert);
                    alert1.show();
                    return;
                }


                // 팀장이면 양도하라고 하기
                if(teamInfo.getMid().equals(MainActivity.member.getMid())) {
                    Toast.makeText (getActivity(), "팀장 권한을 양도해야 탈퇴하실 수 있습니다.", Toast.LENGTH_LONG).show();
                    return;
                }


                // 탈퇴여부 다시 묻는 다이얼로그
                AlertDialog.Builder alt_bld2 = new AlertDialog.Builder(getActivity());
                alt_bld2.setMessage("팀에서 탈퇴하시겠습니까?").setCancelable(
                        false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Network3.withdrawFromTeam(MainActivity.member.getMid(), MainActivity.tid, getActivity());
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragmentContainer, new HomeFragment())
                                        .commit();

                                Toast.makeText (getActivity(), "팀에서 탈퇴했습니다.", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert2 = alt_bld2.create();
                // Title for AlertDialog
                alert2.setTitle("탈퇴");
                // Icon for AlertDialog
                alert2.setIcon(android.R.drawable.ic_dialog_alert);
                alert2.show();
            }
        });


        // 멤버에게 팀장양도하는 버튼나오는 버튼이벤트처리
        btnEditMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowTransferAdim == false) {
                    membersAdapter.setIsAdmin(true);
                    membersAdapter.notifyDataSetChanged();
                    btnEditMember.setBackgroundResource(android.R.drawable.ic_delete);
                    isShowTransferAdim = true;
                } else {
                    membersAdapter.setIsAdmin(false);
                    membersAdapter.notifyDataSetChanged();
                    btnEditMember.setBackgroundResource(R.drawable.teaminfo_modify_icon);
                    isShowTransferAdim = false;
                }
            }
        });


        // 팀이름 수정 버튼 이벤트 처리
        btnCorrectTeamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editableTname == true) {
                    // not null 처리
                    if(txtTeamName.getText().toString().getBytes().length <= 0) {
                        Toast.makeText (getActivity(), "팀이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                        txtTeamName.setText(prevTname);
                        btnCorrectTeamName.setBackgroundResource(R.drawable.teaminfo_modify_icon);
                        btnCancelUpdateTname.setVisibility(View.INVISIBLE);
                        txtTeamName.setEnabled(false);
                        editableTname = false;

                    } else {
                        teamInfo.setTname(txtTeamName.getText().toString());
                        boolean result = Network3.updateTeamInfo(teamInfo);
                        if(result == false) {
                            Toast.makeText (getActivity(), "수정에 실패했습니다.", Toast.LENGTH_LONG).show();
                            txtTeamName.setText(prevTname);
                        } else {
                            Toast.makeText (getActivity(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                        }
                        btnCorrectTeamName.setBackgroundResource(R.drawable.teaminfo_modify_icon);
                        btnCancelUpdateTname.setVisibility(View.INVISIBLE);
                        txtTeamName.setEnabled(false);
                        editableTname = false;
                    }
                } else {
                    prevTname = txtTeamName.getText().toString();
                    btnCorrectTeamName.setBackgroundResource(R.drawable.btn_finish);
                    btnCancelUpdateTname.setVisibility(View.VISIBLE);
                    txtTeamName.setEnabled(true);
                    editableTname = true;
                }
            }
        });

        // 팀이름 수정 취소 버튼 이벤트 처리
        btnCancelUpdateTname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamInfo.setTdescr(txtTeamDescr.getText().toString());
                Network3.updateTeamInfo(teamInfo);
                btnCorrectTeamName.setBackgroundResource(R.drawable.teaminfo_modify_icon);
                btnCancelUpdateTname.setVisibility(View.INVISIBLE);
                txtTeamName.setEnabled(false);
                editableTname = false;
            }
        });


        // 팀설명 수정 버튼 이벤트 처리
        btnCorrectTeamDescr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editableTdescr == true) {
                    teamInfo.setTdescr(txtTeamDescr.getText().toString());
                    boolean result = Network3.updateTeamInfo(teamInfo);
                    if(result == false) {
                        Toast.makeText (getActivity(), "수정에 실패했습니다.", Toast.LENGTH_LONG).show();
                        txtTeamDescr.setText(prevTdescr);
                    } else {
                        Toast.makeText (getActivity(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                    }
                    btnCorrectTeamDescr.setBackgroundResource(R.drawable.teaminfo_modify_icon);
                    btnCancelUpdateTdescr.setVisibility(View.INVISIBLE);
                    txtTeamDescr.setEnabled(false);
                    editableTdescr = false;
                } else {
                    prevTdescr = txtTeamDescr.getText().toString();
                    btnCorrectTeamDescr.setBackgroundResource(R.drawable.btn_finish);
                    btnCancelUpdateTdescr.setVisibility(View.VISIBLE);
                    txtTeamDescr.setEnabled(true);
                    editableTdescr = true;
                }
            }
        });

        // 팀설명 수정 취소 버튼 이벤트 처리
        btnCancelUpdateTdescr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCorrectTeamDescr.setBackgroundResource(R.drawable.teaminfo_modify_icon);
                btnCancelUpdateTdescr.setVisibility(View.INVISIBLE);
                txtTeamDescr.setEnabled(false);
                editableTdescr = false;
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==1){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                String str = "camera";
                imgViewTeamProfile.setTag(str);
                imgViewTeamProfile.setImageBitmap(bitmap);
                //imgViewTeamProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if(Network3.updateProfile(teamInfo.getTprofile(), bitmap) == true) {
                    Toast.makeText (getContext(), "팀 사진을 변경하였습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText (getContext(), "팀 사진을 변경하지 못했습니다.", Toast.LENGTH_LONG).show();
                }

            } else if(requestCode == 2) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    String str = "gallery";
                    imgViewTeamProfile.setTag(str);
                    imgViewTeamProfile.setImageBitmap(bitmap);
                    //imgViewTeamProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


