package com.qtctek.aladin.view.user_control.posted_post;

import com.qtctek.aladin.dto.Product;

import java.util.ArrayList;

public interface ViewHandlePostedPost {

    void onHandlePostListSuccessful(ArrayList<Product> arrListProduct);

    void onHandlePostListError(String error);

    void onDeletePostSuccessful();

    void onDeletePostError(String error);

}
