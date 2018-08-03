package com.qtctek.aladin.view.new_post.description_information;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.new_post.PresenterNewPost;
import com.qtctek.aladin.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;

public class DescriptionInformationFragment extends Fragment implements View.OnClickListener, View.OnKeyListener,
        ViewHandleModelNewPost{

    private NewPostActivity mActivity;

    private View mView;

    private TextView mEdtQualityCharacter;
    private EditText mEdtDescription;
    private Button mBtnNext;

    private boolean mIsEdited = false;
    public boolean isSaveTemp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_description_information, container, false);

        this.mActivity = (NewPostActivity) getActivity();

        initViews();
        handleStart();

        return mView;
    }

    private void initViews(){
        this.mEdtDescription = mView.findViewById(R.id.edt_description);
        this.mBtnNext = mView.findViewById(R.id.btn_next_to);
        this.mEdtQualityCharacter = mView.findViewById(R.id.txv_quality_character);

        this.mEdtDescription.setOnKeyListener(this);
        this.mBtnNext.setOnClickListener(this);
        this.mEdtQualityCharacter.setOnKeyListener(this);
    }

    private void handleStart(){
        this.mEdtDescription.setText(mActivity.product.getDescription());

        int qualityCharacter = this.mEdtDescription.getText().toString().trim().length();

        String text = (2000 - qualityCharacter) + "";
        this.mEdtQualityCharacter.setText(text);
    }

    public boolean checkSavedInformation(){
        if(mIsEdited){
            return false;
        }
        else{
            return true;
        }
    }

    public void handleSaveDescriptionInformation(){
        if(!mIsEdited){
            if(!isSaveTemp) {
                ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
                viewPager.setCurrentItem(3);
            }
            return;
        }

        mActivity.product.setDescription(this.mEdtDescription.getText().toString().trim());

        mActivity.getDialogHelper().show();
        new PresenterNewPost(this)
                .handleUpdateDescriptionInformation(mActivity.product.getId(),
                        mActivity.product.getDescription());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next_to:
                isSaveTemp = false;
                handleSaveDescriptionInformation();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        int qualityCharacter = this.mEdtDescription.getText().toString().length();
        //xét thêm điều kiện ở đây
        if(qualityCharacter >= 2000){
            this.mEdtQualityCharacter.setText("0");

            if(keyCode >= 7 && keyCode <= 16 || keyCode >= 29 && keyCode <= 54){
                return true;
            }

            return false;
        }
        else{
            String text = (2000 - qualityCharacter) + "";
            this.mEdtQualityCharacter.setText(text);
        }
        this.mIsEdited = true;
        return false;
    }

    @Override
    public void onInsertBlankPost(boolean status, int postId) {

    }

    @Override
    public void onUploadImages(boolean status) {

    }

    @Override
    public void onUpdateProductInformation(boolean status) {

    }

    @Override
    public void onUpdateDescriptionInformation(boolean status) {
        mActivity.getDialogHelper().dismiss();
        if(status){
            mIsEdited = false;
            if(!isSaveTemp){
                ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
                viewPager.setCurrentItem(3);

            }
            else{
                mActivity.getToastHelper().toast(R.string.save_data_successful, ToastHelper.LENGTH_SHORT);
            }
        }
        else{
            mActivity.getToastHelper().toast(R.string.error_save_data, ToastHelper.LENGTH_SHORT);
        }
    }

    @Override
    public void onUpdateMapInformation(boolean status) {

    }

    @Override
    public void onDeleteFile(boolean status) {

    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }

    @Override
    public void onDestroyView() {
        //clear memory
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }
}
