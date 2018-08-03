package com.qtctek.aladin.view.new_post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.general.Constant;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.dto.Photo;
import com.qtctek.aladin.dto.Product;
import com.qtctek.aladin.helper.DialogHelper;
import com.qtctek.aladin.helper.KeyboardHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.new_post.PresenterNewPost;
import com.qtctek.aladin.view.new_post.description_information.DescriptionInformationFragment;
import com.qtctek.aladin.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.aladin.presenter.post_detail.PresenterPostDetail;
import com.qtctek.aladin.view.new_post.adapter.NewPostAdapter;
import com.qtctek.aladin.view.new_post.map_information.fragment.MapInformationFragment;
import com.qtctek.aladin.view.new_post.product_information.fragment.ProductInformationFragment;
import com.qtctek.aladin.view.post_detail.interfaces.ViewHandlePostDetail;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.new_post.images_information.ImagesInformationFragment;
import com.qtctek.aladin.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewPostActivity extends AppCompatActivity implements ViewHandleModelNewPost, ViewHandlePostDetail,
        View.OnTouchListener, View.OnClickListener, AlertHelper.AlertHelperCallback, ViewPager.OnPageChangeListener {

    public ViewPager viewPaper;
    private Toolbar mToolbar;
    private Fragment mCurrentFragment;
    private TextView mTxvToolbarTitle;
    private Menu mMenu;

    private AlertHelper mAlertHelper;
    private ToastHelper mToastHelper;
    private DialogHelper mDialogHelper;
    private KeyboardHelper mKeyboardHelper;

    public static boolean IS_UPDATE = false;
    public Product product;
    private boolean mDoubleBackToExitPressedOnce = false;
    private int mCurrentPositionFragment;

    private ImageView mImvBack;
    private ImageView mImvNext;
    private MenuItem mMenuItem;

    private NewPostAdapter mNewPostAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        initViews();
        addToolbar();

        mAlertHelper = new AlertHelper(this);
        mToastHelper = new ToastHelper(this);
        mDialogHelper = new DialogHelper(this);
        mKeyboardHelper = new KeyboardHelper(this);

        getDataFromIntent();
    }

    public AlertHelper getAlertHelper() {
        return mAlertHelper;
    }

    public ToastHelper getToastHelper() {
        return mToastHelper;
    }

    public DialogHelper getDialogHelper() {
        return mDialogHelper;
    }

    public KeyboardHelper getKeyboardHelper() {
        return mKeyboardHelper;
    }

    private void initViews(){
        viewPaper = findViewById(R.id.view_pager);
        this.mImvBack = findViewById(R.id.imv_back);
        this.mImvNext = findViewById(R.id.imv_next);
        this.mToolbar = findViewById(R.id.toolbar);
        this.mTxvToolbarTitle = findViewById(R.id.txv_toolbar_title);

        viewPaper.setOnTouchListener(this);
        viewPaper.addOnPageChangeListener(this);
        this.mImvBack.setOnClickListener(this);
        this.mImvNext.setOnClickListener(this);
    }

    private void addControl() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mNewPostAdapter = new NewPostAdapter(fragmentManager);
        viewPaper.setAdapter(mNewPostAdapter);

        this.mCurrentFragment = getSupportFragmentManager().getFragments().get(0);
        this.mCurrentPositionFragment = 0;
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getDataFromIntent(){

        Intent intent = getIntent();

        int productId = intent.getIntExtra(Product.ID, -1);
        if(productId != -1){
            IS_UPDATE = true;
        }
        else{
            this.mImvNext.setVisibility(View.GONE);
            IS_UPDATE = false;
        }
        handleStart(productId);
    }

    private void handleStart(int productId){
        mDialogHelper.show();
        if(!IS_UPDATE){
            new PresenterNewPost(this).handleInsertBlankPost(MainActivity.USER.getId());
        }
        else{
            new PresenterPostDetail(this).handleGetDataProductDetail(productId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_new_post, menu);

        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        mMenuItem = item;
        if(this.viewPaper.getCurrentItem() <= 3 && item.getItemId() != R.id.action_save_temp){
            boolean isSavedInformation = true;
            switch (mCurrentPositionFragment){
                case 0:
                    isSavedInformation = ((ImagesInformationFragment) mCurrentFragment).checkSavedInformation();
                    break;
                case 1:
                    isSavedInformation = ((ProductInformationFragment) mCurrentFragment).checkSavedInformation();
                    break;
                case 2:
                    isSavedInformation = ((DescriptionInformationFragment) mCurrentFragment).checkSavedInformation();
                    break;
                case 3:
                    isSavedInformation = ((MapInformationFragment) mCurrentFragment).checkSavedInformation();
                    break;
            }
            if(isSavedInformation){
                mAlertHelper.setCallback(this);
                mAlertHelper.alert(getResources().getString(R.string.add_new_post), getResources().getString(R.string.exit_add_new_post),
                        false, getResources().getString(R.string.ok) ,getResources().getString(R.string.cancel),
                        Constant.OPTION_MENU_SELECTED);
            }
            else{
                mAlertHelper.setCallback(this);
                mAlertHelper.alert(getResources().getString(R.string.add_new_post), getResources().getString(R.string.exit_add_new_post_without_save),
                        false, getResources().getString(R.string.ok), getResources().getString(R.string.cancel),
                        Constant.OPTION_MENU_SELECTED);
            }
        }
        else{
            handleOptionItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleSaveTemp(){
        switch (mCurrentPositionFragment){
            case 0:
                ((ImagesInformationFragment) mCurrentFragment).isSaveTemp = true;
                ((ImagesInformationFragment) mCurrentFragment).handleSaveImageInformation();
                break;
            case 1:
                ((ProductInformationFragment) mCurrentFragment).isSaveTemp = true;
                ((ProductInformationFragment) mCurrentFragment).handleSaveProductInformation();
                break;
            case 2:
                ((DescriptionInformationFragment) mCurrentFragment).isSaveTemp = true;
                ((DescriptionInformationFragment) mCurrentFragment).handleSaveDescriptionInformation();
                break;
            case 3:
                ((MapInformationFragment) mCurrentFragment).isSaveTemp = true;
                ((MapInformationFragment) mCurrentFragment).handleSaveMapInformation();
                break;
        }
    }

    private void handleOptionItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_home:
                finish();
                break;
            case R.id.action_manage_user:
                Intent intent1 = new Intent(NewPostActivity.this, UserControlActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.action_save_temp:
                handleSaveTemp();
                break;
        }
    }

    @Override
    public void onInsertBlankPost(boolean status, int productId) {
        mDialogHelper.dismiss();
        if(!status){

            mAlertHelper.setCallback(this);
            mAlertHelper.alert(getResources().getString(R.string.error_connect), getResources().getString(R.string.error_connect_notification), false,
                    getResources().getString(R.string.ok), Constant.INSERT_DATABASE);
        }
        else{
            product = new Product();
            product.setId(productId);
            addControl();
        }
    }

    @Override
    public void onUploadImages(boolean status) {

    }
    @Override
    public void onUpdateProductInformation(boolean status) {

    }

    @Override
    public void onUpdateDescriptionInformation(boolean status) {

    }

    @Override
    public void onUpdateMapInformation(boolean status) {
    }

    @Override
    public void onDeleteFile(boolean status) {

    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onHandleDataPostDetailSuccessful(Product product, ArrayList<Photo> arrPhoto) {

        mDialogHelper.dismiss();
        this.product = product;

        addControl();

        setAvartar();
        ImagesInformationFragment.ARR_PHOTO.clear();
        ImagesInformationFragment.ARR_PHOTO.addAll(arrPhoto);

        ImagesInformationFragment.IMAGE_ADAPTER.notifyDataSetChanged();

        if(!IS_UPDATE || arrPhoto.size() <= 1){
            ImagesInformationFragment.FILE_NAME = 1;
        }
        else{
            int maxFile = 1;
            for(int i = 0; i < arrPhoto.size(); i++){
                try{
                    String[] strImage = arrPhoto.get(arrPhoto.size() - 1).getPhotoLink().split("_");
                    StringBuilder stringBuilder = new StringBuilder(strImage[1]);
                    stringBuilder.delete(1, 5);
                    int temp = Integer.parseInt(stringBuilder.toString());
                    if(temp > maxFile){
                        maxFile = temp;
                    }
                }
                catch (Exception ignored){
                }
            }
            ImagesInformationFragment.FILE_NAME = maxFile + 1;

        }

    }

    private void setAvartar(){

        ImagesInformationFragment.PROGRESSBAR.setVisibility(View.VISIBLE);

        String url = MainActivity.WEB_SERVER + MainActivity.IMAGE_URL_RELATIVE + product.getThumbnail();

        if(url.equals("")){
            ImagesInformationFragment.PROGRESSBAR.setVisibility(View.GONE);
            ImagesInformationFragment.IMV_AVARTAR.setImageResource(R.drawable.icon_product);
            return;
        }

        Picasso.with(this).load(url).into(ImagesInformationFragment.IMV_AVARTAR, new Callback() {
            @Override
            public void onSuccess() {
                ImagesInformationFragment.PROGRESSBAR.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                ImagesInformationFragment.PROGRESSBAR.setVisibility(View.GONE);
                ImagesInformationFragment.IMV_AVARTAR.setImageResource(R.drawable.icon_product);
            }
        });
    }

    @Override
    public void onHandleDataPostDetailError(String error) {
        mDialogHelper.dismiss();

        mAlertHelper.setCallback(this);
        mAlertHelper.alert(getResources().getString(R.string.error_read_data), getResources().getString(R.string.error_read_data_notification),
                false, getResources().getString(R.string.ok), Constant.HANDLE_ERROR);
    }


    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            finish();
            MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.exitApp();
        }

        this.mDoubleBackToExitPressedOnce = true;
        mToastHelper.toast(getResources().getString(R.string.double_press_back_to_exit), ToastHelper.LENGTH_SHORT);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                if(viewPaper.getCurrentItem() == 0){
                    Intent intent = new Intent(this, UserControlActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(viewPaper.getCurrentItem() < 4){
                    int currentPage = viewPaper.getCurrentItem();
                    viewPaper.setCurrentItem(currentPage - 1);
                }
                break;
            case R.id.imv_next:
                if(viewPaper.getCurrentItem() < 3){
                    int currentPage = viewPaper.getCurrentItem();
                    viewPaper.setCurrentItem(currentPage + 1);
                }

        }
    }

    @Override
    protected void onStop() {
        //clear memory
        Runtime.getRuntime().gc();
        System.gc();

        super.onStop();
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.OPTION_MENU_SELECTED){
            handleOptionItemSelected(mMenuItem);
        }
        else if (option == Constant.INSERT_DATABASE){
            Intent intent = new Intent(NewPostActivity.this, UserControlActivity.class);
            startActivity(intent);
            finish();
        }
        else if(option == Constant.HANDLE_ERROR){
            Intent intent = new Intent(this, UserControlActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.mCurrentFragment = mNewPostAdapter.getItem(position);

        mCurrentPositionFragment = position;

        switch (position){
            case 0:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.new_post_image));
                break;
            case 1:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.new_post_information));
                break;
            case 2:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.new_post_description));
                break;
            case 3:
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.new_post_location));
                break;
            case 4:
                MenuItem menuItem = mMenu.getItem(2);
                menuItem.setVisible(false);
                this.mTxvToolbarTitle.setText(getResources().getString(R.string.new_post_finish));
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state == 0){
            mKeyboardHelper.hideKeyboard(this);
        }
    }
}
