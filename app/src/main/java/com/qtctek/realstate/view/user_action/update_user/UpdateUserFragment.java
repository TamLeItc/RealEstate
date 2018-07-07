package com.qtctek.realstate.view.user_action.update_user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.dto.User_Object;
import com.qtctek.realstate.presenter.user_action.general.FormatPattern;
import com.qtctek.realstate.presenter.user_action.general.HashMD5;
import com.qtctek.realstate.presenter.user_action.update_user.PresenterUpdateUser;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.Calendar;

import static com.qtctek.realstate.view.post_news.activity.MainActivity.USER;

public class UpdateUserFragment extends Fragment implements View.OnClickListener, ViewHandleUpdateUser {

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
    private TextView mTxvTitle;

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
        this.mEdtPassword = mView.findViewById(R.id.edt_password);
        this.mEdtConfirmPassword = mView.findViewById(R.id.edt_confirm_password);
        this.mEdtPhoneNumber = mView.findViewById(R.id.edt_phone_number);
        this.mBtnConfirm = mView.findViewById(R.id.btn_confirm);
        this.mRdoFemale = mView.findViewById(R.id.rdo_female);
        this.mRdoMale = mView.findViewById(R.id.rdo_male);
        this.mTxvBirthDay = mView.findViewById(R.id.txv_birthday);
        this.mEdtAddress = mView.findViewById(R.id.edt_address);
        this.mImvCalendar = mView.findViewById(R.id.imv_calendar);
        this.mEdtUsername = mView.findViewById(R.id.edt_username);
        this.mTxvTitle = mView.findViewById(R.id.txv_title);
        this.mRdoOther = mView.findViewById(R.id.rdo_other);

        this.mBtnConfirm.setOnClickListener(this);
        this.mImvCalendar.setOnClickListener(this);

        this.mEdtUsername.setInputType(InputType.TYPE_NULL);
        this.mEdtEmail.setInputType(InputType.TYPE_NULL);

        this.mBtnConfirm.setOnClickListener(this);
    }


    private void handleStart(){
        this.mPresenterUpdate = new PresenterUpdateUser(this);
        this.mTxvTitle.setText(getActivity().getResources().getString(R.string.update_user));

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
        String password = mEdtPassword.getText().toString().trim();
        String phoneNumber = mEdtPhoneNumber.getText().toString().trim();
        String address = mEdtAddress.getText().toString().trim();

        mLoadingDialog.dismiss();

        if(username.equals("")){
            Toast.makeText(getContext(), "Vui lòng nhập tên đăng nhập!!!", Toast.LENGTH_SHORT).show();
            this.mEdtName.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(email.equals("")){
            Toast.makeText(getContext(), "Vui lòng nhập email!!!", Toast.LENGTH_SHORT).show();
            this.mEdtEmail.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(!FormatPattern.checkEmail(email)){
            Toast.makeText(getContext(), "Email không chính xác!!!", Toast.LENGTH_SHORT).show();
            this.mEdtEmail.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(password.equals("")){
            Toast.makeText(getContext(), "Vui lòng nhập mật khẩu!!!", Toast.LENGTH_SHORT).show();
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(password.length() < 6){
            Toast.makeText(getContext(), "Mật khẩu phải có độ dài ít nhất 6 kí tự!!!", Toast.LENGTH_SHORT).show();
            this.mEdtPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(password.equals("")){
            Toast.makeText(getContext(), "Vui lòng xác nhận mật khẩu!!!", Toast.LENGTH_SHORT).show();
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(!password.equals(password)){
            Toast.makeText(getContext(), "Mật khẩu và xác nhận mật khẩu không giống nhau!!!", Toast.LENGTH_SHORT).show();
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(phoneNumber.equals("")){
            Toast.makeText(getContext(), "Vui lòng nhập số điện thoại!!", Toast.LENGTH_SHORT).show();
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else{
            mLoadingDialog.show();
            User user = new User(name, sex, birthday, phoneNumber, email, address, username,
                    HashMD5.md5(password));
            this.mPresenterUpdate.handleUpdateUser(user);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Cập nhật tài khoản")
                .setCancelable(false)
                .setMessage("Cập nhật tài khoản thành công!!!")
                .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), UserControlActivity.class);
                        intent.putExtra("email_user", MainActivity.USERr);
                        intent.putExtra("role", MainActivity.LEVEL);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        builder.show();
    }

    @Override
    public void onUpdateUserError(String error) {

        this.mLoadingDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Cập nhật tài khoản")
                .setCancelable(false)
                .setMessage("Cập nhật tài khoản không thành công. Vui lòng thử lại sau!!!")
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
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthDate = mDpkBirthDay.getDayOfMonth() + "/" + (mDpkBirthDay.getMonth() + 1) + "/" + mDpkBirthDay.getYear();
                mTxvBirthDay.setText(birthDate);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
