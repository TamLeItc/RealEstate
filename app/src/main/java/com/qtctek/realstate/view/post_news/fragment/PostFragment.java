package com.qtctek.realstate.view.post_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Product1;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.squareup.picasso.Picasso;

public class PostFragment extends Fragment {

    private View mView;

    private LinearLayout mLLItem;
    private TextView mTxvPrice;
    private TextView mTxvArea;
    private TextView mTxvDistrictProvinceCity;
    private TextView mTxvRooms;
    private TextView mTxvAddress;
    private ImageView mImvProduct;
    private Button mBtnCancel;

    private Product1 mProduct;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.item_post, container, false);

        this.mProduct = MapPostNewsFragment.ARR_POST.get(MapPostNewsFragment.POSITION).getProduct();

        try {
            initViews();
            setValue();
        }
        catch (Exception e){
            Toast.makeText(getContext(), "Có lỗi xảy ra trong quá trình xử lí", Toast.LENGTH_SHORT).show();
        }

        return this.mView;
    }

    private void initViews() throws NullPointerException{
        this.mLLItem = mView.findViewById(R.id.ll_item);
        this.mTxvPrice = mView.findViewById(R.id.txv_price);
        this.mTxvArea = mView.findViewById(R.id.txv_area);
        this.mTxvDistrictProvinceCity = mView.findViewById(R.id.txv_district_province_city);
        this.mTxvRooms = mView.findViewById(R.id.txv_rooms);
        this.mTxvAddress = mView.findViewById(R.id.txv_address);
        this.mImvProduct = mView.findViewById(R.id.imv_product_avartar);
        this.mTxvAddress = mView.findViewById(R.id.txv_address);
        this.mBtnCancel = mView.findViewById(R.id.btn_cancel);

        this.mBtnCancel.setVisibility(View.VISIBLE);

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
            }
        });


        this.mLLItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
                intent.putExtra("post_id", mProduct.getId());
                startActivity(intent);
            }
        });

    }

    private void removeFragment(){
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    private void setValue() throws NullPointerException{

        String strArea = mProduct.getArea() + "m²";
        this.mTxvArea.setText(strArea);

        String temp = mProduct.getDistrict() + ", " + mProduct.getProvinceCity();
        this.mTxvDistrictProvinceCity.setText(temp);

        temp = mProduct.getBedrooms() + " phòng ngủ, " + mProduct.getBathrooms() + " phòng tắm.";
        this.mTxvRooms.setText(temp);

        this.mTxvAddress.setText(this.mProduct.getAddress());

        String urlImage = MainActivity.HOST + "/real_estate/images/" + mProduct.getId() + "_avartar.jpg";
        Picasso.with(getContext()).load(urlImage).into(this.mImvProduct);

        String strPrice = "";
        float price = (float)mProduct.getPrice();
        if(price > 1000000000){
            price /= 1000000000;
            strPrice = Math.round( price * 100.0)/ 100.0 + " tỉ";
        }
        else if(price > 1000000){
            price /= 1000000;
            strPrice = Math.round( price * 100.0)/ 100.0 + " triệu";
        }
        else if(price > 1000){
            price /= 1000;
            strPrice = Math.round( price * 100.0)/ 100.0 + "K";
        }
        else{
            strPrice = "Thương lượng";
        }
        this.mTxvPrice.setText(strPrice);

        this.mTxvAddress.setText(mProduct.getAddress());
    }

}
