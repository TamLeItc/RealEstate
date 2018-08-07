package com.qtctek.aladin.view.user_action.update_user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.common.FormatPattern;
import com.qtctek.aladin.common.HashMD5;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.user_action.update_user.PresenterUpdateUser;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.user_action.activity.UserActionActivity;

import static com.qtctek.aladin.view.post_news.activity.MainActivity.USER;

public class UpdateUserFragment extends Fragment implements View.OnClickListener, ViewHandleUpdateUser, View.OnFocusChangeListener, View.OnKeyListener {

    private UserActionActivity mActivity;

    private View mView;

    private EditText mEdtName;
    private EditText mEdtEmail;
    private EditText mEdtNewPassword;
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
    private EditText mEdtNowPassword;
    private TextView mTxvNewPassword;
    private ImageView mImvShowNowPassword;
    private ImageView mImvShowNewPassword;
    private ImageView mImvShowConfirmNewPassword;
    private RelativeLayout mRlNowPassword;
    private RelativeLayout mRlNewPassowrd;
    private RelativeLayout mRlConfirmNewPassword;

    private Dialog mLoadingDialog;

    private PresenterUpdateUser mPresenterUpdate;

    private boolean mIsShowNowPassword = false;
    private boolean mIsShowNewPassword = false;
    private boolean mIsShowConfirmNewPassword = false;
    private String mFullName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_register, container, false);

        try{

            this.mActivity = (UserActionActivity)getActivity();

            initViews();
            createLoadingDialog();
            handleStart();
        }
        catch (java.lang.NullPointerException ignored){}
        return this.mView;
    }

    private void initViews(){
        this.mEdtName = mView.findViewById(R.id.edt_full_name);
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        this.mEdtNowPassword = mView.findViewById(R.id.edt_now_password);
        this.mEdtNewPassword = mView.findViewById(R.id.edt_password);
        this.mTxvNewPassword = mView.findViewById(R.id.txv_password);
        this.mEdtConfirmPassword = mView.findViewById(R.id.edt_confirm_password);
        this.mEdtPhoneNumber = mView.findViewById(R.id.edt_phone_number);
        this.mBtnConfirm = mView.findViewById(R.id.btn_confirm);
        this.mRdoFemale = mView.findViewById(R.id.rdo_female);
        this.mRdoMale = mView.findViewById(R.id.rdo_male);
        this.mTxvBirthDay = mView.findViewById(R.id.txv_birthday);
        this.mEdtAddress = mView.findViewById(R.id.edt_address);
        this.mImvCalendar = mView.findViewById(R.id.imv_calendar);
        this.mEdtUsername = mView.findViewById(R.id.edt_username);
        this.mRdoOther = mView.findViewById(R.id.rdo_other);
        this.mImvShowNowPassword = mView.findViewById(R.id.imv_show_now_password);
        this.mImvShowNewPassword = mView.findViewById(R.id.imv_show_password);
        this.mImvShowConfirmNewPassword = mView.findViewById(R.id.imv_show_confirm_password);
        this.mRlNowPassword = mView.findViewById(R.id.rl_now_password);
        this.mRlNewPassowrd = mView.findViewById(R.id.rl_password);
        this.mRlConfirmNewPassword = mView.findViewById(R.id.rl_confirm_password);

        this.mTxvNewPassword.setText(mActivity.getResources().getString(R.string.new_password));

        this.mBtnConfirm.setOnClickListener(this);
        this.mImvCalendar.setOnClickListener(this);

        this.mEdtUsername.setFocusable(false);
        this.mEdtEmail.setFocusable(false);

        this.mBtnConfirm.setOnClickListener(this);
        this.mImvShowNowPassword.setOnClickListener(this);
        this.mImvShowNewPassword.setOnClickListener(this);
        this.mImvShowConfirmNewPassword.setOnClickListener(this);

        this.mEdtNowPassword.setOnKeyListener(this);
        this.mEdtNewPassword.setOnKeyListener(this);
        this.mEdtConfirmPassword.setOnKeyListener(this);
        this.mEdtPhoneNumber.setOnKeyListener(this);
    }


    private void handleStart(){
        this.mPresenterUpdate = new PresenterUpdateUser(this);

        mFullName = USER.getFullName();
        this.mEdtName.setText(USER.getFullName());
        this.mEdtAddress.setText(USER.getAddress());
        this.mEdtUsername.setText(USER.getUsername());
        this.mEdtEmail.setText(USER.getEmail());
        this.mEdtPhoneNumber.setText(USER.getPhone());
        this.mTxvBirthDay.setText(USER.getBirthDay());

        String sex = USER.getSex();

        switch (sex) {
            case "Nam":
                this.mRdoMale.setChecked(true);
                break;
            case "Nữ":
                this.mRdoFemale.setChecked(true);
                break;
            default:
                this.mRdoOther.setChecked(true);
                break;
        }

        this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_gray));
        this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_gray));
        this.mEdtUsername.setTextColor(getResources().getColor(R.color.colorGray));
        this.mEdtEmail.setTextColor(getResources().getColor(R.color.colorGray));

    }

    private void handleUpdateUser(){

        mLoadingDialog.show();

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
        String password = mEdtNewPassword.getText().toString().trim();
        String phoneNumber = mEdtPhoneNumber.getText().toString().trim();
        String address = mEdtAddress.getText().toString().trim();
        String nowPassword = mEdtNowPassword.getText().toString().trim();

        mLoadingDialog.dismiss();


        if(TextUtils.isEmpty(nowPassword)){
            mActivity.getToastHelper().toast(R.string.please_enter_now_password, ToastHelper.LENGTH_SHORT);
            this.mEdtNowPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mRlNowPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(TextUtils.isEmpty(password)){
            mActivity.getToastHelper().toast(R.string.please_enter_new_password, ToastHelper.LENGTH_SHORT);
            this.mEdtNewPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mRlNewPassowrd.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(password.length() < 6){
            mActivity.getToastHelper().toast(R.string.minimum_length_password, ToastHelper.LENGTH_SHORT);
            this.mEdtNewPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mRlNewPassowrd.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(password.length() > 20){
            mActivity.getToastHelper().toast(R.string.maximum_length_password, ToastHelper.LENGTH_SHORT);
            this.mEdtNewPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mRlNewPassowrd.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(mEdtConfirmPassword.getText().toString().equals("")){
            mActivity.getToastHelper().toast(R.string.please_enter_confirm_password, ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mRlConfirmNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(!mEdtConfirmPassword.getText().toString().equals(password)){
            mActivity.getToastHelper().toast(R.string.password_confirm_password_incorrect, ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mRlConfirmNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            mActivity.getToastHelper().toast(R.string.please_enter_number_phone, ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else if(!FormatPattern.checkNumberPhone(phoneNumber)){
            mActivity.getToastHelper().toast(R.string.number_phone_incorrect, ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
        }
        else{
            mLoadingDialog.show();
            User user = new User(name, sex, birthday, phoneNumber, email, address, username,
                    HashMD5.md5(password));
            this.mPresenterUpdate.handleUpdateUser(user, HashMD5.md5(nowPassword));
        }
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(mActivity);
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    @Override
    public void onUpdateUserSuccessful() {

        this.mLoadingDialog.dismiss();

        mFullName = USER.getFullName();
        MainActivity.USER.setFullName(this.mEdtName.getText().toString());
        MainActivity.USER.setBirthDay(this.mTxvBirthDay.getText().toString());
        MainActivity.USER.setPhone(this.mEdtPhoneNumber.getText().toString());
        MainActivity.USER.setAddress(this.mEdtAddress.getText().toString());
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
        MainActivity.USER.setSex(sex);

        mActivity.getAlertHelper().alert(R.string.update_account,
                R.string.update_account_successful, false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
    }

    @Override
    public void onUpdateUserError(String error) {

        if(error.equals(Constant.NOW_PASSWORD_INCORRECT)){
            mActivity.getAlertHelper().alert(R.string.error,
                    R.string.now_password_incorrect, false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
        }
        else{
            mActivity.getAlertHelper().alert(R.string.error,
                    R.string.update_account_failed, false, R.string.ok, AlertHelper.ALERT_NO_ACTION);
        }

        this.mLoadingDialog.dismiss();


    }

    private void createDialogCalendar(){
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calendar);

        mDpkBirthDay = dialog.findViewById(R.id.dpk_birthday);
        Button mBtnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button mBtnCancel = dialog.findViewById(R.id.btn_cancel);

        updateDatePicker();

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthDate = mDpkBirthDay.getDayOfMonth() + "/" + (mDpkBirthDay.getMonth() + 1) + "/" + mDpkBirthDay.getYear();
                mTxvBirthDay.setText(birthDate);
                dialog.dismiss();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
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
    public void onStop() {
        Intent intent = mActivity.getIntent();
        intent.putExtra(Constant.UPDATE_ACCOUNT_INFORMATION, mFullName);
        mActivity.setResult(Constant.UPDATE, intent);
        super.onStop();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            mActivity.getKeyboardHelper().hideKeyboard(v);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                handleUpdateUser();
                break;
            case R.id.imv_calendar:
                createDialogCalendar();
                break;
            case R.id.imv_show_now_password:
                mActivity.handleShowPassword(mImvShowNowPassword, mEdtNowPassword, mIsShowNowPassword);

                mIsShowNowPassword = !mIsShowNowPassword;
                break;
            case R.id.imv_show_password:
                mActivity.handleShowPassword(mImvShowNewPassword, mEdtNewPassword, mIsShowNewPassword);

                mIsShowNewPassword = !mIsShowNewPassword;
                break;
            case R.id.imv_show_confirm_password:
                mActivity.handleShowPassword(mImvShowConfirmNewPassword, mEdtConfirmPassword, mIsShowConfirmNewPassword);

                mIsShowConfirmNewPassword = !mIsShowConfirmNewPassword;
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (v.getId()){
            case R.id.edt_now_password:
                if(this.mEdtNowPassword.getText().toString().trim().length() < 6 || this.mEdtNowPassword.getText().toString().trim().length() > 20){
                    this.mRlNowPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mRlNowPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_password:
                if(this.mEdtNewPassword.getText().toString().trim().length() < 6 || this.mEdtNewPassword.getText().toString().trim().length() > 20){
                    this.mRlNewPassowrd.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mRlNewPassowrd.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_confirm_password:
                if(!this.mEdtNewPassword.getText().toString().trim().equals(this.mEdtConfirmPassword.getText().toString().trim())){
                    this.mRlConfirmNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_gray));
                }
                else{
                    this.mRlConfirmNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
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
