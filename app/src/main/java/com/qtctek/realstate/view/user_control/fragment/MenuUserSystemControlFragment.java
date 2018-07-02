package com.qtctek.realstate.view.user_control.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;

public class MenuUserSystemControlFragment extends Fragment implements View.OnClickListener {

    private View mView;

    private Button mBtnPostManagement;
    private Button mBtnUserManagement;
    private Button mBtnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_option_user_system_control, container, false);

        return this.mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews(){
        this.mBtnPostManagement = mView.findViewById(R.id.btn_post_management);
        this.mBtnUserManagement = mView.findViewById(R.id.btn_user_management);
        this.mBtnLogout = mView.findViewById(R.id.btn_logout);

        //Moderator can't view manager user of app
        if(MainActivity.ROLE_USER.equals("3")){
            this.mBtnUserManagement.setVisibility(View.GONE);
        }

        this.mBtnPostManagement.setOnClickListener(this);
        this.mBtnUserManagement.setOnClickListener(this);
        this.mBtnLogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(MainActivity.ROLE_USER.equals("1")){
            switch (v.getId()){
                case R.id.btn_post_management:
                    ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.btn_user_management:
                    ViewPager viewPager1 = getActivity().findViewById(R.id.view_pager);
                    viewPager1.setCurrentItem(2);
                    break;
                case R.id.btn_logout:
                    Intent intent = new Intent(getActivity(), UserActionActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
            }
        }
        else if(MainActivity.ROLE_USER.equals("3")){
            switch (v.getId()){
                case R.id.btn_post_management:
                    ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.btn_logout:
                    Intent intent = new Intent(getActivity(), UserActionActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
            }
        }
    }
}
