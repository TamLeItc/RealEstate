package com.qtctek.realstate.view.user_action.login;

import com.qtctek.realstate.dto.User;

public interface ViewHandleLogin {

    void onHandleCheckUserNotExists();

    void onHandleCheckUserLoginSuccessful(User user);

    void onHandleCheckUserLoginError(String error);

    void onGetDataSaveLoginSuccessful(String userName, String password);

    void onGetDataSaveLoginError(String error);

    void onUpdateDataSaveLoginSuccessful();

    void onUpdateDataSaveLoginError(String error);

}
