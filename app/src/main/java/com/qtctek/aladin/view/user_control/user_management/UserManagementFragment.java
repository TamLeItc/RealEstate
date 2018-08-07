package com.qtctek.aladin.view.user_control.user_management;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.user_control.user_management.PresenterUserManagement;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;
import com.qtctek.aladin.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment implements ViewHandleUserManagement, AbsListView.OnScrollListener, AdapterView.OnItemClickListener,
        AlertHelper.AlertHelperCallback, ManagementFilterCallback, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UserControlActivity mActivity;

    private View mView;

    private ArrayList<User> mArrUser = new ArrayList<>();
    private UserAdapter mUserAdapter;

    private ListView mLsvUsers;
    private TextView mTxvInformation;
    private RelativeLayout mRlItemUser;
    private ImageView mImvUp;
    private SwipeRefreshLayout mSRLUser;

    private PresenterUserManagement mPresenterUserManagement;

    private int mPositionClick = 0;
    private boolean mIsFirstLoadList = true;

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
        this.mSRLUser = mView.findViewById(R.id.srl_user);

        this.mLsvUsers.setOnScrollListener(this);
        this.mLsvUsers.setOnItemClickListener(this);
        this.mImvUp.setOnClickListener(this);
        this.mSRLUser.setOnRefreshListener(this);
    }

    private void handleStart(){
        mActivity.getDialogHelper().show();

        this.mUserAdapter = new UserAdapter(this.mArrUser, mActivity);
        this.mLsvUsers.setAdapter(mUserAdapter);
        this.mPresenterUserManagement = new PresenterUserManagement(this);
        this.mPresenterUserManagement.handleGetUserList(0, 20, mActivity.userStatus);
    }

    @Override
    public void onHandleUserListSuccessful(ArrayList<User> arrayListUser) {

        mActivity.getDialogHelper().dismiss();
        mSRLUser.setRefreshing(false);

        if(mIsFirstLoadList){
            mArrUser.clear();
            mIsFirstLoadList = false;
        }
        else if(arrayListUser.size() == 0){
            return;
        }

        addList(arrayListUser);
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

    /*
     * Dữ liệu trên server có thể thay đổi khiến limit và start có thể sẽ lấy về dữ liệu đã có trong danh sách
     * Phương thức này đảm bảo không có 2 đối tượng giống nhau nào trong danh sách
     */
    private void addList(ArrayList<User> arrProduct){
        int size = this.mArrUser.size();
        for(int i = 0; i < arrProduct.size(); i++){
            boolean isExisted = false;
            for(int j = 0; j < size; j++){
                if(arrProduct.get(i).getId() == this.mArrUser.get(j).getId()){
                    isExisted = true;
                    break;
                }
            }
            if(!isExisted){
                this.mArrUser.add(arrProduct.get(i));
                arrProduct.remove(i);
                i--;
            }
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

            mActivity.getToastHelper().toast(R.string.able_user_successful, ToastHelper.LENGTH_SHORT);

            mArrUser.get(mPositionClick).setStatus(Constant.YES);
        }
        else{
            mUserAdapter.notifyDataSetChanged();

            mActivity.getToastHelper().toast(R.string.disable_user_successful, ToastHelper.LENGTH_SHORT);

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

        PopupMenu popupMenu = new PopupMenu(mActivity, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_for_user_management, popupMenu.getMenu());

        this.mRlItemUser = view.findViewById(R.id.rl_item_user);
        mRlItemUser.setBackground(getResources().getDrawable(R.drawable.custom_border_normal));

        MenuItem menuItem = popupMenu.getMenu().getItem(0);
        if(this.mArrUser.get(position).getStatus().equals(Constant.NO)){
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
                            mActivity.getAlertHelper().alert(R.string.error, R.string.permission_denied, true,
                                    R.string.ok, Constant.DENIED);
                        }
                        else{
                            mActivity.getDialogHelper().show();
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
            ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    @Override
    public void onFilter() {

        mIsFirstLoadList = true;

        mActivity.getDialogHelper().show();
        this.mPresenterUserManagement.handleGetUserList(0, 20, mActivity.userStatus);
    }

    @Override
    public void onClick(View v) {
        mLsvUsers.smoothScrollToPosition(0);
    }


    @Override
    public void onRefresh() {

        mActivity.userStatus = "%";
        mIsFirstLoadList = true;

        this.mPresenterUserManagement.handleGetUserList(0, 20, mActivity.userStatus);
    }
}
