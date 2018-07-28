package com.qtctek.realstate.view.user_action.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.common.general.HashMD5;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_action.login.PresenterLogin;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

public class LoginFragment extends Fragment implements ViewHandleLogin, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View mView;

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnConfirm;
    private TextView mTxvForgotPassword;
    private TextView mTxvRegister;
    private CheckBox mChkSaveLogin;
    private Switch mSwtShowPassword;

    private PresenterLogin mPresenterUserManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        MainActivity.ON_USER_LOGIN.onUserLoginSuccessful();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        unitViews();
        handleStart();

    }

    private void unitViews(){
        this.mEdtPassword = this.mView.findViewById(R.id.edt_password);
        this.mEdtEmail = this.mView.findViewById(R.id.edt_email_address);
        this.mBtnConfirm = this.mView.findViewById(R.id.btn_login);
        this.mTxvForgotPassword = this.mView.findViewById(R.id.txv_forgot_password);
        this.mTxvRegister = this.mView.findViewById(R.id.txv_register_user);
        this.mChkSaveLogin = mView.findViewById(R.id.chk_save_login);
        this.mSwtShowPassword = mView.findViewById(R.id.swt_show_password);


        this.mBtnConfirm.setOnClickListener(this);
        this.mTxvForgotPassword.setOnClickListener(this);
        this.mTxvRegister.setOnClickListener(this);
        this.mChkSaveLogin.setOnCheckedChangeListener(this);
        this.mSwtShowPassword.setOnCheckedChangeListener(this);

    }

    private void handleStart(){

        this.mPresenterUserManager = new PresenterLogin(this);
        mPresenterUserManager.handleGetDataSaveLogin(getContext());

    }

    private void handleLogin(){

        String email = this.mEdtEmail.getText().toString().trim();
        String password = this.mEdtPassword.getText().toString().trim();

        if(email.equals("")){
            ((UserActionActivity)getActivity()).toastHelper.toast("Vui lòng nhập email/tên đăng nhập", ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
        } else if(password.equals("")){
            ((UserActionActivity)getActivity()).toastHelper.toast("Vui lòng nhập mật khẩu", ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
        } else {
            ((UserActionActivity)getActivity()).dialogHelper.show();
            String passwordMD5 = HashMD5.md5(password);
            this.mPresenterUserManager.handleCheckUserLogin(email, passwordMD5);
        }
    }

    @Override
    public void onHandleCheckUserNotExists() {
        ((UserActionActivity)getActivity()).dialogHelper.dismiss();
        ((UserActionActivity)getActivity()).toastHelper.toast("Email/tên đăng nhập hoặc mật khẩu không chính xác!!!", ToastHelper.LENGTH_SHORT);
        this.mEdtPassword.requestFocus();
        this.mEdtPassword.setText("");

    }

    @Override
    public void onHandleCheckUserLoginSuccessful(User user) {
        ((UserActionActivity)getActivity()).dialogHelper.dismiss();
        if(user.getStatus().equals("no")){
            ((NewPostActivity)getActivity()).alertHelper.alert("Lỗi", "Tài khoản của bạn bị tạm" +
                    " khóa. Vui lòng liên hệ với admin để được hỗ trợ", false, "Xác nhập",
                    AlertHelper.ALERT_NO_ACTION);
        }
        else{
            MainActivity.USER = user;

            handleSaveLogin();

            MainActivity.ON_USER_LOGIN.onUserLoginSuccessful();
            Intent intent = new Intent(getActivity(), UserControlActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void handleSaveLogin() {
        if(!mChkSaveLogin.isChecked()){
            mPresenterUserManager.handleUpdateDataSaveLogin("", "", getContext());
        }
        else{
            String username = "";
            if(MainActivity.USER.getUsername().equals(this.mEdtEmail.getText())){
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

        ((UserActionActivity)getActivity()).dialogHelper.dismiss();

        ((UserActionActivity)getActivity()).alertHelper.alert("Lỗi", "Đăng nhập không thành công " +
                        "vui lòng thử lại sau", false, "Xác nhập",
                AlertHelper.ALERT_NO_ACTION);
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
        ((UserActionActivity)getActivity()).toastHelper.toast("Lỗi xử lí", ToastHelper.LENGTH_SHORT);
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
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(1);
                break;
            case R.id.txv_forgot_password:
                ViewPager viewPager1 = getActivity().findViewById(R.id.view_pager);
                viewPager1.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onDestroyView() {

        ((UserActionActivity)getActivity()).dialogHelper.dismiss();
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
            case R.id.swt_show_password:
                if(!isChecked){
                    this.mEdtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    this.mEdtPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                }
                break;
        }

    }
}
