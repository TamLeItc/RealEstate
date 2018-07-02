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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.presenter.user_action.general.HashMD5;
import com.qtctek.realstate.presenter.user_action.update_user.PresenterUpdateUser;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

public class UpdateUserFragment extends Fragment implements View.OnClickListener, ViewHandleUpdateUser {

    private View mView;

    private EditText mEdtName;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private EditText mEdtPhoneNumber;
    private Button mBtnConfirm;

    private Dialog mLoadingDialog;

    private PresenterUpdateUser mPresenterUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_register, container, false);

        initViews();
        createLoadingDialog();
        handleStart();
        return this.mView;
    }

    private void initViews(){
        this.mEdtName = mView.findViewById(R.id.edt_full_name);
        this.mEdtEmail = mView.findViewById(R.id.edt_email_address);
        this.mEdtPassword = mView.findViewById(R.id.edt_password);
        this.mEdtConfirmPassword = mView.findViewById(R.id.edt_confirm_password);
        this.mEdtPhoneNumber = mView.findViewById(R.id.edt_phone_number);
        this.mBtnConfirm = mView.findViewById(R.id.btn_confirm);

        this.mEdtEmail.setInputType(InputType.TYPE_NULL);
        this.mEdtEmail.setText(MainActivity.EMAIL_USER);

        this.mBtnConfirm.setOnClickListener(this);
    }

    private void handleStart(){
        this.mPresenterUpdate = new PresenterUpdateUser(this);

        if(!MainActivity.EMAIL_USER.equals("")){
            this.mLoadingDialog.show();
            this.mPresenterUpdate.handleGetInformationUser(MainActivity.EMAIL_USER);
        }
    }

    private void handleLogin(){
        String name = this.mEdtName.getText().toString().trim();
        String password = this.mEdtPassword.getText().toString().trim();
        String confirmPassword = this.mEdtConfirmPassword.getText().toString().trim();
        String phoneNumber = this.mEdtPhoneNumber.getText().toString().trim();

        if(name.equals("")){
            Toast.makeText(getContext(), "Vui lòng nhập họ và tên!!!", Toast.LENGTH_SHORT).show();
            this.mEdtName.requestFocus();
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

            User user = new User(name, MainActivity.EMAIL_USER, HashMD5.md5(password), phoneNumber);
            this.mPresenterUpdate.handleUpdateUser(user);
            this.mLoadingDialog.show();

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
                        intent.putExtra("email_user", MainActivity.EMAIL_USER);
                        intent.putExtra("role", MainActivity.ROLE_USER);
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
    public void onHandleInformationUserSuccessful(User user) {

        this.mLoadingDialog.dismiss();

        this.mEdtName.setText(user.getName());
        this.mEdtEmail.setText(user.getEmail());
        this.mEdtPhoneNumber.setText(user.getPhoneNumber());
    }

    @Override
    public void onHandleInformationUserError(String error) {

        this.mLoadingDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Cập nhật tài khoản")
                .setCancelable(false)
                .setMessage("Đọc thông tin từ server thất bại!!!")
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
                handleLogin();
                break;
        }
    }
}
