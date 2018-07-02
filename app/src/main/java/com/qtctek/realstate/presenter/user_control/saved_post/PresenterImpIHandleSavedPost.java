package com.qtctek.realstate.presenter.user_control.saved_post;

public interface PresenterImpIHandleSavedPost {

    void onGetSavedPostListSuccessful(String data);

    void onHandleSavedPostListError(String error);

    void onExecuteUnSavePostSuccessful();
    void onExecuteUnSavePostError(String e);

}
