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
import com.qtctek.realstate.dto.User_Object;
import com.qtctek.realstate.presenter.user_control.user_management.PresenterUserManagement;
import com.qtctek.realstate.view.post_news.activity.MainActivity;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment implements ViewHandleUserManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private View mView;
    private View mItemView;

    private ArrayList<User> mArrUser = new ArrayList<>();
    private UserAdapter mUserAdapter;

    private ListView mLsvUsers;

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
    public void onHandleUserListSuccessful(ArrayList<User> arrayListUser) {

        this.mLoadingDialog.dismiss();
        this.mArrUser.addAll(arrayListUser);
        this.mUserAdapter.notifyDataSetChanged();

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
        Toast.makeText(getActivity(), "Cập nhật trạng thái user thất bại", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvUsers.getLastVisiblePosition() - mLsvUsers.getHeaderViewsCount() -
                mLsvUsers.getFooterViewsCount()) >= (mUserAdapter.getCount() - 1)) {

            mLoadingDialog.show();
            this.mPresenterUserManagement.handleGetUserList(this.mArrUser.size(), 20);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if(mArrUser.get(position).getEmail().equals(MainActivity.USER)){
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
                        if(mArrUser.get(mPositionClick).getLevel() == 1){
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
                            mPresenterUserManagement.handleUpdateStatusUser(mArrUser.get(mPositionClick).getId());
                            break;
                        }
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
