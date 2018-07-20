package com.qtctek.realstate.view.post_news.interfaces;

import com.qtctek.realstate.dto.Product;

import java.util.ArrayList;

public interface ViewHandlePostNews {

    void handlePostListSuccessful(ArrayList<Product> arrProduct);

    void handlePostListError(String error);

    void handleQualityPostSuccessful(int quality);

    void handleQualityPostError(String error);

}

