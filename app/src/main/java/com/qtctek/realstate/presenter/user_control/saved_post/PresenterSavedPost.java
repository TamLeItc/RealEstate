package com.qtctek.realstate.presenter.user_control.saved_post;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.model.user_control.ModelSavedPost;
import com.qtctek.realstate.view.user_control.saved_post.ViewHandelSavedPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterSavedPost implements PresenterImpIHandleSavedPost {

    private ViewHandelSavedPost mViewHandelSavedPost;
    private ModelSavedPost mModelSavedPost;

    private int mQualityPost = 0;

    public PresenterSavedPost(ViewHandelSavedPost viewHandelSavedPost){
        this.mViewHandelSavedPost = viewHandelSavedPost;
    }

    public void handleGetSavedPostList(String email, int start, int quality){
        this.mModelSavedPost = new ModelSavedPost(this);
        this.mModelSavedPost.requireSavedPostList(email, start, quality);
    }

    public void handleUnSavePost(int id){
        this.mModelSavedPost.requireUnSavePost(id);
    }

    private ArrayList<PostSale> handleSavedPostList(String data) throws JSONException {
        ArrayList<PostSale> products = new ArrayList<>();

        JSONObject jsonObjectData = new JSONObject(data);
        this.mQualityPost = Integer.parseInt(jsonObjectData.getString("quality_post"));

        JSONArray jsonArray = jsonObjectData.getJSONArray("post_list");
        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            PostSale postSale = new PostSale();
            postSale.setId(jsonObject.getInt("id"));
            postSale.setStatus(jsonObject.getString("status"));

            Product product = new Product();
            product.setArea((float) jsonObject.getDouble("area"));
            product.setBathrooms(jsonObject.getInt("bathrooms"));
            product.setBedrooms(jsonObject.getInt("bedrooms"));
            product.setDistrict(jsonObject.getString("district"));
            product.setProvinceCity(jsonObject.getString("province_city"));
            product.setAddress(jsonObject.getString("address"));
            product.setPrice(jsonObject.getLong("price"));
            postSale.setProduct(product);

            products.add(postSale);

        }
        return products;

    }

    @Override
    public void onGetSavedPostListSuccessful(String data) {
        try {

            ArrayList<PostSale> arrListPost = handleSavedPostList(data);
            this.mViewHandelSavedPost.onHandleSavedPostListSuccessful(this.mQualityPost, arrListPost);

        } catch (JSONException e) {
            this.mViewHandelSavedPost.onHandleSavedPostListError(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleSavedPostListError(String error) {
        this.mViewHandelSavedPost.onHandleSavedPostListError(error);
    }

    @Override
    public void onExecuteUnSavePostSuccessful() {
        this.mViewHandelSavedPost.onHandleUnSavePostSuccessful();
    }

    @Override
    public void onExecuteUnSavePostError(String e) {
        this.mViewHandelSavedPost.onHandleUnSavePostError(e);
    }
}
