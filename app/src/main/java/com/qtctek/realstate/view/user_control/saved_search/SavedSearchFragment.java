package com.qtctek.realstate.view.user_control.saved_search;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Condition;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.save_search.PresenterSavedSearch;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.realstate.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;

public class SavedSearchFragment extends Fragment implements ViewHandleSavedSearch, AdapterView.OnItemClickListener {

    private View mView;

    private ListView mLsvSavedSearch;

    private SavedSearchAdapter mSavedSearchAdapter;
    private int mPositionClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_saved_search, container,false);

        initViews();

        return mView;
    }

    private void initViews(){
        this.mLsvSavedSearch = mView.findViewById(R.id.lsv_saved_search);

        this.mLsvSavedSearch.setOnItemClickListener(this);

        mSavedSearchAdapter = new SavedSearchAdapter(getContext(), ListPostNewsFragment.LIST_SAVED_SEARCH);
        this.mLsvSavedSearch.setAdapter(mSavedSearchAdapter);
    }

    @Override
    public void onHandleDataSavedSearchSuccessful(ArrayList<Condition> lists) {

    }

    @Override
    public void onHandleDataSavedSearchError(String error) {

    }

    @Override
    public void onHandleUpdateSavedSearchListSuccessful() {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Xóa tìm kiếm thành công", ToastHelper.LENGTH_SHORT);
        ListPostNewsFragment.LIST_SAVED_SEARCH.remove(mPositionClick);

        this.mSavedSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHandleUpdateSavedSearchListError(String error) {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();
        ((UserControlActivity)getActivity()).toastHelper.toast("Xóa tìm kiếm không thành công", ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_saved_search, popupMenu.getMenu());

        mPositionClick = position;

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.control_view_detail:
                        MapPostNewsFragment.ON_EVENT_FROM_ACTIVITY.onHandleSearch(ListPostNewsFragment.LIST_SAVED_SEARCH.get(position));
                        getActivity().finish();
                        break;
                    case R.id.control_un_save:
                        ((UserControlActivity)getActivity()).dialogHelper.show();
                        handleUpdateSavedSearch();
                        break;

                }

                return true;
            }
        });
        popupMenu.show();
    }

    private void handleUpdateSavedSearch(){
        new PresenterSavedSearch(this).handleUpdateSavedSearchList(ListPostNewsFragment.LIST_SAVED_SEARCH, getContext());
    }
}