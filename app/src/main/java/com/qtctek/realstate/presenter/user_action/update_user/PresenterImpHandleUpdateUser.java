package com.qtctek.realstate.presenter.user_action.update_user;

public interface PresenterImpHandleUpdateUser {

    void onUpdateUserSuccessful();

    void onUpdateUserError(String error);

    void onGetInformationUserSuccessful(String data);

    void onGetInformationUserError(String error);

}
