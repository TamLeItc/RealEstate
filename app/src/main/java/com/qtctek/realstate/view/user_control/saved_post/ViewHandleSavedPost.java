package com.qtctek.realstate.view.user_control.saved_post;

import com.qtctek.realstate.dto.Product;

import java.util.ArrayList;
import java.util.HashMap;

public interface ViewHandleSavedPost {

    void onHandleDataProductIdsSuccessful(HashMap<String, String> list);

    void onHandleDataProductIdsError(String error);

    void onHandleUpdateProductIdListSuccessful();

    void onHandleUpdateProductIdListError(String e);

    void onHandleSavedProductListSuccessful(ArrayList<Product> mArrListProduct);

    void onHandleSavedProductListError(String error);
}
