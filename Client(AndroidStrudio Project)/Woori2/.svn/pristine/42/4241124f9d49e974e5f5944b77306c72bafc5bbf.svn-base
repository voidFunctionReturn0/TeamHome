package com.example.myapplication.team.news;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.Newscomment;
import com.example.myapplication.network.Network1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-12.
 */
public class CommentItemViewAdapter extends BaseAdapter{
    private List<Newscomment> commentList = new ArrayList<>();
    private Context context;
    private Fragment fragment;
    private Newscomment comment;

    public void setFragment(Fragment fragment) { this.fragment = fragment; }

    public void setCommentList(List<Newscomment> commentList) {
        this.commentList = commentList;
    }

    public CommentItemViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.comment_item_view, parent, false);
        }

        //댓글 레이아웃의 뷰 및 버튼
        ImageView commentMemberProfile = (ImageView)convertView.findViewById(R.id.commentMemberProfile);
        TextView commentMemberId = (TextView)convertView.findViewById(R.id.commentMemberId);
        TextView commentDate = (TextView)convertView.findViewById(R.id.commentDate);
        EditText commentContent = (EditText)convertView.findViewById(R.id.commentContent);
        Button btnDeleteComment = (Button)convertView.findViewById(R.id.btnDeleteComment);
        Button btnModifyComment = (Button)convertView.findViewById(R.id.btnModifyComment);
        Button btnCancelComment = (Button)convertView.findViewById(R.id.btnCancelComment);
        final LinearLayout normalComment = (LinearLayout)convertView.findViewById(R.id.normalComment);
        final LinearLayout modifyComment = (LinearLayout)convertView.findViewById(R.id.modifyComment);
        //final EditText modifyCommentEditText = (EditText)convertView.findViewById(R.id.modifyCommentEditText);
        final int layoutHeight = normalComment.getMeasuredHeight();




        //view생성 시 position값
        comment = commentList.get(position);

        Network1.getBitmap(comment.getMprofile(),commentMemberProfile);

        commentMemberId.setText(comment.getMid());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        commentDate.setText(sdf.format(comment.getCdate()));
        commentContent.setText(comment.getCcontent());


       /* AppCompatActivity activity = (AppCompatActivity)convertView.getContext();
        final Fragment fragment1 = activity.getSupportFragmentManager().getFragments().get(0);*/

        btnDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭된 view의 position값
                Newscomment nc=commentList.get(position);
                Network1.deleteComment(nc.getCid(), fragment);
            }
        });

        btnModifyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("mylog", "댓글 수정 버튼 클릭");
                modifyComment.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
                modifyComment.setWeightSum(1);
                normalComment.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                normalComment.setWeightSum(0);

            }
        });


        btnCancelComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("mylog", "댓글 수정 취소 버튼 클릭");
                normalComment.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
                normalComment.setWeightSum(1);
                modifyComment.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                modifyComment.setWeightSum(0);
            }
        });




        return convertView;
    }
}
