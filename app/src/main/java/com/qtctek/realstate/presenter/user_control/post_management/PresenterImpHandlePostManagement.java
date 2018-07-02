package com.qtctek.realstate.presenter.user_control.post_management;

public interface PresenterImpHandlePostManagement {

    void onGetPostListSuccessful(String data);

    void onGetPostListError(String error);

    void onAcceptPostSuccessful();

    void onAcceptPostError(String error);

    void onDeletePostSuccessful();

    void onDeletePostError(String error);

}

