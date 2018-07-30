package com.qtctek.realstate.dto;

public class Product {
    private int mId;
    private String mFormality;
    private String mTitle;
    private String mThumbnail;
    private Long mPrice;
    private String mDateUpload;
    private float mArea;
    private int mBathroom;
    private int mBedroom;
    private String mDescription;
    private String mMapLat;
    private String mMapLng;
    private String mAmenities;
    private String mAddress;
    private int mCityId;
    private int mDistrictId;
    private String mCity;
    private String mDistrict;
    private String mType;
    private int mTypeId;
    private String mArchitecture;
    private int mArchitectureId;
    private String mStatus;
    private int mUserId;
    private String mUserName;
    private String mUserFullName;
    private String mUserPhone;
    private String mUserEmail;
    private boolean mIsSaved = false;

    public Product(){
        this.mFormality = "yes";
        this.mTitle = "";
        this.mThumbnail = "";
        this.mPrice = Long.valueOf(0);
        this.mDateUpload = "";
        this.mArea = 0;
        this.mBathroom = 0;
        this.mBedroom = 0;
        this.mDescription = "";
        this.mMapLat = "";
        this.mMapLng = "";
        this.mAmenities = "";
        this.mCityId = 79;
        this.mCity = "";
        this.mDistrictId = 760;
        this.mDistrict = "";
        this.mType = "";
        this.mAmenities = "";
        this.mStatus = "1";
        this.mArchitecture = "";
        this.mArchitectureId = 1;
        this.mType = "";
        this.mTypeId = 1;
    }

    public Product(int mId, String mFormality, String mTitle, String mThumbnail, Long mPrice,
                   String mDateUpload, float area, int bathroom, int bedroom, String description,
                   String mMapLat, String mMapLng, String mAmenities, String address, int mCityId, int mDistrictId, String mCity, String mDistrict,
                   String mType, String mArchitecture, String mStatus, int mUserId, String mUserName,
                   String userFullName, String userPhone, String userEmail) {
        this.mId = mId;
        this.mFormality = mFormality;
        this.mTitle = mTitle;
        this.mThumbnail = mThumbnail;
        this.mPrice = mPrice;
        this.mDateUpload = mDateUpload;
        this.mArea = area;
        this.mBathroom = bathroom;
        this.mBedroom = bedroom;
        this.mDescription = description;
        this.mMapLat = mMapLat;
        this.mMapLng = mMapLng;
        this.mAmenities = mAmenities;
        this.mAddress = address;
        this.mCityId = mCityId;
        this.mDistrictId = mDistrictId;
        this.mCity = mCity;
        this.mDistrict = mDistrict;
        this.mType = mType;
        this.mArchitecture = mArchitecture;
        this.mStatus = mStatus;
        this.mUserId = mUserId;
        this.mUserName = mUserName;
        this.mUserFullName = userFullName;
        this.mUserPhone = userPhone;
        this.mUserEmail = userEmail;
    }

    public int getTypeId() {
        return mTypeId;
    }

    public void setTypeId(int mTypeId) {
        this.mTypeId = mTypeId;
    }

    public int getArchitectureId() {
        return mArchitectureId;
    }

    public void setArchitectureId(int mArchitectureId) {
        this.mArchitectureId = mArchitectureId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getFormality() {
        return mFormality;
    }

    public void setFormality(String mFormality) {
        this.mFormality = mFormality;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long mPrice) {
        this.mPrice = mPrice;
    }

    public String getDateUpload() {
        return mDateUpload;
    }

    public void setDateUpload(String mDateUpload) {
        this.mDateUpload = mDateUpload;
    }

    public float getArea() {
        return mArea;
    }

    public void setArea(float area) {
        this.mArea = area;
    }

    public int getBathroom() {
        return mBathroom;
    }

    public void setBathroom(int bathroom) {
        this.mBathroom = bathroom;
    }

    public int getBedroom() {
        return mBedroom;
    }

    public void setBedroom(int bedroom) {
        this.mBedroom = bedroom;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getMapLat() {
        return mMapLat;
    }

    public void setMapLat(String mMapLat) {
        this.mMapLat = mMapLat;
    }

    public String getMapLng() {
        return mMapLng;
    }

    public void setMapLng(String mMapLng) {
        this.mMapLng = mMapLng;
    }

    public String getAmenities() {
        return mAmenities;
    }

    public void setAmenities(String mAmenities) {
        this.mAmenities = mAmenities;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int mCityId) {
        this.mCityId = mCityId;
    }

    public int getDistrictId() {
        return mDistrictId;
    }

    public void setDistrictId(int mDistrictId) {
        this.mDistrictId = mDistrictId;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public void setDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
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

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserFullName() {
        return mUserFullName;
    }

    public void setUserFullName(String mUserFullName) {
        this.mUserFullName = mUserFullName;
    }

    public String getUserPhone() {
        return mUserPhone;
    }

    public void setUserPhone(String mUserPhone) {
        this.mUserPhone = mUserPhone;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public boolean getIsSaved() {
        return mIsSaved;
    }

    public void setIsSaved(boolean mIsSaved) {
        this.mIsSaved = mIsSaved;
    }
}
