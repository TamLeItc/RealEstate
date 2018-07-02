package com.qtctek.realstate.presenter.user_control.user_management;

public interface PresenterImpHandleUserManagement {

    void onGetUserListSuccessful(String data);

    void onGetUserListError(String error);

    void onUpdateStatusUserSuccessful();

    void onUpdateStatusUserError(String error);

}
