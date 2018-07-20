package com.qtctek.realstate.dto;

public class Condition {

    private Double mMapLat;
    private Double mMapLng;
    private float mZoom;
    private String mMinPrice;
    private String mMaxPrice;
    private String mFormality;
    private String mType;
    private String mArchitecture;
    private int mBathroom;
    private int mBedroom;
    private String mName;

    public Condition() {
    }

    public Condition(Double mMapLat, Double mMapLng, float mZoom, String mMinPrice, String mMaxPrice,
                     String mFormality, String mType, String mArchitecture, int mBathroom,
                     int mBedroom, String mName) {
        this.mMapLat = mMapLat;
        this.mMapLng = mMapLng;
        this.mZoom = mZoom;
        this.mMinPrice = mMinPrice;
        this.mMaxPrice = mMaxPrice;
        this.mFormality = mFormality;
        this.mType = mType;
        this.mArchitecture = mArchitecture;
        this.mBathroom = mBathroom;
        this.mBedroom = mBedroom;
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Double getMapLat() {
        return mMapLat;
    }

    public void setMapLat(Double mMapLat) {
        this.mMapLat = mMapLat;
    }

    public Double getMapLng() {
        return mMapLng;
    }

    public void setMapLng(Double mMapLng) {
        this.mMapLng = mMapLng;
    }

    public float getZoom() {
        return mZoom;
    }

    public void setZoom(float mZoom) {
        this.mZoom = mZoom;
    }

    public String getMinPrice() {
        return mMinPrice;
    }

    public void setMinPrice(String mMinPrice) {
        this.mMinPrice = mMinPrice;
    }

    public String getMaxPrice() {
        return mMaxPrice;
    }

    public void setMaxPrice(String mMaxPrice) {
        this.mMaxPrice = mMaxPrice;
    }

    public String getFormality() {
        return mFormality;
    }

    public void setFormality(String mFormality) {
        this.mFormality = mFormality;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getArchitecture() {
        return mArchitecture;
    }

    public void setArchitecture(String mArchitecture) {
        this.mArchitecture = mArchitecture;
    }

    public int getBathroom() {
        return mBathroom;
    }

    public void setBathroom(int mBathroom) {
        this.mBathroom = mBathroom;
    }

    public int getBedroom() {
        return mBedroom;
    }

    public void setBedroom(int mBedroom) {
        this.mBedroom = mBedroom;
    }
}
