package com.qtctek.realstate.view.new_post.map_information.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.KeyboardHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MapInformationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener, ViewHandleModelNewPost, AlertHelper.AlertHelperCallback {

    private View mView;

    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private SupportMapFragment mFrgMap;
    private GoogleMap mMap;
    private Marker mMarker;

    private Button mBtnSave;
    private Button mBtnPost;
    private android.app.Fragment mFrgSearch;
    private EditText mEdtSearch;
    public FrameLayout flSearch;
    private TextView mTxvInformation;

    private String mMapLat = "";
    private String mMapLng = "";
    private String mOption;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map_information, container, false);

        initSupportMapFragment();
        initViews();

        if (!checkGoogleService()) {
            ((NewPostActivity)getActivity()).alertHelper.setCallback(this);
            ((NewPostActivity)getActivity()).alertHelper.alert("Lỗi thiết bị","Thiết bị của bạn không hỗ trợ " +
                    "dịch vụ Google Play",false, "OK", Constant.GOOGLE_PLAY_SERVICE_NOT_FOUND);

        }

        return mView;
    }

    private void initViews(){
        this.mBtnSave = mView.findViewById(R.id.btn_save_temp);
        this.mBtnPost = mView.findViewById(R.id.btn_post);
        this.mEdtSearch = mView.findViewById(R.id.edt_search);
        this.flSearch = mView.findViewById(R.id.fl_search);
        this.mTxvInformation = mView.findViewById(R.id.txv_information);

        this.mBtnSave.setOnClickListener(this);
        this.mBtnPost.setOnClickListener(this);
        this.mEdtSearch.setOnClickListener(this);

        this.mFrgSearch = new SearchPlaceFragment();
        android.app.FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
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
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext())
                == ConnectionResult.SUCCESS;
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
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

        this.mMapLat = NewPostActivity.PRODUCT.getMapLat();
        this.mMapLng = NewPostActivity.PRODUCT.getMapLng();
        try{

            LatLng latLng = new LatLng(Double.parseDouble(mMapLat), Double.parseDouble(mMapLng));
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
        catch (Exception e){
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mTxvInformation.setVisibility(View.GONE);

                try{
                    mMarker.remove();
                }
                catch (Exception e){}

                StringBuilder stringBuilder = new StringBuilder(latLng.toString());
                stringBuilder.delete(0, 10);
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);

                String[] location = stringBuilder.toString().split(",");
                mMapLat = location[0].trim();
                mMapLng = location[1].trim();

                mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nhà bán tại đây"));
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

    public void handleNext(){
        ((NewPostActivity)getActivity()).dialogHelper.show();

        NewPostActivity.PRODUCT.setMapLat(this.mMapLat);
        NewPostActivity.PRODUCT.setMapLng(this.mMapLng);
        new PresenterNewPost(this).handleUpdateLocationProduct(
                NewPostActivity.PRODUCT.getId(),
                NewPostActivity.PRODUCT.getMapLat(),
                NewPostActivity.PRODUCT.getMapLng(),
                mOption
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imb_save:
                mMap.clear();
                mOption = "save";
                handleNext();
                break;
            case R.id.btn_post:
                mOption = "post";
                mMap.clear();
                if(this.mMapLat.equals("")){
                    ((NewPostActivity)getActivity()).toastHelper.toast("Bạn chưa chọn vị trí nhà. Không thể đăng!!!", ToastHelper.LENGTH_SHORT);
                }
                else{
                    handleNext();
                }
                break;
            case R.id.edt_search:

                SearchPlaceFragment searchPlaceFragment = (SearchPlaceFragment) mFrgSearch;
                searchPlaceFragment.edtSearch.setFocusable(true);
                searchPlaceFragment.edtSearch.setFocusableInTouchMode(true);

                ((NewPostActivity)getActivity()).keyboardHelper.showKeyboard(searchPlaceFragment.edtSearch);

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
        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        if(status){
            if(mOption.equals("save")){
                mMarker.remove();
                mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(mMapLat), Double.parseDouble(mMapLng))).title("Nhà bán tại đây"));
                ((NewPostActivity)getActivity()).toastHelper.toast("Lưu thành công", ToastHelper.LENGTH_SHORT);
            }
            else{
                mMap.clear();
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(4);
                ((NewPostActivity)getActivity()).progressBarState.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
            }
        }
        else{
            ((NewPostActivity)getActivity()).toastHelper.toast("Lỗi lưu dữ liệu!!!", ToastHelper.LENGTH_SHORT);
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
            if(checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
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
                getActivity().finishAffinity();
            }
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
