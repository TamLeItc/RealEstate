package com.qtctek.realstate.view.user_action.forgot_password;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_action.forgot_password.PresenterForgotPassword;
import com.qtctek.realstate.common.general.FormatPattern;
import com.qtctek.realstate.common.general.HashMD5;
import com.qtctek.realstate.common.general.RandomString;
import com.qtctek.realstate.common.general.send_gmail.GMailSender;
import com.qtctek.realstate.presenter.user_action.register.PresenterRegister;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_action.register.ViewHandleRegister;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, ViewHandleForgotPassword ,
        ViewHandleRegister, AlertHelper.AlertHelperCallback {

    private View mView;

    private EditText mEdtEmail;
    private Button mBtnConfirm;

    private PresenterRegister mPresenterRegister;
    private PresenterForgotPassword mPresenterForgotPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        initViews();
        this.mPresenterRegister = new PresenterRegister(this);
        this.mPresenterForgotPassword = new PresenterForgotPassword(this);

        return this.mView;
    }

    private void initViews(){
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        this.mBtnConfirm = mView.findViewById(R.id.btn_confirm);

        this.mBtnConfirm.setOnClickListener(this);
    }

    private void handleForgotPassword(){
        if(this.mEdtEmail.getText().toString().trim().equals("")){
            ((UserActionActivity)getActivity()).toastHelper.toast("Vui lòng nhập email", ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
        }
        else if(!FormatPattern.checkEmail(this.mEdtEmail.getText().toString().trim())){
            ((UserActionActivity)getActivity()).toastHelper.toast("Email không chính xác", ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
        }
        else{
            ((UserActionActivity)getActivity()).dialogHelper.show();
            this.mPresenterRegister.handleCheckEmail(this.mEdtEmail.getText().toString().trim(), "");
        }
    }

    private boolean sendConfirmCodeToGMail(final String newPasswork){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender gMailSender = new GMailSender();
                    gMailSender.sendMail("Reset mật khẩu tài khoản Real Estate", "Mật khẩu tài" +
                            "khoản Real Estate mới là: " + newPasswork, mEdtEmail.getText().toString().trim());

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
        ((UserActionActivity)getActivity()).dialogHelper.dismiss();
        if(message.equals("email_existed")){
            String mNewPassword = RandomString.getSaltString();
            if(sendConfirmCodeToGMail(mNewPassword)){
                mPresenterForgotPassword.handleResetPassword(this.mEdtEmail.getText().toString().trim(),
                        HashMD5.md5(mNewPassword));
            }
            else{
                ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
                ((UserActionActivity)getActivity()).alertHelper.alert("Quên mật khẩu",
                        "Reset mật khẩu thất bại",
                        false, "Xác nhận", AlertHelper.ALERT_NO_ACTION);
            }

        }
        else{
            ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
            ((UserActionActivity)getActivity()).alertHelper.alert("Quên mật khẩu",
                    "Tài khoản không tồn tại. Vui lòng kiểm tra lại!!!",
                    false, "Xác nhận", AlertHelper.ALERT_NO_ACTION);
        }
    }

    @Override
    public void onConnectServerError(String s) {
        ((UserActionActivity)getActivity()).dialogHelper.dismiss();
        ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
        ((UserActionActivity)getActivity()).alertHelper.alert("Quên mật khẩu",
                "Kết nối server thất bại. Kiểm tra kết nối của bạn!!!",
                false, "Xác nhận", AlertHelper.ALERT_NO_ACTION);
    }

    @Override
    public void onResetPasswordSuccessful() {
        ((UserActionActivity)getActivity()).dialogHelper.dismiss();
        ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
        ((UserActionActivity)getActivity()).alertHelper.alert("Quên mật khẩu",
                "Mật khẩu của bạn đã được reset. Vui lòng truy cập mail của bạn để xem mật khẩu mới.",
                false, "Xác nhận", Constant.HANDLE_SUCCESSFUL);

    }

    @Override
    public void onResetPasswordError() {

        ((UserActionActivity)getActivity()).dialogHelper.dismiss();

        ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
        ((UserActionActivity)getActivity()).alertHelper.alert("Lỗi",
                "Không thể reset mật khẩu",
                false, "Xác nhận", AlertHelper.ALERT_NO_ACTION);
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
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
