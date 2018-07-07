package com.qtctek.realstate.dto;

public class Product1 {

    private int mId;
    private String mLatitude = "";
    private String mLongitude = "";
    private String mCategoryProduct = "";
    private int mCategoryProductId = 0;
    private float mArea = 0;
    private int mBathrooms = 0;
    private int mBedrooms = 0;
    private String mDescription = "";
    private String mDistrict = "";
    private int mDistrictId = 0;
    private String mProvinceCity;
    private int mProvinceCityId = 0;
    private String mAddress = "";
    private Long mPrice = Long.parseLong(0 + "");
    private ProductDetail mProductDetail;

    public Product1() {
        this.mProductDetail = new ProductDetail();
    }

    public ProductDetail getProductDetail() {
        return mProductDetail;
    }

    public void setProductDetail(ProductDetail mProductDetail) {
        this.mProductDetail = mProductDetail;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getCategoryProduct() {
        return mCategoryProduct;
    }

    public void setCategoryProduct(String mCategoriesProduct) {
        this.mCategoryProduct = mCategoriesProduct;
    }

    public int getCategoryProductId() {
        return mCategoryProductId;
    }

    public void setCategoryProductId(int mCategoriesProductId) {
        this.mCategoryProductId = mCategoriesProductId;
    }

    public int getDistrictId() {
        return mDistrictId;
    }

    public void setDistrictId(int mDistrictId) {
        this.mDistrictId = mDistrictId;
    }

    public int getProvinceCityId() {
        return mProvinceCityId;
    }

    public void setProvinceCityId(int mProvinceCityId) {
        this.mProvinceCityId = mProvinceCityId;
    }

    public float getArea() {
        return mArea;
    }

    public void setArea(float mArea) {
        this.mArea = mArea;
    }

    public int getBathrooms() {
        return mBathrooms;
    }

    public void setBathrooms(int mBathrooms) {
        this.mBathrooms = mBathrooms;
    }

    public int getBedrooms() {
        return mBedrooms;
    }

    public void setBedrooms(int mBedrooms) {
        this.mBedrooms = mBedrooms;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public void setDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
    }

    public String getProvinceCity() {
        return mProvinceCity;
    }

    public void setProvinceCity(String mProvinceCity) {
        this.mProvinceCity = mProvinceCity;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long mPrice) {
        this.mPrice = mPrice;
    }

}
