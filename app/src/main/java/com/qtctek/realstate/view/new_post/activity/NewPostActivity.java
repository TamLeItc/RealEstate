package com.qtctek.realstate.view.new_post.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.presenter.post_detail.PresenterPostDetail;
import com.qtctek.realstate.view.new_post.adapter.NewPostAdapter;
import com.qtctek.realstate.view.post_detail.interfaces.ViewHandlePostDetail;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.images_information.ImagesInformationFragment;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;

public class NewPostActivity extends AppCompatActivity implements ViewHandleModelNewPost, ViewHandlePostDetail,
        View.OnTouchListener, View.OnClickListener {

    private ViewPager mViewPaper;
    private Toolbar mToolbar;

    public static boolean IS_UPDATE = false;

    private Dialog mLoadingDialog;

    public static Product PRODUCT;
    private boolean mDoubleBackToExitPressedOnce = false;

    private Button mBtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        initViews();
        addToolbar();
        getDataFromIntent();
        createLoadingDialog();
        mLoadingDialog.show();

    }

    private void initViews(){
        mViewPaper = findViewById(R.id.view_pager);
        this.mBtnBack = findViewById(R.id.btn_back);
        this.mToolbar = findViewById(R.id.toolbar);

        mViewPaper.setOnTouchListener(this);
        this.mBtnBack.setOnClickListener(this);
    }

    private void addControl() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NewPostAdapter newPostAdapter = new NewPostAdapter(fragmentManager);
        mViewPaper.setAdapter(newPostAdapter);
        mViewPaper.setCurrentItem(0);
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
            IS_UPDATE = false;
        }
        handleStart(productId);
    }

    private void handleStart(int productId){
        if(!IS_UPDATE){
            new PresenterNewPost(this).handleInsertBlankPost(MainActivity.USER.getEmail());
        }
        else{
            new PresenterPostDetail(this).handleGetDataProductDetail(productId);
        }
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(this);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if(this.mViewPaper.getCurrentItem() <= 4){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Bạn có chắc muốn thoát ra!!!");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handleOptionItemSelected(item);
                }
            })
            .setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
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
        mLoadingDialog.dismiss();
        if(!status){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Có lỗi xảy ra trong kết nối. Xin thử lại sau!!!");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(NewPostActivity.this, UserControlActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            alertDialog.show();
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
    public void onHandleDataPostDetailSuccessful(Product product, ArrayList<String> arrImages) {

        mLoadingDialog.dismiss();



        addControl();

        String avartar = product.getThumbnail();
        for(int i = 0; i < arrImages.size(); i++){
            if(!avartar.equals(arrImages.get(i))){
                Uri uri = Uri.parse(arrImages.get(i));
                ImagesInformationFragment.ARR_URI.add(uri);
            }
        }

        ImagesInformationFragment.QUALITY_IMAGE = arrImages.size() - 1;
        ImagesInformationFragment.QUALITY_IMAGE_UPLOADED = arrImages.size() - 1;
        ImagesInformationFragment.IMAGE_ADAPTER.notifyDataSetChanged();

        if(!IS_UPDATE || arrImages.size() <= 1){
            ImagesInformationFragment.FILE_NAME = 1;
        }
        else{
            int maxFile = 1;
            for(int i = 0; i < arrImages.size(); i++){
                try{
                    String[] strImage = arrImages.get(arrImages.size() - 1).split("_");
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

    @Override
    public void onHandleDataPostDetailError(String error) {
        mLoadingDialog.dismiss();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Có lỗi xảy ra trong quá trình đọc dữ liệu. Xin thử lại sau!!!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                comebackActivity();
            }
        });
        alertDialog.show();
    }

    private void comebackActivity(){
        Intent intent = new Intent(this, UserControlActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn thêm lần nữa để thoát khỏi ứng dụng", Toast.LENGTH_SHORT).show();

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
            case R.id.btn_back:
                if(mViewPaper.getCurrentItem() == 0){
                    Intent intent = new Intent(this, UserControlActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    mViewPaper.setCurrentItem(mViewPaper.getCurrentItem() - 1);
                }

        }
    }
}
