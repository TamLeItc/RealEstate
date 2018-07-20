package com.qtctek.realstate.view.post_news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.realstate.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.realstate.view.post_news.fragment.StartFragment;

public class MainAdapter extends FragmentStatePagerAdapter {

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg = null;
        switch (position){
            case 0:
                frg = new MapPostNewsFragment();
                break;
            case 1:
                frg = new ListPostNewsFragment();
                break;
        }
        return frg;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
