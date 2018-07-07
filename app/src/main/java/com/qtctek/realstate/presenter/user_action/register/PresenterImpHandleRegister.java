package com.qtctek.realstate.presenter.user_action.register;

public interface PresenterImpHandleRegister {

    void onInsertValueToServerSuccessful();

    void onInsertValueToServerError(String error);

    void onCheckExistEmail(String message);

    void onConnectServerError(String s);

}
