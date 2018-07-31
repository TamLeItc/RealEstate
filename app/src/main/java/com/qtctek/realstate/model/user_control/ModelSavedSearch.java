package com.qtctek.realstate.model.user_control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.presenter.user_control.save_search.PresenterImpHandleSavedSearch;

public class ModelSavedSearch {

    private PresenterImpHandleSavedSearch mPresenterImpHandleSavedSearch;

    public ModelSavedSearch(PresenterImpHandleSavedSearch presenterImpHandleSavedSearch){
        this.mPresenterImpHandleSavedSearch = presenterImpHandleSavedSearch;
    }

    public void requireGetDataSavedSearch(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
        String data = sharedPreferences.getString(Constant.SAVED_SEARCH_LIST, "");
        mPresenterImpHandleSavedSearch.onGetDataSavedSearchSuccessful(data);
    }

    public void requireGetDataLastSearch(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
        String data = sharedPreferences.getString(Constant.LAST_SEARCH, "");
        mPresenterImpHandleSavedSearch.onGetDataLastSearchSuccessful(data);
    }

    public void requireUpdateDataSavedSearch(String data, Context context){

            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(Constant.SAVED_SEARCH_LIST, data);
            editor.commit();

            mPresenterImpHandleSavedSearch.onUpdateDataSavedSearchSuccessful();
    }

    public void requireUpdateDataLastSearch(String data, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constant.LAST_SEARCH, data);
        editor.commit();
    }

}
