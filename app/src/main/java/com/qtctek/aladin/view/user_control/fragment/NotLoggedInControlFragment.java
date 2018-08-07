package com.qtctek.aladin.view.user_control.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;
import com.qtctek.aladin.view.user_control.adapter.UserControlAdapter;

public class NotLoggedInControlFragment extends Fragment implements View.OnClickListener {

    private UserControlActivity mActivity;

    private View mView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ImageView mImvBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_saved_information, container, false);

        this.mActivity = (UserControlActivity)getActivity();
        initViews();
        addControl();
        addToolbar();

        return mView;
    }

    private void initViews(){
        mViewPager = mView.findViewById(R.id.view_pager);
        mTabLayout = mView.findViewById(R.id.tab_layout);
        this.mImvBack = mActivity.findViewById(R.id.imv_back);

        this.mToolbar = mActivity.findViewById(R.id.toolbar);
        this.mImvBack.setOnClickListener(this);
    }

    private void addControl() {
        FragmentManager manager = getChildFragmentManager();
        UserControlAdapter adapter = new UserControlAdapter(manager);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mViewPager.setCurrentItem(mActivity.positionFragment);

    }

    private void addToolbar(){
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                mActivity.finish();
                break;
        }
    }
}
