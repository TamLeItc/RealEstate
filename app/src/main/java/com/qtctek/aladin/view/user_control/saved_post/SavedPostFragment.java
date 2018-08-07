package com.qtctek.aladin.view.user_control.saved_post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.Product;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.aladin.view.post_detail.activity.PostDetailActivity;
import com.qtctek.aladin.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedPostFragment extends Fragment implements ViewHandleSavedPost,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UserControlActivity mActivity;

    private View mView;

    private ListView mLsvSavedPost;
    private TextView mTxvInformation;
    private RelativeLayout mRlPostItem;
    private ImageView mImvUp;
    private SwipeRefreshLayout mSRLPost;

    private AdapterPostSale mAdapterListPost;
    private ArrayList<Product> mArrListProduct = new ArrayList<>();

    private PresenterSavedPost mPresenterSavedPost;

    private int mPositionClick;
    private boolean mIsFirstLoadList = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_posted_post_saved_post, container, false);

        this.mActivity = (UserControlActivity)getActivity();
        initViews();
        handleStart();

        return mView;
    }

    private void initViews(){
        this.mLsvSavedPost = mView.findViewById(R.id.lsv_posts);
        this.mTxvInformation = mView.findViewById(R.id.txv_information);
        this.mImvUp = mView.findViewById(R.id.imv_up);
        this.mSRLPost = mView.findViewById(R.id.srl_posts);

        this.mLsvSavedPost.setOnScrollListener(this);
        this.mLsvSavedPost.setOnItemClickListener(this);
        this.mImvUp.setOnClickListener(this);
        this.mSRLPost.setOnRefreshListener(this);
    }

    private void handleStart(){

        mActivity.getDialogHelper().show();

        this.mPresenterSavedPost = new PresenterSavedPost(this);

        this.mPresenterSavedPost.handleGetSavedProductList(0, 20, getStrProductIdList(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID));

        this.mAdapterListPost = new AdapterPostSale(mArrListProduct, mActivity, R.layout.item_post);
        this.mLsvSavedPost.setAdapter(this.mAdapterListPost);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvSavedPost.getLastVisiblePosition() - mLsvSavedPost.getHeaderViewsCount() -
                mLsvSavedPost.getFooterViewsCount()) >= (mAdapterListPost.getCount() - 1)) {

            this.mPresenterSavedPost.handleGetSavedProductList(mArrListProduct.size(), 20, getStrProductIdList(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID));
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem > 5){
            this.mImvUp.setVisibility(View.VISIBLE);
        }
        else{
            this.mImvUp.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_saved_post, popupMenu.getMenu());

        mPositionClick = position;

        this.mRlPostItem = view.findViewById(R.id.rl_post_item);
        mRlPostItem.setBackground(getResources().getDrawable(R.drawable.custom_border_normal));


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_view_detail:
                        Intent intent = new Intent(mActivity, PostDetailActivity.class);
                        intent.putExtra(Product.ID, mArrListProduct.get(mPositionClick).getId());
                        intent.putExtra(Constant.ACTIVITY, UserControlActivity.ACTIVITY);
                        startActivity(intent);
                        break;
                    case R.id.action_un_save:
                        mActivity.getDialogHelper().show();
                        String id = mArrListProduct.get(mPositionClick).getId() + "";
                        ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.remove(id);
                        mPresenterSavedPost.handleUpdateDataProductIds(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID, getContext());
                        break;
                }

                return true;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                mRlPostItem.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
        });

        popupMenu.show();
    }
    public static String getStrProductIdList(HashMap<String, String> hashMap){
        String data = "";
        for(String key : hashMap.keySet()){
            if(data.equals("")){
                data += key;
            }
            else{
                data += "," + key;
            }
        }
        return data;
    }

    @Override
    public void onHandleDataProductIdsSuccessful(HashMap<String, String> list) {

    }

    @Override
    public void onHandleDataProductIdsError(String error) {

    }

    @Override
    public void onHandleUpdateProductIdListSuccessful() {

        mActivity.getDialogHelper().dismiss();
        this.mArrListProduct.remove(mPositionClick);

        mAdapterListPost.notifyDataSetChanged();

        mActivity.getToastHelper().toast(R.string.unsave_successful, ToastHelper.LENGTH_SHORT);

        if(mArrListProduct.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHandleUpdateProductIdListError(String e) {
        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(R.string.unsave_failed, ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onHandleSavedProductListSuccessful(ArrayList<Product> arrListProduct) {

        mActivity.getDialogHelper().dismiss();
        mSRLPost.setRefreshing(false);

        if(mIsFirstLoadList){
            mArrListProduct.clear();
            mIsFirstLoadList = false;
        }
        else if(arrListProduct.size() == 0){
            return;
        }

        this.mArrListProduct.addAll(arrListProduct);
        this.mAdapterListPost.notifyDataSetChanged();

        if(mArrListProduct.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHandleSavedProductListError(String error) {

        this.mTxvInformation.setText(getResources().getString(R.string.load_data_error));
        this.mTxvInformation.setVisibility(View.VISIBLE);

        mActivity.getToastHelper().toast(getResources().getString(R.string.load_data_error), ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        this.mLsvSavedPost.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {

        mIsFirstLoadList = true;
        this.mPresenterSavedPost.handleGetSavedProductList(0, 20, getStrProductIdList(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID));

    }
}
