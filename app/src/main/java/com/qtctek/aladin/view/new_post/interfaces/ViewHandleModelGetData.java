package com.qtctek.aladin.view.new_post.interfaces;

import com.qtctek.aladin.dto.Category;
import com.qtctek.aladin.dto.Place;

import java.util.ArrayList;

public interface ViewHandleModelGetData {

    void onGetCityList(boolean status, ArrayList<Place> mArr);

    void onGetDistrictList(boolean status, ArrayList<Place> mArr);

    void onGetCategoriesProduct(boolean status, ArrayList<Category> mArr);

}
