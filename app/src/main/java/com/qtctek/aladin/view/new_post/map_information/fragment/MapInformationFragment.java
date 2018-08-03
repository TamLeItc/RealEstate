package com.qtctek.aladin.view.new_post.map_information.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qtctek.aladin.R;
import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.common.general.Constant;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.presenter.new_post.PresenterNewPost;
import com.qtctek.aladin.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MapInformationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener, ViewHandleModelNewPost, AlertHelper.AlertHelperCallback {

    private NewPostActivity mActivity;

    private View mView;

    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private SupportMapFragment mFrgMap;
    private GoogleMap mMap;
    private Marker mMarker;

    private Button mBtnPost;
    private android.app.Fragment mFrgSearch;
    private EditText mEdtSearch;
    public FrameLayout flSearch;

    private String mMapLat = "";
    private String mMapLng = "";
    public boolean isSaveTemp;
    private boolean mIsEditedLocation = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map_information, container, false);

        this.mActivity = (NewPostActivity) getActivity();

        initSupportMapFragment();
        initViews();

        if (!checkGoogleService()) {
            mActivity.getAlertHelper().setCallback(this);
            mActivity.getAlertHelper().alert(R.string.error, R.string.not_support_google_service,false,
                    R.string.ok, Constant.GOOGLE_PLAY_SERVICE_NOT_FOUND);

        }

        return mView;
    }

    private void initViews(){
        this.mBtnPost = mView.findViewById(R.id.btn_post);
        this.mEdtSearch = mView.findViewById(R.id.edt_search);
        this.flSearch = mView.findViewById(R.id.fl_search);

        this.mBtnPost.setOnClickListener(this);
        this.mEdtSearch.setOnClickListener(this);

        this.mFrgSearch = new SearchPlaceFragment();
        android.app.FragmentTransaction fragmentTransaction = mActivity.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_search, mFrgSearch);
        fragmentTransaction.commit();

    }

    private void initSupportMapFragment(){
        this.mFrgMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg_map);
        this.mFrgMap.getMapAsync(this);
    }

    //Kiểm tra máy có hỗ trợ dịch vụ Google không
    private boolean checkGoogleService(){
        if(isPlayServicesAvailable()){
            setUpLocationClientIfNeeded();
            buildLocationRequest();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isPlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mActivity)
                == ConnectionResult.SUCCESS;
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void buildLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroyView() {
        if (mGoogleApiClient != null
                && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        mMap.clear();

        //clear memory
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mActivity, R.raw.map_style_json));

        this.mMapLat = mActivity.product.getMapLat();
        this.mMapLng = mActivity.product.getMapLng();
        try{

            LatLng latLng = new LatLng(Double.parseDouble(mMapLat), Double.parseDouble(mMapLng));
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
        catch (Exception ignored){
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                try{
                    mMarker.remove();
                }
                catch (Exception ignored){}

                StringBuilder stringBuilder = new StringBuilder(latLng.toString());
                stringBuilder.delete(0, 10);
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);

                String[] location = stringBuilder.toString().split(",");
                mMapLat = location[0].trim();
                mMapLng = location[1].trim();

                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));

                mIsEditedLocation = true;
            }
        });

        initPermission();
    }

    @SuppressLint("MissingPermission")
    private void setButtonMyLocation(){

        mMap.setMyLocationEnabled(true);

    }

    public void moveCameraToLocationClick(Double mapLat, Double mapLng){

        this.flSearch.setVisibility(View.GONE);

        this.mMapLat = mapLat + "";
        this.mMapLat = mapLng + "";

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapLat, mapLng), 16));
    }

    public boolean checkSavedInformation(){
        if(mIsEditedLocation){
            return false;
        }
        else{
            return true;
        }
    }

    public void handleSaveMapInformation(){

        if(!mIsEditedLocation && (isSaveTemp || (!isSaveTemp && mActivity.product.getStatus() == "3"))){
            return;
        }

        String option = "post";
        if(isSaveTemp){
            option = "save";
        }

        mActivity.getDialogHelper().show();

        mActivity.product.setMapLat(this.mMapLat);
        mActivity.product.setMapLng(this.mMapLng);
        new PresenterNewPost(this).handleUpdateLocationProduct(
                mActivity.product.getId(),
                mActivity.product.getMapLat(),
                mActivity.product.getMapLng(),
                option
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                isSaveTemp = false;
                mMap.clear();
                if(this.mMapLat.equals("")){
                    mActivity.getToastHelper().toast(R.string.not_location_selected_can_not_save, ToastHelper.LENGTH_SHORT);
                }
                else{
                    handleSaveMapInformation();
                }
                break;
            case R.id.edt_search:

                SearchPlaceFragment searchPlaceFragment = (SearchPlaceFragment) mFrgSearch;
                searchPlaceFragment.edtSearch.setFocusable(true);
                searchPlaceFragment.edtSearch.setFocusableInTouchMode(true);

                mActivity.getKeyboardHelper().showKeyboard(searchPlaceFragment.edtSearch);

                flSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInsertBlankPost(boolean status, int postId) {

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
        mActivity.getDialogHelper().dismiss();
        if(status){
            if(isSaveTemp){
                mMarker.remove();
                mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(mMapLat), Double.parseDouble(mMapLng))).title("Nhà bán tại đây"));
                mActivity.getToastHelper().toast(R.string.save_data_successful, ToastHelper.LENGTH_SHORT);
            }
            else{
                mMap.clear();
                ViewPager viewPager = mActivity.findViewById(R.id.view_pager);
                viewPager.setCurrentItem(4);
            }
            mIsEditedLocation = false;
        }
        else{
            mActivity.getToastHelper().toast(R.string.error_save_data, ToastHelper.LENGTH_SHORT);
        }
    }

    @Override
    public void onDeleteFile(boolean status) {

    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }

    private void initPermission(){
        String[] permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(permission, AppUtils.REQUEST_CODE_PERMISSION);
            }
            else{
                setButtonMyLocation();
            }
        }
        else{
            setButtonMyLocation();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppUtils.REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setButtonMyLocation();
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.GOOGLE_PLAY_SERVICE_NOT_FOUND){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mActivity.finishAffinity();
            }
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
