package com.example.myapplication.team.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.News;
import com.example.myapplication.network.Network1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016-07-06.
 */
public class NewsItemViewAdapter extends BaseAdapter {
    private List<News> list=new ArrayList<>();

    public void setList(List<News> list) {
        this.list = list;
    }

    private Context context;

    public NewsItemViewAdapter(Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.news_item_view, parent, false);
        }

        ImageView newsMemberImageView;
        TextView newsNameTextView;
        TextView newsDateTextView;
        TextView newsContentTextView;
        ImageView newsPhotoImageView;
        TextView newsTimeTextView;
        TextView commentNumber;
        Button btnIsData;

        newsMemberImageView = (ImageView)convertView.findViewById(R.id.newsMemberImageView);
        newsNameTextView = (TextView)convertView.findViewById(R.id.newsNameTextView);
        newsDateTextView = (TextView)convertView.findViewById(R.id.newsDateTextView);
        newsContentTextView = (TextView)convertView.findViewById(R.id.newsContentTextView);
        newsPhotoImageView = (ImageView)convertView.findViewById(R.id.newsPhotoImageView);
        newsTimeTextView = (TextView)convertView.findViewById(R.id.newsTimeTextView);
        commentNumber = (TextView)convertView.findViewById(R.id.commentNumber);
        btnIsData = (Button)convertView.findViewById(R.id.btnIsData);

        News news = list.get(position);

        SimpleDateFormat sdfDay = new SimpleDateFormat("MMMM dd, yyyy", new Locale("en", "US"));
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        //newsMemberImageView.setImageResource(R.drawable.member09);

        Network1.getBitmap(news.getMprofile(), newsMemberImageView);


        newsNameTextView.setText(news.getMid());
        newsDateTextView.setText(sdfDay.format(news.getNdate()));
        newsTimeTextView.setText(sdfTime.format(news.getNdate()));
        newsContentTextView.setText(news.getNcontent());

        Network1.getTotalCommentNo(news.getNid(), commentNumber);

        Network1.getBitmap("photo/" + news.getNphotoname(), newsPhotoImageView);

        if(news.getNdataname().equals("")) {
            btnIsData.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
