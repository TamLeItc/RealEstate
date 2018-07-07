package com.qtctek.realstate.dto;

public class PostSale {

    private int mId;
    private User_Object mUser;
    private Product1 mProduct;
    private String mContactName = "";
    private String mContactNumberPhone = "";
    private String mStatus = "Tạm lưu";
    private String mPostDate = "";


    public PostSale() {
        this.mUser = new User_Object();
        this.mProduct = new Product1();
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public User_Object getUser() {
        return mUser;
    }

    public void setUser(User_Object mUser) {
        this.mUser = mUser;
    }

    public Product1 getProduct() {
        return mProduct;
    }

    public void setProduct(Product1 mProduct) {
        this.mProduct = mProduct;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public String getContactNumberPhone() {
        return mContactNumberPhone;
    }

    public void setContactNumberPhone(String mContactNumberPhone) {
        this.mContactNumberPhone = mContactNumberPhone;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getPostDate() {
        return mPostDate;
    }

    public void setPostDate(String mPostDate) {
        this.mPostDate = mPostDate;
    }
}
