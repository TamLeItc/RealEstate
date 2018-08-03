package com.qtctek.aladin.presenter.post_news;

public interface PresenterImpHandlePostNews {

    void getPostListSuccessful(String data);

    void getPostListError(String error);

    void getQualityPostSuccessful(String data);

    void getQualityPostError(String error);
}
