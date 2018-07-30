package com.qtctek.realstate.view.user_control.saved_search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Condition;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.save_search.PresenterSavedSearch;
import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.realstate.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;

public class SavedSearchFragment extends Fragment implements ViewHandleSavedSearch, AdapterView.OnItemClickListener, View.OnClickListener {

    private View mView;

    private ListView mLsvSavedSearch;
    private TextView mTxvInformation;
    private RelativeLayout mRlSearchItem;

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
        this.mTxvInformation = mView.findViewById(R.id.txv_information);

        this.mLsvSavedSearch.setOnItemClickListener(this);

        mSavedSearchAdapter = new SavedSearchAdapter(getContext(), ListPostNewsFragment.LIST_SAVED_SEARCH);
        this.mLsvSavedSearch.setAdapter(mSavedSearchAdapter);

        if(ListPostNewsFragment.LIST_SAVED_SEARCH.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setVisibility(View.VISIBLE);
        }
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

        if(ListPostNewsFragment.LIST_SAVED_SEARCH.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setVisibility(View.VISIBLE);
        }

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
        this.mRlSearchItem = view.findViewById(R.id.rl_item_1);

        mRlSearchItem.setBackground(getResources().getDrawable(R.drawable.custom_border_normal));

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_view_detail:
                        MapPostNewsFragment.ON_EVENT_FROM_ACTIVITY.onHandleSearch(ListPostNewsFragment.LIST_SAVED_SEARCH.get(position));
                        getActivity().finish();
                        break;
                    case R.id.action_un_save:
                        ((UserControlActivity)getActivity()).dialogHelper.show();
                        handleUpdateSavedSearch();
                        break;

                }

                return true;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                mRlSearchItem.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
        });

        popupMenu.show();
    }

    private void handleUpdateSavedSearch(){
        new PresenterSavedSearch(this).handleUpdateSavedSearchList(ListPostNewsFragment.LIST_SAVED_SEARCH, getContext());
    }

    @Override
    public void onClick(View v) {
    }
}
