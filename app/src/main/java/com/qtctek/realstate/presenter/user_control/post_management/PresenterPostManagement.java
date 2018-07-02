package com.qtctek.realstate.presenter.user_control.post_management;

import com.qtctek.realstate.dto.PostSale;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.model.user_control.ModelPostManagement;
import com.qtctek.realstate.view.user_control.post_management.ViewHandlePostManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterPostManagement implements PresenterImpHandlePostManagement {

    private ViewHandlePostManagement mViewHandlePostManagement;

    private ModelPostManagement mModelPostManagement;

    private int mQualityPost = 0;

    public PresenterPostManagement(ViewHandlePostManagement viewHandlePostManagement){
        this.mViewHandlePostManagement = viewHandlePostManagement;
        this.mModelPostManagement = new ModelPostManagement(this);
    }

    public void handleGetPostListForAdmin(int start, int quality, int isNotActive){
        this.mModelPostManagement.requirePostListForAdmin(start, quality, isNotActive);
    }

    public void handleUpdateAcceptPost(int postId){
        this.mModelPostManagement.requireAcceptPost(postId);
    }
    public void handleDeletePost(int postId){
        this.mModelPostManagement.requireDeletePost(postId);
    }


    private ArrayList<PostSale> handlePostList(String data) throws JSONException {
        ArrayList<PostSale> arrListPost = new ArrayList<>();
        JSONObject jsonObjectData = new JSONObject(data);
        this.mQualityPost = Integer.parseInt(jsonObjectData.getString("quality_post"));

        JSONArray jsonArray = jsonObjectData.getJSONArray("post_list");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            PostSale postSale = new PostSale();
            postSale.setId(jsonObject.getInt("id"));
            postSale.setPostDate(jsonObject.getString("post_date"));
            postSale.setStatus(jsonObject.getString("status"));

            Product product = new Product();
            product.setPrice(jsonObject.getLong("price"));
            product.setArea((float) jsonObject.getDouble("area"));
            product.setDistrict(jsonObject.getString("district"));
            product.setProvinceCity(jsonObject.getString("province_city"));
            postSale.setProduct(product);

            User user = new User();
            user.setName(jsonObject.getString("name_poster"));
            user.setPhoneNumber(jsonObject.getString("phone_number_poster"));
            user.setEmail(jsonObject.getString("email_poster"));
            postSale.setUser(user);

            arrListPost.add(postSale);

        }

        return arrListPost;

    }

    @Override
    public void onGetPostListSuccessful(String data) {
        try {

            ArrayList<PostSale> handlePostList = handlePostList(data);
            this.mViewHandlePostManagement.onHandlePostListSuccessful(this.mQualityPost, handlePostList);

        } catch (JSONException e) {
            this.mViewHandlePostManagement.onHandlePostListError("error");
            e.printStackTrace();
        }
    }

    @Override
    public void onGetPostListError(String error) {
        this.mViewHandlePostManagement.onHandlePostListError("error");
    }

    @Override
    public void onAcceptPostSuccessful() {
        this.mViewHandlePostManagement.onAcceptPostSuccessful();
    }

    @Override
    public void onAcceptPostError(String error) {
        this.mViewHandlePostManagement.onAcceptPostError(error);
    }

    @Override
    public void onDeletePostSuccessful() {
        this.mViewHandlePostManagement.onDeletePostSuccessful();
    }

    @Override
    public void onDeletePostError(String error) {
        this.mViewHandlePostManagement.onDeletePostError(error);
    }
}
