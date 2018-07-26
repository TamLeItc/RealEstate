package com.qtctek.realstate.view.user_control.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.DialogHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.adapter.PostFilterAdapter;
import com.qtctek.realstate.view.user_control.adapter.UserFilterAdapter;
import com.qtctek.realstate.view.user_control.fragment.UserNormalControlFragment;
import com.qtctek.realstate.view.user_control.fragment.UserSystemControlFragment;
import com.qtctek.realstate.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class UserControlActivity extends AppCompatActivity implements AlertHelper.AlertHelperCallback {

    private Toolbar mToolbar;
    public Menu menuOption;
    public TextView txvToolbarTitle;

    private boolean mDoubleBackToExitPressedOnce = false;
    public static int POSITION_FRAGMENT = 0;

    public AlertHelper alertHelper;
    public ToastHelper toastHelper;
    public DialogHelper dialogHelper;

    public ManagementFilterCallback postFilterCallback;

    public ManagementFilterCallback userFilterCallback;

    public int currentFragment;

    public String productFormality = "%";
    public String productStatus = "%";
    private int mProductStatusTemp;

    public String userStatus = "%";

    public static final int POST_MANAGEMENT = 101;
    public static final int POSTED_POST = 102;
    public static final int USER_MANAGEMENT = 103;
    public static final int OTHER = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_control);

        alertHelper = new AlertHelper(this);
        toastHelper = new ToastHelper(this);
        dialogHelper = new DialogHelper(this, R.layout.dialog_loading);

        initViews();
        addToolbar();
        handleValueFromIntent();
        handleStart();
    }

    private void initViews(){
        this.mToolbar = findViewById(R.id.toolbar);
        this.txvToolbarTitle = findViewById(R.id.txv_toolbar_title);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void handleValueFromIntent(){
        try{
            Intent intent = getIntent();
            POSITION_FRAGMENT = intent.getIntExtra("fragment", 0);
        }
        catch (java.lang.NullPointerException e){
            POSITION_FRAGMENT = 0;
        }
    }

    public void showMenuFilter(final int currentFrg){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MenuItem menuItemFilter = menuOption.getItem(0);
                if(currentFrg == UserControlActivity.OTHER){
                    menuItemFilter.setVisible(false);
                }
                else{
                    menuItemFilter.setVisible(true);
                }
                currentFragment = currentFrg;
            }
        }, 500);
    }

    //To know what screen to display after the user log on
    private void handleStart(){
        if(MainActivity.USER.getLevel() == 3 || MainActivity.USER.getLevel() == User.USER_NULL){ //người dùng
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_user_control, new UserNormalControlFragment());
            fragmentTransaction.commit();
        }
        else{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_user_control, new UserSystemControlFragment());
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_user_control, menu);

        MenuItem menuItemNewPost = menu.findItem(R.id.control_new_post);
        MenuItem menuItemLogout = menu.findItem(R.id.control_logout);
        MenuItem menuItemUpdateUser = menu.findItem(R.id.control_update_information);
        if(MainActivity.USER.getId() == User.USER_NULL){
            menuItemNewPost.setVisible(false);
            menuItemLogout.setVisible(false);
            menuItemUpdateUser.setVisible(false);
        }

        if(MainActivity.USER.getLevel() == 3){
            menuItemNewPost.setVisible(true);
        }
        else{
            menuItemNewPost.setVisible(false);
        }

        menuOption = menu;
        MenuItem menuItemFilter = menuOption.getItem(0);
        if((currentFragment == POST_MANAGEMENT && (MainActivity.USER.getLevel() == 1 || MainActivity.USER.getLevel() == 2))
                || currentFragment == USER_MANAGEMENT && MainActivity.USER.getLevel() == 1
                || currentFragment == POSTED_POST && (MainActivity.USER.getLevel() == 3) ){
            menuItemFilter.setVisible(true);
        }
        else{
            menuItemFilter.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.control_management_filter:
                handleShowFilter();
                break;
            case R.id.control_home:
                finish();
                break;
            case R.id.control_new_post:
                intent = new Intent(UserControlActivity.this, NewPostActivity.class);
                intent.putExtra("post_id", -1);
                startActivity(intent);
                finish();
                break;
            case R.id.control_update_information:
                intent = new Intent(UserControlActivity.this, UserActionActivity.class);
                intent.putExtra("option", "update_user");
                startActivity(intent);
                finish();
                break;
            case R.id.control_logout:
                alertHelper.setCallback(this);
                alertHelper.alert("Xác nhận", "Bạn có chắc chắn muốn đăng xuất", false,
                        "Xác nhập", "Hủy bỏ", Constant.LOGOUT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleShowFilter(){
        if(this.currentFragment == POST_MANAGEMENT || this.currentFragment == POSTED_POST){
            handlePostFilter();
        }
        else{
            handleUserFilter();
        }
    }

    private void handleUserFilter() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_list);

        ListView lsvItem = dialog.findViewById(R.id.lsv_item);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView txvTitle = dialog.findViewById(R.id.txv_title);

        txvTitle.setText(getResources().getString(R.string.status));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        int option;
        if(userStatus.equals("%")){
            option = 0;
        }
        else if(userStatus.equals("no")){
            option = 1;
        }
        else{
            option = 2;
        }

        String[] arrStatus = getResources().getStringArray(R.array.user_status);
        ArrayList<String> arrListStatus = new ArrayList<>(Arrays.asList(arrStatus));
        final UserFilterAdapter userFilterAdapter = new UserFilterAdapter(this, arrListStatus, option);

        lsvItem.setAdapter(userFilterAdapter);

        lsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    userStatus = "%";
                }
                else if(position == 1){
                    userStatus = "no";
                }
                else{
                    userStatus = "yes";
                }

                userFilterCallback.onFilter();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void handlePostFilter() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_management_fillter);

        RecyclerView recyclerViewStatus = dialog.findViewById(R.id.recycler_view_status);
        final CheckBox chkForSale = dialog.findViewById(R.id.chk_for_sale);
        final CheckBox chkRent = dialog.findViewById(R.id.chk_rent);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mProductStatusTemp == 0){
                    productStatus = "%";
                }
                else{
                    productStatus = mProductStatusTemp + "";
                }
                if(chkForSale.isChecked() && chkRent.isChecked() || !chkForSale.isChecked() && !chkRent.isChecked()){
                    productFormality = "%";
                }
                else if(chkForSale.isChecked()){
                    productFormality = "yes";
                }
                else{
                    productFormality = "no";
                }
                postFilterCallback.onFilter();
                dialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewStatus.setLayoutManager(linearLayoutManager);

        if(productFormality.equals("%")){
            chkForSale.setChecked(true);
            chkRent.setChecked(true);
        }
        else if(productFormality.equals("yes")){
            chkForSale.setChecked(true);
        }
        else{
            chkRent.setChecked(true);
        }

        String[] arrStatus = getResources().getStringArray(R.array.product_status);
        ArrayList<String> arrListStatus = new ArrayList<>(Arrays.asList(arrStatus));

        final int statusSelected;
        if(productStatus.equals("%")){
            statusSelected = 0;
        }
        else{
            statusSelected = Integer.parseInt(productStatus);
        }

        PostFilterAdapter statusAdapter = new PostFilterAdapter(this, arrListStatus, statusSelected, new PostFilterAdapter.StatusAdapterCallback() {
            @Override
            public void onItemSelected(int position) {
                mProductStatusTemp = position;
            }
        });

        recyclerViewStatus.setAdapter(statusAdapter);

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        toastHelper.toast("Ấn thêm lần nữa để thoát ra màn hình chính", ToastHelper.LENGTH_SHORT);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onStop() {

        //clear memory
        Runtime.getRuntime().gc();
        //System.gc();

        super.onStop();
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(Constant.LOGOUT == option){
            MainActivity.USER.clearData();
            Intent intent = new Intent(UserControlActivity.this, UserActionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
