package com.qtctek.realstate.view.user_control.fragment;

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

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.adapter.UserControlAdapter;

public class NotLoggedInControlFragment extends Fragment implements View.OnClickListener {

    private View mView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ImageView mImvBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_saved_information, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        addControl();
        addToolbar();
    }

    private void initViews(){
        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        this.mImvBack = ((UserControlActivity) getActivity()).findViewById(R.id.imv_back);

        this.mToolbar = ((UserControlActivity)getActivity()).findViewById(R.id.toolbar);
        this.mImvBack.setOnClickListener(this);
    }

    private void addControl() {
        FragmentManager manager = getChildFragmentManager();
        UserControlAdapter adapter = new UserControlAdapter(manager);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mViewPager.setCurrentItem(((UserControlActivity)getActivity()).positionFragment);

    }

    private void addToolbar(){
        ((UserControlActivity)getActivity()).setSupportActionBar(mToolbar);
        ((UserControlActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                getActivity().finish();
                break;
        }
    }
}
