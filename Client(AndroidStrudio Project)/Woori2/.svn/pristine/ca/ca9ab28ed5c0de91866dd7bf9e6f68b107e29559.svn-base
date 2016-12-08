package com.example.myapplication.team.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.News;
import com.example.myapplication.network.Network1;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyNewsFragment extends Fragment {
    private EditText newsEditText;
    private ImageView modifyNewsMemberImageView;
    private TextView modifyNewsMemberName;
    private TextView modifyNewsDate;
    private Button btnComplete;
    private News news;
    private int ModifyNewsFragmentId;

    public void setNews(News news) { this.news = news;}

    public ModifyNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modify_news, container, false);
        ModifyNewsFragmentId = this.getId();

        modifyNewsMemberImageView = (ImageView)view.findViewById(R.id.modifyNewsMemberImageView);
        modifyNewsMemberName = (TextView)view.findViewById(R.id.modifyNewsMemberName);
        modifyNewsDate = (TextView)view.findViewById(R.id.modifyNewsDate);
        newsEditText = (EditText)view.findViewById(R.id.newsEditText);
        btnComplete = (Button)view.findViewById(R.id.btnComplete);
        Network1.getBitmap(news.getMprofile(),modifyNewsMemberImageView);
        modifyNewsMemberName.setText(news.getMid());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm-ss");
        modifyNewsDate.setText(sdf.format(news.getNdate()));
        newsEditText.setText(news.getNcontent());

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network1.modifyNews(news.getNid(),newsEditText.getText().toString(), getActivity().getSupportFragmentManager().findFragmentById(ModifyNewsFragmentId));
            }
        });

        return view;
    }



}
