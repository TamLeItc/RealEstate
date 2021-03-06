package com.qtctek.aladin.view.user_control.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.user_control.post_management.PostManagementFragment;
import com.qtctek.aladin.view.user_control.posted_post.PostedPostFragment;
import com.qtctek.aladin.view.user_control.saved_post.SavedPostFragment;
import com.qtctek.aladin.view.user_control.fragment.UserControlFragment;
import com.qtctek.aladin.view.user_control.saved_search.SavedSearchFragment;
import com.qtctek.aladin.view.user_control.user_management.UserManagementFragment;

public class UserControlAdapter extends FragmentStatePagerAdapter {

    public UserControlAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        return handleGetItem(position);
    }

    private Fragment handleGetItem(int position){
        Fragment frag=null;
        if(MainActivity.USER.getLevel() == 1){
            switch (position){
                case 0:
                    frag = new UserControlFragment();
                    break;
                case 1:
                    frag = new PostManagementFragment();
                    break;
                case 2:
                    frag = new UserManagementFragment();
                    break;
                case 3:
                    frag = new SavedPostFragment();
                    break;
                case 4:
                    frag = new SavedSearchFragment();
                    break;
            }
        }
        else if(MainActivity.USER.getLevel() == 2){
            switch (position) {
                case 0:
                    frag = new UserControlFragment();
                    break;
                case 1:
                    frag = new PostManagementFragment();
                    break;
                case 2:
                    frag = new SavedPostFragment();
                    break;
                case 3:
                    frag = new SavedSearchFragment();
                    break;
            }
        }
        else if(MainActivity.USER.getLevel() == 3){
            switch (position) {
                case 0:
                    frag = new UserControlFragment();
                    break;
                case 1:
                    frag = new PostedPostFragment();
                    break;
                case 2:
                    frag = new SavedPostFragment();
                    break;
                case 3:
                    frag = new SavedSearchFragment();
                    break;
            }
        }
        else if(MainActivity.USER.getLevel() == User.USER_NULL){
            switch (position){
                case 0:
                    frag = new SavedPostFragment();
                    break;
                case 1:
                    frag = new SavedSearchFragment();
                    break;
            }
        }
        return frag;
    }

    @Override
    public int getCount() {

        if(MainActivity.USER.getLevel() == 1){
            return 5;
        }
        else if(MainActivity.USER.getLevel() == 2){
            return 4;
        }
        else if(MainActivity.USER.getLevel() == 3){
            return 4;
        }
        else if(MainActivity.USER.getLevel() == User.USER_NULL){
            return 2;
        }
        else{
            return 0;
        }

    }
    @Override
    public CharSequence getPageTitle(int position) {
        return handleGetPageTitle(position);
    }

    private CharSequence handleGetPageTitle(int position){
        String title = "";
        if(MainActivity.USER.getLevel() == User.USER_NULL){
            switch (position){
                case 0:
                    title = "Tin đã lưu";
                    break;
                case 1:
                    title = "T.kiếm đã lưu";
                    break;
            }
        }
        return title;
    }

}
