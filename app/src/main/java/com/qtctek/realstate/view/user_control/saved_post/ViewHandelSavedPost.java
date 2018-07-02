package com.qtctek.realstate.view.user_control.saved_post;

import com.qtctek.realstate.dto.PostSale;

import java.util.ArrayList;

public interface ViewHandelSavedPost {

    void onHandleSavedPostListSuccessful(int qualityPost, ArrayList<PostSale> arrListProduct);

    void onHandleSavedPostListError(String error);

    void onHandleUnSavePostSuccessful();

    void onHandleUnSavePostError(String e);

}
