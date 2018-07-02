package com.qtctek.realstate.view.new_post.description_information;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

public class DescriptionInformationFragment extends Fragment implements View.OnClickListener, View.OnKeyListener,
        ViewHandleModelNewPost{

    private View mView;

    private TextView mEdtQualityCharacter;
    private EditText mEdtDescription;
    private Button mBtnNext;
    private Button mBtnSaveAndContinue;
    private Dialog mLoadingDialog;
    private boolean mIsEdited = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_description_information, container, false);

        createLoadingDialog();
        initViews();
        handleStart();

        return mView;
    }

    private void initViews(){
        this.mEdtDescription = mView.findViewById(R.id.edt_description);
        this.mBtnNext = mView.findViewById(R.id.btn_next_to);
        this.mBtnSaveAndContinue = mView.findViewById(R.id.btn_save_continue);
        this.mEdtQualityCharacter = mView.findViewById(R.id.txv_quality_character);

        this.mEdtDescription.setOnKeyListener(this);
        this.mBtnNext.setOnClickListener(this);
        this.mEdtQualityCharacter.setOnKeyListener(this);
        this.mBtnSaveAndContinue.setOnClickListener(this);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(getContext());
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void handleStart(){
        this.mEdtDescription.setText(NewPostActivity.POST_SALE.getProduct().getDescription());

        int qualityCharacter = this.mEdtDescription.getText().toString().trim().length();

        String text = (2000 - qualityCharacter) + "";
        this.mEdtQualityCharacter.setText(text);
    }

    private void handleNext(){

        if(!mIsEdited){
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(3);
            return;
        }

        NewPostActivity.POST_SALE.getProduct().setDescription(this.mEdtDescription.getText().toString().trim());

        mLoadingDialog.show();
        new PresenterNewPost(this).handleUpdateDescriptionInformation(NewPostActivity.POST_SALE.getProduct());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save_continue:
                handleNext();
                break;
            case R.id.btn_next_to:
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        int qualityCharacter = this.mEdtDescription.getText().toString().length();
        //xét thêm điều kiện ở đây
        if(qualityCharacter >= 2000){
            this.mEdtQualityCharacter.setText("0");

            if(keyCode >= 8 && keyCode <= 16 || keyCode >= 29 && keyCode <= 54){
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
    public void onUpdateNormalInformation(boolean status) {

    }

    @Override
    public void onUpdateMoreInformation(boolean status) {

    }

    @Override
    public void onUpdateDescriptionInformation(boolean status) {
        mLoadingDialog.dismiss();
        if(status){
            mIsEdited = false;
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(3);
        }
        else{
            Toast.makeText(getContext(), "Có lỗi xảy ra trong việc lưu dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateMapInformation(boolean status) {

    }

    @Override
    public void onUpdateContactInformation(boolean status) {

    }

    @Override
    public void onDeleteFile(boolean status) {

    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }
}
