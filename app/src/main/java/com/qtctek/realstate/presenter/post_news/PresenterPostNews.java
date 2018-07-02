package com.qtctek.realstate.presenter.post_news;

import com.google.android.gms.maps.model.LatLng;
import com.qtctek.realstate.dto.PostSale;
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

    public void handleGetPostList(String minPrice, String maxPrice, String categoryProduct,
                                  LatLng farRight, LatLng nearRight, LatLng farLeft, LatLng nearLeft){

        this.mModelPostNews.requireGetPostList(minPrice, maxPrice, categoryProduct, farRight, nearRight, farLeft, nearLeft);

    }

    private ArrayList<PostSale> handlePostList(String data) throws JSONException {
        ArrayList<PostSale> arrListPost = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(data);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            PostSale postSale = new PostSale();
            postSale.setId(jsonObject.getInt("id"));
            postSale.setPostDate(jsonObject.getString("post_date"));

            Product product = new Product();
            product.setId(postSale.getId());
            product.setPrice(jsonObject.getLong("price"));
            product.setLatitude(jsonObject.getString("latitude"));
            product.setLongitude(jsonObject.getString("longitude"));
            product.setArea((float) jsonObject.getDouble("area"));
            product.setBathrooms(jsonObject.getInt("bathrooms"));
            product.setBedrooms(jsonObject.getInt("bedrooms"));
            product.setDistrict(jsonObject.getString("district"));
            product.setProvinceCity(jsonObject.getString("province_city"));
            product.setAddress(jsonObject.getString("address"));
            postSale.setProduct(product);

            arrListPost.add(postSale);

        }
        return arrListPost;

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
}
