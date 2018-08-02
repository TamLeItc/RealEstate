package com.qtctek.realstate.view.user_control.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.adapter.UserControlAdapter;

public class LoggedInControlFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private UserControlActivity mActivity;

    private View mView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView mImvBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_saved_information, container, false);

        this.mActivity = (UserControlActivity) getActivity();
        initViews();
        addControl();

        return mView;
    }
    
    private void initViews(){
        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);

        this.mImvBack = mActivity.findViewById(R.id.imv_back);

        this.mImvBack.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    private void addControl() {
        this.mTabLayout.setVisibility(View.GONE);
        FragmentManager manager = getChildFragmentManager();
        UserControlAdapter adapter = new UserControlAdapter(manager);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mViewPager.setCurrentItem(mActivity.positionFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                if(mActivity.isRequireAccountManagement){
                    if(mViewPager.getCurrentItem() == 0){
                        mActivity.finish();
                    }
                    else{
                        mViewPager.setCurrentItem(0);
                    }
                }
                else{
                    mActivity.finish();
                }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 1){
            mActivity.handleShowButtonFilter(UserControlActivity.POST);

            if(MainActivity.USER.getLevel() == 1 || MainActivity.USER.getLevel() == 2){
                mActivity.txvToolbarTitle.setText(getResources().getString(R.string.post_management));
            }
            else{
                mActivity.txvToolbarTitle.setText(getResources().getString(R.string.posted_post_management));
            }
        }
        else if(position == 2 && MainActivity.USER.getLevel() == 1){
            mActivity.handleShowButtonFilter(UserControlActivity.USER);

            mActivity.txvToolbarTitle.setText(getResources().getString(R.string.user_management));
        }
        else{

            if(MainActivity.USER.getLevel() == 1 && position == 3 || position == 2){
                mActivity.txvToolbarTitle.setText(getResources().getString(R.string.saved_post));
            }
            else if(MainActivity.USER.getLevel() == 1 && position == 4 || position == 3){
                mActivity.txvToolbarTitle.setText(getResources().getString(R.string.saved_search));
            }
            else{
                mActivity.txvToolbarTitle.setText(getResources().getString(R.string.account_manage));
            }

            mActivity.handleShowButtonFilter(UserControlActivity.OTHER);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
