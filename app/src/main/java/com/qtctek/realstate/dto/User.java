package com.qtctek.realstate.dto;

public class User {

    private int mId;
    private String mFullName;
    private String mSex;
    private String mBirthDay;
    private String mPhone;
    private String mEmail;
    private String mAddress;
    private String mUsername;
    private String mPassword;
    private String mType;
    private String mStatus;
    private int mLevel;

    public User(){}

    public User(int mId, String mFullName, String mSex, String mBirthDay, String mPhone, String mEmail, String mAddress, String mUsername, String mPassword, String mType, String mStatus, int mLevel) {
        this.mId = mId;
        this.mFullName = mFullName;
        this.mSex = mSex;
        this.mBirthDay = mBirthDay;
        this.mPhone = mPhone;
        this.mEmail = mEmail;
        this.mAddress = mAddress;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.mType = mType;
        this.mStatus = mStatus;
        this.mLevel = mLevel;
    }

    public User(String mFullName, String mSex, String mBirthDay, String mPhone, String mEmail, String mAddress, String mUsername, String mPassword) {
        this.mFullName = mFullName;
        this.mSex = mSex;
        this.mBirthDay = mBirthDay;
        this.mPhone = mPhone;
        this.mEmail = mEmail;
        this.mAddress = mAddress;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.mType = mType;
        this.mStatus = mStatus;
        this.mLevel = mLevel;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String mSex) {
        this.mSex = mSex;
    }

    public String getBirthDay() {
        return mBirthDay;
    }

    public void setBirthDay(String mBirthDay) {
        this.mBirthDay = mBirthDay;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }
}
