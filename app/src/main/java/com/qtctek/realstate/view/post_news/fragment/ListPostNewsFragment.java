package com.qtctek.realstate.view.post_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.realstate.view.post_news.adapter.PostAdapter;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.user_control.saved_post.ViewGetDataLocal;

import java.util.HashMap;

public class ListPostNewsFragment extends Fragment implements AdapterView.OnItemClickListener,
        ViewGetDataLocal {

    private View mView;

    private ListView mLsvListProduct;

    public static TextView TXV_INFORMATION;

    public static PostAdapter POST_ADAPTER;

    public static HashMap<String, String> LIST_SAVED_PRODUCT_ID;

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

        this.mLsvListProduct.setOnItemClickListener(this);
    }

    private void handleStart(){
        POST_ADAPTER = new PostAdapter(getContext(), MapPostNewsFragment.ARR_POST);
        this.mLsvListProduct.setAdapter(POST_ADAPTER);

        LIST_SAVED_PRODUCT_ID = new HashMap<>();
        new PresenterSavedPost(this).handleGetDataProductIds(getContext());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("post_id", MapPostNewsFragment.ARR_POST.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onHandleDataProductIdsSuccessful(HashMap<String, String> list) {
        LIST_SAVED_PRODUCT_ID.putAll(list);
    }

    @Override
    public void onHandleDataProductIdsError(String error) {

    }
}
