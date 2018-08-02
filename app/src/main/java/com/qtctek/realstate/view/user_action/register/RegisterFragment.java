package com.qtctek.realstate.view.user_action.register;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.common.general.FormatPattern;
import com.qtctek.realstate.common.general.HashMD5;
import com.qtctek.realstate.common.general.RandomString;
import com.qtctek.realstate.common.general.send_gmail.GMailSender;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_action.register.PresenterRegister;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class RegisterFragment extends Fragment implements View.OnClickListener, ViewHandleRegister,
        AlertHelper.AlertHelperCallback, View.OnKeyListener {

    private UserActionActivity mActivity;

    private View mView;

    private EditText mEdtName;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private EditText mEdtPhoneNumber;
    private RadioButton mRdoMale;
    private RadioButton mRdoFemale;
    private TextView mTxvBirthDay;
    private EditText mEdtAddress;
    private EditText mEdtUsername;
    private DatePicker mDpkBirthDay;
    private RelativeLayout mRlNowPassword;
    private ImageView mImvShowPassword;
    private ImageView mImvShowConfirmPassword;
    private RelativeLayout mRlPassword;
    private RelativeLayout mRlConfirmPassword;

    private Dialog mDialog;
    private EditText mEdtConfirmCode;

    private String mConfirmCode = "";
    private boolean mIsShowPassword = false;
    private boolean mIsShowConfirmPassword = true;

    private PresenterRegister mPresenterRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_register, container, false);

        mActivity = (UserActionActivity)getActivity();

        initViews();
        handleStart();

        return this.mView;
    }

    private void initViews(){
        this.mEdtName = mView.findViewById(R.id.edt_full_name);
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        this.mEdtPassword = mView.findViewById(R.id.edt_password);
        this.mEdtConfirmPassword = mView.findViewById(R.id.edt_confirm_password);
        this.mEdtPhoneNumber = mView.findViewById(R.id.edt_phone_number);
        Button mBtnConfirm = mView.findViewById(R.id.btn_confirm);
        this.mRdoFemale = mView.findViewById(R.id.rdo_female);
        this.mRdoMale = mView.findViewById(R.id.rdo_male);
        RadioButton mRdoOther = mView.findViewById(R.id.rdo_other);
        this.mTxvBirthDay = mView.findViewById(R.id.txv_birthday);
        this.mEdtAddress = mView.findViewById(R.id.edt_address);
        ImageView mImvCalendar = mView.findViewById(R.id.imv_calendar);
        this.mEdtUsername = mView.findViewById(R.id.edt_username);
        LinearLayout mLLNowPassword = mView.findViewById(R.id.ll_now_password);
        this.mRlNowPassword = mView.findViewById(R.id.rl_now_password);
        this.mImvShowPassword = mView.findViewById(R.id.imv_show_password);
        this.mImvShowConfirmPassword = mView.findViewById(R.id.imv_show_confirm_password);
        this.mRlPassword = mView.findViewById(R.id.rl_password);
        this.mRlConfirmPassword = mView.findViewById(R.id.rl_confirm_password);

        mLLNowPassword.setVisibility(View.GONE);
        this.mRlNowPassword.setVisibility(View.GONE);

        mBtnConfirm.setOnClickListener(this);
        mImvCalendar.setOnClickListener(this);

        this.mEdtUsername.setOnKeyListener(this);
        this.mEdtEmail.setOnKeyListener(this);
        this.mEdtPassword.setOnKeyListener(this);
        this.mEdtConfirmPassword.setOnKeyListener(this);
        this.mEdtPhoneNumber.setOnKeyListener(this);
        this.mImvShowPassword.setOnClickListener(this);
        this.mImvShowConfirmPassword.setOnClickListener(this);
    }


    private void handleStart(){
        this.mPresenterRegister = new PresenterRegister(this);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        this.mTxvBirthDay.setText(day + "/" + (month +1) + "/" + year);
    }

    private void handleRegister(){
        String username = this.mEdtUsername.getText().toString().trim();
        String email = this.mEdtEmail.getText().toString().trim();
        String password = this.mEdtPassword.getText().toString().trim();
        String confirmPassword = this.mEdtConfirmPassword.getText().toString().trim();
        String phoneNumber = this.mEdtPhoneNumber.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mActivity.getToastHelper().toast(R.string.please_enter_email, ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(!FormatPattern.checkEmail(email)){
            mActivity.getToastHelper().toast(R.string.email_incorrect, ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(TextUtils.isEmpty(username)){
            mActivity.getToastHelper().toast(R.string.please_enter_username, ToastHelper.LENGTH_SHORT);
            this.mEdtUsername.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(TextUtils.isEmpty(password)){
            mActivity.getToastHelper().toast(R.string.please_enter_password, ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mRlNowPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(password.length() < 6){
            mActivity.getToastHelper().toast(R.string.minimum_length_password, ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mRlPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(password.length() > 20){
            mActivity.getToastHelper().toast(R.string.maximum_length_password, ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mRlPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            mActivity.getToastHelper().toast(R.string.please_enter_confirm_password, ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mRlConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(!password.equals(confirmPassword)){
            mActivity.getToastHelper().toast(R.string.password_confirm_password_incorrect, ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mRlConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            mActivity.getToastHelper().toast(R.string.please_enter_number_phone, ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(!FormatPattern.checkNumberPhone(phoneNumber)){
            mActivity.getToastHelper().toast(R.string.number_phone_incorrect, ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else{
            mActivity.getDialogHelper().show();
            this.mPresenterRegister.handleCheckEmail(email, username);
        }
    }

    private boolean sendConfirmCodeToGMail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender gMailSender = new GMailSender();
                    gMailSender.sendMail("Xác nhận đăng kí tài khoản Real Estate", "Mã xác nhận " +
                            " tài khoản Real Estate là: " + mConfirmCode, mEdtEmail.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    private void handleConfirmEmail(){

        mActivity.getDialogHelper().show();

        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_confirm_email);

        this.mConfirmCode = RandomString.getSaltString();


        if(!sendConfirmCodeToGMail()){
            mActivity.getAlertHelper().setCallback(this);
            mActivity.getAlertHelper().alert(R.string.error, R.string.sign_up_failed, false,
                    R.string.ok, AlertHelper.ALERT_NO_ACTION);
            return;
        }

        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        Button btnConfirm = mDialog.findViewById(R.id.btn_confirm);
        this.mEdtConfirmCode = mDialog.findViewById(R.id.edt_confirm_code);

        mDialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getDialogHelper().dismiss();
                mDialog.dismiss();
            }
        });

        mActivity.getAlertHelper().setCallback(this);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mConfirmCode.equals(mEdtConfirmCode.getText().toString())){

                    String name = mEdtName.getText().toString().trim();

                    String sex;
                    if(mRdoFemale.isChecked()){
                        sex = getResources().getString(R.string.female);
                    }
                    else if(mRdoMale.isChecked()){
                        sex = getResources().getString(R.string.male);
                    }
                    else{
                        sex = getResources().getString(R.string.other_sex);
                    }

                    String birthday = mTxvBirthDay.getText().toString().trim();
                    String username = mEdtUsername.getText().toString().trim();
                    String email = mEdtEmail.getText().toString().trim();
                    String password = mEdtPassword.getText().toString().trim();
                    String phoneNumber = mEdtPhoneNumber.getText().toString().trim();
                    String address = mEdtAddress.getText().toString().trim();

                    mActivity.getDialogHelper().show();
                    User user = new User(name, sex, birthday, phoneNumber, email, address, username,
                            HashMD5.md5(password));

                    mPresenterRegister.handleRegister(user);
                }
                else{
                    mActivity.getDialogHelper().dismiss();
                    mActivity.getAlertHelper().alert(R.string.error, R.string.confirm_code_incorrect, false,
                            R.string.ok, AlertHelper.ALERT_NO_ACTION);
                }
            }
        });


    }

    @Override
    public void onRegisterSuccessful() {

        mActivity.getDialogHelper().dismiss();
        mDialog.dismiss();

        mActivity.getAlertHelper().setCallback(this);
        mActivity.getAlertHelper().alert(R.string.sign_up_account, R.string.sign_up_successful,
                false, R.string.ok, Constant.HANDLE_SUCCESSFUL);
    }

    @Override
    public void onRegisterError(String error) {
        mActivity.getDialogHelper().dismiss();

        mActivity.getAlertHelper().setCallback(this);
        mActivity.getAlertHelper().alert(R.string.error, R.string.sign_up_failed,
                false, R.string.ok, Constant.HANDLE_SUCCESSFUL);

    }

    @Override
    public void onCheckExistEmail(String message) {
        mActivity.getDialogHelper().dismiss();
        if(message.equals(Constant.EMAIL_EXISTED) || message.equals(Constant.USERNAME_EXISTED)){
            String stMessage;
            if(message.equals(Constant.EMAIL_EXISTED)){
                stMessage = getResources().getString(R.string.email_existed);
            }
            else{
                stMessage = getResources().getString(R.string.username_existed);
            }

            mActivity.getAlertHelper().setCallback(this);
            mActivity.getAlertHelper().alert(getResources().getString(R.string.error), stMessage, false, "Xác nhận",
                    Constant.EXISTED);

        }
        else{
            this.mActivity.getDialogHelper().show();
            handleConfirmEmail();
        }
    }

    @Override
    public void onConnectServerError(String s) {
        this.mActivity.getDialogHelper().dismiss();

        mActivity.getAlertHelper().setCallback(this);
        mActivity.getAlertHelper().alert(R.string.error_connect,
                R.string.error_connect_notification, false, R.string.ok,
                Constant.EXISTED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                handleRegister();
                break;
            case R.id.imv_calendar:
                createDialogCalendar();
                break;
            case R.id.imv_show_password:
                mActivity.handleShowPassword(mImvShowPassword, mEdtPassword, mIsShowPassword);

                mIsShowPassword = !mIsShowPassword;
                break;
            case R.id.imv_show_confirm_password:
                mActivity.handleShowPassword(mImvShowConfirmPassword, mEdtConfirmPassword, mIsShowConfirmPassword);

                mIsShowConfirmPassword = !mIsShowConfirmPassword;
                break;
        }
    }

    private void createDialogCalendar(){
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calendar);

        mDpkBirthDay = dialog.findViewById(R.id.dpk_birthday);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        updateDatePicker();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthDate = mDpkBirthDay.getDayOfMonth() + "/" + (mDpkBirthDay.getMonth() + 1) + "/" + mDpkBirthDay.getYear();
                mTxvBirthDay.setText(birthDate);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateDatePicker(){
        String[] birthDate = this.mTxvBirthDay.getText().toString().split("/");
        if(birthDate.length != 3){
            return;
        }

        try{
            //year-month-day
            this.mDpkBirthDay.updateDate(Integer.parseInt(birthDate[2]), Integer.parseInt(birthDate[1]) - 1, Integer.parseInt(birthDate[0]));
        }
        catch (Exception ignored){}

    }

    @Override
    public void onDestroyView() {

        Runtime.getRuntime().gc();

        super.onDestroyView();
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.HANDLE_SUCCESSFUL){
            ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
            viewPager.setCurrentItem(0);
        }
        else if(option == Constant.EXISTED){
            mEdtPassword.setText("");
            mEdtConfirmPassword.setText("");
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (v.getId()){
            case R.id.edt_username:
                if(this.mEdtUsername.getText().toString().trim().equals("")){
                    this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_email_address:
                if(!FormatPattern.checkEmail(this.mEdtEmail.getText().toString().trim())){
                    this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_password:
                if(this.mEdtPassword.getText().toString().trim().length() < 6 || this.mEdtPassword.getText().toString().trim().length() > 20){
                    this.mRlPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mRlPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_confirm_password:
                if(!this.mEdtPassword.getText().toString().trim().equals(this.mEdtConfirmPassword.getText().toString().trim())){
                    this.mRlConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mRlConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_phone_number:
                if(!FormatPattern.checkNumberPhone(this.mEdtPhoneNumber.getText().toString().trim())){
                    this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
        }

        return false;
    }
}
