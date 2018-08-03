package com.qtctek.aladin.view.user_control.saved_search;


import com.qtctek.aladin.dto.Condition;

import java.util.ArrayList;

public interface ViewHandleSavedSearch {

    void onHandleDataSavedSearchSuccessful(ArrayList<Condition> lists);

    void onHandleDataSavedSearchError(String error);

    void onHandleDataLastSearchSuccessful(Condition condition);

    void onHandleUpdateSavedSearchListSuccessful();

    void onHandleUpdateSavedSearchListError(String error);

}
