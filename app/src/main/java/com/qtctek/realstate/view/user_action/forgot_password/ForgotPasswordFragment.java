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
import com.qtctek.realstate.presenter.user_action.forgot_password.PresenterForgotPassword;
import com.qtctek.realstate.presenter.user_action.general.FormatPattern;
import com.qtctek.realstate.presenter.user_action.general.HashMD5;
import com.qtctek.realstate.presenter.user_action.general.RandomString;
import com.qtctek.realstate.presenter.user_action.general.send_gmail.GMailSender;
import com.qtctek.realstate.presenter.user_action.register.PresenterRegister;
import com.qtctek.realstate.view.user_action.register.ViewHandleRegister;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, ViewHandleForgotPassword ,
        ViewHandleRegister {

    private View mView;

    private EditText mEdtEmail;
    private Button mBtnConfirm;

    private Dialog mLoadingDialog;

    private PresenterRegister mPresenterRegister;
    private PresenterForgotPassword mPresenterForgotPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        initViews();
        createLoadingDialog();
        this.mPresenterRegister = new PresenterRegister(this);
        this.mPresenterForgotPassword = new PresenterForgotPassword(this);

        return this.mView;
    }

    private void initViews(){
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        this.mBtnConfirm = mView.findViewById(R.id.btn_confirm);

        this.mBtnConfirm.setOnClickListener(this);
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    private void handleForgotPassword(){
        if(this.mEdtEmail.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Vui lòng nhập email!!!", Toast.LENGTH_SHORT).show();
            this.mEdtEmail.requestFocus();
        }
        else if(!FormatPattern.checkEmail(this.mEdtEmail.getText().toString().trim())){
            Toast.makeText(getContext(), "Vui lòng nhập email chính xác!!!", Toast.LENGTH_SHORT).show();
            this.mEdtEmail.requestFocus();
        }
        else{
            mLoadingDialog.show();
            this.mPresenterRegister.handleCheckEmail(this.mEdtEmail.getText().toString().trim(), "");
        }
    }

    private boolean sendConfirmCodeToGMail(String newPasswork){
        GMailSender gMailSender = new GMailSender();
        try {
            gMailSender.sendMail("Reset mật khẩu tài khoản Real Estate", "Mật khẩu tài" +
                    "khoản Real Estate mới là: " + newPasswork, this.mEdtEmail.getText().toString().trim());
        } catch (Exception e) {
            return false;
        }
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
        mLoadingDialog.dismiss();
        if(message.equals("email_existed")){
            String mNewPassword = RandomString.getSaltString();
            if(sendConfirmCodeToGMail(mNewPassword)){
                mPresenterForgotPassword.handleResetPassword(this.mEdtEmail.getText().toString().trim(),
                        HashMD5.md5(mNewPassword));
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Quên mật khẩu")
                        .setCancelable(false)
                        .setMessage("Mật khẩu của bạn đã được reset. Vui lòng truy cập mail của bạn để xem mật khẩu mới.")
                        .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Quên mật khẩu")
                    .setCancelable(false)
                    .setMessage("Tài khoản không tồn tại. Vui lòng kiểm tra lại!!!")
                    .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
    }

    @Override
    public void onConnectServerError(String s) {
        this.mLoadingDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Quên mật khẩu")
                .setCancelable(false)
                .setMessage("Kết nối với server thất bại. Vui lòng kiểm tra kết nối của bạn!!!")
                .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    @Override
    public void onResetPasswordSuccessful() {

        mLoadingDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Quên mật khẩu")
                .setCancelable(false)
                .setMessage("Mật khẩu của bạn đã được reset. Vui lòng truy cập mail của bạn để xem mật khẩu mới.")
                .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                        viewPager.setCurrentItem(0);
                    }
                });
        builder.show();
    }

    @Override
    public void onResetPasswordError() {

        mLoadingDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Quên mật khẩu")
                .setCancelable(false)
                .setMessage("Có lỗi xảy ra trong quá trình reset mật khẩu. Vui lòng thử lại sau!!!")
                .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                handleForgotPassword();
                break;
        }
    }
}
