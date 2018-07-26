package com.qtctek.realstate.view.user_control.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.adapter.UserControlAdapter;
import com.qtctek.realstate.view.user_control.post_management.PostManagementFragment;
import com.qtctek.realstate.view.user_control.user_management.UserManagementFragment;

public class    UserSystemControlFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View mView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Button mBtnBack;

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
        mViewPager.addOnPageChangeListener(this);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 1){
            ((UserControlActivity)getActivity()).showMenuFilter(UserControlActivity.POST_MANAGEMENT);

            ((UserControlActivity)getActivity()).txvToolbarTitle.setText(getResources().getString(R.string.post_management));
        }
        else if(position == 2 && MainActivity.USER.getLevel() == 1){
            ((UserControlActivity)getActivity()).showMenuFilter(UserControlActivity.USER_MANAGEMENT);

            ((UserControlActivity)getActivity()).txvToolbarTitle.setText(getResources().getString(R.string.user_management));
        }
        else{

            ((UserControlActivity)getActivity()).txvToolbarTitle.setText(getResources().getString(R.string.account_manage));

            ((UserControlActivity)getActivity()).showMenuFilter(UserControlActivity.OTHER);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
