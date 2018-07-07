package com.qtctek.realstate.presenter.user_control.saved_post;

public interface PresenterImpIHandleSavedPost {

    void onGetDataProductIdsSuccessful(String data);

    void onGetDataProductIdsError(String error);

    void onUpdateProductIdListSuccessful();

    void onUpdateProductIdListError(String e);

    void onGetSavedProductListSuccessful(String data);

    void onGetSavedProductListError(String error);

}
