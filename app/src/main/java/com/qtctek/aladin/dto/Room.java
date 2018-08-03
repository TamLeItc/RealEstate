package com.qtctek.aladin.dto;

public class Room {

    private int mQuality;
    private boolean mIsSelected = false;

    public Room(int mQuality, boolean mIsSelected) {
        this.mQuality = mQuality;
        this.mIsSelected = mIsSelected;
    }

    public int getQuality() {
        return mQuality;
    }

    public void setQuality(int mQuality) {
        this.mQuality = mQuality;
    }

    public boolean isIsSelected() {
        return mIsSelected;
    }

    public void setIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }
}
