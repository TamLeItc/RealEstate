package com.qtctek.aladin.view.user_control.posted_post;

;
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
import com.qtctek.aladin.presenter.user_control.posted_post.PresenterPostedPost;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;
import com.qtctek.aladin.view.post_detail.activity.PostDetailActivity;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;
import com.qtctek.aladin.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;

public class PostedPostFragment extends Fragment implements ViewHandlePostedPost ,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AlertHelper.AlertHelperCallback,
        ManagementFilterCallback, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UserControlActivity mActivity;

    private View mView;

    private ListView mLsvPostedPost;
    private TextView mTxvInformation;
    private RelativeLayout mRlPostItem;
    private ImageView mImvUp;
    private SwipeRefreshLayout mSWLPosts;

    private AdapterPostSale mAdapterListPost;
    private ArrayList<Product> mArrProduct = new ArrayList<>();

    private PresenterPostedPost mPresenterPostedPost;

    private int mPositionClick;
    private boolean mIsFirstLoadList = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_posted_post_saved_post, container, false);

        this.mActivity = (UserControlActivity)getActivity();
        initViews();
        handleStart();

        mActivity.postFilterCallback = this;

        return mView;
    }

    private void initViews(){
        this.mLsvPostedPost = mView.findViewById(R.id.lsv_posts);
        this.mTxvInformation = mView.findViewById(R.id.txv_information);
        this.mImvUp = mView.findViewById(R.id.imv_up);
        this.mSWLPosts = mView.findViewById(R.id.srl_posts);

        this.mLsvPostedPost.setOnScrollListener(this);
        this.mLsvPostedPost.setOnItemClickListener(this);
        this.mImvUp.setOnClickListener(this);
        this.mSWLPosts.setOnRefreshListener(this);
    }

    private void handleStart(){

        mActivity.getDialogHelper().show();

        this.mPresenterPostedPost = new PresenterPostedPost(this);
        this.mPresenterPostedPost.handleGetListPostedPost(0, 20, MainActivity.USER.getEmail(),
                mActivity.productFormality, mActivity.productStatus);

        this.mAdapterListPost = new AdapterPostSale(mArrProduct, getActivity(), R.layout.item_post);
        this.mLsvPostedPost.setAdapter(this.mAdapterListPost);

    }


    @Override
    public void onHandlePostListSuccessful(ArrayList<Product> arrListPost) {

        mActivity.getDialogHelper().dismiss();
        mSWLPosts.setRefreshing(false);

        if(mIsFirstLoadList){
            mArrProduct.clear();
            mIsFirstLoadList = false;
        }
        else{
            if(arrListPost.size() == 0){
                return;
            }
        }

        addList(arrListPost);
        this.mAdapterListPost.notifyDataSetChanged();

        if(mArrProduct.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);

            if(mActivity.isFilter){
                mLsvPostedPost.smoothScrollToPosition(0);
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

        mActivity.getToastHelper().toast(R.string.load_data_error, ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onDeletePostSuccessful() {

        mActivity.getDialogHelper().dismiss();
        mActivity.getToastHelper().toast(R.string.delete_successful, ToastHelper.LENGTH_SHORT);
        mArrProduct.remove(mPositionClick);
        mAdapterListPost.notifyDataSetChanged();

        if(mArrProduct.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvPostedPost.getLastVisiblePosition() - mLsvPostedPost.getHeaderViewsCount() -
                mLsvPostedPost.getFooterViewsCount()) >= (mAdapterListPost.getCount() - 1)) {

            mActivity.getDialogHelper().show();
            this.mPresenterPostedPost.handleGetListPostedPost(this.mArrProduct.size(), 20, MainActivity.USER.getEmail(),
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_posted_post, popupMenu.getMenu());

        mPositionClick = position;
        this.mRlPostItem = view.findViewById(R.id.rl_post_item);

        mRlPostItem.setBackground(getResources().getDrawable(R.drawable.custom_border_normal));

        mActivity.getAlertHelper().setCallback(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(Product.ID, mArrProduct.get(mPositionClick).getId());
                        intent.putExtra(Constant.ACTIVITY, UserControlActivity.ACTIVITY);
                        startActivity(intent);
                        break;
                    case R.id.action_edit_post:

                        mActivity.getAlertHelper().alert(R.string.information,
                               R.string.confirm_edit_product, false,
                                R.string.ok, R.string.cancel, Constant.EDIT);
                        break;
                    case R.id.action_delete_post:
                        mActivity.getAlertHelper().alert(R.string.delete_post, R.string.delete_post_notificaton,
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
        if(option == Constant.DELETE){
            mPresenterPostedPost.handleDeletePost(mArrProduct.get(mPositionClick).getId());
        }
        else if(option == Constant.EDIT){
            Intent intent1 = new Intent(getActivity(), NewPostActivity.class);
            intent1.putExtra(Product.ID, mArrProduct.get(mPositionClick).getId());
            startActivity(intent1);
            getActivity().finish();
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    @Override
    public void onFilter() {

        mIsFirstLoadList = true;

        mActivity.getDialogHelper().show();
        this.mPresenterPostedPost.handleGetListPostedPost(0, 20, MainActivity.USER.getEmail(),
                mActivity.productFormality, mActivity.productStatus);
    }

    @Override
    public void onClick(View v) {
        mLsvPostedPost.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {

        mIsFirstLoadList = true;
        mActivity.productFormality = "%";
        mActivity.productStatus = "%";

        this.mPresenterPostedPost.handleGetListPostedPost(0, 20, MainActivity.USER.getEmail(),
                mActivity.productFormality, mActivity.productStatus);
    }
}
