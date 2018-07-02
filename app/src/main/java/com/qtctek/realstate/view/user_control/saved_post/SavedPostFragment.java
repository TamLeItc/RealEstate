package com.qtctek.realstate.view.user_control.saved_post;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;

import java.util.ArrayList;

public class SavedPostFragment extends Fragment implements ViewHandelSavedPost, View.OnClickListener,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private View mView;

    private ListView mLsvSavedPost;
    private Button mBtnMoreView;
    private TextView mTxvQualityPost;
    private TextView mTxvTitle;
    private CheckBox mChkUnapprovedPost;


    private Dialog mLoadingDialog;

    private AdapterPostSale mAdapterListPost;
    private ArrayList<PostSale> mArrListPost = new ArrayList<>();

    private int mQualityPost = 0;
    private int mPreLast = 0;
    private int mPosition;

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
        this.mBtnMoreView = mView.findViewById(R.id.btn_more_view);
        this.mTxvQualityPost = mView.findViewById(R.id.txv_quality_post);

        this.mBtnMoreView.setOnClickListener(this);
        this.mLsvSavedPost.setOnScrollListener(this);
        this.mLsvSavedPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        this.mLoadingDialog.show();

        this.mPresenterSavedPost = new PresenterSavedPost(this);
        this.mPresenterSavedPost.handleGetSavedPostList(MainActivity.EMAIL_USER, 0, 20);

        this.mAdapterListPost = new AdapterPostSale(mArrListPost, getActivity(), R.layout.item_post);
        this.mLsvSavedPost.setAdapter(this.mAdapterListPost);
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    @Override
    public void onHandleSavedPostListSuccessful(int qualityPost, ArrayList<PostSale> arrListPost) {

        this.mLoadingDialog.dismiss();

        this.mArrListPost.addAll(arrListPost);

        this.mQualityPost = qualityPost;
        String temp = "Hiển thị " + this.mArrListPost.size() + " tin. Tổng " + this.mQualityPost + " tin";
        this.mTxvQualityPost.setText(temp);

        this.mBtnMoreView.setVisibility(View.GONE);
    }

    @Override
    public void onHandleSavedPostListError(String error) {

        this.mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Đọc dữ liệu thất bại. Vui lòng thử lại sau");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    @Override
    public void onHandleUnSavePostSuccessful() {
        mLoadingDialog.dismiss();
        Toast.makeText(getContext(), "Bỏ lưu thành công", Toast.LENGTH_SHORT).show();
        this.mArrListPost.remove(mPosition);
        this.mAdapterListPost.notifyDataSetChanged();
    }

    @Override
    public void onHandleUnSavePostError(String e) {
        mLoadingDialog.dismiss();
        Toast.makeText(getContext(), "Bỏ lưu không thành công. Vui lòng thử lại sau!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_more_view:
                if(this.mQualityPost > this.mArrListPost.size()){
                    this.mPresenterSavedPost.handleGetSavedPostList(MainActivity.EMAIL_USER, this.mArrListPost.size(), 20);
                }
                this.mBtnMoreView.setVisibility(View.GONE);
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
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.poup_menu_for_saved_post, popupMenu.getMenu());

        mPosition = position;

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.control_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("post_id", mArrListPost.get(mPosition).getId());
                        startActivity(intent);
                        break;
                    case R.id.control_un_save:
                        mLoadingDialog.show();
                        mPresenterSavedPost.handleUnSavePost(mArrListPost.get(position).getId());
                        break;

                }

                return true;
            }
        });
        popupMenu.show();
    }
}
