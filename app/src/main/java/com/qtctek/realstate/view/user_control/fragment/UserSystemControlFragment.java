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
import android.widget.Button;

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.adapter.UserControlAdapter;

public class    UserSystemControlFragment extends Fragment implements View.OnClickListener {

    private View mView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Button mBtnBack;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_user_control, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        addControl();
    }

    private void initViews(){
        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);

        this.mBtnBack = ((UserControlActivity) getActivity()).findViewById(R.id.imv_back);

        this.mBtnBack.setOnClickListener(this);
    }

    private void addControl() {
        this.mTabLayout.setVisibility(View.GONE);
        FragmentManager manager = getChildFragmentManager();
        UserControlAdapter adapter = new UserControlAdapter(manager);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mViewPager.setCurrentItem(UserControlActivity.POSITION_FRAGMENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                if(mViewPager.getCurrentItem() == 0){
                    getActivity().finish();
                }
                else{
                    mViewPager.setCurrentItem(0);
                }
        }
    }

}
