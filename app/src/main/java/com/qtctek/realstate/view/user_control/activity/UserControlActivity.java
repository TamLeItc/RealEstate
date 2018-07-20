package com.qtctek.realstate.view.user_control.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.DialogHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.fragment.UserNormalControlFragment;
import com.qtctek.realstate.view.user_control.fragment.UserSystemControlFragment;

public class UserControlActivity extends AppCompatActivity implements AlertHelper.AlertHelperCallback {

    private Toolbar mToolbar;
    private boolean mDoubleBackToExitPressedOnce = false;
    public static int POSITION_FRAGMENT = 0;

    public AlertHelper alertHelper;
    public ToastHelper toastHelper;
    public DialogHelper dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_control);

        alertHelper = new AlertHelper(this);
        toastHelper = new ToastHelper(this);
        dialogHelper = new DialogHelper(this, R.layout.dialog_loading);

        initViews();
        handleStart();
        addToolbar();
        getValueFromIntent();

    }

    private void initViews(){
        this.mToolbar = findViewById(R.id.toolbar);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getValueFromIntent(){
        try{
            Intent intent = getIntent();
            POSITION_FRAGMENT = intent.getIntExtra("fragment", 0);
        }
        catch (java.lang.NullPointerException e){
            POSITION_FRAGMENT = 0;
        }
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
        getMenuInflater().inflate(R.menu.user_control_menu, menu);

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
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
