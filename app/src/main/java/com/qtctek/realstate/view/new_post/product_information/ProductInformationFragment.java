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
import com.qtctek.realstate.dto.CategoriesProduct;
import com.qtctek.realstate.dto.District;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.ProvinceCity;
import com.qtctek.realstate.presenter.new_post.GetData.PresenterGetData;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelGetData;
import com.qtctek.realstate.presenter.new_post.PresenterNewPost;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelNewPost;
import com.qtctek.realstate.view.post_news.adapter.CategoriesProductAdapter;
import com.qtctek.realstate.view.post_news.adapter.DistrictAdapter;
import com.qtctek.realstate.view.post_news.adapter.ProvinceCityAdapter;
import com.qtctek.realstate.view.new_post.activity.NewPostActivity;

import java.util.ArrayList;

import static com.qtctek.realstate.view.new_post.activity.NewPostActivity.POST_SALE;

public class ProductInformationFragment extends Fragment implements View.OnClickListener, ViewHandleModelGetData,
        ViewHandleModelNewPost, View.OnKeyListener {

    private TextView mTxvTypeOfProduct;
    private TextView mTxvProvinceCity;
    private TextView mTxvDistrict;
    private EditText mEdtAddress;
    private EditText mEdtPrices;
    private EditText mEdtArea;
    private EditText mEdtBathrooms;
    private EditText mEdtBedrooms;
    private EditText mEdtFloors;
    private EditText mEdtLegal;
    private EditText mEdtUtility;
    private Button mBtnSaveNormalInformation;
    private Button mBtnSaveMoreInformation;
    private Button mBtnNextTo;
    private Dialog mDialog;

    private PresenterGetData mPresenterGetData;

    private View mView;
    private Dialog mLoadingDialog;

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
        this.mTxvTypeOfProduct = mView.findViewById(R.id.txv_type_product);
        this.mTxvProvinceCity = mView.findViewById(R.id.txv_province_city);
        this.mTxvDistrict = mView.findViewById(R.id.txv_district);
        this.mEdtAddress = mView.findViewById(R.id.edt_address);
        this.mEdtPrices = mView.findViewById(R.id.edt_price);
        this.mEdtBathrooms = mView.findViewById(R.id.edt_bathrooms);
        this.mEdtBedrooms = mView.findViewById(R.id.edt_bedrooms);
        this.mEdtArea = mView.findViewById(R.id.edt_area);
        this.mEdtFloors = mView.findViewById(R.id.edt_floors);
        this.mEdtLegal = mView.findViewById(R.id.edt_legal);
        this.mBtnSaveNormalInformation = mView.findViewById(R.id.btn_save_normal_information);
        this.mBtnSaveMoreInformation = mView.findViewById(R.id.btn_save_more_information);
        this.mBtnNextTo = mView.findViewById(R.id.btn_next_to);
        this.mEdtUtility = mView.findViewById(R.id.edt_utility);

        this.mBtnNextTo.setOnClickListener(this);
        this.mBtnSaveNormalInformation.setOnClickListener(this);
        this.mBtnSaveMoreInformation.setOnClickListener(this);
        this.mTxvTypeOfProduct.setOnClickListener(this);
        this.mTxvProvinceCity.setOnClickListener(this);
        this.mTxvDistrict.setOnClickListener(this);
        this.mEdtLegal.setOnKeyListener(this);
        this.mEdtUtility.setOnKeyListener(this);
        this.mEdtAddress.setOnKeyListener(this);
    }

    private void createLoadingDialog(){
        mLoadingDialog = new Dialog(getContext());
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        mLoadingDialog.setCancelable(false);
    }

    private void handleStart(){
        Product product = NewPostActivity.POST_SALE.getProduct();
        this.mTxvTypeOfProduct.setText(product.getCategoryProduct());
        this.mTxvProvinceCity.setText(product.getProvinceCity());
        this.mTxvDistrict.setText(product.getDistrict());
        this.mEdtAddress.setText(product.getAddress());
        this.mEdtBathrooms.setText(product.getBathrooms() + "");
        this.mEdtBedrooms.setText(product.getBedrooms() + "");
        this.mEdtPrices.setText(product.getPrice() + "");
        this.mEdtArea.setText(product.getArea() + "");

        this.mEdtFloors.setText(NewPostActivity.POST_SALE.getProduct().getProductDetail().getFloors() + "");
        this.mEdtLegal.setText(NewPostActivity.POST_SALE.getProduct().getProductDetail().getLegal());
        this.mEdtUtility.setText(product.getProductDetail().getUtility());
    }

    private void handleSaveNormalInformation(){
        if(NewPostActivity.POST_SALE.getProduct().getCategoryProductId() == 0){
            Toast.makeText(getContext(), "Bạn chưa chọn loại nhà. Không thể lưu!!!", Toast.LENGTH_SHORT).show();
        }
        else if(NewPostActivity.POST_SALE.getProduct().getProvinceCityId() == 0){
            Toast.makeText(getContext(), "Bạn chưa chọn tỉnh/thành phố. Không thể lưu!!!", Toast.LENGTH_SHORT).show();
        }
        else if(NewPostActivity.POST_SALE.getProduct().getDistrictId() == 0){
            Toast.makeText(getContext(), "Bạn chưa chọn quận/huyện. Không thể lưu!!!", Toast.LENGTH_SHORT).show();
        }
        else{

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
                POST_SALE.getProduct().setAddress(this.mEdtAddress.getText().toString());
                POST_SALE.getProduct().setBedrooms(Integer.parseInt(this.mEdtBedrooms.getText().toString().trim()));
                POST_SALE.getProduct().setBathrooms(Integer.parseInt(this.mEdtBathrooms.getText().toString().trim()));
                POST_SALE.getProduct().setPrice(Long.parseLong(this.mEdtPrices.getText().toString().trim()));
                POST_SALE.getProduct().setArea(Float.parseFloat(this.mEdtArea.getText().toString().trim()));

                mLoadingDialog.show();
                new PresenterNewPost(this).handleUpdateNormalInformation(POST_SALE.getProduct());
            }
            catch (Exception e){
                Toast.makeText(getContext(), "Có lỗi xảy ra trong quá trình lưu dữ liệu. Xin vui" + " lòng thử sau!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSaveMoreInformation(){

        mLoadingDialog.show();

        if(this.mEdtFloors.getText().toString().trim().equals("")){
            this.mEdtFloors.setText("0");
        }

        NewPostActivity.POST_SALE.getProduct().getProductDetail().setFloors(Integer.parseInt(this.mEdtFloors.getText().toString()));
        NewPostActivity.POST_SALE.getProduct().getProductDetail().setLegal(this.mEdtLegal.getText().toString().trim());
        NewPostActivity.POST_SALE.getProduct().getProductDetail().setUtility(this.mEdtUtility.getText().toString().trim());

        new PresenterNewPost(this).handleUpdateMoreInformation(NewPostActivity.POST_SALE.getProduct());

    }

    private void handleNext(){
        if(NewPostActivity.POST_SALE.getProduct().getCategoryProductId() == 0){
            Toast.makeText(getContext(), "Bạn chưa chọn loại nhà. Không thể tiếp tục!!!", Toast.LENGTH_SHORT).show();
        }
        else if(NewPostActivity.POST_SALE.getProduct().getProvinceCityId() == 0){
            Toast.makeText(getContext(), "Bạn chưa chọn tỉnh/thành phố. Không thể tiếp tục!!!", Toast.LENGTH_SHORT).show();
        }
        else if(NewPostActivity.POST_SALE.getProduct().getDistrictId() == 0){
            Toast.makeText(getContext(), "Bạn chưa chọn quận/huyện. Không thể tiếp tục!!!", Toast.LENGTH_SHORT).show();
        }
        else{
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txv_type_product:
                mLoadingDialog.show();
                mPresenterGetData.handleGetCategoriesProduct();
                break;
            case R.id.txv_province_city:
                mLoadingDialog.show();
                mPresenterGetData.handleGetProvinceCity();
                break;
            case R.id.txv_district:
                if(NewPostActivity.POST_SALE.getProduct().getProvinceCityId() == 0){
                    Toast.makeText(getContext(), "Chọn Tỉnh/Thành phố trước", Toast.LENGTH_SHORT).show();
                }
                else{
                    mLoadingDialog.show();
                    mPresenterGetData.handleGetDistrict(NewPostActivity.POST_SALE.getProduct().getProvinceCityId());
                }
                break;
            case R.id.btn_next_to:
                handleNext();
                break;
            case R.id.btn_save_normal_information:
                handleSaveNormalInformation();
                break;
            case R.id.btn_save_more_information:
                handleSaveMoreInformation();
                break;
        }
    }

    @Override
    public void onGetProvinceCity(boolean status, final ArrayList<ProvinceCity> mArr) {
        ProvinceCityAdapter provinceCityAdapter = new ProvinceCityAdapter( getContext(), mArr);

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

        mLoadingDialog.dismiss();
        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTxvProvinceCity.setText(mArr.get(position).getName());
                mDialog.dismiss();
                NewPostActivity.POST_SALE.getProduct().setProvinceCityId(mArr.get(position).getId());
                mTxvDistrict.setText("");
                NewPostActivity.POST_SALE.getProduct().setDistrictId(0);
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    @Override
    public void onGetDistrict(boolean status, final ArrayList<District> mArr) {

        mLoadingDialog.dismiss();

        DistrictAdapter district = new DistrictAdapter( getContext(), mArr);

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
                NewPostActivity.POST_SALE.getProduct().setDistrictId(mArr.get(position).getId());
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    @Override
    public void onGetCategoriesProduct(boolean status, final ArrayList<CategoriesProduct> mArr) {

        mLoadingDialog.dismiss();

        CategoriesProductAdapter categoriesProductAdapter = new CategoriesProductAdapter(
                getContext(), mArr);


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
                mTxvTypeOfProduct.setText(mArr.get(position).getName());
                NewPostActivity.POST_SALE.getProduct().setCategoryProductId(mArr.get(position).getId());
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
    public void onUpdateNormalInformation(boolean status) {
        mLoadingDialog.dismiss();
        if(!status){
            Toast.makeText(getContext(), "Có lỗi xảy ra trong quá trình lưu dữ liệu. Vui lòng thử lại sau!!!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "Lưu thông tin thành công!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateMoreInformation(boolean status) {
        mLoadingDialog.dismiss();
        if(status){
            Toast.makeText(getContext(), "Lưu thông tin thành công!!!", Toast.LENGTH_SHORT).show();
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
    public void onUpdateContactInformation(boolean status) {

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
            case R.id.edt_legal:
            case R.id.edt_utility:
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
