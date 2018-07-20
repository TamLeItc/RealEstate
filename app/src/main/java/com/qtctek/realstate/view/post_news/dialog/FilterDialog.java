package com.qtctek.realstate.view.post_news.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.AppUtils;
import com.qtctek.realstate.dto.Category;
import com.qtctek.realstate.dto.Place;
import com.qtctek.realstate.dto.Room;
import com.qtctek.realstate.presenter.new_post.GetData.PresenterGetData;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelGetData;
import com.qtctek.realstate.view.post_news.adapter.CategoryAdapter;
import com.qtctek.realstate.view.post_news.adapter.QualityAdapter;
import com.qtctek.realstate.view.post_news.interfaces.OnFromAdapter;

import java.util.ArrayList;

public class FilterDialog extends Activity implements View.OnClickListener, View.OnKeyListener, ViewHandleModelGetData,
        OnFromAdapter{

    private TextView mTxvPrice;
    private EditText mEdtMinPrice;
    private EditText mEdtMaxPrice;
    private TextView mTxvArchitecture;
    private TextView mTxvType;
    private Button mBtnCancel;
    private Button mBtnSearch;
    private Button mBtnResetArchitecture;
    private Button mBtnResetType;
    private CheckBox mChkForSale;
    private CheckBox mChkRent;
    private RecyclerView mRecyclerViewBathroom;
    private RecyclerView mRecyclerViewBedroom;

    private Dialog mDialog;
    private Dialog mLoadingDialog;

    private QualityAdapter mQualityBathroomAdapter;
    private QualityAdapter mQualityBedroomAdapter;

    private ArrayList<Room> mArrListQualityBathroom;
    private ArrayList<Room> mArrListQualityBedroom;

    private String mCategory = "";
    public static int QUALITY_BATHROOM = 0;
    public static int QUALITY_BEDROOM = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.mArrListQualityBathroom = AppUtils.getArrListQuality();
        this.mArrListQualityBedroom = AppUtils.getArrListQuality();

        createLoadingDialog();
        initViews();
        createRecyclerViewQuality();
        handleValueFromIntent();
    }

    private synchronized void initViews(){
        this.mTxvPrice = findViewById(R.id.txv_price);
        this.mEdtMinPrice = findViewById(R.id.edt_min_price);
        this.mEdtMaxPrice = findViewById(R.id.edt_max_price);
        this.mBtnCancel = findViewById(R.id.btn_cancel);
        this.mBtnSearch = findViewById(R.id.btn_search);
        this.mTxvArchitecture = findViewById(R.id.txv_architecture);
        this.mTxvType = findViewById(R.id.txv_type);
        this.mBtnResetArchitecture = findViewById(R.id.btn_reset_architecture);
        this.mBtnResetType = findViewById(R.id.btn_reset_type);
        this.mChkForSale = findViewById(R.id.chk_for_sale);
        this.mChkRent = findViewById(R.id.chk_rent);
        this.mRecyclerViewBathroom = findViewById(R.id.recycler_view_bathroom);
        this.mRecyclerViewBedroom = findViewById(R.id.recycler_view_bedroom);

        this.mBtnSearch.setOnClickListener(this);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnResetArchitecture.setOnClickListener(this);
        this.mBtnResetType.setOnClickListener(this);
        this.mTxvArchitecture.setOnClickListener(this);
        this.mTxvType.setOnClickListener(this);
        this.mEdtMinPrice.setOnKeyListener(this);
        this.mEdtMaxPrice.setOnKeyListener(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerViewBathroom.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplication());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerViewBedroom.setLayoutManager(linearLayoutManager1);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(this);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void handleValueFromIntent(){
        Intent data = getIntent();

        String formality = data.getStringExtra("formality");
        if(formality.equals("yes")){
            this.mChkForSale.setChecked(true);
        }
        else if(formality.equals("no")){
            this.mChkRent.setChecked(true);
        }
        else if(formality.equals("%")){
            this.mChkRent.setChecked(true);
            this.mChkForSale.setChecked(true);
        }
        String architecture = data.getStringExtra("architecture");
        String type = data.getStringExtra("type");
        String minPrice = data.getStringExtra("min_price");
        String maxPrice = data.getStringExtra("max_price");

        if(!architecture.equals("%")){
            this.mBtnResetArchitecture.setBackgroundResource(R.drawable.icon_close_black_24dp);
        }
        else{
            architecture = "Tất cả";
        }
        if(!type.equals("%")){
            this.mBtnResetType.setBackgroundResource(R.drawable.icon_close_black_24dp);
        }
        else{
            type = "Tất cả";
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

        this.mTxvArchitecture.setText(architecture);
        this.mTxvType.setText(type);
        this.mEdtMinPrice.setText(stringBuilderMinPrice.toString());
        this.mEdtMaxPrice.setText(stringBuilderMaxPrice.toString());
        this.QUALITY_BATHROOM = (data.getIntExtra(AppUtils.QUALITY_BATHROOM, 0));
        this.QUALITY_BEDROOM = (data.getIntExtra(AppUtils.QUALITY_BEDROOM, 0));

        this.mArrListQualityBathroom.get(QUALITY_BATHROOM).setIsSelected(true);
        this.mArrListQualityBedroom.get(QUALITY_BEDROOM).setIsSelected(true);
    }

    private synchronized void createRecyclerViewQuality(){
        this.mQualityBathroomAdapter = new QualityAdapter(getApplication(), this.mArrListQualityBathroom, AppUtils.QUALITY_BATHROOM, this);
        this.mRecyclerViewBathroom.setAdapter(mQualityBathroomAdapter);

        this.mQualityBedroomAdapter = new QualityAdapter(getApplication(), this.mArrListQualityBedroom, AppUtils.QUALITY_BEDROOM, this);
        this.mRecyclerViewBedroom.setAdapter(mQualityBedroomAdapter);
    }

    private void handleSearch(){
        Intent intent = getIntent();

        String formality = "%";
        String architecture = this.mTxvArchitecture.getText().toString();
        String type = this.mTxvType.getText().toString();

        if(mChkForSale.isChecked() && !mChkRent.isChecked()){
            formality = "yes";
        }
        else if(mChkRent.isChecked() && !mChkForSale.isChecked()){
            formality = "no";
        }
        if(type.equals("Tất cả")){
            type = "%";
        }
        if(architecture.equals("Tất cả")){
            architecture = "%";
        }

        intent.putExtra("formality", formality);
        intent.putExtra("architecture", architecture);
        intent.putExtra("type", type);
        intent.putExtra(AppUtils.QUALITY_BATHROOM, QUALITY_BATHROOM);
        intent.putExtra(AppUtils.QUALITY_BEDROOM, QUALITY_BEDROOM);

        Long minPrice;
        try {
            minPrice = Long.valueOf(this.mEdtMinPrice.getText().toString());
        }
        catch (java.lang.NumberFormatException e){
            minPrice = Long.valueOf(0);
        }
        intent.putExtra("min_price", minPrice + "000000");

        Long maxPrice;
        try {
            maxPrice = Long.valueOf(this.mEdtMaxPrice.getText().toString());
            intent.putExtra("max_price", maxPrice + "000000");
        }
        catch (java.lang.NumberFormatException e){
            maxPrice = Long.valueOf(0);
            intent.putExtra("max_price", "1000000000000000");
        }
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
            case R.id.btn_reset_architecture:
                this.mTxvArchitecture.setText("Tất cả");
                this.mBtnResetArchitecture.setBackgroundResource(R.drawable.icon_arrow_right_black_24dp);
                break;
            case R.id.btn_reset_type:
                this.mTxvType.setText("Tất cả");
                this.mBtnResetType.setBackgroundResource(R.drawable.icon_arrow_right_black_24dp);
                break;
            case R.id.txv_architecture:
                this.mLoadingDialog.show();
                mCategory = "architecture";
                new PresenterGetData(this).handleGetCategoriesProduct("tbl_architecture", "architecture");
                break;
            case R.id.txv_type:
                this.mLoadingDialog.show();
                mCategory = "type";
                new PresenterGetData(this).handleGetCategoriesProduct("tbl_type", "type");
                break;
            case R.id.txv_city:
                this.mLoadingDialog.show();
                new PresenterGetData(this).handleGetProvinceCity();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Long minPrice = Long.valueOf(0);
        Long maxPrice = Long.valueOf(0);

        boolean status = false;
        switch (v.getId()){
            case R.id.edt_min_price:
                if(this.mEdtMinPrice.getText().toString().length() >= 6 && keyCode >= 7 && keyCode <= 16){
                    status = true;
                }
                break;
            case R.id.edt_max_price:
                if(this.mEdtMaxPrice.getText().toString().length() >= 6 && keyCode >= 7 && keyCode <= 16){
                    status = true;
                }
                break;
        }

        handleDisplayPrice(minPrice, maxPrice);

        return status;
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
    public void onGetCityList(boolean status, final ArrayList<Place> mArrPlace) {
    }

    @Override
    public void onGetDistrictList(boolean status, final ArrayList<Place> mArrPlace) {
    }

    @Override
    public void onGetCategoriesProduct(boolean status, final ArrayList<Category> mArrCategory) {

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, mArrCategory, getPositionSelected(mArrCategory));

        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_list);
        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(categoryAdapter);

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
                if(mCategory.equals("architecture")){
                    mTxvArchitecture.setText(mArrCategory.get(position).getName());
                    mBtnResetArchitecture.setBackgroundResource(R.drawable.icon_close_black_24dp);
                }
                else{
                    mTxvType.setText(mArrCategory.get(position).getName());
                    mBtnResetType.setBackgroundResource(R.drawable.icon_close_black_24dp);
                }
                mDialog.dismiss();
            }
        });

        this.mLoadingDialog.dismiss();
        mDialog.show();
    }

    private int getPositionSelected(ArrayList<Category> mArrCategory){
        if(mCategory.equals("architecture")){
            String text = this.mTxvArchitecture.getText().toString();
            if(text.equals("Tất cả")){
                return -1;
            }
            else{
                for(int i = 0; i < mArrCategory.size(); i++){
                    if(mArrCategory.equals(text)){
                        return i;
                    }
                }
            }
        }
        else{
            String text = this.mTxvType.getText().toString();
            if(text.equals("Tất cả")){
                return -1;
            }
            else{
                for(int i = 0; i < mArrCategory.size(); i++){
                    if(mArrCategory.equals(text)){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    protected void onStop() {

        Runtime.getRuntime().gc();
        System.gc();

        super.onStop();
    }

    @Override
    public void onItemBathroomClick(int position) {
        this.mQualityBathroomAdapter.notifyDataSetChanged();
        this.mArrListQualityBathroom.get(QUALITY_BATHROOM).setIsSelected(false);
        this.mArrListQualityBathroom.get(position).setIsSelected(true);


        this.QUALITY_BATHROOM = position;
    }

    @Override
    public void onItemBedroomClick(int position) {
        this.mQualityBedroomAdapter.notifyDataSetChanged();
        this.mArrListQualityBedroom.get(QUALITY_BEDROOM).setIsSelected(false);
        this.mArrListQualityBedroom.get(position).setIsSelected(true);


        this.QUALITY_BEDROOM = position;
    }
}

