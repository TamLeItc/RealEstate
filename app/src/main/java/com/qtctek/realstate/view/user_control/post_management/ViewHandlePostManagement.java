package com.qtctek.realstate.view.user_control.post_management;

import com.qtctek.realstate.dto.PostSale;

import java.util.ArrayList;

public interface ViewHandlePostManagement {

    void onHandlePostListSuccessful(int qualityPost, ArrayList<PostSale> arrListPost);

    void onHandlePostListError(String error);

    void onAcceptPostSuccessful();

    void onAcceptPostError(String error);

    void onDeletePostSuccessful();

    void onDeletePostError(String error);

}
