package com.example.myapplication.team.photo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.dto.News;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPhotoFragment extends Fragment {
    private int position;
    private List<News> list=new ArrayList<>();

    public void setList(List<News> list) {
        this.list = list;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //photo클릭시 viewpager에 필요한 필드
    private ViewPager viewPager;
    private PhotoPagerAdapter adapter;
    private int pageSelectedPosition, pageScrollState;



    public DetailPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detail_photo, container, false);


        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin((int)(10*getResources().getDisplayMetrics().density));
        adapter = new PhotoPagerAdapter(getChildFragmentManager());
        adapter.setList(list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //스크롤 중일 때
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(pageScrollState == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (position == pageSelectedPosition) {
                        //오른쪽으로 이동
                        if (positionOffset < 0.5) {
                            Log.i("mylog", "onPageScrolled: 오른쪽 페이지로 이동(현재 페이지에 가까움) position(" + position + "), positonOffset(" + positionOffset + "), positonOffsetPixels("+positionOffsetPixels + ")");
                        } else {
                            Log.i("mylog", "onPageScrolled: 오른쪽 페이지로 이동(오른쪽 페이지에 가까움) position(" + position + "), positonOffset(" + positionOffset + "), positonOffsetPixels("+positionOffsetPixels + ")");
                        }
                    } else {
                        //왼쪽으로 이동(position 값이 pageSelectedPosition 보다 1 작음)
                        if (positionOffset < 0.5) {
                            Log.i("mylog", "onPageScrolled: 왼쪽 페이지로 이동(왼쪽 페이지에 가까움) position(" + position + "), positonOffset(" + positionOffset + "), positonOffsetPixels("+positionOffsetPixels + ")");
                        } else {
                            Log.i("mylog", "onPageScrolled: 왼쪽 페이지로 이동(현재 페이지에 가까움) position(" + position + "), positonOffset(" + positionOffset + "), positonOffsetPixels("+positionOffsetPixels + ")");
                        }
                    }
                }
            }
            //스크롤이 끝나고 페이지가 선택되었을 때
            @Override
            public void onPageSelected(int position) {
                Log.i("mylog", "onPageSelected: " + position);
                pageSelectedPosition = position;
            }
            //스크롤의 상태 변화가 있을 때
            @Override
            public void onPageScrollStateChanged(int state) {
                pageScrollState = state;
                if(state == ViewPager.SCROLL_STATE_DRAGGING) {
                    Log.i("mylog", "onPageScrollStateChanged: SCROLL_STATE_DRAGGING");
                } else if(state == ViewPager.SCROLL_STATE_IDLE) {
                    Log.i("mylog", "onPageScrollStateChanged: SCROLL_STATE_IDLE");
                } else if(state == ViewPager.SCROLL_STATE_SETTLING) {
                    Log.i("mylog", "onPageScrollStateChanged: SCROLL_STATE_SETTLING");
                }
            }
        });

        return view;
    }

}
