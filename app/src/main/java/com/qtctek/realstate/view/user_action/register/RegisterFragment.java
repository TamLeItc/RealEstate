package com.qtctek.realstate.view.user_action.register;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
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

import java.util.Calendar;

public class RegisterFragment extends Fragment implements View.OnClickListener, ViewHandleRegister,
        AlertHelper.AlertHelperCallback, View.OnKeyListener, CompoundButton.OnCheckedChangeListener {

    private View mView;

    private EditText mEdtName;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private EditText mEdtPhoneNumber;
    private Button mBtnConfirm;
    private RadioButton mRdoMale;
    private RadioButton mRdoFemale;
    private RadioButton mRdoOther;
    private TextView mTxvBirthDay;
    private EditText mEdtAddress;
    private EditText mEdtUsername;
    private ImageView mImvCalendar;
    private DatePicker mDpkBirthDay;
    private LinearLayout mLLOlPassword;
    private EditText mEdtOldPassword;
    private Switch mSwtPassword;
    private Switch mSwtConfirmPassword;

    private Dialog mDialog;
    private Dialog mLoadingDialog;
    private EditText mEdtConfirmCode;

    private String mConfirmCode = "";

    private PresenterRegister mPresenterRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_register, container, false);

        handleStart();
        initViews();
        createLoadingDialog();

        return this.mView;
    }

    private void initViews(){
        this.mEdtName = mView.findViewById(R.id.edt_full_name);
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        this.mEdtPassword = mView.findViewById(R.id.edt_password);
        this.mEdtConfirmPassword = mView.findViewById(R.id.edt_confirm_password);
        this.mEdtPhoneNumber = mView.findViewById(R.id.edt_phone_number);
        this.mBtnConfirm = mView.findViewById(R.id.btn_confirm);
        this.mRdoFemale = mView.findViewById(R.id.rdo_female);
        this.mRdoMale = mView.findViewById(R.id.rdo_male);
        this.mRdoOther = mView.findViewById(R.id.rdo_other);
        this.mTxvBirthDay = mView.findViewById(R.id.txv_birthday);
        this.mEdtAddress = mView.findViewById(R.id.edt_address);
        this.mImvCalendar = mView.findViewById(R.id.imv_calendar);
        this.mEdtUsername = mView.findViewById(R.id.edt_username);
        this.mLLOlPassword = mView.findViewById(R.id.ll_now_password);
        this.mEdtOldPassword = mView.findViewById(R.id.edt_now_password);
        this.mSwtPassword = mView.findViewById(R.id.swt_password);
        this.mSwtConfirmPassword = mView.findViewById(R.id.swt_confirm_password);

        this.mLLOlPassword.setVisibility(View.GONE);
        this.mEdtOldPassword.setVisibility(View.GONE);

        this.mBtnConfirm.setOnClickListener(this);
        this.mImvCalendar.setOnClickListener(this);

        this.mEdtUsername.setOnKeyListener(this);
        this.mEdtEmail.setOnKeyListener(this);
        this.mEdtPassword.setOnKeyListener(this);
        this.mEdtConfirmPassword.setOnKeyListener(this);
        this.mEdtPhoneNumber.setOnKeyListener(this);

        this.mSwtPassword.setOnCheckedChangeListener(this);
        this.mSwtConfirmPassword.setOnCheckedChangeListener(this);
    }


    private void handleStart(){
        this.mPresenterRegister = new PresenterRegister(this);
    }

    private void handleRegister(){
        String username = this.mEdtUsername.getText().toString().trim();
        String email = this.mEdtEmail.getText().toString().trim();
        String password = this.mEdtPassword.getText().toString().trim();
        String confirmPassword = this.mEdtConfirmPassword.getText().toString().trim();
        String phoneNumber = this.mEdtPhoneNumber.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.please_enter_email), ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(!FormatPattern.checkEmail(email)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.email_incorrect), ToastHelper.LENGTH_SHORT);
            this.mEdtEmail.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(TextUtils.isEmpty(username)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.please_enter_username), ToastHelper.LENGTH_SHORT);
            this.mEdtUsername.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(TextUtils.isEmpty(password)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.please_enter_password), ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(password.length() < 6){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.minimum_length_password), ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(password.length() > 20){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.maximum_length_password), ToastHelper.LENGTH_SHORT);
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.please_enter_confirm_password), ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(!password.equals(confirmPassword)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.password_confirm_password_incorrect), ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.please_enter_number_phone), ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(!FormatPattern.checkNumberPhone(phoneNumber)){
            ((UserActionActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.number_phone_incorrect), ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else{
            mLoadingDialog.show();
            this.mPresenterRegister.handleCheckEmail(email, username);
        }
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
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


        mLoadingDialog.show();

        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_confirm_email);

        this.mConfirmCode = RandomString.getSaltString();


        if(!sendConfirmCodeToGMail()){
            ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
            ((UserActionActivity)getActivity()).alertHelper.alert(getResources().getString(R.string.error),
                    getResources().getString(R.string.sign_up_failed), false,
                    getResources().getString(R.string.ok), AlertHelper.ALERT_NO_ACTION);
        }
        else{
        }

        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        Button btnConfirm = mDialog.findViewById(R.id.btn_confirm);
        this.mEdtConfirmCode = mDialog.findViewById(R.id.edt_confirm_code);

        mDialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.dismiss();
                mDialog.dismiss();
            }
        });

        ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
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

                    mLoadingDialog.show();
                    User user = new User(name, sex, birthday, phoneNumber, email, address, username,
                            HashMD5.md5(password));

                    mPresenterRegister.handleRegister(user);
                }
                else{
                    mLoadingDialog.dismiss();
                    ((UserActionActivity)getActivity()).alertHelper.alert(getResources().getString(R.string.error),
                            getResources().getString(R.string.confirm_code_incorrect), false,
                            getResources().getString(R.string.ok), AlertHelper.ALERT_NO_ACTION);
                }
            }
        });


    }

    @Override
    public void onRegisterSuccessful() {

        mLoadingDialog.dismiss();
        mDialog.dismiss();

        ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
        ((UserActionActivity)getActivity()).alertHelper.alert(getResources().getString(R.string.sign_up_account),
                getResources().getString(R.string.sign_up_successful), false,
                getResources().getString(R.string.ok), Constant.HANDLE_SUCCESSFUL);
    }

    @Override
    public void onRegisterError(String error) {

        mLoadingDialog.dismiss();

        ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
        ((UserActionActivity)getActivity()).alertHelper.alert(getResources().getString(R.string.error),
                getResources().getString(R.string.sign_up_failed), false,
                getResources().getString(R.string.ok),
                Constant.HANDLE_SUCCESSFUL);

    }

    @Override
    public void onCheckExistEmail(String message) {
        mLoadingDialog.dismiss();
        if(message.equals(Constant.EMAIL_EXISTED) || message.equals(Constant.USERNAME_EXISTED)){
            String stMessage;
            if(message.equals(Constant.EMAIL_EXISTED)){
                stMessage = getResources().getString(R.string.email_existed);
            }
            else{
                stMessage = getResources().getString(R.string.username_existed);
            }

            ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
            ((UserActionActivity)getActivity()).alertHelper.alert(getResources().getString(R.string.error), stMessage, false, "Xác nhận",
                    Constant.EXISTED);

        }
        else{
            this.mLoadingDialog.show();
            handleConfirmEmail();
        }
    }

    @Override
    public void onConnectServerError(String s) {
        this.mLoadingDialog.dismiss();

        ((UserActionActivity)getActivity()).alertHelper.setCallback(this);
        ((UserActionActivity)getActivity()).alertHelper.alert(getResources().getString(R.string.error_connect),
                getResources().getString(R.string.error_connect_notification), false, "Xác nhận",
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
        }
    }

    private void createDialogCalendar(){
        Calendar calendar = Calendar.getInstance();
        // Lấy ra năm - tháng - ngày hiện tại
        int year = calendar.get(calendar.YEAR);
        final int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        final Dialog dialog = new Dialog(getContext());
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
            this.mDpkBirthDay.updateDate(Integer.parseInt(birthDate[2]), Integer.parseInt(birthDate[1]), Integer.parseInt(birthDate[0]));
        }
        catch (Exception e){}

    }

    @Override
    public void onDestroyView() {

        Runtime.getRuntime().gc();

        super.onDestroyView();
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.HANDLE_SUCCESSFUL){
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
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
                    this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
                }
                else{
                    this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_email_address:
                if(!FormatPattern.checkEmail(this.mEdtEmail.getText().toString().trim())){
                    this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
                }
                else{
                    this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_password:
                if(this.mEdtPassword.getText().toString().trim().length() < 6 || this.mEdtPassword.getText().toString().trim().length() > 20){
                    this.mEdtPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
                }
                else{
                    this.mEdtPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_confirm_password:
                if(!this.mEdtPassword.getText().toString().trim().equals(this.mEdtConfirmPassword.getText().toString().trim())){
                    this.mEdtConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
                }
                else{
                    this.mEdtConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_phone_number:
                if(!FormatPattern.checkNumberPhone(this.mEdtPhoneNumber.getText().toString().trim())){
                    this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
                }
                else{
                    this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
        }

        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.swt_password:
                if(!isChecked){
                    this.mEdtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    this.mEdtPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                }
                break;
            case R.id.swt_confirm_password:
                if(!isChecked){
                    this.mEdtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    this.mEdtConfirmPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                }
                break;
        }
    }
}
