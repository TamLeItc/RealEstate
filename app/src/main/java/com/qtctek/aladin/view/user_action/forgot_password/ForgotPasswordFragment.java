package com.qtctek.aladin.view.user_action.forgot_password;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.user_action.forgot_password.PresenterForgotPassword;
import com.qtctek.aladin.common.FormatPattern;
import com.qtctek.aladin.common.HashMD5;
import com.qtctek.aladin.common.RandomString;
import com.qtctek.aladin.common.send_gmail.GMailSender;
import com.qtctek.aladin.presenter.user_action.register.PresenterRegister;
import com.qtctek.aladin.view.user_action.activity.UserActionActivity;
import com.qtctek.aladin.view.user_action.register.ViewHandleRegister;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, ViewHandleForgotPassword ,
        ViewHandleRegister, AlertHelper.AlertHelperCallback {

    private UserActionActivity mActivity;

    private View mView;

    private EditText mEdtEmail;

    private PresenterRegister mPresenterRegister;
    private PresenterForgotPassword mPresenterForgotPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        this.mActivity = (UserActionActivity) getActivity();

        initViews();
        this.mPresenterRegister = new PresenterRegister(this);
        this.mPresenterForgotPassword = new PresenterForgotPassword(this);

        return this.mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews(){
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        Button mBtnConfirm = mView.findViewById(R.id.btn_confirm);

        mBtnConfirm.setOnClickListener(this);
    }

    private void handleForgotPassword(){
        if(this.mEdtEmail.getText().toString().trim().equals("")){
            mActivity.getToastHelper().toast(R.string.please_enter_email, ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
        }
        else if(!FormatPattern.checkEmail(this.mEdtEmail.getText().toString().trim())){
            mActivity.getToastHelper().toast(R.string.email_incorrect, ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
        }
        else{
            mActivity.getDialogHelper().show();
            this.mPresenterRegister.handleCheckEmail(this.mEdtEmail.getText().toString().trim(), "");
        }
    }

    private boolean sendConfirmCodeToGMail(final String newPasswork){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender gMailSender = new GMailSender();
                    gMailSender.sendMail("Reset mật khẩu tài khoản Real Estate", String.format("Mật khẩu tàikhoản Real Estate mới là: %s", newPasswork), mEdtEmail.getText().toString().trim());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    @Override
    public void onRegisterSuccessful() {

    }

    @Override
    public void onRegisterError(String error) {

    }

    @Override
    public void onCheckExistEmail(String message) {
        mActivity.getDialogHelper().dismiss();
        if(message.equals(Constant.EMAIL_EXISTED)){
            String mNewPassword = RandomString.getSaltString();
            if(sendConfirmCodeToGMail(mNewPassword)){
                mPresenterForgotPassword.handleResetPassword(this.mEdtEmail.getText().toString().trim(),
                        HashMD5.md5(mNewPassword));
            }
            else{
                mActivity.getAlertHelper().setCallback(this);
                mActivity.getAlertHelper().alert(R.string.error, R.string.reset_password_failed,
                        false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
            }

        }
        else{
            mActivity.getAlertHelper().setCallback(this);
            mActivity.getAlertHelper().alert(R.string.error, R.string.account_not_exist,
                    false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
        }
    }

    @Override
    public void onConnectServerError(String s) {
        mActivity.getDialogHelper().dismiss();
        mActivity.getAlertHelper().setCallback(this);
        mActivity.getAlertHelper().alert(R.string.error,
                R.string.error_connect_notification, false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
    }

    @Override
    public void onResetPasswordSuccessful() {
        mActivity.getDialogHelper().dismiss();
        mActivity.getAlertHelper().setCallback(this);
        mActivity.getAlertHelper().alert(R.string.error,
                R.string.reset_password_successful, false, R.string.ok, Constant.HANDLE_SUCCESSFUL);

    }

    @Override
    public void onResetPasswordError() {

        mActivity.getDialogHelper().dismiss();

        mActivity.getAlertHelper().setCallback(this);
        mActivity.getAlertHelper().alert(R.string.error,
                R.string.reset_password_failed, false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                handleForgotPassword();
                break;
        }
    }

    @Override
    public void onDestroyView() {

        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.HANDLE_SUCCESSFUL){
            ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
