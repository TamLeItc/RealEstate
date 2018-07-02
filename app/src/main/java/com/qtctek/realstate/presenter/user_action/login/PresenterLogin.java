package com.qtctek.realstate.presenter.user_action.login;

import com.qtctek.realstate.model.user_action.ModelLogin;
import com.qtctek.realstate.view.user_action.login.ViewHandleLogin;

public class PresenterLogin implements PresenterImpHandleLogin {

    private ViewHandleLogin mViewHandleUserManager;

    public PresenterLogin(ViewHandleLogin viewHandleUserManager){
        this.mViewHandleUserManager = viewHandleUserManager;
    }

    public void handleCheckUserLogin(String email, String password){
        String relativeUrl = "";
        ModelLogin modelUserManager = new ModelLogin(this);
        modelUserManager.requireCheckUserLogin(email, password);
    }

    @Override
    public void onCheckUserLoginSuccessful(String message) {
        this.mViewHandleUserManager.onHandleCheckUserLoginSuccessful(message);
    }

    @Override
    public void onCheckUserLoginError(String error) {
        this.mViewHandleUserManager.onHandleCheckUserLoginError(error);
    }
}
