package com.qtctek.realstate.view.user_control.post_management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.presenter.user_control.post_management.PresenterPostManagement;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;

import java.util.ArrayList;

public class PostManagementFragment extends Fragment implements ViewHandlePostManagement,
        View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private View mView;

    private ListView mLsvPost;
    private Button mBtnMoreView;
    private TextView mTxvQualityPost;
    private CheckBox mChkOption;

    private Dialog mLoadingDialog;

    private AdapterPostSale mAdapterListPostForAdmin;
    private ArrayList<PostSale> mArrListPost = new ArrayList<>();

    private PresenterPostManagement mPresenterPostManagement;

    private int mQualityPost = 0;
    private int mPreLast = 0;
    private int mPostStatus = 0;    //if = 0. Display all post
    private int mPosition;



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
        this.mBtnMoreView = mView.findViewById(R.id.btn_more_view);
        this.mTxvQualityPost = mView.findViewById(R.id.txv_quality_post);
        this.mChkOption = mView.findViewById(R.id.chk_unapproved_post);

        this.mBtnMoreView.setOnClickListener(this);
        this.mLsvPost.setOnScrollListener(this);
        this.mChkOption.setOnClickListener(this);

        this.mLsvPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        this.mLoadingDialog.dismiss();

        this.mPresenterPostManagement = new PresenterPostManagement(this);
        this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20, mPostStatus);

        this.mAdapterListPostForAdmin = new AdapterPostSale(mArrListPost, getActivity());
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
                mPresenterPostManagement.handleUpdateAcceptPost(mArrListPost.get(mPosition).getId());
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
                mPresenterPostManagement.handleDeletePost(mArrListPost.get(mPosition).getId());
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_more_view:
                if(this.mQualityPost > this.mArrListPost.size()){
                    this.mPresenterPostManagement.handleGetPostListForAdmin(this.mArrListPost.size(), 20, mPostStatus);
                }
                this.mBtnMoreView.setVisibility(View.GONE);
                break;
            case R.id.chk_unapproved_post:
                if(mChkOption.isChecked()){
                    this.mPostStatus = 1;
                    this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20, mPostStatus);
                    this.mArrListPost.clear();
                }
                else{
                    this.mPostStatus = 0;
                    this.mPresenterPostManagement.handleGetPostListForAdmin(0, 20, mPostStatus);
                    this.mArrListPost.clear();
                }
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        switch (view.getId()){
            case R.id.lsv_posts:
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(this.mPreLast > lastItem){
                    this.mBtnMoreView.setVisibility(View.GONE);
                }
                else{
                    if(lastItem == totalItemCount){
                        if(this.mQualityPost > this.mArrListPost.size()){
                            this.mBtnMoreView.setVisibility(View.VISIBLE);
                        }
                    }
                    this.mPreLast = lastItem;
                }
        }
    }

    @Override
    public void onHandlePostListSuccessful(int qualityPost, ArrayList<PostSale> arrListPost) {

        this.mLoadingDialog.dismiss();

        this.mArrListPost.addAll(arrListPost);

        this.mQualityPost = qualityPost;
        String temp = "Hiển thị " + this.mArrListPost.size() + " tin. Tổng " + this.mQualityPost + " tin";
        this.mTxvQualityPost.setText(temp);

        this.mAdapterListPostForAdmin.notifyDataSetChanged();
    }

    @Override
    public void onHandlePostListError(String error) {

        this.mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Đọc dữ liệu thất bại. Vui lòng thử lại sau");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(0);
            }
        });
        alertDialog.show();
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
                mArrListPost.clear();
                if(mChkOption.isChecked()){
                    mPresenterPostManagement.handleGetPostListForAdmin(0, mQualityPost, 1);
                }
                else{
                    mPresenterPostManagement.handleGetPostListForAdmin(0, mQualityPost, 0);
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void onAcceptPostError(String error) {
        this.mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Duyệt bài thất bại. Vui lòng kiểm tra lại!!!");
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
                mArrListPost.clear();
                if(mChkOption.isChecked()){
                    mPresenterPostManagement.handleGetPostListForAdmin(0, mQualityPost - 1, 1);
                }
                else{
                    mPresenterPostManagement.handleGetPostListForAdmin(0, mQualityPost - 1, 0);
                }
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

        mPosition = position;
        if(mArrListPost.get(mPosition).getStatus().equals("Đã đăng")){
            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.control_accept_post);
            menuItem.setVisible(false);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.control_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("post_id", mArrListPost.get(mPosition).getId());
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
