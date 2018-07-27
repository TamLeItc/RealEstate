package com.qtctek.realstate.view.user_control.posted_post;

;
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
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.posted_post.PresenterPostedPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;

public class PostedPostFragment extends Fragment implements ViewHandlePostedPost ,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AlertHelper.AlertHelperCallback,
        ManagementFilterCallback{

    private View mView;

    private ListView mLsvPostedPost;
    private TextView mTxvInformation;
    private RelativeLayout mRlPostItem;

    private AdapterPostSale mAdapterListPost;
    private ArrayList<Product> mArrProduct = new ArrayList<>();

    private PresenterPostedPost mPresenterPostedPost;

    private int mPositionClick;
    private boolean mIsFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_posted_post_saved_post, container, false);

        ((UserControlActivity)getActivity()).postFilterCallback = this;

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        handleStart();
    }

    private void initViews(){
        this.mLsvPostedPost = mView.findViewById(R.id.lsv_posts);
        this.mTxvInformation = mView.findViewById(R.id.txv_information);

        this.mLsvPostedPost.setOnScrollListener(this);
        this.mLsvPostedPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        ((UserControlActivity)getActivity()).dialogHelper.show();

        this.mPresenterPostedPost = new PresenterPostedPost(this);
        this.mPresenterPostedPost.handleGetListPostedPost(0, 20, MainActivity.USER.getEmail(),
                ((UserControlActivity)getActivity()).productFormality, ((UserControlActivity)getActivity()).productStatus);

        this.mAdapterListPost = new AdapterPostSale(mArrProduct, getActivity(), R.layout.item_post);
        this.mLsvPostedPost.setAdapter(this.mAdapterListPost);

    }


    @Override
    public void onHandlePostListSuccessful(ArrayList<Product> arrListPost) {

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        if(mIsFirstLoad){
            this.mArrProduct.clear();
            mIsFirstLoad = false;
        }

        this.mArrProduct.addAll(arrListPost);
        this.mAdapterListPost.notifyDataSetChanged();

        if(mArrProduct.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
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

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Tải dữ liệu thất bại", ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onDeletePostSuccessful() {

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();
        ((UserControlActivity)getActivity()).toastHelper.toast("Xóa bài thành công", ToastHelper.LENGTH_SHORT);
        mArrProduct.remove(mPositionClick);
        mAdapterListPost.notifyDataSetChanged();

        if(mArrProduct.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDeletePostError(String error) {


        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Xóa bài không thành công", ToastHelper.LENGTH_SHORT);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvPostedPost.getLastVisiblePosition() - mLsvPostedPost.getHeaderViewsCount() -
                mLsvPostedPost.getFooterViewsCount()) >= (mAdapterListPost.getCount() - 1)) {

            ((UserControlActivity)getActivity()).dialogHelper.show();
            this.mPresenterPostedPost.handleGetListPostedPost(this.mArrProduct.size(), 20, MainActivity.USER.getEmail(),
                    ((UserControlActivity)getActivity()).productFormality, ((UserControlActivity)getActivity()).productStatus);
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_posted_post, popupMenu.getMenu());

        mPositionClick = position;
        this.mRlPostItem = view.findViewById(R.id.rl_post_item);

        mRlPostItem.setBackground(getResources().getDrawable(R.drawable.custom_border_normal));

        ((UserControlActivity)getActivity()).alertHelper.setCallback(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.control_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("product_id", mArrProduct.get(mPositionClick).getId());
                        startActivity(intent);
                        break;
                    case R.id.control_edit_post:

                        ((UserControlActivity)getActivity()).alertHelper.alert("Xác nhận",
                                getActivity().getResources().getString(R.string.confirm_edit_product), false, "Xác nhận",
                                "Hủy bỏ", Constant.EDIT);
                        break;
                    case R.id.control_delete_post:
                        ((UserControlActivity)getActivity()).alertHelper.alert("Xác nhận",
                                "Bạn có chắc chắn xóa bài đăng này", false, "Xác nhận",
                                "Hủy bỏ", Constant.DELETE);
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
            intent1.putExtra("post_id", mArrProduct.get(mPositionClick).getId());
            startActivity(intent1);
            getActivity().finish();
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    @Override
    public void onFilter() {

        mIsFirstLoad = true;

        ((UserControlActivity)getActivity()).dialogHelper.show();
        this.mPresenterPostedPost.handleGetListPostedPost(0, 20, MainActivity.USER.getEmail(),
                ((UserControlActivity)getActivity()).productFormality, ((UserControlActivity)getActivity()).productStatus);
    }
}
