package com.qtctek.realstate.view.user_action.register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.qtctek.realstate.presenter.user_action.general.RandomString;
import com.qtctek.realstate.presenter.user_action.general.send_gmail.GMailSender;
import com.qtctek.realstate.presenter.user_action.register.PresenterRegister;

import java.util.Calendar;

public class RegisterFragment extends Fragment implements View.OnClickListener, ViewHandleRegister {

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

        this.mBtnConfirm.setOnClickListener(this);
        this.mImvCalendar.setOnClickListener(this);
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
        else if(confirmPassword.equals("")){
            Toast.makeText(getContext(), "Vui lòng xác nhận mật khẩu!!!", Toast.LENGTH_SHORT).show();
            this.mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
        }
        else if(!password.equals(confirmPassword)){
            Toast.makeText(getContext(), "Mật khẩu và xác nhận mật khẩu không giống nhau!!!", Toast.LENGTH_SHORT).show();
            this.mEdtConfirmPassword.requestFocus();
        }
        else if(phoneNumber.equals("")){
            Toast.makeText(getContext(), "Vui lòng nhập số điện thoại!!", Toast.LENGTH_SHORT).show();
            this.mEdtPhoneNumber.requestFocus();
            mEdtConfirmPassword.setText("");
            mEdtPassword.setText("");
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
        GMailSender gMailSender = new GMailSender();
        try {
            gMailSender.sendMail("Xác nhận đăng kí tài khoản Real Estate", "Mã xác nhận " +
                    " tài khoản Real Estate là: " + this.mConfirmCode, this.mEdtEmail.getText().toString().trim());
        } catch (Exception e) {
            return false;
        }
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Đăng kí tài khoản")
                    .setCancelable(false)
                    .setMessage("Có lỗi xảy ra trong quá trình đăng kí. Vui lòng thử lại sau!!!")
                    .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                            viewPager.setCurrentItem(0);
                            mDialog.dismiss();
                        }
                    });
            builder.show();
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
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mConfirmCode.equals(mEdtConfirmCode.getText().toString())){

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

                    mLoadingDialog.show();
                    User user = new User(name, sex, birthday, phoneNumber, email, address, username,
                            HashMD5.md5(password));
                    Log.d("ttt", "aaa");
                    mPresenterRegister.handleRegister(user);
                }
                else{
                    mLoadingDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Đăng kí tài khoản")
                            .setCancelable(false)
                            .setMessage("Mã xác nhận không chính xác. Vui lòng kiểm tra lại!!!")
                            .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                }
            }
        });


    }

    @Override
    public void onRegisterSuccessful() {

        mLoadingDialog.dismiss();
        mDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Đăng kí tài khoản")
                .setCancelable(false)
                .setMessage("Đăng kí tài khoản thành công. Mời đăng nhập để sử dụng dịch vụ")
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
    public void onRegisterError(String error) {

        mLoadingDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Đăng kí tài khoản")
                .setCancelable(false)
                .setMessage("Đăng kí tài khoản không thành công. Vui lòng thử lại sau!!!")
                .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    public void onCheckExistEmail(String message) {
        mLoadingDialog.dismiss();
        if(message.equals("email_existed") || message.equals("username_existed")){
            String stMessage;
            if(message.equals("email_existed")){
                stMessage = "Email đã được sử dụng cho một tài khoản khác. Vui lòng thử lại email khác!!!";
            }
            else{
                stMessage = "Tên đăng nhập đã được sử dụng. Vui lòng thử lại tên khác!!!";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Đăng kí tài khoản")
                    .setCancelable(false)
                    .setMessage(stMessage)
                    .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEdtPassword.setText("");
                            mEdtConfirmPassword.setText("");
                        }
                    });
            builder.show();
        }
        else{
            this.mLoadingDialog.show();
            handleConfirmEmail();
        }
    }

    @Override
    public void onConnectServerError(String s) {
        this.mLoadingDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Đăng kí tài khoản")
                .setCancelable(false)
                .setMessage("Kết nối server thất bại. Vui lòng kiểm tra kết nối của bạn và thử lại!!!")
                .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEdtEmail.requestFocus();
                        mEdtPassword.setText("");
                        mEdtConfirmPassword.setText("");
                    }
                });
        builder.show();
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
