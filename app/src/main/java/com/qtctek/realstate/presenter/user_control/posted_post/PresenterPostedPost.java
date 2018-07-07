package com.qtctek.realstate.presenter.user_control.posted_post;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.Product1;
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

    public void handleGetListPostedPost(int start, int limit, String email){
        this.mModelUserControlPost.requirePostedPostList(start, limit, email);
    }

    public void handleDeletePost(int productId){
        this.mModelUserControlPost.requireDeletePost(productId);
    }

    @Override
    public void onGetPostListSuccessful(String data) {

        try {

            ArrayList<Product> arrListPost = handleData(data);
            this.mViewHandleUserControlPost.onHandlePostListSuccessful(arrListPost);

        } catch (JSONException e) {
            this.mViewHandleUserControlPost.onHandlePostListError(e.toString());
            e.printStackTrace();
        }

    }

    private ArrayList<Product> handleData(String data) throws JSONException {

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

            arrProduct.add(product);
        }

        return arrProduct;

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
