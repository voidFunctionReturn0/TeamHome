package com.example.myapplication.team;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.myapplication.MainActivity;
import com.example.myapplication.team.data.DataFragment;
import com.example.myapplication.team.news.NewsFragment;
import com.example.myapplication.team.photo.PhotoFragment;

/**
 * Created by Administrator on 2016-07-05.
 */
public class TeamAdapter extends FragmentStatePagerAdapter {
    private int newsFragmentId, dataFragmentId, photoFragmentId;

    public TeamAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            NewsFragment newsFragment = new NewsFragment();
            newsFragmentId = newsFragment.getId();
            newsFragment.setTid(MainActivity.tid);
            return newsFragment;
        }else if(position == 1) {
            DataFragment dataFragment = new DataFragment();
            dataFragmentId = dataFragment.getId();
            dataFragment.setTid(MainActivity.tid);
            return dataFragment;
        }else if(position == 2) {

            PhotoFragment photoFragment = new PhotoFragment();
            photoFragmentId = photoFragment.getId();
            photoFragment.setTid(MainActivity.tid);
            return photoFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //아이템 갱신x
        //super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return "NEWS";
        }else if(position == 1) {
            return "DATA";
        }else if(position == 2) {
            return "GALLAY";
        }
        return null;
    }
}
