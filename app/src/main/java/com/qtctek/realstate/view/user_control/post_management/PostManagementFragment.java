package com.qtctek.realstate.view.user_control.post_management;

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

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.post_management.PresenterPostManagement;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;

public class PostManagementFragment extends Fragment implements ViewHandlePostManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AlertHelper.AlertHelperCallback {

    private View mView;

    private ListView mLsvPost;

    private AdapterPostSale mAdapterListPostForAdmin;
    private ArrayList<Product> mArrProduct = new ArrayList<>();

    private PresenterPostManagement mPresenterPostManagement;

    private int mPositionClick;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_post_management, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        handleStart();
    }

    private void initViews(){
        this.mLsvPost = mView.findViewById(R.id.lsv_posts);

        this.mLsvPost.setOnScrollListener(this);
        this.mLsvPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        ((UserControlActivity)getActivity()).dialogHelper.show();

        this.mPresenterPostManagement = new PresenterPostManagement(this);
        this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20);

        this.mAdapterListPostForAdmin = new AdapterPostSale(mArrProduct, getActivity());
        this.mLsvPost.setAdapter(this.mAdapterListPostForAdmin);
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvPost.getLastVisiblePosition() - mLsvPost.getHeaderViewsCount() -
                mLsvPost.getFooterViewsCount()) >= (mAdapterListPostForAdmin.getCount() - 1)) {

            ((UserControlActivity)getActivity()).dialogHelper.show();
            this.mPresenterPostManagement.handleGetPostListForAdmin(this.mArrProduct.size(), 20);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onHandlePostListSuccessful(ArrayList<Product> mArrProduct) {

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        this.mArrProduct.addAll(mArrProduct);

        this.mAdapterListPostForAdmin.notifyDataSetChanged();
    }

    @Override
    public void onHandlePostListError(String error) {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Đọc dữ liệu thất bại. Vui lòng thử lại sau", ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onAcceptPostSuccessful() {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Duyệt bài thành công", ToastHelper.LENGTH_SHORT);

        mArrProduct.get(mPositionClick).setStatus("3");
        mAdapterListPostForAdmin.notifyDataSetChanged();
    }

    @Override
    public void onAcceptPostError(String error) {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Duyệt bài không thành công", ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onDeletePostSuccessful() {

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();
        ((UserControlActivity)getActivity()).toastHelper.toast("Xóa bài thành công", ToastHelper.LENGTH_SHORT);

        mArrProduct.remove(mPositionClick);
        mAdapterListPostForAdmin.notifyDataSetChanged();

    }

    @Override
    public void onDeletePostError(String error) {

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Xóa bài không thành công", ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_post_management, popupMenu.getMenu());

        mPositionClick = position;
        if(mArrProduct.get(mPositionClick).getStatus().equals("3")){
            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.control_accept_post);
            menuItem.setVisible(false);
        }

        ((UserControlActivity)getActivity()).alertHelper.setCallback(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.control_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("product_id", mArrProduct.get(mPositionClick).getId());
                        startActivity(intent);
                        break;
                    case R.id.control_accept_post:
                        ((UserControlActivity)getActivity()).alertHelper.alert("Xác nhận",
                                "Bạn chắc chắc muốn duyệt bài đăng này", false,
                                "Xác nhận", "Hủy bỏ", Constant.ACCEPT);
                        break;
                    case R.id.control_delete_post:
                        ((UserControlActivity)getActivity()).alertHelper.alert("Xác nhận",
                                "Bạn chắc chắc muốn duyệt bài đăng này", false,
                                "Xác nhận", "Hủy bỏ", Constant.DELETE);
                        break;

                }

                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.ACCEPT){
            ((UserControlActivity)getActivity()).dialogHelper.dismiss();
            mPresenterPostManagement.handleUpdateAcceptPost(mArrProduct.get(mPositionClick).getId());
        }
        else if(option == Constant.DELETE){
            ((UserControlActivity)getActivity()).dialogHelper.dismiss();
            mPresenterPostManagement.handleDeletePost(mArrProduct.get(mPositionClick).getId());
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
