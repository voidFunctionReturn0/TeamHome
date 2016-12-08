package com.example.myapplication.team.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.News;
import com.example.myapplication.network.Network1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-06.
 */
public class PhotoItemViewAdapter extends BaseAdapter {
    private List<News> list=new ArrayList<>();

    public void setList(List<News> list) {
        this.list = list;
    }

    public List<News> getList() {
        return list;
    }

    private Context context;

    public PhotoItemViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.photo_item_view, parent, false);
        }

        ImageView photoImageView;
        ImageView photoMemberImageView;
        TextView photoNameTextView;
        TextView photoDateTextView;
        TextView photoContentTextView;


        photoImageView = (ImageView)convertView.findViewById(R.id.photoImageView);
        /*photoMemberImageView = (ImageView)convertView.findViewById(R.id.photoMemberImageView);
        photoNameTextView = (TextView)convertView.findViewById(R.id.photoNameTextView);
        photoDateTextView = (TextView)convertView.findViewById(R.id.photoDateTextView);
        photoContentTextView = (TextView)convertView.findViewById(R.id.photoContentTextView);*/



        News news = list.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Network1.getBitmap(news.getMprofile(), photoMemberImageView);
        Network1.getBitmap("photo/"+news.getNphotoname(), photoImageView);
        /*photoNameTextView.setText(news.getMid());
        photoDateTextView.setText(sdf.format(news.getNdate()));
        photoContentTextView.setText(news.getNcontent());*/



        return convertView;
    }


}
