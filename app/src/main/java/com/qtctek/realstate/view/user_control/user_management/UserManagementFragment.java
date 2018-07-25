package com.qtctek.realstate.view.user_control.user_management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.user_management.PresenterUserManagement;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment implements ViewHandleUserManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AlertHelper.AlertHelperCallback {

    private View mView;
    private View mItemView;

    private ArrayList<User> mArrUser = new ArrayList<>();
    private UserAdapter mUserAdapter;

    private ListView mLsvUsers;
    private TextView mTxvInformation;

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
        handleStart();
    }

    private void initViews(){
        this.mLsvUsers = mView.findViewById(R.id.lsv_user);
        this.mTxvInformation = mView.findViewById(R.id.txv_information);

        this.mLsvUsers.setOnScrollListener(this);
        this.mLsvUsers.setOnItemClickListener(this);
    }

    private void handleStart(){
        ((UserControlActivity)getActivity()).dialogHelper.show();

        this.mUserAdapter = new UserAdapter(this.mArrUser, getContext());
        this.mLsvUsers.setAdapter(mUserAdapter);
        this.mPresenterUserManagement = new PresenterUserManagement(this);
        this.mPresenterUserManagement.handleGetUserList(0, 20);
    }

    @Override
    public void onHandleUserListSuccessful(ArrayList<User> arrayListUser) {

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();
        this.mArrUser.addAll(arrayListUser);
        this.mUserAdapter.notifyDataSetChanged();

        if(mArrUser.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
            this.mTxvInformation.setVisibility(View.VISIBLE);
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
        }

    }

    @Override
    public void onHandleUserListError(String error) {

        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((NewPostActivity)getActivity()).alertHelper.setCallback(this);
        ((NewPostActivity)getActivity()).alertHelper.alert("Lỗi",
                "Đọc dữ liệu thất bại. Vui lòng thử lại sau", false,
                "Xác nhận", Constant.HANDLE_ERROR);
    }

    @Override
    public void onHandleUpdateStatusUserSuccessful() {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();
        if(mArrUser.get(mPositionClick).getStatus().equals("disable")){
            this.mItemView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPostActive));

            ((UserControlActivity)getActivity()).toastHelper.toast("Bỏ khóa tài khoản thành công", ToastHelper.LENGTH_SHORT);

            mArrUser.get(mPositionClick).setStatus("enable");
        }
        else{
            this.mItemView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPostNotActive));

            ((UserControlActivity)getActivity()).toastHelper.toast("Khóa tài khoản thành công", ToastHelper.LENGTH_SHORT);

            mArrUser.get(mPositionClick).setStatus("disable");
        }

    }

    @Override
    public void onHandleUpdateStatusUserError(String error) {
        ((UserControlActivity)getActivity()).dialogHelper.dismiss();

        ((UserControlActivity)getActivity()).toastHelper.toast("Cập nhật trạng thái user thất bại", ToastHelper.LENGTH_SHORT);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvUsers.getLastVisiblePosition() - mLsvUsers.getHeaderViewsCount() -
                mLsvUsers.getFooterViewsCount()) >= (mUserAdapter.getCount() - 1)) {

            ((UserControlActivity)getActivity()).dialogHelper.show();
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

        ((UserControlActivity)getActivity()).alertHelper.setCallback(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.control_update_status:
                        if(mArrUser.get(mPositionClick).getLevel() == 1){
                            ((UserControlActivity)getActivity()).alertHelper.alert("Lỗi",
                                    "Bạn không thể thực thay đổi trạng thái môt" +
                                            " user có quyền \"Admin\"", true, "Xác nhận",
                                    Constant.DENIED);
                        }
                        else{
                            ((UserControlActivity)getActivity()).dialogHelper.dismiss();
                            mPresenterUserManagement.handleUpdateStatusUser(mArrUser.get(mPositionClick).getId());
                            break;
                        }
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.HANDLE_ERROR){
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
