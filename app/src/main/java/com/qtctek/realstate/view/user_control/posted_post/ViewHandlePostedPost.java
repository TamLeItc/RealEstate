package com.qtctek.realstate.view.user_control.posted_post;

import com.qtctek.realstate.dto.Product;

import java.util.ArrayList;

public interface ViewHandlePostedPost {

    void onHandlePostListSuccessful(ArrayList<Product> arrListProduct);

    void onHandlePostListError(String error);

    void onDeletePostSuccessful();

    void onDeletePostError(String error);

}
