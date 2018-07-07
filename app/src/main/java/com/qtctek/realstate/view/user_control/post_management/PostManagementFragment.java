package com.qtctek.realstate.view.user_control.post_management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.presenter.user_control.post_management.PresenterPostManagement;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;

import java.util.ArrayList;

public class PostManagementFragment extends Fragment implements ViewHandlePostManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private View mView;

    private ListView mLsvPost;

    private Dialog mLoadingDialog;

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
        createLoadingDialog();
        handleStart();
    }

    private void initViews(){
        this.mLsvPost = mView.findViewById(R.id.lsv_posts);

        this.mLsvPost.setOnScrollListener(this);
        this.mLsvPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        this.mLoadingDialog.dismiss();

        this.mPresenterPostManagement = new PresenterPostManagement(this);
        this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20);

        this.mAdapterListPostForAdmin = new AdapterPostSale(mArrProduct, getActivity());
        this.mLsvPost.setAdapter(this.mAdapterListPostForAdmin);
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    private void confirmAcceptPost(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Bạn có chắc duyệt bài đăng này!!!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLoadingDialog.show();
                mPresenterPostManagement.handleUpdateAcceptPost(mArrProduct.get(mPositionClick).getId());
            }
        });
        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    private void confirmDeletePost(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Bạn có chắc xóa bài đăng này!!!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLoadingDialog.show();
                mPresenterPostManagement.handleDeletePost(mArrProduct.get(mPositionClick).getId());
            }
        });
        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvPost.getLastVisiblePosition() - mLsvPost.getHeaderViewsCount() -
                mLsvPost.getFooterViewsCount()) >= (mAdapterListPostForAdmin.getCount() - 1)) {

            this.mLoadingDialog.show();
            this.mPresenterPostManagement.handleGetPostListForAdmin(this.mArrProduct.size(), 20);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onHandlePostListSuccessful(ArrayList<Product> mArrProduct) {

        this.mLoadingDialog.dismiss();

        this.mArrProduct.addAll(mArrProduct);

        this.mAdapterListPostForAdmin.notifyDataSetChanged();
    }

    @Override
    public void onHandlePostListError(String error) {
        this.mLoadingDialog.dismiss();

        Toast.makeText(getActivity(), "Đọc dữ liệu thất bại. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptPostSuccessful() {
        this.mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Duyệt bài thành công");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mArrProduct.clear();
                mArrProduct.get(mPositionClick).setStatus("3");
                mAdapterListPostForAdmin.notifyDataSetChanged();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onAcceptPostError(String error) {
        this.mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(error);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    @Override
    public void onDeletePostSuccessful() {

        this.mLoadingDialog.dismiss();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Xóa thành công!!!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mArrProduct.remove(mPositionClick);
                mAdapterListPostForAdmin.notifyDataSetChanged();
            }
        });
        alertDialog.show();


    }

    @Override
    public void onDeletePostError(String error) {

        this.mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Xóa thất bại. vui lòng kiểm tra lại!!!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_post_management, popupMenu.getMenu());

        mPositionClick = position;
        if(mArrProduct.get(mPositionClick).getStatus().equals("Đã đăng")){
            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.control_accept_post);
            menuItem.setVisible(false);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.control_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("post_id", mArrProduct.get(mPositionClick).getId());
                        startActivity(intent);
                        break;
                    case R.id.control_accept_post:
                        confirmAcceptPost();
                        break;
                    case R.id.control_delete_post:
                        confirmDeletePost();
                        break;

                }

                return true;
            }
        });
        popupMenu.show();
    }
}
