package com.qtctek.realstate.view.post_detail.interfaces;

import com.qtctek.realstate.dto.Photo;
import com.qtctek.realstate.dto.Product;

import java.util.ArrayList;

public interface ViewHandlePostDetail {

    void onHandleDataPostDetailSuccessful(Product product, ArrayList<Photo> arrPhoto);

    void onHandleDataPostDetailError(String error);
}
