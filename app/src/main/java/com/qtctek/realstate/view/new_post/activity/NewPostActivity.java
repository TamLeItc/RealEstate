package com.qtctek.realstate.view.new_post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.dto.Photo;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.helper.DialogHelper;
import com.qtctek.realstate.helper.KeyboardHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.description_information.DescriptionInformationFragment;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.presenter.post_detail.PresenterPostDetail;
import com.qtctek.realstate.view.new_post.adapter.NewPostAdapter;
import com.qtctek.realstate.view.new_post.map_information.fragment.MapInformationFragment;
import com.qtctek.realstate.view.new_post.product_information.fragment.ProductInformationFragment;
import com.qtctek.realstate.view.post_detail.interfaces.ViewHandlePostDetail;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.images_information.ImagesInformationFragment;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
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

    public AlertHelper alertHelper;
    public ToastHelper toastHelper;
    public DialogHelper dialogHelper;
    public KeyboardHelper keyboardHelper;

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

        alertHelper = new AlertHelper(this);
        toastHelper = new ToastHelper(this);
        dialogHelper = new DialogHelper(this);
        keyboardHelper = new KeyboardHelper(this);

        getDataFromIntent();
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

        int productId = intent.getIntExtra("post_id", -1);
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

        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if(this.viewPaper.getCurrentItem() <= 4 && item.getItemId() != R.id.action_save_temp){

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
        dialogHelper.dismiss();
        if(!status){

            alertHelper.setCallback(this);
            alertHelper.alert("Lỗi", "Có lỗi xảy ra trong kết nối. Xin thử lại sau!!!", false,
                    "Xác nhận", Constant.INSERT_DATABASE);
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

        dialogHelper.dismiss();
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
                catch (Exception e){
                }
            }
            ImagesInformationFragment.FILE_NAME = maxFile + 1;

        }

    }

    private void setAvartar(){

        ImagesInformationFragment.PROGRESSBAR.setVisibility(View.VISIBLE);

        String url = MainActivity.WEB_SERVER + "images/" + product.getThumbnail();

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
        if(position == 0){
            this.mCurrentFragment = getSupportFragmentManager().getFragments().get(0);
        }
        else{
            this.mCurrentFragment = getSupportFragmentManager().getFragments().get(1);
        }
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
            AppUtils.hideKeyboard(this);
        }
    }
}
