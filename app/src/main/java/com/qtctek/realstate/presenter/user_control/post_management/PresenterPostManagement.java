package com.qtctek.realstate.presenter.user_control.post_management;

import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.model.user_control.ModelPostManagement;
import com.qtctek.realstate.view.user_control.post_management.ViewHandlePostManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterPostManagement implements PresenterImpHandlePostManagement {

    private ViewHandlePostManagement mViewHandlePostManagement;

    private ModelPostManagement mModelPostManagement;

    public PresenterPostManagement(ViewHandlePostManagement viewHandlePostManagement){
        this.mViewHandlePostManagement = viewHandlePostManagement;
        this.mModelPostManagement = new ModelPostManagement(this);
    }

    public void handleGetPostListForAdmin(int start, int limit, String formality, String status){
        this.mModelPostManagement.requirePostListForAdmin(start, limit, formality, status);
    }

    public void handleUpdateAcceptPost(int productId){
        this.mModelPostManagement.requireAcceptPost(productId);
    }
    public void handleDeletePost(int productId){
        this.mModelPostManagement.requireDeletePost(productId);
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
            product.setUserEmail(jsonObject.getString("login_email"));

            arrProduct.add(product);
        }

        return arrProduct;

    }

    @Override
    public void onGetPostListSuccessful(String data) {
        try {

            ArrayList<Product> arrProduct = handlePostList(data);
            this.mViewHandlePostManagement.onHandlePostListSuccessful(arrProduct);

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
