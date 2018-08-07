package com.qtctek.aladin.view.user_control.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.DialogHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.aladin.view.user_action.activity.UserActionActivity;
import com.qtctek.aladin.view.user_control.adapter.PostFilterAdapter;
import com.qtctek.aladin.view.user_control.adapter.UserFilterAdapter;
import com.qtctek.aladin.view.user_control.fragment.NotLoggedInControlFragment;
import com.qtctek.aladin.view.user_control.fragment.LoggedInControlFragment;
import com.qtctek.aladin.view.user_control.fragment.UserControlFragment;
import com.qtctek.aladin.view.user_control.interfaces.ManagementFilterCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class UserControlActivity extends AppCompatActivity implements AlertHelper.AlertHelperCallback, View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mImvOptionMenu;
    private ImageView mImvFilter;
    public TextView txvToolbarTitle;

    private AlertHelper alertHelper;
    private ToastHelper toastHelper;
    private DialogHelper dialogHelper;

    public ManagementFilterCallback postFilterCallback;

    public ManagementFilterCallback userFilterCallback;

    public int optionManagement;
    public int positionFragment = 0;
    public boolean isRequireAccountManagement = false;
    private boolean mDoubleBackToExitPressedOnce = false;
    public boolean isFilter = false;

    public String productFormality = "%";
    public String productStatus = "%";
    private int mProductStatusTemp;
    public String userStatus = "%";

    public static final int POST = 101;
    public static final int USER = 103;
    public static final int OTHER = 100;

    public static final String ACTIVITY = "user_control";

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

    public AlertHelper getAlertHelper() {
        if(alertHelper == null){
            alertHelper = new AlertHelper(this);
        }
        return alertHelper;
    }

    public ToastHelper getToastHelper() {
        if(toastHelper == null){
            toastHelper = new ToastHelper(this);
        }
        return toastHelper;
    }

    public DialogHelper getDialogHelper() {
        if(dialogHelper == null){
            dialogHelper = new DialogHelper(this);
        }
        return dialogHelper;
    }

    private void initViews(){
        this.mToolbar = findViewById(R.id.toolbar);
        this.txvToolbarTitle = findViewById(R.id.txv_toolbar_title);
        this.mImvOptionMenu = findViewById(R.id.imv_option_menu);
        this.mImvFilter = findViewById(R.id.imv_filter);

        this.mImvFilter.setVisibility(View.GONE);

        this.mImvOptionMenu.setOnClickListener(this);
        this.mImvFilter.setOnClickListener(this);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void handleValueFromIntent(){
        try{
            Intent intent = getIntent();
            positionFragment = intent.getIntExtra(Constant.FRAGMENT, 0);

            if(positionFragment == 0){
                    this.isRequireAccountManagement = true;
            }
            else{
                this.isRequireAccountManagement = false;
            }

        }
        catch (java.lang.NullPointerException e){
            positionFragment = 0;
            this.isRequireAccountManagement = false;
        }
    }

    public void handleShowButtonFilter(final int currentFrg){
        optionManagement = currentFrg;
        if(currentFrg == UserControlActivity.OTHER){
            this.mImvFilter.setVisibility(View.GONE);
        }
        else{
            this.mImvFilter.setVisibility(View.VISIBLE);
        }
    }

    //To know what screen to display after the user log on
    private void handleStart(){
        if(MainActivity.USER.getLevel() == User.USER_NULL){ //Chưa đăng nhập

            this.txvToolbarTitle.setText(getResources().getString(R.string.saved_information));

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_user_control, new NotLoggedInControlFragment());
            fragmentTransaction.commit();
        }
        else{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_user_control, new LoggedInControlFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_option_menu:
                handlePopupOptionMenu();
                break;
            case R.id.imv_filter:
                handleShowFilter();
                break;
        }
    }

    private void handlePopupOptionMenu(){
        PopupMenu popupMenu = new PopupMenu(this, this.mImvOptionMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_user_control, popupMenu.getMenu());

        Menu menu = popupMenu.getMenu();
        MenuItem menuItemUpdateUser = menu.findItem(R.id.action_update_information);
        MenuItem menuItemLogout = menu.findItem(R.id.action_logout);
        MenuItem menuItemLogin = menu.findItem(R.id.action_login);

        if(MainActivity.USER.getId() == User.USER_NULL){
            menuItemLogout.setVisible(false);
            menuItemUpdateUser.setVisible(false);
        }
        else{
            menuItemLogin.setVisible(false);
        }

        if(!isRequireAccountManagement){
            menuItemUpdateUser.setVisible(false);
        }
        else{
            menuItemUpdateUser.setVisible(true);
        }

        alertHelper.setCallback(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.action_home:
                        finish();
                        break;
                    case R.id.action_update_information:
                        intent = new Intent(UserControlActivity.this, UserActionActivity.class);
                        intent.putExtra(Constant.OPTION, Constant.UPDATE_ACCOUNT_INFORMATION);
                        startActivityForResult(intent, Constant.UPDATE);
                        finish();
                        break;
                    case R.id.action_login:
                        intent = new Intent(UserControlActivity.this, UserActionActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.action_logout:
                        alertHelper.alert(R.string.log_out, R.string.log_out_notifaction, false,
                                R.string.ok, R.string.cancel, Constant.LOGOUT);
                        break;
                }

                return false;
            }
        });

        popupMenu.show();
    }

    private void handleShowFilter(){
        if(this.optionManagement == POST || this.optionManagement == POST){
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

                isFilter = true;
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

                isFilter = true;
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
        else if(productFormality.equals(Constant.YES)){
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
            finish();
            MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.exitApp();
        }

        this.mDoubleBackToExitPressedOnce = true;
        toastHelper.toast(R.string.double_press_back_to_exit, ToastHelper.LENGTH_SHORT);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constant.UPDATE && resultCode == RESULT_OK && data != null){
            String fullName = data.getStringExtra(Constant.UPDATE_ACCOUNT_INFORMATION);

            if(UserControlFragment.TXV_USER_NAME_TITLE != null){
                UserControlFragment.TXV_USER_NAME_TITLE.setText(fullName);
            }
            if(UserControlFragment.TXV_USER_NAME_LOGOUT != null){
                UserControlFragment.TXV_USER_NAME_LOGOUT.setText(fullName);
            }

        }
    }
}
