package com.qtctek.realstate.view.user_control.post_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.post_management.PresenterPostManagement;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;

public class PostManagementFragment extends Fragment implements ViewHandlePostManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        AlertHelper.AlertHelperCallback, ManagementFilterCallback, View.OnClickListener {

    private UserControlActivity mActivity;

    private View mView;

    private ListView mLsvPost;
    private TextView mTxvInformation;
    private RelativeLayout mRlPostItem;
    private ImageView mImvUp;

    private AdapterPostSale mAdapterListPostForAdmin;
    private ArrayList<Product> mArrProduct = new ArrayList<>();

    private PresenterPostManagement mPresenterPostManagement;

    private int mPositionClick;
    private boolean mIsFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_post_management, container, false);

        this.mActivity = (UserControlActivity) getActivity();
        initViews();
        handleStart();

        mActivity.postFilterCallback = this;

        return mView;
    }

    private void initViews(){
        this.mLsvPost = mView.findViewById(R.id.lsv_posts);
        this.mTxvInformation = mView.findViewById(R.id.txv_information);
        this.mImvUp = mView.findViewById(R.id.imv_up);

        this.mLsvPost.setOnScrollListener(this);
        this.mLsvPost.setOnItemClickListener(this);
        this.mImvUp.setOnClickListener(this);
    }

    private void handleStart(){
        mActivity.getDialogHelper().show();

        this.mPresenterPostManagement = new PresenterPostManagement(this);
        this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20, mActivity.productFormality,
                mActivity.productStatus);

        this.mAdapterListPostForAdmin = new AdapterPostSale(mArrProduct, getActivity());
        this.mLsvPost.setAdapter(this.mAdapterListPostForAdmin);
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvPost.getLastVisiblePosition() - mLsvPost.getHeaderViewsCount() -
                mLsvPost.getFooterViewsCount()) >= (mAdapterListPostForAdmin.getCount() - 1)) {

            mActivity.getDialogHelper().show();
            this.mPresenterPostManagement.handleGetPostListForAdmin(this.mArrProduct.size(), 20,
                    mActivity.productFormality, mActivity.productStatus);
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
    public void onHandlePostListSuccessful(ArrayList<Product> arrProduct) {

        mActivity.getDialogHelper().dismiss();

        if(mIsFirstLoad){
            this.mArrProduct.clear();
            mIsFirstLoad = false;
        }

        this.mArrProduct.addAll(arrProduct);

        this.mAdapterListPostForAdmin.notifyDataSetChanged();

        if(mArrProduct.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);

            if(mActivity.isFilter){
                mLsvPost.smoothScrollToPosition(0);
                mActivity.isFilter = false;
            }
        }
        else{
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHandlePostListError(String error) {

        this.mTxvInformation.setText(getResources().getString(R.string.load_data_error));
        this.mTxvInformation.setVisibility(View.VISIBLE);

        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(getResources().getString(R.string.error_read_data), ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onAcceptPostSuccessful() {
        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(getResources().getString(R.string.accept_post_successful), ToastHelper.LENGTH_SHORT);

        mArrProduct.get(mPositionClick).setStatus("3");
        mAdapterListPostForAdmin.notifyDataSetChanged();
    }

    @Override
    public void onAcceptPostError(String error) {
        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(getResources().getString(R.string.accept_post_failed), ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onDeletePostSuccessful() {

        mActivity.getDialogHelper().dismiss();
        mActivity.getToastHelper().toast(getResources().getString(R.string.delete_successful), ToastHelper.LENGTH_SHORT);

        mArrProduct.remove(mPositionClick);
        mAdapterListPostForAdmin.notifyDataSetChanged();

        if(mArrProduct.size() == 0){
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeletePostError(String error) {

        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(R.string.delete_failed, ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_post_management, popupMenu.getMenu());

        mPositionClick = position;
        this.mRlPostItem = view.findViewById(R.id.rl_post_item);

        mRlPostItem.setBackground(getResources().getDrawable(R.drawable.custom_border_normal));

        if(mArrProduct.get(mPositionClick).getStatus().equals("3")){
            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.action_accept_post);
            menuItem.setVisible(false);
        }

        mActivity.getAlertHelper().setCallback(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(Product.ID, mArrProduct.get(mPositionClick).getId());
                        startActivity(intent);
                        break;
                    case R.id.action_accept_post:
                        mActivity.getAlertHelper().alert(getResources().getString(R.string.delete_post),
                                getResources().getString(R.string.delete_post_notificaton), false,
                                getResources().getString(R.string.ok), getResources().getString(R.string.cancel), Constant.ACCEPT);
                        break;
                    case R.id.action_delete_post:
                        mActivity.getAlertHelper().alert(getResources().getString(R.string.accept_post),
                                getResources().getString(R.string.accept_post_notification), false,
                                getResources().getString(R.string.ok), getResources().getString(R.string.cancel), Constant.DELETE);
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

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.ACCEPT){
            mActivity.getDialogHelper().dismiss();
            mPresenterPostManagement.handleUpdateAcceptPost(mArrProduct.get(mPositionClick).getId());
        }
        else if(option == Constant.DELETE){
            mActivity.getDialogHelper().dismiss();
            mPresenterPostManagement.handleDeletePost(mArrProduct.get(mPositionClick).getId());
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    @Override
    public void onFilter() {

        mIsFirstLoad = true;

        mActivity.getDialogHelper().show();
        this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20,
                mActivity.productFormality, mActivity.productStatus);
    }

    @Override
    public void onClick(View v) {
        this.mLsvPost.smoothScrollToPosition(0);
    }
}
