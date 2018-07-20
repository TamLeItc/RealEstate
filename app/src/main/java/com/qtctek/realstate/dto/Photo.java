package com.qtctek.realstate.dto;

import android.net.Uri;

import java.io.File;

public class Photo {


    private int mId;
    private String mPhotoLink;
    private int mProductId;
    private Uri mUri;
    private boolean mIsUpload = false;

    public Photo(){}

    public Photo(Uri mUri) {
        this.mUri = mUri;
        this.mIsUpload = true;
    }

    public Photo(int mId, String mPhotoLink, int mProductId, Uri mFile, boolean mIsUpload) {
        this.mId = mId;
        this.mPhotoLink = mPhotoLink;
        this.mProductId = mProductId;
        this.mUri = mUri;
        this.mIsUpload = mIsUpload;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getPhotoLink() {
        return mPhotoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.mPhotoLink = photoLink;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int mProductId) {
        this.mProductId = mProductId;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    public boolean getIsUpload() {
        return mIsUpload;
    }

    public void setIsUpload(boolean mIsUpload) {
        this.mIsUpload = mIsUpload;
    }


}
