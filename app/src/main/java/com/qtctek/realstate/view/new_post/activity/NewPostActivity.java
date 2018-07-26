package com.qtctek.realstate.view.new_post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.dto.Photo;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.helper.DialogHelper;
import com.qtctek.realstate.helper.KeyboardHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.presenter.post_detail.PresenterPostDetail;
import com.qtctek.realstate.view.new_post.adapter.NewPostAdapter;
import com.qtctek.realstate.view.post_detail.interfaces.ViewHandlePostDetail;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.images_information.ImagesInformationFragment;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewPostActivity extends AppCompatActivity implements ViewHandleModelNewPost, ViewHandlePostDetail,
        View.OnTouchListener, View.OnClickListener, AlertHelper.AlertHelperCallback {

    public ViewPager viewPaper;
    private Toolbar mToolbar;

    public static boolean IS_UPDATE = false;

    public AlertHelper alertHelper;
    public ToastHelper toastHelper;
    public DialogHelper dialogHelper;
    public KeyboardHelper keyboardHelper;

    public static Product PRODUCT;
    private boolean mDoubleBackToExitPressedOnce = false;

    private String[] mArrDescriptionDataProgressBarState;

    private Button mBtnBack;
    private Button mBtnNext;
    private MenuItem mMenuItem;
    public StateProgressBar progressBarState;

    private NewPostAdapter mNewPostAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        this.mArrDescriptionDataProgressBarState = getResources().getStringArray(R.array.description_data_progressbar_state);

        initViews();
        addToolbar();

        alertHelper = new AlertHelper(this);
        toastHelper = new ToastHelper(this);
        dialogHelper = new DialogHelper(this);
        keyboardHelper = new KeyboardHelper(this);

        getDataFromIntent();
    }

    private void initViews(){
        viewPaper = findViewById(R.id.view_pager);
        this.mBtnBack = findViewById(R.id.imv_back);
        this.mBtnNext = findViewById(R.id.btn_next);
        this.mToolbar = findViewById(R.id.toolbar);
        this.progressBarState = findViewById(R.id.progress_bar_state);

        viewPaper.setOnTouchListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnNext.setOnClickListener(this);

        this.progressBarState.setStateDescriptionData(mArrDescriptionDataProgressBarState);
    }

    private void addControl() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mNewPostAdapter = new NewPostAdapter(fragmentManager);
        viewPaper.setAdapter(mNewPostAdapter);
        viewPaper.setCurrentItem(0);

    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getDataFromIntent(){

        Intent intent = getIntent();

        int productId = intent.getIntExtra("post_id", -1);
        if(productId != -1){
            IS_UPDATE = true;
        }
        else{
            this.mBtnNext.setVisibility(View.GONE);
            IS_UPDATE = false;
        }
        handleStart(productId);
    }

    private void handleStart(int productId){
        dialogHelper.show();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if(this.viewPaper.getCurrentItem() <= 4){

            alertHelper.setCallback(this);
            alertHelper.alert("", "Bạn chắc chắn thoát ra", false, "Xác nhận",
                    "Hủy bỏ", Constant.OPTION_MENU_SELECTED);
            mMenuItem = item;
        }
        else{
            handleOptionItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
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
        }
    }

    @Override
    public void onInsertBlankPost(boolean status, int productId) {
        dialogHelper.dismiss();
        if(!status){

            alertHelper.setCallback(this);
            alertHelper.alert("Lỗi", "Có lỗi xảy ra trong kết nối. Xin thử lại sau!!!", false,
                    "Xác nhận", Constant.INSERT_DATABASE);
        }
        else{
            PRODUCT = new Product();
            PRODUCT.setId(productId);
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

        dialogHelper.dismiss();
        NewPostActivity.PRODUCT = product;

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
                catch (Exception e){
                }
            }
            ImagesInformationFragment.FILE_NAME = maxFile + 1;

        }

    }

    private void setAvartar(){

        ImagesInformationFragment.PROGRESSBAR.setVisibility(View.VISIBLE);

        String url = MainActivity.WEB_SERVER + "images/" + NewPostActivity.PRODUCT.getThumbnail();

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
        dialogHelper.dismiss();

        alertHelper.setCallback(this);
        alertHelper.alert("Lỗi", "Có lỗi xảy ra trong quá trình đọc dữ liệu. Xin thử lại sau!!!",
                false, "Xác nhận", Constant.HANDLE_ERROR);
    }


    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn thêm lần nữa để thoát ra màn hình chính", Toast.LENGTH_SHORT).show();

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
                    setCurrentStateNumberProgressBar(viewPaper.getCurrentItem());
                }
                break;
            case R.id.btn_next:
                if(viewPaper.getCurrentItem() < 3){
                    int currentPage = viewPaper.getCurrentItem();
                    viewPaper.setCurrentItem(currentPage + 1);
                    setCurrentStateNumberProgressBar(viewPaper.getCurrentItem());
                }

        }
    }

    public void setCurrentStateNumberProgressBar(int current){
        if(current == 0){
            progressBarState.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        }
        else if(current == 1){
            progressBarState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        }
        else if(current == 2){
            progressBarState.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        }
        else if(current == 3){
            progressBarState.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
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
}
