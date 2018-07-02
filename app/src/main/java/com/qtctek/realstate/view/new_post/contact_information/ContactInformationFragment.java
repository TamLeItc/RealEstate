package com.qtctek.realstate.view.new_post.contact_information;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

public class ContactInformationFragment extends Fragment implements View.OnClickListener, ViewHandleModelNewPost, View.OnKeyListener {

    private View mView;
    private EditText mEdtContactName;
    private EditText mEdtContactNumberPhone;
    private Button mBtnPost;
    private Button mBtnSave;
    private Dialog mLoadingDialog;

    private boolean mIsEdit = false;

    private PresenterNewPost mPresenterNewPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact_information, container, false);


        mPresenterNewPost = new PresenterNewPost(this);

        createLoadingDialog();
        initViews();
        handleStart();

        return mView;
    }

    private void initViews(){
        this.mEdtContactName = mView.findViewById(R.id.edt_contact_name);
        this.mEdtContactNumberPhone = mView.findViewById(R.id.edt_contact_number_phone);
        this.mBtnPost = mView.findViewById(R.id.btn_post);
        this.mBtnSave = mView.findViewById(R.id.btn_save);

        this.mBtnPost.setOnClickListener(this);
        this.mBtnSave.setOnClickListener(this);
        this.mEdtContactNumberPhone.setOnKeyListener(this);
        this.mEdtContactName.setOnKeyListener(this);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(getContext());
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void handleStart(){
        this.mEdtContactName.setText(NewPostActivity.POST_SALE.getContactName());
        this.mEdtContactNumberPhone.setText(NewPostActivity.POST_SALE.getContactNumberPhone());
    }

    private void handleSave(){
        NewPostActivity.POST_SALE.setContactName(this.mEdtContactName.getText().toString());
        NewPostActivity.POST_SALE.setContactNumberPhone(this.mEdtContactNumberPhone.getText().toString());

        mLoadingDialog.show();
        mPresenterNewPost.handleUpdateContactInformation(NewPostActivity.POST_SALE);
    }

    private void handleNext(){
        if(mIsEdit){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setMessage("Bạn chưa lưu thông tin liên lạc. Bạn có muốn tiếp tục?")
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLoadingDialog.show();
                            mPresenterNewPost.handleExcutePost(NewPostActivity.POST_SALE.getId());
                        }
                    })
                    .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
        else{
            mLoadingDialog.show();
            mPresenterNewPost.handleExcutePost(NewPostActivity.POST_SALE.getId());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                handleSave();
                break;
            case R.id.btn_post:
                handleNext();
                break;
        }
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

    }

    @Override
    public void onUpdateMapInformation(boolean status) {

    }

    @Override
    public void onUpdateContactInformation(boolean status) {
        mLoadingDialog.dismiss();
        if(status){
            Toast.makeText(getContext(), "Lưu thông tin liên lạc thành công", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "Có lỗi xảy ra trong việc lưu dữ liệu", Toast.LENGTH_SHORT).show();
        }
        mIsEdit = false;
    }

    @Override
    public void onDeleteFile(boolean status) {

    }

    @Override
    public void onUpdateHandlePost(boolean status) {
        mLoadingDialog.dismiss();
        if(status){
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(5);
        }
        else{
            Toast.makeText(getContext(), "Có lỗi xảy ra trong việc thực hiện đăng bài", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(this.mEdtContactName.getText().toString().trim().length() > 0
                || this.mEdtContactNumberPhone.getText().toString().trim().length() > 0){
            mIsEdit = true;
        }
        else{
            mIsEdit = false;
        }
        return false;
    }
}
