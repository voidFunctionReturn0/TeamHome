package com.example.myapplication.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.network.Network2;
import com.example.myapplication.team.AddTeamFragment;
import com.example.myapplication.team.TeamHomeFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    //private Button btnAddTeam;
    private GridView gridView;
    private List<Teams> list;
    private ItemViewAdapter itemViewAdapter;
    int homeFragmentId;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.presentFragment = "HomeFragment";
        getActivity().invalidateOptionsMenu();
        MainActivity.toggle.setDrawerIndicatorEnabled(true);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        /*btnAddTeam = (Button)view.findViewById(R.id.btnAddTeam);
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new AddTeamFragment())
                        .commit();
            }
        });*/

        //데이터
        homeFragmentId = this.getId();
        Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentById
                (homeFragmentId);

        //그리드 뷰에 어댑터 설정
        gridView = (GridView) view.findViewById(R.id.gridView);
        itemViewAdapter = new ItemViewAdapter(getActivity());
        Network2.getTeamListByMid(MainActivity.member.getMid(), fragment, itemViewAdapter);


        //grid뷰 이벤트 처리
        gridView.setOnItemClickListener(onItemClickListener);
        gridView.setOnItemLongClickListener(onItemLongClickListener);
        return view;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new
            AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Review review = list.get(position);

                    Teams team = (Teams) itemViewAdapter.getItem(position);

                    if(team.getTid() == 0){
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, new AddTeamFragment())
                                .commit();
                    }else {
                        TeamHomeFragment teamHomeFragment =new TeamHomeFragment();
                        MainActivity.tid = team.getTid();
                        Log.i("myinfo",MainActivity.tid +"");
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, teamHomeFragment)
                                .addToBackStack(null) //fragment 스택 쌓기!! 신기아술
                                .commit();
                        MainActivity.presentFragment = "TeamHomeFragment";
                        getActivity().invalidateOptionsMenu();
                    }

                }
            };

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new
            AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            };
}
