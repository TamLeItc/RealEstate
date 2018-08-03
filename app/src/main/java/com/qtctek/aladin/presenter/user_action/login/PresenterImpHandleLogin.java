package com.qtctek.aladin.presenter.user_action.login;

public interface PresenterImpHandleLogin {

    void onCheckUserLoginSuccessful(String data);

    void onCheckUserLoginError(String error);

    void onGetDataSaveLoginSuccessful(String userName, String password);

    void onGetDataSaveLoginError(String error);

    void onUpdateDataSaveLoginSuccessful();

    void onUpdateDataSaveLoginError(String error);

}
