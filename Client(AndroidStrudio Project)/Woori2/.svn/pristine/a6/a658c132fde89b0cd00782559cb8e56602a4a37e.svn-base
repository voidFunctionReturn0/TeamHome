package com.example.myapplication.team.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dto.News;
import com.example.myapplication.network.Network1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-06.
 */
public class DataItemViewAdapter extends BaseAdapter {
    private List<News> list=new ArrayList<>();

    public void setList(List<News> list) {
        this.list = list;
    }

    private Context context;

    public DataItemViewAdapter(Context context) {
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
            convertView = layoutInflater.inflate(R.layout.data_item_view, parent, false);
        }

        ImageView dataMemberImageView;
        TextView dataNameTextView;
        TextView dataDateTextView;
        TextView dataContentTextView;
        Button btnDownData;

        dataMemberImageView = (ImageView)convertView.findViewById(R.id.dataMemberImageView);
        dataNameTextView = (TextView)convertView.findViewById(R.id.dataNameTextView);
        dataDateTextView = (TextView)convertView.findViewById(R.id.dataDateTextView);
       // dataContentTextView = (TextView)convertView.findViewById(R.id.dataContentTextView);
        btnDownData = (Button)convertView.findViewById(R.id.btnDownData);

        News news = list.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Network1.getBitmap(news.getMprofile(), dataMemberImageView);




        // "시간_파일이름" 에서 "시간_"을 지운 문자열 보여주기
        int cutIdx = news.getNdataname().indexOf("_");
        String showDataName = news.getNdataname().substring(++cutIdx);


        dataNameTextView.setText(showDataName);
        dataDateTextView.setText(sdf.format(news.getNdate()));
        //dataContentTextView.setText(news.getNcontent());

        btnDownData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "자료 다운로드 클릭", Toast.LENGTH_SHORT).show();
                News n = list.get(position);
                Network1.downloadData(n.getNdataname(), context);
            }
        });

        return convertView;
    }


}
