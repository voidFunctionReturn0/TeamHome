package com.example.myapplication.team;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.network.Network1;
import com.example.myapplication.team.news.AddNewsFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamHomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private android.support.design.widget.AppBarLayout app_bar;
    private android.support.design.widget.CollapsingToolbarLayout toolbar_layout;
    private CoordinatorLayout.LayoutParams invisibleParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
    public static CoordinatorLayout.LayoutParams visibleParams=null;
    private int app_bar_height = 0;
    private TextView teamName;
    private TextView teamDescr;
    private Teams team;


    public TeamHomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        final View view =  inflater.inflate(R.layout.fragment_team_home, container, false);


        //fragment_team_home.xml의 tabLayout, viewPager 가져오기
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        toolbar_layout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        app_bar = (AppBarLayout)view.findViewById(R.id.app_bar);
        teamName = (TextView)view.findViewById(R.id.teamName);
        teamDescr = (TextView)view.findViewById(R.id.teamDescr);

        Network1.getBitmap(MainActivity.tid, toolbar_layout);
        Network1.getTeamInfo(MainActivity.tid, teamName, teamDescr);

        //소식 추가
        final FloatingActionButton fabAddNews = (FloatingActionButton) view.findViewById(R.id.fabAddNews);
        fabAddNews.setOnClickListener(onClickFab);




        //viewPager에 들어갈 fragment의 배치Adapter 생성 및 viewPager에 해당 Adapter설정
        //tabLayout의 탭과 viewPager 연결
        final TeamAdapter teamAdapter = new TeamAdapter(getChildFragmentManager() );
        viewPager.setAdapter(teamAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != viewPager.getCurrentItem()) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                //소식 외의 tab선택시 teamhome의 이미지 안보이기
                if(tab.getPosition()==1) {
                    app_bar.setLayoutParams(invisibleParams);
                    fabAddNews.setVisibility(View.INVISIBLE);
                    tabLayout.getTabAt(0).setIcon(R.drawable.teamhome_news_icon);
                    tabLayout.getTabAt(1).setIcon(R.drawable.teamhome_data_click_icon);
                    tabLayout.getTabAt(2).setIcon(R.drawable.teamhome_gallary_icon);
                }else if(tab.getPosition()==2) {
                    app_bar.setLayoutParams(invisibleParams);
                    fabAddNews.setVisibility(View.INVISIBLE);
                    tabLayout.getTabAt(0).setIcon(R.drawable.teamhome_news_icon);
                    tabLayout.getTabAt(1).setIcon(R.drawable.teamhome_data_icon);
                    tabLayout.getTabAt(2).setIcon(R.drawable.teamhome_gallary_click_icon);
                }else if(tab.getPosition()==0) {
                    app_bar.setLayoutParams(visibleParams);
                    fabAddNews.setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(0).setIcon(R.drawable.teamhome_news_click_icon);
                    tabLayout.getTabAt(1).setIcon(R.drawable.teamhome_data_icon);
                    tabLayout.getTabAt(2).setIcon(R.drawable.teamhome_gallary_icon);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        //처음 아이콘 셋업
        tabLayout.getTabAt(0).setIcon(R.drawable.teamhome_news_click_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.teamhome_data_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.teamhome_gallary_icon);




        return view;

    }

    private void DialogProgress(){
        ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "잠시만 기다려 주세요 ...", true);
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog.dismiss();*/
    }

    // 소식추가 버튼이벤트 처리
    private View.OnClickListener onClickFab = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new AddNewsFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        }
    };

}
