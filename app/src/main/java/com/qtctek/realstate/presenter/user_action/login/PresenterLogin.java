package com.qtctek.realstate.presenter.user_action.login;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.model.user_action.ModelLogin;
import com.qtctek.realstate.view.user_action.login.ViewHandleLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresenterLogin implements PresenterImpHandleLogin {

    private ViewHandleLogin mViewHandleUserManager;

    public PresenterLogin(ViewHandleLogin viewHandleUserManager){
        this.mViewHandleUserManager = viewHandleUserManager;
    }

    public void handleCheckUserLogin(String user, String password){
        ModelLogin modelUserManager = new ModelLogin(this);
        modelUserManager.requireCheckUserLogin(user, password);
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
}
