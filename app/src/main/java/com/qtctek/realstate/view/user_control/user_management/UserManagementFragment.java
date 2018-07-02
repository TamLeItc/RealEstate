package com.qtctek.realstate.view.user_control.user_management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.presenter.user_control.user_management.PresenterUserManagement;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment implements ViewHandleUserManagement,
        View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private View mView;
    private View mItemView;

    private int mQualityUser = 0;
    private int mPreLast;

    private ArrayList<User> mArrUser = new ArrayList<>();
    private UserAdapter mUserAdapter;

    private ListView mLsvUsers;
    private TextView mTxvQualityUser;
    private Button mBtnMoreView;

    private Dialog mLoadingDialog;

    private PresenterUserManagement mPresenterUserManagement;
    private int mPositionClick = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_user_management, container, false);

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
        this.mLsvUsers = mView.findViewById(R.id.lsv_user);
        this.mTxvQualityUser = mView.findViewById(R.id.txv_quality_user);
        this.mBtnMoreView = mView.findViewById(R.id.btn_more_view);

        this.mBtnMoreView.setOnClickListener(this);
        this.mLsvUsers.setOnScrollListener(this);
        this.mLsvUsers.setOnItemClickListener(this);
    }

    private void handleStart(){
        this.mLoadingDialog.show();

        this.mUserAdapter = new UserAdapter(this.mArrUser, getContext());
        this.mLsvUsers.setAdapter(mUserAdapter);
        this.mPresenterUserManagement = new PresenterUserManagement(this);
        this.mPresenterUserManagement.handleGetUserList(0, 20);
    }

    private void createLoadingDialog(){
        this.mLoadingDialog = new Dialog(getActivity());
        this.mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mLoadingDialog.setContentView(R.layout.dialog_loading);
        this.mLoadingDialog.setCancelable(false);
    }

    @Override
    public void onHandleUserListSuccessful(int qualityUser, ArrayList<User> arrayListUser) {

        this.mLoadingDialog.dismiss();

        this.mArrUser.addAll(arrayListUser);
        this.mUserAdapter.notifyDataSetChanged();
        this.mQualityUser = qualityUser;
        String temp = "Hiển thị " + this.mArrUser.size() + " user. Tổng " + this.mQualityUser + " user";
        this.mTxvQualityUser.setText(temp);

        this.mBtnMoreView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onHandleUserListError(String error) {

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
    public void onHandleUpdateStatusUserSuccessful() {
        mLoadingDialog.dismiss();
        if(mArrUser.get(mPositionClick).getStatus().equals("disable")){
            this.mItemView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPostActive));
            Toast.makeText(getActivity(), "Bỏ khóa tài khoản thành công", Toast.LENGTH_SHORT).show();

            mArrUser.get(mPositionClick).setStatus("enable");
        }
        else{
            this.mItemView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPostNotActive));
            Toast.makeText(getActivity(), "Khóa tài khoản thành công", Toast.LENGTH_SHORT).show();

            mArrUser.get(mPositionClick).setStatus("disable");
        }

    }

    @Override
    public void onHandleUpdateStatusUserError(String error) {
        mLoadingDialog.dismiss();
        Log.d("ttt", error);
        Toast.makeText(getActivity(), "Cập nhật trạng thái user thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_more_view:
                if(this.mArrUser.size() < this.mQualityUser){
                    this.mPresenterUserManagement.handleGetUserList(this.mArrUser.size(), 20);
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
        switch(view.getId())
        {
            case R.id.lsv_user:

                final int lastItem = firstVisibleItem + visibleItemCount;

                if(this.mPreLast > lastItem){
                    this.mBtnMoreView.setVisibility(View.GONE);
                }
                else{
                    if(lastItem == totalItemCount){
                        if(this.mQualityUser > this.mArrUser.size()){
                            this.mBtnMoreView.setVisibility(View.VISIBLE);
                        }
                    }
                    this.mPreLast = lastItem;
                }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if(mArrUser.get(position).getEmail().equals(MainActivity.EMAIL_USER)){
            return;
        }

        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_user_management, popupMenu.getMenu());

        MenuItem menuItem = popupMenu.getMenu().getItem(0);
        if(this.mArrUser.get(position).getStatus().equals("disable")){
            menuItem.setTitle(getActivity().getResources().getString(R.string.enable_user));
        }
        else{
            menuItem.setTitle(getActivity().getResources().getString(R.string.disable_user));
        }

        mPositionClick = position;
        this.mItemView = view;

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.control_update_status:
                        if(mArrUser.get(mPositionClick).getRole().equals("Admin")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                    .setMessage("Bạn không thể thay đổi trạng thái của một user " +
                                            "có quyền \"Admin\"")
                                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            builder.show();
                        }
                        else{
                            mLoadingDialog.show();
                            mPresenterUserManagement.handleUpdateStatusUser(mArrUser.get(mPositionClick).getIdUser());
                            break;
                        }
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
