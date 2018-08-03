package com.qtctek.aladin.view.post_news.activity;

import android.annotation.SuppressLint;
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
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.qtctek.aladin.Environments;
import com.qtctek.aladin.R;
import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.common.general.Constant;
import com.qtctek.aladin.dto.Condition;
import com.qtctek.aladin.dto.Product;
import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.helper.AlertHelper;
import com.qtctek.aladin.helper.DialogHelper;
import com.qtctek.aladin.helper.KeyboardHelper;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.network.receiver.NetworkConnectionReceiver;
import com.qtctek.aladin.presenter.user_control.save_search.PresenterSavedSearch;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;
import com.qtctek.aladin.view.post_news.adapter.MainAdapter;
import com.qtctek.aladin.view.post_news.adapter.SortAdapter;
import com.qtctek.aladin.view.post_news.fragment.FilterFragment;
import com.qtctek.aladin.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.aladin.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.aladin.view.post_news.fragment.PostFragment;
import com.qtctek.aladin.view.post_news.fragment.SearchPlaceFragment;
import com.qtctek.aladin.view.post_news.fragment.StartFragment;
import com.qtctek.aladin.view.post_news.interfaces.OnUserLogin;
import com.qtctek.aladin.view.user_action.activity.UserActionActivity;
import com.qtctek.aladin.view.user_control.activity.UserControlActivity;
import com.qtctek.aladin.view.user_control.saved_search.ViewHandleSavedSearch;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnTouchListener, View.OnClickListener, OnUserLogin, ViewHandleSavedSearch, AlertHelper.AlertHelperCallback {

    public static String WEB_SERVER;
    public static String IMAGE_URL_RELATIVE;

    public static User USER = new User();

    public static OnUserLogin ON_USER_LOGIN;

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
    private ExpandableLayout expandableLayoutStart;
    private ExpandableLayout expandableLayoutFilter;
    private Fragment frgSearchPlace;
    private Fragment frgFilter;

    public static Dialog NETWORK_CONNECTION_DIALOG;
    private AlertHelper alertHelper;
    private ToastHelper toastHelper;
    private DialogHelper dialogHelper;
    private KeyboardHelper keyboardHelper;

    //product
    public Double mapLat = 0.0;
    public Double mapLng = 0.0;
    public String minPrice = "000000";
    public String maxPrice = "999999999999";
    public String formality = "%";
    public String architecture = "%";
    public String type = "%";
    public int bathroom = 0;
    public int bedroom = 0;

    private boolean mDoubleBackToExitPressedOnce = false;
    public int optionSort = 0;
    public boolean isFirstOpenApp;
    public boolean isEditSaved = false;
    public MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WEB_SERVER = Environments.DOMAIN;
        IMAGE_URL_RELATIVE = Environments.IMAGE_URL_RELATIVE;

        toastHelper = new ToastHelper(this);
        alertHelper = new AlertHelper(this);
        dialogHelper = new DialogHelper(this);
        keyboardHelper = new KeyboardHelper(this);

        handleInternetReceiver();
        createNetworkConnectionFailedDialog();

        handleGetSavedSearch();
        handleCreateAuthFragment();
        initViews();
        addToolbar();
        addNavigationView();
        addControl();

        ON_USER_LOGIN = this;

    }

    public AlertHelper getAlertHelper() {
        if(alertHelper == null){
            alertHelper = new AlertHelper(this);
        }
        return alertHelper;
    }

    public ToastHelper getToastHelper() {
        if(toastHelper == null){
            toastHelper = new ToastHelper(this);
        }
        return toastHelper;
    }

    public DialogHelper getDialogHelper() {
        if(dialogHelper == null){
            dialogHelper = new DialogHelper(this);
        }
        return dialogHelper;
    }

    public KeyboardHelper getKeyboardHelper() {
        if(keyboardHelper == null){
            keyboardHelper = new KeyboardHelper(this);
        }
        return keyboardHelper;
    }

    private void handleInternetReceiver(){
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(new NetworkConnectionReceiver(), intentFilter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        viewPaper = findViewById(R.id.view_pager);
        this.mToolbar = findViewById(R.id.toolbar);
        mLlProfile = findViewById(R.id.ll_profile);
        mLlSort = findViewById(R.id.ll_sort);
        this.mLlViewMode = findViewById(R.id.ll_view_mode);
        this.mImvViewMode = findViewById(R.id.imv_view_mode);
        this.mTxvViewMode = findViewById(R.id.txv_view_mode);
        this.expandableLayoutProduct = findViewById(R.id.expandable_layout_product);
        this.expandableLayoutSearch = findViewById(R.id.expandable_layout_search);
        this.expandableLayoutStart = findViewById(R.id.expandable_layout_start);
        this.expandableLayoutFilter = findViewById(R.id.expandable_layout_filter);
        this.edtSearch = findViewById(R.id.edt_search);
        this.mLlSaveSearch = findViewById(R.id.ll_save_search);
        this.frgSearchPlace = getFragmentManager().findFragmentById(R.id.frg_search_place);
        this.frgFilter = getFragmentManager().findFragmentById(R.id.frg_filter);

        viewPaper.setOnTouchListener(this);

        this.mLlSaveSearch.setOnClickListener(this);
        this.mLlSort.setOnClickListener(this);
        this.mLlProfile.setOnClickListener(this);
        this.mLlViewMode.setOnClickListener(this);
        this.edtSearch.setOnClickListener(this);
    }

    private void addNavigationView(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void addControl() {
        FragmentManager manager = getSupportFragmentManager();
        mainAdapter = new MainAdapter(manager);
        viewPaper.setAdapter(mainAdapter);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.icon_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);

        this.mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void handleCreateAuthFragment(){
        final StartFragment startFragment = new StartFragment();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.custom_frag_in, R.animator.custom_frg_out);
        fragmentTransaction.replace(R.id.fl_start, startFragment);
        fragmentTransaction.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expandableLayoutStart.collapse();
            }
        }, 2000);
    }

    public void disPlayPostItemFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PostFragment mPostFragment = new PostFragment();
        fragmentTransaction.replace(R.id.fl_item_post, mPostFragment);
        fragmentTransaction.commit();

        expandableLayoutProduct.expand();
    }

    private void createNetworkConnectionFailedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.error_connect_internet_notification))
                .setCancelable(false);
        NETWORK_CONNECTION_DIALOG = builder.create();
    }


    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        toastHelper.toast(R.string.double_press_back_to_exit, ToastHelper.LENGTH_SHORT);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce=false;
            }
        }, 2000);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        expandableLayoutProduct.collapse();

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view) {

            if(item.getTitle().equals("List")){

                viewPaper.setCurrentItem(1);
                item.setTitle("Map");

                this.mLlViewMode.setVisibility(View.GONE);
                this.mLlSort.setVisibility(View.VISIBLE);

            }
            else{
                if(isEditSaved){
                    MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) mainAdapter.getItem(0);
                    mapPostNewsFragment.loadMarker();

                    isEditSaved = false;
                }

                viewPaper.setCurrentItem(0);
                item.setTitle("List");
                this.mLlViewMode.setVisibility(View.VISIBLE);
                this.mLlSort.setVisibility(View.GONE);
            }

        }
        else if (id == R.id.action_filter){
            expandableLayoutFilter.expand();
            ((FilterFragment)frgFilter).initValue();
        }
        else{
            toastHelper.toast("Chức năng đang được phát triển, vui lòng quay lại sau. Xin cảm ơn!!!", ToastHelper.LENGTH_SHORT);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1001 && resultCode == RESULT_OK && data != null){

            this.formality = data.getStringExtra(Product.FORMALITY);
            this.architecture = data.getStringExtra(Product.ARCHITECTURE);
            this.type = data.getStringExtra(Product.TYPE);
            this.bathroom = data.getIntExtra(AppUtils.QUALITY_BATHROOM, 0);
            this.bedroom = data.getIntExtra(AppUtils.QUALITY_BEDROOM, 0);
            this.minPrice = data.getStringExtra(Constant.MIN_PRICE);
            this.maxPrice = data.getStringExtra(Constant.MAX_PRICE);

            MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) mainAdapter.getItem(0);

            MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.onHandleFilter();
            viewPaper.setCurrentItem(0);

            mapPostNewsFragment.resetLastClick();
        }
    }

    public void handleFilter() {

        expandableLayoutFilter.collapse();

        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) mainAdapter.getItem(0);

        MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.onHandleFilter();
        viewPaper.setCurrentItem(0);

        mapPostNewsFragment.resetLastClick();
    }

    public void cancelFilter(){
        expandableLayoutFilter.collapse();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        dialogHelper.show();

        int id = item.getItemId();

        if(id == R.id.nav_new_post){
            if(USER.getLevel() == User.USER_NULL){
                alertHelper.setCallback(this);
                alertHelper.alert(R.string.error, R.string.login_to_continue, false, R.string.ok, R.string.cancel, Constant.LOGIN);
            }
            else{
                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                intent.putExtra(Product.ID, -1);
                startActivity(intent);
            }
        }
        if(id == R.id.nav_post_management){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra(Constant.FRAGMENT, 1);
            startActivity(intent);
        }
        else if(id == R.id.nav_user_management){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra(Constant.FRAGMENT, 2);
            startActivity(intent);
        }
        else if(id == R.id.nav_posted_post){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra(Constant.FRAGMENT, 1);
            startActivity(intent);
        }
        else if (id == R.id.nav_saved_post) {
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            if(USER.getLevel() == User.USER_NULL){
                intent.putExtra(Constant.FRAGMENT, 0);
            }
            else if(USER.getLevel() == 1){
                intent.putExtra(Constant.FRAGMENT, 3);
            }
            else if(USER.getLevel() == 2){
                intent.putExtra(Constant.FRAGMENT, 2);
            }
            else{
                intent.putExtra(Constant.FRAGMENT, 2);
            }
            startActivity(intent);

        }
        else if(id == R.id.nav_saved_search){
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            if(USER.getLevel() == User.USER_NULL){
                intent.putExtra(Constant.FRAGMENT, 1);
            }
            else if(USER.getLevel() == 1){
                intent.putExtra(Constant.FRAGMENT, 4);
            }
            else if(USER.getLevel() == 2){
                intent.putExtra(Constant.FRAGMENT, 3);
            }
            else{
                intent.putExtra(Constant.FRAGMENT, 3);
            }
            startActivity(intent);
        }
        else if (id == R.id.nav_account_manage) {
            Intent intent = new Intent(MainActivity.this, UserControlActivity.class);
            intent.putExtra(Constant.FRAGMENT, 0);
            startActivity(intent);
        }
        else if(id == R.id.nav_login){
            Intent intent = new Intent(MainActivity.this, UserActionActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_logout){

            alertHelper.setCallback(this);
            alertHelper.alert(R.string.log_out, R.string.log_out_notifaction, false, R.string.ok, R.string.cancel, Constant.LOGOUT);
        }
        else if (id == R.id.nav_introduction) {
            toastHelper.toast("Chức năng đang được phát triển, vui lòng quay lại sau. Xin cảm ơn!!!", ToastHelper.LENGTH_SHORT);
        }
        else if (id == R.id.nav_feedback) {
            toastHelper.toast("Chức năng đang được phát triển, vui lòng quay lại sau. Xin cảm ơn!!!", ToastHelper.LENGTH_SHORT);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        dialogHelper.dismiss();
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
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
                handleSavedSearch();
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

        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) mainAdapter.getItem(0);
        LatLng latLng = mapPostNewsFragment.mMap.getCameraPosition().target;
        Condition condition = new Condition(latLng.latitude, latLng.longitude, mapPostNewsFragment.mMap.getCameraPosition().zoom,
                minPrice, maxPrice, formality, type, architecture, bathroom, bedroom, name);

        ListPostNewsFragment.LIST_SAVED_SEARCH.add(condition);
        new PresenterSavedSearch(this).handleUpdateSavedSearchList(ListPostNewsFragment.LIST_SAVED_SEARCH, this);

    }


    private void handleViewMode(){
        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment)mainAdapter.getItem(0);
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
        arrOption.add(getResources().getString(R.string.latest_news));
        arrOption.add(getResources().getString(R.string.price_increase));
        arrOption.add(getResources().getString(R.string.price_decrease));
        arrOption.add(getResources().getString(R.string.area_increase));
        arrOption.add(getResources().getString(R.string.area_decrease));

        SortAdapter sortAdapter = new SortAdapter(this, arrOption, optionSort);

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
                if(optionSort != position){
                    dialogHelper.show();
                    optionSort = position;
                    handleSort();
                    dialog.dismiss();

                    MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment)mainAdapter.getItem(0);
                    mapPostNewsFragment.handlePostList(mapPostNewsFragment.arrProduct);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onUserLoginSuccessful() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem mINewPost = menu.getItem(0);
        MenuItem mIManagement = menu.getItem(1);

        SubMenu subMenuManagement = mIManagement.getSubMenu();
        MenuItem mIPostManagement = subMenuManagement.getItem(0);
        MenuItem mIUserManagement = subMenuManagement.getItem(1);
        MenuItem mIPostedPost = subMenuManagement.getItem(2);
        MenuItem mIAccountManagement = subMenuManagement.getItem(5);
        MenuItem mILogin = subMenuManagement.getItem(6);
        MenuItem mILogout = subMenuManagement.getItem(7);

        if(USER.getLevel() == 1){
            mINewPost.setVisible(false);
            mIPostManagement.setVisible(true);
            mIUserManagement.setVisible(true);
            mIPostedPost.setVisible(false);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else if(USER.getLevel() == 2){
            mINewPost.setVisible(false);
            mIPostManagement.setVisible(true);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(false);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else if(USER.getLevel() == 3){
            mINewPost.setVisible(true);
            mIPostManagement.setVisible(false);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(true);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else {
            mINewPost.setVisible(true);
            mIPostManagement.setVisible(false);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(false);
            mIAccountManagement.setVisible(false);
            mILogout.setVisible(false);
            mILogin.setVisible(true);
        }
    }

    public synchronized void handleSort(){

        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) mainAdapter.getItem(0);

        if(optionSort == 0){
            Collections.sort(mapPostNewsFragment.arrProduct, new ProductDateSort());
        }
        else if(optionSort == 1){
            Collections.sort(mapPostNewsFragment.arrProduct, new PriceProductIncreaseSort());
        }
        else if(optionSort == 2){
            Collections.sort(mapPostNewsFragment.arrProduct, new PriceProductDecreaseSort());
        }
        else if(optionSort == 3){
            Collections.sort(mapPostNewsFragment.arrProduct, new AreaProductIncreaseSort());
        }
        else if(optionSort == 4){
            Collections.sort(mapPostNewsFragment.arrProduct, new AreaProductDecreaseSort());
        }
        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();

        ListPostNewsFragment fragment = (ListPostNewsFragment) mainAdapter.getItem(1);

        try {
            fragment.mLsvListProduct.smoothScrollToPosition(0);
        }
        catch (Exception ignored){}

        dialogHelper.dismiss();
    }

    public void lastSearch(){
        new PresenterSavedSearch(this).handleGetDataLastSearch(this);
    }

    @Override
    public void onHandleDataSavedSearchSuccessful(ArrayList<Condition> lists) {
        ListPostNewsFragment.LIST_SAVED_SEARCH = lists;

        isFirstOpenApp = ListPostNewsFragment.LIST_SAVED_SEARCH.size() == 0;

    }

    @Override
    public void onHandleDataSavedSearchError(String error) {

    }

    @Override
    public void onHandleDataLastSearchSuccessful(Condition condition) {
        if(condition.getArchitecture() != null){
            MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.onHandleSearch(condition);
        }
        else{
            MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) mainAdapter.getItem(0);
            mapPostNewsFragment.searchNearby();
        }
    }

    @Override
    public void onHandleUpdateSavedSearchListSuccessful() {
        toastHelper.toast(R.string.save_data_successful, ToastHelper.LENGTH_SHORT);
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
        else if(option == Constant.LOGIN){
            Intent intent = new Intent(MainActivity.this, UserActionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNegativeButtonClick(int option) {

    }

    @Override
    protected void onStop() {
        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment)mainAdapter.getItem(0);
        Condition condition = new Condition(mapLat, mapLng, mapPostNewsFragment.mMap.getCameraPosition().zoom,
                minPrice, maxPrice, formality, type, architecture, bathroom, bedroom, Constant.LAST_SEARCH);

        new PresenterSavedSearch(this).handleSaveLastSearch(condition, this);

        super.onStop();
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
            return Float.compare(left.getArea(), right.getArea());
        }
    }

    class AreaProductDecreaseSort implements Comparator<Product>{

        @Override
        public int compare(Product left, Product right) {
            return Float.compare(right.getArea(), left.getArea());
        }
    }
}
