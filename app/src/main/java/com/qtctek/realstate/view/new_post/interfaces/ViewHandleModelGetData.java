package com.qtctek.realstate.view.new_post.interfaces;

import com.qtctek.realstate.dto.CategoriesProduct;
import com.qtctek.realstate.dto.District;
import com.qtctek.realstate.dto.ProvinceCity;

import java.util.ArrayList;

public interface ViewHandleModelGetData {

    void onGetProvinceCity(boolean status, ArrayList<ProvinceCity> mArr);

    void onGetDistrict(boolean status, ArrayList<District> mArr);

    void onGetCategoriesProduct(boolean status, ArrayList<CategoriesProduct> mArr);

}
