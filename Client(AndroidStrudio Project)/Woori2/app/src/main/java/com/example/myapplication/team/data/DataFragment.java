package com.example.myapplication.team.data;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.dto.News;
import com.example.myapplication.network.Network1;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment {

    private List<News> list;
    private ListView dataListView;
    //private Button btnDownData;
    private  DataItemViewAdapter dataItemViewAdapter;
    private int tid=1;

    public void setTid(int tid) { this.tid=tid; }

    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);


        dataListView = (ListView)view.findViewById(R.id.dataListView);
        dataItemViewAdapter = new DataItemViewAdapter(view.getContext());
        Network1.getDataList(tid, getActivity(), dataItemViewAdapter);
        dataListView.setAdapter(dataItemViewAdapter);



        return view;
    }

}
