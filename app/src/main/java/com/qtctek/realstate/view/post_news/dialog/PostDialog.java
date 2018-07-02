package com.qtctek.realstate.view.post_news.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.view.post_news.activity.MainActivity;
import com.qtctek.realstate.view.post_news.fragment.MapPostNewsFragment;
import com.qtctek.realstate.view.post_detail.activity.PostDetailActivity;
import com.squareup.picasso.Picasso;

public class PostDialog extends Activity {

    private LinearLayout mLLItem;
    private TextView mTxvPrice;
    private TextView mTxvArea;
    private TextView mTxvDistrictProvinceCity;
    private TextView mTxvRooms;
    private TextView mTxvAddress;
    private ImageView mImvProduct;
    private Button mBtnCancel;

    private Product mProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        getDataFromIntent();

        try{
            initViews();
            setValue();
        }
        catch (java.lang.NullPointerException e){
            Toast.makeText(this, "Có lỗi xảy ra trong quá trình xử lí. Xin vui lòng thử" +
                    " lại sau!!!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void getDataFromIntent(){
        int position = getIntent().getIntExtra("position", -1);
        if(position == -1){
            Toast.makeText(this, "Có lỗi xảy ra trong qua trình đọc dữ liệu. Xin vui " +
                    "lòng thử lại sau", Toast.LENGTH_SHORT).show();;
        }
        else{
            try
            {
                this.mProduct = MapPostNewsFragment.ARR_POST.get(position).getProduct();
            }
            catch (java.lang.IndexOutOfBoundsException e){}
        }

    }

    private void initViews() throws NullPointerException{
        this.mLLItem = findViewById(R.id.ll_item);
        this.mTxvPrice = findViewById(R.id.txv_price);
        this.mTxvArea = findViewById(R.id.txv_area);
        this.mTxvDistrictProvinceCity = findViewById(R.id.txv_district_province_city);
        this.mTxvRooms = findViewById(R.id.txv_rooms);
        this.mTxvAddress = findViewById(R.id.txv_address);
        this.mImvProduct = findViewById(R.id.imv_product_avartar);
        this.mTxvAddress = findViewById(R.id.txv_address);
        this.mBtnCancel = findViewById(R.id.btn_cancel);

        this.mBtnCancel.setVisibility(View.VISIBLE);

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.mLLItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDialog.this, PostDetailActivity.class);
                intent.putExtra("post_id", mProduct.getId());
                startActivity(intent);
                finish();
            }
        });

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
        Picasso.with(this).load(urlImage).into(this.mImvProduct);

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
