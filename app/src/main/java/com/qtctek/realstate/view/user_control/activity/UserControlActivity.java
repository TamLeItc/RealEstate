package com.qtctek.realstate.view.user_control.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.user_action.activity.UserActionActivity;
import com.qtctek.realstate.view.user_control.fragment.UserNormalControlFragment;
import com.qtctek.realstate.view.user_control.fragment.UserSystemControlFragment;

public class UserControlActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private boolean mDoubleBackToExitPressedOnce = false;
    public static int POSITION_FRAGMENT = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_control);

        initViews();
        handleStart();
        addToolbar();
        getValueFromIntent();

    }

    private void initViews(){
        this.mToolbar = findViewById(R.id.toolbar);
    }

    private void addToolbar(){
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getValueFromIntent(){
        try{
            Intent intent = getIntent();
            POSITION_FRAGMENT = intent.getIntExtra("fragment", 0);
        }
        catch (java.lang.NullPointerException e){
            POSITION_FRAGMENT = 0;
        }
    }

    //To know what screen to display after the user log on
    private void handleStart(){
        if(MainActivity.ROLE_USER.equals("2")){ //người dùng
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_user_control, new UserNormalControlFragment());
            fragmentTransaction.commit();
        }
        else{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_user_control, new UserSystemControlFragment());
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_control_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.control_new_post);
        if(MainActivity.ROLE_USER.equals("2")){
            menuItem.setVisible(true);
        }
        else{
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.control_home:
                finish();
                break;
            case R.id.control_new_post:
                intent = new Intent(UserControlActivity.this, NewPostActivity.class);
                intent.putExtra("post_id", -1);
                startActivity(intent);
                finish();
                break;
            case R.id.control_update_information:
                intent = new Intent(UserControlActivity.this, UserActionActivity.class);
                intent.putExtra("option", "update_user");
                startActivity(intent);
                finish();
                break;
            case R.id.control_logout:
                handleLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc chắn muốn đăng xuất!")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.ROLE_USER = "";
                        MainActivity.EMAIL_USER = "";
                        Intent intent = new Intent(UserControlActivity.this, UserActionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
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
}
