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
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

public class UserControlFragment extends Fragment implements View.OnClickListener, AlertHelper.AlertHelperCallback {

    private View mView;

    private TextView mTxvTitle;
    private TextView mTxvNewPost;
    private View mView1;
    private TextView mTxvPostedPost;
    private TextView mTxvPostManagement;
    private TextView mTxvUserManagement;
    private TextView mTxvSavedPost;
    private TextView mTxvSavedSearch;
    private TextView mTxvIntroduction;
    private TextView mTxvFeedback;
    private TextView mTxvUpdateInformation;
    private TextView mTxvLogout;

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
        handleStart();
    }

    private void initViews(){
        this.mTxvTitle = mView.findViewById(R.id.txv_title);
        this.mTxvNewPost = mView.findViewById(R.id.txv_new_post);
        this.mView1 = mView.findViewById(R.id.view_1);
        this.mTxvPostedPost = mView.findViewById(R.id.txv_posted_post);
        this.mTxvPostManagement = mView.findViewById(R.id.txv_post_management);
        this.mTxvUserManagement = mView.findViewById(R.id.txv_user_management);
        this.mTxvSavedPost = mView.findViewById(R.id.txv_saved_post);
        this.mTxvSavedSearch = mView.findViewById(R.id.txv_saved_search);
        this.mTxvIntroduction = mView.findViewById(R.id.txv_introduction);
        this.mTxvFeedback = mView.findViewById(R.id.txv_feedback);
        this.mTxvUpdateInformation = mView.findViewById(R.id.txv_update_information);
        this.mTxvLogout = mView.findViewById(R.id.txv_logout);

        if(MainActivity.USER.getLevel() == 1){
            this.mTxvNewPost.setVisibility(View.GONE);
            this.mView1.setVisibility(View.GONE);
            this.mTxvPostedPost.setVisibility(View.GONE);
        }
        else if(MainActivity.USER.getLevel() == 2){
            this.mTxvNewPost.setVisibility(View.GONE);
            this.mView1.setVisibility(View.GONE);
            this.mTxvPostedPost.setVisibility(View.GONE);
            this.mTxvUserManagement.setVisibility(View.GONE);
        }
        else if(MainActivity.USER.getLevel() == 3){
            this.mTxvPostManagement.setVisibility(View.GONE);
            this.mTxvUserManagement.setVisibility(View.GONE);
        }

        this.mTxvNewPost.setOnClickListener(this);
        this.mTxvPostedPost.setOnClickListener(this);
        this.mTxvPostManagement.setOnClickListener(this);
        this.mTxvUserManagement.setOnClickListener(this);
        this.mTxvUpdateInformation.setOnClickListener(this);
        this.mTxvSavedPost.setOnClickListener(this);
        this.mTxvSavedSearch.setOnClickListener(this);
        this.mTxvFeedback.setOnClickListener(this);
        this.mTxvIntroduction.setOnClickListener(this);
        this.mTxvLogout.setOnClickListener(this);
    }

    public void handleStart(){
        this.mTxvTitle.append(MainActivity.USER.getFullName() + ",");
        this.mTxvLogout.append(" (" + MainActivity.USER.getFullName() + ")");
    }


    @Override
    public void onClick(View v) {
        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        switch (v.getId()){
            case R.id.txv_new_post:
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                intent.putExtra("post_id", -1);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.txv_posted_post:
                viewPager.setCurrentItem(1);
                break;
            case R.id.txv_post_management:
                viewPager.setCurrentItem(1);
                break;
            case R.id.txv_user_management:
                viewPager.setCurrentItem(2);
                break;
            case R.id.txv_saved_post:
                if(MainActivity.USER.getLevel() == 1){
                    viewPager.setCurrentItem(3);
                }
                else{
                    viewPager.setCurrentItem(2);
                }
                break;
            case R.id.txv_saved_search:
                if(MainActivity.USER.getLevel() == 1){
                    viewPager.setCurrentItem(4);
                }
                else{
                    viewPager.setCurrentItem(3);
                }
                break;
            case R.id.txv_introduction:
            case R.id.txv_feedback:
                ((UserControlActivity)getActivity()).toastHelper.toast("Chức năng đang được phát triển." +
                        " Vui lòng quay lại sau", ToastHelper.LENGTH_SHORT);
                break;
            case R.id.txv_update_information:
                Intent intent1 = new Intent(getActivity(), UserActionActivity.class);
                intent1.putExtra("option", "update_user");
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.txv_logout:
                ((UserControlActivity)getActivity()).alertHelper.setCallback(this);
                ((UserControlActivity)getActivity()).alertHelper.alert("Xác nhận", "Bạn có chắc chắn muốn đăng xuất", false,
                        "Xác nhận", "Hủy bỏ", Constant.LOGOUT);
                break;
        }
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.LOGOUT){
            MainActivity.USER.clearData();
            Intent intent = new Intent(getActivity(), UserActionActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
