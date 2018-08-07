package com.qtctek.aladin.view.post_detail.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qtctek.aladin.R;
import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.Photo;
import com.qtctek.aladin.dto.Product;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.DialogHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.post_detail.PresenterPostDetail;
import com.qtctek.aladin.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.aladin.view.post_detail.interfaces.ViewHandlePostDetail;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.aladin.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.aladin.view.post_news.fragment.PostFragment;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;
import com.qtctek.aladin.view.user_control.saved_post.ViewHandleSavedPost;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PostDetailActivity extends AppCompatActivity implements ViewHandlePostDetail, View.OnClickListener,
    OnMapReadyCallback, ViewHandleSavedPost, ViewTreeObserver.OnScrollChangedListener, AlertHelper.AlertHelperCallback {

    private Button mBtnImageView;
    private Button mBtnMapView;
    private SliderLayout mSliderLayout;
    private TextView mTxvPrice;
    private TextView mTxvArea;
    private TextView mTxvBedroom;
    private TextView mTxvBathroom;
    private TextView mTxvAMonth;
    private TextView mDateUpload;
    private TextView mTxvDescription;
    private TextView mTxvContactName;
    private TextView mTxvContactNumberPhone;
    private TextView mTxvContactEmail;
    private TextView mTxvAddress;
    private TextView mTxvDistrictProvinceCity;
    private TextView mTxvType;
    private TextView mTxvArchitecture;
    private TextView mTxvAmenities;
    private TextView mTxvTitle;
    private TextView mTxvFormality;
    private ImageView mImvBack;
    private ImageButton mImvMore;
    private ImageButton mImbSave;
    private ImageButton mImbCall;
    private ImageButton mImbMail;
    private LinearLayout mLlMore;
    private RelativeLayout mRlOption ;
    private ExpandableLayout mExpandableLayout;
    private ScrollView mScrollView;
    private RelativeLayout mRlSlide;

    private SupportMapFragment mFrgMap;
    private GoogleMap mMap;

    private PresenterPostDetail mPresenterPostDetail;

    public AlertHelper alertHelper;
    public ToastHelper toastHelper;
    public DialogHelper dialogHelper;

    private Product mProduct;

    private int mPosition;
    private String mActivity;
    private int mScrollY;

    private boolean mDoubleBackToExitPressedOnce = false;
    private boolean mIsDescriptionExpand = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        alertHelper = new AlertHelper(this);
        toastHelper = new ToastHelper(this);
        dialogHelper = new DialogHelper(this);
        dialogHelper.createLoadingDialog();

        handleGetDataFromIntent();

        initViews();
        initSupportMapFragment();
    }

    private void handleGetDataFromIntent(){
        dialogHelper.show();

        Intent intent = getIntent();
        int idPost = intent.getIntExtra(Product.ID, 0);
        this.mPosition = intent.getIntExtra(Constant.POSITION, -1);
        this.mActivity = intent.getStringExtra(Constant.ACTIVITY);

        this.mPresenterPostDetail = new PresenterPostDetail(this);
        this.mPresenterPostDetail.handleGetDataProductDetail(idPost);
    }

    private void initViews(){
        this.mScrollView = findViewById(R.id.scroll_view);
        this.mBtnImageView = findViewById(R.id.btn_image_view);
        this.mBtnMapView = findViewById(R.id.btn_map_view);
        this.mSliderLayout = findViewById(R.id.slider);
        this.mTxvPrice = findViewById(R.id.txv_price);
        this.mTxvArea = findViewById(R.id.txv_area);
        this.mTxvBedroom = findViewById(R.id.txv_bedroom);
        this.mTxvBathroom = findViewById(R.id.txv_bathroom);
        this.mTxvAMonth = findViewById(R.id.txv_a_month);
        this.mDateUpload = findViewById(R.id.txv_date_upload);
        this.mTxvDescription = findViewById(R.id.txv_description);
        this.mTxvContactName = findViewById(R.id.txv_contact_name);
        this.mTxvContactNumberPhone = findViewById(R.id.txv_contact_number_phone);
        this.mTxvContactEmail = findViewById(R.id.txv_contact_email);
        this.mTxvAddress = findViewById(R.id.txv_address);
        this.mTxvDistrictProvinceCity = findViewById(R.id.txv_district_province_city);
        this.mTxvType = findViewById(R.id.txv_type);
        this.mTxvArchitecture = findViewById(R.id.txv_architecture);
        this.mTxvAmenities = findViewById(R.id.txv_amenities);
        this.mImvBack = findViewById(R.id.imv_back);
        this.mImvMore = findViewById(R.id.imv_more);
        this.mImbCall =
                findViewById(R.id.imb_call);
        this.mImbSave = findViewById(R.id.imb_save);
        this.mLlMore = findViewById(R.id.ll_more);
        this.mTxvTitle = findViewById(R.id.txv_title);
        this.mExpandableLayout = findViewById(R.id.expandable_layout);
        this.mTxvFormality = findViewById(R.id.txv_formality);
        this.mRlOption = findViewById(R.id.rl_option);
        this.mRlSlide = findViewById(R.id.rl_slide);
        this.mImbMail = findViewById(R.id.imb_mail);

        if(this.mActivity.equals(UserControlActivity.ACTIVITY)){
            this.mImbSave.setVisibility(View.GONE);
        }
        else{
            this.mImbSave.setVisibility(View.VISIBLE);
        }

        this.mBtnMapView.setOnClickListener(this);
        this.mBtnImageView.setOnClickListener(this);
        this.mImvMore.setOnClickListener(this);
        this.mLlMore.setOnClickListener(this);
        this.mImvMore.setOnClickListener(this);
        this.mImvBack.setOnClickListener(this);
        this.mImbCall.setOnClickListener(this);
        this.mImbSave.setOnClickListener(this);
        this.mImbMail.setOnClickListener(this);
        this.mScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
    }

    private void initSupportMapFragment(){
        this.mFrgMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg_map);
        this.mFrgMap.getMapAsync(this);
    }


    @SuppressLint("SetTextI18n")
    private void handleView(){

        this.mDateUpload.setText(formatDate(mProduct.getDateUpload()));
        this.mTxvDescription.setText(mProduct.getDescription());
        this.mTxvContactName.setText(mProduct.getUserFullName());
        this.mTxvContactNumberPhone.setText(mProduct.getUserPhone());
        this.mTxvContactEmail.setText(mProduct.getUserEmail());
        this.mTxvAddress.setText(mProduct.getAddress());

        if(mProduct.getTitle().equals("")){
            this.mTxvTitle.setVisibility(View.GONE);
        }
        else{
            this.mTxvTitle.setText(mProduct.getTitle());
        }

        this.mTxvArchitecture.setText(": " + mProduct.getArchitecture());
        this.mTxvType.setText(": " + mProduct.getType());
        this.mTxvAmenities.setText(": " + mProduct.getAmenities());

        String temp = mProduct.getDistrict() + ", " + mProduct.getCity();
        this.mTxvDistrictProvinceCity.setText(temp);

        String strArea = mProduct.getArea() + "";
        this.mTxvArea.setText(strArea);

        this.mTxvBedroom.setText(mProduct.getBedroom() + "");
        this.mTxvBathroom.setText(mProduct.getBathroom() + "");

        String strPrice = AppUtils.getStringPrice(mProduct.getPrice(), AppUtils.LONG_PRICE);

        if(mProduct.getFormality().equals(Constant.NO)){
            if(!strPrice.equals(AppUtils.LONG_NEGOTIATE)){
                this.mTxvAMonth.setVisibility(View.VISIBLE);
            }
            else{
                this.mTxvAMonth.setVisibility(View.GONE);
            }
            this.mTxvFormality.setText(getResources().getString(R.string.rent_house));
        }
        else{
            this.mTxvAMonth.setVisibility(View.GONE);
            this.mTxvFormality.setText(getResources().getString(R.string.house_for_sale));
        }

        this.mTxvPrice.setText(strPrice);

        if(this.mProduct.getIsSaved()){
            this.mImbSave.setImageResource(R.drawable.icon_favorite_red_24dp);
        }
        else{
            this.mImbSave.setImageResource(R.drawable.icon_favorite_border_white_24dp);
        }

    }

    private String formatDate(String dt){
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.YYYY_MM_DD);
            Date date = simpleDateFormat.parse(dt);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat(Constant.DD_MM_YYYY);
            return df.format(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return "";
    }

    private void handleSliderPhotoProduct(ArrayList<Photo> arrPhoto){
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        if(!mProduct.getThumbnail().equals("")){
            TextSliderView textSliderView = new TextSliderView(this);

            String url = MainActivity.IMAGE_URL + mProduct.getThumbnail();
            textSliderView.image(url)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            mSliderLayout.addSlider(textSliderView);
        }
        else{
            if(arrPhoto.size() == 0){
                RelativeLayout rlSlide = findViewById(R.id.rl_slide);
                rlSlide.setVisibility(View.GONE);
                this.mBtnImageView.setVisibility(View.GONE);
                this.mBtnMapView.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        }

        for(int i = 0; i < arrPhoto.size(); i++){
            TextSliderView textSliderView = new TextSliderView(this);

            String url = MainActivity.IMAGE_URL + arrPhoto.get(i).getPhotoLink();
            textSliderView.image(url)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            mSliderLayout.addSlider(textSliderView);
        }

    }

    @Override
    public void onHandleDataPostDetailSuccessful(Product product, ArrayList<Photo> arrPhoto) {

        dialogHelper.dismiss();

        this.mProduct = product;

        Intent intent = getIntent();
        boolean isSave = intent.getBooleanExtra(Constant.SAVE, false);
        if(isSave){
            this.mProduct.setIsSaved(true);
        }

        handleView();

        handleSliderPhotoProduct(arrPhoto);

        try{
            LatLng latLng = new LatLng(Double.parseDouble(mProduct.getMapLat()), Double.parseDouble(mProduct.getMapLng()));
            mMap.addMarker(new MarkerOptions().position(latLng).title(mProduct.getAddress()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
        catch (NullPointerException | NumberFormatException e){
        }
    }

    @Override
    public void onHandleDataPostDetailError(String error) {
        dialogHelper.dismiss();

        alertHelper.setCallback(this);
        alertHelper.alert(R.string.error_connect, R.string.error_connect_notification, false, R.string.ok, Constant.HANDLE_ERROR);

    }

    private void handleConfirmCall(String numberPhone){

        alertHelper.setCallback(this);
        alertHelper.alert(getResources().getString(R.string.call_phone), getResources().getString(R.string.call_phone_first)
                        + numberPhone + getResources().getString(R.string.call_phone_second),
                true, getResources().getString(R.string.ok), getResources().getString(R.string.cancel), Constant.CALL_PHONE);

    }

    private void handleConfirmSendMail(String mail){

        alertHelper.setCallback(this);
        alertHelper.alert(getResources().getString(R.string.send_mail), getResources().getString(R.string.send_mail_first)
                        + mail + getResources().getString(R.string.call_phone_second),
                true, getResources().getString(R.string.ok), getResources().getString(R.string.cancel), Constant.SEND_MAIL);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_image_view:
                this.mRlSlide.setVisibility(View.VISIBLE);
                this.mBtnImageView.setTextColor(getResources().getColor(R.color.colorWhite));
                this.mBtnMapView.setTextColor(getResources().getColor(R.color.colorGrayLight));
                break;
            case R.id.btn_map_view:
                this.mRlSlide.setVisibility(View.GONE);

                this.mBtnMapView.setTextColor(getResources().getColor(R.color.colorWhite));
                this.mBtnImageView.setTextColor(getResources().getColor(R.color.colorGrayLight));
                break;
            case R.id.imv_back:
                mMap.clear();
                finish();
                break;
            case R.id.imb_mail:
                handleConfirmSendMail(this.mTxvContactEmail.getText().toString());
                break;
            case R.id.imb_call:
                handleConfirmCall(this.mTxvContactNumberPhone.getText().toString());
                break;
            case R.id.imv_more:
            case R.id.ll_more:
                if(!mIsDescriptionExpand){
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_180_left);
                    this.mImvMore.startAnimation(animation);

                    this.mExpandableLayout.expand();
                    mIsDescriptionExpand = true;
                }
                else{
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_180_right);
                    this.mImvMore.startAnimation(animation);

                    this.mExpandableLayout.collapse();
                    mIsDescriptionExpand = false;
                }
                break;
            case R.id.imb_save:
                if(mProduct.getIsSaved()){
                    try{
                        ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.remove(mProduct.getId() + "");
                        new PresenterSavedPost(this).handleUpdateDataProductIds(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID, this);
                    }
                    catch (java.lang.NullPointerException e){
                        toastHelper.toast(R.string.error_save_data, ToastHelper.LENGTH_SHORT);
                    }

                }
                else{
                    ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.put(mProduct.getId() + "", mProduct.getId() + "");
                    new PresenterSavedPost(this).handleUpdateDataProductIds(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID, this);
                }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try{
            LatLng latLng = new LatLng(Double.parseDouble(mProduct.getMapLat()), Double.parseDouble(mProduct.getMapLng()));
            mMap.addMarker(new MarkerOptions().position(latLng).title(mProduct.getMapLng()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }
        catch (NullPointerException | NumberFormatException ignored){

        }
    }

    @Override
    protected void onDestroy() {
        mMap.clear();
        super.onDestroy();
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

    public void initPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1001);
            }
            else{
                handleCallPhone();
            }
        }
        else{
            handleCallPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1001){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                handleCallPhone();
            }
            else{
                toastHelper.toast(R.string.call_not_permission, ToastHelper.LENGTH_SHORT);
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handleCallPhone(){
        try{
            String dial = "tel:" + this.mTxvContactNumberPhone.getText().toString().trim();
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }
        catch (Exception e){
            toastHelper.toast(R.string.error_handle, ToastHelper.LENGTH_SHORT);
        }
    }

    private void handleSendMail(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("mailto:" + this.mTxvContactEmail.getText().toString()));
        intent.putExtra(Intent.EXTRA_SUBJECT, this.mTxvTitle.getText().toString());

        startActivity(Intent.createChooser(intent, getResources().getString(R.string.send_a_mail)));
    }

    @Override
    protected void onStop() {

        mSliderLayout.stopAutoCycle();
        mMap.clear();


        //clear memory
        Runtime.getRuntime().gc();
        System.gc();

        super.onStop();
    }

    @Override
    public void onHandleDataProductIdsSuccessful(HashMap<String, String> list) {

    }

    @Override
    public void onHandleDataProductIdsError(String error) {

    }

    @Override
    public void onHandleUpdateProductIdListSuccessful() {
        if(mProduct.getIsSaved()){
            toastHelper.toast(R.string.unsave_successful, ToastHelper.LENGTH_SHORT);
            mProduct.setIsSaved(false);

            if(this.mScrollY < 625){
                this.mImbSave.setImageResource(R.drawable.icon_favorite_border_white_24dp);
            }
            else{
                this.mImbSave.setImageResource(R.drawable.icon_favorite_border_black_24dp);
            }

            try {
                PostFragment.IMB_SAVE.setImageResource(R.drawable.icon_favorite_border_white_24dp);
            }
            catch (java.lang.NullPointerException ignored){}
        }
        else{
            toastHelper.toast(R.string.save_data_successful, ToastHelper.LENGTH_SHORT);
            mProduct.setIsSaved(true);
            this.mImbSave.setImageResource(R.drawable.icon_favorite_red_24dp);
            try {
                PostFragment.IMB_SAVE.setImageResource(R.drawable.icon_favorite_red_24dp);
            }
            catch (java.lang.NullPointerException ignored){}
        }

        if(this.mPosition != -1){
            MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.onChangeStatusSaveOfProduct(this.mPosition, mProduct.getIsSaved());
        }
        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();
    }

    @Override
    public void onHandleUpdateProductIdListError(String e) {
        if(mProduct.getIsSaved()){
            toastHelper.toast(R.string.unsave_failed, ToastHelper.LENGTH_SHORT);
            mProduct.setIsSaved(false);
        }
        else{
            toastHelper.toast(R.string.error_save_data, ToastHelper.LENGTH_SHORT);
            mProduct.setIsSaved(true);
        }
    }

    @Override
    public void onHandleSavedProductListSuccessful(ArrayList<Product> mArrListProduct) {

    }

    @Override
    public void onHandleSavedProductListError(String error) {

    }

    @Override
    public void onScrollChanged() {
        this.mScrollY = mScrollView.getScrollY();
        if(mScrollY < 625){
            this.mRlOption.setBackgroundResource(R.drawable.custom_for_product_detail);
            this.mImvBack.setImageResource(R.drawable.icon_arrow_left_white_24dp);
            this.mImbMail.setImageResource(R.drawable.icon_mail_outline_white_24dp);
            this.mImbCall.setImageResource(R.drawable.icon_call_white_24dp);

            if(mProduct.getIsSaved()){
                this.mImbSave.setImageResource(R.drawable.icon_favorite_red_24dp);
            }
            else{
                this.mImbSave.setImageResource(R.drawable.icon_favorite_border_white_24dp);
            }
        }
        else{
            this.mRlOption.setBackground(getResources().getDrawable(R.drawable.icon_shadow_black));
            this.mImvBack.setImageResource(R.drawable.icon_arrow_left_black_24dp);
            this.mImbMail.setImageResource(R.drawable.icon_mail_outline_black_24dp);
            this.mImbCall.setImageResource(R.drawable.icon_call_black_24dp);

            if(mProduct.getIsSaved()){
                this.mImbSave.setImageResource(R.drawable.icon_favorite_red_24dp);
            }
            else{
                this.mImbSave.setImageResource(R.drawable.icon_favorite_border_black_24dp);
            }
        }
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.HANDLE_ERROR){
            finish();
        }
        else if(option == Constant.CALL_PHONE){
            initPermission();
        }
        else if(option == Constant.SEND_MAIL){
            handleSendMail();
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
