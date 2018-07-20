package com.qtctek.realstate.presenter.user_action.register;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.model.user_action.ModelRegister;
import com.qtctek.realstate.view.user_action.register.ViewHandleRegister;

public class PresenterRegister implements PresenterImpHandleRegister {

    private ViewHandleRegister mViewHandleRegister;

    private ModelRegister mModelRegister;

    public PresenterRegister(ViewHandleRegister viewHandleRegister){
        this.mViewHandleRegister = viewHandleRegister;
        mModelRegister = new ModelRegister(this);
    }

    public void handleCheckEmail(String email, String username){

        this.mModelRegister.requireCheckEmail(email, username);
    }

    public void handleRegister(User user){
        this.mModelRegister.requireInsertUser(user);
    }
    @Override
    public void onInsertValueToServerSuccessful() {
        this.mViewHandleRegister.onRegisterSuccessful();
    }

    @Override
    public void onInsertValueToServerError(String error) {
        this.mViewHandleRegister.onRegisterError(error);
    }

    @Override
    public void onCheckExistEmail(String message) {
        this.mViewHandleRegister.onCheckExistEmail(message);
    }

    @Override
    public void onConnectServerError(String s) {
        this.mViewHandleRegister.onConnectServerError(s);
    }
}
