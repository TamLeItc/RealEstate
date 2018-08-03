package com.qtctek.aladin.view.user_control.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.general.Constant;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.user_action.activity.UserActionActivity;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;

public class UserControlFragment extends Fragment implements View.OnClickListener, AlertHelper.AlertHelperCallback {

    private UserControlActivity mActivity;

    private View mView;

    @SuppressLint("StaticFieldLeak")
    public static TextView TXV_USER_NAME_TITLE;
    @SuppressLint("StaticFieldLeak")
    public static TextView TXV_USER_NAME_LOGOUT;

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
    private LinearLayout mLlLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_user_control, container, false);

        this.mActivity = (UserControlActivity)getActivity();
        initViews();
        changeUserName();

        return this.mView;
    }

    private void initViews(){
        TXV_USER_NAME_TITLE = mView.findViewById(R.id.txv_user_name_title);
        TXV_USER_NAME_LOGOUT = mView.findViewById(R.id.txv_user_name_logout);
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
        this.mLlLogout = mView.findViewById(R.id.ll_logout);

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
        this.mLlLogout.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    public void changeUserName(){
        TXV_USER_NAME_TITLE.setText(MainActivity.USER.getFullName() + ",");
        TXV_USER_NAME_LOGOUT.setText(" (" + MainActivity.USER.getFullName() + ")");
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
                mActivity.getToastHelper().toast("Chức năng đang được phát triển." +
                        " Vui lòng quay lại sau", ToastHelper.LENGTH_SHORT);
                break;
            case R.id.txv_update_information:
                Intent intent1 = new Intent(mActivity, UserActionActivity.class);
                intent1.putExtra(Constant.OPTION, Constant.UPDATE_ACCOUNT_INFORMATION);
                startActivity(intent1);
                mActivity.finish();
                break;
            case R.id.ll_logout:
                mActivity.getAlertHelper().setCallback(this);
                mActivity.getAlertHelper().alert(getResources().getString(R.string.log_out), getResources().getString(R.string.log_out_notifaction), false,
                        getResources().getString(R.string.ok), getResources().getString(R.string.cancel), Constant.LOGOUT);
                break;
        }
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.LOGOUT){
            MainActivity.USER.clearData();
            Intent intent = new Intent(getActivity(), UserActionActivity.class);
            startActivity(intent);
            mActivity.finish();
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
