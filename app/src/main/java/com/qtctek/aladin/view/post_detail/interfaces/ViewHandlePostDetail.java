package com.qtctek.aladin.view.post_detail.interfaces;

import com.qtctek.aladin.dto.Photo;
import com.qtctek.aladin.dto.Product;

import java.util.ArrayList;

public interface ViewHandlePostDetail {

    void onHandleDataPostDetailSuccessful(Product product, ArrayList<Photo> arrPhoto);

    void onHandleDataPostDetailError(String error);
}
