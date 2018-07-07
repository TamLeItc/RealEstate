package com.qtctek.realstate.view.new_post.product_information;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.qtctek.realstate.dto.Category;
import com.qtctek.realstate.dto.Place;
import com.qtctek.realstate.presenter.new_post.GetData.PresenterGetData;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelGetData;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.post_news.adapter.CategoriesProductAdapter;
import com.qtctek.realstate.view.post_news.adapter.DistrictAdapter;
import com.qtctek.realstate.view.post_news.adapter.ProvinceCityAdapter;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

import java.util.ArrayList;

public class ProductInformationFragment extends Fragment implements View.OnClickListener, ViewHandleModelGetData,
        ViewHandleModelNewPost, View.OnKeyListener {

    private TextView mTxvArchitecture;
    private TextView mTxvType;
    private TextView mTxvProvinceCity;
    private TextView mTxvDistrict;
    private EditText mEdtAddress;
    private EditText mEdtPrices;
    private EditText mEdtArea;
    private EditText mEdtBathrooms;
    private EditText mEdtBedrooms;

    private TextView mTxvAmenities;
    private Button mBtnNextTo;
    private Dialog mDialog;

    private PresenterGetData mPresenterGetData;

    private View mView;
    private Dialog mLoadingDialog;

    private String mCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_information, container, false);

        mDialog = new Dialog(getContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        createLoadingDialog();
        initViews();
        handleStart();
        this.mPresenterGetData = new PresenterGetData(this);

        return mView;
    }

    private void initViews(){
        this.mTxvArchitecture = mView.findViewById(R.id.txv_architecture);
        this.mTxvType = mView.findViewById(R.id.txv_type);
        this.mTxvProvinceCity = mView.findViewById(R.id.txv_province_city);
        this.mTxvDistrict = mView.findViewById(R.id.txv_district);
        this.mTxvAmenities = mView.findViewById(R.id.txv_amenities);
        this.mEdtAddress = mView.findViewById(R.id.edt_address);
        this.mEdtPrices = mView.findViewById(R.id.edt_price);
        this.mEdtBathrooms = mView.findViewById(R.id.edt_bathrooms);
        this.mEdtBedrooms = mView.findViewById(R.id.edt_bedrooms);
        this.mEdtArea = mView.findViewById(R.id.edt_area);
        this.mBtnNextTo = mView.findViewById(R.id.btn_next_to);

        this.mBtnNextTo.setOnClickListener(this);
        this.mTxvArchitecture.setOnClickListener(this);
        this.mTxvType.setOnClickListener(this);
        this.mTxvAmenities.setOnClickListener(this);
        this.mTxvProvinceCity.setOnClickListener(this);
        this.mTxvDistrict.setOnClickListener(this);
        this.mEdtAddress.setOnKeyListener(this);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(getContext());
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void handleStart(){
        this.mTxvArchitecture.setText(NewPostActivity.PRODUCT.getArchitecture());
        this.mTxvProvinceCity.setText(NewPostActivity.PRODUCT.getCity());
        this.mTxvDistrict.setText(NewPostActivity.PRODUCT.getDistrict());
        this.mEdtAddress.setText(NewPostActivity.PRODUCT.getAddress());
        this.mEdtBathrooms.setText(NewPostActivity.PRODUCT.getBathroom() + "");
        this.mEdtBedrooms.setText(NewPostActivity.PRODUCT.getBedroom() + "");
        this.mEdtPrices.setText(NewPostActivity.PRODUCT.getPrice() + "");
        this.mEdtArea.setText(NewPostActivity.PRODUCT.getArea() + "");
        this.mTxvAmenities.setText(NewPostActivity.PRODUCT.getAmenities());
        this.mTxvType.setText(NewPostActivity.PRODUCT.getType());
    }

    private void handleSaveProductInformation(){

        if(this.mEdtBathrooms.getText().toString().trim().equals("")){
            this.mEdtBathrooms.setText("0");
        }
        if(this.mEdtBedrooms.getText().toString().trim().equals("")){
            this.mEdtBedrooms.setText("0");
        }
        if(this.mEdtPrices.getText().toString().trim().equals("")){
            this.mEdtPrices.setText("0");
        }

        try{
            NewPostActivity.PRODUCT.setAddress(this.mEdtAddress.getText().toString().trim());
            NewPostActivity.PRODUCT.setBathroom(Integer.parseInt(this.mEdtBathrooms.getText().toString().trim()));
            NewPostActivity.PRODUCT.setBedroom(Integer.parseInt(this.mEdtBedrooms.getText().toString().trim()));
            NewPostActivity.PRODUCT.setPrice(Long.valueOf(this.mEdtPrices.getText().toString().trim()));
            NewPostActivity.PRODUCT.setArea(Float.parseFloat(this.mEdtArea.getText().toString().trim()));
            NewPostActivity.PRODUCT.setAmenities(this.mTxvAmenities.getText().toString().trim());

            mLoadingDialog.show();
            new PresenterNewPost(this).handleUpdateProductInformation(NewPostActivity.PRODUCT);

        }
        catch (Exception e){
            Toast.makeText(getContext(), "Dữ liệu không chính xác. Xin vui kiểm tra lại!!!", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txv_type:
                mLoadingDialog.show();
                mPresenterGetData.handleGetCategoriesProduct("tbl_type", "type");
                this.mCategory = "type";
            case R.id.txv_amenities:
                mLoadingDialog.show();
                mPresenterGetData.handleGetCategoriesProduct("tbl_amenities", "amenities");
                this.mCategory = "amenities";
            case R.id.txv_architecture:
                mLoadingDialog.show();
                mPresenterGetData.handleGetCategoriesProduct("tbl_architecture", "architecture");
                this.mCategory = "architecture";
                break;
            case R.id.txv_province_city:
                mLoadingDialog.show();
                mPresenterGetData.handleGetProvinceCity();
                break;
            case R.id.txv_district:
                mLoadingDialog.show();
                mPresenterGetData.handleGetDistrict(NewPostActivity.PRODUCT.getCityId());
                break;
            case R.id.btn_next_to:
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(2);
                break;
            case R.id.btn_save_continue:
                handleSaveProductInformation();
                break;
        }
    }

    @Override
    public void onGetProvinceCity(boolean status, final ArrayList<Place> mArr) {
        PlaceAdapter provinceCityAdapter = new PlaceAdapter( getContext(), mArr);

        mDialog.setContentView(R.layout.dialog_list);

        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        txvTitle.setText("Chọn tỉnh/thành phố");

        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(provinceCityAdapter);



        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mLoadingDialog.dismiss();
        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTxvProvinceCity.setText(mArr.get(position).getName());
                mDialog.dismiss();
                NewPostActivity.PRODUCT.setCityId(mArr.get(position).getId());
                mTxvDistrict.setText("");
                NewPostActivity.PRODUCT.setDistrictId(0);
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    @Override
    public void onGetDistrict(boolean status, final ArrayList<Place> mArr) {

        mLoadingDialog.dismiss();

        DistrictAdapter district = new DistrictAdapter( getContext(), mArr);

        mDialog.setContentView(R.layout.dialog_list);

        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        txvTitle.setText("Chọn quận/huyện");

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
                NewPostActivity.PRODUCT.setDistrictId(mArr.get(position).getId());
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    @Override
    public void onGetCategoriesProduct(boolean status, ArrayList<Category> mArrCategory) {

        mLoadingDialog.dismiss();
        if(this.mCategory.equals("amenities")){

        }
        else{

        }

    }

    private void handleDisplayCategoryAmenityProduct(final ArrayList<Category> mArrCategory){

        final String amenity = NewPostActivity.PRODUCT.getAmenities();
        String[] amenities = amenity.split(",");

        ArrayList<String> mArrSelected = new ArrayList<>();
        for(int i = 0; i < amenities.length; i++){
            mArrSelected.add(amenities[i].trim());
        }

        AmenityAdapter amenityAdapter = new AmenityAdapter(getContext(), mArrCategory, mArrSelected);

        mDialog.setContentView(R.layout.dialog_list);

        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        txvTitle.setText("Chọn tiện ích");

        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(amenityAdapter);

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
                if(NewPostActivity.PRODUCT.getAmenities().trim().equals("")){
                    NewPostActivity.PRODUCT.setAmenities(mArrCategory.get(position).getName());
                }
                else{
                    String amenity1 = NewPostActivity.PRODUCT.getAmenities() + ", " + mArrCategory.get(position).getName();
                    NewPostActivity.PRODUCT.setAmenities(amenity1);
                    mTxvAmenities.setText( NewPostActivity.PRODUCT.getAmenities());

                }
            }
        });

        mDialog.show();
    }

    private void handleDisplayCategoryProduct(final ArrayList<Category> mArrCategory){
        CategoriesProductAdapter categoriesProductAdapter = new CategoriesProductAdapter(
                getContext(), mArrCategory);


        mDialog.setContentView(R.layout.dialog_list);

        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        if(this.mCategory.equals("type")){
            txvTitle.setText("Chọn loại nhà");
        }
        else if(this.mCategory.equals("architecture")){
            txvTitle.setText("Chọn kiểu nhà");
        }

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
                if(mCategory.equals("type")){
                    mTxvType.setText(mArrCategory.get(position).getName());
                    NewPostActivity.PRODUCT.setTypeId(mArrCategory.get(position).getId());
                    mDialog.dismiss();
                }
                else if(mCategory.equals("architecture")){
                    mTxvArchitecture.setText(mArrCategory.get(position).getName());
                    NewPostActivity.PRODUCT.setArchitectureId(mArrCategory.get(position).getId());
                    mDialog.dismiss();
                }
            }
        });

        mDialog.show();
    }

    @Override
    public void onInsertBlankPost(boolean status, int postId) {

    }

    @Override
    public void onUploadImages(boolean status) {

    }

    @Override
    public void onUpdateProductInformation(boolean status) {
        mLoadingDialog.dismiss();
        if(status){
            Toast.makeText(getContext(), "Lưu thông tin thành công!!!", Toast.LENGTH_SHORT).show();
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(2);
        }
        else{
            Toast.makeText(getContext(), "Có lỗi xảy ra trong việc lưu dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateDescriptionInformation(boolean status) {

    }

    @Override
    public void onUpdateMapInformation(boolean status) {

    }

    @Override
    public void onDeleteFile(boolean status) {

    }

    @Override
    public void onUpdateHandlePost(boolean status) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (v.getId()){
            case R.id.edt_address:
                EditText edt = (EditText) v;
                if(edt.getText().toString().length() >= 100){
                    if(keyCode >= 8 && keyCode <= 16 || keyCode >= 29 && keyCode <= 54){
                        return true;
                    }
                }
                break;
        }

        return false;
    }
}
