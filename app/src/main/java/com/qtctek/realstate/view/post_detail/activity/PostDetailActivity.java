package com.qtctek.realstate.view.post_detail.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.presenter.post_detail.PresenterPostDetail;
import com.qtctek.realstate.view.post_detail.interfaces.ViewHandlePostDetail;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_detail.adapter.ImageAdapter;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity implements ViewHandlePostDetail, View.OnClickListener,
    OnMapReadyCallback{

    private Button mBtnImageView;
    private Button mBtnMapView;
    private RecyclerView mRecyclerViewImages;
    private TextView mTxvPrice;
    private TextView mTxvArea;
    private TextView mTxvRooms;
    private TextView mTxvPostDate;
    private TextView mTxvDescription;
    private TextView mTxvContactName;
    private TextView mTxvContactNumberPhone;
    private TextView mTxvFloors;
    private TextView mTxvLegal;
    private TextView mTxvAddress;
    private TextView mTxvDistrictProvinceCity;
    private TextView mTxvUtility;
    private TextView mCategoryProduct;
    private Toolbar mToolbar;
    private Button mBtnBack;
    private LinearLayout mLlProfile;
    private LinearLayout mLlSavePost;
    private LinearLayout mLlCall;

    private Dialog mLoadingDialog;

    private SupportMapFragment mFrgMap;
    private GoogleMap mMap;

    private PresenterPostDetail mPresenterPostDetail;

    private PostSale mPostSale;

    private boolean mDoubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        createLoadingDialog();
        getValueFromIntent();

        initViews();
        handleStart();
        initSupportMapFragment();
        addToolbar();
    }

    private void getValueFromIntent(){
        mLoadingDialog.show();

        Intent intent = getIntent();
        int idPost = intent.getIntExtra("post_id", 0);

        this.mPresenterPostDetail = new PresenterPostDetail(this);
        this.mPresenterPostDetail.handleGetDataProductDetail(idPost);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(this);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void initViews(){
        this.mBtnImageView = findViewById(R.id.btn_image_view);
        this.mBtnMapView = findViewById(R.id.btn_map_view);
        this.mRecyclerViewImages = findViewById(R.id.recycler_images);
        this.mTxvPrice = findViewById(R.id.txv_price);
        this.mTxvArea = findViewById(R.id.txv_area);
        this.mTxvFloors = findViewById(R.id.txv_floors);
        this.mTxvLegal = findViewById(R.id.txv_legal);
        this.mTxvRooms = findViewById(R.id.txv_rooms);
        this.mTxvPostDate = findViewById(R.id.txv_post_date);
        this.mTxvDescription = findViewById(R.id.txv_description);
        this.mCategoryProduct = findViewById(R.id.txv_category_product);
        this.mTxvContactName = findViewById(R.id.txv_contact_name);
        this.mTxvContactNumberPhone = findViewById(R.id.txv_contact_number_phone);
        this.mToolbar = findViewById(R.id.toolbar);
        this.mTxvAddress = findViewById(R.id.txv_address);
        this.mTxvDistrictProvinceCity = findViewById(R.id.txv_district_province_city);
        this.mToolbar = findViewById(R.id.toolbar);
        this.mBtnBack = findViewById(R.id.btn_back);
        this.mLlCall = findViewById(R.id.ll_call);
        this.mLlProfile = findViewById(R.id.ll_profile);
        this.mLlSavePost = findViewById(R.id.ll_save_post);
        this.mTxvUtility = findViewById(R.id.txv_utility);

        this.mBtnMapView.setOnClickListener(this);
        this.mBtnImageView.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mLlSavePost.setOnClickListener(this);
        this.mLlProfile.setOnClickListener(this);
        this.mLlCall.setOnClickListener(this);
    }

    private void handleStart(){
        if(!MainActivity.ROLE_USER.equals("2")){
            this.mLlSavePost.setVisibility(View.GONE);

            LinearLayout llBottom = findViewById(R.id.ll_bottom);
            llBottom.setWeightSum(2);
        }
    }

    private void initSupportMapFragment(){
        this.mFrgMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg_map);
        this.mFrgMap.getMapAsync(this);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void handleView(PostSale postSale){

        this.mTxvPostDate.setText(formatDate(postSale.getPostDate()));
        this.mTxvDescription.setText(postSale.getProduct().getDescription());
        this.mTxvContactName.setText(postSale.getContactName());
        this.mTxvContactNumberPhone.setText(postSale.getContactNumberPhone());
        this.mTxvAddress.setText(postSale.getProduct().getAddress());

        String floor = ": " + postSale.getProduct().getProductDetail().getFloors() + "";
        this.mTxvFloors.setText(floor);
        String legal = ": " + postSale.getProduct().getProductDetail().getLegal();
        this.mTxvLegal.setText(legal);
        String utility = ": " + postSale.getProduct().getProductDetail().getUtility();
        this.mTxvUtility.setText(utility);
        String categoryProduct = ": " + postSale.getProduct().getCategoryProduct();
        this.mCategoryProduct.setText(categoryProduct);

        String temp = postSale.getProduct().getDistrict() + ", " + postSale.getProduct().getProvinceCity();
        this.mTxvDistrictProvinceCity.setText(temp);

        String strArea = postSale.getProduct().getArea() + " m²";
        this.mTxvArea.setText(strArea);

        String strRooms = postSale.getProduct().getBedrooms() + " phòng ngủ, " + postSale.getProduct().getBathrooms() + " phòng tắm.";
        this.mTxvRooms.setText(strRooms);

        String strPrice = "";
        float price = (float)postSale.getProduct().getPrice();
        if(price > 1000000000){
            price  = price / 1000000000;
            strPrice = Math.round( price * 100.0)/ 100.0 + " tỉ";
        }
        else if(price > 1000000){
            price /= 1000000;
            strPrice = Math.round( price * 100.0)/ 100.0 + " triệu";
        }
        else if(price > 1000){
            price /= 1000;
            strPrice = Math.round( price * 100.0)/ 100.0 + "K";
        }
        else{
            strPrice = "Thương lượng";
        }
        this.mTxvPrice.setText(strPrice);

    }

    private String formatDate(String dt){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date = simpleDateFormat.parse(dt);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            return df.format(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return "";
    }



    @Override
    public void onHandleDataPostDetailSuccessful(PostSale postSale, ArrayList<String> arrImages) {

        mLoadingDialog.dismiss();

        handleView(postSale);
        ImageAdapter imageAdapter = new ImageAdapter(this, arrImages);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerViewImages.setLayoutManager(linearLayoutManager);

        this.mRecyclerViewImages.setAdapter(imageAdapter);

        this.mPostSale = postSale;

        try{
            LatLng latLng = new LatLng(Double.parseDouble(mPostSale.getProduct().getLatitude()), Double.parseDouble(mPostSale.getProduct().getLongitude()));
            mMap.addMarker(new MarkerOptions().position(latLng).title(mPostSale.getProduct().getAddress()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
        catch (java.lang.NullPointerException e){
        }
        catch (java.lang.NumberFormatException e){
        }
    }

    @Override
    public void onHandleDataPostDetailError(String error) {
        mLoadingDialog.dismiss();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Có lỗi xảy ra trong quá trình đọc dữ liệu. Xin vui lòng thử lại sau!!!")
                .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }

    @Override
    public void onSavePost(String value) {
        if(value.equals("exist")){
            Toast.makeText(this, "Bạn đã lưu tin này trước đó", Toast.LENGTH_SHORT).show();
        }
        else if(value.equals("successful")){
            Toast.makeText(this, "Lưu tin thành công", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Lưu tin không thành công. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_image_view:
                this.mRecyclerViewImages.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_map_view:
                this.mRecyclerViewImages.setVisibility(View.GONE);
                break;
            case R.id.btn_back:
                mMap.clear();
                finish();
                break;
            case R.id.ll_profile:
                if(MainActivity.EMAIL_USER.equals("")){
                    Intent intent = new Intent(PostDetailActivity.this, UserActionActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(PostDetailActivity.this, UserControlActivity.class);
                    intent.putExtra("email_user", MainActivity.EMAIL_USER);
                    intent.putExtra("role", MainActivity.ROLE_USER);
                    startActivity(intent);
                }

                finish();
                break;
            case R.id.ll_save_post:
                if(MainActivity.EMAIL_USER.equals("")){
                    Toast.makeText(this, "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
                }
                else{
                    mPresenterPostDetail.handleSavePost(mPostSale.getId(), MainActivity.EMAIL_USER);
                }
                break;
            case R.id.ll_call:
                initPermission();
                break;

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        try{
            LatLng latLng = new LatLng(Double.parseDouble(mPostSale.getProduct().getLatitude()), Double.parseDouble(mPostSale.getProduct().getLongitude()));
            mMap.addMarker(new MarkerOptions().position(latLng).title(mPostSale.getProduct().getAddress()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }
        catch (java.lang.NullPointerException e){

        }
        catch (java.lang.NumberFormatException e){
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

    public void initPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                //permission don't granted
                if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                    Toast.makeText(this, "Bạn không cung cấp quyền gọi điện thoại cho ứng dụng", Toast.LENGTH_SHORT).show();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1001);
                }
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
                Toast.makeText(this, "Không cung cấp quyền gọi điện thoại cho ứng dụng", Toast.LENGTH_SHORT);
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
            Toast.makeText(this, "Có lỗi xảy ra trong việc thực hiện cuộc gọi. Vui lòng thử lại sau!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
