package com.qtctek.realstate.view.post_news.interfaces;

public interface OnEventFromActivity {

    void onDataSearch(String lat, String lng, String minPrice, String maxPrice, String categoryProduct);

    void onReloadMarker();

}
