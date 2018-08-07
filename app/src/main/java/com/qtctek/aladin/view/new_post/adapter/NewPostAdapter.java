package com.qtctek.aladin.view.new_post.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qtctek.aladin.view.new_post.description_information.DescriptionInformationFragment;
import com.qtctek.aladin.view.new_post.NewPostSuccessFragment;
import com.qtctek.aladin.view.new_post.images_information.ImagesInformationFragment;
import com.qtctek.aladin.view.new_post.map_information.fragment.MapInformationFragment;
import com.qtctek.aladin.view.new_post.product_information.fragment.ProductInformationFragment;

public class NewPostAdapter extends FragmentStatePagerAdapter {

    private ImagesInformationFragment mImagesInformationFragment = new ImagesInformationFragment();
    private ProductInformationFragment mProductInformationFragment = new ProductInformationFragment();
    private DescriptionInformationFragment mDescriptionInformationFragment = new DescriptionInformationFragment();
    private MapInformationFragment mMapInformationFragment = new MapInformationFragment();
    private NewPostSuccessFragment mNewPostSuccessFragment = new NewPostSuccessFragment();

    public NewPostAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg = null;
        switch (position){
            case 0:
                frg = mImagesInformationFragment;
                break;
            case 1:
                frg = mProductInformationFragment;
                break;
            case 2:
                frg = mDescriptionInformationFragment;
                break;
            case 3:
                frg = mMapInformationFragment;
                break;
            case 4:
                frg = mNewPostSuccessFragment;
                break;
        }
        return frg;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
