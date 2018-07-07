package com.qtctek.realstate.presenter.post_detail;

import com.google.gson.JsonObject;
import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.Product1;
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

    public void handleGetDataProductDetail(int productId){
        this.mModelPostDetail.requireGetProductDetail(productId);
    }

    public void handleData(String data) throws JSONException {

        JSONObject jsonObject = new JSONObject(data);

        JSONObject jsonObjectProduct = jsonObject.getJSONObject("product");
        Product product = new Product();
        product.setId(jsonObjectProduct.getInt("id"));
        product.setFormality(jsonObjectProduct.getString("formality"));
        product.setTitle(jsonObjectProduct.getString("title"));
        product.setThumbnail(jsonObjectProduct.getString("thumbnail"));
        product.setPrice(jsonObjectProduct.getLong("price"));
        product.setDateUpload(jsonObjectProduct.getString("date_upload"));
        product.setArea((float) jsonObjectProduct.getDouble("area"));
        product.setBedroom(jsonObjectProduct.getInt("bedroom"));
        product.setBathroom(jsonObjectProduct.getInt("bathroom"));
        product.setDescription(jsonObjectProduct.getString("description"));
        product.setMapLat(jsonObjectProduct.getString("map_latitude"));
        product.setMapLng(jsonObjectProduct.getString("map_longitude"));
        product.setAmenities(jsonObjectProduct.getString("amenities"));
        product.setCity(jsonObjectProduct.getString("city"));
        product.setDistrict(jsonObjectProduct.getString("district"));
        product.setType(jsonObjectProduct.getString("type"));
        product.setArchitecture(jsonObjectProduct.getString("architecture"));
        product.setUserId(jsonObjectProduct.getInt("id_login"));
        product.setUserFullName(jsonObjectProduct.getString("login_fullname"));
        product.setUserPhone(jsonObjectProduct.getString("login_phone"));
        product.setUserEmail(jsonObjectProduct.getString("login_email"));

        JSONArray jsonArrayImage = jsonObject.getJSONArray("photo");
        ArrayList<String> arrImage = new ArrayList<>();
        for(int i = 0; i < jsonArrayImage.length(); i++){
            JSONObject jsonObjectImage = jsonArrayImage.getJSONObject(i);
            arrImage.add(jsonObjectImage.getString("photo_url"));
        }

        this.mViewHandlePostDetail.onHandleDataPostDetailSuccessful(product, arrImage);
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

}
