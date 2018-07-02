package com.qtctek.realstate.dto;

public class ProductDetail {

    private int mFloors = 0;
    private String mLegal = "";
    private String mUtility = "";

    public int getFloors() {
        return mFloors;
    }

    public void setFloors(int floors) {
        this.mFloors = floors;
    }

    public String getLegal() {
        return mLegal;
    }

    public void setLegal(String legal) {
        this.mLegal = legal;
    }

    public String getUtility() {
        return mUtility;
    }

    public void setUtility(String mUtility) {
        this.mUtility = mUtility;
    }
}
