package com.qtctek.aladin.view.user_control.post_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.user_control.post_management.PresenterPostManagement;
import com.qtctek.aladin.view.post_detail.activity.PostDetailActivity;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;
import com.qtctek.aladin.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;

public class PostManagementFragment extends Fragment implements ViewHandlePostManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        AlertHelper.AlertHelperCallback, ManagementFilterCallback, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UserControlActivity mActivity;

    private View mView;

    private ListView mLsvPost;
    private TextView mTxvInformation;
    private RelativeLayout mRlPostItem;
    private ImageView mImvUp;
    private SwipeRefreshLayout mSRLPosts;

    private AdapterPostSale mAdapterListPostForAdmin;
    private ArrayList<Product> mArrProduct = new ArrayList<>();

    private PresenterPostManagement mPresenterPostManagement;

    private int mPositionClick;
    private boolean mIsFirstLoadList = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
        this.mSRLPosts = mView.findViewById(R.id.srl_posts);

        this.mLsvPost.setOnScrollListener(this);
        this.mLsvPost.setOnItemClickListener(this);
        this.mImvUp.setOnClickListener(this);
        this.mSRLPosts.setOnRefreshListener(this);
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
        mSRLPosts.setRefreshing(false);

        if(mIsFirstLoadList){
            mArrProduct.clear();
            mIsFirstLoadList = false;
        }
        else if(arrProduct.size() == 0){
            return;
        }

        addList(arrProduct);

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

    /*
    * Dữ liệu trên server có thể thay đổi khiến limit và start có thể sẽ lấy về dữ liệu đã có trong danh sách
    * Phương thức này đảm bảo không có 2 đối tượng giống nhau nào trong danh sách
    */
    private void addList(ArrayList<Product> arrProduct){
        int size = this.mArrProduct.size();
        for(int i = 0; i < arrProduct.size(); i++){
            boolean isExisted = false;
            for(int j = 0; j < size; j++){
                if(arrProduct.get(i).getId() == this.mArrProduct.get(j).getId()){
                    isExisted = true;
                    break;
                }
            }
            if(!isExisted){
                this.mArrProduct.add(arrProduct.get(i));
                arrProduct.remove(i);
                i--;
            }
        }
    }

    @Override
    public void onHandlePostListError(String error) {

        this.mTxvInformation.setText(getResources().getString(R.string.load_data_error));
        this.mTxvInformation.setVisibility(View.VISIBLE);

        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(R.string.error_read_data, ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onAcceptPostSuccessful() {
        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(R.string.accept_post_successful, ToastHelper.LENGTH_SHORT);

        mArrProduct.get(mPositionClick).setStatus("3");
        mAdapterListPostForAdmin.notifyDataSetChanged();
    }

    @Override
    public void onAcceptPostError(String error) {
        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(R.string.accept_post_failed, ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onDeletePostSuccessful() {

        mActivity.getDialogHelper().dismiss();
        mActivity.getToastHelper().toast(R.string.delete_successful, ToastHelper.LENGTH_SHORT);

        mArrProduct.remove(mPositionClick);
        mAdapterListPostForAdmin.notifyDataSetChanged();

        if(mArrProduct.size() == 0){
            this.mTxvInformation.setText(R.string.no_data);
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
                        intent.putExtra(Constant.ACTIVITY, UserControlActivity.ACTIVITY);
                        startActivity(intent);
                        break;
                    case R.id.action_accept_post:
                        mActivity.getAlertHelper().alert(R.string.delete_post, R.string.delete_post_notificaton,
                                false, R.string.ok, R.string.cancel, Constant.ACCEPT);
                        break;
                    case R.id.action_delete_post:
                        mActivity.getAlertHelper().alert(R.string.accept_post,R.string.accept_post_notification,
                                false, R.string.ok, R.string.cancel, Constant.DELETE);
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

        mIsFirstLoadList = true;

        mActivity.getDialogHelper().show();
        this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20,
                mActivity.productFormality, mActivity.productStatus);
    }

    @Override
    public void onClick(View v) {
        this.mLsvPost.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {

        mActivity.productFormality = "%";
        mActivity.productStatus = "%";

        mIsFirstLoadList = true;

        this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20,
                mActivity.productFormality, mActivity.productStatus);
    }
}
