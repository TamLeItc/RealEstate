package com.qtctek.realstate.view.new_post.product_information.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qtctek.realstate.R;
import com.qtctek.realstate.common.general.Constant;
import com.qtctek.realstate.dto.Category;
import com.qtctek.realstate.dto.Place;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.new_post.GetData.PresenterGetData;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelGetData;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.new_post.product_information.adapter.AmenityAdapter;
import com.qtctek.realstate.view.new_post.product_information.adapter.CategoryAdapter;
import com.qtctek.realstate.view.new_post.product_information.adapter.PlaceAdapter;

import java.security.Key;
import java.util.ArrayList;

public class ProductInformationFragment extends Fragment implements View.OnClickListener, ViewHandleModelGetData,
        ViewHandleModelNewPost, View.OnKeyListener, CompoundButton.OnCheckedChangeListener {

    private EditText mEdtTitle;
    private TextView mTxvArchitecture;
    private TextView mTxvType;
    private TextView mTxvProvinceCity;
    private TextView mTxvDistrict;
    private EditText mEdtAddress;
    private EditText mEdtPrices;
    private EditText mEdtArea;
    private EditText mEdtBathrooms;
    private EditText mEdtBedrooms;
    private RadioButton mRdoForSale;
    private RadioButton mRdoRent;
    private TextView mTxvAMonth;
    private TextView mTxvAmenities;
    private Button mBtnNextTo;
    private Dialog mDialog;

    private PresenterGetData mPresenterGetData;

    private View mView;

    private String mCategory;
    public boolean isSaveTemp;
    private boolean isEdited = false;

    private ArrayList<Category> mArrCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_information, container, false);

        mDialog = new Dialog(getContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        initViews();
        handleStart();
        this.mPresenterGetData = new PresenterGetData(this);

        return mView;
    }

    private void initViews(){
        this.mTxvArchitecture = mView.findViewById(R.id.txv_architecture);
        this.mTxvType = mView.findViewById(R.id.txv_type);
        this.mTxvProvinceCity = mView.findViewById(R.id.txv_city);
        this.mTxvDistrict = mView.findViewById(R.id.txv_district);
        this.mTxvAmenities = mView.findViewById(R.id.txv_amenities);
        this.mEdtAddress = mView.findViewById(R.id.edt_address);
        this.mEdtPrices = mView.findViewById(R.id.edt_price);
        this.mEdtBathrooms = mView.findViewById(R.id.edt_bathrooms);
        this.mEdtBedrooms = mView.findViewById(R.id.edt_bedrooms);
        this.mEdtArea = mView.findViewById(R.id.edt_area);
        this.mBtnNextTo = mView.findViewById(R.id.btn_next_to);
        this.mRdoForSale = mView.findViewById(R.id.rdo_for_sale);
        this.mRdoRent = mView.findViewById(R.id.rdo_rent);
        this.mEdtTitle = mView.findViewById(R.id.edt_title);
        this.mTxvAMonth = mView.findViewById(R.id.txv_a_month);

        this.mBtnNextTo.setOnClickListener(this);
        this.mTxvArchitecture.setOnClickListener(this);
        this.mTxvType.setOnClickListener(this);
        this.mTxvAmenities.setOnClickListener(this);
        this.mTxvProvinceCity.setOnClickListener(this);
        this.mTxvDistrict.setOnClickListener(this);
        this.mEdtAddress.setOnKeyListener(this);
        this.mEdtPrices.setOnKeyListener(this);
        this.mEdtBathrooms.setOnKeyListener(this);
        this.mEdtBedrooms.setOnKeyListener(this);
        this.mEdtTitle.setOnKeyListener(this);
        this.mRdoForSale.setOnCheckedChangeListener(this);
    }


    private void handleStart(){
        this.mTxvArchitecture.setText(((NewPostActivity)getActivity()).product.getArchitecture());
        this.mTxvProvinceCity.setText(((NewPostActivity)getActivity()).product.getCity());
        this.mTxvDistrict.setText(((NewPostActivity)getActivity()).product.getDistrict());
        this.mEdtAddress.setText(((NewPostActivity)getActivity()).product.getAddress());
        this.mEdtBathrooms.setText(((NewPostActivity)getActivity()).product.getBathroom() + "");
        this.mEdtBedrooms.setText(((NewPostActivity)getActivity()).product.getBedroom() + "");
        this.mEdtPrices.setText(((NewPostActivity)getActivity()).product.getPrice() + "");
        this.mEdtArea.setText(((NewPostActivity)getActivity()).product.getArea() + "");
        this.mTxvAmenities.setText(((NewPostActivity)getActivity()).product.getAmenities());
        this.mTxvType.setText(((NewPostActivity)getActivity()).product.getType());
        this.mEdtTitle.setText(((NewPostActivity)getActivity()).product.getTitle());

        if(((NewPostActivity)getActivity()).product.getFormality().equals("yes")){
            mRdoForSale.setChecked(true);
        }
        else{
            mRdoRent.setChecked(true);
        }
        isEdited = false;
    }

    public boolean checkSavedInformation(){
        if(isEdited){
            return false;
        }
        else{
            return true;
        }
    }

    public void handleSaveProductInformation(){

        if(!isEdited){
            if(!isSaveTemp){
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(2);
            }
            return;
        }

        if(this.mEdtBathrooms.getText().toString().trim().equals("")){
            this.mEdtBathrooms.setText("0");
        }
        if(this.mEdtBedrooms.getText().toString().trim().equals("")){
            this.mEdtBedrooms.setText("0");
        }
        if(this.mEdtPrices.getText().toString().trim().equals("")){
            this.mEdtPrices.setText("0");
        }
        if(this.mTxvDistrict.getText().toString().trim().equals("")){
            ((NewPostActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.not_address_select_can_not_save), ToastHelper.LENGTH_SHORT);
            return;
        }

        try{
            ((NewPostActivity)getActivity()).product.setAddress(this.mEdtAddress.getText().toString().trim());
            ((NewPostActivity)getActivity()).product.setBathroom(Integer.parseInt(this.mEdtBathrooms.getText().toString().trim()));
            ((NewPostActivity)getActivity()).product.setBedroom(Integer.parseInt(this.mEdtBedrooms.getText().toString().trim()));
            ((NewPostActivity)getActivity()).product.setPrice(Long.valueOf(this.mEdtPrices.getText().toString().trim()));
            ((NewPostActivity)getActivity()).product.setArea(Float.parseFloat(this.mEdtArea.getText().toString().trim()));
            ((NewPostActivity)getActivity()).product.setAmenities(this.mTxvAmenities.getText().toString().trim());
            ((NewPostActivity)getActivity()).product.setTitle(this.mEdtTitle.getText().toString());

            if(mRdoForSale.isChecked()){
                ((NewPostActivity)getActivity()).product.setFormality("yes");
            }
            else{
                ((NewPostActivity)getActivity()).product.setFormality("no");
            }

            ((NewPostActivity)getActivity()).dialogHelper.show();
            new PresenterNewPost(this).handleUpdateProductInformation(((NewPostActivity)getActivity()).product);

        }
        catch (Exception e){
            ((NewPostActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.data_incorrect_format), ToastHelper.LENGTH_SHORT);
        }

    }

    @Override
    public void onGetCityList(boolean status, final ArrayList<Place> mArrCity) {
        PlaceAdapter placeAdapter = new PlaceAdapter( getContext(), mArrCity, ((NewPostActivity)getActivity()).product.getCityId());

        mDialog.setContentView(R.layout.dialog_list);

        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        txvTitle.setText(getActivity().getResources().getString(R.string.selected_district));

        final ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(placeAdapter);

        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTxvProvinceCity.setText(mArrCity.get(position).getName());
                ((NewPostActivity)getActivity()).product.setCityId(mArrCity.get(position).getId());

                ((NewPostActivity)getActivity()).product.setDistrictId(0);
                mTxvDistrict.setText("");
                isEdited = true;
                mDialog.dismiss();
            }
        });


        final int itemSelectedIndex = getLocationSelectedPlace(mArrCity, true) + 4;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lsv.smoothScrollToPosition(itemSelectedIndex);
            }
        }, 250);

        mDialog.show();
    }

    @Override
    public void onGetDistrictList(boolean status, final ArrayList<Place> mArrDistrict) {

        ((NewPostActivity)getActivity()).dialogHelper.dismiss();

        PlaceAdapter placeAdapter = new PlaceAdapter( getContext(), mArrDistrict, ((NewPostActivity)getActivity()).product.getDistrictId());

        mDialog.setContentView(R.layout.dialog_list);

        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        txvTitle.setText(getActivity().getResources().getString(R.string.selected_province_city));

        final ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(placeAdapter);

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
                mTxvDistrict.setText(mArrDistrict.get(position).getName());
                ((NewPostActivity)getActivity()).product.setDistrictId(mArrDistrict.get(position).getId());
                isEdited = true;
                mDialog.dismiss();
            }
        });

        final int itemSelectedIndex = getLocationSelectedPlace(mArrDistrict, false) + 4;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lsv.smoothScrollToPosition(itemSelectedIndex);
            }
        }, 250);


        mDialog.show();
    }
    private int getLocationSelectedPlace(ArrayList<Place> mArrPlace, boolean isCity){
        int location = 0;

        if(isCity){
            for (int i = 0; i < mArrPlace.size(); i++){
                if(((NewPostActivity)getActivity()).product.getCityId() == mArrPlace.get(i).getId()){
                    location = i;
                    break;
                }
            }
        }
        else{
            for (int i = 0; i < mArrPlace.size(); i++){
                if(((NewPostActivity)getActivity()).product.getDistrictId() == mArrPlace.get(i).getId()){
                    location = i;
                    break;
                }
            }
        }

        return location;
    }


    @Override
    public void onGetCategoriesProduct(boolean status, ArrayList<Category> arrCategory) {
        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        if(!status){
            ((NewPostActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.error_read_data_notification), ToastHelper.LENGTH_SHORT);
            return;
        }
        if(this.mCategory.equals(Product.AMENITIES)){
            handleDisplayCategoryAmenityProduct(arrCategory);
        }
        else{
            handleDisplayCategoryProduct(arrCategory);
        }

    }

    private void reloadAmenitiesSelected(){

        for(int i = 0; i < mArrCategory.size(); i++){
            mArrCategory.get(i).setIsSelected(false);
        }

        String[] temp = mTxvAmenities.getText().toString().split(",");
        for(int i = 0; i < mArrCategory.size(); i++){
            for(int j = 0; j < temp.length; j++){
                if(temp[j].trim().equals(mArrCategory.get(i).getName())){
                    mArrCategory.get(i).setIsSelected(true);
                }
            }
        }
    }

    private void handleDisplayCategoryAmenityProduct(final ArrayList<Category> arrCategory){

        this.mArrCategory = arrCategory;

        reloadAmenitiesSelected();

        AmenityAdapter amenityAdapter = new AmenityAdapter(getContext(), mArrCategory);

        mDialog.setContentView(R.layout.dialog_list);

        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        txvTitle.setText(getResources().getString(R.string.select_amenities));

        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(amenityAdapter);

        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadAmenitiesSelected();
                mDialog.dismiss();
            }
        });

        Button btnConfirm = mDialog.findViewById(R.id.btn_confirm);
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imvSelected = view.findViewById(R.id.imv_selected);
                if(imvSelected.getVisibility() == View.VISIBLE){
                    imvSelected.setVisibility(View.GONE);
                    mArrCategory.get(position).setIsSelected(false);
                }
                else{
                    mArrCategory.get(position).setIsSelected(true);
                    imvSelected.setVisibility(View.VISIBLE);
                }
            }
        });


        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String amenities = "";
                for(int i = 0; i < mArrCategory.size(); i++){
                    if(mArrCategory.get(i).getIsSelected()){
                        if(amenities.trim().equals("")){
                            amenities = mArrCategory.get(i).getName();
                        }
                        else{
                            amenities += ", " + mArrCategory.get(i).getName();
                        }
                    }
                }
                isEdited = true;
                mTxvAmenities.setText( amenities);
            }
        });

        mDialog.show();
    }

    private int getLocationSelectedCategoryProduct(ArrayList<Category> mArrCategory){
        int location = 0;
        for (int i = 0; i < mArrCategory.size(); i++){
            if(mArrCategory.get(i).getIsSelected()){
                location = i;
                break;
            }
        }
        return location;
    }

    private void handleDisplayCategoryProduct(final ArrayList<Category> mArrCategory){
        CategoryAdapter categoriesProductAdapter = null;

        mDialog.setContentView(R.layout.dialog_list);
        TextView txvTitle = mDialog.findViewById(R.id.txv_title);
        if(this.mCategory.equals(Product.TYPE)){
            txvTitle.setText(getResources().getString(R.string.select_type));
            categoriesProductAdapter = new CategoryAdapter(
                    getContext(), mArrCategory, ((NewPostActivity)getActivity()).product.getTypeId());
        }
        else if(this.mCategory.equals(Product.ARCHITECTURE)){
            txvTitle.setText(getResources().getString(R.string.select_architecture));
            categoriesProductAdapter = new CategoryAdapter(
                    getContext(), mArrCategory, ((NewPostActivity)getActivity()).product.getArchitectureId());
        }

        ListView lsv = mDialog.findViewById(R.id.lsv_item);
        lsv.setAdapter(categoriesProductAdapter);

        lsv.smoothScrollToPosition(getLocationSelectedCategoryProduct(mArrCategory));

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
                if(mCategory.equals(Product.TYPE)){
                    mTxvType.setText(mArrCategory.get(position).getName());
                    ((NewPostActivity)getActivity()).product.setTypeId(mArrCategory.get(position).getId());
                }
                else if(mCategory.equals(Product.ARCHITECTURE)){
                    mTxvArchitecture.setText(mArrCategory.get(position).getName());
                    ((NewPostActivity)getActivity()).product.setArchitectureId(mArrCategory.get(position).getId());
                }
                isEdited = true;
                mDialog.dismiss();
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
        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        if(status){
            isEdited = false;
            if(!isSaveTemp){
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(2);
            }
            else{
                ((NewPostActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.save_data_successful), ToastHelper.LENGTH_SHORT);
            }
        }
        else{
            ((NewPostActivity)getActivity()).toastHelper.toast(getResources().getString(R.string.error_save_data), ToastHelper.LENGTH_SHORT);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txv_type:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetCategoriesProduct(Product.TABLE_TYPE, Product.TYPE);
                this.mCategory = Product.TYPE;
                break;
            case R.id.txv_amenities:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetCategoriesProduct(Product.TABLE_AMENITIES, Product.AMENITY);
                this.mCategory = Product.AMENITIES;
                break;
            case R.id.txv_architecture:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetCategoriesProduct(Product.TABLE_ARCHITECTURE, Product.ARCHITECTURE);
                this.mCategory = Product.ARCHITECTURE;
                break;
            case R.id.txv_city:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetProvinceCity();
                break;
            case R.id.txv_district:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetDistrict(((NewPostActivity)getActivity()).product.getCityId());
                break;
            case R.id.btn_next_to:
                isSaveTemp = false;
                handleSaveProductInformation();
                break;
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()){
            case R.id.edt_address:
                EditText edt = (EditText) v;
                if(edt.getText().toString().length() >= 100){
                    if(keyCode >= 7 && keyCode <= 16 || keyCode >= 29 && keyCode <= 54){
                        return true;
                    }
                }
                break;
            case R.id.edt_title:
                EditText edt1 = (EditText) v;
                if(edt1.getText().toString().length() >= 200){
                    if(keyCode >= 7 && keyCode <= 16 || keyCode >= 29 && keyCode <= 54){
                        return true;
                    }
                }
                break;
        }
        this.isEdited = true;
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            this.mTxvAMonth.setVisibility(View.GONE);
        }
        else{
            this.mTxvAMonth.setVisibility(View.VISIBLE);
        }
        this.isEdited = true;
    }

    @Override
    public void onDestroyView() {

        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }
}
