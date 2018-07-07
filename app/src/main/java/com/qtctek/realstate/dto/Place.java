package com.qtctek.realstate.dto;

public class Place {

    private int mId;
    private String mName;
    private String mLatlng;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getLatlng() {
        return mLatlng;
    }

    public void setLatlng(String mLatlng) {
        this.mLatlng = mLatlng;
    }

}
