package com.qtctek.aladin.presenter.user_action.forgot_password;

import com.qtctek.aladin.model.user_action.ModelForgotPassword;
import com.qtctek.aladin.view.user_action.forgot_password.ViewHandleForgotPassword;

public class PresenterForgotPassword implements PresenterImpHandleForgotPassword {

    private ViewHandleForgotPassword mViewHandleForgotPassword;
    private ModelForgotPassword mModelForgotPassword;

    public PresenterForgotPassword(ViewHandleForgotPassword viewHandleForgotPassword){
        this.mViewHandleForgotPassword = viewHandleForgotPassword;
        this.mModelForgotPassword = new ModelForgotPassword(this);
    }

    public void handleResetPassword(String email, String password){
        this.mModelForgotPassword.requireUpdatePassword(email, password);
    }

    @Override
    public void onUpdatePasswordSuccessful() {
        this.mViewHandleForgotPassword.onResetPasswordSuccessful();
    }

    @Override
    public void onUpdatePasswordError() {
        this.mViewHandleForgotPassword.onResetPasswordError();
    }
}
