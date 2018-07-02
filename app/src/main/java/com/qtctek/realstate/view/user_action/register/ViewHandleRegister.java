package com.qtctek.realstate.view.user_action.register;

public interface ViewHandleRegister {

    void onRegisterSuccessful();

    void onRegisterError(String error);

    void onCheckExistEmail(boolean isExisted);

    void onConnectServerError();

}
