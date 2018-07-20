package com.qtctek.realstate.presenter.user_control.saved_post;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qtctek.realstate.dto.Product;
import com.qtctek.realstate.model.user_control.ModelSavedPost;
import com.qtctek.realstate.view.user_control.saved_post.ViewHandleSavedPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class PresenterSavedPost implements PresenterImpIHandleSavedPost {

    private ViewHandleSavedPost mViewHandelSavedPost;
    private ModelSavedPost mModelSavedPost;

    public PresenterSavedPost(ViewHandleSavedPost viewHandelSavedPost){
        this.mViewHandelSavedPost = viewHandelSavedPost;
        this.mModelSavedPost = new ModelSavedPost(this);
    }

    public void handleGetDataProductIds(Context context){
        this.mModelSavedPost.requireDataProductIds(context);
    }

    public void handleUpdateDataProductIds(HashMap<String, String> listProductId, Context context){

        Gson gson = new Gson();
        String data = gson.toJson(listProductId);

        this.mModelSavedPost.requireUpdateDataProductIds(data, context);
    }

    public void handleGetSavedProductList(int start, int limit, String listId){
        this.mModelSavedPost.requireSavedProductList(start, limit, listId);
    }

    @Override
    public void onGetDataProductIdsSuccessful(String data) {

        HashMap<String, String> listProductId = new HashMap<>();
        Gson gson = new Gson();
        if(!data.isEmpty()){
            Type type = new TypeToken<HashMap<String, String>>() {}.getType();
            listProductId = gson.fromJson(data, type);
        }
        this.mViewHandelSavedPost.onHandleDataProductIdsSuccessful(listProductId);
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
    public void onGetDataProductIdsError(String error) {
        this.mViewHandelSavedPost.onHandleDataProductIdsError(error);
    }

    @Override
    public void onUpdateProductIdListSuccessful() {
        this.mViewHandelSavedPost.onHandleUpdateProductIdListSuccessful();
    }

    @Override
    public void onUpdateProductIdListError(String e) {
        this.mViewHandelSavedPost.onHandleUpdateProductIdListError(e);
    }

    @Override
    public void onGetSavedProductListSuccessful(String data) {
        try {
            ArrayList<Product> mArrListProduct = handleData(data);
            mViewHandelSavedPost.onHandleSavedProductListSuccessful(mArrListProduct);
        } catch (JSONException e) {
            mViewHandelSavedPost.onHandleSavedProductListError(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onGetSavedProductListError(String error) {
        mViewHandelSavedPost.onHandleSavedProductListError(error);
    }
}
