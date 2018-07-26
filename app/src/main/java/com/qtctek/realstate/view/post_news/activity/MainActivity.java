package com.qtctek.realstate.view.post_news.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.qtctek.realstate.Environments;
import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.helper.AlertHelper;
import com.qtctek.realstate.helper.DialogHelper;
import com.qtctek.realstate.helper.KeyboardHelper;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.network.receiver.NetworkConnectionReceiver;
import com.qtctek.realstate.dto.Condition;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.presenter.user_control.save_search.PresenterSavedSearch;
import com.qtctek.realstate.view.post_news.adapter.MainAdapter;
import com.qtctek.realstate.view.post_news.adapter.SortAdapter;
import com.qtctek.realstate.view.post_news.dialog.SearchFilterDialog;
import com.qtctek.realstate.view.post_news.fragment.SearchPlaceFragment;
import com.qtctek.realstate.view.post_news.fragment.PostFragment;
import com.qtctek.realstate.view.post_news.fragment.StartFragment;
import com.qtctek.realstate.view.post_news.interfaces.OnUserLogin;
import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.realstate.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;
import com.qtctek.realstate.view.user_control.saved_search.ViewHandleSavedSearch;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener, View.OnClickListener,
        OnUserLogin, ViewHandleSavedSearch, AlertHelper.AlertHelperCallback {

    public static String WEB_SERVER;

    public static User USER = new User();

    public static OnUserLogin ON_USER_LOGIN;

    private PostFragment mPostFragment;

    public ViewPager viewPaper;
    private Toolbar mToolbar;
    private LinearLayout mLlProfile;
    private LinearLayout mLlSort;
    private LinearLayout mLlViewMode;
    private LinearLayout mLlSaveSearch;
    private ImageView mImvViewMode;
    private TextView mTxvViewMode;
    public EditText edtSearch;
    public ExpandableLayout expandableLayoutProduct;
    public ExpandableLayout expandableLayoutSearch;
    private Fragment frgSearchPlace;

    public static Dialog NETWORK_CONNECTION_DIALOG;
    public AlertHelper alertHelper;
    public ToastHelper toastHelper;
    public DialogHelper dialogHelper;
    public KeyboardHelper keyboardHelper;

    //product
    public String minPrice = "000000";
    public String maxPrice = "999999999999";
    public String formality = "%";
    public String architecture = "%";
    public String type = "%";
    public int bathroom = 0;
    public int bedroom = 0;

    private boolean mDoubleBackToExitPressedOnce = false;
    private int mOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WEB_SERVER = Environments.DOMAIN;

        toastHelper = new ToastHelper(this);
        alertHelper = new AlertHelper(this);
        dialogHelper = new DialogHelper(this);
        keyboardHelper = new KeyboardHelper(this);

        handleInternetReceiver();
        createNetworkConnectionFailedDialog();

        handleCreateAuthFragment();
        initViews();
        addToolbar();
        addNavigationView();
        addControl();
        handleGetSavedSearch();

        ON_USER_LOGIN = this;

    }

    private void handleInternetReceiver(){
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(new NetworkConnectionReceiver(), intentFilter);
    }

    private void initViews() {
        viewPaper = (ViewPager) findViewById(R.id.view_pager);
        this.mToolbar = findViewById(R.id.toolbar);
        mLlProfile = findViewById(R.id.ll_profile);
        mLlSort = findViewById(R.id.ll_sort);
        this.mLlViewMode = findViewById(R.id.ll_view_mode);
        this.mImvViewMode = findViewById(R.id.imv_view_mode);
        this.mTxvViewMode = findViewById(R.id.txv_view_mode);
        this.expandableLayoutProduct = findViewById(R.id.expandable_layout_product);
        this.expandableLayoutSearch = findViewById(R.id.expandable_layout_search);
        this.edtSearch = findViewById(R.id.edt_search);
        this.mLlSaveSearch = findViewById(R.id.ll_save_search);
        this.frgSearchPlace = getFragmentManager().findFragmentById(R.id.frg_search_place);

        viewPaper.setOnTouchListener(this);

        this.mLlSaveSearch.setOnClickListener(this);
        this.mLlSort.setOnClickListener(this);
        this.mLlProfile.setOnClickListener(this);
        this.mLlViewMode.setOnClickListener(this);
        this.edtSearch.setOnClickListener(this);
    }

    private void addNavigationView(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem mIPostManagement = menu.getItem(0);
        MenuItem mIUserManagement = menu.getItem(1);
        MenuItem mIPostedPost = menu.getItem(2);
        MenuItem mISavedPost = menu.getItem(3);
        MenuItem mISavedSearch = menu.getItem(4);
        MenuItem mIAccountManagement = menu.getItem(5);
        MenuItem mILogin = menu.getItem(6);
        MenuItem mILogout = menu.getItem(7);

        mIPostManagement.setVisible(false);
        mIUserManagement.setVisible(false);
        mIPostedPost.setVisible(false);
        mISavedPost.setVisible(true);
        mISavedSearch.setVisible(true);
        mIAccountManagement.setVisible(false);
        mILogout.setVisible(false);
        mILogin.setVisible(true);
    }

    private void addControl() {
        FragmentManager manager = getSupportFragmentManager();
        MainAdapter adapter = new MainAdapter(manager);
        viewPaper.setAdapter(adapter);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        this.mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void handleCreateAuthFragment(){
        final StartFragment startFragment = new StartFragment();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_start, startFragment);
        fragmentTransaction.commit();

        final FrameLayout flStart = findViewById(R.id.fl_start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getFragmentManager().beginTransaction().remove(startFragment).commit();
                flStart.setVisibility(View.GONE);
            }
        }, 2000);
    }

    public void disPlayPostItemFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mPostFragment = new PostFragment();
        fragmentTransaction.replace(R.id.fl_item_post, mPostFragment);
        fragmentTransaction.commit();

        expandableLayoutProduct.expand();
    }

    private void createNetworkConnectionFailedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Không thể kết nối internet. Kiểm tra kết nối của bạn!!!")
                .setCancelable(false);
        this.NETWORK_CONNECTION_DIALOG = builder.create();
    }


    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        toastHelper.toast("Ấn thêm lần nữa để thoát khỏi ứng dụng!!!", ToastHelper.LENGTH_SHORT);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce=false;
            }
        }, 2000);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try{
            if(mPostFragment != null){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(mPostFragment);
                fragmentTransaction.commit();
            }
        }
        catch (java.lang.NullPointerException e){}


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view) {

            if(item.getTitle().equals("List")){
                ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();
                viewPaper.setCurrentItem(1);
                item.setTitle("Map");

                this.mLlViewMode.setVisibility(View.GONE);
                this.mLlSort.setVisibility(View.VISIBLE);

            }
            else{
                mOption = 0;
                viewPaper.setCurrentItem(0);
                item.setTitle("List");
                this.mLlViewMode.setVisibility(View.VISIBLE);
                this.mLlSort.setVisibility(View.GONE);
            }

        }
        else if (id == R.id.action_search){
            Intent intent = new Intent(MainActivity.this, SearchFilterDialog.class);
            intent.putExtra("formality", formality);
            intent.putExtra("architecture", architecture);
            intent.putExtra("type", type);
            intent.putExtra(AppUtils.QUALITY_BATHROOM, bathroom);
            intent.putExtra(AppUtils.QUALITY_BEDROOM, bedroom);
            intent.putExtra("min_price", minPrice);
            intent.putExtra("max_price", maxPrice);

            startActivityForResult(intent, 1001);
        }
        else{
            toastHelper.toast("Chức năng đang được phát triển, vui lòng quay lại sau. Xin cảm ơn!!!", ToastHelper.LENGTH_SHORT);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1001 && resultCode == RESULT_OK && data != null){

            this.formality = data.getStringExtra("formality");
            this.architecture = data.getStringExtra("architecture");
            this.type = data.getStringExtra("type");
            this.bathroom = data.getIntExtra(AppUtils.QUALITY_BATHROOM, 0);
            this.bedroom = data.getIntExtra(AppUtils.QUALITY_BEDROOM, 0);
            this.minPrice = data.getStringExtra("min_price");
            this.maxPrice = data.getStringExtra("max_price");

            MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) getSupportFragmentManager().getFragments().get(0);

            MapPostNewsFragment.ON_EVENT_FROM_ACTIVITY.onDataFilter();
            viewPaper.setCurrentItem(0);

            mapPostNewsFragment.resetLastClick();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        dialogHelper.show();

        int id = item.getItemId();

        if(id == R.id.nav_post_management){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra("fragment", 1);
            startActivity(intent);
        }
        else if(id == R.id.nav_user_management){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra("fragment", 2);
            startActivity(intent);
        }
        else if(id == R.id.nav_posted_post){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra("fragment", 0);
            startActivity(intent);
        }
        else if (id == R.id.nav_saved_post) {
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            if(USER.getLevel() == User.USER_NULL){
                intent.putExtra("fragment", 0);
            }
            else{
                intent.putExtra("fragment", 1);
            }
            startActivity(intent);

        }
        else if(id == R.id.nav_saved_search){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            if(USER.getLevel() == User.USER_NULL){
                intent.putExtra("fragment", 1);
            }
            else{
                intent.putExtra("fragment", 2);
            }
            startActivity(intent);
        }
        else if (id == R.id.nav_account_manage) {
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra("fragment", 0);
            startActivity(intent);
        }
        else if(id == R.id.nav_login){
            Intent intent = new Intent(MainActivity.this, UserActionActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_logout){

            alertHelper.setCallback(this);
            alertHelper.alert("Xác nhận", "Bạn có chắc chắn muốn đăng xuất", false,
                    "Xác nhận", "Hủy bỏ", Constant.LOGOUT);
        }
        else if (id == R.id.nav_introduction) {
            toastHelper.toast("Chức năng đang được phát triển, vui lòng quay lại sau. Xin cảm ơn!!!", ToastHelper.LENGTH_SHORT);
        }
        else if (id == R.id.nav_feedback) {
            toastHelper.toast("Chức năng đang được phát triển, vui lòng quay lại sau. Xin cảm ơn!!!", ToastHelper.LENGTH_SHORT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        dialogHelper.dismiss();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_profile:
                if(USER.getId() == User.USER_NULL){
                    Intent intent = new Intent(MainActivity.this, UserActionActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_sort:
                startDialogSort();
                break;
            case R.id.ll_view_mode:
                handleViewMode();
                break;
            case R.id.ll_save_search:
                if(USER.getLevel() != User.USER_NULL && USER.getLevel() != 3){
                    toastHelper.toast("User của bạn không thể sử dụng chức năng này", ToastHelper.LENGTH_SHORT);
                }
                else{
                    handleSavedSearch();
                }
                break;
            case R.id.edt_search:
                final SearchPlaceFragment searchPlaceFragment = (SearchPlaceFragment) frgSearchPlace;
                searchPlaceFragment.edtSearch.setFocusable(true);
                searchPlaceFragment.edtSearch.setFocusableInTouchMode(true);

                searchPlaceFragment.edtSearch.setText(this.edtSearch.getText().toString());

                expandableLayoutSearch.expand();

                keyboardHelper.showKeyboard(searchPlaceFragment.edtSearch);

                break;

        }
    }


    private void handleGetSavedSearch() {
        new PresenterSavedSearch(this).handleGetDataSavedSearch(this);
    }

    public void handleSavedSearch() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name_save_search);

        final EditText mEdtNameSaveSearch = dialog.findViewById(R.id.edt_name_save_search);
        Button mBtnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button mBtnCancel = dialog.findViewById(R.id.btn_cancel);

        dialog.show();
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConfirmSavedSearch(mEdtNameSaveSearch.getText().toString().trim());
                dialog.dismiss();
            }
        });
    }

    private void handleConfirmSavedSearch(String name){

        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment)getSupportFragmentManager().getFragments().get(0);
        LatLng latLng = mapPostNewsFragment.mMap.getCameraPosition().target;
        Condition condition = new Condition(latLng.latitude, latLng.longitude, mapPostNewsFragment.mMap.getCameraPosition().zoom,
                minPrice, maxPrice, formality, type, architecture, bathroom, bedroom, name);

        ListPostNewsFragment.LIST_SAVED_SEARCH.add(condition);
        new PresenterSavedSearch(this).handleUpdateSavedSearchList(ListPostNewsFragment.LIST_SAVED_SEARCH, this);

    }


    private void handleViewMode(){
        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment)getSupportFragmentManager().getFragments().get(0);
        if(mapPostNewsFragment.mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE){
            mapPostNewsFragment.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            this.mImvViewMode.setImageResource(R.drawable.icon_streetview_black_24dp);
            this.mTxvViewMode.setText(getResources().getString(R.string.satellite));
        }
        else{
            mapPostNewsFragment.mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            this.mImvViewMode.setImageResource(R.drawable.icon_map_black_24dp);
            this.mTxvViewMode.setText(getResources().getString(R.string.map_vietnamese));
        }
    }

    private void startDialogSort(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sort);

        ArrayList<String> arrOption = new ArrayList<>();
        arrOption.add("Tin mới nhất");
        arrOption.add("Giá tăng dần");
        arrOption.add("Giá giảm dần");
        arrOption.add("Diện tích tăng dần");
        arrOption.add("Diện tích giảm dần");

        SortAdapter sortAdapter = new SortAdapter(this, arrOption, mOption);

        ListView lsv = dialog.findViewById(R.id.lsv_item);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lsv.setAdapter(sortAdapter);
        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mOption != position){
                    dialogHelper.show();
                    mOption = position;
                    handleSort();
                    dialog.dismiss();

                    MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment)getSupportFragmentManager().getFragments().get(0);
                    mapPostNewsFragment.handlePostList(mapPostNewsFragment.arrProduct);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onUserLoginSuccessful() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem mIPostManagement = menu.getItem(0);
        MenuItem mIUserManagement = menu.getItem(1);
        MenuItem mIPostedPost = menu.getItem(2);
        MenuItem mISavedPost = menu.getItem(3);
        MenuItem mISavedSearch = menu.getItem(4);
        MenuItem mIAccountManagement = menu.getItem(5);
        MenuItem mILogin = menu.getItem(6);
        MenuItem mILogout = menu.getItem(7);

        if(USER.getLevel() == 1){
            mIPostManagement.setVisible(true);
            mIUserManagement.setVisible(true);
            mIPostedPost.setVisible(false);
            mISavedPost.setVisible(false);
            mISavedSearch.setVisible(false);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else if(USER.getLevel() == 2){
            mISavedSearch.setVisible(false);
            mIPostManagement.setVisible(true);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(false);
            mISavedPost.setVisible(false);
            mISavedSearch.setVisible(false);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else if(USER.getLevel() == 3){
            mIPostManagement.setVisible(false);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(true);
            mISavedPost.setVisible(true);
            mISavedSearch.setVisible(true);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else {
            mIPostManagement.setVisible(false);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(false);
            mIAccountManagement.setVisible(false);
            mILogout.setVisible(false);
            mILogin.setVisible(true);
            mISavedSearch.setVisible(true);
            mISavedPost.setVisible(true);
        }
    }

    private void handleSort(){

        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) getSupportFragmentManager().getFragments().get(0);

        if(mOption == 0){
            Collections.sort(mapPostNewsFragment.arrProduct, new ProductDateSort());
        }
        else if(mOption == 1){
            Collections.sort(mapPostNewsFragment.arrProduct, new PriceProductIncreaseSort());
        }
        else if(mOption == 2){
            Collections.sort(mapPostNewsFragment.arrProduct, new PriceProductDecreaseSort());
        }
        else if(mOption == 3){
            Collections.sort(mapPostNewsFragment.arrProduct, new AreaProductIncreaseSort());
        }
        else if(mOption == 4){
            Collections.sort(mapPostNewsFragment.arrProduct, new AreaProductDecreaseSort());
        }
        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();

        ListPostNewsFragment fragment = (ListPostNewsFragment) getSupportFragmentManager().getFragments().get(1);
        fragment.mLsvListProduct.smoothScrollToPosition(0);

        dialogHelper.dismiss();
    }

    @Override
    public void onHandleDataSavedSearchSuccessful(ArrayList<Condition> lists) {
        ListPostNewsFragment.LIST_SAVED_SEARCH = lists;
    }

    @Override
    public void onHandleDataSavedSearchError(String error) {

    }

    @Override
    public void onHandleUpdateSavedSearchListSuccessful() {
        toastHelper.toast("Lưu thành công", ToastHelper.LENGTH_SHORT);
    }

    @Override
    public void onHandleUpdateSavedSearchListError(String error) {

    }

    @Override
    public void onPositiveButtonClick(int option) {
        if(option == Constant.LOGOUT){
            MainActivity.USER.clearData();
            Intent intent = new Intent(MainActivity.this, UserActionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    class ProductDateSort implements Comparator<Product>{
        @Override
        public int compare(Product left, Product right) {
            return right.getDateUpload().compareTo(left.getDateUpload());
        }
    }

    class PriceProductIncreaseSort implements Comparator<Product>{
        @Override
        public int compare(Product left, Product right) {
            return left.getPrice().compareTo(right.getPrice());
        }
    }

    class PriceProductDecreaseSort implements Comparator<Product>{
        @Override
        public int compare(Product left, Product right) {
            return right.getPrice().compareTo(left.getPrice());
        }
    }

    class AreaProductIncreaseSort implements Comparator<Product>{

        @Override
        public int compare(Product left, Product right) {
            if(left.getArea() < right.getArea()){
                return -1;
            }
            else if (left.getArea() > right.getArea()){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

    class AreaProductDecreaseSort implements Comparator<Product>{

        @Override
        public int compare(Product left, Product right) {
            if(right.getArea() < left.getArea()){
                return -1;
            }
            else if (right.getArea() > left.getArea()){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
}
