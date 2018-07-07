package com.qtctek.realstate.view.post_detail.interfaces;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;

import java.util.ArrayList;

public interface ViewHandlePostDetail {

    void onHandleDataPostDetailSuccessful(Product product, ArrayList<String> arrImages);

    void onHandleDataPostDetailError(String error);
}
