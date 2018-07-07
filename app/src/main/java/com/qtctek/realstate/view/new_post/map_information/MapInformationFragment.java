package com.qtctek.realstate.view.new_post.map_information;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.qtctek.realstate.R;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MapInformationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener, ViewHandleModelNewPost {

    private View mView;

    public static final String TAG = NewPostActivity.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private boolean mIsAutoUpdateLocation = true;
    private boolean mIsMoveCamera = false;

    private SupportMapFragment mFrgMap;
    private GoogleMap mMap;
    private Marker mMarker;

    private Button mBtnNext;
    private Button mBtnSaveContinue;

    private String mLat = "";
    private String mLng = "";
    private Dialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map_information, container, false);

        createLoadingDialog();
        initSupportMapFragment();
        initViews();
        initPermission();

        if(!checkGoogleService()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Device does not support Google Play services");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getActivity().finishAffinity();
                    }
                    else{
                        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                        viewPager.setCurrentItem(4);
                    }
                }
            });
        }

        return mView;
    }

    private void initViews(){
        this.mBtnNext = mView.findViewById(R.id.btn_next_to);
        this.mBtnSaveContinue = mView.findViewById(R.id.btn_save_continue);

        this.mBtnNext.setOnClickListener(this);
        this.mBtnSaveContinue.setOnClickListener(this);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(getContext());
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
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

    //Kiểm tra xem GPS có đang bậc hay không
    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void updateUi() {
        //Kiểm tra GPS có bật chưa
        if(!isGpsOn()){
            Toast.makeText(getContext(), "GPS is off", Toast.LENGTH_SHORT).show();
        }

        //Kiểm tra xem có quyền cập quyền truy cập vị trí hiện tại hay không
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Tiến hành cập nhật lại vị trí hiện tại
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            mLastLocation = lastLocation;
        }

        if (mLastLocation != null) {
            //Khởi tạo nên một vị trí mới
            LatLng position = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());


            //Duy chuyển tới vị trí hiện tại
            if(!mIsMoveCamera){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 11));
                this.mIsMoveCamera = true;
            }
        }
    }

    //Thiết lập việc cập nhật vị trí tự động
    protected void startLocationUpdates() {

        //Kiểm tra xem có quyền cập quyền truy cập vị trí hiện tại hay không
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Cập nhật vị trí tự động
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    //Tắt tự động cập nhật ví trí
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            mLastLocation = lastLocation;
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, String.format(Locale.getDefault(), "onLocationChanged : %f, %f",
                location.getLatitude(), location.getLongitude()));

        mLastLocation = location;
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null
                && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        mMap.clear();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        this.mLat = NewPostActivity.PRODUCT.getMapLat();
        this.mLng = NewPostActivity.PRODUCT.getMapLng();
        try{

            LatLng latLng = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng));
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
        catch (Exception e){
        }

        //Kiểm tra có quyền truy cập vị trí chưa
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Kiểm tra GPS có bật chưa
            if(!isGpsOn()){
                Toast.makeText(getContext(), "GPS is off", Toast.LENGTH_SHORT).show();
            }

            return;
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                try{
                    mMarker.remove();
                }
                catch (Exception e){}

                StringBuilder stringBuilder = new StringBuilder(latLng.toString());
                stringBuilder.delete(0, 10);
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);

                String[] location = stringBuilder.toString().split(",");
                mLat = location[0].trim();
                mLng = location[1].trim();

                mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nhà bán tại đây"));
            }
        });

        mMap.setMyLocationEnabled(true);
    }

    public void handleNext(){
        mLoadingDialog.show();
        NewPostActivity.PRODUCT.setMapLat(this.mLat);
        NewPostActivity.PRODUCT.setMapLng(this.mLng);
        new PresenterNewPost(this).handleUpdateLocationProduct(
                NewPostActivity.PRODUCT.getId(),
                NewPostActivity.PRODUCT.getMapLat(),
                NewPostActivity.PRODUCT.getMapLng()
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next_to:
                mMap.clear();
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(4);
                break;
            case R.id.btn_save_continue:
                if(this.mLat.equals("")){
                    Toast.makeText(getContext(), "Bạn chưa chọn vị trí nhà", Toast.LENGTH_SHORT).show();
                }
                else{
                    handleNext();
                }
                break;
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
        mLoadingDialog.dismiss();
        if(status){
            mMap.clear();
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(4);
        }
        else{
            Toast.makeText(getContext(), "Có lỗi xảy ra trong việc lưu dữ liệu", Toast.LENGTH_SHORT).show();
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
                if(!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    requestPermissions(permission, 11);
                }
            }
        }
    }
}
