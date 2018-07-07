package com.qtctek.realstate.view.user_control.saved_post;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedPostFragment extends Fragment implements ViewHandleSavedPost,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private View mView;

    private ListView mLsvSavedPost;


    private Dialog mLoadingDialog;

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
        createLoadingDialog();
        handleStart();
    }

    private void initViews(){
        this.mLsvSavedPost = mView.findViewById(R.id.lsv_posts);

        this.mLsvSavedPost.setOnScrollListener(this);
        this.mLsvSavedPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        this.mLoadingDialog.show();

        this.mPresenterSavedPost = new PresenterSavedPost(this);
        this.mPresenterSavedPost.handleGetSavedProductList(0, 20, getStrProductIdList(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID));

        this.mAdapterListPost = new AdapterPostSale(mArrListProduct, getActivity(), R.layout.item_post);
        this.mLsvSavedPost.setAdapter(this.mAdapterListPost);
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
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
        popupMenu.getMenuInflater().inflate(R.menu.poup_menu_for_saved_post, popupMenu.getMenu());

        mPositionClick = position;

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.control_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("post_id", mArrListProduct.get(mPositionClick).getId());
                        startActivity(intent);
                        break;
                    case R.id.control_un_save:
                        mLoadingDialog.show();
                        String id = mArrListProduct.get(mPositionClick).getId() + "";
                        ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.remove(id);
                        mPresenterSavedPost.handleUpdateDataProductIds(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID, getContext());
                        break;

                }

                return true;
            }
        });
        popupMenu.show();
    }

    public static String getStrProductIdList(HashMap<String, String> hashMap){
        String data = "";
        boolean isFirst = true;
        for(String item : hashMap.keySet()){
            if(isFirst){
                data += item;
            }
            else{
                data += "," + item;
            }
        }
        return data;
    }

    @Override
    public void onHandleUpdateProductIdListSuccessful() {
        mLoadingDialog.dismiss();
        this.mArrListProduct.remove(mPositionClick);
        mAdapterListPost.notifyDataSetChanged();
        Toast.makeText(getContext(), "Bỏ thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHandleUpdateProductIdListError(String e) {
        mLoadingDialog.dismiss();
        Toast.makeText(getContext(), "Lỗi trong quá trình lưu dữ liệu. Vui lòng thử lại sau!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHandleSavedProductListSuccessful(ArrayList<Product> mArrListProduct) {
        mLoadingDialog.dismiss();
        this.mArrListProduct.addAll(mArrListProduct);
        this.mAdapterListPost.notifyDataSetChanged();
    }

    @Override
    public void onHandleSavedProductListError(String error) {
        Toast.makeText(getContext(), "Lỗi trong quá trình tải dữ liệu. Vui lòng thử lại sau!!!", Toast.LENGTH_SHORT).show();
    }
}
