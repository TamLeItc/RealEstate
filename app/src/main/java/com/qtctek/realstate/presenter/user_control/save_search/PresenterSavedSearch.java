package com.qtctek.realstate.presenter.user_control.save_search;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qtctek.realstate.dto.Condition;
import com.qtctek.realstate.model.user_control.ModelSavedSearch;
import com.qtctek.realstate.view.user_control.saved_search.ViewHandleSavedSearch;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PresenterSavedSearch implements PresenterImpHandleSavedSearch {

    private ViewHandleSavedSearch mViewHandleSavedSearch;
    private ModelSavedSearch mModelSavedSearch;

    public PresenterSavedSearch(ViewHandleSavedSearch mViewHandleSavedSearch) {
        this.mViewHandleSavedSearch = mViewHandleSavedSearch;
        this.mModelSavedSearch = new ModelSavedSearch(this);
    }

    public void  handleGetDataSavedSearch(Context context){
        this.mModelSavedSearch.requireGetDataSavedSearch(context);
    }

    public void  handleGetDataLastSearch(Context context){
        this.mModelSavedSearch.requireGetDataLastSearch(context);
    }

    public void handleUpdateSavedSearchList(ArrayList<Condition> arrayList, Context context){
        Gson gson = new Gson();
        String data = gson.toJson(arrayList);

        this.mModelSavedSearch.requireUpdateDataSavedSearch(data, context);
    }

    public void handleSaveLastSearch(Condition condition, Context context){
        Gson gson = new Gson();
        String data = gson.toJson(condition);

        this.mModelSavedSearch.requireUpdateDataLastSearch(data, context);
    }

    @Override
    public void onGetDataSavedSearchSuccessful(String data) {
        ArrayList<Condition> arrayList = new ArrayList<>();

        Gson gson = new Gson();
        if(!data.isEmpty()){
            Type type = new TypeToken<ArrayList<Condition>>(){}.getType();

            arrayList = gson.fromJson(data, type);
        }
        this.mViewHandleSavedSearch.onHandleDataSavedSearchSuccessful(arrayList);
    }

    @Override
    public void onGetDataLastSearchSuccessful(String data) {
        Condition condition = new Condition();

        Gson gson = new Gson();
        if(!data.isEmpty()){
            Type type = new TypeToken<Condition>(){}.getType();
            condition = gson.fromJson(data, type);
        }

        this.mViewHandleSavedSearch.onHandleDataLastSearchSuccessful(condition);
    }


    @Override
    public void onUpdateDataSavedSearchSuccessful() {
        this.mViewHandleSavedSearch.onHandleUpdateSavedSearchListSuccessful();
    }
}
