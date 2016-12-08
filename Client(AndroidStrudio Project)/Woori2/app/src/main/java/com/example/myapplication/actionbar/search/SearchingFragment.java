package com.example.myapplication.actionbar.search;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.network.Network2;
import com.example.myapplication.team.TeamHomeFragment;

import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class SearchingFragment extends Fragment {

    private List<Teams> list;
    private ListView listView;
    private SearchView searchView;
    private SearchItemViewAdapter searchItemViewAdapter;
    int searchFragmentId;
    public SearchingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_searching, container, false);
        //데이터
        searchFragmentId = this.getId();
        Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentById(searchFragmentId);
        searchView = (SearchView) view.findViewById(R.id.searchView);

/*        SearchManager searchManager = (SearchManager)  getSystemService(Context.SEARCH_SERVICE);
        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }*/
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search Keyword");



        //리스트 뷰에 어댑터 설정
        listView = (ListView) view.findViewById(R.id.searchListView);
        searchItemViewAdapter = new SearchItemViewAdapter(view.getContext());
        Network2.getTeamListByKeyword(MainActivity.member.getMid(),"", fragment, searchItemViewAdapter);
        listView.setAdapter(searchItemViewAdapter);

        //리스트뷰 이벤트 처리
        listView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemLongClickListener(onItemLongClickListener);
        //서치뷰 이벤트 처리
        searchView.setOnQueryTextListener(queryTextListener);
        return view;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public boolean onQueryTextSubmit(String keword) {
            // TODO Auto-generated method stub
            //searchItem.collapseActionView();

            Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentById(searchFragmentId);
            Network2.getTeamListByKeyword(MainActivity.member.getMid(), keword, fragment, searchItemViewAdapter);
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            // TODO Auto-generated method stub
            //    ((Searchable)mActivity).updateQuery(newText);
            return false;
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Review review = list.get(position);
            Teams team = (Teams)searchItemViewAdapter.getItem(position);
            TeamHomeFragment teamHomeFragment =new TeamHomeFragment();
            MainActivity.tid = team.getTid();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, teamHomeFragment)
                    .addToBackStack(null)
                    .commit();
            MainActivity.presentFragment = "TeamHomeFragment";
            getActivity().invalidateOptionsMenu();

        }
    };

    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    };
}
