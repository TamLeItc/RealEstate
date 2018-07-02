package com.qtctek.realstate.view.post_detail.interfaces;

import com.qtctek.realstate.dto.PostSale;

import java.util.ArrayList;

public interface ViewHandlePostDetail {

    void onHandleDataPostDetailSuccessful(PostSale postSale, ArrayList<String> arrImages);

    void onHandleDataPostDetailError(String error);

    void onSavePost(String value);
}
