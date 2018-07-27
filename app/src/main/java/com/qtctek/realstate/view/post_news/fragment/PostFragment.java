package com.qtctek.realstate.view.post_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.user_control.saved_post.PresenterSavedPost;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.qtctek.realstate.view.user_control.saved_post.ViewHandleSavedPost;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PostFragment extends Fragment implements View.OnClickListener, ViewHandleSavedPost {

    private View mView;

    private RelativeLayout mRLItem;
    private TextView mTxvPrice;
    private TextView mTxvArea;
    private TextView mTxvDistrictProvinceCity;
    private TextView mTxvBedroom;
    private TextView mTxvBathroom;
    private TextView mTxvAddress;
    private TextView mTxvAMonth;
    private TextView mTxvTitle;
    private ImageView mImvProduct;
    private ImageButton mImvCancel;
    public static ImageButton IMB_SAVE;
    private ProgressBar mProgressBar;

    private Product mProduct;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.item_post_2, container, false);

        try {

            MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) ((MainActivity)getActivity()).getSupportFragmentManager().getFragments().get(0);

            this.mProduct = mapPostNewsFragment.arrProduct.get(MapPostNewsFragment.POSITION);
            initViews();
            setValue();
        }
        catch (Exception e){
            removeFragment();
            ((MainActivity)getActivity()).toastHelper.toast("Lỗi xử lí", ToastHelper.LENGTH_SHORT);
        }

        return this.mView;
    }

    private void initViews() throws NullPointerException{
        this.mRLItem = mView.findViewById(R.id.rl_item);
        this.mTxvPrice = mView.findViewById(R.id.txv_price);
        this.mTxvArea = mView.findViewById(R.id.txv_area);
        this.mTxvDistrictProvinceCity = mView.findViewById(R.id.txv_district_province_city);
        this.mTxvBedroom = mView.findViewById(R.id.txv_bedroom);
        this.mTxvBathroom = mView.findViewById(R.id.txv_bathroom);
        this.mTxvAddress = mView.findViewById(R.id.txv_address);
        this.mTxvTitle = mView.findViewById(R.id.txv_title);
        this.mImvProduct = mView.findViewById(R.id.imb_product_avartar);
        this.mTxvAddress = mView.findViewById(R.id.txv_address);
        this.mTxvAMonth = mView.findViewById(R.id.txv_a_month);
        this.mImvCancel = mView.findViewById(R.id.imv_cancel);
        this.IMB_SAVE = mView.findViewById(R.id.imb_save);
        this.mProgressBar = mView.findViewById(R.id.progress_bar);

        this.mImvCancel.setVisibility(View.VISIBLE);

        this.mImvCancel.setOnClickListener(this);
        this.IMB_SAVE.setOnClickListener(this);
        this.mRLItem.setOnClickListener(this);

    }

    private void removeFragment(){
        try{
            getFragmentManager().beginTransaction().remove(this).commit();
        }
        catch (java.lang.NullPointerException e){
        }
    }

    private void setValue() throws NullPointerException{

        String temp = mProduct.getDistrict() + ", " + mProduct.getCity();
        this.mTxvDistrictProvinceCity.setText(temp);

        this.mTxvTitle.setText(mProduct.getTitle());
        this.mTxvBathroom.setText(mProduct.getBathroom() + "");
        this.mTxvBedroom.setText(mProduct.getBedroom() + "");
        this.mTxvArea.setText(mProduct.getArea() + "");
        this.mTxvAddress.setText(this.mProduct.getAddress());

        String urlImage = MainActivity.WEB_SERVER + "images/" + mProduct.getThumbnail();
        Picasso.with(getContext()).load(urlImage).into(this.mImvProduct, new Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mProgressBar.setVisibility(View.GONE);
                mImvProduct.setImageResource(R.drawable.icon_product);
            }
        });


        this.mTxvPrice.setText(AppUtils.getStringPrice(mProduct.getPrice(), AppUtils.LONG_PRICE));
        this.mTxvAddress.setText(mProduct.getAddress());

        if(mProduct.getFormality().equals("no")){
            this.mTxvAMonth.setVisibility(View.VISIBLE);
        }
        else{
            this.mTxvAMonth.setVisibility(View.GONE);
        }

        setBackgroundForButton();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_cancel:
                ((MainActivity)getActivity()).expandableLayoutProduct.collapse();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeFragment();
                    }
                }, 300);
                break;
            case R.id.rl_item:
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
                intent.putExtra("product_id", mProduct.getId());
                intent.putExtra("save", mProduct.getIsSaved());
                intent.putExtra("position", MapPostNewsFragment.POSITION);
                startActivity(intent);
                break;
            case R.id.imb_save:
                if(mProduct.getIsSaved()){
                    try {
                        ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.remove(mProduct.getId() + "");
                    }
                    catch (Exception e){}
                    new PresenterSavedPost(this).handleUpdateDataProductIds(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID, getContext());
                }
                else{
                    ListPostNewsFragment.LIST_SAVED_PRODUCT_ID.put(mProduct.getId() + "", mProduct.getId() + "");
                    new PresenterSavedPost(this).handleUpdateDataProductIds(ListPostNewsFragment.LIST_SAVED_PRODUCT_ID, getContext());

                }
        }
    }

    private void setBackgroundForButton(){
        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) ((MainActivity)getActivity()).getSupportFragmentManager().getFragments().get(0);

        if(mapPostNewsFragment.arrProduct.get(MapPostNewsFragment.POSITION).getIsSaved()){
            this.IMB_SAVE.setImageResource(R.drawable.icon_favorite_red_24dp);
        }
        else{
            this.IMB_SAVE.setImageResource(R.drawable.icon_favorite_border_white_24dp);
        }
    }

    @Override
    public void onHandleDataProductIdsSuccessful(HashMap<String, String> list) {

    }

    @Override
    public void onHandleDataProductIdsError(String error) {

    }

    @Override
    public void onHandleUpdateProductIdListSuccessful() {

        MapPostNewsFragment mapPostNewsFragment = (MapPostNewsFragment) ((MainActivity)getActivity()).getSupportFragmentManager().getFragments().get(0);
        if(this.mProduct.getIsSaved()){
            mapPostNewsFragment.arrProduct.get(MapPostNewsFragment.POSITION).setIsSaved(false);
        }
        else{
            mapPostNewsFragment.arrProduct.get(MapPostNewsFragment.POSITION).setIsSaved(true);
        }
        setBackgroundForButton();
        mapPostNewsFragment.handlePostList(mapPostNewsFragment.arrProduct);
    }

    @Override
    public void onHandleUpdateProductIdListError(String e) {
        Toast.makeText(getContext(), "Thực hiện thất bại", Toast.LENGTH_SHORT);
    }

    @Override
    public void onHandleSavedProductListSuccessful(ArrayList<Product> mArrListProduct) {

    }

    @Override
    public void onHandleSavedProductListError(String error) {

    }

    @Override
    public void onDestroyView() {

        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }
}
