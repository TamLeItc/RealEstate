package com.qtctek.aladin.view.user_action.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.DialogHelper;
import com.qtctek.aladin.helper.KeyboardHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.aladin.view.user_action.adapter.UserActionAdapter;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;

public class UserActionActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ImageView mImvBack;
    private TextView mTxvToolbarTitle;

    private AlertHelper alertHelper;
    private ToastHelper toastHelper;
    private DialogHelper dialogHelper;
    private KeyboardHelper keyboardHelper;

    private boolean mDoubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        initViews();
        addControl();
        addToolbar();

        getValueFromIntent();

        alertHelper = new AlertHelper(this);
        toastHelper = new ToastHelper(this);
        dialogHelper = new DialogHelper(this);
        dialogHelper.createLoadingDialog();
    }

    public AlertHelper getAlertHelper(){
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

    public KeyboardHelper getKeyboardHelper() {
        if(keyboardHelper == null){
            keyboardHelper = new KeyboardHelper(this);
        }
        return keyboardHelper;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews(){
        mViewPager = findViewById(R.id.view_pager);
        this.mToolbar = findViewById(R.id.toolbar);
        this.mImvBack = findViewById(R.id.imv_back);
        this.mTxvToolbarTitle = findViewById(R.id.txv_toolbar_title);

        mViewPager.setOnTouchListener(this);
        mViewPager.addOnPageChangeListener(this);
        this.mImvBack.setOnClickListener(this);
    }

    private void addControl() {
        FragmentManager manager = getSupportFragmentManager();
        UserActionAdapter adapter = new UserActionAdapter(manager);
        mViewPager.setAdapter(adapter);
    }

    private void addToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.mImvBack.setOnClickListener(this);
    }

    private void getValueFromIntent(){
        try{
            Intent intent = getIntent();
            String option = intent.getStringExtra(Constant.OPTION);
            if(option.equals(Constant.UPDATE_ACCOUNT_INFORMATION)){
                mViewPager.setCurrentItem(3);
            }
        }
        catch (Exception ignored){
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                switch (mViewPager.getCurrentItem()){
                    case 0: //fragment_login
                        finish();
                        break;
                    case 1: //fragment_register
                        mViewPager.setCurrentItem(0);
                        break;
                    case 2: //fragment_forgot_password:
                        mViewPager.setCurrentItem(0);
                        break;
                    case 3: //update_user
                        Intent intent1 = new Intent(this, UserControlActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                }
        }
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
        System.gc();

        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.app_name));
                break;
            case 1:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.register_account));
                break;
            case 2:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.forgot_password));
                break;
            case 3:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.update_user));
                break;
            default:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.app_name));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void handleShowPassword(ImageView imv, EditText edt, boolean parameter){
        if(parameter){
            edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            moveCursorEnd(edt);
            imv.setImageDrawable(getResources().getDrawable(R.drawable.icon_visibility_black_24dp));
        }
        else{
            edt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            moveCursorEnd(edt);
            imv.setImageDrawable(getResources().getDrawable(R.drawable.icon_visibility_off_black_24dp));
        }
    }

    public void moveCursorEnd(EditText edt){
        edt.setSelection(edt.getText().length());
    }
}
