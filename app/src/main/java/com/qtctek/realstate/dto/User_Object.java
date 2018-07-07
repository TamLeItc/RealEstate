package com.qtctek.realstate.dto;

public class User_Object {

    private int mIdUser;
    private String mName;
    private String mEmail;
    private String mPassword;
    private String mPhoneNumber;
    private String mRole;
    private int mQualityPostSale;
    private String mStatus;

    public User_Object(){}

    public User_Object(String mName, String mEmail, String mPassword, String mPhoneNumber) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mPhoneNumber = mPhoneNumber;
    }

    public int getIdUser() {
        return mIdUser;
    }

    public void setIdUser(int mIdUser) {
        this.mIdUser = mIdUser;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String mRole) {
        this.mRole = mRole;
    }

    public int getQualityPostSale() {
        return mQualityPostSale;
    }

    public void setQualityPostSale(int mQualityPostSale) {
        this.mQualityPostSale = mQualityPostSale;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
