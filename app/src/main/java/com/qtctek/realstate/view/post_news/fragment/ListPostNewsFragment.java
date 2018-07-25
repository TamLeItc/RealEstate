package com.qtctek.realstate.view.post_news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Condition;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_news.adapter.PostAdapter;
import com.qtctek.realstate.view.user_control.saved_post.ViewHandleSavedPost;

import java.util.ArrayList;
import java.util.HashMap;

public class ListPostNewsFragment extends Fragment implements ViewHandleSavedPost {

    private View mView;

    public ListView mLsvListProduct;

    public static TextView TXV_INFORMATION;

    public static PostAdapter POST_ADAPTER;

    public static HashMap<String, String> LIST_SAVED_PRODUCT_ID;
    public static ArrayList<Condition> LIST_SAVED_SEARCH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_post_news, container, false);

        initViews();
        handleStart();

        return mView;
    }

    private void initViews(){
        this.mLsvListProduct = mView.findViewById(R.id.lsv_list_product);
        TXV_INFORMATION = mView.findViewById(R.id.txv_information);
    }

    private void handleStart(){

        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) ((MainActivity)getActivity()).getSupportFragmentManager().getFragments().get(0);

        POST_ADAPTER = new PostAdapter(getContext(), mapPostNewsFragment.arrProduct, mapPostNewsFragment);
        this.mLsvListProduct.setAdapter(POST_ADAPTER);

        LIST_SAVED_PRODUCT_ID = new HashMap<>();
        new PresenterSavedPost(this).handleGetDataProductIds(getContext());
    }


    @Override
    public void onHandleDataProductIdsSuccessful(HashMap<String, String> list) {
        LIST_SAVED_PRODUCT_ID.putAll(list);
    }

    @Override
    public void onHandleDataProductIdsError(String error) {
    }

    @Override
    public void onHandleUpdateProductIdListSuccessful() {

    }

    @Override
    public void onHandleUpdateProductIdListError(String e) {

    }

    @Override
    public void onHandleSavedProductListSuccessful(ArrayList<Product> mArrListProduct) {

    }

    @Override
    public void onHandleSavedProductListError(String error) {

    }
}
