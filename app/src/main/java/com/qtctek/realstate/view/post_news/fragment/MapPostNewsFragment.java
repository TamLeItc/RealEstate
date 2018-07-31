package com.qtctek.realstate.view.post_news.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.ui.IconGenerator;
import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.network.NetworkConnection;
import com.qtctek.realstate.dto.Condition;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.presenter.post_news.PresenterPostNews;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_news.interfaces.ViewHandlePostNews;
import com.qtctek.realstate.view.post_news.interfaces.OnEventForMapPostNews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MapPostNewsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnEventForMapPostNews, ViewHandlePostNews, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraIdleListener, AlertHelper.AlertHelperCallback {

    private View mView;

    private final long mUpdateInterval = 5000;
    private final long mFastestInterval = 5000;

    public static OnEventForMapPostNews ON_EVENT_FROM_ACTIVITY;
    public static int POSITION;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private SupportMapFragment mFrgMap;
    public GoogleMap mMap;

    private TextView mTxvInformation;
    private TextView mTxvQualityPost;
    private LinearLayout mLlQualityPost;

    private PresenterPostNews mPresenterPostNews = new PresenterPostNews(this);

    public ArrayList<Product> arrProduct = new ArrayList<>();

    public Marker lastMarkerClicked = null;
    public Product lastProductClicked = null;

    private int mMinZoom = 10;
    private int mSecondZoom = 15;
    private int mZoom = 16;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map_post_news, container, false);

        ON_EVENT_FROM_ACTIVITY = this;

        initViews();
        initSupportMapFragment();

        if (!checkGoogleService()) {

            ((MainActivity) Objects.requireNonNull(getActivity())).alertHelper.setCallback(this);
            ((MainActivity) Objects.requireNonNull(getActivity())).alertHelper.alert("Lỗi thiết bị","Thiết bị của bạn không hỗ trợ " +
                    "dịch vụ Google Play",false, "OK", Constant.GOOGLE_PLAY_SERVICE_NOT_FOUND);

        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (NetworkConnection.isNetworkConnected(Objects.requireNonNull(getContext()))) {
                        ((MainActivity) Objects.requireNonNull(getActivity())).lastSearch();
                    }
                }
            }, 2500);
        }

        return mView;
    }

    private void initSupportMapFragment() {
        this.mFrgMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg_map);
        this.mFrgMap.getMapAsync(this);
    }

    private void initViews() {
        this.mTxvInformation = mView.findViewById(R.id.txv_information);
        this.mTxvQualityPost = mView.findViewById(R.id.txv_quality_post);
        this.mLlQualityPost = mView.findViewById(R.id.ll_quality_post);
    }

    //Kiểm tra máy có hỗ trợ dịch vụ Google không
    private boolean checkGoogleService() {
        if (isPlayServicesAvailable()) {
            setUpLocationClientIfNeeded();
            buildLocationRequest();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext())
                == ConnectionResult.SUCCESS;
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
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
        LocationManager manager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(LOCATION_SERVICE);
        assert manager != null;
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
    public void onDestroy() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        mMap.clear();
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);

        try {
            mMap.setMyLocationEnabled(true);
        } catch (java.lang.SecurityException e) {
            initPermission();
        }
    }

    public void searchNearby() {
        ((MainActivity) Objects.requireNonNull(getActivity())).alertHelper.setCallback(this);
        ((MainActivity) getActivity()).alertHelper.alert("Tìm kiếm", "Bạn có muốn tìm kiếm gần" +
                        " vị trí hiện tại không", false, "OK", "Hủy bỏ",
                Constant.NEAR_BY_SEARCH);
    }

    @Override
    public void handlePostListSuccessful(ArrayList<Product> arrPost) {
        arrProduct.clear();
        if (arrPost.size() > 0) {
            ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.GONE);
            this.mTxvInformation.setVisibility(View.GONE);
        } else {
            ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.VISIBLE);
            ListPostNewsFragment.TXV_INFORMATION.setText(getActivity().getResources().getText(R.string.no_data_here));

            this.mTxvInformation.setVisibility(View.VISIBLE);
            this.mTxvInformation.setText(getActivity().getResources().getText(R.string.no_data_here));

            mMap.clear();
        }

        this.mLlQualityPost.setVisibility(View.VISIBLE);
        this.mTxvQualityPost.setText(arrPost.size() + "");

        handlePostList(arrPost);
    }

    public void resetLastClick() {
        this.lastMarkerClicked = null;
        this.lastProductClicked = null;
    }


    @Override
    public void handlePostListError(String error) {
        arrProduct.clear();

        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();

        this.mLlQualityPost.setVisibility(View.GONE);
        ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.VISIBLE);
        ListPostNewsFragment.TXV_INFORMATION.setText(getActivity().getResources().getText(R.string.load_data_error));

        this.mTxvInformation.setVisibility(View.VISIBLE);
        this.mTxvInformation.setText(getActivity().getResources().getText(R.string.load_data_error));
    }

    @Override
    public void handleQualityPostSuccessful(int quality) {
        arrProduct.clear();
        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();

        ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.VISIBLE);
        this.mTxvInformation.setVisibility(View.VISIBLE);
        if (quality == 0) {
            ListPostNewsFragment.TXV_INFORMATION.setText(getResources().getString(R.string.no_data));
            this.mTxvInformation.setText(getResources().getString(R.string.no_data));
        } else {
            ListPostNewsFragment.TXV_INFORMATION.setText(getResources().getString(R.string.please_zoom_to_view_list));
            this.mTxvInformation.setText(getResources().getString(R.string.please_zoom_to_view_list));
        }

        this.mLlQualityPost.setVisibility(View.VISIBLE);
        this.mTxvQualityPost.setText(quality + "");
    }

    @Override
    public void handleQualityPostError(String error) {
        arrProduct.clear();

        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();

        this.mLlQualityPost.setVisibility(View.GONE);

        ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.VISIBLE);
        ListPostNewsFragment.TXV_INFORMATION.setText(getActivity().getResources().getText(R.string.load_data_error));

        this.mTxvInformation.setVisibility(View.VISIBLE);
        this.mTxvInformation.setText(getActivity().getResources().getText(R.string.load_data_error));
    }


    public void handlePostList(ArrayList<Product> arrPost) {
        ArrayList<Product> arrayList = new ArrayList<>();
        for (int i = 0; i < arrPost.size(); i++) {
            arrayList.add(arrPost.get(i));
            if (i == arrProduct.size() - 1) {
                arrProduct.clear();
            }
        }

        this.arrProduct.addAll(arrayList);
        setSavedForList(arrProduct);
        ((MainActivity) Objects.requireNonNull(getActivity())).handleSort();
        loadMarker();

        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();
    }

    public synchronized void loadMarker(){
        mMap.clear();

        for (int i = 0; i < arrProduct.size(); i++) {

            Product product = arrProduct.get(i);

            if(lastProductClicked != null && product.getId() == lastProductClicked.getId()){
                lastProductClicked = product;
            }
            else{
                LatLng latLng = new LatLng(Double.parseDouble(product.getMapLat()),
                        Double.parseDouble(product.getMapLng()));

                String strShortPrice = AppUtils.getStringPrice(product.getPrice(), AppUtils.SHORT_PRICE);

                if (arrProduct.get(i).getFormality().equals("no")) {
                    createMarkerProduct(latLng, i, R.color.colorGreen, strShortPrice);
                } else {
                    createMarkerProduct(latLng, i, R.color.colorMain, strShortPrice);
                }
            }
        }

        handleMarkerClicked(lastMarkerClicked, lastProductClicked);
    }

    private synchronized void setSavedForList(ArrayList<Product> arrProduct) {
        for (int i = 0; i < arrProduct.size(); i++) {
            for (String key : ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.keySet()) {
                if ((arrProduct.get(i).getId() + "").equals(key)) {
                    arrProduct.get(i).setIsSaved(true);
                    break;
                }
            }
        }
    }

    private boolean isNew(String strDateUpload){
        String strToday = AppUtils.getCurrentDate();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date toDate;
        Date dateUpload;
        try {
            toDate = simpleDateFormat.parse(strToday);
            dateUpload = simpleDateFormat.parse(strDateUpload);

            return toDate.equals(dateUpload);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createMarkerProduct(LatLng latLng, int positionInArr, @ColorRes int color, String infor) {
        if(positionInArr > arrProduct.size() - 1 || positionInArr < 0){
            return;
        }

        IconGenerator iconGenerator = getIconMarker(infor, arrProduct.get(positionInArr), color);

        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .snippet(positionInArr + "")
                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
                .anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()));
    }

    private IconGenerator getIconMarker(String info, Product product, @ColorRes int color){
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_post_map, null);
        ImageView imvFavorite = view.findViewById(R.id.imv_favorite);
        TextView txvPrice = view.findViewById(R.id.txv_price);

        txvPrice.setText(info);

        if(product.getIsSaved()){
            imvFavorite.setVisibility(View.VISIBLE);
        }
        else{
            if(isNew(product.getDateUpload())){
                imvFavorite.setVisibility(View.VISIBLE);
                imvFavorite.setImageResource(R.drawable.icon_new_border_red_24dp);
            }
            else{
                imvFavorite.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(5, 0, 5, 0);
                txvPrice.setLayoutParams(layoutParams);
            }
        }

        IconGenerator iconFactory = new IconGenerator(getContext());
        iconFactory.setContentView(view);
        iconFactory.setColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), color));
        iconFactory.setTextAppearance(R.style.iconGenText);
        iconFactory.setContentPadding(0, 0, 0, 0);

        return iconFactory;
    }

    private void handleGetData(String option) {
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        initSupportMapFragment();

        MainActivity main = ((MainActivity) getActivity());
        assert main != null;
        mPresenterPostNews.handleGetPostList(option, main.bathroom, main.bedroom, main.minPrice + "",
                main.maxPrice + "", main.formality, main.architecture, main.type, farRight, nearRight,
                farLeft, nearLeft);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            POSITION = Integer.parseInt(marker.getSnippet());
        }
        catch (Exception ignored){}

        ((MainActivity) Objects.requireNonNull(getActivity())).disPlayPostItemFragment();

        try {

            handleMarkerClicked(marker, arrProduct.get(POSITION));

        } catch (java.lang.IndexOutOfBoundsException e) {

            ((MainActivity)getActivity()).toastHelper.toast("Lỗi xử lí", ToastHelper.LENGTH_SHORT);
        }

        return true;
    }

    private void handleMarkerClicked(Marker marker, Product product) {
            if(lastMarkerClicked == null || lastProductClicked == null){
                lastMarkerClicked = marker;
                lastProductClicked = product;
            }
            else{
                int color;
                if (lastProductClicked.getFormality().equals("no")) {
                    color = R.color.colorGreen;
                } else {
                    color = R.color.colorMain;
                }
                String strShortPrice = AppUtils.getStringPrice(lastProductClicked.getPrice(), AppUtils.SHORT_PRICE);
                IconGenerator iconGenerator = getIconMarker(strShortPrice, lastProductClicked, color);

                try {
                    lastMarkerClicked.setIcon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()));
                }
                catch (Exception ignored){ }
            }

            //marker clicked
            if(marker != null && product != null){
                String strShortPrice = AppUtils.getStringPrice(product.getPrice(), AppUtils.SHORT_PRICE);
                IconGenerator iconGenerator = getIconMarker(strShortPrice, product, R.color.colorMainLight);

                lastProductClicked = product;
                try {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()));
                    lastMarkerClicked = marker;
                }
                catch (Exception e){
                    int position = findLocationProduct(product.getId());
                    lastMarkerClicked = mMap.addMarker(new MarkerOptions()
                            .position(marker.getPosition())
                            .snippet(position + "")
                            .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
                            .anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()));

                    if(position == arrProduct.size()){
                        arrProduct.add(position, product);
                    }
                }
            }
    }

    private int findLocationProduct(int idProduct){
        for(int i = 0; i < arrProduct.size(); i++){
            if(arrProduct.get(i).getId() == idProduct){
                return i;
            }
        }
        return arrProduct.size();
    }

    @Override
    public void onCameraIdle() {

        ((MainActivity) Objects.requireNonNull(getActivity())).mapLat = mMap.getCameraPosition().target.latitude;
        ((MainActivity)getActivity()).mapLng = mMap.getCameraPosition().target.longitude;

        if (mMap.getCameraPosition().zoom <= mMinZoom) {

            mMap.clear();
            arrProduct.clear();

            this.mLlQualityPost.setVisibility(View.GONE);
            this.mTxvInformation.setVisibility(View.VISIBLE);
            this.mTxvInformation.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.please_zoom_to_view));

            ListPostNewsFragment.TXV_INFORMATION.setVisibility(View.VISIBLE);
            ListPostNewsFragment.TXV_INFORMATION.setText(getContext().getResources().getString(R.string.please_zoom_to_view));

            return;

        }

        String text = "Đang tải...";
        this.mLlQualityPost.setVisibility(View.GONE);
        this.mTxvInformation.setVisibility(View.VISIBLE);
        this.mTxvInformation.setText(text);

        if (mMap.getCameraPosition().zoom >= mSecondZoom) {
            handleGetData("list");
        } else {
            mMap.clear();
            arrProduct.clear();

            handleGetData("count");
        }
    }

    @Override
    public void onDataFilter() {
        if (mMap.getCameraPosition().zoom >= mMinZoom) {
            handleGetData("list");
        } else {
            handleGetData("count");
        }
    }


    @Override
    public void onPlaceSelected(String address, LatLngBounds latLngs, Double lat, Double lng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), mZoom));

        ((MainActivity) Objects.requireNonNull(getActivity())).edtSearch.setText(address);


