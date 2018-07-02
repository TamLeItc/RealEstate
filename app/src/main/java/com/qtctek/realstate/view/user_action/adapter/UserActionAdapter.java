package com.qtctek.realstate.view.user_action.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qtctek.realstate.view.user_action.login.LoginFragment;
import com.qtctek.realstate.view.user_action.forgot_password.ForgotPasswordFragment;
import com.qtctek.realstate.view.user_action.register.RegisterFragment;
import com.qtctek.realstate.view.user_action.update_user.UpdateUserFragment;

public class UserActionAdapter extends FragmentStatePagerAdapter {

    public UserActionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fm = null;
        switch (position){
            case 0:
                fm = new LoginFragment();
                break;
            case 1:
                fm = new RegisterFragment();
                break;
            case 2:
                fm = new ForgotPasswordFragment();
                break;
            case 3:
                fm = new UpdateUserFragment();
                break;
        }
        return fm;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
