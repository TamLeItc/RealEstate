package com.qtctek.aladin.view.post_news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qtctek.aladin.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.aladin.view.post_news.fragment.MapPostNewsFragment;

public class MainAdapter extends FragmentStatePagerAdapter {

    private MapPostNewsFragment mMapPostNewsFragment = new MapPostNewsFragment();
    private ListPostNewsFragment mListPostNewsFragment = new ListPostNewsFragment();

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg = null;
        switch (position){

            case 0:
                frg = mMapPostNewsFragment;
                break;
            case 1:
                frg = mListPostNewsFragment;
                break;
        }
        return frg;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
