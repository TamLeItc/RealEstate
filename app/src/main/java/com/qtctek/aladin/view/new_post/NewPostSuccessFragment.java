package com.qtctek.aladin.view.new_post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;

public class NewPostSuccessFragment extends Fragment implements View.OnClickListener {

    private NewPostActivity mActivity;

    private View mView;

    private Button mBtnViewPostedPost;
    private Button mBtnNewPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_post_successful, container, false);

        mActivity = (NewPostActivity) getActivity();

        initViews();

        return mView;
    }

    private void initViews(){
        this.mBtnNewPost = mView.findViewById(R.id.btn_new_post);
        this.mBtnViewPostedPost = mView.findViewById(R.id.btn_posted_post);

        this.mBtnNewPost.setOnClickListener(this);
        this.mBtnViewPostedPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_new_post:
                Intent intent = new Intent(getContext(), NewPostActivity.class);
                startActivity(intent);
                mActivity.finish();
                break;
            case R.id.btn_posted_post:
                Intent intent1 = new Intent(getContext(), UserControlActivity.class);
                intent1.putExtra(Constant.FRAGMENT, 1);
                startActivity(intent1);
                mActivity.finish();
        }

    }
}
