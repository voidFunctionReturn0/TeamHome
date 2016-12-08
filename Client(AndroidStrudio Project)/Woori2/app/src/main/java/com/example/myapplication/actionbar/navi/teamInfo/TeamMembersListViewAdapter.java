package com.example.myapplication.actionbar.navi.teamInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Member;
import com.example.myapplication.network.Network3;
import com.example.myapplication.network.Network5;

import java.util.List;

/**
 * Created by Administrator on 2016-07-20.
 */
public class TeamMembersListViewAdapter extends BaseAdapter {

    private boolean isAdmin = false;
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    private String adminId;
    public void setAdminId(String adminId) { this.adminId = adminId; }

    private List<Member> list;
    public void setList(List<Member> list) { this.list = list; }

    private Context context;
    public TeamMembersListViewAdapter(Context context) { this.context = context; }


    @Override
    public int getCount() {
        if(list != null)    return list.size();
        else                return -1;
    }

    @Override
    public Object getItem(int position) {
        if(list != null)    return list.get(position);
        else                return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 아이템뷰를 새로 만드는 경우
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view_team_members, parent, false);
        }

        // 뷰 인플레이션
        ImageView memberProfileImageView = (ImageView) convertView.findViewById(R.id.memberProfileImageView);
        TextView txtMid = (TextView) convertView.findViewById(R.id.txtMid);
        TextView txtMbirth = (TextView) convertView.findViewById(R.id.txtMbirth);
        Button btnAdminTransfer = (Button) convertView.findViewById(R.id.btnAdminTransfer);

        // 팀장임명버튼 보이기/숨기기
        if(isAdmin==true && !adminId.equals(list.get(position).getMid())) { btnAdminTransfer.setVisibility(View.VISIBLE); }
        else { btnAdminTransfer.setVisibility(View.INVISIBLE); }


        // 리스트 정보로 아이템뷰를 세팅하기 시작
        Member m = list.get(position);

        if(m.getMprofile() != null) {
            Network5.getBitmap(m.getMprofile(), memberProfileImageView);
        }
        txtMid.setText(m.getMid());
        if(m.getMbirth() != null) {
            txtMbirth.setText("생일: " + m.getMbirth().toString());
        }

        // 팀장이면 아이디를 초록색으로 칠하기
        if(m.getMid().equals(adminId)) {
            txtMid.setTextColor(Color.parseColor("#b22222"));
        }


        // 팀장임명 버튼이벤트처리
        btnAdminTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팀장임명 다시 묻는 다이얼로그
                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
                alt_bld.setMessage(list.get(position).getMid() + "님을 팀장으로 임명하시겠습니까?").setCancelable(
                        false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Network3.transferAdmin(list.get(position).getMid(), MainActivity.tid, (FragmentActivity)context);
                                dialog.cancel();
                                Toast.makeText (context, list.get(position).getMid()+"님이 팀장이 되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                // Title for AlertDialog
                alert.setTitle("탈퇴");
                // Icon for AlertDialog
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.show();
            }
        });
        return convertView;
    }
}
