package com.qtctek.aladin.view.post_news.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qtctek.aladin.R;
import com.qtctek.aladin.common.AppUtils;
import com.qtctek.aladin.common.Constant;
import com.qtctek.aladin.dto.Category;
import com.qtctek.aladin.dto.Place;
import com.qtctek.aladin.dto.Product;
import com.qtctek.aladin.dto.Room;
import com.qtctek.aladin.presenter.new_post.GetData.PresenterGetData;
import com.qtctek.aladin.view.new_post.interfaces.ViewHandleModelGetData;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.post_news.adapter.CategoryAdapter;
import com.qtctek.aladin.view.post_news.adapter.QualityAdapter;
import com.qtctek.aladin.view.post_news.interfaces.OnFromAdapter;

import java.util.ArrayList;

public class FilterFragment extends Fragment implements View.OnClickListener, View.OnKeyListener, ViewHandleModelGetData,
        OnFromAdapter, CompoundButton.OnCheckedChangeListener {

    private MainActivity mActivity;
    private View mView;

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
    private RadioButton mRdoToday;
    private RadioButton mRdoOneWeek;
    private RadioButton mRdoOneMonth;
    private RadioButton mRdoAll;
    private RecyclerView mRecyclerViewBathroom;
    private RecyclerView mRecyclerViewBedroom;

    private Dialog mDialog;
    private Dialog mLoadingDialog;

    private QualityAdapter mQualityBathroomAdapter;
    private QualityAdapter mQualityBedroomAdapter;

    private ArrayList<Room> mArrListQualityBathroom;
    private ArrayList<Room> mArrListQualityBedroom;

    private String mCategory = "";

    private RadioButton mRdoTimePost;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_filter, container, false);

        this.mActivity = (MainActivity)getActivity();

        this.mArrListQualityBathroom = AppUtils.getArrListQuality();
        this.mArrListQualityBedroom = AppUtils.getArrListQuality();

        createLoadingDialog();
        initViews();
        createRecyclerViewQuality();

        return mView;
    }

    private synchronized void initViews(){
        this.mTxvPrice = mView.findViewById(R.id.txv_price);
        this.mEdtMinPrice = mView.findViewById(R.id.edt_min_price);
        this.mEdtMaxPrice = mView.findViewById(R.id.edt_max_price);
        this.mBtnCancel = mView.findViewById(R.id.btn_cancel);
        this.mBtnSearch = mView.findViewById(R.id.btn_search);
        this.mTxvArchitecture = mView.findViewById(R.id.txv_architecture);
        this.mTxvType = mView.findViewById(R.id.txv_type);
        this.mBtnResetArchitecture = mView.findViewById(R.id.btn_reset_architecture);
        this.mBtnResetType = mView.findViewById(R.id.btn_reset_type);
        this.mChkForSale = mView.findViewById(R.id.chk_for_sale);
        this.mChkRent = mView.findViewById(R.id.chk_rent);
        this.mRecyclerViewBathroom = mView.findViewById(R.id.recycler_view_bathroom);
        this.mRecyclerViewBedroom = mView.findViewById(R.id.recycler_view_bedroom);
        this.mRdoToday = mView.findViewById(R.id.rdo_today);
        this.mRdoOneWeek = mView.findViewById(R.id.rdo_one_week);
        this.mRdoOneMonth = mView.findViewById(R.id.rdo_one_month);
        this.mRdoAll = mView.findViewById(R.id.rdo_all);

        this.mBtnSearch.setOnClickListener(this);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnResetArchitecture.setOnClickListener(this);
        this.mBtnResetType.setOnClickListener(this);
        this.mTxvArchitecture.setOnClickListener(this);
        this.mTxvType.setOnClickListener(this);
        this.mEdtMinPrice.setOnKeyListener(this);
        this.mEdtMaxPrice.setOnKeyListener(this);
        this.mRdoToday.setOnCheckedChangeListener(this);
        this.mRdoOneWeek.setOnCheckedChangeListener(this);
        this.mRdoOneMonth.setOnCheckedChangeListener(this);
        this.mRdoAll.setOnCheckedChangeListener(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerViewBathroom.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mActivity);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.mRecyclerViewBedroom.setLayoutManager(linearLayoutManager1);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(mActivity);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    public void initValue(){

        String formality = mActivity.formality;
        if(formality.equals(Constant.YES)){
            this.mChkForSale.setChecked(true);
            this.mChkRent.setChecked(false);
        }
        else if(formality.equals(Constant.NO)){
            this.mChkRent.setChecked(true);
            this.mChkForSale.setChecked(false);
        }
        else if(formality.equals(Constant.PERCENT)){
            this.mChkRent.setChecked(true);
            this.mChkForSale.setChecked(true);
        }

        String architecture = mActivity.architecture;
        String type = mActivity.type;
        String minPrice = mActivity.minPrice;
        String maxPrice = mActivity.maxPrice;

        if(!architecture.equals(Constant.PERCENT)){
            this.mBtnResetArchitecture.setBackgroundResource(R.drawable.icon_close_black_24dp);
        }
        else{
            architecture = getResources().getString(R.string.all);
        }
        if(!type.equals(Constant.PERCENT)){
            this.mBtnResetType.setBackgroundResource(R.drawable.icon_close_black_24dp);
        }
        else{
            type = getResources().getString(R.string.all);
        }

        StringBuilder stringBuilderMinPrice = new StringBuilder(minPrice);
        StringBuilder stringBuilderMaxPrice = new StringBuilder(maxPrice);

        stringBuilderMinPrice.delete(minPrice.length() - 6, minPrice.length());
        stringBuilderMaxPrice.delete(maxPrice.length() - 6, maxPrice.length());

        Long longMinPrice;
        try{
            longMinPrice = Long.valueOf(stringBuilderMinPrice.toString());
        }
        catch (NumberFormatException e){
            longMinPrice = 0L;
        }

        Long longMaxPrice;
        try{
            longMaxPrice = Long.valueOf(stringBuilderMaxPrice.toString());
        }
        catch (NumberFormatException e){
            longMaxPrice = 0L;
        }

        handleDisplayPrice(longMinPrice, longMaxPrice);

        if(mActivity.timePost == 0){
            this.mRdoToday.setChecked(true);
        }
        else if(mActivity.timePost == 7){
            this.mRdoOneWeek.setChecked(true);
        }
        else if(mActivity.timePost == 30){
            this.mRdoOneMonth.setChecked(true);
        }
        else{
            this.mRdoAll.setChecked(true);
        }

        this.mTxvArchitecture.setText(architecture);
        this.mTxvType.setText(type);
        this.mEdtMinPrice.setText(stringBuilderMinPrice.toString());
        this.mEdtMaxPrice.setText(stringBuilderMaxPrice.toString());

        this.mArrListQualityBathroom.get(mActivity.bathroom).setIsSelected(true);
        this.mArrListQualityBedroom.get(mActivity.bedroom).setIsSelected(true);
    }

    private synchronized void createRecyclerViewQuality(){
        this.mQualityBathroomAdapter = new QualityAdapter(mActivity, this.mArrListQualityBathroom, AppUtils.QUALITY_BATHROOM, this);
        this.mRecyclerViewBathroom.setAdapter(mQualityBathroomAdapter);

        this.mQualityBedroomAdapter = new QualityAdapter(mActivity, this.mArrListQualityBedroom, AppUtils.QUALITY_BEDROOM, this);
        this.mRecyclerViewBedroom.setAdapter(mQualityBedroomAdapter);
    }

     void setGetValueFilter(){

        String formality = Constant.PERCENT;
        String architecture = this.mTxvArchitecture.getText().toString();
        String type = this.mTxvType.getText().toString();

        if(mChkForSale.isChecked() && !mChkRent.isChecked()){
            formality = Constant.YES;;
        }
        else if(mChkRent.isChecked() && !mChkForSale.isChecked()){
            formality = Constant.NO;
        }
        if(type.equals(getResources().getString(R.string.all))){
            type = Constant.PERCENT;
        }
        if(architecture.equals(getResources().getString(R.string.all))){
            architecture = Constant.PERCENT;
        }

        mActivity.formality = formality;
        mActivity.architecture = architecture;
        mActivity.type = type;

        Long minPrice;
        try {
            minPrice = Long.valueOf(this.mEdtMinPrice.getText().toString());
        }
        catch (NumberFormatException e){
            minPrice = 0L;
        }

        mActivity.minPrice = minPrice + "000000";

        try {
            mActivity.maxPrice = Long.valueOf(this.mEdtMaxPrice.getText().toString()) + "000000";
        }
        catch (NumberFormatException e){
            mActivity.maxPrice = "999999000000";
        }

        if(mRdoToday.isChecked()){
            mActivity.timePost = 0;
        }
        else if(mRdoOneWeek.isChecked()){
            mActivity.timePost = 7;
        }
        else if(mRdoOneMonth.isChecked()){
            mActivity.timePost = 30;
        }
        else{
            mActivity.timePost = 60;
        }

         mActivity.handleFilter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                mActivity.cancelFilter();
                mActivity.getKeyboardHelper().hideKeyboard(mActivity);
                break;
            case R.id.btn_search:
                setGetValueFilter();
                break;
            case R.id.btn_reset_architecture:
                this.mTxvArchitecture.setText(getResources().getString(R.string.all));
                this.mBtnResetArchitecture.setBackgroundResource(R.drawable.icon_arrow_right_black_24dp);
                break;
            case R.id.btn_reset_type:
                this.mTxvType.setText(getResources().getString(R.string.all));
                this.mBtnResetType.setBackgroundResource(R.drawable.icon_arrow_right_black_24dp);
                break;
            case R.id.txv_architecture:
                this.mLoadingDialog.show();
                mCategory = Product.ARCHITECTURE;
                new PresenterGetData(this).handleGetCategoriesProduct(Product.TABLE_ARCHITECTURE, Product.ARCHITECTURE);
                break;
            case R.id.txv_type:
                this.mLoadingDialog.show();
                mCategory = Product.TYPE;
                new PresenterGetData(this).handleGetCategoriesProduct(Product.TABLE_TYPE, Product.TYPE);
                break;
            case R.id.txv_city:
                this.mLoadingDialog.show();
                new PresenterGetData(this).handleGetProvinceCity();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Long minPrice = 0L;
        Long maxPrice = 0L;

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
        }catch (NumberFormatException ignored){
        }

        try{
            maxPrice = Long.parseLong(this.mEdtMaxPrice.getText().toString());
        }catch (NumberFormatException ignored){
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
            return (float)price / 1000 + " " + getResources().getString(R.string.billion);
        }
        else{
            return price + " " + getResources().getString(R.string.million);
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

        CategoryAdapter categoryAdapter = new CategoryAdapter(mActivity, mArrCategory, getPositionSelected(mArrCategory));

        mDialog = new Dialog(mActivity);
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
                if(mCategory.equals(Product.ARCHITECTURE)){
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
        if(mActivity.architecture.equals(Product.ARCHITECTURE)){
            String text = this.mTxvArchitecture.getText().toString();
            if(text.equals(getResources().getString(R.string.all))){
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
            if(text.equals(getResources().getString(R.string.all))){
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
    public void onItemBathroomClick(int position) {
        this.mQualityBathroomAdapter.notifyDataSetChanged();
        this.mArrListQualityBathroom.get(mActivity.bathroom).setIsSelected(false);
        this.mArrListQualityBathroom.get(position).setIsSelected(true);


        mActivity.bathroom = position;
    }

    @Override
    public void onItemBedroomClick(int position) {
        this.mQualityBedroomAdapter.notifyDataSetChanged();
        this.mArrListQualityBedroom.get(mActivity.bedroom).setIsSelected(false);
        this.mArrListQualityBedroom.get(position).setIsSelected(true);


        mActivity.bedroom = position;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(mRdoTimePost != null){
            mRdoTimePost.setChecked(false);

        }
        mRdoTimePost = (RadioButton) buttonView;
    }
}

