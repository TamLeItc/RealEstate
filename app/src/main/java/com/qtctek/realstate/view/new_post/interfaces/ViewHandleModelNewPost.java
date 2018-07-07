package com.qtctek.realstate.view.new_post.interfaces;

public interface ViewHandleModelNewPost {

    void onInsertBlankPost(boolean status, int productId);

    void onUploadImages(boolean status);

    void onUpdateProductInformation(boolean status);

    void onUpdateDescriptionInformation(boolean status);

    void onUpdateMapInformation(boolean status);

    void onDeleteFile(boolean status);

    void onUpdateHandlePost(boolean status);

}
