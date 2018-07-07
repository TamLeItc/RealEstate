package com.qtctek.realstate.view.post_news.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.view.post_news.adapter.MainAdapter;
import com.qtctek.realstate.view.post_news.adapter.SortAdapter;
import com.qtctek.realstate.view.post_news.dialog.SearchDialog;
import com.qtctek.realstate.view.post_news.interfaces.OnUserLogin;
import com.qtctek.realstate.view.post_news.fragment.ListPostNewsFragment;
import com.qtctek.realstate.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.activity.UserControlActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener, View.OnClickListener,
        OnUserLogin{

    public static String HOST = "http://192.168.100.27/";
    public static String WEB_SERVER = "http://antoniquang.noip.me:5555/real_estate/";

    public static String USERr = "";
    public static String LEVEL = "";
    public static User USER = new User();

    public static OnUserLogin ON_USER_LOGIN;

    private ViewPager mViewPaper;
    private Dialog mLoadingDialog;
    private Toolbar mToolbar;
    private LinearLayout mLlProfile;
    private LinearLayout mLlSort;

    private String mMinPrice = "000000";
    private String mMaxPrice = "1000000000000000";
    private String mCategoryProduct = "%";
    private String mProvinceCity = "Hồ Chí Minh";
    private String mDistrict = "%";

    private boolean mDoubleBackToExitPressedOnce = false;
    private int mOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        addToolbar();
        addNavigationView();
        addControl();

        initPermission();
        createLoadingDialog();
        getDataFromIntent();

        ON_USER_LOGIN = this;
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        try{
            USERr = intent.getStringExtra("email_user");
            LEVEL = intent.getStringExtra("role");
            if(USERr == null){
                USERr = "";
            }
            if(LEVEL == null){
                LEVEL = "";
            }
        }
        catch (Exception e){
            USERr = "";
            LEVEL = "";
        }
    }

    private void initViews() {
        mViewPaper = (ViewPager) findViewById(R.id.view_pager);
        this.mToolbar = findViewById(R.id.toolbar);
        mLlProfile = findViewById(R.id.ll_profile);
        mLlSort = findViewById(R.id.ll_sort);

        mViewPaper.setOnTouchListener(this);
        this.mLlSort.setOnClickListener(this);
        this.mLlProfile.setOnClickListener(this);
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
        MenuItem mIAccountManagement = menu.getItem(4);
        MenuItem mILogin = menu.getItem(5);
        MenuItem mILogout = menu.getItem(6);

        mIPostManagement.setVisible(false);
        mIUserManagement.setVisible(false);
        mIPostedPost.setVisible(false);
        mISavedPost.setVisible(false);
        mIAccountManagement.setVisible(false);
        mILogout.setVisible(false);
        mILogin.setVisible(true);
    }

    private void addControl() {
        FragmentManager manager = getSupportFragmentManager();
        MainAdapter adapter = new MainAdapter(manager);
        mViewPaper.setAdapter(adapter);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(this);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view) {
            if(item.getTitle().equals("List")){
                mViewPaper.setCurrentItem(1);
                item.setTitle("Map");
            }
            else{
                try{
                    mOption = 0;
                    MapPostNewsFragment.ON_RESULT_FROM_ACTIVITY.onReloadMarker();
                }
                catch (Exception e){

                }
                mViewPaper.setCurrentItem(0);
                item.setTitle("List");
            }

        }
        else if (id == R.id.action_search){
            Intent intent = new Intent(MainActivity.this, SearchDialog.class);
            intent.putExtra("category_product", mCategoryProduct);
            intent.putExtra("province_city", mProvinceCity);
            intent.putExtra("district", mDistrict);
            intent.putExtra("min_price", mMinPrice);
            intent.putExtra("max_price", mMaxPrice);
            startActivityForResult(intent, 1001);
        }
        else{
            Toast.makeText(this, "Chức năng đang được phát triển, vui lòng quay lại sau. Xin cảm ơn!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1001 && resultCode == RESULT_OK && data != null){

            this.mCategoryProduct = data.getStringExtra("category_product");
            this.mProvinceCity = data.getStringExtra("province_city");
            this.mDistrict = data.getStringExtra("district");
            this.mMinPrice = data.getStringExtra("min_price");
            this.mMaxPrice = data.getStringExtra("max_price");

            MapPostNewsFragment.ON_RESULT_FROM_ACTIVITY.onDataSearch(data.getStringExtra("lat"),
                    data.getStringExtra("lng"), mMinPrice, mMaxPrice, mCategoryProduct);
            mViewPaper.setCurrentItem(0);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        mLoadingDialog.show();

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
            intent.putExtra("fragment", 1);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Bạn có chắc chắn muốn đăng xuất!")
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.LEVEL = "";
                            MainActivity.USERr = "";
                            Intent intent = new Intent(MainActivity.this, UserActionActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
        else if (id == R.id.nav_introduction) {
            Toast.makeText(this, "Nội dung này đang được phát triển. Xin quay lại say", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_feedback) {
            Toast.makeText(this, "Nội dung này đang được phát triển. Xin quay lại say", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        mLoadingDialog.dismiss();
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
                if(USERr.equals("")){
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
        }
    }

    private void startDialogSort(){
        if(mViewPaper.getCurrentItem() == 0){
            Toast.makeText(this, "Chỉ dùng sắp xếp khi xem ở định dạng danh sách.", Toast.LENGTH_SHORT).show();
            return;
        }

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
                mLoadingDialog.show();
                mOption = position;
                handleSort();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void handleSort(){
        if(mOption == 0){
            Collections.sort(MapPostNewsFragment.ARR_POST, new PostDateSort());
        }
        else if(mOption == 1){
            Collections.sort(MapPostNewsFragment.ARR_POST, new PriceProductIncreaseSort());
        }
        else if(mOption == 2){
            Collections.sort(MapPostNewsFragment.ARR_POST, new PriceProductDecreaseSort());
        }
        else if(mOption == 3){
            Collections.sort(MapPostNewsFragment.ARR_POST, new AreaProductIncreaseSort());
        }
        else if(mOption == 4){
            Collections.sort(MapPostNewsFragment.ARR_POST, new AreaProductDecreaseSort());
        }
        ListPostNewsFragment.POST_ADAPTER.notifyDataSetChanged();

        mLoadingDialog.dismiss();
    }

    @Override
    public void onUserLoginSuccessful(String role) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem mIPostManagement = menu.getItem(0);
        MenuItem mIUserManagement = menu.getItem(1);
        MenuItem mIPostedPost = menu.getItem(2);
        MenuItem mISavedPost = menu.getItem(3);
        MenuItem mIAccountManagement = menu.getItem(4);
        MenuItem mILogin = menu.getItem(5);
        MenuItem mILogout = menu.getItem(6);

        if(role.equals("")){
            mIPostManagement.setVisible(false);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(false);
            mISavedPost.setVisible(false);
            mIAccountManagement.setVisible(false);
            mILogout.setVisible(false);
            mILogin.setVisible(true);
        }
        else if(role.equals("1")){
            mIPostManagement.setVisible(true);
            mIUserManagement.setVisible(true);
            mIPostedPost.setVisible(false);
            mISavedPost.setVisible(false);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else if(role.equals("2")){
            mIPostManagement.setVisible(false);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(true);
            mISavedPost.setVisible(true);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
        else if(role.equals("3")){
            mIPostManagement.setVisible(true);
            mIUserManagement.setVisible(false);
            mIPostedPost.setVisible(false);
            mISavedPost.setVisible(false);
            mIAccountManagement.setVisible(true);
            mILogout.setVisible(true);
            mILogin.setVisible(false);
        }
    }

    class PostDateSort implements Comparator<PostSale>{
        @Override
        public int compare(PostSale left, PostSale right) {
            return left.getPostDate().compareTo(right.getPostDate());
        }
    }

    class PriceProductIncreaseSort implements Comparator<PostSale>{
        @Override
        public int compare(PostSale left, PostSale right) {
            return left.getProduct().getPrice().compareTo(right.getProduct().getPrice());
        }
    }

    class PriceProductDecreaseSort implements Comparator<PostSale>{
        @Override
        public int compare(PostSale left, PostSale right) {
            return right.getProduct().getPrice().compareTo(left.getProduct().getPrice());
        }
    }

    class AreaProductIncreaseSort implements Comparator<PostSale>{

        @Override
        public int compare(PostSale left, PostSale right) {
            if(left.getProduct().getArea() < right.getProduct().getArea()){
                return -1;
            }
            else if (left.getProduct().getArea() > right.getProduct().getArea()){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

    class AreaProductDecreaseSort implements Comparator<PostSale>{

        @Override
        public int compare(PostSale left, PostSale right) {
            if(right.getProduct().getArea() < left.getProduct().getArea()){
                return -1;
            }
            else if (right.getProduct().getArea() > left.getProduct().getArea()){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

    private void initPermission(){
        String[] permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    requestPermissions(permission, 1001);
                }
            }
        }
    }
}
