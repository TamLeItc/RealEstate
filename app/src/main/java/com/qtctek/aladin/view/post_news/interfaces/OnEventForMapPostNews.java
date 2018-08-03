package com.qtctek.aladin.view.post_news.interfaces;

import com.google.android.gms.maps.model.LatLngBounds;
import com.qtctek.aladin.dto.Condition;

public interface OnEventForMapPostNews {

    void onHandleFilter();

    void onPlaceSelected(String address, LatLngBounds latlngs, Double lat, Double lng);

    void onChangeStatusSaveOfProduct(int position, boolean status);

    void onNearBySearchForSale();

    void onNearBySearchForRent();

    void onHandleSearch(Condition condition);

    void exitApp();

}
