package com.qtctek.realstate.presenter.post_news;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.model.post_news.ModelPostNews;
import com.qtctek.realstate.view.post_news.interfaces.ViewHandlePostNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterPostNews implements PresenterImpHandlePostNews {

    private ViewHandlePostNews mViewHandlePostNews;

    private ModelPostNews mModelPostNews;

    public PresenterPostNews(ViewHandlePostNews viewHandlePostNews){
        this.mViewHandlePostNews = viewHandlePostNews;
        this.mModelPostNews = new ModelPostNews(this);
    }

    public void handleGetPostList(String option, int bathroom, int bedroom, String minPrice, String maxPrice, String formality, String architecture, String type,
                                  LatLng farRight, LatLng nearRight, LatLng farLeft, LatLng nearLeft){

        this.mModelPostNews.requireGetPostList(option, bathroom, bedroom, minPrice, maxPrice, formality, architecture, type, farRight, nearRight, farLeft, nearLeft);

    }

    private ArrayList<Product> handlePostList(String data) throws JSONException {
        ArrayList<Product> arrProduct = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(data);

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Product product = new Product();
            product.setId(jsonObject.getInt("id"));
            product.setFormality(jsonObject.getString("formality"));
            product.setTitle(jsonObject.getString("title"));
            product.setThumbnail(jsonObject.getString("thumbnail"));
            product.setPrice(jsonObject.getLong("price"));
            product.setDateUpload(jsonObject.getString("date_upload"));
            product.setArea((float) jsonObject.getDouble("area"));
            product.setBathroom(jsonObject.getInt("bathroom"));
            product.setBedroom(jsonObject.getInt("bedroom"));
            product.setStatus(jsonObject.getString("status"));
            product.setCity(jsonObject.getString("city"));
            product.setDistrict(jsonObject.getString("district"));
            product.setUserName(jsonObject.getString("login_username"));
            product.setUserId(jsonObject.getInt("id_login"));
            product.setUserFullName(jsonObject.getString("login_fullname"));
            product.setUserPhone(jsonObject.getString("login_phone"));
            product.setMapLat(jsonObject.getString("map_latitude"));
            product.setMapLng(jsonObject.getString("map_longitude"));
            product.setAddress(jsonObject.getString("address"));

            arrProduct.add(product);
        }

        return arrProduct;

    }

    @Override
    public void getPostListSuccessful(String data) {
        try {
            this.mViewHandlePostNews.handlePostListSuccessful(handlePostList(data));
        } catch (JSONException e) {
            this.mViewHandlePostNews.handlePostListError(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void getPostListError(String error) {
        this.mViewHandlePostNews.handlePostListError(error);
    }

    @Override
    public void getQualityPostSuccessful(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            int quality = jsonObject.getInt("message");

            mViewHandlePostNews.handleQualityPostSuccessful(quality);

        } catch (JSONException e) {
            this.mViewHandlePostNews.handleQualityPostError(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void getQualityPostError(String error) {
        this.mViewHandlePostNews.handleQualityPostError(error);
    }
}
