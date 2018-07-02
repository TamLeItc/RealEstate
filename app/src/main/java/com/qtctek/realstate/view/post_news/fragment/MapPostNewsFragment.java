package com.qtctek.realstate.view.post_news.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.ui.IconGenerator;
import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.presenter.post_news.PresenterPostNews;
import com.qtctek.realstate.view.post_news.interfaces.ViewHandlePostNews;
import com.qtctek.realstate.view.post_news.dialog.PostDialog;
import com.qtctek.realstate.view.post_news.interfaces.OnEventFromActivity;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;

public class MapPostNewsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks, OnEventFromActivity,
        ViewHandlePostNews, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {

    private View mView;

    private final String mTag = NewPostActivity.class.getSimpleName();
    private final long mUpdateInterval = 5000;
    private final long mFastestInterval = 5000;
    public static ArrayList<PostSale> ARR_POST = new ArrayList<>();
    public static OnEventFromActivity ON_RESULT_FROM_ACTIVITY;

    private int mPosition;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private boolean mIsAutoUpdateLocation = true;
    private boolean mIsMoveCamera = false;
    private boolean mIsUpdateLocation = false;

    private SupportMapFragment mFrgMap;
    private GoogleMap mMap;

    private TextView mTxvInformation;

    private PresenterPostNews mPresenterPostNews = new PresenterPostNews(this);

    private ArrayList<Marker> mArrMarket = new ArrayList<>();

    private int mMinZoom = 13;
    private String mMinPrice = "0";
    private String mMaxPrice = "1000000000000000";
    private String mCategoryProduct = "%";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map_post_news, container, false);

        initViews();
        initSupportMapFragment();

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
                        viewPager.setCurrentItem(6);
                    }
                }
            });
        }

        ON_RESULT_FROM_ACTIVITY = this;

        return mView;
    }

    private void initSupportMapFragment(){
        this.mFrgMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg_map);
        this.mFrgMap.getMapAsync(this);
    }

    private void initViews(){
        this.mTxvInformation = mView.findViewById(R.id.txv_information);
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
        mLocationRequest.setInterval(mUpdateInterval);
        mLocationRequest.setFastestInterval(mFastestInterval);
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
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

            if(!this.mIsMoveCamera){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                this.mIsMoveCamera = true;
            }
        }
    }

    //Thiết lập việc cập nhật vị trí tự động
    protected void startLocationUpdates() {

        //Kiểm tra xem có quyền cập quyền truy cập vị trí hiện tại hay không
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Cập nhật vị trí tự động
        if(!mIsUpdateLocation){
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                    mIsUpdateLocation = true;
                }
            });
        }
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            mLastLocation = lastLocation;
            startLocationUpdates();
        }

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

        mLastLocation = location;

        //Update lại ví trí tự động
        if (mIsAutoUpdateLocation) {
            updateUi();
        }

    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        mMap.clear();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);

        //Kiểm tra có quyền truy cập vị trí chưa
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Kiểm tra GPS có bật chưa
            if(!isGpsOn()){
                Toast.makeText(getContext(), "GPS is off", Toast.LENGTH_SHORT).show();
                LatLng position = new LatLng(10.811376, 106.619471);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
            }

            return;
        }


        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void handlePostListSuccessful(ArrayList<PostSale> arrPost) {
        if(arrPost.size() > 0){
            ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.GONE);
            this.mTxvInformation.setVisibility(View.GONE);
        }
        else{
            ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.VISIBLE);
            ListPostNewsFragment.TXV_INFORMATION.setText(getActivity().getResources().getText(R.string.no_data_here));

            this.mTxvInformation.setVisibility(View.VISIBLE);
            this.mTxvInformation.setText(getActivity().getResources().getText(R.string.no_data_here));
        }
        handlePostList(arrPost);
    }

    @Override
    public void handlePostListError(String error) {
        Toast.makeText(getContext(), "Có vấn đề trong việc tải dữ liệu. Xin vui lòng thử lại sau!!!", Toast.LENGTH_SHORT).show();
        ARR_POST.clear();
        ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.VISIBLE);
        ListPostNewsFragment.TXV_INFORMATION.setText(getActivity().getResources().getText(R.string.load_data_error));

        this.mTxvInformation.setVisibility(View.VISIBLE);
        this.mTxvInformation.setText(getActivity().getResources().getText(R.string.load_data_error));
    }


    private void handlePostList(ArrayList<PostSale> arrPost){
        for(int i = 0; i < mArrMarket.size(); i++){
            Marker marker = mArrMarket.get(i);
            marker.remove();
        }

        this.ARR_POST.addAll(arrPost);
        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();

        this.mArrMarket.clear();
        for(int i = 0; i < arrPost.size(); i++){

            PostSale postSale = ARR_POST.get(i);

            LatLng latLng = new LatLng(Double.parseDouble(postSale.getProduct().getLatitude()),
                    Double.parseDouble(postSale.getProduct().getLongitude()));

            String strShortPrice = getStringShortPrice(postSale.getProduct().getPrice());

            IconGenerator iconFactory = new IconGenerator(getContext());
            iconFactory.setColor(getResources().getColor(R.color.colorMain));
            iconFactory.setTextAppearance(R.style.iconGenText);
            iconFactory.setContentPadding(3, 1, 3, 1);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .snippet(i + "")
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(strShortPrice)))
                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV()));

            this.mArrMarket.add(marker);
        }
    }

     private String getStringShortPrice(Long price){
        float fPrice;
        if(price > 1000000000){
            fPrice = (float)price / 1000000000;
            return Math.round( fPrice * 100.0)/ 100.0 + " tỉ";
        }
        else if(price > 1000000){
            fPrice = (float)price / 1000000;
            return  Math.round( fPrice * 100.0)/ 100.0 + " tr";
        }
        else if(price > 1000){
            fPrice = (float)price / 1000;
            return Math.round( fPrice * 100.0)/ 100.0 + " K";
        }
        else{
            return "TL";
        }
    }

    private void handleGetData(){

        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        initSupportMapFragment();
        mPresenterPostNews.handleGetPostList(mMinPrice, mMaxPrice, mCategoryProduct, farRight, nearRight,
                farLeft, nearLeft);
    }

    private void createDialogPost(Marker marker){

        mPosition = Integer.parseInt(marker.getSnippet());
        Intent intent = new Intent(getActivity(), PostDialog.class);
        intent.putExtra("position", mPosition);
        startActivity(intent);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        createDialogPost(marker);

        return false;
    }


    @Override
    public void onCameraIdle() {
        this.ARR_POST.clear();

        if(mMap.getCameraPosition().zoom >= mMinZoom){
            handleGetData();
            String text = "Đang tải...";
            this.mTxvInformation.setVisibility(View.VISIBLE);
            this.mTxvInformation.setText(text);
        }
        else{
            this.mTxvInformation.setVisibility(View.VISIBLE);
            this.mTxvInformation.setText(getActivity().getResources().getText(R.string.please_zoom));
            for(int i = 0; i < this.mArrMarket.size(); i++){
                this.mArrMarket.get(i).remove();
            }
        }
    }

    @Override
    public void onDataSearch(String lat, String lng, String minPrice, String maxPrice, String categoryProduct) {
        this.mMinPrice = minPrice;
        this.mMaxPrice = maxPrice;
        this.mCategoryProduct = categoryProduct;

        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    public void onReloadMarker() {
        handlePostList(ARR_POST);
    }
}
