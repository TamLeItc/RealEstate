package com.qtctek.realstate.view.post_news.interfaces;

import com.google.android.gms.maps.model.LatLngBounds;
import com.qtctek.realstate.dto.Condition;

public interface OnEventForMapPostNews {

    void onDataFilter();

    void onPlaceSelected(String address, LatLngBounds latlngs, Double lat, Double lng);

    void onChangeStatusSaveOfProduct(int position, boolean status);

    void onNearBySearchForSale();

    void onNearBySearchForRent();

    void onHandleSearch(Condition condition);

}
