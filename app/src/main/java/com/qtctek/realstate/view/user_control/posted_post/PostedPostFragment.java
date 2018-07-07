package com.qtctek.realstate.view.user_control.posted_post;

import android.app.AlertDialog;;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.presenter.user_control.posted_post.PresenterPostedPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;

import java.util.ArrayList;

public class PostedPostFragment extends Fragment implements ViewHandlePostedPost ,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private View mView;

    private ListView mLsvPostedPost;

    private Dialog mLoadingDialog;

    private AdapterPostSale mAdapterListPost;
    private ArrayList<Product> mArrProduct = new ArrayList<>();

    private PresenterPostedPost mPresenterPostedPost;

    private int mPositionClick;

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
        this.mLsvPostedPost = mView.findViewById(R.id.lsv_posts);

        this.mLsvPostedPost.setOnScrollListener(this);
        this.mLsvPostedPost.setOnItemClickListener(this);
    }

    private void handleStart(){

        this.mLoadingDialog.show();

        this.mPresenterPostedPost = new PresenterPostedPost(this);
        this.mPresenterPostedPost.handleGetListPostedPost(0, 20, MainActivity.USER.getEmail());

        this.mAdapterListPost = new AdapterPostSale(mArrProduct, getActivity(), R.layout.item_post);
        this.mLsvPostedPost.setAdapter(this.mAdapterListPost);

    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    private void confirmDeletePost(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Bạn có chắc xóa bài đăng này!!!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLoadingDialog.show();
                mPresenterPostedPost.handleDeletePost(mArrProduct.get(mPositionClick).getId());
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
    public void onHandlePostListSuccessful(ArrayList<Product> arrListPost) {

        this.mLoadingDialog.dismiss();

        this.mArrProduct.addAll(arrListPost);
        this.mAdapterListPost.notifyDataSetChanged();
    }

    @Override
    public void onHandlePostListError(String error) {

        this.mLoadingDialog.dismiss();

        Toast.makeText(getActivity(), "Đọc dữ liệu thất bại. Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
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
                mAdapterListPost.notifyDataSetChanged();
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvPostedPost.getLastVisiblePosition() - mLsvPostedPost.getHeaderViewsCount() -
                mLsvPostedPost.getFooterViewsCount()) >= (mAdapterListPost.getCount() - 1)) {

            this.mLoadingDialog.show();
            this.mPresenterPostedPost.handleGetListPostedPost(this.mArrProduct.size(), 20, MainActivity.USER.getEmail());
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.control_view_detail:
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra("post_id", mArrProduct.get(mPositionClick).getId());
                        startActivity(intent);
                        break;
                    case R.id.control_edit_post:
                        handlePopupMenuEditPost();
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

    private void handlePopupMenuEditPost(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Chú ý nếu bạn sủa bất kì thông tin nào về hình ảnh, thông tin nhà, " +
                        "thông tin liên lạc của tin này nó sẽ chuyển sang chế độ \"tạm lưu\". Cho đến khi bạn" +
                        " ấn \"đăng\" trở lại thì tin sẽ chuyển sang chế độ \"chờ duyệt\"")
                .setPositiveButton("Đã hiểu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1 = new Intent(getActivity(), NewPostActivity.class);
                        intent1.putExtra("post_id", mArrProduct.get(mPositionClick).getId());
                        startActivity(intent1);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
