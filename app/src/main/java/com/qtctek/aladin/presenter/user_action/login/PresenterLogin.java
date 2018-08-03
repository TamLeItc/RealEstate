package com.qtctek.aladin.presenter.user_action.login;

import android.content.Context;

import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.model.user_action.ModelLogin;
import com.qtctek.aladin.view.user_action.login.ViewHandleLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresenterLogin implements PresenterImpHandleLogin {

    private ViewHandleLogin mViewHandleUserManager;
    private ModelLogin mModelLogin;

    public PresenterLogin(ViewHandleLogin viewHandleUserManager){
        this.mViewHandleUserManager = viewHandleUserManager;
        mModelLogin = new ModelLogin(this);
    }

    public void handleCheckUserLogin(String user, String password){
        mModelLogin.requireCheckUserLogin(user, password);
    }

    public void handleGetDataSaveLogin(Context context){
        mModelLogin.requireGetDataSaveLogin(context);
    }

    public void handleUpdateDataSaveLogin(String userName, String password, Context context){
        mModelLogin.requireUpdateDataSaveLogin(userName, password, context);
    }

    @Override
    public void onCheckUserLoginSuccessful(String message) {
        if(message.equals("0")){
            this.mViewHandleUserManager.onHandleCheckUserNotExists();
        }
        else{
            try {
                JSONArray jsonArray = new JSONArray(message);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                User user = new User();
                user.setId(jsonObject.getInt("id"));
                user.setFullName(jsonObject.getString("name"));
                user.setSex(jsonObject.getString("sex"));
                user.setBirthDay(jsonObject.getString("birthday"));
                user.setPhone(jsonObject.getString("phone"));
                user.setEmail(jsonObject.getString("email"));
                user.setAddress(jsonObject.getString("address"));
                user.setUsername(jsonObject.getString("username"));
                user.setStatus(jsonObject.getString("status"));
                user.setLevel(jsonObject.getInt("level"));

                this.mViewHandleUserManager.onHandleCheckUserLoginSuccessful(user);

            } catch (JSONException e) {
                this.mViewHandleUserManager.onHandleCheckUserLoginError(e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCheckUserLoginError(String error) {
        this.mViewHandleUserManager.onHandleCheckUserLoginError(error);
    }

    @Override
    public void onGetDataSaveLoginSuccessful(String userName, String password) {
        this.mViewHandleUserManager.onGetDataSaveLoginSuccessful(userName, password);
    }

    @Override
    public void onGetDataSaveLoginError(String error) {

    }

    @Override
    public void onUpdateDataSaveLoginSuccessful() {
        this.mViewHandleUserManager.onUpdateDataSaveLoginSuccessful();
    }

    @Override
    public void onUpdateDataSaveLoginError(String error) {

    }
}
