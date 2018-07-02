package com.qtctek.realstate.view.post_news.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.CategoriesProduct;
import com.qtctek.realstate.dto.District;
import com.qtctek.realstate.dto.ProvinceCity;
import com.qtctek.realstate.presenter.new_post.GetData.PresenterGetData;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelGetData;
import com.qtctek.realstate.view.post_news.adapter.CategoriesProductAdapter;
import com.qtctek.realstate.view.post_news.adapter.DistrictAdapter;
import com.qtctek.realstate.view.post_news.adapter.ProvinceCityAdapter;

import java.util.ArrayList;

public class SearchDialog extends Activity implements View.OnClickListener, View.OnKeyListener, ViewHandleModelGetData {

    private TextView mTxvProvinceCity;
    private TextView mTxvDistrict;
    private TextView mTxvPrice;
    private EditText mEdtMinPrice;
    private EditText mEdtMaxPrice;
    private TextView mTxvCategoryProduct;
    private Button mBtnCancel;
    private Button mBtnSearch;
    private Button mBtnResetDistrict;
    private Button mBtnResetCategoryProduct;

    private Dialog mDialog;

    private int mProvinceCityId = 79;
    private String mLatlng = "10.823099, 106.629664";
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        createLoadingDialog();
        initViews();
        handleValueFromIntent();
    }

    private void initViews(){
        this.mTxvProvinceCity = findViewById(R.id.txv_province_city);
        this.mTxvDistrict = findViewById(R.id.txv_district);
        this.mTxvPrice = findViewById(R.id.txv_price);
        this.mEdtMinPrice = findViewById(R.id.edt_min_price);
        this.mEdtMaxPrice = findViewById(R.id.edt_max_price);
        this.mBtnCancel = findViewById(R.id.btn_cancel);
        this.mBtnSearch = findViewById(R.id.btn_search);
        this.mTxvCategoryProduct = findViewById(R.id.txv_category_product);
        this.mBtnResetCategoryProduct = findViewById(R.id.btn_reset_category_product);
        this.mBtnResetDistrict = findViewById(R.id.btn_reset_district);

        this.mBtnSearch.setOnClickListener(this);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnResetDistrict.setOnClickListener(this);
        this.mBtnResetCategoryProduct.setOnClickListener(this);
        this.mTxvProvinceCity.setOnClickListener(this);
        this.mTxvDistrict.setOnClickListener(this);
        this.mTxvCategoryProduct.setOnClickListener(this);
        this.mEdtMinPrice.setOnKeyListener(this);
        this.mEdtMaxPrice.setOnKeyListener(this);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(this);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void handleValueFromIntent(){
        Intent data = getIntent();

        String categoryProduct = data.getStringExtra("category_product");
        String provinceCity = data.getStringExtra("province_city");
        String district = data.getStringExtra("district");
        String minPrice = data.getStringExtra("min_price");
        String maxPrice = data.getStringExtra("max_price");


        if(!district.equals("%")){
            this.mBtnResetDistrict.setText("X");
        }
        else{
            district = "Tất cả";
        }
        if(!categoryProduct.equals("%")){
            this.mBtnResetCategoryProduct.setText("X");
        }
        else{
            categoryProduct = "Tất cả";
        }

        StringBuilder stringBuilderMinPrice = new StringBuilder(minPrice);
        StringBuilder stringBuilderMaxPrice = new StringBuilder(maxPrice);

        stringBuilderMinPrice.delete(minPrice.length() - 6, minPrice.length());
        stringBuilderMaxPrice.delete(maxPrice.length() - 6, maxPrice.length());

        Long longMinPrice;
        try{
            longMinPrice = Long.valueOf(stringBuilderMinPrice.toString());
        }
        catch (java.lang.NumberFormatException e){
            longMinPrice = Long.valueOf(0);
        }

        Long longMaxPrice;
        try{
            longMaxPrice = Long.valueOf(stringBuilderMaxPrice.toString());
        }
        catch (java.lang.NumberFormatException e){
            longMaxPrice = Long.valueOf(0);
        }

        handleDisplayPrice(longMinPrice, longMaxPrice);

        this.mTxvCategoryProduct.setText(categoryProduct);
        this.mTxvProvinceCity.setText(provinceCity);
        this.mTxvDistrict.setText(district);
        this.mEdtMinPrice.setText(stringBuilderMinPrice.toString());
        this.mEdtMaxPrice.setText(stringBuilderMaxPrice.toString());
    }

    private void handleSearch(){
        Intent intent = getIntent();

        String[] latlng = this.mLatlng.split(",");
        intent.putExtra("lat", latlng[0].trim());
        intent.putExtra("lng", latlng[1].trim());

        String categoryProduct = this.mTxvCategoryProduct.getText().toString().trim();
        String provinceCity = this.mTxvProvinceCity.getText().toString().trim();
        String district = this.mTxvDistrict.getText().toString().trim();
        if(categoryProduct.equals("Tất cả")){
            categoryProduct = "%";
        }
        if(district.equals("Tất cả")){
            district = "%";
        }


        intent.putExtra("province_city", provinceCity);
        intent.putExtra("district", district);
        intent.putExtra("category_product", categoryProduct);

        Long minPrice = Long.valueOf(0);
        try {
            minPrice = Long.valueOf(this.mEdtMinPrice.getText().toString());
        }
        catch (java.lang.NumberFormatException e){
            Log.d("ttt", e.toString());
            minPrice = Long.valueOf(0);
        }
        intent.putExtra("min_price", minPrice + "000000");

        Long maxPrice = Long.valueOf(0);
        try {
            maxPrice = Long.valueOf(this.mEdtMaxPrice.getText().toString());
            intent.putExtra("max_price", maxPrice + "000000");
        }
        catch (java.lang.NumberFormatException e){
            maxPrice = Long.valueOf(0);
            intent.putExtra("max_price", "1000000000000000");
        }
        Log.d("ttt", minPrice + "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_search:
                handleSearch();
                break;
            case R.id.btn_reset_category_product:
                this.mTxvCategoryProduct.setText("Tất cả");
                this.mBtnResetCategoryProduct.setText(">");
                break;
            case R.id.btn_reset_district:
                this.mTxvDistrict.setText("Tất cả");
                mBtnResetDistrict.setText(">");
                break;
            case R.id.txv_category_product:
                this.mLoadingDialog.show();
                new PresenterGetData(this).handleGetCategoriesProduct();
                break;
            case R.id.txv_province_city:
                this.mLoadingDialog.show();
                new PresenterGetData(this).handleGetProvinceCity();
                break;
            case R.id.txv_district:
                if(mProvinceCityId == 0){
                    Toast.makeText(this, "Chọn Tỉnh/Thành phố trước", Toast.LENGTH_SHORT).show();
                }
                else{
                    this.mLoadingDialog.show();
                    new PresenterGetData(this).handleGetDistrict(mProvinceCityId);
                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Long minPrice = Long.valueOf(0);
        Long maxPrice = Long.valueOf(0);

        switch (v.getId()){
            case R.id.edt_min_price:
                if(this.mEdtMinPrice.getText().toString().length() >= 10 && keyCode >= 7 && keyCode <= 16){
                    return true;
                }
                break;
            case R.id.edt_max_price:
                if(this.mEdtMaxPrice.getText().toString().length() >= 10 && keyCode >= 7 && keyCode <= 16){
                    return true;
                }
                break;
        }

        handleDisplayPrice(minPrice, maxPrice);

        return false;
    }

    private void handleDisplayPrice(Long minPrice, Long maxPrice){
        try{
            minPrice = Long.parseLong(this.mEdtMinPrice.getText().toString());
        }catch (java.lang.NumberFormatException e){
        }

        try{
            maxPrice = Long.parseLong(this.mEdtMaxPrice.getText().toString());
        }catch (java.lang.NumberFormatException e){
        }

        if(minPrice == 0 && maxPrice > 0){
            String text = "< " + getStringPrice(maxPrice);
            this.mTxvPrice.setText(text);
        }
        else if(minPrice > 0 && maxPrice == 0){
            String text = "> " + getStringPrice(minPrice);
            this.mTxvPrice.setText(text);
        }
        else if(minPrice > 0 && maxPrice > 0){
            String text = getStringPrice(minPrice) + " - " + getStringPrice(maxPrice);
            this.mTxvPrice.setText(text);
        }
        else{
            this.mTxvPrice.setText("");
        }
    }

    private String getStringPrice(Long price){
        if(price >= 1000){
            return (float)price / 1000 + " tỷ";
        }
        else{
            return price + " triệu";
        }
    }

    @Override
    public void onGetProvinceCity(boolean status, final ArrayList<ProvinceCity> mArr) {
        ProvinceCityAdapter provinceCityAdapter = new ProvinceCityAdapter(this, mArr);

        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_list);
        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(provinceCityAdapter);

        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        this.mLoadingDialog.dismiss();
        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProvinceCity provinceCity = mArr.get(position);
                mTxvProvinceCity.setText(provinceCity.getName());
                mProvinceCityId = provinceCity.getId();

                mLatlng = provinceCity.getLatlng();

                mTxvDistrict.setText("Tất cả");
                mBtnResetDistrict.setText(">");
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    @Override
    public void onGetDistrict(boolean status, final ArrayList<District> mArr) {

        DistrictAdapter district = new DistrictAdapter(this, mArr);

        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_list);
        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(district);

        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTxvDistrict.setText(mArr.get(position).getName());

                mLatlng = mArr.get(position).getLatlng();

                mBtnResetDistrict.setText("X");
                mDialog.dismiss();
            }
        });

        this.mLoadingDialog.dismiss();

        mDialog.show();
    }

    @Override
    public void onGetCategoriesProduct(boolean status, final ArrayList<CategoriesProduct> mArr) {

        CategoriesProductAdapter categoriesProductAdapter = new CategoriesProductAdapter(this, mArr);

        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_list);
        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(categoriesProductAdapter);

        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTxvCategoryProduct.setText(mArr.get(position).getName());

                mBtnResetCategoryProduct.setText("X");
                mDialog.dismiss();
            }
        });

        this.mLoadingDialog.dismiss();
        mDialog.show();
    }
}

