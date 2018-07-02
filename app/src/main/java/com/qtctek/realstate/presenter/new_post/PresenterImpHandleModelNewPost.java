package com.qtctek.realstate.presenter.new_post;

public interface PresenterImpHandleModelNewPost {

    void onInsertBlankPost(boolean status, int postId);

    void onUploadImages(boolean status);

    void onUpdateNormalInformation(boolean status);

    void onUpdateMoreInformation(boolean status);

    void onUpdateDescriptionInformation(boolean status);

    void onUpdateMapInformation(boolean status);

    void onUpdateContactInformation(boolean status);

    void onDeleteFile(boolean status);

    void onUpdateHandlePost(boolean status);

}
