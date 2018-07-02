package com.qtctek.realstate.view.new_post.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qtctek.realstate.view.new_post.contact_information.ContactInformationFragment;
import com.qtctek.realstate.view.new_post.description_information.DescriptionInformationFragment;
import com.qtctek.realstate.view.new_post.NewPostSuccess;
import com.qtctek.realstate.view.new_post.images_information.ImagesInformationFragment;
import com.qtctek.realstate.view.new_post.map_information.MapInformationFragment;
import com.qtctek.realstate.view.new_post.product_information.ProductInformationFragment;

public class NewPostAdapter extends FragmentStatePagerAdapter {

    public NewPostAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg = null;
        switch (position){
            case 0:
                frg = new ImagesInformationFragment();
                break;
            case 1:
                frg = new ProductInformationFragment();
                break;
            case 2:
                frg = new DescriptionInformationFragment();
                break;
            case 3:
                frg = new MapInformationFragment();
                break;
            case 4:
                frg = new ContactInformationFragment();
                break;
            case 5:
                frg = new NewPostSuccess();
                break;
        }
        return frg;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
