package com.qtctek.aladin.view.user_action.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.common.HashMD5;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.user_action.login.PresenterLogin;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.user_action.activity.UserActionActivity;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;


public class LoginFragment extends Fragment implements ViewHandleLogin, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private UserActionActivity mActivity;

    private View mView;

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private CheckBox mChkSaveLogin;
    private ImageView mImvShowPassword;
    private TextView mTxvForgotPassword;
    private TextView mTxvRegister;

    private PresenterLogin mPresenterUserManager;

    private boolean mIsShowPassword = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        this.mActivity = (UserActionActivity)getActivity();

        MainActivity.ON_USER_LOGIN.onUserLoginSuccessful();
        unitViews();
        handleStart();

        return mView;
    }

    private void unitViews(){
        this.mEdtPassword = this.mView.findViewById(R.id.edt_password);
        this.mEdtEmail = this.mView.findViewById(R.id.edt_email_address);
        this.mChkSaveLogin = mView.findViewById(R.id.chk_save_login);

        Button mBtnConfirm = this.mView.findViewById(R.id.btn_login);
        mTxvForgotPassword = this.mView.findViewById(R.id.txv_forgot_password);
        mTxvRegister = this.mView.findViewById(R.id.txv_register_user);
        mImvShowPassword = this.mView.findViewById(R.id.imv_show_password);


        mBtnConfirm.setOnClickListener(this);
        mTxvForgotPassword.setOnClickListener(this);
        mTxvRegister.setOnClickListener(this);
        this.mChkSaveLogin.setOnCheckedChangeListener(this);
        mImvShowPassword.setOnClickListener(this);

    }

    private void handleStart(){
        this.mPresenterUserManager = new PresenterLogin(this);
        mPresenterUserManager.handleGetDataSaveLogin(getContext());
    }

    private void handleLogin(){

        String email = this.mEdtEmail.getText().toString().trim();
        String password = this.mEdtPassword.getText().toString().trim();

        if(email.equals("")){
            mActivity.getToastHelper().toast(R.string.please_enter_username_or_email, ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.setText("");
            this.mEdtEmail.requestFocus();
        } else if(password.equals("")){
            mActivity.getToastHelper().toast(R.string.please_enter_password, ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
        } else {
            mActivity.getDialogHelper().show();
            String passwordMD5 = HashMD5.md5(password);
            this.mPresenterUserManager.handleCheckUserLogin(email, passwordMD5);
        }
    }

    @Override
    public void onHandleCheckUserNotExists() {
        mActivity.getDialogHelper().dismiss();
        mActivity.getToastHelper().toast(R.string.information_login_incorrect, ToastHelper.LENGTH_SHORT);
        this.mEdtPassword.requestFocus();
        this.mEdtPassword.setText("");
    }

    @Override
    public void onHandleCheckUserLoginSuccessful(User user) {
        mActivity.getDialogHelper().dismiss();
        if(user.getStatus().equals(Constant.NO)){
            mActivity.getAlertHelper().alert(R.string.error, R.string.account_disable,
                    false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
        }
        else{
            MainActivity.USER = user;

            handleSaveLogin();

            MainActivity.ON_USER_LOGIN.onUserLoginSuccessful();
            Intent intent = new Intent(mActivity, UserControlActivity.class);
            startActivity(intent);
            mActivity.finish();
        }
    }

    private void handleSaveLogin() {
        if(!mChkSaveLogin.isChecked()){
            mPresenterUserManager.handleUpdateDataSaveLogin("", "", getContext());
        }
        else{
            String username;
            if(MainActivity.USER.getUsername().equals(this.mEdtEmail.getText().toString())){
                username = MainActivity.USER.getUsername();
            }
            else{
                username = MainActivity.USER.getEmail();
            }
            mPresenterUserManager.handleUpdateDataSaveLogin(username, this.mEdtPassword.getText().toString(), getContext());
        }
    }

    @Override
    public void onHandleCheckUserLoginError(String error) {
        mActivity.getDialogHelper().dismiss();

        mActivity.getAlertHelper().alert(R.string.error,
                R.string.login_failed, false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
    }

    @Override
    public void onGetDataSaveLoginSuccessful(String userName, String password) {
        this.mEdtEmail.setText(userName);
        this.mEdtPassword.setText(password);

        if(!userName.equals("")){
            this.mChkSaveLogin.setChecked(true);
        }
        else{
            this.mEdtEmail.setText("itcdeveloper15@gmail.com");
            this.mEdtPassword.setText("111111");
        }
    }

    @Override
    public void onGetDataSaveLoginError(String error) {
        mActivity.getToastHelper().toast(R.string.error_handle, ToastHelper.LENGTH_SHORT);
        this.mEdtEmail.setText("");
        this.mEdtPassword.setText("");
    }

    @Override
    public void onUpdateDataSaveLoginSuccessful() {

    }

    @Override
    public void onUpdateDataSaveLoginError(String error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                handleLogin();
                break;
            case R.id.txv_register_user:
                handleChangeColorClickTextView(mTxvRegister);
                ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
                viewPager.setCurrentItem(1);
                break;
            case R.id.txv_forgot_password:
                handleChangeColorClickTextView(mTxvForgotPassword);
                ViewPager viewPager1 = mActivity.findViewById(R.id.view_pager);
                viewPager1.setCurrentItem(2);
                break;
            case R.id.imv_show_password:
                handleShowPassword();
        }
    }

    public void handleChangeColorClickTextView(final TextView textView){
        textView.setTextColor(getResources().getColor(R.color.colorBlack));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setTextColor(getResources().getColor(R.color.colorGrayDark));
            }
        }, 50);
    }

    private void handleShowPassword(){
        mActivity.handleShowPassword(mImvShowPassword, mEdtPassword, mIsShowPassword);

        mIsShowPassword = !mIsShowPassword;
    }

    @Override
    public void onDestroyView() {

        mActivity.getDialogHelper().dismiss();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.chk_save_login:
                if(!isChecked){
                    mPresenterUserManager.handleUpdateDataSaveLogin("", "", getContext());
                }
                break;
        }

    }
}