//        try {
//            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
//            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
//
//            if(addresses.size() > 0){
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public void onChangeStatusSaveOfProduct(int position, boolean status) {
        this.arrProduct.get(position).setIsSaved(status);

        handleMarkerClicked(lastMarkerClicked, lastProductClicked);

    }

    @Override
    public void onNearBySearchForSale() {

        if (!isGpsOn()) {
            alertGPSIsOff();
            return;
        }

        try {
            ((MainActivity) Objects.requireNonNull(getActivity())).formality = "yes";

            initPermission();
        } catch (java.lang.NullPointerException e) {
            ((MainActivity)getActivity()).toastHelper.toast("Lỗi xử lí", ToastHelper.LENGTH_SHORT);
        }
    }

    @Override
    public void onNearBySearchForRent() {

        if (!isGpsOn()) {
            alertGPSIsOff();
            return;
        }
        try {

            ((MainActivity) Objects.requireNonNull(getActivity())).formality = "no";

            initPermission();

        } catch (java.lang.NullPointerException e) {
            ((MainActivity)getActivity()).toastHelper.toast("Lỗi xử lí", ToastHelper.LENGTH_SHORT);
        }
    }

    @Override
    public void onHandleSearch(Condition condition) {

        MainActivity main = ((MainActivity) getActivity());

        assert main != null;
        main.formality = condition.getFormality();
        main.type = condition.getType();
        main.architecture = condition.getArchitecture();
        main.minPrice = condition.getMinPrice();
        main.maxPrice = condition.getMaxPrice();
        main.bedroom = condition.getBedroom();
        main.bathroom = condition.getBathroom();

        lastProductClicked = null;
        lastMarkerClicked = null;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(condition.getMapLat(), condition.getMapLng()), condition.getZoom()));
    }

    private void alertGPSIsOff() {
        ((MainActivity) Objects.requireNonNull(getActivity())).alertHelper.alert("Lỗi", "Bạn chưa bận GPS. Vui lòng bật GPS để sử dụng chức năng này",
                false, "Cài đặt", "Hủy bỏ", Constant.GPS_IS_OFF);
    }

    public void initPermission() {
        String[] permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permission, AppUtils.REQUEST_CODE_PERMISSION);
            } else {
                moveCameraToMyLocation();
            }
        } else {
            moveCameraToMyLocation();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppUtils.REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                moveCameraToMyLocation();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void moveCameraToMyLocation() {

        if (!isGpsOn()) {
            alertGPSIsOff();
            return;
        }

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), mZoom));
    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.NEAR_BY_SEARCH){
            if(!isGpsOn()){
                GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
                    public void onGpsStatusChanged(int event) {
                        if( event == GpsStatus.GPS_EVENT_FIRST_FIX){
                            initPermission();
                        }
                    }
                };

                alertGPSIsOff();
            }
            else{
                initPermission();
            }
        }
        else if(option == Constant.GOOGLE_PLAY_SERVICE_NOT_FOUND){
            Objects.requireNonNull(getActivity()).finishAffinity();
        }
        else if(option == Constant.GPS_IS_OFF){
            String locationProviders = Settings.Secure.getString(Objects.requireNonNull(getActivity()).getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (locationProviders == null || locationProviders.equals("")) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
                ((MainActivity)getActivity()).toastHelper.toast("Không thể mở cài đặt", ToastHelper.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }
}
