package com.qtctek.realstate.view.user_control.saved_search;


import com.qtctek.realstate.dto.Condition;

import java.util.ArrayList;

public interface ViewHandleSavedSearch {

    void onHandleDataSavedSearchSuccessful(ArrayList<Condition> lists);

    void onHandleDataSavedSearchError(String error);

    void onHandleUpdateSavedSearchListSuccessful();

    void onHandleUpdateSavedSearchListError(String error);

}
