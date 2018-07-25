package com.qtctek.realstate.view.new_post.product_information.fragment;

import android.app.Dialog;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

import com.qtctek.realstate.R;
import com.qtctek.realstate.dto.Category;
import com.qtctek.realstate.dto.Place;
import com.qtctek.realstate.helper.ToastHelper;
import com.qtctek.realstate.presenter.new_post.GetData.PresenterGetData;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelGetData;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;
import com.qtctek.realstate.view.new_post.product_information.adapter.AmenityAdapter;
import com.qtctek.realstate.view.new_post.product_information.adapter.CategoryAdapter;
import com.qtctek.realstate.view.new_post.product_information.adapter.PlaceAdapter;

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
    private Button mBtnSaveTemp;

    private PresenterGetData mPresenterGetData;

    private View mView;

    private String mCategory;

    private ArrayList<Category> mArrCategory;

    private boolean mIsSaveTemp;

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
        this.mBtnSaveTemp = mView.findViewById(R.id.btn_save_temp);
        this.mEdtTitle = mView.findViewById(R.id.edt_title);
        this.mTxvAMonth = mView.findViewById(R.id.txv_a_month);

        this.mBtnNextTo.setOnClickListener(this);
        this.mTxvArchitecture.setOnClickListener(this);
        this.mTxvType.setOnClickListener(this);
        this.mTxvAmenities.setOnClickListener(this);
        this.mTxvProvinceCity.setOnClickListener(this);
        this.mTxvDistrict.setOnClickListener(this);
        this.mEdtAddress.setOnKeyListener(this);
        this.mEdtTitle.setOnKeyListener(this);
        this.mBtnSaveTemp.setOnClickListener(this);
        this.mRdoForSale.setOnCheckedChangeListener(this);
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
        this.mEdtTitle.setText(NewPostActivity.PRODUCT.getTitle());

        if(NewPostActivity.PRODUCT.getFormality().equals("yes")){
            mRdoForSale.setChecked(true);
        }
        else{
            mRdoRent.setChecked(true);
        }

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
        if(this.mTxvDistrict.getText().toString().trim().equals("")){
            ((NewPostActivity)getActivity()).toastHelper.toast("Chưa chọn quận/huyện. Không thể lưu!!!", ToastHelper.LENGTH_SHORT);
            return;
        }

        try{
            NewPostActivity.PRODUCT.setAddress(this.mEdtAddress.getText().toString().trim());
            NewPostActivity.PRODUCT.setBathroom(Integer.parseInt(this.mEdtBathrooms.getText().toString().trim()));
            NewPostActivity.PRODUCT.setBedroom(Integer.parseInt(this.mEdtBedrooms.getText().toString().trim()));
            NewPostActivity.PRODUCT.setPrice(Long.valueOf(this.mEdtPrices.getText().toString().trim()));
            NewPostActivity.PRODUCT.setArea(Float.parseFloat(this.mEdtArea.getText().toString().trim()));
            NewPostActivity.PRODUCT.setAmenities(this.mTxvAmenities.getText().toString().trim());
            NewPostActivity.PRODUCT.setTitle(this.mEdtTitle.getText().toString());

            if(mRdoForSale.isChecked()){
                NewPostActivity.PRODUCT.setFormality("yes");
            }
            else{
                NewPostActivity.PRODUCT.setFormality("no");
            }

            ((NewPostActivity)getActivity()).dialogHelper.show();
            new PresenterNewPost(this).handleUpdateProductInformation(NewPostActivity.PRODUCT);

        }
        catch (Exception e){
            ((NewPostActivity)getActivity()).toastHelper.toast("Dữ liệu không chính xác. Xin vui kiểm tra lại!!!", ToastHelper.LENGTH_SHORT);
        }

    }

    @Override
    public void onGetCityList(boolean status, final ArrayList<Place> mArrCity) {
        PlaceAdapter placeAdapter = new PlaceAdapter( getContext(), mArrCity, NewPostActivity.PRODUCT.getCityId());

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
                NewPostActivity.PRODUCT.setCityId(mArrCity.get(position).getId());
                mTxvDistrict.setText("");
                NewPostActivity.PRODUCT.setDistrictId(0);

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

        PlaceAdapter placeAdapter = new PlaceAdapter( getContext(), mArrDistrict, NewPostActivity.PRODUCT.getDistrictId());

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
                NewPostActivity.PRODUCT.setDistrictId(mArrDistrict.get(position).getId());
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
                if(NewPostActivity.PRODUCT.getCityId() == mArrPlace.get(i).getId()){
                    location = i;
                    break;
                }
            }
        }
        else{
            for (int i = 0; i < mArrPlace.size(); i++){
                if(NewPostActivity.PRODUCT.getDistrictId() == mArrPlace.get(i).getId()){
                    location = i;
                    break;
                }
            }
        }
        Log.d("ttt", location + "");

        return location;
    }


    @Override
    public void onGetCategoriesProduct(boolean status, ArrayList<Category> arrCategory) {
        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        if(!status){
            ((NewPostActivity)getActivity()).toastHelper.toast("Đọc dữ liệu thất bại. Xin vui lòng thử lại sau!!!", ToastHelper.LENGTH_SHORT);
            return;
        }
        if(this.mCategory.equals("amenities")){
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
        txvTitle.setText("Chọn tiện ích");

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
        if(this.mCategory.equals("type")){
            txvTitle.setText("Chọn loại nhà");
            categoriesProductAdapter = new CategoryAdapter(
                    getContext(), mArrCategory, NewPostActivity.PRODUCT.getTypeId());
        }
        else if(this.mCategory.equals("architecture")){
            txvTitle.setText("Chọn kiểu nhà");
            categoriesProductAdapter = new CategoryAdapter(
                    getContext(), mArrCategory, NewPostActivity.PRODUCT.getArchitectureId());
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
        ((NewPostActivity)getActivity()).dialogHelper.dismiss();
        if(status){
            ((NewPostActivity)getActivity()).toastHelper.toast("Lưu thành công", ToastHelper.LENGTH_SHORT);
            if(!mIsSaveTemp){
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(2);
                ((NewPostActivity)getActivity()).setCurrentStateNumberProgressBar(
                        ((NewPostActivity) getActivity()).viewPaper.getCurrentItem());
            }
        }
        else{
            ((NewPostActivity)getActivity()).toastHelper.toast("Lỗi lưu dữ liệu!!!", ToastHelper.LENGTH_SHORT);
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
                mPresenterGetData.handleGetCategoriesProduct("tbl_type", "type");
                this.mCategory = "type";
                break;
            case R.id.txv_amenities:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetCategoriesProduct("tbl_amenities", "amenity");
                this.mCategory = "amenities";
                break;
            case R.id.txv_architecture:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetCategoriesProduct("tbl_architecture", "architecture");
                this.mCategory = "architecture";
                break;
            case R.id.txv_city:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetProvinceCity();
                break;
            case R.id.txv_district:
                ((NewPostActivity)getActivity()).dialogHelper.show();
                mPresenterGetData.handleGetDistrict(NewPostActivity.PRODUCT.getCityId());
                break;
            case R.id.btn_next_to:
                mIsSaveTemp = false;
                handleSaveProductInformation();
                break;
            case R.id.btn_save_temp:
                mIsSaveTemp = true;
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
                    if(keyCode >= 8 && keyCode <= 16 || keyCode >= 29 && keyCode <= 54){
                        return true;
                    }
                }
                break;
            case R.id.edt_title:
                EditText edt1 = (EditText) v;
                if(edt1.getText().toString().length() >= 200){
                    if(keyCode >= 8 && keyCode <= 16 || keyCode >= 29 && keyCode <= 54){
                        return true;
                    }
                }
                break;
        }

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
    }

    @Override
    public void onDestroyView() {

        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroyView();
    }
}
