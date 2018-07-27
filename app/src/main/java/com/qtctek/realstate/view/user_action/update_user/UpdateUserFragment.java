package com.qtctek.realstate.view.user_action.update_user;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.common.general.FormatPattern;
import com.qtctek.realstate.common.general.HashMD5;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_action.update_user.PresenterUpdateUser;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;

import java.util.Calendar;
import java.util.Objects;

import static com.qtctek.realstate.view.post_news.activity.MainActivity.USER;

public class UpdateUserFragment extends Fragment implements View.OnClickListener, ViewHandleUpdateUser, View.OnFocusChangeListener, View.OnKeyListener, CompoundButton.OnCheckedChangeListener {

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
    private TextView mTxvNowPassword;
    private EditText mEdtNowPassword;
    private TextView mTxvNewPassword;
    private Switch mSwtNowPassword;
    private Switch mSwtNewPassword;
    private Switch mSwtConfirmPassword;

    private Dialog mLoadingDialog;

    private PresenterUpdateUser mPresenterUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_register, container, false);

        try{
            initViews();
            createLoadingDialog();
            handleStart();
        }
        catch (java.lang.NullPointerException e){}
        return this.mView;
    }

    private void initViews(){
        this.mEdtName = mView.findViewById(R.id.edt_full_name);
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        this.mEdtNewPassword = mView.findViewById(R.id.edt_password);
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
        this.mTxvNewPassword = mView.findViewById(R.id.txv_password);
        this.mTxvNowPassword = mView.findViewById(R.id.txv_now_password);
        this.mEdtNowPassword = mView.findViewById(R.id.edt_now_password);
        this.mSwtNowPassword = mView.findViewById(R.id.swt_now_password);
        this.mSwtNewPassword = mView.findViewById(R.id.swt_password);
        this.mSwtConfirmPassword = mView.findViewById(R.id.swt_confirm_password);

        this.mTxvNewPassword.setText(getActivity().getResources().getString(R.string.new_password));

        this.mBtnConfirm.setOnClickListener(this);
        this.mImvCalendar.setOnClickListener(this);

        this.mEdtUsername.setFocusable(false);
        this.mEdtEmail.setFocusable(false);

        this.mBtnConfirm.setOnClickListener(this);

        this.mEdtNowPassword.setOnKeyListener(this);
        this.mEdtNewPassword.setOnKeyListener(this);
        this.mEdtConfirmPassword.setOnKeyListener(this);
        this.mEdtPhoneNumber.setOnKeyListener(this);

        this.mSwtNowPassword.setOnCheckedChangeListener(this);
        this.mSwtNewPassword.setOnCheckedChangeListener(this);
        this.mSwtConfirmPassword.setOnCheckedChangeListener(this);
    }


    private void handleStart(){
        this.mPresenterUpdate = new PresenterUpdateUser(this);

        this.mEdtName.setText(USER.getFullName());
        this.mEdtAddress.setText(USER.getAddress());
        this.mEdtUsername.setText(USER.getUsername());
        this.mEdtEmail.setText(USER.getEmail());
        this.mEdtPhoneNumber.setText(USER.getPhone());
        this.mTxvBirthDay.setText(USER.getBirthDay());

        String sex = USER.getSex();
        if(sex.equals("Nam")){
            this.mRdoMale.setChecked(true);
        }
        else if(sex.equals("Nữ")){
            this.mRdoFemale.setChecked(true);
        }
        else{
            this.mRdoOther.setChecked(true);
        }

        this.mEdtEmail.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_bakcgroud_gray));
        this.mEdtUsername.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_bakcgroud_gray));

    }

    private void handleUpdateUser(){

        mLoadingDialog.show();

        String name = mEdtName.getText().toString().trim();

        String sex;
        if(mRdoFemale.isChecked()){
            sex = "Nữ";
        }
        else if(mRdoMale.isChecked()){
            sex = "Nam";
        }
        else{
            sex = "Khác";
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
            ((UserActionActivity)getActivity()).toastHelper.toast("Vui lòng nhập mật khẩu hiện tại!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtNowPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtNowPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(TextUtils.isEmpty(password)){
            ((UserActionActivity)getActivity()).toastHelper.toast("Vui lòng nhập mật khẩu mới!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtNewPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(password.length() < 6){
            ((UserActionActivity)getActivity()).toastHelper.toast("Mật khẩu phải có độ dài ít nhất 6 kí tự!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtNewPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(password.length() > 20){
            ((UserActionActivity)getActivity()).toastHelper.toast("Mật khẩu tối đa 20 kí tự!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtNewPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(mEdtConfirmPassword.getText().toString().equals("")){
            ((UserActionActivity)getActivity()).toastHelper.toast("Vui lòng nhập xác nhận mật khẩu!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(!mEdtConfirmPassword.getText().toString().equals(password)){
            ((UserActionActivity)getActivity()).toastHelper.toast("Mật khẩu và xác nhận mật khẩu không giống nhau!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtConfirmPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            ((UserActionActivity)getActivity()).toastHelper.toast("Vui lòng nhập số điện thoại!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else if(!FormatPattern.checkNumberPhone(phoneNumber)){
            ((UserActionActivity)getActivity()).toastHelper.toast("Số điện thoại không hợp lệ!!!", ToastHelper.LENGTH_SHORT);
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtNewPassword.setText("");
            mEdtNowPassword.setText("");

            this.mEdtPhoneNumber.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
        }
        else{
            mLoadingDialog.show();
            User user = new User(name, sex, birthday, phoneNumber, email, address, username,
                    HashMD5.md5(password));
            this.mPresenterUpdate.handleUpdateUser(user, HashMD5.md5(nowPassword));
        }
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    @Override
    public void onUpdateUserSuccessful() {

        this.mLoadingDialog.dismiss();

        ((UserActionActivity) Objects.requireNonNull(getActivity())).getAlertHelper().alert("Cập nhật tài khoản",
                "Cập nhật tài khoản thành công!!!",
                false, "OK", AlertHelper.ALERT_NO_ACTION);
    }

    @Override
    public void onUpdateUserError(String error) {

        if(error.equals("old_password_not_true")){
            ((UserActionActivity) Objects.requireNonNull(getActivity())).getAlertHelper().alert("Cập nhật tài khoản",
                    "Mật khẩu hiện tại không chính xác. Vui lòng kiểm tra lại!!!",
                    false, "OK", AlertHelper.ALERT_NO_ACTION);
        }
        else{
            ((UserActionActivity) Objects.requireNonNull(getActivity())).getAlertHelper().alert("Cập nhật tài khoản",
                    "Cập nhật tài khoản không thành công. Vui lòng thử lại sau!!!",
                    false, "OK", AlertHelper.ALERT_NO_ACTION);
        }

        this.mLoadingDialog.dismiss();


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
        Button mBtnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button mBtnCancel = dialog.findViewById(R.id.btn_cancel);

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


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            AppUtils.hideKeyboard(getActivity());
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (v.getId()){
            case R.id.edt_now_password:
                if(this.mEdtNowPassword.getText().toString().trim().length() < 6 || this.mEdtNowPassword.getText().toString().trim().length() > 20){
                    this.mEdtNowPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
                }
                else{
                    this.mEdtNowPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_password:
                if(this.mEdtNewPassword.getText().toString().trim().length() < 6 || this.mEdtNewPassword.getText().toString().trim().length() > 20){
                    this.mEdtNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_red_backgroud_white));
                }
                else{
                    this.mEdtNewPassword.setBackground(getResources().getDrawable(R.drawable.custom_border_gray_backgroud_default));
                }
                break;
            case R.id.edt_confirm_password:
                if(!this.mEdtNewPassword.getText().toString().trim().equals(this.mEdtConfirmPassword.getText().toString().trim())){
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
            case R.id.swt_now_password:
                if(!isChecked){
                    this.mEdtNowPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    this.mEdtNowPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
            case R.id.swt_password:
                if(!isChecked){
                    this.mEdtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    this.mEdtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
            case R.id.swt_confirm_password:
                if(!isChecked){
                    this.mEdtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    this.mEdtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
        }
    }
}
