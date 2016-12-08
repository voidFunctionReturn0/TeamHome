package com.example.myapplication.team.photo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.myapplication.dto.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-20.
 */
public class PhotoPagerAdapter extends FragmentStatePagerAdapter {
    private List<News> list=new ArrayList<>();

    public void setList(List<News> list) {
        this.list = list;
    }

    public PhotoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //News news=list.get(position);
        for(int i=0; i<list.size();i++) {
            if(position==i) {
                News news = list.get(i);
                DetailPhotoItemFragment detailPhotoItemFragment = new DetailPhotoItemFragment();
                detailPhotoItemFragment.setNews(news);
                return detailPhotoItemFragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
