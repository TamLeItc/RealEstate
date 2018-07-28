package com.qtctek.realstate.view.new_post.description_information;

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

import com.qtctek.realstate.R;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

public class DescriptionInformationFragment extends Fragment implements View.OnClickListener, View.OnKeyListener,
        ViewHandleModelNewPost{

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
        this.mEdtDescription.setText(((NewPostActivity)getActivity()).product.getDescription());

        int qualityCharacter = this.mEdtDescription.getText().toString().trim().length();

        String text = (2000 - qualityCharacter) + "";
        this.mEdtQualityCharacter.setText(text);
    }

    public void handleSaveDescriptionInformation(){
        if(!mIsEdited){
            if(!isSaveTemp) {
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(3);
            }
            return;
        }

        ((NewPostActivity)getActivity()).product.setDescription(this.mEdtDescription.getText().toString().trim());

        ((NewPostActivity)getActivity()).dialogHelper.show();
        new PresenterNewPost(this)
                .handleUpdateDescriptionInformation(((NewPostActivity)getActivity()).product.getId(),
                        ((NewPostActivity)getActivity()).product.getDescription());
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
        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        if(status){
            mIsEdited = false;
            if(!isSaveTemp){
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(3);

            }
            else{
                ((NewPostActivity)getActivity()).toastHelper.toast("Lưu thành công", ToastHelper.LENGTH_SHORT);
            }
        }
        else{
            ((NewPostActivity)getActivity()).toastHelper.toast("Lỗi lưu dữ liệu", ToastHelper.LENGTH_SHORT);
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
