package com.example.myapplication.team.photo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.network.Network1;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {
    private GridView photoGridView;
    private PhotoItemViewAdapter photoItemViewAdapter;
    private int tid;



    public void setTid(int tid) {
        this.tid = tid;
    }

    public PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        photoGridView = (GridView) view.findViewById(R.id.photoGridView);
        photoItemViewAdapter=new PhotoItemViewAdapter(view.getContext());
        Network1.getPhotoList(MainActivity.tid, getActivity(), photoItemViewAdapter);
        photoGridView.setAdapter(photoItemViewAdapter);




        //photo 클릭시
        photoGridView.setOnItemClickListener(onItemClick);











        return view;
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DetailPhotoFragment detailPhotoFragment = new DetailPhotoFragment();
            detailPhotoFragment.setList(photoItemViewAdapter.getList());
            detailPhotoFragment.setPosition(position);
            //getActivity().getSupportFragmentManager().beginTransaction().add(detailPh
            // otoFragment, "detailPhotoFragment");
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, detailPhotoFragment, "detailPhotoFragment").commit();
        }
    };


}
