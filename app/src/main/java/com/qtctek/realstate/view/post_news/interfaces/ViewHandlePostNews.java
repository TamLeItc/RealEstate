package com.qtctek.realstate.view.post_news.interfaces;

import com.qtctek.realstate.dto.PostSale;

import java.util.ArrayList;

public interface ViewHandlePostNews {

    void handlePostListSuccessful(ArrayList<PostSale> arrPost);
    void handlePostListError(String error);

}
