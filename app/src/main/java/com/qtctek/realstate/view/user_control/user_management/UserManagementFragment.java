package com.qtctek.realstate.view.user_control.user_management;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.user_management.PresenterUserManagement;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment implements ViewHandleUserManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        AlertHelper.AlertHelperCallback, ManagementFilterCallback, View.OnClickListener {

    private UserControlActivity mActivity;

    private View mView;

    private ArrayList<User> mArrUser = new ArrayList<>();
    private UserAdapter mUserAdapter;

    private ListView mLsvUsers;
    private TextView mTxvInformation;
    private RelativeLayout mRlItemUser;
    private ImageView mImvUp;

    private PresenterUserManagement mPresenterUserManagement;
    private int mPositionClick = 0;

    private boolean isFistLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_user_management, container, false);

        this.mActivity = (UserControlActivity)getActivity();

        mActivity.userFilterCallback = this;

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
        this.mImvUp = mView.findViewById(R.id.imv_up);

        this.mLsvUsers.setOnScrollListener(this);
        this.mLsvUsers.setOnItemClickListener(this);
        this.mImvUp.setOnClickListener(this);
    }

    private void handleStart(){
        mActivity.getDialogHelper().show();

        this.mUserAdapter = new UserAdapter(this.mArrUser, getContext());
        this.mLsvUsers.setAdapter(mUserAdapter);
        this.mPresenterUserManagement = new PresenterUserManagement(this);
        this.mPresenterUserManagement.handleGetUserList(0, 20, mActivity.userStatus);
    }

    @Override
    public void onHandleUserListSuccessful(ArrayList<User> arrayListUser) {

        if(isFistLoad){
            this.mArrUser.clear();
            isFistLoad = false;
        }

        mActivity.getDialogHelper().dismiss();
        this.mArrUser.addAll(arrayListUser);
        this.mUserAdapter.notifyDataSetChanged();

        if(mArrUser.size() > 0){
            this.mTxvInformation.setVisibility(View.GONE);

            if(mActivity.isFilter){
                mLsvUsers.smoothScrollToPosition(0);
                mActivity.isFilter = false;
            }
        }
        else{
            this.mTxvInformation.setVisibility(View.VISIBLE);
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
        }

    }

    @Override
    public void onHandleUserListError(String error) {

        mActivity.getDialogHelper().dismiss();

        mActivity.getAlertHelper().setCallback(this);
        mActivity.getAlertHelper().alert(getResources().getString(R.string.error),
                getResources().getString(R.string.error_read_data), false,
                getResources().getString(R.string.ok), Constant.HANDLE_ERROR);
    }

    @Override
    public void onHandleUpdateStatusUserSuccessful() {
        mActivity.getDialogHelper().dismiss();
        if(mArrUser.get(mPositionClick).getStatus().equals(Constant.NO)){
            mUserAdapter.notifyDataSetChanged();

            mActivity.getToastHelper().toast(getResources().getString(R.string.able_user_successful), ToastHelper.LENGTH_SHORT);

            mArrUser.get(mPositionClick).setStatus(Constant.YES);
        }
        else{
            mUserAdapter.notifyDataSetChanged();

            mActivity.getToastHelper().toast(getResources().getString(R.string.disable_user_successful), ToastHelper.LENGTH_SHORT);

            mArrUser.get(mPositionClick).setStatus(Constant.NO);
        }

    }

    @Override
    public void onHandleUpdateStatusUserError(String error) {

        mActivity.getDialogHelper().dismiss();

        mActivity.getToastHelper().toast(R.string.update_status_user_error, ToastHelper.LENGTH_SHORT);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (mLsvUsers.getLastVisiblePosition() - mLsvUsers.getHeaderViewsCount() -
                mLsvUsers.getFooterViewsCount()) >= (mUserAdapter.getCount() - 1)) {

            mActivity.getDialogHelper().show();
            this.mPresenterUserManagement.handleGetUserList(this.mArrUser.size(), 20, mActivity.userStatus);
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

        if(mArrUser.get(position).getEmail().equals(MainActivity.USER)){
            return;
        }

        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_user_management, popupMenu.getMenu());

        this.mRlItemUser = view.findViewById(R.id.rl_item_user);
        mRlItemUser.setBackground(getResources().getDrawable(R.drawable.custom_border_normal));

        MenuItem menuItem = popupMenu.getMenu().getItem(0);
        if(this.mArrUser.get(position).getStatus().equals("no")){
            menuItem.setTitle(mActivity.getResources().getString(R.string.enable_user));
        }
        else{
            menuItem.setTitle(mActivity.getResources().getString(R.string.disable_user));
        }

        mPositionClick = position;

        mActivity.getAlertHelper().setCallback(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_update_status:
                        if(mArrUser.get(mPositionClick).getLevel() == 1){
                            mActivity.getAlertHelper().alert(getResources().getString(R.string.error),
                                    getResources().getString(R.string.permission_denied), true, getResources().getString(R.string.ok),
                                    Constant.DENIED);
                        }
                        else{
                            mActivity.getDialogHelper().dismiss();
                            mPresenterUserManagement.handleUpdateStatusUser(mArrUser.get(mPositionClick).getId());
                            break;
                        }
                }
                return true;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                mRlItemUser.setBackground(getResources().getDrawable(R.color.colorWhite));
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

    @Override
    public void onFilter() {

        isFistLoad = true;

        mActivity.getDialogHelper().show();
        this.mPresenterUserManagement.handleGetUserList(0, 20, mActivity.userStatus);
    }

    @Override
    public void onClick(View v) {
        mLsvUsers.smoothScrollToPosition(0);
    }
}
