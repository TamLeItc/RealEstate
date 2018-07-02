package com.qtctek.realstate.presenter.post_detail;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.ProductDetail;
import com.qtctek.realstate.model.post_detail.ModelPostDetail;
import com.qtctek.realstate.view.post_detail.interfaces.ViewHandlePostDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterPostDetail implements PresenterImpHandlePostDetail {

    private ViewHandlePostDetail mViewHandlePostDetail;
    private ModelPostDetail mModelPostDetail;

    public PresenterPostDetail(ViewHandlePostDetail viewHandlePostDetail){
        this.mViewHandlePostDetail = viewHandlePostDetail;
        this.mModelPostDetail = new ModelPostDetail(this);
    }

    public void handleGetDataProductDetail(int id){
        this.mModelPostDetail.requireGetProductDetail(id);
    }

    public void handleSavePost(int postSaleId, String emailUser){
        mModelPostDetail.requireInsertSavePost(postSaleId, emailUser);
    }

    public void handleData(String data) throws JSONException {

        PostSale postSale = new PostSale();
        ArrayList<String> arrImage = new ArrayList<>();


        JSONObject jsonObject = new JSONObject(data);

        JSONArray jsonArrayBasic = jsonObject.getJSONArray("basic");
        JSONObject jsonObjectBasic = jsonArrayBasic.getJSONObject(0);

        JSONArray jsonArrayDetail = jsonObject.getJSONArray("detail");
        JSONObject jsonObjectDetail = jsonArrayDetail.getJSONObject(0);

        postSale.setId(jsonObjectBasic.getInt("id"));
        postSale.setStatus(jsonObjectBasic.getString("status"));
        postSale.setContactName(jsonObjectBasic.getString("contact_name"));
        postSale.setContactNumberPhone(jsonObjectBasic.getString("contact_number_phone"));
        postSale.setPostDate(jsonObjectBasic.getString("post_date"));

        Product product = new Product();
        product.setId(postSale.getId());
        product.setLatitude(jsonObjectBasic.getString("latitude"));
        product.setLongitude(jsonObjectBasic.getString("longitude"));
        product.setCategoryProduct(jsonObjectBasic.getString("category_product"));
        product.setCategoryProductId(jsonObjectBasic.getInt("category_product_id"));
        product.setArea(Float.parseFloat(jsonObjectBasic.getString("area")));
        product.setBedrooms(jsonObjectBasic.getInt("bedrooms"));
        product.setBathrooms(jsonObjectBasic.getInt("bathrooms"));
        product.setDescription(jsonObjectBasic.getString("description"));
        product.setDistrict(jsonObjectBasic.getString("district"));
        product.setDistrictId(jsonObjectBasic.getInt("district_id"));
        product.setProvinceCity(jsonObjectBasic.getString("province_city"));
        product.setProvinceCityId(jsonObjectBasic.getInt("province_city_id"));
        product.setAddress(jsonObjectBasic.getString("address"));
        product.setPrice(Long.valueOf(jsonObjectBasic.getString("price")));

        ProductDetail productDetail = new ProductDetail();
        productDetail.setFloors(jsonObjectDetail.getInt("floors"));
        productDetail.setLegal(jsonObjectDetail.getString("legal"));
        productDetail.setUtility(jsonObjectDetail.getString("utility"));
        product.setProductDetail(productDetail);

        postSale.setProduct(product);

        JSONArray jsonArrayImage = jsonObject.getJSONArray("image");
        for(int i = 0; i < jsonArrayImage.length(); i++){
            JSONObject jsonObjectImage = jsonArrayImage.getJSONObject(i);
            arrImage.add(jsonObjectImage.getString("link_image"));
        }

        this.mViewHandlePostDetail.onHandleDataPostDetailSuccessful(postSale, arrImage);
    }

    @Override
    public void onGetDataPostDetailSuccessful(String data) {
        try {
            handleData(data);
        } catch (JSONException e) {
            this.mViewHandlePostDetail.onHandleDataPostDetailError(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onGetDataPostDetailError(String error) {
        this.mViewHandlePostDetail.onHandleDataPostDetailError(error);
    }

    @Override
    public void onInsertDataSavePost(String value) {
        mViewHandlePostDetail.onSavePost(value);
    }
}
