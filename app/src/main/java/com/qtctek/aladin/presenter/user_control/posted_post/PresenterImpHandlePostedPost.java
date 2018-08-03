package com.qtctek.aladin.presenter.user_control.posted_post;

public interface PresenterImpHandlePostedPost {

    void onGetPostListSuccessful(String data);

    void onGetPostListError(String error);

    void onDeletePostSuccessful();

    void onDeletePostError(String error);

}
