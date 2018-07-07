package com.qtctek.realstate.view.new_post.interfaces;

import com.qtctek.realstate.dto.Category;
import com.qtctek.realstate.dto.Place;

import java.util.ArrayList;

public interface ViewHandleModelGetData {

    void onGetProvinceCity(boolean status, ArrayList<Place> mArr);

    void onGetDistrict(boolean status, ArrayList<Place> mArr);

    void onGetCategoriesProduct(boolean status, ArrayList<Category> mArr);

}
