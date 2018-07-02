package com.qtctek.realstate.view.user_control.posted_post;

import com.qtctek.realstate.dto.PostSale;

import java.util.ArrayList;

public interface ViewHandlePostedPost {

    void onHandlePostListSuccessful(int qualityPost, ArrayList<PostSale> arrListProduct);

    void onHandlePostListError(String error);

    void onDeletePostSuccessful();

    void onDeletePostError(String error);

}
