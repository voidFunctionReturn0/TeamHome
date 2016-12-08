package com.example.myapplication.team.news;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.dto.News;
import com.example.myapplication.network.Network1;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private List<News> list;
    private ListView newsListView;
    private NewsItemViewAdapter newsItemViewAdapter;
    private int newsFrgmentId;
    int tid=1;
    private boolean lastItem;
    private boolean firstItem;



    public void setTid(int tid) { this.tid=tid; }



    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        newsFrgmentId = this.getId();

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        //소식 조회
        newsListView = (ListView)view.findViewById(R.id.newsListView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newsListView.setNestedScrollingEnabled(true);
        }


        newsItemViewAdapter = new NewsItemViewAdapter(view.getContext());
        Network1.getNewsList(tid, getActivity(), newsItemViewAdapter);
        newsListView.setAdapter(newsItemViewAdapter);


        //소식 이벤트 처리
        newsListView.setOnItemClickListener(onNewsItemClickListener);



        /*//소식 리스트 처리
        newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //마지막 아이템이고 스크롤이 더이상 움직이지 않을 때 새로 리스트 갱신 처리
                if(lastItem && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)){
                    //Network1.getNews(MainActivity.tid, getActivity().getSupportFragmentManager().findFragmentById(newsFrgmentId), newsItemViewAdapter);
                    newsListView.setAdapter(newsItemViewAdapter);
                }

                //
                //if(firstItem && (scrollState == AbsListView.OnScrollListener.))

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //마지막 아이템인지 아닌지 체크
                if(totalItemCount>0 && (firstVisibleItem+visibleItemCount>=totalItemCount-1)) {
                    lastItem = true;
                }else {
                    lastItem = false;
                }

            }
        });*/


        return view;

    }

    private AdapterView.OnItemClickListener onNewsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            News news = (News)parent.getItemAtPosition(position);
            //get news 네트워킹
            Network1.getNews(news.getNid(), getActivity());
        }
    };




}
