package com.qtctek.realstate.presenter.new_post.GetData;

import com.qtctek.realstate.dto.Category;
import com.qtctek.realstate.dto.Place;
import com.qtctek.realstate.model.new_post.ModelGetData;
import com.qtctek.realstate.view.new_post.interfaces.ViewHandleModelGetData;

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
        mModelGetData.requireGetProvinceCity();
    }

    public void handleGetDistrict(int provinceCity){
        mModelGetData.requireGetDistrict(provinceCity);
    }

    public void handleGetCategoriesProduct(String table, String columnName){
        mModelGetData.requireGetCategoriesProduct(table, columnName);
    }

    @Override
    public void onGetProvinceCity(boolean status, String data) {
        if(status){
            ArrayList<Place> mArr = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Place provinceCity = new Place();
                    provinceCity.setId(jsonObject.getInt("id"));
                    provinceCity.setName(jsonObject.getString("name"));
                    provinceCity.setLatlng(jsonObject.getString("latlng"));
                    mArr.add(provinceCity);
                }

                this.mViewHandleModelGetData.onGetProvinceCity(true, mArr);

            } catch (JSONException e) {
                this.mViewHandleModelGetData.onGetProvinceCity(false, mArr);
                e.printStackTrace();
            }
        }
        else{
            this.mViewHandleModelGetData.onGetProvinceCity(false, null);
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
                    district.setLatlng(jsonObject.getString("latlng"));
                    mArr.add(district);
                }

                this.mViewHandleModelGetData.onGetDistrict(true, mArr);

            } catch (JSONException e) {
                this.mViewHandleModelGetData.onGetDistrict(false, mArr);
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
