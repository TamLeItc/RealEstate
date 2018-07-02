package com.qtctek.realstate.view.new_post.interfaces;

public interface ViewHandleModelNewPost {

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
