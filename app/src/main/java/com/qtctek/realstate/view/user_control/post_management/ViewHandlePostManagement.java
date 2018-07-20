package com.qtctek.realstate.view.user_control.post_management;

import com.qtctek.realstate.dto.Product;

import java.util.ArrayList;

public interface ViewHandlePostManagement {

    void onHandlePostListSuccessful(ArrayList<Product> arrListProduct);

    void onHandlePostListError(String error);

    void onAcceptPostSuccessful();

    void onAcceptPostError(String error);

    void onDeletePostSuccessful();

    void onDeletePostError(String error);

}
