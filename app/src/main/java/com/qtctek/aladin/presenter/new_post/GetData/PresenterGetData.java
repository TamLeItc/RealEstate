package com.qtctek.aladin.presenter.new_post.GetData;

import com.qtctek.aladin.dto.Category;
import com.qtctek.aladin.dto.Place;
import com.qtctek.aladin.model.new_post.ModelGetData;
import com.qtctek.aladin.view.new_post.interfaces.ViewHandleModelGetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterGetData implements PresenterImpHandleModelGetData {

    private ViewHandleModelGetData mViewHandleModelGetData;
    private ModelGetData mModelGetData;

    public PresenterGetData(ViewHandleModelGetData mViewHandleModelGetData){
        this.mViewHandleModelGetData = mViewHandleModelGetData;
        this.mModelGetData = new ModelGetData(this);
    }

    public void handleGetProvinceCity(){
        mModelGetData.requireGetListCity();
    }

    public void handleGetDistrict(int provinceCity){
        mModelGetData.requireGetListDistrict(provinceCity);
    }

    public void handleGetCategoriesProduct(String table, String columnName){
        mModelGetData.requireGetCategoriesProduct(table, columnName);
    }

    @Override
    public void onGetListCity(boolean status, String data) {
        if(status){
            ArrayList<Place> mArr = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Place place = new Place();
                    place.setId(jsonObject.getInt("id"));
                    place.setName(jsonObject.getString("name"));
                    place.setLatlng(jsonObject.getString("location"));
                    mArr.add(place);
                }

                this.mViewHandleModelGetData.onGetCityList(true, mArr);

            } catch (JSONException e) {
                this.mViewHandleModelGetData.onGetCityList(false, mArr);
                e.printStackTrace();
            }
        }
        else{
            this.mViewHandleModelGetData.onGetCityList(false, null);
        }
    }

    @Override
    public void onGetDistrict(boolean status, String data) {
        if(status){
            ArrayList<Place> mArr = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Place district = new Place();
                    district.setId(jsonObject.getInt("id"));
                    district.setName(jsonObject.getString("name"));
                    district.setLatlng(jsonObject.getString("location"));
                    mArr.add(district);
                }

                this.mViewHandleModelGetData.onGetDistrictList(true, mArr);

            } catch (JSONException e) {
                this.mViewHandleModelGetData.onGetDistrictList(false, mArr);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGetCategoriesProduct(boolean status, String data) {
        if(status){
            ArrayList<Category> mArr = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Category categoriesProduct = new Category();
                    categoriesProduct.setId(jsonObject.getInt("id"));
                    categoriesProduct.setName(jsonObject.getString("name"));
                    mArr.add(categoriesProduct);
                }

                this.mViewHandleModelGetData.onGetCategoriesProduct(true, mArr);

            } catch (JSONException e) {
                this.mViewHandleModelGetData.onGetCategoriesProduct(false, mArr);
                e.printStackTrace();
            }
        }
    }
}
