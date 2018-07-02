package com.qtctek.realstate.view.user_action.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.presenter.user_action.general.FormatPattern;
import com.qtctek.realstate.presenter.user_action.general.HashMD5;
import com.qtctek.realstate.presenter.user_action.login.PresenterLogin;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

public class LoginFragment extends Fragment implements ViewHandleLogin, View.OnClickListener {

    private View mView;

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnConfirm;
    private TextView mTxvForgotPassword;
    private TextView mTxvRegister;

    private Dialog mLoadingDialog;

    private PresenterLogin mPresenterUserManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        MainActivity.ON_USER_LOGIN.onUserLoginSuccessful(MainActivity.ROLE_USER);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        unitViews();
        handleStart();
        createLoadingDialog();

    }

    private void unitViews(){
        this.mEdtPassword = this.mView.findViewById(R.id.edt_password);
        this.mEdtEmail = this.mView.findViewById(R.id.edt_email_address);
        this.mBtnConfirm = this.mView.findViewById(R.id.btn_login);
        this.mTxvForgotPassword = this.mView.findViewById(R.id.txv_forgot_password);
        this.mTxvRegister = this.mView.findViewById(R.id.txv_register_user);

        this.mBtnConfirm.setOnClickListener(this);
        this.mTxvForgotPassword.setOnClickListener(this);
        this.mTxvRegister.setOnClickListener(this);

        this.mEdtEmail.setText("itcdeveloper11@gmail.com");
        this.mEdtPassword.setText("123456");
    }

    private void handleStart(){

        this.mPresenterUserManager = new PresenterLogin(this);

    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    private void handleLogin(){

        String email = this.mEdtEmail.getText().toString().trim();
        String password = this.mEdtPassword.getText().toString().trim();

        if(email.equals("")){
            Toast.makeText(getActivity(), "Vui lòng nhập email!!!", Toast.LENGTH_SHORT).show();
            this.mEdtEmail.requestFocus();
        } else if(!FormatPattern.checkEmail(email)){
            Toast.makeText(getActivity(), "Email không hợp lệ!!!", Toast.LENGTH_SHORT).show();;
            this.mEdtEmail.requestFocus();
        } else if(password.equals("")){
            Toast.makeText(getActivity(), "Vui lòng nhập mật khẩu!!!", Toast.LENGTH_SHORT).show();;
            this.mEdtPassword.requestFocus();
        } else {
            this.mLoadingDialog.show();
            String passwordMD5 = HashMD5.md5(password);
            this.mPresenterUserManager.handleCheckUserLogin(email, passwordMD5);
        }
    }

    @Override
    public void onHandleCheckUserLoginSuccessful(String data) {

        this.mLoadingDialog.dismiss();

        if(data.equals("0")){
            Toast.makeText(getActivity(), "Email hoặc mật khẩu không chính xác!!!", Toast.LENGTH_SHORT).show();;
            this.mEdtPassword.requestFocus();
            this.mEdtPassword.setText("");
        }
        else if(data.equals("-1")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage("Tài khoản của bạn bị khóa. Vui lòng liên hệ Real Estate để được giải đáp!!!");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }
        else {

            MainActivity.EMAIL_USER = this.mEdtEmail.getText().toString();
            MainActivity.ROLE_USER = data;
            MainActivity.ON_USER_LOGIN.onUserLoginSuccessful(data);

            Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT);
            Intent intent = new Intent(getActivity(), UserControlActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onHandleCheckUserLoginError(String error) {

        this.mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Đăng nhập không thành công. Vui lòng thử lại sau!!!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                handleLogin();
                break;
            case R.id.txv_register_user:
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(1);
                break;
            case R.id.txv_forgot_password:
                ViewPager viewPager1 = getActivity().findViewById(R.id.view_pager);
                viewPager1.setCurrentItem(2);
                break;
        }
    }
}
