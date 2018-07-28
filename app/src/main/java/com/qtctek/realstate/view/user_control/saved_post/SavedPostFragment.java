package com.qtctek.realstate.view.user_control.saved_post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedPostFragment extends Fragment implements ViewHandleSavedPost,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private View mView;

    private ListView mLsvSavedPost;
    private TextView mTxvInformation;
    private RelativeLayout mRlPostItem;

    private AdapterPostSale mAdapterListPost;
    private ArrayList<Product> mArrListProduct = new ArrayList<>();

    private int mPositionClick;

    private PresenterSavedPost mPresenterSavedPost;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_posted_post_saved_post, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();

        handleStart();
    }

    private void initViews(){
        this.mLsvSavedPost = mView.findViewById(R.id.lsv_posts);
        this.mTxvInformation = mView.findViewById(R.id.txv_information);

        this.mLsvSavedPost.setOnScrollListener(this);
        this.mLsvSavedPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        ((UserControlActivity)getActivity()).dialogHelper.show();

        this.mPresenterSavedPost = new PresenterSavedPost(this);

        this.mPresenterSavedPost.handleGetSavedProductList(0, 20, getStrProductIdList(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID));

        this.mAdapterListPost = new AdapterPostSale(mArrListProduct, getActivity(), R.layout.item_post);
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
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("product_id", mArrListProduct.get(mPositionClick).getId());
                        startActivity(intent);
                        break;
                    case R.id.action_un_save:
                        ((UserControlActivity)getActivity()).dialogHelper.show();
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
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();
        this.mArrListProduct.remove(mPositionClick);
        mAdapterListPost.notifyDataSetChanged();

        ((UserControlActivity)getActivity()).toastHelper.toast("Bỏ lưu thành công", ToastHelper.LENGTH_SHORT);

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
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Bỏ lưu không thành công", ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onHandleSavedProductListSuccessful(ArrayList<Product> arrListProduct) {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();
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

        ((UserControlActivity)getActivity()).toastHelper.toast("Lỗi trong quá trình tải dữ liệu. Vui lòng thử lại sau!!!", ToastHelper.LENGTH_SHORT);
    }
}
