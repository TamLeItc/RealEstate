package com.qtctek.realstate.presenter.user_control.posted_post;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.model.user_control.ModelPostedPost;
import com.qtctek.realstate.view.user_control.posted_post.ViewHandlePostedPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterPostedPost implements PresenterImpHandlePostedPost {

    private ViewHandlePostedPost mViewHandleUserControlPost;
    private ModelPostedPost mModelUserControlPost;

    private int mQualityPost;

    public PresenterPostedPost(ViewHandlePostedPost viewHandleUserControlPost){
        this.mViewHandleUserControlPost = viewHandleUserControlPost;
        this.mModelUserControlPost = new ModelPostedPost(this);
    }

    public void handleGetListPostedPost(String email, int start, int end){
        this.mModelUserControlPost.requirePostedPostList(email, start, end);
    }

    public void handleDeletePost(int postId){
        this.mModelUserControlPost.requireDeletePost(postId);
    }

    @Override
    public void onGetPostListSuccessful(String data) {

        try {

            ArrayList<PostSale> arrListPost = handlePostedPostData(data);
            this.mViewHandleUserControlPost.onHandlePostListSuccessful(this.mQualityPost, arrListPost);

        } catch (JSONException e) {
            this.mViewHandleUserControlPost.onHandlePostListError(e.toString());
            e.printStackTrace();
        }

    }

    private ArrayList<PostSale> handlePostedPostData(String data) throws JSONException {

        ArrayList<PostSale> postSales = new ArrayList<>();

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

            postSales.add(postSale);

        }
        return postSales;

    }

    @Override
    public void onGetPostListError(String error) {
        this.mViewHandleUserControlPost.onHandlePostListError(error);
    }

    @Override
    public void onDeletePostSuccessful() {
        this.mViewHandleUserControlPost.onDeletePostSuccessful();
    }

    @Override
    public void onDeletePostError(String error) {
        this.mViewHandleUserControlPost.onDeletePostError(error);
    }
}
